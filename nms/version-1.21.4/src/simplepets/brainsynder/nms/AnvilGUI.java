package simplepets.brainsynder.nms;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.view.AnvilView;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class was modified from the original class by Sytm on github
 * The original class can be found here: https://github.com/Sytm/AnvilGUI
 *
 * The original class was modified to not use Paper as the base and use plain Spigot instead
 */
public final class AnvilGUI {

    /**
     * If the given ItemStack is null, return an empty ItemStack, otherwise return the given ItemStack
     *
     * @param stack The ItemStack to check
     * @return air or the given ItemStack
     */
    private static ItemStack copyItemNotNull(ItemStack stack) {
        return stack == null ? new ItemStack(Material.AIR) : stack.clone();
    }

    /**
     * The {@link Plugin} that this anvil GUI is associated with
     */
    private final Plugin plugin;
    /**
     * The player who has the GUI open
     */
    private final Player player;
    /**
     * The title of the anvil inventory
     */
    private final String title;
    /**
     * The initial contents of the inventory
     */
    private final ItemStack[] initialContents;
    /**
     * A state that decides where the anvil GUI is able to get closed by the user
     */
    private final boolean preventClose;

    /**
     * A set of slot numbers that are permitted to be interacted with by the user. An interactable
     * slot is one that is able to be minipulated by the player, i.e. clicking and picking up an item,
     * placing in a new one, etc.
     */
    private final Set<Integer> interactableSlots;

    /**
     * An {@link Consumer} that is called when the anvil GUI is close
     */
    private final Consumer<StateSnapshot> closeListener;
    /**
     * A flag that decides whether the async click handler can be run concurrently
     */
    private final boolean concurrentClickHandlerExecution;
    /**
     * An {@link BiFunction} that is called when a slot is clicked
     */
    private final ClickHandler clickHandler;

    private AnvilView view;
    /**
     * The inventory that is used on the Bukkit side of things
     */
    private AnvilInventory inventory;
    /**
     * The listener holder class
     */
    private final ListenUp listener = new ListenUp();

    /**
     * Represents the state of the inventory being open
     */
    private boolean open;

    /**
     * Create an AnvilGUI
     *
     * @param plugin                          A {@link org.bukkit.plugin.java.JavaPlugin} instance
     * @param player                          The {@link Player} to open the inventory for
     * @param title                           What to have the text already set to
     * @param initialContents                 The initial contents of the inventory
     * @param preventClose                    Whether to prevent the inventory from closing
     * @param closeListener                   A {@link Consumer} when the inventory closes
     * @param concurrentClickHandlerExecution Flag to allow concurrent execution of the click handler
     * @param clickHandler                    A {@link ClickHandler} that is called when the player clicks a slot
     */
    private AnvilGUI(
            Plugin plugin,
            Player player,
            String title,
            ItemStack[] initialContents,
            boolean preventClose,
            Set<Integer> interactableSlots,
            Consumer<StateSnapshot> closeListener,
            boolean concurrentClickHandlerExecution,
            ClickHandler clickHandler) {
        this.plugin = plugin;
        this.player = player;
        this.title = title;
        this.initialContents = initialContents;
        this.preventClose = preventClose;
        this.interactableSlots = interactableSlots;
        this.closeListener = closeListener;
        this.concurrentClickHandlerExecution = concurrentClickHandlerExecution;
        this.clickHandler = clickHandler;
    }

    /**
     * Opens the anvil GUI
     */
    private void openInventory() {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);

        view = MenuType.ANVIL.create(player, title);
        inventory = view.getTopInventory();
        player.openInventory(view);

        // We need to use setItem instead of setContents because a Minecraft ContainerAnvil
        // contains two separate inventories: the result inventory and the ingredients inventory.
        // The setContents method only updates the ingredients inventory unfortunately,
        // but setItem handles the index going into the result inventory.
        for (int i = 0; i < initialContents.length; i++) {
            inventory.setItem(i, initialContents[i]);
        }

        open = true;
    }

    /**
     * Closes the inventory if it's open.
     */
    public void closeInventory() {
        closeInventory0();
        player.closeInventory();
    }

    private void closeInventory0() {
        if (!open) {
            return;
        }

        open = false;

        final StateSnapshot state = StateSnapshot.fromAnvilGUI(this);

        inventory.clear(); // Prevent item drops

        HandlerList.unregisterAll(listener);

        if (closeListener != null) {
            closeListener.accept(state);
        }
    }

    /**
     * Returns the Bukkit inventory for this anvil gui
     *
     * @return the {@link Inventory} for this anvil gui
     */
    public AnvilInventory getInventory() {
        return inventory;
    }

    /**
     * Returns the entered text and replaces null text with empty text
     *
     * @return The entered text
     */
    public String getRenameText() {
        return Objects.requireNonNullElse(view.getRenameText(), "");
    }

    /**
     * Simply holds the listeners for the GUI
     */
    private final class ListenUp implements Listener {

        // Clear inventory before server shutdown
        @EventHandler
        public void onPluginDisable(PluginDisableEvent event) {
            if (event.getPlugin().equals(plugin)) {
                closeInventory();
            }
        }

        // Cant make this highest because then we might run after EcoEnchants
        @EventHandler(priority = EventPriority.HIGH)
        public void onPrepareAnvil(PrepareAnvilEvent event) {
            if (!event.getInventory().equals(inventory)) {
                return;
            }

            view.setRepairCost(0);

            ItemStack result = initialContents[Slot.OUTPUT];
            if (result != null) {
                event.setResult(result);
            }
            player.updateInventory(); // Awaiting Paper #9683
        }

        /**
         * Boolean storing the running status of the latest click handler to prevent double execution.
         * All accesses to this boolean will be from the main server thread, except for the rare event
         * that the plugin is disabled and the mainThreadExecutor throws an exception
         */
        private boolean clickHandlerRunning = false;

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if (!event.getInventory().equals(inventory)) {
                return;
            }

            final Player clicker = (Player) event.getWhoClicked();
            // prevent players from merging items from the anvil inventory
            final Inventory clickedInventory = event.getClickedInventory();
            if (clickedInventory != null
                    && clickedInventory.equals(clicker.getInventory())
                    && event.getClick().equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
                return;
            }

            // Blanket cancel move events that do not originate from the anvil, because the involved slots
            // in the anvil cannot be determined
            if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY
                    && !event.getClickedInventory().equals(inventory)) {
                event.setCancelled(true);
                return;
            }

            // Using rawSlot makes sure only the top inventory is handled
            final int rawSlot = event.getRawSlot();
            if (rawSlot >= Slot.INPUT_LEFT && rawSlot <= Slot.OUTPUT) {
                event.setCancelled(!interactableSlots.contains(rawSlot));
                if (clickHandlerRunning && !concurrentClickHandlerExecution) {
                    // A click handler is running, don't launch another one
                    return;
                }

                final CompletableFuture<List<ResponseAction>> actionsFuture =
                        clickHandler.apply(rawSlot, StateSnapshot.fromAnvilGUI(AnvilGUI.this));

                clickHandlerRunning = true;
                // If the plugin is disabled and the Executor throws an exception, the exception will be
                // passed to the .handle method
                actionsFuture
                        // Note: Original code used .thenAcceptAsync, but kept throwing an exception due to running PrepareAnvilEvent as async
                        .thenAccept(
                                actions -> {
                                    for (final ResponseAction action : actions) {
                                        action.accept(AnvilGUI.this, clicker);
                                    }
                                })
                        .handle((results, exception) -> {
                            if (exception != null) {
                                plugin.getLogger().severe("An exception occurred in the AnvilGUI clickHandler");
                                exception.printStackTrace();
                            }
                            // Whether an exception occurred or not, set running to false
                            clickHandlerRunning = false;
                            return null;
                        });
            }
        }

        @EventHandler
        public void onInventoryDrag(InventoryDragEvent event) {
            if (event.getInventory().equals(inventory)) {
                for (int slot : Slot.values) {
                    if (event.getRawSlots().contains(slot)) {
                        if (!interactableSlots.contains(slot)) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {
            if (open && event.getInventory().equals(inventory)) {
                closeInventory0();
            }
        }
    }

    /**
     * Creates a new builder instance
     *
     * @return A new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder class for an {@link AnvilGUI} object
     */
    public static final class Builder {

        /**
         * Create new Builder.
         * {@link #plugin(Plugin)} and {@link #onClick(BiFunction)} are required values.
         */
        public Builder() {}

        /**
         * An {@link Consumer} that is called when the anvil GUI is close
         */
        private Consumer<StateSnapshot> closeListener;
        /**
         * A flag that decides whether the async click handler can be run concurrently
         */
        private boolean concurrentClickHandlerExecution = false;
        /**
         * An {@link Function} that is called when a slot in the inventory has been clicked
         */
        private ClickHandler clickHandler;
        /**
         * A state that decides where the anvil GUI is able to be closed by the user
         */
        private boolean preventClose = false;
        /**
         * A set of integers containing the slot numbers that should be modifiable by the user.
         */
        private Set<Integer> interactableSlots = Collections.emptySet();
        /**
         * The {@link Plugin} that this anvil GUI is associated with
         */
        private Plugin plugin;
        /**
         * The text that will be displayed to the user
         */
        private String title = "";
        /**
         * The starting text on the item
         */
        private String itemText;
        /**
         * An {@link ItemStack} to be put in the left input slot
         */
        private ItemStack itemLeft;
        /**
         * An {@link ItemStack} to be put in the right input slot
         */
        private ItemStack itemRight;
        /**
         * An {@link ItemStack} to be placed in the output slot
         */
        private ItemStack itemOutput;

        /**
         * Prevents the closing of the anvil GUI by the user
         *
         * @return The {@link Builder} instance
         */
        public Builder preventClose() {
            preventClose = true;
            return this;
        }

        /**
         * Permit the user to modify (take items in and out) the slot numbers provided.
         *
         * @param slots A varags param for the slot numbers. You can avoid relying on magic constants by using
         *              the {@link AnvilGUI.Slot} class.
         * @return The {@link Builder} instance
         */
        public Builder interactableSlots(int... slots) {
            final Set<Integer> newValue = new HashSet<>();
            for (int slot : slots) {
                newValue.add(slot);
            }
            interactableSlots = newValue;
            return this;
        }

        /**
         * Listens for when the inventory is closed
         *
         * @param closeListener An {@link Consumer} that is called when the anvil GUI is closed
         * @return The {@link Builder} instance
         * @throws NullPointerException when the closeListener is null
         */
        public Builder onClose(Consumer<StateSnapshot> closeListener) {
            this.closeListener = Objects.requireNonNull(closeListener, "closeListener");
            return this;
        }

        /**
         * Do an action when a slot is clicked in the inventory
         * <p>
         * The ClickHandler is only called when the previous execution of the ClickHandler has finished.
         * To alter this behaviour use {@link #allowConcurrentClickHandlerExecution()}
         *
         * @param clickHandler A {@link ClickHandler} that is called when the user clicks a slot. The
         *                     {@link Integer} is the slot number corresponding to {@link Slot}, the
         *                     {@link StateSnapshot} contains information about the current state of the anvil,
         *                     and the response is a {@link CompletableFuture} that will eventually return a
         *                     list of {@link ResponseAction} to execute in the order that they are supplied.
         * @return The {@link Builder} instance
         * @throws NullPointerException when the function supplied is null
         */
        public Builder onClickAsync(ClickHandler clickHandler) {
            this.clickHandler = Objects.requireNonNull(clickHandler, "clickHandler");
            return this;
        }

        /**
         * By default, the {@link #onClickAsync(ClickHandler) async click handler} will not run concurrently
         * and instead wait for the previous {@link CompletableFuture} to finish before executing it again.
         * <p>
         * If this trait is desired, it can be enabled by calling this method but may lead to inconsistent
         * behaviour if not handled properly.
         *
         * @return The {@link Builder} instance
         */
        public Builder allowConcurrentClickHandlerExecution() {
            this.concurrentClickHandlerExecution = true;
            return this;
        }

        /**
         * Do an action when a slot is clicked in the inventory
         *
         * @param clickHandler A {@link BiFunction} that is called when the user clicks a slot. The
         *                     {@link Integer} is the slot number corresponding to {@link Slot}, the
         *                     {@link StateSnapshot} contains information about the current state of the anvil,
         *                     and the response is a list of {@link ResponseAction} to execute in the order
         *                     that they are supplied.
         * @return The {@link Builder} instance
         * @throws NullPointerException when the function supplied is null
         */
        public Builder onClick(
                BiFunction<
                        Integer,
                        StateSnapshot,
                        List<ResponseAction>>
                        clickHandler) {
            Objects.requireNonNull(clickHandler, "clickHandler");
            this.clickHandler = (slot, stateSnapshot) ->
                    CompletableFuture.completedFuture(clickHandler.apply(slot, stateSnapshot));
            return this;
        }

        /**
         * Sets the plugin for the {@link AnvilGUI}
         *
         * @param plugin The {@link Plugin} this anvil GUI is associated with
         * @return The {@link Builder} instance
         * @throws NullPointerException if the plugin is null
         */
        public Builder plugin(Plugin plugin) {
            this.plugin = Objects.requireNonNull(plugin, "plugin");
            return this;
        }

        /**
         * Sets the initial item-text that is displayed to the user.
         *
         * @param text The initial name of the item in the anvil
         * @return The {@link Builder} instance
         * @throws NullPointerException if the text is null
         */
        public Builder text(String text) {
            this.itemText = Objects.requireNonNull(text, "text");
            return this;
        }

        /**
         * Sets the AnvilGUI title that is to be displayed to the user.
         * <br>
         * The provided title will be treated as literal text.
         *
         * @param title The title that is to be displayed to the user
         * @return The {@link Builder} instance
         * @throws NullPointerException if the title is null
         */
        public Builder title(String title) {
            this.title = Objects.requireNonNull(title, "title");
            return this;
        }

        /**
         * Sets the {@link ItemStack} to be put in the first slot
         *
         * @param item The {@link ItemStack} to be put in the first slot
         * @return The {@link Builder} instance
         * @throws NullPointerException if the item is null
         */
        public Builder itemLeft(ItemStack item) {
            this.itemLeft = Objects.requireNonNull(item, "item").clone();
            return this;
        }

        /**
         * Sets the {@link ItemStack} to be put in the second slot
         *
         * @param item The {@link ItemStack} to be put in the second slot
         * @return The {@link Builder} instance
         * @throws NullPointerException if the item is null
         */
        public Builder itemRight(ItemStack item) {
            this.itemRight = Objects.requireNonNull(item, "item").clone();
            return this;
        }

        /**
         * Sets the {@link ItemStack} to be put in the output slot
         *
         * @param item The {@link ItemStack} to be put in the output slot
         * @return The {@link Builder} instance
         * @throws NullPointerException if the item is null
         */
        public Builder itemOutput(ItemStack item) {
            this.itemOutput = Objects.requireNonNull(item, "item").clone();
            return this;
        }

        /**
         * Creates the anvil GUI and opens it for the player
         *
         * @param player The {@link Player} the anvil GUI should open for
         * @return The {@link AnvilGUI} instance from this builder
         * @throws NullPointerException if the player is null
         * @throws NullPointerException when the clickHandler or plugin has not been set yet
         */
        public AnvilGUI open(Player player) {
            Objects.requireNonNull(plugin, "Plugin must be set");
            Objects.requireNonNull(clickHandler, "clickHandler must be set");
            Objects.requireNonNull(player, "player");

            if (itemText != null) {
                if (itemLeft == null) {
                    itemLeft = new ItemStack(Material.PAPER);
                }

                ItemMeta meta = itemLeft.getItemMeta();
                meta.setDisplayName(itemText);
                itemLeft.setItemMeta(meta);
            }

            final AnvilGUI anvilGUI = new AnvilGUI(
                    plugin,
                    player,
                    title,
                    new ItemStack[]{itemLeft, itemRight, itemOutput},
                    preventClose,
                    interactableSlots,
                    closeListener,
                    concurrentClickHandlerExecution,
                    clickHandler);
            anvilGUI.openInventory();
            return anvilGUI;
        }
    }

    /**
     * A handler that is called when the user clicks a slot. The
     * {@link Integer} is the slot number corresponding to {@link Slot}, the
     * {@link StateSnapshot} contains information about the current state of the anvil,
     * and the response is a {@link CompletableFuture} that will eventually return a
     * list of {@link ResponseAction} to execute in the order that they are supplied.
     */
    @FunctionalInterface
    public interface ClickHandler
            extends BiFunction<
            Integer,
            StateSnapshot,
            CompletableFuture<List<ResponseAction>>> {}

    /**
     * An action to run in response to a player clicking the output slot in the GUI. This interface is public
     * and permits you, the developer, to add additional response features easily to your custom AnvilGUIs.
     */
    @FunctionalInterface
    public interface ResponseAction extends BiConsumer<AnvilGUI, Player> {

        /**
         * Replace the input text box value with the provided text value.
         * <br>
         * Before using this method, it must be verified by the caller that items are either in
         * {@link Slot#INPUT_LEFT} or {@link Slot#OUTPUT} present.
         *
         * @param text The text to write in the input box
         * @return The {@link ResponseAction} to achieve the text replacement
         * @throws NullPointerException  when the text is null
         * @throws IllegalStateException when the slots {@link Slot#INPUT_LEFT} and {@link Slot#OUTPUT} are <code>null</code>
         */
        static ResponseAction replaceInputText(String text) {
            Objects.requireNonNull(text, "text");
            return (anvilgui, player) -> {
                ItemStack item = anvilgui.getInventory().getItem(Slot.OUTPUT);
                if (item == null) {
                    // Fallback on left input slot if player hasn't typed anything yet
                    item = anvilgui.getInventory().getItem(Slot.INPUT_LEFT);
                }
                if (item == null) {
                    throw new IllegalStateException(
                            "replaceInputText can only be used if slots OUTPUT or INPUT_LEFT are not empty");
                }

                final ItemStack cloned = item.clone();
                final ItemMeta meta = cloned.getItemMeta();
                meta.setDisplayName(text);
                cloned.setItemMeta(meta);
                anvilgui.getInventory().setItem(Slot.INPUT_LEFT, cloned);
            };
        }

        /**
         * Updates the title of the AnvilGUI to the new one.
         *
         * @param title              The new title to display
         * @param preserveRenameText Whether to preserve the entered rename text
         * @return The {@link ResponseAction} to achieve the title update
         * @throws NullPointerException when literalTitle is null
         * @see Builder#title(String)
         */
        static ResponseAction updateTitle(
                String title, boolean preserveRenameText) {
            Objects.requireNonNull(title, "title");
            return (anvilGUI, player) -> {
                final StateSnapshot oldState = StateSnapshot.fromAnvilGUI(anvilGUI);
                anvilGUI.inventory.clear();

                anvilGUI.view = MenuType.ANVIL.create(player, title);
                AnvilInventory inventory = anvilGUI.view.getTopInventory();
                anvilGUI.inventory = inventory;

                if (preserveRenameText) {
                    ItemStack firstItem = oldState.leftItem();
                    ItemBuilder builder = ItemBuilder.fromItem(firstItem);
                    if ((firstItem != null) && (firstItem.getType() != Material.AIR)) {
                        builder.withName(oldState.text);
                        inventory.setItem(Slot.INPUT_LEFT, builder.build());
                    }
                } else {
                    inventory.setItem(Slot.INPUT_LEFT, oldState.leftItem());
                }
                inventory.setItem(Slot.INPUT_RIGHT, oldState.rightItem());
                inventory.setItem(Slot.OUTPUT, oldState.outputItem());

                player.openInventory(anvilGUI.view);
            };
        }

        /**
         * Open another inventory
         *
         * @param otherInventory The inventory to open
         * @return The {@link ResponseAction} to achieve the inventory open
         * @throws NullPointerException when the otherInventory is null
         */
        static ResponseAction openInventory(Inventory otherInventory) {
            Objects.requireNonNull(otherInventory, "otherInventory");
            return (anvilgui, player) -> player.openInventory(otherInventory);
        }

        /**
         * Close the AnvilGUI
         *
         * @return The {@link ResponseAction} to achieve closing the AnvilGUI
         */
        static ResponseAction close() {
            return (anvilgui, player) -> anvilgui.closeInventory();
        }

        /**
         * Run the provided runnable
         *
         * @param runnable The runnable to run
         * @return The {@link ResponseAction} to achieve running the runnable
         * @throws NullPointerException when the runnable is null
         */
        static ResponseAction run(Runnable runnable) {
            Objects.requireNonNull(runnable, "runnable");
            return (anvilgui, player) -> runnable.run();
        }
    }

    /**
     * Class wrapping the magic constants of slot numbers in an anvil GUI
     */
    public static final class Slot {

        private Slot() {}

        private static final int[] values = new int[]{Slot.INPUT_LEFT, Slot.INPUT_RIGHT, Slot.OUTPUT};

        /**
         * The slot on the far left, where the first input is inserted. An {@link ItemStack} is always inserted
         * here to be renamed
         */
        public static final int INPUT_LEFT = 0;
        /**
         * Not used, but in a real anvil you are able to put the second item you want to combine here
         */
        public static final int INPUT_RIGHT = 1;
        /**
         * The output slot, where an item is put when two items are combined from {@link #INPUT_LEFT} and
         * {@link #INPUT_RIGHT} or {@link #INPUT_LEFT} is renamed
         */
        public static final int OUTPUT = 2;

        /**
         * Get all anvil slot values
         *
         * @return The array containing all possible anvil slots
         */
        public static int[] values() {
            return values.clone();
        }
    }

    /**
     * The event parameter constructor
     *
     * @param text       The text that has been entered into the anvil
     * @param leftItem   The left item in the combine slot of the anvilGUI
     * @param rightItem  The right item in the combine slot of the anvilGUI
     * @param outputItem The item that would have been outputted, when the items would have been combined
     * @param player     The player that clicked the output slot
     */
    public record StateSnapshot(
            String text,
            ItemStack leftItem,
            ItemStack rightItem,
            ItemStack outputItem,
            Player player) {

        /**
         * Create an {@link StateSnapshot} from the current state of an {@link AnvilGUI}
         *
         * @param anvilGUI The instance to take the snapshot of
         * @return The snapshot
         */
        private static StateSnapshot fromAnvilGUI(AnvilGUI anvilGUI) {
            final AnvilInventory inventory = anvilGUI.getInventory();
            return new StateSnapshot(
                    anvilGUI.getRenameText(),
                    copyItemNotNull(inventory.getItem(Slot.INPUT_LEFT)),
                    copyItemNotNull(inventory.getItem(Slot.INPUT_RIGHT)),
                    copyItemNotNull(inventory.getItem(Slot.OUTPUT)),
                    anvilGUI.player);
        }
    }
}