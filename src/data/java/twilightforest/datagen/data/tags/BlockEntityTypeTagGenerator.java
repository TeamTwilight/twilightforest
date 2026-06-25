package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlockEntities;
import twilightforest.tags.TFBlockEntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class BlockEntityTypeTagGenerator extends KeyTagProvider<BlockEntityType<?>> {

	public BlockEntityTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.BLOCK_ENTITY_TYPE, provider, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(TFBlockEntityTypeTags.RELOCATION_NOT_SUPPORTED).add(
			TFBlockEntities.ANTIBUILDER.getKey(),
			TFBlockEntities.BEANSTALK_GROWER.getKey(),
			TFBlockEntities.NAGA_SPAWNER.getKey(),
			TFBlockEntities.LICH_SPAWNER.getKey(),
			TFBlockEntities.MINOSHROOM_SPAWNER.getKey(),
			TFBlockEntities.HYDRA_SPAWNER.getKey(),
			TFBlockEntities.KNIGHT_PHANTOM_SPAWNER.getKey(),
			TFBlockEntities.UR_GHAST_SPAWNER.getKey(),
			TFBlockEntities.ALPHA_YETI_SPAWNER.getKey(),
			TFBlockEntities.SNOW_QUEEN_SPAWNER.getKey(),
			TFBlockEntities.FINAL_BOSS_SPAWNER.getKey());

		this.tag(TFBlockEntityTypeTags.IMMOVABLE).add(
			TFBlockEntities.ANTIBUILDER.getKey(),
			TFBlockEntities.BEANSTALK_GROWER.getKey(),
			TFBlockEntities.NAGA_SPAWNER.getKey(),
			TFBlockEntities.LICH_SPAWNER.getKey(),
			TFBlockEntities.MINOSHROOM_SPAWNER.getKey(),
			TFBlockEntities.HYDRA_SPAWNER.getKey(),
			TFBlockEntities.KNIGHT_PHANTOM_SPAWNER.getKey(),
			TFBlockEntities.UR_GHAST_SPAWNER.getKey(),
			TFBlockEntities.ALPHA_YETI_SPAWNER.getKey(),
			TFBlockEntities.SNOW_QUEEN_SPAWNER.getKey(),
			TFBlockEntities.FINAL_BOSS_SPAWNER.getKey());
	}

	@Override
	public String getName() {
		return "Twilight Forest Block Entity Tags";
	}
}
