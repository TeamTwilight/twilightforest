package twilightforest.worldgen.structures;

import com.google.common.math.StatsAccumulator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.ChestType;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.*;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.*;

import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;
import twilightforest.enums.StructureWoodVariant;
import twilightforest.loot.TFTreasure;
import twilightforest.structures.RandomizedTemplateProcessor;
import twilightforest.structures.TFStructureProcessors;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenDruidHut extends Feature<NoFeatureConfig> {

	public GenDruidHut(Codec<NoFeatureConfig> config) {
		super(config);
	}

	@Override // Loosely based on WorldGenFossils FIXME See if we can move this over to purely-datadriven
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		//Random random = world.getChunk(pos).getRandomWithSeed(987234911L);
		Random random = world.getRandom();

		TemplateManager templatemanager = world.getWorld().getServer().getTemplateManager();
		Template template = templatemanager.getTemplate(HutType.values()[random.nextInt(HutType.size)].RL);
		if(template == null)
			return false;

		Rotation[] rotations = Rotation.values();
		Rotation rotation = rotations[random.nextInt(rotations.length)];

		Mirror[] mirrors = Mirror.values();
		Mirror mirror = mirrors[random.nextInt(mirrors.length+1) % mirrors.length];

		ChunkPos chunkpos = new ChunkPos(pos.add(-8, 0, -8));
		MutableBoundingBox structureboundingbox = new MutableBoundingBox(chunkpos.getXStart() + 8, 0, chunkpos.getZStart() + 8, chunkpos.getXEnd() + 8, 255, chunkpos.getZEnd() + 8);
		PlacementSettings placementsettings = (new PlacementSettings()).setMirror(mirror).setRotation(rotation).setBoundingBox(structureboundingbox).setRandom(random);

		BlockPos posSnap = chunkpos.asBlockPos().add(8, pos.getY() - 1, 8); // Verify this is correct. Originally chunkpos.getBlock(8, pos.getY() - 1, 8);

		BlockPos transformedSize = template.transformedSize(rotation);
		int dx = random.nextInt(17 - transformedSize.getX());
		int dz = random.nextInt(17 - transformedSize.getZ());
		posSnap.add(dx, 0, dz);

		BlockPos.Mutable startPos = new BlockPos.Mutable(posSnap.getX(), posSnap.getY(), posSnap.getZ());

		if (!offsetToAverageGroundLevel(world, startPos, transformedSize)) {
			return false;
		}

		BlockPos placementPos = template.getZeroPositionWithTransform(startPos, mirror, rotation);
		template.func_237146_a_(world, placementPos, placementPos, placementsettings.clearProcessors().addProcessor(new HutTemplateProcessor(0.0F, rand.nextInt(), rand.nextInt(), rand.nextInt())), random, 20);
		List<Template.BlockInfo> data = new ArrayList<>(template.func_215381_a(placementPos, placementsettings, Blocks.STRUCTURE_BLOCK));

		if (random.nextBoolean()) {
			template = templatemanager.getTemplate(BasementType.values()[random.nextInt(BasementType.size)].getBasement(random.nextBoolean()));
			if(template == null)
				return false;
			placementPos = placementPos.down(12).offset(rotation.rotate(mirror.mirror(Direction.NORTH)), 1).offset(rotation.rotate(mirror.mirror(Direction.EAST)), 1);

			template.func_237146_a_(world, placementPos, placementPos, placementsettings.clearProcessors().addProcessor(new HutTemplateProcessor(0.0F, rand.nextInt(14), rand.nextInt(14), rand.nextInt(14))), random, 20);
			data.addAll(template.func_215381_a(placementPos, placementsettings, Blocks.STRUCTURE_BLOCK));
		}

		data.forEach(info -> {
			if (info.nbt != null && StructureMode.valueOf(info.nbt.getString("mode")) == StructureMode.DATA) {
				String s = info.nbt.getString("metadata");
				BlockPos blockPos = info.pos;
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
					if (world.removeBlock(blockPos, false) && world.setBlockState(blockPos, Blocks.SPAWNER.getDefaultState(), 16 | 2)) {
						MobSpawnerTileEntity ms = (MobSpawnerTileEntity) world.getTileEntity(blockPos);

						if (ms != null) {
							ms.getSpawnerBaseLogic().setEntityType(TFEntities.skeleton_druid);
						}
					}
				} else if (s.startsWith("loot")) {
					world.removeBlock(blockPos, false);
					BlockState chest = s.endsWith("T") ? Blocks.TRAPPED_CHEST.getDefaultState() : Blocks.CHEST.getDefaultState();

					switch (s.substring(5, 6)) {
						case "L":
							chest = chest.with(ChestBlock.TYPE, mirror != Mirror.NONE ? ChestType.RIGHT : ChestType.LEFT);
							break;
						case "R":
							chest = chest.with(ChestBlock.TYPE, mirror != Mirror.NONE ? ChestType.LEFT : ChestType.RIGHT);
							break;
						default:
							chest = chest.with(ChestBlock.TYPE, ChestType.SINGLE);
							break;
					}

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
						TFTreasure.basement.generateChestContents(world, blockPos);
					}
				}
			}
		});

		return true;
	}

    private static boolean offsetToAverageGroundLevel(ISeedReader world, BlockPos.Mutable startPos, BlockPos size) {
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

		private final StructureWoodVariant OAK_SWIZZLE;
		private final StructureWoodVariant SPRUCE_SWIZZLE;
		private final StructureWoodVariant BIRCH_SWIZZLE;
		public int dummy = 0;
		public static final Codec<HutTemplateProcessor> codecHutProcessor = RecordCodecBuilder.create((instance) ->
				instance.group(
					Codec.FLOAT.fieldOf("integrity").orElse(1.0F).forGetter((obj) -> obj.integrity),
					Codec.INT.fieldOf("integrity").orElse(0).forGetter((obj) -> obj.dummy),
					Codec.INT.fieldOf("integrity").orElse(0).forGetter((obj) -> obj.dummy),
					Codec.INT.fieldOf("integrity").orElse(0).forGetter((obj) -> obj.dummy))
				.apply(instance, HutTemplateProcessor::new));



		public HutTemplateProcessor(float integrity, int oakSwizzle, int spruceSwizzle, int birchSwizzle) {
            super(integrity);
			int limit = StructureWoodVariant.values().length;
			this.OAK_SWIZZLE    = StructureWoodVariant.values()[ Math.floorMod(oakSwizzle   , limit) ];
			this.SPRUCE_SWIZZLE = StructureWoodVariant.values()[ Math.floorMod(spruceSwizzle, limit) ];
			this.BIRCH_SWIZZLE  = StructureWoodVariant.values()[ Math.floorMod(birchSwizzle , limit) ];
        }

		@Override
		protected IStructureProcessorType<?> getType() {
			return TFStructureProcessors.HUT;
		}

		@Nullable
		@Override
		public Template.BlockInfo process(IWorldReader worldIn, BlockPos pos, BlockPos piecepos, Template.BlockInfo p_215194_3_, Template.BlockInfo blockInfo, PlacementSettings settings, @Nullable Template template) {

			Random random = settings.getRandom(pos);

			if (!shouldPlaceBlock(random)) return null;

			BlockState state = blockInfo.state;
			Block block = state.getBlock();

			if (block == Blocks.COBBLESTONE)
				return random.nextBoolean() ? blockInfo : new Template.BlockInfo(blockInfo.pos, Blocks.MOSSY_COBBLESTONE.getDefaultState(), null);

			if (block == Blocks.COBBLESTONE_WALL)
				return random.nextBoolean() ? blockInfo : new Template.BlockInfo(blockInfo.pos, Blocks.MOSSY_COBBLESTONE_WALL.getDefaultState(), null);

			if (block == Blocks.STONE_BRICKS) { // TODO: By default it's not chiseled stone as that's a different block
				return random.nextBoolean() ? blockInfo : new Template.BlockInfo(blockInfo.pos, random.nextInt(2) == 0 ? Blocks.CRACKED_STONE_BRICKS.getDefaultState() : Blocks.MOSSY_STONE_BRICKS.getDefaultState(), null);
			}

			StructureWoodVariant type = StructureWoodVariant.getVariantFromBlock(block);
			if (type != null) {
				switch (type) {
					case OAK:
						return new Template.BlockInfo(blockInfo.pos, StructureWoodVariant.modifyBlockWithType(state, OAK_SWIZZLE   ), null);
					case SPRUCE:
						return new Template.BlockInfo(blockInfo.pos, StructureWoodVariant.modifyBlockWithType(state, SPRUCE_SWIZZLE), null);
					case BIRCH:
						return new Template.BlockInfo(blockInfo.pos, StructureWoodVariant.modifyBlockWithType(state, BIRCH_SWIZZLE ), null);
				}
			}

			return blockInfo;
		}
    }
}
