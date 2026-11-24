package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlockEntities;
import twilightforest.tags.TFBlockEntityTypeTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BlockEntityTypeTagGenerator extends TagsProvider<BlockEntityType<?>> {

	public BlockEntityTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.BLOCK_ENTITY_TYPE, provider, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(TFBlockEntityTypeTags.RELOCATION_NOT_SUPPORTED).addAll(List.of(
			TFBlockEntities.ANTIBUILDER.key(),
			TFBlockEntities.BEANSTALK_GROWER.key(),
			TFBlockEntities.NAGA_SPAWNER.key(),
			TFBlockEntities.LICH_SPAWNER.key(),
			TFBlockEntities.MINOSHROOM_SPAWNER.key(),
			TFBlockEntities.HYDRA_SPAWNER.key(),
			TFBlockEntities.KNIGHT_PHANTOM_SPAWNER.key(),
			TFBlockEntities.UR_GHAST_SPAWNER.key(),
			TFBlockEntities.ALPHA_YETI_SPAWNER.key(),
			TFBlockEntities.SNOW_QUEEN_SPAWNER.key(),
			TFBlockEntities.FINAL_BOSS_SPAWNER.key()));

		this.tag(TFBlockEntityTypeTags.IMMOVABLE).addAll(List.of(
			TFBlockEntities.ANTIBUILDER.key(),
			TFBlockEntities.BEANSTALK_GROWER.key(),
			TFBlockEntities.NAGA_SPAWNER.key(),
			TFBlockEntities.LICH_SPAWNER.key(),
			TFBlockEntities.MINOSHROOM_SPAWNER.key(),
			TFBlockEntities.HYDRA_SPAWNER.key(),
			TFBlockEntities.KNIGHT_PHANTOM_SPAWNER.key(),
			TFBlockEntities.UR_GHAST_SPAWNER.key(),
			TFBlockEntities.ALPHA_YETI_SPAWNER.key(),
			TFBlockEntities.SNOW_QUEEN_SPAWNER.key(),
			TFBlockEntities.FINAL_BOSS_SPAWNER.key()));
	}

	@Override
	public String getName() {
		return "Twilight Forest Block Entity Tags";
	}
}
