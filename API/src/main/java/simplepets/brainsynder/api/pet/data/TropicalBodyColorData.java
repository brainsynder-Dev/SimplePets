package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.apache.WordUtils;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.nms.DataConverter;
import lib.brainsynder.utils.DyeColorWrapper;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityTropicalFishPet;
import simplepets.brainsynder.api.pet.PetData;

import java.util.Optional;

@Namespace(namespace = "body_color")
public class TropicalBodyColorData extends PetData<IEntityTropicalFishPet> {
    public TropicalBodyColorData() {
        for (DyeColorWrapper color : DyeColorWrapper.values()) {
            addDefaultItem(color.name(), DataConverter.getColoredMaterial(DataConverter.MaterialType.WOOL, color)
                    .withName(" ")
                    .addLore(
                            "&#c8c8c8Previous: {previousColor}{previousName}",
                            "&#c8c8c8Current: {currentColor}{currentName}",
                            "&#c8c8c8Next: {nextColor}{nextName}"));
        }
    }

    @Override
    public Object getDefaultValue() {
        return DyeColorWrapper.WHITE;
    }

    @Override
    public Optional<ItemBuilder> getItem(IEntityTropicalFishPet entity) {
        Optional<ItemBuilder> optional = super.getItem(entity);
        if (optional.isPresent()) {
            // We have to do this to replace the placholders if there is any

            DyeColorWrapper previous = DyeColorWrapper.getPrevious(entity.getBodyColor());
            DyeColorWrapper next = DyeColorWrapper.getNext(entity.getBodyColor());

            ItemBuilder builder = optional.get();
            builder.replaceInLore("{previousColor}", previous.getChatColor())
                    .replaceInLore("{currentColor}", entity.getBodyColor().getChatColor())
                    .replaceInLore("{nextColor}", next.getChatColor())
                    .replaceInLore("{previousName}", WordUtils.capitalize(previous.name().toLowerCase().replace("_", " ")))
                    .replaceInLore("{currentName}", WordUtils.capitalize(entity.getBodyColor().name().toLowerCase().replace("_", " ")))
                    .replaceInLore("{nextName}", WordUtils.capitalize(next.name().toLowerCase().replace("_", " ")));

            builder.replaceInName("{previousColor}", previous.getChatColor())
                    .replaceInName("{currentColor}", entity.getBodyColor().getChatColor())
                    .replaceInName("{nextColor}", next.getChatColor())
                    .replaceInName("{previousName}", WordUtils.capitalize(previous.name().toLowerCase().replace("_", " ")))
                    .replaceInName("{currentName}", WordUtils.capitalize(entity.getBodyColor().name().toLowerCase().replace("_", " ")))
                    .replaceInName("{nextName}", WordUtils.capitalize(next.name().toLowerCase().replace("_", " ")));
            return Optional.of(builder);
        }
        return optional;
    }

    @Override
    public void onLeftClick(IEntityTropicalFishPet entity) {
        entity.setBodyColor(DyeColorWrapper.getNext(entity.getBodyColor()));
    }

    @Override
    public void onRightClick(IEntityTropicalFishPet entity) {
        entity.setBodyColor(DyeColorWrapper.getPrevious(entity.getBodyColor()));
    }

    @Override
    public Object value(IEntityTropicalFishPet entity) {
        return entity.getBodyColor();
    }
}
