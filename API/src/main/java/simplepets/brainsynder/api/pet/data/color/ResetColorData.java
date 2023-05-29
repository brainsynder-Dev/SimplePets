package simplepets.brainsynder.api.pet.data.color;

import lib.brainsynder.apache.WordUtils;
import lib.brainsynder.item.ItemBuilder;
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
    public Object getDefaultValue() {
        return ColorWrapper.NONE;
    }

    @Override
    public Optional<ItemBuilder> getItem(IResetColor entity) {
        Optional<ItemBuilder> optional = super.getItem(entity);
        if (optional.isPresent()) {
            // We have to do this to replace the placholders if there is any

            ColorWrapper previous = ColorWrapper.getPrevious(entity.getColorWrapper());
            ColorWrapper next = ColorWrapper.getNext(entity.getColorWrapper());

            ItemBuilder builder = optional.get();
            builder.replaceString("{previousColor}", previous.getChatColor())
                    .replaceString("{currentColor}", entity.getColorWrapper().getChatColor())
                    .replaceString("{nextColor}", next.getChatColor())
                    .replaceString("{previousName}", WordUtils.capitalize(previous.name().toLowerCase().replace("_", " ")))
                    .replaceString("{currentName}", WordUtils.capitalize(entity.getColorWrapper().name().toLowerCase().replace("_", " ")))
                    .replaceString("{nextName}", WordUtils.capitalize(next.name().toLowerCase().replace("_", " ")));
            return Optional.of(builder);
        }
        return optional;
    }

    @Override
    public void onLeftClick(IResetColor entity) {
        entity.setColorWrapper(ColorWrapper.getNext(entity.getColorWrapper()));
    }

    @Override
    public void onRightClick(IResetColor entity) {
        entity.setColorWrapper(ColorWrapper.getPrevious(entity.getColorWrapper()));
    }

    @Override
    public Object value(IResetColor entity) {
        return entity.getColorWrapper();
    }
}
