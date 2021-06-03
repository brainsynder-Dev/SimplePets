package simplepets.brainsynder.api.pet.data;

import com.google.common.collect.Lists;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.json.JsonObject;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.misc.ISizable;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.plugin.SimplePets;

import java.util.LinkedList;
import java.util.Optional;

@Namespace(namespace = "size")
public class SizeData extends PetData<ISizable> {
    public SizeData() {
        addDefaultItem("1", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &#e1eb5b1")
                .setTexture("http://textures.minecraft.net/texture/ca516fbae16058f251aef9a68d3078549f48f6d5b683f19cf5a1745217d72cc"));
        addDefaultItem("2", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &#e1eb5b2")
                .setTexture("http://textures.minecraft.net/texture/4698add39cf9e4ea92d42fadefdec3be8a7dafa11fb359de752e9f54aecedc9a"));
        addDefaultItem("3", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &#e1eb5b3")
                .setTexture("http://textures.minecraft.net/texture/fd9e4cd5e1b9f3c8d6ca5a1bf45d86edd1d51e535dbf855fe9d2f5d4cffcd2"));
        addDefaultItem("4", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &#e1eb5b4")
                .setTexture("http://textures.minecraft.net/texture/f2a3d53898141c58d5acbcfc87469a87d48c5c1fc82fb4e72f7015a3648058"));
    }

    @Override
    public Object getDefaultValue() {
        return 1;
    }

    @Override
    public Optional<ItemBuilder> getItem(ISizable entity) {
        Optional<ItemBuilder> optional = super.getItem(entity);
        if (!optional.isPresent()) {
            if (!getSizes(entity).contains(entity.getSize()))
                // Placeholder in case the pets data was modified and there is no item for that size
                return Optional.of(new ItemBuilder(Material.PLAYER_HEAD)
                        .withName("&#c8c8c8Size: &#e1eb5b?")
                        .setTexture("http://textures.minecraft.net/texture/bc8ea1f51f253ff5142ca11ae45193a4ad8c3ab5e9c6eec8ba7a4fcb7bac40"));
        }

        return optional;
    }

    private LinkedList<Integer> getSizes(ISizable entity) {
        LinkedList<Integer> sizes = new LinkedList<>();

        Optional<IPetConfig> optional = SimplePets.getPetConfigManager().getPetConfig(entity.getPetType());
        Namespace namespace = getNamespace();
        if (optional.isPresent()) {
            IPetConfig config = optional.get();
            JsonObject json = config.getRawData(namespace.namespace());

            json.names().forEach(string -> {
                try {
                    int size = Integer.parseInt(string);
                    sizes.add(size);
                } catch (NumberFormatException ignored) {}
            });
        }

        // Adds all the default sizes
        if (sizes.isEmpty()) sizes.addAll(Lists.newArrayList(1, 2, 3, 4));

        return sizes;
    }

    @Override
    public void onLeftClick(ISizable entity) {
        Navigating navigating = new Navigating(getSizes(entity));
        entity.setSize(navigating.getNext(navigating.getIndex(entity.getSize())));
    }

    @Override
    public void onRightClick(ISizable entity) {
        Navigating navigating = new Navigating(getSizes(entity));
        entity.setSize(navigating.getPrevious(navigating.getIndex(entity.getSize())));
    }

    @Override
    public Object value(ISizable entity) {
        return entity.getSize();
    }


    static class Navigating {
        private final LinkedList<Integer> list;

        Navigating(LinkedList<Integer> list) {this.list = list;}

        /**
         * Gets the current index based on the {@param currentNumber}
         *
         * @param currentNumber - A number in the list
         * @return - Will return an index of 0 if its not found
         */
        int getIndex(int currentNumber) {
            int index = 0;
            for (int num : list) {
                if (num == currentNumber) break;
                index++;
            }
            return index;
        }

        /**
         * Gets the next number from the list
         */
        int getNext(int index) {
            if (list.isEmpty()) return 1; // Has no values return 1 (since that is a default number)

            // Just small checks before actual code
            if (index < 0) index = 0;

            if (index >= (list.size() - 1)) return list.get(0);
            return list.get(++index);
        }

        /**
         * Gets the previous number from the list
         */
        int getPrevious(int index) {
            if (list.isEmpty()) return 4; // Has no values return 4 (since that is a default number)

            // Just small checks before actual code
            if (index >= list.size()) index = (list.size() - 1);

            if (index <= 0) return list.get((list.size() - 1));
            return list.get(--index);
        }
    }
}
