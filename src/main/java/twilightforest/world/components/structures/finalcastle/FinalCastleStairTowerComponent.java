package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.components.structures.lichtower.TowerWingComponent;


public class FinalCastleStairTowerComponent extends TowerWingComponent {

	public FinalCastleStairTowerComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCStTo.get(), nbt);
	}

	public FinalCastleStairTowerComponent(int i, int x, int y, int z, Direction rotation) {
		super(TFStructurePieceTypes.TFFCStTo.get(), i, x, y, z);
		this.setOrientation(rotation);
		this.size = 9;
		this.height = 51;
		this.boundingBox = BoundingBoxUtils.getComponentToAddBoundingBox(x, y, z, -4, 0, -4, 8, 50, 8, Direction.SOUTH, false);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}
		// add crown
		FinalCastleRoof9CrenellatedComponent roof = new FinalCastleRoof9CrenellatedComponent(4, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(roof);
		roof.addChildren(this, list, rand);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		RandomSource decoRNG = RandomSource.create(world.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));

		generateBox(world, sbb, 0, 0, 0, 8, 49, 8, false, rand, deco.randomBlocks);

		// add branching runes
		int numBranches = 6 + decoRNG.nextInt(4);
		for (int i = 0; i < numBranches; i++) {
			makeGlyphBranches(world, decoRNG, this.getGlyphMeta(), sbb);
		}

		// beard
		for (int i = 1; i < 4; i++) {
			generateBox(world, sbb, i, -(i * 2), i, 8 - i, 1 - (i * 2), 8 - i, false, rand, deco.randomBlocks);
		}
		this.placeBlock(world, deco.blockState, 4, -7, 4, sbb);

		// door, first floor
		final BlockState castleDoor = TFBlocks.YELLOW_CASTLE_DOOR.get().defaultBlockState();
		this.generateBox(world, sbb, 0, 1, 1, 0, 3, 2, castleDoor, AIR, false);

		// stairs
		Rotation rotation = Rotation.CLOCKWISE_90;
		for (int f = 0; f < 5; f++) {
			//int rotation = (f + 2) % 4;
			rotation = rotation.getRotated(Rotation.CLOCKWISE_90);
			int y = f * 3 + 1;
			for (int i = 0; i < 3; i++) {
				int sx = 3 + i;
				int sy = y + i;
				int sz = 1;

				this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, false), sx, sy, sz, rotation, sbb);
				this.setBlockStateRotated(world, deco.blockState, sx, sy - 1, sz, rotation, sbb);
				this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, false), sx, sy, sz + 1, rotation, sbb);
				this.setBlockStateRotated(world, deco.blockState, sx, sy - 1, sz + 1, rotation, sbb);
			}
			// landing
			this.fillBlocksRotated(world, sbb, 6, y + 2, 1, 7, y + 2, 2, deco.blockState, rotation);
		}

		// door, second floor
		this.generateBox(world, sbb, 1, 18, 0, 2, 20, 0, castleDoor, AIR, false);

		BlockState stairState = getStairState(deco.stairState, Direction.SOUTH, false);

		// second floor landing
		this.generateBox(world, sbb, 1, 17, 1, 3, 17, 3, deco.blockState, deco.blockState, false);
		this.generateBox(world, sbb, 1, 17, 4, 2, 17, 4, stairState, stairState, false);
		this.generateBox(world, sbb, 1, 16, 4, 2, 16, 4, deco.blockState, deco.blockState, false);
		this.generateBox(world, sbb, 1, 16, 5, 2, 16, 5, stairState, stairState, false);
		this.generateBox(world, sbb, 1, 15, 5, 2, 15, 5, deco.blockState, deco.blockState, false);

		// door, roof
		this.generateBox(world, sbb, 1, 39, 0, 2, 41, 0, TFBlocks.VIOLET_CASTLE_DOOR.get().defaultBlockState(), AIR, false);

		// stairs
		rotation = Rotation.COUNTERCLOCKWISE_90;
		for (int f = 0; f < 7; f++) {
			//int rotation = (f + 0) % 4;
			rotation = rotation.getRotated(Rotation.CLOCKWISE_90);
			int y = f * 3 + 18;
			for (int i = 0; i < 3; i++) {
				int sx = 3 + i;
				int sy = y + i;
				int sz = 1;

				this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, false), sx, sy, sz, rotation, sbb);
				this.setBlockStateRotated(world, deco.blockState, sx, sy - 1, sz, rotation, sbb);
				this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, false), sx, sy, sz + 1, rotation, sbb);
				this.setBlockStateRotated(world, deco.blockState, sx, sy - 1, sz + 1, rotation, sbb);
			}
			// landing
			this.fillBlocksRotated(world, sbb, 6, y + 2, 1, 7, y + 2, 2, deco.blockState, rotation);
		}

		// roof access landing
		this.generateBox(world, sbb, 1, 38, 1, 3, 38, 5, deco.blockState, deco.blockState, false);
		this.generateBox(world, sbb, 3, 39, 1, 3, 39, 5, deco.fenceState, deco.fenceState, false);
	}

	public BlockState getGlyphMeta() {
		return TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get().defaultBlockState();
	}
}
