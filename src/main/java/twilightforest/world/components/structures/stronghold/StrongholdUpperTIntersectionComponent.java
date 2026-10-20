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

public class StrongholdUpperTIntersectionComponent extends StrongholdUpperComponent {

	protected static final List<SmallDoorway> SMALL_DOORWAYS = List.of(
		new SmallDoorway(Direction.Axis.Z, 2, 1, 0),
		new SmallDoorway(Direction.Axis.X, 4, 1, 2),
		new SmallDoorway(Direction.Axis.X, 0, 1, 2)
	);

	public StrongholdUpperTIntersectionComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFSUTI.get(), nbt);
	}

	public StrongholdUpperTIntersectionComponent(int i, Direction facing, int x, int y, int z) {
		super(TFStructurePieceTypes.TFSUTI.get(), i, facing, x, y, z);
	}

	@Override
	public BoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return BoundingBox.orientBox(x, y, z, -2, -1, 0, 5, 5, 5, facing);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource random) {
		super.addChildren(parent, list, random);

		// make a random component to the left
		addNewUpperComponent(parent, list, random, Rotation.COUNTERCLOCKWISE_90, 5, 1, 2);

		// make a random component to the right
		addNewUpperComponent(parent, list, random, Rotation.CLOCKWISE_90, -1, 1, 2);
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
