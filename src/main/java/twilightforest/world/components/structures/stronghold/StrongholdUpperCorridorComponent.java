package twilightforest.world.components.structures.stronghold;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;

import java.util.List;

public class StrongholdUpperCorridorComponent extends StrongholdUpperComponent {

	protected static final List<SmallDoorway> SMALL_DOORWAYS = List.of(
		new SmallDoorway(Direction.Axis.Z, 2, 1, 0),
		new SmallDoorway(Direction.Axis.Z, 2, 1, 8)
	);

	public StrongholdUpperCorridorComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFSUCo.get(), nbt);
	}

	public StrongholdUpperCorridorComponent(int i, Direction facing, int x, int y, int z) {
		super(TFStructurePieceTypes.TFSUCo.get(), i, facing, x, y, z);
	}

	@Override
	public BoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return BoundingBox.orientBox(x, y, z, -2, -1, 0, 5, 5, 9, facing);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource random) {
		super.addChildren(parent, list, random);

		// make a random component at the end
		addNewUpperComponent(parent, list, random, Rotation.NONE, 2, 1, 9);
	}

	@Override
	protected List<SmallDoorway> getSmallDoorways() {
		return SMALL_DOORWAYS;
	}

	@Override
	public boolean isComponentProtected() {
		return false;
	}
}
