package twilightforest.client.model.block.forcefield;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import org.jspecify.annotations.Nullable;
import twilightforest.block.ForceFieldBlock;
import twilightforest.client.model.block.forcefield.ForceFieldElement.Condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForceFieldModel implements DynamicBlockStateModel {

	private final List<BakedElement> elements;
	private final Material.Baked particle;
	private final boolean ambientOcclusion;
	private final int materialFlags;

	public ForceFieldModel(List<BakedElement> elements, Material.Baked particle, boolean ambientOcclusion) {
		this.elements = elements;
		this.particle = particle;
		this.ambientOcclusion = ambientOcclusion;

		int flags = 0;
		for (BakedElement element : elements) {
			flags |= element.quad().materialInfo().flags();
		}
		this.materialFlags = flags;
	}

	@Override
	public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		Map<ExtraDirection, List<Direction>> connections = this.connections(level, pos, state);
		QuadCollection.Builder quads = new QuadCollection.Builder();

		for (BakedElement element : this.elements) {
			if (this.skipRender(connections, element.condition(), element.side())) {
				continue;
			}

			if (element.cullFace() != null) {
				quads.addCulledFace(element.cullFace(), element.quad());
			} else {
				quads.addUnculledFace(element.quad());
			}
		}

		parts.add(new SimpleModelWrapper(quads.build(), this.ambientOcclusion, this.particle));
	}

	@Override
	public Material.Baked particleMaterial() {
		return this.particle;
	}

	@Override
	public @BakedQuad.MaterialFlags int materialFlags() {
		return this.materialFlags;
	}

	private Map<ExtraDirection, List<Direction>> connections(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		Map<ExtraDirection, List<Direction>> map = new HashMap<>();

		for (ExtraDirection extraDirection : this.getExtraDirections(state, level, pos)) {
			List<Direction> directionList = new ArrayList<>();
			for (Direction dir : Direction.values()) {
				ExtraDirection mirrored = extraDirection.mirrored(dir.getAxis());
				if (mirrored != extraDirection) {
					BlockState other = level.getBlockState(pos.relative(dir));
					if (other.getBlock() instanceof ForceFieldBlock) {
						if (this.getExtraDirections(other, level, pos.relative(dir)).contains(mirrored)) {
							directionList.add(dir);
						}
					}
				}
			}
			map.put(extraDirection, directionList);
		}

		return map;
	}

	private boolean skipRender(Map<ExtraDirection, List<Direction>> directions, @Nullable Condition condition, Direction side) {
		if (condition == null) {
			return false;
		}

		for (ExtraDirection parent : condition.parents()) {
			if (!directions.containsKey(parent)) {
				return true;
			}
		}

		boolean hasKey = directions.containsKey(condition.direction());
		if (hasKey != condition.value()) {
			return true;
		}
		if (hasKey) {
			return directions.get(condition.direction()).contains(side);
		}
		return false;
	}

	private List<ExtraDirection> getExtraDirections(BlockState state, BlockGetter level, BlockPos pos) {
		List<ExtraDirection> directions = new ArrayList<>();

		if (!(state.getBlock() instanceof ForceFieldBlock)) {
			return directions;
		}

		boolean down = state.getValue(ForceFieldBlock.DOWN);
		boolean up = state.getValue(ForceFieldBlock.UP);
		boolean north = state.getValue(ForceFieldBlock.NORTH);
		boolean south = state.getValue(ForceFieldBlock.SOUTH);
		boolean west = state.getValue(ForceFieldBlock.WEST);
		boolean east = state.getValue(ForceFieldBlock.EAST);

		if (down) {
			directions.add(ExtraDirection.DOWN);
			if (north && ForceFieldBlock.cornerConnects(level, pos, Direction.DOWN, Direction.NORTH)) directions.add(ExtraDirection.DOWN_NORTH);
			if (south && ForceFieldBlock.cornerConnects(level, pos, Direction.DOWN, Direction.SOUTH)) directions.add(ExtraDirection.DOWN_SOUTH);
			if (west && ForceFieldBlock.cornerConnects(level, pos, Direction.DOWN, Direction.WEST)) directions.add(ExtraDirection.DOWN_WEST);
			if (east && ForceFieldBlock.cornerConnects(level, pos, Direction.DOWN, Direction.EAST)) directions.add(ExtraDirection.DOWN_EAST);
		}
		if (up) {
			directions.add(ExtraDirection.UP);
			if (north && ForceFieldBlock.cornerConnects(level, pos, Direction.UP, Direction.NORTH)) directions.add(ExtraDirection.UP_NORTH);
			if (south && ForceFieldBlock.cornerConnects(level, pos, Direction.UP, Direction.SOUTH)) directions.add(ExtraDirection.UP_SOUTH);
			if (west && ForceFieldBlock.cornerConnects(level, pos, Direction.UP, Direction.WEST)) directions.add(ExtraDirection.UP_WEST);
			if (east && ForceFieldBlock.cornerConnects(level, pos, Direction.UP, Direction.EAST)) directions.add(ExtraDirection.UP_EAST);
		}
		if (north) {
			directions.add(ExtraDirection.NORTH);
			if (west && ForceFieldBlock.cornerConnects(level, pos, Direction.NORTH, Direction.WEST)) directions.add(ExtraDirection.NORTH_WEST);
			if (east && ForceFieldBlock.cornerConnects(level, pos, Direction.NORTH, Direction.EAST)) directions.add(ExtraDirection.NORTH_EAST);
		}
		if (south) {
			directions.add(ExtraDirection.SOUTH);
			if (west && ForceFieldBlock.cornerConnects(level, pos, Direction.SOUTH, Direction.WEST)) directions.add(ExtraDirection.SOUTH_WEST);
			if (east && ForceFieldBlock.cornerConnects(level, pos, Direction.SOUTH, Direction.EAST)) directions.add(ExtraDirection.SOUTH_EAST);
		}
		if (west) directions.add(ExtraDirection.WEST);
		if (east) directions.add(ExtraDirection.EAST);

		return directions;
	}

	public record BakedElement(@Nullable Condition condition, Direction side, @Nullable Direction cullFace, BakedQuad quad) {
	}

	public enum ExtraDirection implements StringRepresentable {
		DOWN("down", 0, 1, 0),
		UP("up", 1, 0, 1),
		NORTH("north", 2, 2, 3),
		SOUTH("south", 3, 3, 2),
		WEST("west", 5, 4, 4),
		EAST("east", 4, 5, 5),

		DOWN_NORTH("down_north", 6, 10, 7),
		DOWN_SOUTH("down_south", 7, 11, 6),
		DOWN_WEST("down_west", 9, 12, 8),
		DOWN_EAST("down_east", 8, 13, 9),

		UP_NORTH("up_north", 10, 6, 11),
		UP_SOUTH("up_south", 11, 7, 10),
		UP_WEST("up_west", 13, 8, 12),
		UP_EAST("up_east", 12, 9, 13),

		NORTH_WEST("north_west", 15, 14, 16),
		NORTH_EAST("north_east", 14, 15, 17),
		SOUTH_WEST("south_west", 17, 16, 14),
		SOUTH_EAST("south_east", 16, 17, 15);

		public static final StringRepresentable.EnumCodec<ExtraDirection> CODEC = StringRepresentable.fromEnum(ExtraDirection::values);
		private final String name;
		private final int xAxisMirror;
		private final int yAxisMirror;
		private final int zAxisMirror;

		ExtraDirection(String name, int xAxisMirror, int yAxisMirror, int zAxisMirror) {
			this.name = name;
			this.xAxisMirror = xAxisMirror;
			this.yAxisMirror = yAxisMirror;
			this.zAxisMirror = zAxisMirror;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}

		public ExtraDirection mirrored(Direction.Axis axis) {
			return switch (axis) {
				case X -> ExtraDirection.values()[this.xAxisMirror];
				case Y -> ExtraDirection.values()[this.yAxisMirror];
				case Z -> ExtraDirection.values()[this.zAxisMirror];
			};
		}
	}
}
