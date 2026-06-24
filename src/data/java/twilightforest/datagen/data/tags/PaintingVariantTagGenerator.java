package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.entity.decoration.painting.PaintingVariants;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.tags.TFPaintingVariantTags;

import java.util.concurrent.CompletableFuture;

public class PaintingVariantTagGenerator extends TagsProvider<@NotNull PaintingVariant> {

	public PaintingVariantTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.PAINTING_VARIANT, provider, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		// Every single painting except for Humble
		getOrCreateRawBuilder(TFPaintingVariantTags.LICH_TOWER_PAINTINGS).addElement(
			PaintingVariants.KEBAB.identifier()).addElement(
			PaintingVariants.AZTEC.identifier()).addElement(
			PaintingVariants.ALBAN.identifier()).addElement(
			PaintingVariants.AZTEC2.identifier()).addElement(
			PaintingVariants.BOMB.identifier()).addElement(
			PaintingVariants.PLANT.identifier()).addElement(
			PaintingVariants.WASTELAND.identifier()).addElement(
			PaintingVariants.POOL.identifier()).addElement(
			PaintingVariants.COURBET.identifier()).addElement(
			PaintingVariants.SEA.identifier()).addElement(
			PaintingVariants.SUNSET.identifier()).addElement(
			PaintingVariants.CREEBET.identifier()).addElement(
			PaintingVariants.WANDERER.identifier()).addElement(
			PaintingVariants.GRAHAM.identifier()).addElement(
			PaintingVariants.MATCH.identifier()).addElement(
			PaintingVariants.BUST.identifier()).addElement(
			PaintingVariants.STAGE.identifier()).addElement(
			PaintingVariants.VOID.identifier()).addElement(
			PaintingVariants.SKULL_AND_ROSES.identifier()).addElement(
			PaintingVariants.WITHER.identifier()).addElement(
			PaintingVariants.FIGHTERS.identifier()).addElement(
			PaintingVariants.POINTER.identifier()).addElement(
			PaintingVariants.PIGSCENE.identifier()).addElement(
			PaintingVariants.BURNING_SKULL.identifier()).addElement(
			PaintingVariants.SKELETON.identifier()).addElement(
			PaintingVariants.DONKEY_KONG.identifier()).addElement(
			PaintingVariants.EARTH.identifier()).addElement(
			PaintingVariants.WIND.identifier()).addElement(
			PaintingVariants.WATER.identifier()).addElement(
			PaintingVariants.FIRE.identifier()).addElement(
			PaintingVariants.BAROQUE.identifier()).addElement(
			PaintingVariants.MEDITATIVE.identifier()).addElement(
			PaintingVariants.PRAIRIE_RIDE.identifier()).addElement(
			PaintingVariants.UNPACKED.identifier()).addElement(
			PaintingVariants.BACKYARD.identifier()).addElement(
			PaintingVariants.BOUQUET.identifier()).addElement(
			PaintingVariants.CAVEBIRD.identifier()).addElement(
			PaintingVariants.CHANGING.identifier()).addElement(
			PaintingVariants.COTAN.identifier()).addElement(
			PaintingVariants.ENDBOSS.identifier()).addElement(
			PaintingVariants.FERN.identifier()).addElement(
			PaintingVariants.FINDING.identifier()).addElement(
			PaintingVariants.LOWMIST.identifier()).addElement(
			PaintingVariants.ORB.identifier()).addElement(
			PaintingVariants.OWLEMONS.identifier()).addElement(
			PaintingVariants.PASSAGE.identifier()).addElement(
			PaintingVariants.POND.identifier()).addElement(
			PaintingVariants.SUNFLOWERS.identifier()).addElement(
			PaintingVariants.TIDES.identifier()
		);
		// Every single painting except for Humble, Unpacked and the 4 elements
		getOrCreateRawBuilder(TFPaintingVariantTags.LICH_BOSS_PAINTINGS).addElement(
			PaintingVariants.KEBAB.identifier()).addElement(
			PaintingVariants.AZTEC.identifier()).addElement(
			PaintingVariants.ALBAN.identifier()).addElement(
			PaintingVariants.AZTEC2.identifier()).addElement(
			PaintingVariants.BOMB.identifier()).addElement(
			PaintingVariants.PLANT.identifier()).addElement(
			PaintingVariants.WASTELAND.identifier()).addElement(
			PaintingVariants.POOL.identifier()).addElement(
			PaintingVariants.COURBET.identifier()).addElement(
			PaintingVariants.SEA.identifier()).addElement(
			PaintingVariants.SUNSET.identifier()).addElement(
			PaintingVariants.CREEBET.identifier()).addElement(
			PaintingVariants.WANDERER.identifier()).addElement(
			PaintingVariants.GRAHAM.identifier()).addElement(
			PaintingVariants.MATCH.identifier()).addElement(
			PaintingVariants.BUST.identifier()).addElement(
			PaintingVariants.STAGE.identifier()).addElement(
			PaintingVariants.VOID.identifier()).addElement(
			PaintingVariants.SKULL_AND_ROSES.identifier()).addElement(
			PaintingVariants.WITHER.identifier()).addElement(
			PaintingVariants.FIGHTERS.identifier()).addElement(
			PaintingVariants.POINTER.identifier()).addElement(
			PaintingVariants.PIGSCENE.identifier()).addElement(
			PaintingVariants.BURNING_SKULL.identifier()).addElement(
			PaintingVariants.SKELETON.identifier()).addElement(
			PaintingVariants.DONKEY_KONG.identifier()).addElement(
			PaintingVariants.BAROQUE.identifier()).addElement(
			PaintingVariants.MEDITATIVE.identifier()).addElement(
			PaintingVariants.PRAIRIE_RIDE.identifier()).addElement(
			PaintingVariants.BACKYARD.identifier()).addElement(
			PaintingVariants.BOUQUET.identifier()).addElement(
			PaintingVariants.CAVEBIRD.identifier()).addElement(
			PaintingVariants.CHANGING.identifier()).addElement(
			PaintingVariants.COTAN.identifier()).addElement(
			PaintingVariants.ENDBOSS.identifier()).addElement(
			PaintingVariants.FERN.identifier()).addElement(
			PaintingVariants.FINDING.identifier()).addElement(
			PaintingVariants.LOWMIST.identifier()).addElement(
			PaintingVariants.ORB.identifier()).addElement(
			PaintingVariants.OWLEMONS.identifier()).addElement(
			PaintingVariants.PASSAGE.identifier()).addElement(
			PaintingVariants.POND.identifier()).addElement(
			PaintingVariants.SUNFLOWERS.identifier()).addElement(
			PaintingVariants.TIDES.identifier()
		);
	}

	@Override
	public String getName() {
		return "Twilight Forest Painting Variant Tags";
	}
}
