package twilightforest.client.model.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class BuiltInItemModel implements IBakedModel {

	private class Overrides extends ItemOverrideList {

		Overrides() {
			super(/*Collections.emptyList()*/);
		}

		@Nullable
		@Override
		public IBakedModel getOverrideModel(IBakedModel originalModel, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity) {
			setItemStack(stack);
			return BuiltInItemModel.this;
		}
	}

	private final TextureAtlasSprite particleTexture;
	private final ItemOverrideList overrides = new Overrides();

	protected BuiltInItemModel(String particleTextureName) {
		this.particleTexture = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(particleTextureName));
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
		return Collections.emptyList();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return true;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return particleTexture;
	}

	@Override
	public boolean isSideLit() {
		return true; //FIXME
	}

	@Override
	public ItemOverrideList getOverrides() {
		return overrides;
	}

	@Override
	public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
		setTransform(cameraTransformType);
		return this;
	}

	protected abstract void setItemStack(ItemStack stack);

	protected abstract void setTransform(ItemCameraTransforms.TransformType transform);
}
