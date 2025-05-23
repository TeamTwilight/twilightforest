package twilightforest.world.components.structures.darktower;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.components.structures.lichtower.TowerRoofComponent;
import twilightforest.world.components.structures.lichtower.TowerWingComponent;

public class DarkTowerRoofComponent extends TowerRoofComponent {

	public DarkTowerRoofComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		this(TFStructurePieceTypes.TFDTRooS.get(), nbt);
	}

	public DarkTowerRoofComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
	}

	public DarkTowerRoofComponent(StructurePieceType piece, int i, TowerWingComponent wing, int x, int y, int z) {
		super(piece, i, x, y, z);

		// same alignment
		this.setOrientation(wing.getOrientation());
		// same size
		this.size = wing.size; // assuming only square towers and roofs right now.
		this.height = 12;

		// just hang out at the very top of the tower
		makeCapBB(wing);

		// spawn list!
		this.spawnListIndex = 1;
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}
	}

	/**
	 * A fence around the roof!
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// fence
		for (int x = 0; x <= size - 1; x++) {
			for (int z = 0; z <= size - 1; z++) {
				if (x == 0 || x == size - 1 || z == 0 || z == size - 1) {
					placeBlock(world, deco.fenceState, x, 1, z, sbb);
				}
			}
		}

		placeBlock(world, deco.accentState, 0, 1, 0, sbb);
		placeBlock(world, deco.accentState, size - 1, 1, 0, sbb);
		placeBlock(world, deco.accentState, 0, 1, size - 1, sbb);
		placeBlock(world, deco.accentState, size - 1, 1, size - 1, sbb);
	}
}
