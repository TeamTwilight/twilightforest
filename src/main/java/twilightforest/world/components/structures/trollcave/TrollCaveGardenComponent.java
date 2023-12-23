package twilightforest.world.components.structures.trollcave;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFConfiguredFeatures;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.structures.StructureSpeleothemConfig;

import java.util.function.Predicate;


public class TrollCaveGardenComponent extends TrollCaveMainComponent {

	public TrollCaveGardenComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFTCGard.get(), ctx, nbt);
	}

	public TrollCaveGardenComponent(int index, int x, int y, int z, int caveSize, int caveHeight, Direction direction, Holder.Reference<StructureSpeleothemConfig> speleothemConfig) {
		super(TFStructurePieceTypes.TFTCGard.get(), index, x, y, z, speleothemConfig);
		this.size = caveSize;
		this.height = caveHeight;
		this.setOrientation(direction);
		this.boundingBox = TFLandmark.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, size - 1, height - 1, size - 1, direction, false);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		// add a cloud
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		Predicate<Biome> highlands = biome -> biome == world.registryAccess().registryOrThrow(Registries.BIOME).get(TFBiomes.HIGHLANDS);
		if (this.isBoundingBoxOutsideBiomes(world, highlands)) {
			return;
		}

		// clear inside
		hollowCaveMiddle(world, sbb, rand, 0, 0, 0, this.size - 1, this.height - 1, this.size - 1);
		RandomSource decoRNG = RandomSource.create(world.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));

		// treasure!
		makeTreasureCrate(world, sbb);

		// dirt!
		for (int i = 0; i < 24; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generate(world, generator, TFConfiguredFeatures.TROLL_CAVE_DIRT, decoRNG, dest.getX(), 0, dest.getZ(), sbb);
		}

		// mycelium!
		for (int i = 0; i < 16; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generate(world, generator, TFConfiguredFeatures.TROLL_CAVE_MYCELIUM, decoRNG, dest.getX(), 0, dest.getZ(), sbb);
		}

		// uberous!
		for (int i = 0; i < 16; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generate(world, generator, TFConfiguredFeatures.UBEROUS_SOIL_PATCH_SMALL, decoRNG, dest.getX(), 0, dest.getZ(), sbb);

			generateAtSurface(world, generator, TFConfiguredFeatures.UBEROUS_SOIL_PATCH_SMALL, decoRNG, dest.getX(), dest.getZ(), sbb);
		}

		// mushglooms first
		for (int i = 0; i < 8; i++) {
			BlockPos.MutableBlockPos dest = getCoordsInCave(decoRNG);
			setBlockStateRotated(world, Blocks.MYCELIUM.defaultBlockState(), dest.getX(), dest.setY(0).getY(), dest.getZ(), this.rotation, sbb);
			generate(world, generator, TFConfiguredFeatures.BIG_MUSHGLOOM, decoRNG, dest.getX(), dest.setY(1).getY(), dest.getZ(), sbb);
		}

		// mushrooms!
		for (int i = 0; i < 16; i++) {
			BlockPos.MutableBlockPos dest = getCoordsInCave(decoRNG);
			setBlockStateRotated(world, Blocks.MYCELIUM.defaultBlockState(), dest.getX(), dest.setY(0).getY(), dest.getZ(), this.rotation, sbb);
			generate(world, generator, rand.nextBoolean() ? TreeFeatures.HUGE_BROWN_MUSHROOM : TreeFeatures.HUGE_RED_MUSHROOM, decoRNG, dest.getX(), dest.setY(1).getY(), dest.getZ(), sbb);
		}

		this.placeSpeleothems(world, rand, sbb, decoRNG);
	}

	protected void generate(WorldGenLevel world, ChunkGenerator generator, ResourceKey<ConfiguredFeature<?, ?>> feature, RandomSource rand, int x, int y, int z, BoundingBox sbb) {
		// are the coordinates in our bounding box?
		int dx = getWorldX(x, z);
		int dy = getWorldY(y);
		int dz = getWorldZ(x, z);

		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isInside(pos)) {
			world.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).get(feature).place(world, generator, rand, pos);
		}
	}
}
