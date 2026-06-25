package twilightforest.client.model.block.connected;

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

import java.util.*;

public class ConnectedTextureModel implements BlockStateModel, DynamicBlockStateModel {

	private final Set<Direction> connectedFaces;
	private final Set<Direction> unculledFaces;
	private final boolean renderOverlayOnAllFaces;
	private final Map<Direction, BakedQuad[]> baseQuads;
	private final Map<Direction, BakedQuad[][]> connectedQuads;
	private final Material.Baked particle;
	private final boolean usesAO;
	private final boolean translucent;
	private final List<Block> validConnectors;

	public ConnectedTextureModel(Set<Direction> connectedFaces, Set<Direction> unculledFaces, boolean renderOverlayOnAllFaces, List<Block> connectableBlocks, Map<Direction, BakedQuad[]> baseQuads, Map<Direction, BakedQuad[][]> connectedQuads, Material.Baked particle, boolean usesAO, boolean translucent) {
		this.connectedFaces = connectedFaces;
		this.unculledFaces = unculledFaces;
		this.renderOverlayOnAllFaces = renderOverlayOnAllFaces;
		this.validConnectors = connectableBlocks;
		this.baseQuads = baseQuads;
		this.connectedQuads = connectedQuads;
		this.particle = particle;
		this.usesAO = usesAO;
		this.translucent = translucent;
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

			BakedQuad[] baseQuads = this.baseQuads.get(face);
			if (baseQuads != null) faceQuads.addAll(List.of(baseQuads));

			if (this.connectedFaces.contains(face) || this.renderOverlayOnAllFaces) {
				for (int quad = 0; quad < 4; ++quad) {
					ConnectionLogic connectionType = this.connectedFaces.contains(face) ? logic[faceIndex][quad] : ConnectionLogic.NONE;
					faceQuads.add(this.connectedQuads.get(face)[quad][connectionType.ordinal()]);
				}
			}

			quadsByDirection[faceIndex] = faceQuads;
		}

		parts.add(new ConnectedTexturePart(quadsByDirection, this.particle, this.usesAO));
	}

	private boolean shouldConnectSide(BlockAndTintGetter getter, BlockPos pos, Direction face, Direction side) {
		BlockState neighborState = getter.getBlockState(pos.relative(side));
		if (this.unculledFaces.contains(face)) return this.validConnectors.stream().anyMatch(neighborState::is);
		return this.validConnectors.stream().anyMatch(neighborState::is) && Block.shouldRenderFace(getter, pos, getter.getBlockState(pos), neighborState, face);
	}

	private boolean isCornerBlockPresent(BlockAndTintGetter getter, BlockPos pos, Direction face, Direction side1, Direction side2) {
		BlockState neighborState = getter.getBlockState(pos.relative(side1).relative(side2));
		if (this.unculledFaces.contains(face)) return this.validConnectors.stream().anyMatch(neighborState::is);
		return this.validConnectors.stream().anyMatch(neighborState::is) && Block.shouldRenderFace(getter, pos, getter.getBlockState(pos), neighborState, face);
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
		return this.translucent ? BakedQuad.FLAG_TRANSLUCENT : 0;
	}

	@Override
	public int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.translucent ? BakedQuad.FLAG_TRANSLUCENT : 0;
	}

	private static final class ConnectedTexturePart implements BlockStateModelPart {
		private final List<BakedQuad>[] quadsByDirection;
		private final Material.Baked particle;
		private final boolean usesAO;

		@SuppressWarnings("unchecked")
		private ConnectedTexturePart(List<BakedQuad>[] quadsByDirection, Material.Baked particle, boolean usesAO) {
			this.quadsByDirection = quadsByDirection;
			this.particle = particle;
			this.usesAO = usesAO;
		}

		@Override
		public List<BakedQuad> getQuads(@Nullable Direction direction) {
			if (direction == null) return List.of();
			List<BakedQuad> quads = this.quadsByDirection[direction.get3DDataValue()];
			return quads != null ? quads : List.of();
		}

		@Override
		@Deprecated
		public boolean useAmbientOcclusion() {
			return this.usesAO;
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
