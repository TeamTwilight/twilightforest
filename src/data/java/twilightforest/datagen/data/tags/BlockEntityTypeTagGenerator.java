package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlockEntities;
import twilightforest.tags.TFBlockEntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class BlockEntityTypeTagGenerator extends TagsProvider<@NotNull BlockEntityType<?>> {

	public BlockEntityTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.BLOCK_ENTITY_TYPE, provider, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.getOrCreateRawBuilder(TFBlockEntityTypeTags.RELOCATION_NOT_SUPPORTED).addElement(
			TFBlockEntities.ANTIBUILDER.getKey().identifier()).addElement(
			TFBlockEntities.BEANSTALK_GROWER.getKey().identifier()).addElement(
			TFBlockEntities.NAGA_SPAWNER.getKey().identifier()).addElement(
			TFBlockEntities.LICH_SPAWNER.getKey().identifier()).addElement(
			TFBlockEntities.MINOSHROOM_SPAWNER.getKey().identifier()).addElement(
			TFBlockEntities.HYDRA_SPAWNER.getKey().identifier()).addElement(
			TFBlockEntities.KNIGHT_PHANTOM_SPAWNER.getKey().identifier()).addElement(
			TFBlockEntities.UR_GHAST_SPAWNER.getKey().identifier()).addElement(
			TFBlockEntities.ALPHA_YETI_SPAWNER.getKey().identifier()).addElement(
			TFBlockEntities.SNOW_QUEEN_SPAWNER.getKey().identifier()).addElement(
			TFBlockEntities.FINAL_BOSS_SPAWNER.getKey().identifier());

		this.getOrCreateRawBuilder(TFBlockEntityTypeTags.IMMOVABLE).addElement(
			TFBlockEntities.ANTIBUILDER.getKey().identifier()).addElement(
			TFBlockEntities.BEANSTALK_GROWER.getKey().identifier()).addElement(
			TFBlockEntities.NAGA_SPAWNER.getKey().identifier()).addElement(
			TFBlockEntities.LICH_SPAWNER.getKey().identifier()).addElement(
			TFBlockEntities.MINOSHROOM_SPAWNER.getKey().identifier()).addElement(
			TFBlockEntities.HYDRA_SPAWNER.getKey().identifier()).addElement(
			TFBlockEntities.KNIGHT_PHANTOM_SPAWNER.getKey().identifier()).addElement(
			TFBlockEntities.UR_GHAST_SPAWNER.getKey().identifier()).addElement(
			TFBlockEntities.ALPHA_YETI_SPAWNER.getKey().identifier()).addElement(
			TFBlockEntities.SNOW_QUEEN_SPAWNER.getKey().identifier()).addElement(
			TFBlockEntities.FINAL_BOSS_SPAWNER.getKey().identifier());
	}

	@Override
	public String getName() {
		return "Twilight Forest Block Entity Tags";
	}
}
