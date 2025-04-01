package twilightforest.datagen.assets.models;

import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import twilightforest.block.TranslucentBuiltBlock;
import twilightforest.util.TFBlockFamilies;
import twilightforest.datagen.helpers.BlockModelBuilders;
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

		this.thorns(TFBlocks.BROWN_THORNS.get());
		this.thorns(TFBlocks.GREEN_THORNS.get());
		this.thorns(TFBlocks.BURNT_THORNS.get());

		this.wrapBlockItem(TFBlocks.ANTIBUILDER.get(), block -> this.blockWithRenderType(block, "cutout", TFBlockModelTemplates.ANTIBUILDER, TFTextureMapping::threeLayerBlock));
		this.blockWithRenderType(TFBlocks.ANTIBUILT_BLOCK.get(), "cutout", TFBlockModelTemplates.ANTIBUILT_BLOCK, TFTextureMapping::twoLayerBlock);
		this.basicCtmBlock(TFBlocks.ARCTIC_FUR_BLOCK.get());
		//TODO aurora blocks
		this.wrapBlockItem(TFBlocks.BEANSTALK_LEAVES.get(), block -> this.blockWithRenderType(block, "cutout_mipped", ModelTemplates.CUBE_ALL, u -> TextureMapping.cube(Blocks.AZALEA_LEAVES)));
		this.castleDoor(TFBlocks.BLUE_CASTLE_DOOR.get());
		this.forcefield(TFBlocks.BLUE_FORCE_FIELD.get());
		this.wrapBlockItem(TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get(), block -> this.createRotatedPillarWithHorizontalVariant(block, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT));
		this.stairsBlock(TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get());
		this.wrapBlockItem(TFBlocks.BOLD_CASTLE_BRICK_TILE.get(), this::createTrivialCube);

		var builtMapping = TextureMapping.cube(TFBlocks.BUILT_BLOCK.get());
		ResourceLocation builtOff = TFBlockModelTemplates.FULLBRIGHT_BLOCK.create(TFBlocks.BUILT_BLOCK.get(), builtMapping, this.modelOutput);
		ResourceLocation builtOn = TFBlockModelTemplates.FULLBRIGHT_BLOCK.createWithSuffix(TFBlocks.BUILT_BLOCK.get(), "_active", builtMapping, this.modelOutput);
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(TFBlocks.BUILT_BLOCK.get())
			.with(PropertyDispatch.property(TranslucentBuiltBlock.ACTIVE).generate(active -> Variant.variant().with(VariantProperties.MODEL, active ? builtOn : builtOff))));
	}
}
