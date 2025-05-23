package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.structures.TFStructureComponentOld;


public class FinalCastleDungeonStepsComponent extends TFStructureComponentOld {

	public FinalCastleDungeonStepsComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCDunSt.get(), nbt);
	}

	public FinalCastleDungeonStepsComponent(int i, int x, int y, int z, Direction rotation) {
		super(TFStructurePieceTypes.TFFCDunSt.get(), i, x, y, z);
		this.spawnListIndex = 2; // dungeon monsters

		this.setOrientation(rotation);
		this.boundingBox = TFStructureComponentOld.getComponentToAddBoundingBox2(x, y, z, -2, -15, -3, 5, 15, 20, rotation);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}
	}

	/**
	 * build more steps towards the specified direction
	 */
	public FinalCastleDungeonStepsComponent buildMoreStepsTowards(StructurePieceAccessor list, RandomSource rand, Rotation rotation) {

		Direction direction = getStructureRelativeRotation(rotation);

		int sx = 2;
		int sy = 0;
		int sz = 17;

		switch (rotation) {
			case NONE -> sz -= 5;
			case CLOCKWISE_90 -> sx -= 5;
			case CLOCKWISE_180 -> sz += 5;
			case COUNTERCLOCKWISE_90 -> sx += 6;
		}

		// find center of landing
		int dx = this.getWorldX(sx, sz);
		int dy = this.getWorldY(sy);
		int dz = this.getWorldZ(sx, sz);


		// build a new stairway there
		FinalCastleDungeonStepsComponent steps = new FinalCastleDungeonStepsComponent(this.genDepth + 1, dx, dy, dz, direction);
		list.addPiece(steps);
		steps.addChildren(this, list, rand);

		return steps;
	}

	/**
	 * build a new level under the exit
	 */
	public FinalCastleDungeonEntranceComponent buildLevelUnder(StructurePieceAccessor list, RandomSource rand, int level) {
		// find center of landing
		int dx = this.getWorldX(2, 19);
		int dy = this.getWorldY(-7);
		int dz = this.getWorldZ(2, 19);

		// build a new dungeon level under there
		FinalCastleDungeonEntranceComponent room = new FinalCastleDungeonEntranceComponent(8, dx, dy, dz, getOrientation(), level);
		list.addPiece(room);
		room.addChildren(this, list, rand);

		return room;
	}

	/**
	 * build the boss room
	 */
	public FinalCastleDungeonForgeRoomComponent buildBossRoomUnder(StructurePieceAccessor list, RandomSource rand) {
		// find center of landing
		int dx = this.getWorldX(2, 19);
		int dy = this.getWorldY(-31);
		int dz = this.getWorldZ(2, 19);

		// build a new dungeon level under there
		FinalCastleDungeonForgeRoomComponent room = new FinalCastleDungeonForgeRoomComponent(8, dx, dy, dz, this.getOrientation());
		list.addPiece(room);
		room.addChildren(this, list, rand);

		return room;
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		final BlockState stairState = deco.stairState.setValue(StairBlock.FACING, Direction.SOUTH);
		for (int z = 0; z < 15; z++) {
			int y = 14 - z;

			this.generateBox(world, sbb, 0, y, z, 4, y, z, stairState, stairState, false);
			this.generateAirBox(world, sbb, 0, y + 1, z, 4, y + 6, z);
		}
		this.generateAirBox(world, sbb, 0, 0, 15, 4, 5, 19);
	}
}
