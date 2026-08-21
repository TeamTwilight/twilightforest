package twilightforest.client.model.block.connected;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ConnectedTextureModel {

	private final Set<Direction> connectedFaces;
	private final Set<Direction> unculledFaces;
	private final boolean renderOverlayOnAllFaces;
	private final Map<Direction, BakedQuad[]> baseQuads;
	private final Map<Direction, BakedQuad[][]> connectedQuads;
	private final TextureAtlasSprite particle;
	private final boolean usesAO;
	private final boolean usesBlockLight;
	private final ItemTransforms transforms;
//	@Nullable
//	private final ChunkRenderTypeSet blockRenderTypes;
	@Nullable
//	private final RenderType itemRenderType;
	private final List<Block> validConnectors;
	private static final ModelProperty<ConnectedTextureData> DATA = new ModelProperty<>();

	public ConnectedTextureModel(Set<Direction> connectedFaces, Set<Direction> unculledFaces, boolean renderOverlayOnAllFaces, List<Block> connectableBlocks, Map<Direction, BakedQuad[]> baseQuads, Map<Direction, BakedQuad[][]> connectedQuads, TextureAtlasSprite particle, boolean usesAO, boolean usesBlockLight, ItemTransforms transforms) {
		this.connectedFaces = connectedFaces;
		this.unculledFaces = unculledFaces;
		this.renderOverlayOnAllFaces = renderOverlayOnAllFaces;
		this.validConnectors = connectableBlocks;
		this.baseQuads = baseQuads;
		this.connectedQuads = connectedQuads;
		this.particle = particle;
		this.usesAO = usesAO;
		this.usesBlockLight = usesBlockLight;
		this.transforms = transforms;
//		this.blockRenderTypes = !group.isEmpty() ? ChunkRenderTypeSet.of(group.block()) : null;
//		this.itemRenderType = !group.isEmpty() ? group.entity() : null;
	}

	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random, ModelData extraData, @Nullable RenderType type) {
		if (side == null) {
			List<BakedQuad> quadList = new ArrayList<>();
			for (Direction direction : this.unculledFaces) quadList.addAll(this.getQuadsForFace(direction, extraData));
			return quadList;
		} else return this.getQuadsForFace(side, extraData);
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

	public ModelData getModelData(BlockAndTintGetter getter, BlockPos pos, BlockState state, ModelData modelData) {
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

		return modelData.derive().with(DATA, data).build();
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

//	@Override
//	public boolean useAmbientOcclusion() {
//		return this.usesAO;
//	}

//	@Override
//	public boolean isGui3d() {
//		return true;
//	}

//	@Override
//	public boolean usesBlockLight() {
//		return this.usesBlockLight;
//	}

//	@Override
//	public TextureAtlasSprite getParticleIcon() {
//		return this.particle;
//	}

//	@Override
//	public ItemTransforms getTransforms() {
//		return this.transforms;
//	}

//	@Override
//	public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
//		return this.blockRenderTypes != null ? this.blockRenderTypes : IDynamicBakedModel.super.getRenderTypes(state, rand, data);
//	}

//	@Override
//	public RenderType getRenderType(ItemStack stack) {
//		return this.itemRenderType != null ? this.itemRenderType : IDynamicBakedModel.super.getRenderType(stack);
//	}

	private static final class ConnectedTextureData {
		private final ConnectionLogic[][] logic = new ConnectionLogic[6][4];

		private ConnectedTextureData() {
		}
	}
}