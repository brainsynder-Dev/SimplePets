package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.apache.WordUtils;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.utils.DyeColorWrapper;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityTropicalFishPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.TropicalPattern;

import java.util.Optional;

@Namespace(namespace = "pattern")
public class TropicalPatternData extends PetData<IEntityTropicalFishPet> {
    public TropicalPatternData() {
        for (TropicalPattern pattern : TropicalPattern.values()) {
            addDefaultItem(pattern.name(), new ItemBuilder(Material.PLAYER_HEAD)
                    .setTexture("http://textures.minecraft.net/texture/36d149e4d499929672e2768949e6477959c21e65254613b327b538df1e4df")
                    .withName("&#c8c8c8{name}: &a" + pattern.name()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return TropicalPattern.KOB;
    }

    @Override
    public Optional<ItemBuilder> getItem(IEntityTropicalFishPet entity) {
        Optional<ItemBuilder> optional = super.getItem(entity);
        if (optional.isPresent()) {
            // We have to do this to replace the placholders if there is any

            DyeColorWrapper previous = DyeColorWrapper.getPrevious(entity.getPatternColor());
            DyeColorWrapper next = DyeColorWrapper.getNext(entity.getPatternColor());

            ItemBuilder builder = optional.get();
            builder.replaceInLore("{previousColor}", previous.getChatColor())
                    .replaceInLore("{currentColor}", entity.getPatternColor().getChatColor())
                    .replaceInLore("{nextColor}", next.getChatColor())
                    .replaceInLore("{previousName}", WordUtils.capitalize(previous.name().toLowerCase().replace("_", " ")))
                    .replaceInLore("{currentName}", WordUtils.capitalize(entity.getPatternColor().name().toLowerCase().replace("_", " ")))
                    .replaceInLore("{nextName}", WordUtils.capitalize(next.name().toLowerCase().replace("_", " ")));

            builder.replaceInName("{previousColor}", previous.getChatColor())
                    .replaceInName("{currentColor}", entity.getPatternColor().getChatColor())
                    .replaceInName("{nextColor}", next.getChatColor())
                    .replaceInName("{previousName}", WordUtils.capitalize(previous.name().toLowerCase().replace("_", " ")))
                    .replaceInName("{currentName}", WordUtils.capitalize(entity.getPatternColor().name().toLowerCase().replace("_", " ")))
                    .replaceInName("{nextName}", WordUtils.capitalize(next.name().toLowerCase().replace("_", " ")));
            return Optional.of(builder);
        }
        return optional;
    }

    @Override
    public void onLeftClick(IEntityTropicalFishPet entity) {
        entity.setPattern(TropicalPattern.getNext(entity.getPattern()));
    }

    @Override
    public void onRightClick(IEntityTropicalFishPet entity) {
        entity.setPattern(TropicalPattern.getPrevious(entity.getPattern()));
    }

    @Override
    public Object value(IEntityTropicalFishPet entity) {
        return entity.getPattern();
    }
}
