package twilightforest.client.model.block.giantblock;

import com.google.common.collect.Iterables;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.block.GiantBlock;
import twilightforest.util.Vec2i;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GiantBlockModel implements IDynamicBakedModel {

	private static final ModelProperty<GiantBlockData> DATA = new ModelProperty<>();
	private static final FaceBakery FACE_BAKERY = new FaceBakery();

	private final TextureAtlasSprite[] textures;
	private final TextureAtlasSprite particle;
	private final ItemOverrides overrides;
	private final ItemTransforms transforms;
	private final ChunkRenderTypeSet blockRenderTypes;
	private final List<RenderType> itemRenderTypes;
	private final List<RenderType> fabulousItemRenderTypes;

	public GiantBlockModel(TextureAtlasSprite[] texture, TextureAtlasSprite particle, ItemOverrides overrides, ItemTransforms transforms, RenderTypeGroup group) {
		this.textures = texture;
		this.particle = particle;
		this.overrides = overrides;
		this.transforms = transforms;
		this.blockRenderTypes = !group.isEmpty() ? ChunkRenderTypeSet.of(group.block()) : null;
		this.itemRenderTypes = !group.isEmpty() ? List.of(group.entity()) : null;
		this.fabulousItemRenderTypes = !group.isEmpty() ? List.of(group.entityFabulous()) : null;
	}

	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
		List<BakedQuad> quads = new ArrayList<>();

		if (extraData.has(DATA) && side != null) {
			BlockPos pos = extraData.get(DATA).pos();
			Vec2i coords = this.calculateOffset(side, pos.offset(this.magicOffsetFromDir(side)));

			TextureAtlasSprite sprite = this.textures[this.textures.length > 1 ? side.ordinal() : 0];

			if (!Iterables.contains(GiantBlock.getVolume(pos), pos.offset(side.getNormal()))) {
				quads.add(FACE_BAKERY.bakeQuad(new Vector3f(0.0F, 0.0F, 0.0F), new Vector3f(16.0F, 16.0F, 16.0F), new BlockElementFace(side, side.ordinal(), side.name(), new BlockFaceUV(new float[]{0.0F + coords.x, 0.0F + coords.z, 4.0F + coords.x, 4.0F + coords.z}, 0)), sprite, side, BlockModelRotation.X0_Y0, null, true, new ResourceLocation(sprite.atlasLocation().getNamespace(), sprite.atlasLocation().getPath() + "_" + side.name().toLowerCase(Locale.ROOT))));
			}
		}

		return quads;
	}

	//based on the offsets written in the original giant block json
	private BlockPos magicOffsetFromDir(Direction side) {
		return switch (side) {
			default -> new BlockPos(0, 0, -1);
			case DOWN -> new BlockPos(0, 0, 2);
			case NORTH, SOUTH -> new BlockPos(0, 1, 0);
			case WEST, EAST -> new BlockPos(0, 1, -1);
		};
	}

	//an offset is calculated between 0 and 15 based on which side its on.
	//it then grabs the remainder after dividing by 4 and then multiply by 4 to scale correctly
	private Vec2i calculateOffset(Direction side, BlockPos pos) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		int offsetX, offsetY;

		if (side.getAxis().isVertical()) {
			offsetX = x % 4;
			offsetY = (side.getStepY() * z + 1) % 4;
		} else if (side.getAxis() == Direction.Axis.Z) {
			offsetX = x % 4;
			offsetY = -y % 4;
		} else {
			offsetX = (z + 1) % 4;
			offsetY = -y % 4;
		}

		if (side == Direction.NORTH || side == Direction.EAST) {
			offsetX = (4 - offsetX - 1) % 4;
		}

		if (offsetX < 0) {
			offsetX += 16;
		}
		if (offsetY < 0) {
			offsetY += 16;
		}

		return new Vec2i((offsetX % 4) * 4, (offsetY % 4) * 4);
	}

	@Override
	public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
		if (modelData == ModelData.EMPTY) {
			modelData = ModelData.builder().with(DATA, new GiantBlockData(level, pos)).build();
		}
		return modelData;
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

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return this.particle;
	}

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

	//modeldata holder
	public record GiantBlockData(BlockAndTintGetter getter, BlockPos pos) {}
}
