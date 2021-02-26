package simplepets.brainsynder.api.pet.data.color;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.nms.DataConverter;
import lib.brainsynder.utils.DyeColorWrapper;
import org.apache.commons.lang.WordUtils;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.misc.IColorable;
import simplepets.brainsynder.api.pet.PetData;

import java.util.Optional;

@Namespace(namespace = "color")
public class ColorData extends PetData<IColorable> {
    public ColorData() {
        for (DyeColorWrapper color : DyeColorWrapper.values()) {
            addDefaultItem(color.name(), DataConverter.getColoredMaterial(DataConverter.MaterialType.CONCRETE, color)
                    .withName(" ")
                    .addLore(
                            "&#c8c8c8Previous: {previousColor}{previousName}",
                            "&#c8c8c8Current: {currentColor}{currentName}",
                            "&#c8c8c8Next: {nextColor}{nextName}"));
        }
    }

    @Override
    public Optional<ItemBuilder> getItem(IColorable entity) {
        Optional<ItemBuilder> optional = super.getItem(entity);
        if (optional.isPresent()) {
            // We have to do this to replace the placholders if there is any

            DyeColorWrapper previous = DyeColorWrapper.getPrevious(entity.getColor());
            DyeColorWrapper next = DyeColorWrapper.getNext(entity.getColor());

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
    public void onLeftClick(IColorable entity) {
        entity.setColor(DyeColorWrapper.getNext(entity.getColor()));
    }

    @Override
    public void onRightClick(IColorable entity) {
        entity.setColor(DyeColorWrapper.getPrevious(entity.getColor()));
    }

    @Override
    public Object value(IColorable entity) {
        return entity.getColor();
    }
}
