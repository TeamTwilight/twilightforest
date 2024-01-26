package twilightforest.world.components.structures.trollcave;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import org.jetbrains.annotations.NotNull;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;

public class CloudComponent extends StructurePiece {
	private static final int OFFSET = 16 * -2;
	private static final int WIDTH = 16 * 6;
	private static final int DEPTH = 8;

	public CloudComponent(int genDepth, int centerChunkX, int yHeight, int centerChunkZ) {
		super(TFStructurePieceTypes.TFCloud.value(), genDepth, makeBoundingBox((centerChunkX + OFFSET) & ~0b1111, yHeight, (centerChunkZ + OFFSET) & ~0b1111));
	}

	@NotNull
	private static BoundingBox makeBoundingBox(int minX, int maxY, int minZ) {
		return new BoundingBox(minX & ~0b1111, maxY - DEPTH, minZ & ~0b1111, (minX + WIDTH) | 0b1111, maxY, (minZ + WIDTH) | 0b1111);
	}

	public CloudComponent(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(TFStructurePieceTypes.TFCloud.value(), tag);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource randomSource, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos structureBottomCenter) {
		BlockPos chunkWorldPos = chunkPos.getWorldPosition();

		int genCenterX = this.boundingBox.minX() - OFFSET - chunkWorldPos.getX() + 16;
		int genCenterZ = this.boundingBox.minZ() - OFFSET - chunkWorldPos.getZ() + 16;

		generateTrollCloud(level.getChunk(chunkPos.x, chunkPos.z), genCenterX, genCenterZ, this.boundingBox.maxY(), randomSource);
	}

	private static void generateTrollCloud(ChunkAccess chunkAccess, int hx, int hz, int cloudHeight, RandomSource random) {
		ChunkPos center = chunkAccess.getPos();
		BlockPos chunkBlockPos = center.getWorldPosition();

		BlockState wispyCloud = TFBlocks.WISPY_CLOUD.get().defaultBlockState();
		BlockState fluffyCloud = TFBlocks.FLUFFY_CLOUD.get().defaultBlockState();

		int regionX = center.x + 8 >> 4;
		int regionZ = center.z + 8 >> 4;

		long seed = regionX * 3129871L ^ regionZ * 116129781L;
		seed = seed * seed * 42317861L + seed * 7L;

		int num0 = 5 * (int) (seed >> 12 & 3L) - 4 * (int) (seed >> 15 & 3L);
		int num1 = 4 * (int) (seed >> 18 & 3L) - 5 * (int) (seed >> 21 & 3L);
		int num2 = 5 * (int) (seed >> 9 & 3L) - 4 * (int) (seed >> 6 & 3L);
		int num3 = 4 * (int) (seed >> 3 & 3L) - 5 * (int) (seed & 3L);

		for (int bx = 0; bx < 4; bx++) {
			int dx = bx * 4 - hx - 2;

			int dx2 = dx + num0;
			int dx3 = dx + num2;

			for (int bz = 0; bz < 4; bz++) {
				int dz = bz * 4 - hz - 2;

				int dz2 = dz + num1;
				int dz3 = dz + num3;

				// take the minimum distance to any center
				float dist0 = Mth.sqrt(dx * dx + dz * dz) / 4.0f;
				float dist2 = Mth.sqrt(dx2 * dx2 + dz2 * dz2) / 3.5f;
				float dist3 = Mth.sqrt(dx3 * dx3 + dz3 * dz3) / 4.5f;

				double dist = Math.min(dist0, Math.min(dist2, dist3));

				float pr = random.nextFloat();
				double cv = dist - 7F - pr * 3.0F;

				// randomize depth and height
				int y = cloudHeight;
				int depth = 4;

				if (pr < 0.1F) {
					y++;
				}
				if (pr > 0.6F) {
					depth++;
				}
				if (pr > 0.9F) {
					depth++;
				}

				// generate cloud
				gen4x4Cloud(chunkAccess, bx, bz, chunkBlockPos, dist, cv, y, wispyCloud, depth, fluffyCloud);
			}
		}
	}

	private static void gen4x4Cloud(ChunkAccess chunkAccess, int bx, int bz, BlockPos chunkBlockPos, double dist, double cv, int y, BlockState wispyCloud, int depth, BlockState fluffyCloud) {
		for (int sx = 0; sx < 4; sx++) {
			int lx = bx * 4 + sx;

			for (int sz = 0; sz < 4; sz++) {
				int lz = bz * 4 + sz;

				BlockPos columnPos = chunkBlockPos.offset(lx, 0, lz);

				if (dist < 7 || cv < 0.05F) {
					setIfAir(chunkAccess, columnPos.atY(y), wispyCloud);

					for (int d = 1; d < depth; d++) {
						setIfAir(chunkAccess, columnPos.atY(y - d), fluffyCloud);
					}

					setIfAir(chunkAccess, columnPos.atY(y - depth), wispyCloud);
				} else if (dist < 8 || cv < 1F) {
					for (int d = 1; d < depth; d++) {
						setIfAir(chunkAccess, columnPos.atY(y - d), wispyCloud);
					}
				}
			}
		}
	}

	private static void setIfAir(ChunkAccess chunkAccess, BlockPos topPos, BlockState blockState) {
		if (chunkAccess.getBlockState(topPos).isAir()) {
			chunkAccess.setBlockState(topPos, blockState, false);
		}
	}
}
