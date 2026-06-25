package twilightforest.client.model.block.carpet;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import org.jetbrains.annotations.Nullable;
import twilightforest.client.model.block.connected.ConnectionLogic;
import twilightforest.init.TFBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoyalRagsModel implements BlockStateModel, DynamicBlockStateModel {
	@Nullable
	private final List<BakedQuad>[] baseQuads;
	private final BakedQuad[][][] quads;
	private final Material.Baked particle;
	private final Block[] validConnectors = {TFBlocks.CORONATION_CARPET.value()};

	public RoyalRagsModel(@Nullable List<BakedQuad>[] baseQuads, BakedQuad[][][] quads, Material.Baked particle) {
		this.baseQuads = baseQuads;
		this.quads = quads;
		this.particle = particle;
	}

	@Override
	public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		// Compute connection logic for each face at this position
		ConnectionLogic[][] logic = new ConnectionLogic[6][4];

		for (Direction face : Direction.values()) {
			Direction[] directions = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];
			boolean[] sideStates = new boolean[4];

			for (int i = 0; i < directions.length; i++) {
				sideStates[i] = this.shouldConnectSide(level, pos, face, directions[i]);
			}

			int faceIndex = face.get3DDataValue();

			for (int dir = 0; dir < directions.length; dir++) {
				int cornerOffset = (dir + 1) % directions.length;
				boolean side1 = sideStates[dir];
				boolean side2 = sideStates[cornerOffset];
				boolean corner = side1 && side2 && this.isCornerBlockPresent(level, pos, face, directions[dir], directions[cornerOffset]);
				logic[faceIndex][dir] = dir % 2 == 0 ? ConnectionLogic.of(side1, side2, corner) : ConnectionLogic.of(side2, side1, corner);
			}
		}

		// Build quads for each direction
		@SuppressWarnings({"unchecked", "rawtypes"})
		List<BakedQuad>[] quadsByDirection = new List[6];
		for (Direction face : Direction.values()) {
			int faceIndex = face.get3DDataValue();
			List<BakedQuad> faceQuads = new ArrayList<>(8);

			if (face.getAxis().isHorizontal() && this.baseQuads != null) {
				faceQuads.addAll(this.baseQuads[face.get2DDataValue()]);
			}

			for (int quad = 0; quad < 4; ++quad) {
				ConnectionLogic connectionType = logic[faceIndex][quad];
				faceQuads.add(this.quads[faceIndex][quad][connectionType.ordinal()]);
			}

			quadsByDirection[faceIndex] = faceQuads;
		}

		parts.add(new RoyalRagsPart(quadsByDirection, this.particle));
	}

	private boolean shouldConnectSide(BlockAndTintGetter getter, BlockPos pos, Direction face, Direction side) {
		BlockState neighborState = getter.getBlockState(pos.relative(side));
		return Arrays.stream(this.validConnectors).anyMatch(neighborState::is) && Block.shouldRenderFace(getter, pos, getter.getBlockState(pos), neighborState, face);
	}

	private boolean isCornerBlockPresent(BlockAndTintGetter getter, BlockPos pos, Direction face, Direction side1, Direction side2) {
		BlockState neighborState = getter.getBlockState(pos.relative(side1).relative(side2));
		return Arrays.stream(this.validConnectors).anyMatch(neighborState::is) && Block.shouldRenderFace(getter, pos, getter.getBlockState(pos), neighborState, face);
	}

	@Override
	@Deprecated
	public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
	}

	@Override
	@Deprecated
	public Material.Baked particleMaterial() {
		return this.particle;
	}

	@Override
	public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.particle;
	}

	@Override
	@Deprecated
	public int materialFlags() {
		return 0;
	}

	@Override
	public int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return 0;
	}

	private static final class RoyalRagsPart implements BlockStateModelPart {
		private final List<BakedQuad>[] quadsByDirection;
		private final Material.Baked particle;

		@SuppressWarnings("unchecked")
		private RoyalRagsPart(List<BakedQuad>[] quadsByDirection, Material.Baked particle) {
			this.quadsByDirection = quadsByDirection;
			this.particle = particle;
		}

		@Override
		public List<BakedQuad> getQuads(@Nullable Direction direction) {
			if (direction == null) return List.of();
			return this.quadsByDirection[direction.get3DDataValue()];
		}

		@Override
		@Deprecated
		public boolean useAmbientOcclusion() {
			return true;
		}

		@Override
		public Material.Baked particleMaterial() {
			return this.particle;
		}

		@Override
		public int materialFlags() {
			return 0;
		}
	}
}
