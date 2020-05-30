package twilightforest.features;

import com.google.common.math.StatsAccumulator;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import net.minecraft.world.server.ServerWorld;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;
import twilightforest.loot.TFTreasure;
import twilightforest.structures.RandomizedTemplateProcessor;
import twilightforest.structures.TFStructureProcessors;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;

public class GenDruidHut<T extends NoFeatureConfig> extends Feature<T> {

	public GenDruidHut(Function<Dynamic<?>, ? extends T> renderer) {
		super(renderer);
	}

	@Override // Loosely based on WorldGenFossils
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, T config) {
		//Random random = world.getChunk(pos).getRandomWithSeed(987234911L);
		Random random = world.getRandom();

		TemplateManager templatemanager = ((ServerWorld)world).getSaveHandler().getStructureTemplateManager();
		Template template = templatemanager.getTemplate(HutType.values()[random.nextInt(HutType.size)].RL);

		Rotation[] rotations = Rotation.values();
		Rotation rotation = rotations[random.nextInt(rotations.length)];

		Mirror[] mirrors = Mirror.values();
		Mirror mirror = mirrors[random.nextInt(mirrors.length+1) % mirrors.length];

		ChunkPos chunkpos = new ChunkPos(pos.add(-8, 0, -8));
		MutableBoundingBox structureboundingbox = new MutableBoundingBox(chunkpos.getXStart() + 8, 0, chunkpos.getZStart() + 8, chunkpos.getXEnd() + 8, 255, chunkpos.getZEnd() + 8);
		PlacementSettings placementsettings = (new PlacementSettings()).setMirror(mirror).setRotation(rotation).setBoundingBox(structureboundingbox).setRandom(random);

		BlockPos posSnap = chunkpos.getBlock(8, pos.getY() - 1, 8);

		BlockPos transformedSize = template.transformedSize(rotation);
		int dx = random.nextInt(17 - transformedSize.getX());
		int dz = random.nextInt(17 - transformedSize.getZ());

		BlockPos.Mutable startPos = new BlockPos.Mutable(posSnap.add(dx, 0, dz));

		if (!offsetToAverageGroundLevel(world.getWorld(), startPos, transformedSize)) {
			return false;
		}

		BlockPos placementPos = template.getZeroPositionWithTransform(startPos, mirror, rotation);
		template.addBlocksToWorld(world, placementPos, placementsettings.addProcessor(new HutTemplateProcessor(0.2F)), 20);

		if (random.nextBoolean()) {
			template = templatemanager.getTemplate(BasementType.values()[random.nextInt(BasementType.size)].getBasement(random.nextBoolean()));
			placementPos = placementPos.down(12).offset(rotation.rotate(mirror.mirror(Direction.NORTH)), 1).offset(rotation.rotate(mirror.mirror(Direction.EAST)), 1);

			template.addBlocksToWorld(world, placementPos, placementsettings.addProcessor(new HutTemplateProcessor(0.2F)), 20);
		}

		return true;
	}

    private static boolean offsetToAverageGroundLevel(World world, BlockPos.Mutable startPos, BlockPos size) {
        StatsAccumulator heights = new StatsAccumulator();

        for (int dx = 0; dx < size.getX(); dx++) {
            for (int dz = 0; dz < size.getZ(); dz++) {

                int x = startPos.getX() + dx;
                int z = startPos.getZ() + dz;

                int y = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);

                while (y >= 0) {
                    BlockState state = world.getBlockState(new BlockPos(x, y, z));
                    if (isBlockNotOk(state)) return false;
                    if (isBlockOk(state)) break;
                    y--;
                }

                if (y < 0) return false;

                heights.add(y);
            }
        }

        if (heights.populationStandardDeviation() > 2.0) {
            return false;
        }

        int baseY = (int) Math.round(heights.mean());
        int maxY = (int) heights.max();

        startPos.setY(baseY);

        return isAreaClear(world, startPos.up(maxY - baseY + 1), startPos.add(size));
    }

    private static boolean isAreaClear(IWorld world, BlockPos min, BlockPos max) {
        for (BlockPos pos : BlockPos.getAllInBoxMutable(min, max)) {
            if (!world.getBlockState(pos).getMaterial().isReplaceable()) {
                return false;
            }
        }
        return true;
    }

    private static boolean isBlockOk(BlockState state) {
        Material material = state.getMaterial();
        return material == Material.ROCK || material == Material.EARTH || material == Material.ORGANIC || material == Material.SAND;
    }

    private static boolean isBlockNotOk(BlockState state) {
        Material material = state.getMaterial();
        return material == Material.WATER || material == Material.LAVA || state.getBlock() == Blocks.BEDROCK;
    }

    public static class DruidHutPieces {

		public static class Piece extends TemplateStructurePiece {

			public Piece(IStructurePieceType p_i51338_1_, int p_i51338_2_) {
				super(p_i51338_1_, p_i51338_2_);
			}

			@Override
			protected void handleDataMarker(String s, BlockPos blockPos, IWorld world, Random random, MutableBoundingBox mutableBoundingBox) {
   		        /*
   		        `spawner` will place a Druid spawner.

   		        `loot` will place a chest facing the was-North.

   		        `lootT` will place a trapped chest facing the was-North.

   		        `lootW` will place a chest facing the was-West.
   		        `lootS` will place a chest facing the was-South.

   		        `lootET` will place a trapped chest facing the was-East.
   		        `lootNT` will place a trapped chest facing the was-North.
   		         */
				if ("spawner".equals(s)) {
					if (world.setBlockState(blockPos, Blocks.SPAWNER.getDefaultState(), 16 | 2)) {
						MobSpawnerTileEntity ms = (MobSpawnerTileEntity) world.getTileEntity(blockPos);

						if (ms != null) {
							ms.getSpawnerBaseLogic().setEntityType(TFEntities.skeleton_druid.get());
						}
					}
				} else if (s.startsWith("loot")) {
					BlockState chest = s.endsWith("T") ? Blocks.TRAPPED_CHEST.getDefaultState() : Blocks.CHEST.getDefaultState();

					switch (s.substring(4, 5)) {
						case "W":
							chest = chest.with(HorizontalBlock.HORIZONTAL_FACING, rotation.rotate(mirror.mirror(Direction.WEST)));
							break;
						case "E":
							chest = chest.with(HorizontalBlock.HORIZONTAL_FACING, rotation.rotate(mirror.mirror(Direction.EAST)));
							break;
						case "S":
							chest = chest.with(HorizontalBlock.HORIZONTAL_FACING, rotation.rotate(mirror.mirror(Direction.SOUTH)));
							break;
						default:
							chest = chest.with(HorizontalBlock.HORIZONTAL_FACING, rotation.rotate(mirror.mirror(Direction.NORTH)));
							break;
					}

					if (world.setBlockState(blockPos, chest, 16 | 2)) {
						TFTreasure.basement.generateChestContents(world.getWorld(), blockPos);
					}
				}
			}
		}

	}

    private enum HutType {

        REGULAR    (TwilightForestMod.prefix("landscape/druid_hut/druid_hut"       )),
        SIDEWAYS   (TwilightForestMod.prefix("landscape/druid_hut/druid_sideways"  )),
        DOUBLE_DECK(TwilightForestMod.prefix("landscape/druid_hut/druid_doubledeck"));

        private final ResourceLocation RL;

        HutType(ResourceLocation rl) {
            this.RL = rl;
            increment();
        }

        private static int size;

        private static void increment() {
            ++size;
        }
    }

    private enum BasementType {

        STUDY  (TwilightForestMod.prefix("landscape/druid_hut/basement_study"  ), TwilightForestMod.prefix("landscape/druid_hut/basement_study_trap"  )),
        SHELVES(TwilightForestMod.prefix("landscape/druid_hut/basement_shelves"), TwilightForestMod.prefix("landscape/druid_hut/basement_shelves_trap")),
        GALLERY(TwilightForestMod.prefix("landscape/druid_hut/basement_gallery"), TwilightForestMod.prefix("landscape/druid_hut/basement_gallery_trap"));

        private final ResourceLocation RL;
        private final ResourceLocation RL_TRAP;

        BasementType(ResourceLocation rl, ResourceLocation rlTrap) {
            this.RL = rl;
            this.RL_TRAP = rlTrap;
            increment();
        }

        private static int size;

        private static void increment() {
            ++size;
        }

        private ResourceLocation getBasement(boolean trapped) {
            return trapped ? RL_TRAP : RL;
        }
    }

    public static class HutTemplateProcessor extends RandomizedTemplateProcessor {

        public HutTemplateProcessor(float integrity) {
            super(integrity);
        }

        public HutTemplateProcessor(Dynamic<?> dynamic) {
        	super(dynamic.get("integrity").asFloat(1.0F));
		}

		@Override
		protected IStructureProcessorType getType() {
			return TFStructureProcessors.HUT;
		}

		@Nullable
		@Override
		public Template.BlockInfo process(IWorldReader worldIn, BlockPos pos, Template.BlockInfo p_215194_3_, Template.BlockInfo blockInfo, PlacementSettings settings, @Nullable Template template) {
			//if (!shouldPlaceBlock()) return null;

			Random random = settings.getRandom(pos);
			BlockState state = blockInfo.state;
			Block block = state.getBlock();

			if (block == Blocks.COBBLESTONE)
				return random.nextBoolean() ? blockInfo : new Template.BlockInfo(pos, Blocks.MOSSY_COBBLESTONE.getDefaultState(), null);

			if (block == Blocks.COBBLESTONE_WALL)
				return random.nextBoolean() ? blockInfo : new Template.BlockInfo(pos, Blocks.MOSSY_COBBLESTONE_WALL.getDefaultState(), null);

			if (block == Blocks.STONE_BRICKS) { // TODO: By default it's not chiseled stone as that's a different block
				return random.nextBoolean() ? blockInfo : new Template.BlockInfo(pos, random.nextInt(2) == 0 ? Blocks.CRACKED_STONE_BRICKS.getDefaultState() : Blocks.MOSSY_STONE_BRICKS.getDefaultState(), null);
			}

			return blockInfo;
		}
    }
}
