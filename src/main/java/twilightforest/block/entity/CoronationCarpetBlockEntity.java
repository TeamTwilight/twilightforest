package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelProperty;
import twilightforest.client.model.block.connected.ConnectionLogic;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;

import java.util.Arrays;

public class CoronationCarpetBlockEntity extends BlockEntity {
	private final Block[] validConnectors = {TFBlocks.CORONATION_CARPET.value()};
	private static final ModelProperty<LoftyCarpetData> DATA = new ModelProperty<>();

	public CoronationCarpetBlockEntity(BlockPos worldPosition, BlockState blockState) {
		super(TFBlockEntities.CORONATION_CARPET.get(), worldPosition, blockState);
	}

	@Override
	public ModelData getModelData() {
		if (this.level == null) {
			return ModelData.EMPTY;
		}

		LoftyCarpetData data = new LoftyCarpetData();

		for (Direction face : Direction.values()) {
			Direction[] directions = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];
			boolean[] sideStates = new boolean[4];

			int faceIndex;
			for (faceIndex = 0; faceIndex < directions.length; faceIndex++) {
				sideStates[faceIndex] = this.shouldConnectSide(this.level, this.worldPosition, face, directions[faceIndex]);
			}

			faceIndex = face.get3DDataValue();

			for (int dir = 0; dir < directions.length; dir++) {
				int cornerOffset = (dir + 1) % directions.length;
				boolean side1 = sideStates[dir];
				boolean side2 = sideStates[cornerOffset];
				boolean corner = side1 && side2 && this.isCornerBlockPresent(this.level, this.worldPosition, face, directions[dir], directions[cornerOffset]);
				data.logic[faceIndex][dir] = dir % 2 == 0 ? ConnectionLogic.of(side1, side2, corner) : ConnectionLogic.of(side2, side1, corner);
			}
		}

		return ModelData.EMPTY.derive().with(DATA, data).build();
	}

	private boolean shouldConnectSide(BlockGetter getter, BlockPos pos, Direction face, Direction side) {
		BlockState neighborState = getter.getBlockState(pos.relative(side));
		return Arrays.stream(this.validConnectors).anyMatch(neighborState::is) && Block.shouldRenderFace(getter, pos, getter.getBlockState(pos), neighborState, face);
	}

	private boolean isCornerBlockPresent(BlockGetter getter, BlockPos pos, Direction face, Direction side1, Direction side2) {
		BlockState neighborState = getter.getBlockState(pos.relative(side1).relative(side2));
		return Arrays.stream(this.validConnectors).anyMatch(neighborState::is) && Block.shouldRenderFace(getter, pos, getter.getBlockState(pos), neighborState, face);
	}

	//we need a class to make model data. Fine, here you go
	private static final class LoftyCarpetData {
		private final ConnectionLogic[][] logic = new ConnectionLogic[6][4];

		private LoftyCarpetData() {
		}
	}
}
