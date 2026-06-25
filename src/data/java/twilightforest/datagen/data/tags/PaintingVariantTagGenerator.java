package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.entity.decoration.painting.PaintingVariants;
import twilightforest.TwilightForestMod;
import twilightforest.tags.TFPaintingVariantTags;

import java.util.concurrent.CompletableFuture;

public class PaintingVariantTagGenerator extends KeyTagProvider<PaintingVariant> {

	public PaintingVariantTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.PAINTING_VARIANT, provider, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		// Every single painting except for Humble
		tag(TFPaintingVariantTags.LICH_TOWER_PAINTINGS).add(
			PaintingVariants.KEBAB,
			PaintingVariants.AZTEC,
			PaintingVariants.ALBAN,
			PaintingVariants.AZTEC2,
			PaintingVariants.BOMB,
			PaintingVariants.PLANT,
			PaintingVariants.WASTELAND,
			PaintingVariants.POOL,
			PaintingVariants.COURBET,
			PaintingVariants.SEA,
			PaintingVariants.SUNSET,
			PaintingVariants.CREEBET,
			PaintingVariants.WANDERER,
			PaintingVariants.GRAHAM,
			PaintingVariants.MATCH,
			PaintingVariants.BUST,
			PaintingVariants.STAGE,
			PaintingVariants.VOID,
			PaintingVariants.SKULL_AND_ROSES,
			PaintingVariants.WITHER,
			PaintingVariants.FIGHTERS,
			PaintingVariants.POINTER,
			PaintingVariants.PIGSCENE,
			PaintingVariants.BURNING_SKULL,
			PaintingVariants.SKELETON,
			PaintingVariants.DONKEY_KONG,
			PaintingVariants.EARTH,
			PaintingVariants.WIND,
			PaintingVariants.WATER,
			PaintingVariants.FIRE,
			PaintingVariants.BAROQUE,
			PaintingVariants.MEDITATIVE,
			PaintingVariants.PRAIRIE_RIDE,
			PaintingVariants.UNPACKED,
			PaintingVariants.BACKYARD,
			PaintingVariants.BOUQUET,
			PaintingVariants.CAVEBIRD,
			PaintingVariants.CHANGING,
			PaintingVariants.COTAN,
			PaintingVariants.ENDBOSS,
			PaintingVariants.FERN,
			PaintingVariants.FINDING,
			PaintingVariants.LOWMIST,
			PaintingVariants.ORB,
			PaintingVariants.OWLEMONS,
			PaintingVariants.PASSAGE,
			PaintingVariants.POND,
			PaintingVariants.SUNFLOWERS,
			PaintingVariants.TIDES
		);
		// Every single painting except for Humble, Unpacked, and the 4 elements
		tag(TFPaintingVariantTags.LICH_BOSS_PAINTINGS).add(
			PaintingVariants.KEBAB,
			PaintingVariants.AZTEC,
			PaintingVariants.ALBAN,
			PaintingVariants.AZTEC2,
			PaintingVariants.BOMB,
			PaintingVariants.PLANT,
			PaintingVariants.WASTELAND,
			PaintingVariants.POOL,
			PaintingVariants.COURBET,
			PaintingVariants.SEA,
			PaintingVariants.SUNSET,
			PaintingVariants.CREEBET,
			PaintingVariants.WANDERER,
			PaintingVariants.GRAHAM,
			PaintingVariants.MATCH,
			PaintingVariants.BUST,
			PaintingVariants.STAGE,
			PaintingVariants.VOID,
			PaintingVariants.SKULL_AND_ROSES,
			PaintingVariants.WITHER,
			PaintingVariants.FIGHTERS,
			PaintingVariants.POINTER,
			PaintingVariants.PIGSCENE,
			PaintingVariants.BURNING_SKULL,
			PaintingVariants.SKELETON,
			PaintingVariants.DONKEY_KONG,
			PaintingVariants.BAROQUE,
			PaintingVariants.MEDITATIVE,
			PaintingVariants.PRAIRIE_RIDE,
			PaintingVariants.BACKYARD,
			PaintingVariants.BOUQUET,
			PaintingVariants.CAVEBIRD,
			PaintingVariants.CHANGING,
			PaintingVariants.COTAN,
			PaintingVariants.ENDBOSS,
			PaintingVariants.FERN,
			PaintingVariants.FINDING,
			PaintingVariants.LOWMIST,
			PaintingVariants.ORB,
			PaintingVariants.OWLEMONS,
			PaintingVariants.PASSAGE,
			PaintingVariants.POND,
			PaintingVariants.SUNFLOWERS,
			PaintingVariants.TIDES
		);
	}

	@Override
	public String getName() {
		return "Twilight Forest Painting Variant Tags";
	}
}
