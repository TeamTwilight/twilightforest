package twilightforest.client.model.block.connected;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class ConnectedTextureModel implements DynamicBlockStateModel {

	private final Set<Direction> connectedFaces;
	private final Set<Direction> unculledFaces;
	private final boolean renderOverlayOnAllFaces;
	private final Map<Direction, BakedQuad[]> baseQuads;
	private final Map<Direction, BakedQuad[][]> connectedQuads;
	private final Predicate<BlockState> validConnectors;
	private final boolean ctmUsesAO;
	private final Material.Baked particleTexture;
	private final int materialFlags;

	public ConnectedTextureModel(Set<Direction> connectedFaces, Set<Direction> unculledFaces, boolean renderOverlayOnAllFaces, Predicate<BlockState> validConnectors, Map<Direction, BakedQuad[]> baseQuads, Map<Direction, BakedQuad[][]> connectedQuads, boolean ctmUsesAO, Material.Baked particleTexture, int materialFlags) {
		this.connectedFaces = connectedFaces;
		this.unculledFaces = unculledFaces;
		this.renderOverlayOnAllFaces = renderOverlayOnAllFaces;
		this.validConnectors = validConnectors;
		this.baseQuads = baseQuads;
		this.connectedQuads = connectedQuads;
		this.ctmUsesAO = ctmUsesAO;
		this.particleTexture = particleTexture;
		this.materialFlags = materialFlags;
	}

	@Override
	public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		QuadCollection.Builder quadCollection = new QuadCollection.Builder();

		ConnectedTextureData data = this.getModelData(level, pos);

		this.getQuads(null, data).forEach(quadCollection::addUnculledFace);

		for (Direction direction : Direction.values()) {
			List<BakedQuad> quads = this.getQuads(direction, data);
			for (BakedQuad quad : quads) {
				quadCollection.addCulledFace(direction, quad);
			}
		}

		parts.add(new SimpleModelWrapper(quadCollection.build(), this.ctmUsesAO, this.particleMaterial()));
	}

	/**
	 * The geometry only depends on which neighbors connect, so NeoForge can reuse previously built geometry for any block with the same connection pattern.
	 */
	@Override
	public Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
		return new GeometryKey(this, this.getModelData(level, pos));
	}

	@Override
	public Material.Baked particleMaterial() {
		return this.particleTexture;
	}

	@Override
	public @BakedQuad.MaterialFlags int materialFlags() {
		return this.materialFlags;
	}

	private List<BakedQuad> getQuads(@Nullable Direction side, ConnectedTextureData data) {
		if (side == null) {
			List<BakedQuad> quadList = new ArrayList<>();
			for (Direction direction : this.unculledFaces) quadList.addAll(this.getQuadsForFace(direction, data));
			return quadList;
		} else if (this.unculledFaces.contains(side)) {
			return List.of();
		} else {
			return this.getQuadsForFace(side, data);
		}
	}

	private List<BakedQuad> getQuadsForFace(Direction side, ConnectedTextureData data) {
		BakedQuad[] baseQuads = this.baseQuads.get(side);
		ArrayList<BakedQuad> quads = new ArrayList<>(4 + (baseQuads != null ? 4 : 0));
		if (baseQuads != null) quads.addAll(List.of(baseQuads));

		if (this.connectedFaces.contains(side) || this.renderOverlayOnAllFaces) {
			for (int quad = 0; quad < 4; ++quad) {
				ConnectionLogic connectionType = this.connectedFaces.contains(side) ? data.logic[side.get3DDataValue()][quad] : ConnectionLogic.NONE;
				quads.add(this.connectedQuads.get(side)[quad][connectionType.ordinal()]);
			}
		}

		return quads;
	}

	private ConnectedTextureData getModelData(BlockAndTintGetter getter, BlockPos pos) {
		ConnectedTextureData data = new ConnectedTextureData();

		for (Direction face : Direction.values()) {
			Direction[] directions = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];
			boolean[] sideStates = new boolean[4];

			int faceIndex;
			for (faceIndex = 0; faceIndex < directions.length; faceIndex++) {
				sideStates[faceIndex] = this.shouldConnectSide(getter, pos, face, directions[faceIndex]);
			}

			faceIndex = face.get3DDataValue();

			for (int dir = 0; dir < directions.length; dir++) {
				int cornerOffset = (dir + 1) % directions.length;
				boolean side1 = sideStates[dir];
				boolean side2 = sideStates[cornerOffset];
				boolean corner = side1 && side2 && this.isCornerBlockPresent(getter, pos, face, directions[dir], directions[cornerOffset]);
				data.logic[faceIndex][dir] = dir % 2 == 0 ? ConnectionLogic.of(side1, side2, corner) : ConnectionLogic.of(side2, side1, corner);
			}
		}

		return data;
	}

	private boolean shouldConnectSide(BlockAndTintGetter getter, BlockPos pos, Direction face, Direction side) {
		return this.connectsTo(getter, pos, face, getter.getBlockState(pos.relative(side)));
	}

	private boolean isCornerBlockPresent(BlockAndTintGetter getter, BlockPos pos, Direction face, Direction side1, Direction side2) {
		return this.connectsTo(getter, pos, face, getter.getBlockState(pos.relative(side1).relative(side2)));
	}

	private boolean connectsTo(BlockAndTintGetter getter, BlockPos pos, Direction face, BlockState neighborState) {
		if (!this.validConnectors.test(neighborState)) return false;
		if (this.unculledFaces.contains(face)) return true;
		return Block.shouldRenderFace(getter, pos.relative(face), neighborState, getter.getBlockState(pos.relative(face)), face);
	}

	private record GeometryKey(ConnectedTextureModel model, ConnectedTextureData data) {
	}

	private static final class ConnectedTextureData {
		private final ConnectionLogic[][] logic = new ConnectionLogic[6][4];

		private ConnectedTextureData() {
		}

		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof ConnectedTextureData that && Arrays.deepEquals(this.logic, that.logic);
		}

		@Override
		public int hashCode() {
			return Arrays.deepHashCode(this.logic);
		}
	}
}
