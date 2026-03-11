package twilightforest.world.components.feature.templates;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;
import twilightforest.loot.TFLootTables;
import twilightforest.util.entities.EntityUtil;
import twilightforest.world.components.feature.config.SwizzleConfig;
import twilightforest.world.components.processors.CobbleVariants;
import twilightforest.world.components.processors.StoneBricksVariants;

public class DruidHutFeature extends TemplateFeature<SwizzleConfig> {
	public DruidHutFeature(Codec<SwizzleConfig> config) {
		super(config);
	}

	@Override
	protected StructureTemplate getTemplate(StructureTemplateManager templateManager, RandomSource random) {
		return templateManager.getOrCreate(Util.getRandom(DruidHutFeature.HutType.values(), random).resourceLocation);
	}

	@Override
	protected void modifySettings(StructurePlaceSettings settings, RandomSource random, SwizzleConfig config) {
		config.buildAddProcessors(settings, random);
	}

	@Override
	protected void postPlacement(WorldGenLevel world, RandomSource random, StructureTemplateManager templateManager, Rotation rotation, Mirror mirror, StructurePlaceSettings placementSettings, BlockPos placementPos, SwizzleConfig config) {
		if (random.nextBoolean()) {
			StructureTemplate template = templateManager.getOrCreate(DruidHutFeature.BasementType.values()[random.nextInt(DruidHutFeature.BasementType.size)].getBasement(random.nextBoolean()));

			if (template == null) return;

			placementPos = placementPos.below(12).relative(rotation.rotate(mirror.mirror(Direction.NORTH)), 1).relative(rotation.rotate(mirror.mirror(Direction.EAST)), 1);

			placementSettings.clearProcessors();
			config.buildAddProcessors(placementSettings, random);
			placementSettings.addProcessor(CobbleVariants.INSTANCE).addProcessor(StoneBricksVariants.INSTANCE);

			template.placeInWorld(world, placementPos, placementPos, placementSettings, random, Block.UPDATE_CLIENTS);

			for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(placementPos, placementSettings, Blocks.STRUCTURE_BLOCK))
				if (info.nbt() != null && StructureMode.valueOf(info.nbt().getString("mode")) == StructureMode.DATA)
					this.processMarkers(info, world, rotation, mirror, random);
		}
	}

	@Override
	protected void processMarkers(StructureTemplate.StructureBlockInfo info, WorldGenLevel world, Rotation rotation, Mirror mirror, RandomSource random) {
		String s = info.nbt().getString("metadata");
		BlockPos blockPos = info.pos();
        /*
         `spawner` will place a Druid spawner.

         `loot` will place a chest facing the was-North.

         `lootT` will place a trapped chest facing the was-North.

         `lootW` will place a chest facing the was-West.
         `lootS` will place a chest facing the was-South.

         `lootET` will place a trapped chest facing the was-East.
         `lootNT` will place a trapped chest facing the was-North.
         */
		// removeBlock calls are required due to WorldGenRegion jank with cached TEs, this ensures the correct TE is used
		if ("spawner".equals(s)) {
			if (world.removeBlock(blockPos, false) && world.setBlock(blockPos, Blocks.SPAWNER.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS)) {
				BlockEntity tile = world.getBlockEntity(blockPos);

				if (tile instanceof SpawnerBlockEntity ms) {
					ms.setEntityId(TFEntities.SKELETON_DRUID.get(), random);
				}
			}
		} else if (s.startsWith("loot")) {
			world.removeBlock(blockPos, false);
			BlockState chest = s.endsWith("T") ? Blocks.TRAPPED_CHEST.defaultBlockState() : Blocks.CHEST.defaultBlockState();

			chest = switch (s.substring(5, 6)) {
				case "L" -> chest.setValue(ChestBlock.TYPE, mirror != Mirror.NONE ? ChestType.RIGHT : ChestType.LEFT);
				case "R" -> chest.setValue(ChestBlock.TYPE, mirror != Mirror.NONE ? ChestType.LEFT : ChestType.RIGHT);
				default -> chest.setValue(ChestBlock.TYPE, ChestType.SINGLE); // S
			};

			chest = switch (s.substring(4, 5)) {
				case "W" -> chest.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(mirror.mirror(Direction.WEST)));
				case "E" -> chest.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(mirror.mirror(Direction.EAST)));
				case "S" -> chest.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(mirror.mirror(Direction.SOUTH)));
				default -> chest.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(mirror.mirror(Direction.NORTH)));
			};

			TFLootTables.generateLootContainer(world, blockPos, chest, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS, (s.endsWith("J") ? TFLootTables.HUT_JUNK : TFLootTables.BASEMENT));
		} else if (s.startsWith("barrel")) {
			world.removeBlock(blockPos, false);
			BlockState chest = Blocks.BARREL.defaultBlockState();

			chest = switch (s.substring(6, 7)) {
				case "D" -> chest.setValue(BlockStateProperties.FACING, rotation.rotate(mirror.mirror(Direction.DOWN)));
				case "W" -> chest.setValue(BlockStateProperties.FACING, rotation.rotate(mirror.mirror(Direction.WEST)));
				case "E" -> chest.setValue(BlockStateProperties.FACING, rotation.rotate(mirror.mirror(Direction.EAST)));
				case "N" -> chest.setValue(BlockStateProperties.FACING, rotation.rotate(mirror.mirror(Direction.NORTH)));
				case "S" -> chest.setValue(BlockStateProperties.FACING, rotation.rotate(mirror.mirror(Direction.SOUTH)));
				default -> chest.setValue(BlockStateProperties.FACING, rotation.rotate(mirror.mirror(Direction.UP)));
			};

			TFLootTables.generateLootContainer(world, blockPos, chest, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS, TFLootTables.HUT_JUNK);
		} else if (s.startsWith("painting")) {
			world.removeBlock(blockPos, false);

			Direction direction = rotation.rotate(mirror.mirror(switch (s.substring(8, 9)) {
				case "W" -> Direction.WEST;
				case "E" -> Direction.EAST;
				case "S" -> Direction.SOUTH;
				default -> Direction.NORTH;
			}));

			String widthS = s.substring(9, 10);
			int paintingWidth = widthS.matches("\\d+") ? Integer.parseInt(widthS) : 1;

			boolean hasFlipped = mirror != Mirror.NONE;
			BlockPos hangPos = hasFlipped ? blockPos.relative(direction.getClockWise()) : blockPos;

			EntityUtil.tryHangPainting(world, hangPos, direction, EntityUtil.getPaintingOfSize(world, random, paintingWidth, paintingWidth == 2 || paintingWidth == 4 ? 2 : 1, true));
		}
	}

	private enum HutType {
		REGULAR(TwilightForestMod.prefix("feature/druid_hut/druid_hut")),
		SIDEWAYS(TwilightForestMod.prefix("feature/druid_hut/druid_sideways")),
		DOUBLE_DECK(TwilightForestMod.prefix("feature/druid_hut/druid_doubledeck"));

		private final ResourceLocation resourceLocation;

		HutType(ResourceLocation rl) {
			this.resourceLocation = rl;
		}
	}

	private enum BasementType {
		STUDY(TwilightForestMod.prefix("feature/druid_hut/basement_study"), TwilightForestMod.prefix("feature/druid_hut/basement_study_trap")),
		SHELVES(TwilightForestMod.prefix("feature/druid_hut/basement_shelves"), TwilightForestMod.prefix("feature/druid_hut/basement_shelves_trap")),
		GALLERY(TwilightForestMod.prefix("feature/druid_hut/basement_gallery"), TwilightForestMod.prefix("feature/druid_hut/basement_gallery_trap"));

		private final ResourceLocation resourceLocation;
		private final ResourceLocation resourceLocationTrap;

		BasementType(ResourceLocation rl, ResourceLocation rlTrap) {
			this.resourceLocation = rl;
			this.resourceLocationTrap = rlTrap;
			increment();
		}

		private static int size;

		private static void increment() {
			++size;
		}

		private ResourceLocation getBasement(boolean trapped) {
			return trapped ? resourceLocationTrap : resourceLocation;
		}
	}
}
