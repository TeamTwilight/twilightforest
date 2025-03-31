package twilightforest.data.models;

import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.BlockStateGenerator;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import twilightforest.data.TFBlockFamilies;
import twilightforest.data.helpers.BlockModelBuilders;
import twilightforest.init.TFBlocks;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BlockModelGenerator extends BlockModelBuilders {
	public BlockModelGenerator(Consumer<BlockStateGenerator> stateOutput, ItemModelOutput itemOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
		super(stateOutput, itemOutput, modelOutput);
	}

	@Override
	public void run() {
		TFBlockFamilies.getAllFamilies().filter(BlockFamily::shouldGenerateModel).forEach((family) -> this.family(family.getBaseBlock()).generateFor(family));

		this.bossSpawner(TFBlocks.NAGA_BOSS_SPAWNER.get());
		this.bossSpawner(TFBlocks.LICH_BOSS_SPAWNER.get());
		this.bossSpawner(TFBlocks.MINOSHROOM_BOSS_SPAWNER.get());
		this.bossSpawner(TFBlocks.HYDRA_BOSS_SPAWNER.get());
		this.bossSpawner(TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get());
		this.bossSpawner(TFBlocks.UR_GHAST_BOSS_SPAWNER.get());
		this.bossSpawner(TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get());
		this.bossSpawner(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get());
		this.bossSpawner(TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get());

		this.blockWithRenderType(TFBlocks.ANTIBUILDER.get(), "cutout", TFBlockModelTemplates.ANTIBUILDER, TFTextureMapping::threeLayerBlock);
		this.blockWithRenderType(TFBlocks.ANTIBUILT_BLOCK.get(), "cutout", TFBlockModelTemplates.ANTIBUILT_BLOCK, TFTextureMapping::twoLayerBlock);
		this.basicCtmBlock(TFBlocks.ARCTIC_FUR_BLOCK.get());
		//TODO aurora blocks
		this.simpleBlockWithRenderType(TFBlocks.BEANSTALK_LEAVES.get(), "cutout_mipped");
		this.castleDoor(TFBlocks.BLUE_CASTLE_DOOR.get());
		this.forcefield(TFBlocks.BLUE_FORCE_FIELD.get());
		this.createRotatedPillarWithHorizontalVariant(TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get(), TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT);
		this.createTrivialCube(TFBlocks.BOLD_CASTLE_BRICK_TILE.get());
	}
}
