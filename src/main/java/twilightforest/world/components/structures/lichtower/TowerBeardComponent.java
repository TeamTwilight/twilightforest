package twilightforest.world.components.structures.lichtower;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.structures.TFStructureComponentOld;


public class TowerBeardComponent extends TFStructureComponentOld {

	final int size;
	final int height;

	public TowerBeardComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		this(TFStructurePieceTypes.TFLTBea.get(), nbt);
	}

	public TowerBeardComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
		this.size = nbt.getInt("beardSize");
		this.height = nbt.getInt("beardHeight");
	}

	public TowerBeardComponent(StructurePieceType piece, int i, TowerWingComponent wing, int x, int y, int z) {
		super(piece, i, x, y, z);

		this.setOrientation(wing.getOrientation());
		this.size = wing.size - 2;
		this.height = size / 2;

		// just hang out at the very bottom of the tower
		this.boundingBox = new BoundingBox(wing.getBoundingBox().minX() + 1, wing.getBoundingBox().minY() - this.height - 1, wing.getBoundingBox().minZ() + 1, wing.getBoundingBox().maxX() - 1, wing.getBoundingBox().minY() - 1, wing.getBoundingBox().maxZ() - 1);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		tagCompound.putInt("beardSize", this.size);
		tagCompound.putInt("beardHeight", this.height);
	}

	/**
	 * Makes a pyramid-shaped beard
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		makePyramidBeard(world, rand, sbb);
	}

	private void makePyramidBeard(WorldGenLevel world, RandomSource rand, BoundingBox sbb) {
		for (int y = 0; y <= height; y++) {
			int min = y;
			int max = size - y - 1;

			generateBox(world, sbb, min, height - y, min, max, height - y, max, false, rand, TFStructureComponentOld.getStrongholdStones());
		}
	}
}
