package twilightforest.world.components.structures.darktower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.components.structures.lichtower.TowerWingComponent;

public class DarkTowerBridgeComponent extends TowerWingComponent {

	public DarkTowerBridgeComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
	}

	public DarkTowerBridgeComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFDTBri.get(), nbt);
	}

	private int dSize;
	private int dHeight;

	protected DarkTowerBridgeComponent(StructurePieceType type, int i, int x, int y, int z, int pSize, int pHeight, Direction direction) {
		super(type, i, x, y, z, 5, 5, direction);

		this.dSize = pSize;
		this.dHeight = pHeight;
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}
		makeTowerWing(list, rand, this.getGenDepth(), 4, 1, 2, dSize, dHeight, Rotation.NONE);
	}

	@Override
	public boolean makeTowerWing(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		// kill too-small towers
		if (wingHeight < 6) {
			return false;
		}

		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, wingSize, direction);

		if (dx[1] + wingHeight > 255) {
			// end of the world!
			return false;
		}

		TowerWingComponent wing = new DarkTowerWingComponent(TFStructurePieceTypes.TFDTWin.get(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// check to see if it intersects something already there
		StructurePiece intersect = list.findCollisionPiece(wing.getBoundingBox());
		if (intersect == null || intersect == this) {
			list.addPiece(wing);
			wing.addChildren(this, list, rand);
			addOpening(x, y, z, rotation);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// make walls
		generateBox(world, sbb, 0, 0, 0, size - 1, height - 1, size - 1, deco.blockState, deco.blockState, false);

		// accents
		for (int x = 0; x < size; x++) {
			this.placeBlock(world, deco.accentState, x, 0, 0, sbb);
			this.placeBlock(world, deco.accentState, x, height - 1, 0, sbb);
			this.placeBlock(world, deco.accentState, x, 0, size - 1, sbb);
			this.placeBlock(world, deco.accentState, x, height - 1, size - 1, sbb);
		}

		// nullify sky light
//		nullifySkyLightForBoundingBox(world.getWorld());

		// clear inside
		generateAirBox(world, sbb, 0, 1, 1, size - 1, height - 2, size - 2);
	}

	/**
	 * Gets the bounding box of the tower wing we would like to make.
	 *
     */
	public BoundingBox getWingBB() {
		int[] dest = offsetTowerCoords(4, 1, 2, dSize, this.getOrientation());
		return BoundingBoxUtils.getComponentToAddBoundingBox(dest[0], dest[1], dest[2], 0, 0, 0, dSize - 1, dHeight - 1, dSize - 1, this.getOrientation(), false);
	}
}
