package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.apache.WordUtils;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.nms.DataConverter;
import lib.brainsynder.utils.DyeColorWrapper;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityCatPet;
import simplepets.brainsynder.api.pet.PetData;

import java.util.Optional;

@Namespace(namespace = "collar")
public class CatCollarData extends PetData<IEntityCatPet> {
    public CatCollarData() {
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
    public Object getDefaultValue() {
        return DyeColorWrapper.WHITE;
    }

    @Override
    public Optional<ItemBuilder> getItem(IEntityCatPet entity) {
        Optional<ItemBuilder> optional = super.getItem(entity);
        if (optional.isPresent()) {
            // We have to do this to replace the placholders if there is any

            DyeColorWrapper previous = DyeColorWrapper.getPrevious(entity.getCollarColor());
            DyeColorWrapper next = DyeColorWrapper.getNext(entity.getCollarColor());

            ItemBuilder builder = optional.get();
            builder.replaceInLore("{previousColor}", previous.getChatColor())
                    .replaceInLore("{currentColor}", entity.getCollarColor().getChatColor())
                    .replaceInLore("{nextColor}", next.getChatColor())
                    .replaceInLore("{previousName}", WordUtils.capitalize(previous.name().toLowerCase().replace("_", " ")))
                    .replaceInLore("{currentName}", WordUtils.capitalize(entity.getCollarColor().name().toLowerCase().replace("_", " ")))
                    .replaceInLore("{nextName}", WordUtils.capitalize(next.name().toLowerCase().replace("_", " ")));

            builder.replaceInName("{previousColor}", previous.getChatColor())
                    .replaceInName("{currentColor}", entity.getCollarColor().getChatColor())
                    .replaceInName("{nextColor}", next.getChatColor())
                    .replaceInName("{previousName}", WordUtils.capitalize(previous.name().toLowerCase().replace("_", " ")))
                    .replaceInName("{currentName}", WordUtils.capitalize(entity.getCollarColor().name().toLowerCase().replace("_", " ")))
                    .replaceInName("{nextName}", WordUtils.capitalize(next.name().toLowerCase().replace("_", " ")));
            return Optional.of(builder);
        }
        return optional;
    }

    @Override
    public void onLeftClick(IEntityCatPet entity) {
        entity.setCollarColor(DyeColorWrapper.getNext(entity.getCollarColor()));
    }

    @Override
    public void onRightClick(IEntityCatPet entity) {
        entity.setCollarColor(DyeColorWrapper.getPrevious(entity.getCollarColor()));
    }

    @Override
    public Object value(IEntityCatPet entity) {
        return entity.getCollarColor();
    }
}
