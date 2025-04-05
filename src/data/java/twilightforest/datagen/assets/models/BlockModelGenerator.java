package twilightforest.datagen.assets.models;

import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.core.Direction;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;
import twilightforest.client.renderer.special.*;
import twilightforest.util.TFBlockFamilies;
import twilightforest.datagen.helpers.BlockModelBuilders;
import twilightforest.init.TFBlocks;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class BlockModelGenerator extends BlockModelBuilders {
	public BlockModelGenerator(Consumer<BlockStateGenerator> stateOutput, ItemModelOutput itemOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
		super(stateOutput, itemOutput, modelOutput);
	}

	@Override
	public void run() {
		TFBlockFamilies.getAllFamilies().filter(BlockFamily::shouldGenerateModel).forEach((family) -> this.family(family.getBaseBlock()).generateFor(family));

		this.spawner(TFBlocks.NAGA_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.LICH_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.MINOSHROOM_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.HYDRA_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.UR_GHAST_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get(), "block/boss_spawner");
		this.spawner(TFBlocks.SINISTER_SPAWNER.get(), "block/sinister_spawner");

		this.thorns(TFBlocks.BROWN_THORNS.get());
		this.thorns(TFBlocks.GREEN_THORNS.get());
		this.thorns(TFBlocks.BURNT_THORNS.get());
		this.directionalCrossModel(TFBlocks.THORN_ROSE.get(), PlantType.NOT_TINTED);
		this.wrapBlockItem(TFBlocks.DEADROCK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.CRACKED_DEADROCK.get(), this::createTrivialCube);
		this.wrapBlockItem(TFBlocks.WEATHERED_DEADROCK.get(), this::createTrivialCube);

		this.nagaStone();

		this.wrapBlockItem(TFBlocks.ANTIBUILDER.get(), block -> this.blockWithRenderType(block, "cutout", TFModelTemplates.ANTIBUILDER, TFTextureMapping::threeLayerBlock));
		this.blockWithRenderType(TFBlocks.ANTIBUILT_BLOCK.get(), "cutout", TFModelTemplates.ANTIBUILT_BLOCK, TFTextureMapping::twoLayerBlock);
		this.basicCtmBlock(TFBlocks.ARCTIC_FUR_BLOCK.get());
		//TODO aurora blocks
		this.wrapBlockItem(TFBlocks.BEANSTALK_LEAVES.get(), block -> this.blockWithRenderType(block, "cutout_mipped", ModelTemplates.CUBE_ALL, u -> TextureMapping.cube(Blocks.AZALEA_LEAVES)));
		this.castleDoor(TFBlocks.BLUE_CASTLE_DOOR.get());
		this.forcefield(TFBlocks.BLUE_FORCE_FIELD.get());
		this.wrapBlockItem(TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get(), block -> this.createRotatedPillarWithHorizontalVariant(block, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT));
		this.stairsBlock(TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get());
		this.wrapBlockItem(TFBlocks.BOLD_CASTLE_BRICK_TILE.get(), this::createTrivialCube);

		var builtMapping = TextureMapping.cube(TFBlocks.BUILT_BLOCK.get());
		ResourceLocation builtOff = TFModelTemplates.FULLBRIGHT_BLOCK.create(TFBlocks.BUILT_BLOCK.get(), builtMapping, this.modelOutput);
		ResourceLocation builtOn = TFModelTemplates.FULLBRIGHT_BLOCK.createWithSuffix(TFBlocks.BUILT_BLOCK.get(), "_active", builtMapping, this.modelOutput);
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(TFBlocks.BUILT_BLOCK.get())
			.with(PropertyDispatch.property(TranslucentBuiltBlock.ACTIVE).generate(active -> Variant.variant().with(VariantProperties.MODEL, active ? builtOn : builtOff))));

		this.generateSpecialModel(TFBlocks.KEEPSAKE_CASKET.get(), Blocks.NETHERITE_BLOCK, block -> ItemModelUtils.specialModel(TwilightForestMod.prefix("item/keepsake_casket"), new KeepsakeCasketSpecialRenderer.Unbaked()));
		this.generateSpecialModel(TFBlocks.SKULL_CHEST.get(), Blocks.BONE_BLOCK, block -> ItemModelUtils.specialModel(TwilightForestMod.prefix("item/skull_chest"), new SkullChestSpecialRenderer.Unbaked()));
		this.generateSpecialModel(TFBlocks.CICADA.get(), Blocks.SLIME_BLOCK, block -> ItemModelUtils.specialModel(TwilightForestMod.prefix("item/cicada"), new CicadaSpecialRenderer.Unbaked()));
		this.generateSpecialModel(TFBlocks.FIREFLY.get(), Blocks.SLIME_BLOCK, block -> ItemModelUtils.specialModel(TwilightForestMod.prefix("item/firefly"), new FireflySpecialRenderer.Unbaked()));
		this.generateSpecialModel(TFBlocks.MOONWORM.get(), Blocks.SLIME_BLOCK, block -> ItemModelUtils.specialModel(TwilightForestMod.prefix("item/moonworm"), new MoonwormSpecialRenderer.Unbaked()));

		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(TFBlocks.CANDELABRA.get())
			.with(PropertyDispatch.property(CandelabraBlock.ON_WALL)
				.select(true, Variant.variant().with(VariantProperties.MODEL, TwilightForestMod.prefix("block/wall_candelabra")))
				.select(false, Variant.variant().with(VariantProperties.MODEL, TwilightForestMod.prefix("block/candelabra"))))
			.with(PropertyDispatch.property(CandelabraBlock.FACING)
				.select(Direction.NORTH, Variant.variant())
				.select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))));
		this.itemModelOutput.accept(TFBlocks.CANDELABRA.asItem(), ItemModelUtils.composite(ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(TFBlocks.CANDELABRA.get())), ItemModelUtils.specialModel(TwilightForestMod.prefix("block/candelabra"), new CandelabraSpecialRenderer.Unbaked())));

		this.generateSkullCandle(TFBlocks.ZOMBIE_SKULL_CANDLE.get(), TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get());
		this.generateSkullCandle(TFBlocks.SKELETON_SKULL_CANDLE.get(), TFBlocks.SKELETON_WALL_SKULL_CANDLE.get());
		this.generateSkullCandle(TFBlocks.CREEPER_SKULL_CANDLE.get(), TFBlocks.CREEPER_WALL_SKULL_CANDLE.get());
		this.generateSkullCandle(TFBlocks.WITHER_SKELE_SKULL_CANDLE.get(), TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.get());
		this.generateSkullCandle(TFBlocks.PLAYER_SKULL_CANDLE.get(), TFBlocks.PLAYER_WALL_SKULL_CANDLE.get());
		this.generateSkullCandle(TFBlocks.PIGLIN_SKULL_CANDLE.get(), TFBlocks.PIGLIN_WALL_SKULL_CANDLE.get());

		var major = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TwilightForestMod.prefix("item/major_boss_trophy"), TextureMapping.layer0(TwilightForestMod.prefix("item/trophy")), this.modelOutput));
		var minor = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TwilightForestMod.prefix("item/minor_boss_trophy"), TextureMapping.layer0(TwilightForestMod.prefix("item/trophy_minor")), this.modelOutput));
		var quest = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(TwilightForestMod.prefix("item/quest_trophy"), TextureMapping.layer0(TwilightForestMod.prefix("item/trophy_quest")), this.modelOutput));

		this.generateTrophy(TFBlocks.NAGA_TROPHY.get(), TFBlocks.NAGA_WALL_TROPHY.get(), major);
		this.generateTrophy(TFBlocks.LICH_TROPHY.get(), TFBlocks.LICH_WALL_TROPHY.get(), major);
		this.generateTrophy(TFBlocks.MINOSHROOM_TROPHY.get(), TFBlocks.MINOSHROOM_WALL_TROPHY.get(), minor, "smaller_gui_trophy");
		this.generateTrophy(TFBlocks.HYDRA_TROPHY.get(), TFBlocks.HYDRA_WALL_TROPHY.get(), major, "hydra_trophy");
		this.generateTrophy(TFBlocks.KNIGHT_PHANTOM_TROPHY.get(), TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.get(), minor, "smaller_gui_trophy");
		this.generateTrophy(TFBlocks.UR_GHAST_TROPHY.get(), TFBlocks.UR_GHAST_WALL_TROPHY.get(), major, "ur_ghast_trophy");
		this.generateTrophy(TFBlocks.ALPHA_YETI_TROPHY.get(), TFBlocks.ALPHA_YETI_WALL_TROPHY.get(), minor, "alpha_yeti_trophy");
		this.generateTrophy(TFBlocks.SNOW_QUEEN_TROPHY.get(), TFBlocks.SNOW_QUEEN_WALL_TROPHY.get(), major);
		this.generateTrophy(TFBlocks.QUEST_RAM_TROPHY.get(), TFBlocks.QUEST_RAM_WALL_TROPHY.get(), quest, "smaller_gui_trophy");

		this.createChest(TFBlocks.TWILIGHT_OAK_CHEST.get(), TFBlocks.TWILIGHT_OAK_PLANKS.get(), TwilightForestMod.prefix("twilight_oak/normal"), false);
		this.createChest(TFBlocks.CANOPY_CHEST.get(), TFBlocks.CANOPY_PLANKS.get(), TwilightForestMod.prefix("canopy/normal"), false);
		this.createChest(TFBlocks.MANGROVE_CHEST.get(), TFBlocks.MANGROVE_PLANKS.get(), TwilightForestMod.prefix("mangrove/normal"), false);
		this.createChest(TFBlocks.DARK_CHEST.get(), TFBlocks.DARK_PLANKS.get(), TwilightForestMod.prefix("darkwood/normal"), false);
		this.createChest(TFBlocks.TIME_CHEST.get(), TFBlocks.TIME_PLANKS.get(), TwilightForestMod.prefix("time/normal"), false);
		this.createChest(TFBlocks.TRANSFORMATION_CHEST.get(), TFBlocks.TRANSFORMATION_PLANKS.get(), TwilightForestMod.prefix("transformation/normal"), false);
		this.createChest(TFBlocks.MINING_CHEST.get(), TFBlocks.MINING_PLANKS.get(), TwilightForestMod.prefix("mining/normal"), false);
		this.createChest(TFBlocks.SORTING_CHEST.get(), TFBlocks.SORTING_PLANKS.get(), TwilightForestMod.prefix("sorting/normal"), false);

		this.createChest(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(), TFBlocks.TWILIGHT_OAK_PLANKS.get(), TwilightForestMod.prefix("twilight_oak/trapped"), false);
		this.createChest(TFBlocks.CANOPY_TRAPPED_CHEST.get(), TFBlocks.CANOPY_PLANKS.get(), TwilightForestMod.prefix("canopy/trapped"), false);
		this.createChest(TFBlocks.MANGROVE_TRAPPED_CHEST.get(), TFBlocks.MANGROVE_PLANKS.get(), TwilightForestMod.prefix("mangrove/trapped"), false);
		this.createChest(TFBlocks.DARK_TRAPPED_CHEST.get(), TFBlocks.DARK_PLANKS.get(), TwilightForestMod.prefix("darkwood/trapped"), false);
		this.createChest(TFBlocks.TIME_TRAPPED_CHEST.get(), TFBlocks.TIME_PLANKS.get(), TwilightForestMod.prefix("time/trapped"), false);
		this.createChest(TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(), TFBlocks.TRANSFORMATION_PLANKS.get(), TwilightForestMod.prefix("transformation/trapped"), false);
		this.createChest(TFBlocks.MINING_TRAPPED_CHEST.get(), TFBlocks.MINING_PLANKS.get(), TwilightForestMod.prefix("mining/trapped"), false);
		this.createChest(TFBlocks.SORTING_TRAPPED_CHEST.get(), TFBlocks.SORTING_PLANKS.get(), TwilightForestMod.prefix("sorting/trapped"), false);
	}

	public <B extends Block> void generateSpecialModel(B block, Block particleBlock, Function<B, ItemModel.Unbaked> itemModel) {
		this.createParticleOnlyBlock(block, particleBlock);
		this.itemModelOutput.accept(block.asItem(), itemModel.apply(block));
	}

	public void generateTrophy(TrophyBlock floor, TrophyWallBlock wall, ItemModel.Unbaked backplate) {
		this.generateTrophy(floor, wall, backplate, "template_trophy");
	}

	public void generateTrophy(TrophyBlock floor, TrophyWallBlock wall, ItemModel.Unbaked backplate, String existingTrophy) {
		ResourceLocation template = ModelLocationUtils.decorateBlockModelLocation("skull");
		this.blockStateOutput.accept(createSimpleBlock(floor, template));
		this.blockStateOutput.accept(createSimpleBlock(wall, template));
		var itemTrophy = ItemModelUtils.specialModel(ModelLocationUtils.decorateItemModelLocation("twilightforest:" + existingTrophy), new TrophySpecialRenderer.Unbaked(floor.getVariant()));
		this.itemModelOutput.accept(floor.asItem(), ItemModelUtils.select(new DisplayContext(), itemTrophy,
			ItemModelUtils.when(ItemDisplayContext.GUI, ItemModelUtils.composite(backplate, itemTrophy))));
	}

	public void generateSkullCandle(AbstractSkullCandleBlock floor, AbstractSkullCandleBlock wall) {
		ResourceLocation template = ModelLocationUtils.decorateBlockModelLocation("skull");
		this.blockStateOutput.accept(createSimpleBlock(floor, template));
		this.blockStateOutput.accept(createSimpleBlock(wall, template));
		this.itemModelOutput.accept(floor.asItem(), ItemModelUtils.specialModel(TwilightForestMod.prefix("item/template_skull_candle"), new SkullCandleSpecialRenderer.Unbaked(floor.getType())));
	}
}
