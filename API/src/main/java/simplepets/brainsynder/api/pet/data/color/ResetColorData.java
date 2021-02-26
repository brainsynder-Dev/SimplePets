package simplepets.brainsynder.api.pet.data.color;

import lib.brainsynder.item.ItemBuilder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.misc.IResetColor;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.ColorWrapper;

import java.util.Optional;

@Namespace(namespace = "color")
public class ResetColorData extends PetData<IResetColor> {
    public ResetColorData() {
        for (ColorWrapper color : ColorWrapper.values()) {
            addDefaultItem(color.name(), new ItemBuilder(Material.PLAYER_HEAD).setTexture(color.getTexture())
                    .withName(" ")
                    .addLore(
                            "&#c8c8c8Previous: {previousColor}{previousName}",
                            "&#c8c8c8Current: {currentColor}{currentName}",
                            "&#c8c8c8Next: {nextColor}{nextName}"));
        }
    }

    @Override
    public Optional<ItemBuilder> getItem(IResetColor entity) {
        Optional<ItemBuilder> optional = super.getItem(entity);
        if (optional.isPresent()) {
            // We have to do this to replace the placholders if there is any

            ColorWrapper previous = ColorWrapper.getPrevious(entity.getColor());
            ColorWrapper next = ColorWrapper.getNext(entity.getColor());

            ItemBuilder builder = optional.get();
            builder.replaceInLore("{previousColor}", previous.getChatColor())
                    .replaceInLore("{currentColor}", entity.getColor().getChatColor())
                    .replaceInLore("{nextColor}", next.getChatColor())
                    .replaceInLore("{previousName}", WordUtils.capitalize(previous.name().toLowerCase().replace("_", " ")))
                    .replaceInLore("{currentName}", WordUtils.capitalize(entity.getColor().name().toLowerCase().replace("_", " ")))
                    .replaceInLore("{nextName}", WordUtils.capitalize(next.name().toLowerCase().replace("_", " ")));

            builder.replaceInName("{previousColor}", previous.getChatColor())
                    .replaceInName("{currentColor}", entity.getColor().getChatColor())
                    .replaceInName("{nextColor}", next.getChatColor())
                    .replaceInName("{previousName}", WordUtils.capitalize(previous.name().toLowerCase().replace("_", " ")))
                    .replaceInName("{currentName}", WordUtils.capitalize(entity.getColor().name().toLowerCase().replace("_", " ")))
                    .replaceInName("{nextName}", WordUtils.capitalize(next.name().toLowerCase().replace("_", " ")));
            return Optional.of(builder);
        }
        return optional;
    }

    @Override
    public void onLeftClick(IResetColor entity) {
        entity.setColor(ColorWrapper.getNext(entity.getColor()));
    }

    @Override
    public void onRightClick(IResetColor entity) {
        entity.setColor(ColorWrapper.getPrevious(entity.getColor()));
    }

    @Override
    public Object value(IResetColor entity) {
        return entity.getColor();
    }
}
