package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
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
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.world.components.structures.TFStructureComponentOld;


public class FinalCastleDungeonExitComponent extends FinalCastleDungeonRoom31Component {

	public FinalCastleDungeonExitComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCDunEx.get(), nbt);
	}

	public FinalCastleDungeonExitComponent(int i, int x, int y, int z, Direction direction, int level) {
		super(TFStructurePieceTypes.TFFCDunEx.get(), i, x, y, z, direction, level);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePiecesBuilder list, RandomSource rand) {
		if (parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}

		// no need for additional rooms, we're along the outside anyways

		// add stairway down
		Rotation bestDir = this.findStairDirectionTowards(parent.getBoundingBox().minX(), parent.getBoundingBox().minZ());

		FinalCastleDungeonStepsComponent steps0 = new FinalCastleDungeonStepsComponent(5, boundingBox.minZ() + 15, boundingBox.minY(), boundingBox.minZ() + 15, bestDir.rotate(Direction.SOUTH));
		list.addPiece(steps0);
		steps0.addChildren(this, list, rand);

		// another level!?
		if (this.level == 1) {
			steps0.buildLevelUnder(list, rand, this.level + 1);
		} else {
			steps0.buildBossRoomUnder(list, rand);
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {

		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		// door
		final BlockState castleDoor = TFBlocks.PINK_CASTLE_DOOR.get().defaultBlockState();

		this.generateBox(world, sbb, 7, 0, 16, 7, 3, 18, castleDoor, AIR, false);
		this.generateBox(world, sbb, 7, 4, 16, 7, 4, 18, deco.blockState, deco.blockState, false);
	}

	public Rotation findStairDirectionTowards(int x, int z) {
		Vec3i center = BoundingBoxUtils.getCenter(this.boundingBox);
		// difference
		int dx = center.getX() - x;
		int dz = center.getZ() - z;

		Rotation absoluteDir;
		if (Math.abs(dz) >= Math.abs(dx)) {
			absoluteDir = (dz >= 0) ? Rotation.CLOCKWISE_180 : Rotation.NONE;
		} else {
			absoluteDir = (dx >= 0) ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90;
		}

		return absoluteDir;
	}

	@Override
	protected BlockState getForceFieldColor(RandomSource decoRNG) {
		return TFBlocks.PINK_FORCE_FIELD.get().defaultBlockState();
	}

	@Override
	protected BlockState getRuneColor(BlockState fieldColor) {
		return TFBlocks.PINK_CASTLE_RUNE_BRICK.get().defaultBlockState();
	}
}
