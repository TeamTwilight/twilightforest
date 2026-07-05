package twilightforest.client.model.block.connected;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ConnectedTextureModel implements UnbakedGeometry {

	private final Set<Direction> connectedFaces;
	private final Set<Direction> unculledFaces;
	private final boolean renderOverlayOnAllFaces;
	private final Map<Direction, BakedQuad[]> baseQuads;
	private final Map<Direction, BakedQuad[][]> connectedQuads;
	private final List<Block> validConnectors;
	private static final ModelProperty<@NotNull ConnectedTextureData> DATA = new ModelProperty<>();

	public ConnectedTextureModel(Set<Direction> connectedFaces, Set<Direction> unculledFaces, boolean renderOverlayOnAllFaces, List<Block> connectableBlocks, Map<Direction, BakedQuad[]> baseQuads, Map<Direction, BakedQuad[][]> connectedQuads) {
		this.connectedFaces = connectedFaces;
		this.unculledFaces = unculledFaces;
		this.renderOverlayOnAllFaces = renderOverlayOnAllFaces;
		this.validConnectors = connectableBlocks;
		this.baseQuads = baseQuads;
		this.connectedQuads = connectedQuads;
	}

	@Override
	public QuadCollection bake(TextureSlots textureSlots, ModelBaker modelBaker, ModelState modelState, ModelDebugName modelDebugName) {
		QuadCollection.Builder builder = new QuadCollection.Builder();

		for (Direction direction : this.unculledFaces) {
			List<BakedQuad> unculledQuads = this.getQuadsForFace(direction, ModelData.EMPTY);
			for (BakedQuad quad : unculledQuads) {
				builder.addUnculledFace(quad);
			}
		}

		for (Direction direction : Direction.values()) {
			List<BakedQuad> culledQuads = this.getQuadsForFace(direction, ModelData.EMPTY);
			for (BakedQuad quad : culledQuads) {
				builder.addCulledFace(direction, quad);
			}
		}

		return builder.build();
	}

	public List<BakedQuad> getQuadsForFace(Direction side, ModelData extraData) {
		BakedQuad[] baseQuads = this.baseQuads.get(side);
		ConnectedTextureData data = extraData.get(DATA);
		ArrayList<BakedQuad> quads = new ArrayList<>(4 + (baseQuads != null ? 4 : 0));
		if (baseQuads != null) quads.addAll(List.of(baseQuads));

		if (this.connectedFaces.contains(side) || this.renderOverlayOnAllFaces) {
			for (int quad = 0; quad < 4; ++quad) {
				//if our model data is null (happens for items), we can skip connected textures since we dont have the info we need
				ConnectionLogic connectionType = data != null && this.connectedFaces.contains(side) ? data.logic[side.get3DDataValue()][quad] : ConnectionLogic.NONE;
				quads.add(this.connectedQuads.get(side)[quad][connectionType.ordinal()]);
			}
		}

		return quads;
	}

	private boolean shouldConnectSide(BlockAndTintGetter getter, BlockPos pos, Direction face, Direction side) {
		BlockState neighborState = getter.getBlockState(pos.relative(side));
		if (this.unculledFaces.contains(face)) return this.validConnectors.stream().anyMatch(neighborState::is);
		return this.validConnectors.stream().anyMatch(neighborState::is) && Block.shouldRenderFace(getter, pos.relative(face), neighborState, getter.getBlockState(pos.relative(face)), face);
	}

	private boolean isCornerBlockPresent(BlockAndTintGetter getter, BlockPos pos, Direction face, Direction side1, Direction side2) {
		BlockState neighborState = getter.getBlockState(pos.relative(side1).relative(side2));
		if (this.unculledFaces.contains(face)) return this.validConnectors.stream().anyMatch(neighborState::is);
		return this.validConnectors.stream().anyMatch(neighborState::is) && Block.shouldRenderFace(getter, pos.relative(face), neighborState, getter.getBlockState(pos.relative(face)), face);
	}

	private static final class ConnectedTextureData {
		private final ConnectionLogic[][] logic = new ConnectionLogic[6][4];

		private ConnectedTextureData() {
		}
	}
}