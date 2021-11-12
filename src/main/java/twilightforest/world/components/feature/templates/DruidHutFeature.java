package twilightforest.world.components.feature.templates;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;
import twilightforest.loot.TFTreasure;
import twilightforest.world.components.processors.CobblePlankSwizzler;
import twilightforest.world.components.processors.CobbleVariants;
import twilightforest.world.components.processors.StoneBricksVariants;

import java.util.Random;

public class DruidHutFeature extends TemplateFeature<NoneFeatureConfiguration> {
	public DruidHutFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	@Override
    protected StructureTemplate getTemplate(StructureManager templateManager, Random random) {
	    return templateManager.getOrCreate(Util.getRandom(DruidHutFeature.HutType.values(), random).resourceLocation);
    }

    @Override
    protected void modifySettings(StructurePlaceSettings settings, Random random) {
        settings.addProcessor(new CobblePlankSwizzler(random)).addProcessor(CobbleVariants.INSTANCE).addProcessor(StoneBricksVariants.INSTANCE);
    }

    @Override
    protected void postPlacement(WorldGenLevel world, Random random, StructureManager templateManager, Rotation rotation, Mirror mirror, StructurePlaceSettings placementSettings, BlockPos placementPos) {
        if (random.nextBoolean()) {
            StructureTemplate template = templateManager.getOrCreate(DruidHutFeature.BasementType.values()[random.nextInt(DruidHutFeature.BasementType.size)].getBasement(random.nextBoolean()));

            if(template == null) return;

            placementPos = placementPos.below(12).relative(rotation.rotate(mirror.mirror(Direction.NORTH)), 1).relative(rotation.rotate(mirror.mirror(Direction.EAST)), 1);

            template.placeInWorld(world, placementPos, placementPos, placementSettings.clearProcessors().addProcessor(new CobblePlankSwizzler(random)).addProcessor(CobbleVariants.INSTANCE).addProcessor(StoneBricksVariants.INSTANCE), random, 20);

            for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(placementPos, placementSettings, Blocks.STRUCTURE_BLOCK))
                if (info.nbt != null && StructureMode.valueOf(info.nbt.getString("mode")) == StructureMode.DATA)
                    this.processMarkers(info, world, rotation, mirror, random);
        }
    }

	@Override
	protected void processMarkers(StructureTemplate.StructureBlockInfo info, WorldGenLevel world, Rotation rotation, Mirror mirror, Random random) {
        String s = info.nbt.getString("metadata");
        BlockPos blockPos = info.pos;
        /**
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
            if (world.removeBlock(blockPos, false) && world.setBlock(blockPos, Blocks.SPAWNER.defaultBlockState(), 16 | 2)) {
                BlockEntity tile = world.getBlockEntity(blockPos);

                if (tile instanceof SpawnerBlockEntity ms) {
                    ms.getSpawner().setEntityId(TFEntities.SKELETON_DRUID);
                }
            }
        } else if (s.startsWith("loot")) {
            world.removeBlock(blockPos, false);
            BlockState chest = s.endsWith("T") ? Blocks.TRAPPED_CHEST.defaultBlockState() : Blocks.CHEST.defaultBlockState();

            switch (s.substring(5, 6)) {
                case "L":
                    chest = chest.setValue(ChestBlock.TYPE, mirror != Mirror.NONE ? ChestType.RIGHT : ChestType.LEFT);
                    break;
                case "R":
                    chest = chest.setValue(ChestBlock.TYPE, mirror != Mirror.NONE ? ChestType.LEFT : ChestType.RIGHT);
                    break;
                default:
                    chest = chest.setValue(ChestBlock.TYPE, ChestType.SINGLE);
                    break;
            }

            switch (s.substring(4, 5)) {
                case "W":
                    chest = chest.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(mirror.mirror(Direction.WEST)));
                    break;
                case "E":
                    chest = chest.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(mirror.mirror(Direction.EAST)));
                    break;
                case "S":
                    chest = chest.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(mirror.mirror(Direction.SOUTH)));
                    break;
                default:
                    chest = chest.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(mirror.mirror(Direction.NORTH)));
                    break;
            }

            TFTreasure.BASEMENT.generateLootContainer(world, blockPos, chest, 16 | 2);
        }
    }

    private enum HutType {
        REGULAR    (TwilightForestMod.prefix("feature/druid_hut/druid_hut"       )),
        SIDEWAYS   (TwilightForestMod.prefix("feature/druid_hut/druid_sideways"  )),
        DOUBLE_DECK(TwilightForestMod.prefix("feature/druid_hut/druid_doubledeck"));

        private final ResourceLocation resourceLocation;

        HutType(ResourceLocation rl) {
            this.resourceLocation = rl;
        }
    }

    private enum BasementType {
        STUDY  (TwilightForestMod.prefix("feature/druid_hut/basement_study"  ), TwilightForestMod.prefix("feature/druid_hut/basement_study_trap"  )),
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
