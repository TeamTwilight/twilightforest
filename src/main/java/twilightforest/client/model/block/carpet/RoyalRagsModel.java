package twilightforest.client.model.block.carpet;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.client.model.block.connected.ConnectionLogic;
import twilightforest.init.TFBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("deprecation")
public class RoyalRagsModel implements IDynamicBakedModel {
	@Nullable
	private final List<BakedQuad>[] baseQuads;
	private final BakedQuad[][][] quads;
	private final TextureAtlasSprite particle;
	private final ItemOverrides overrides;
	private final ItemTransforms transforms;
	private final ChunkRenderTypeSet blockRenderTypes;
	private final List<RenderType> itemRenderTypes;
	private final List<RenderType> fabulousItemRenderTypes;
	// FIXME Generalize
	private final Block[] validConnectors = {TFBlocks.CORONATION_CARPET.value()};
	private static final ModelProperty<LoftyCarpetData> DATA = new ModelProperty<>();

	public RoyalRagsModel(@Nullable List<BakedQuad>[] baseQuads, BakedQuad[][][] quads, TextureAtlasSprite particle, ItemOverrides overrides, ItemTransforms transforms, RenderTypeGroup group) {
		this.baseQuads = baseQuads;
		this.quads = quads;
		this.particle = particle;
		this.overrides = overrides;
		this.transforms = transforms;
		this.blockRenderTypes = !group.isEmpty() ? ChunkRenderTypeSet.of(group.block()) : null;
		this.itemRenderTypes = !group.isEmpty() ? List.of(group.entity()) : null;
		this.fabulousItemRenderTypes = !group.isEmpty() ? List.of(group.entityFabulous()) : null;
	}

	@NotNull
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource random, @NotNull ModelData extraData, @Nullable RenderType type) {
		if (side != null) {
			ArrayList<BakedQuad> quads = new ArrayList<>(4 + (this.baseQuads != null ? 4 : 0));
			if (side.getAxis().isHorizontal()) {
				if (this.baseQuads != null) {
					quads.addAll(this.baseQuads[side.get2DDataValue()]);
				}
			} else {
				int faceIndex = side.get3DDataValue();
				LoftyCarpetData data = extraData.get(DATA);
				for (int quad = 0; quad < 4; ++quad) {
					//if our model data is null (I really hope it isn't) we can skip connected textures since we dont have the info we need
					//i'd rather do this than crash the game or skip rendering the block entirely
					ConnectionLogic connectionType = data != null ? data.logic[faceIndex][quad] : ConnectionLogic.NONE;
					quads.add(this.quads[faceIndex][quad][connectionType.ordinal()]);
				}
			}

			return quads;
		} else {
			return List.of();
		}
	}

	@NotNull
	@Override
	public ModelData getModelData(@NotNull BlockAndTintGetter getter, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
		LoftyCarpetData data = new LoftyCarpetData();

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
		return Arrays.stream(this.validConnectors).anyMatch(neighborState::is) && Block.shouldRenderFace(neighborState, getter, pos, face, pos.relative(face));
	}

	private boolean isCornerBlockPresent(BlockAndTintGetter getter, BlockPos pos, Direction face, Direction side1, Direction side2) {
		BlockState neighborState = getter.getBlockState(pos.relative(side1).relative(side2));
		return Arrays.stream(this.validConnectors).anyMatch(neighborState::is) && Block.shouldRenderFace(neighborState, getter, pos, face, pos.relative(face));
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean usesBlockLight() {
		return true;
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	@NotNull
	@Override
	public TextureAtlasSprite getParticleIcon() {
		return this.particle;
	}

	@NotNull
	@Override
	public ItemOverrides getOverrides() {
		return this.overrides;
	}

	@NotNull
	@Override
	public ItemTransforms getTransforms() {
		return this.transforms;
	}

	@NotNull
	@Override
	public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
		return this.blockRenderTypes != null ? this.blockRenderTypes : IDynamicBakedModel.super.getRenderTypes(state, rand, data);
	}

	@NotNull
	@Override
	public List<RenderType> getRenderTypes(@NotNull ItemStack stack, boolean fabulous) {
		if (!fabulous) {
			if (this.itemRenderTypes != null) {
				return this.itemRenderTypes;
			}
		} else if (this.fabulousItemRenderTypes != null) {
			return this.fabulousItemRenderTypes;
		}

		return IDynamicBakedModel.super.getRenderTypes(stack, fabulous);
	}

	//we need a class to make model data. Fine, here you go
	private static final class LoftyCarpetData {
		private final ConnectionLogic[][] logic = new ConnectionLogic[6][4];

		private LoftyCarpetData() {
		}
	}
}