package twilightforest.compat.ie;

import blusunrize.immersiveengineering.client.ClientUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.client.TFClientEvents;
import twilightforest.client.shader.ShaderManager;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static twilightforest.TFConstants.MOD_ID;

public class ShaderBagItemModel implements BakedModel {

	protected final BakedModel delegate;
	protected final ItemStack item;
	private final ResourceLocation bg = TwilightForestMod.prefix("textures/items/star_burst_mask.png");
	ModelResourceLocation backModelLocation = new ModelResourceLocation(MOD_ID + ":trophy_minor", "inventory");


	public ShaderBagItemModel(BakedModel delegate, ItemStack item) {
		this.delegate = delegate;
		this.item = item;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pSide, Random pRand) {
		return this.delegate.getQuads(pState, pSide, pRand);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return delegate.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return delegate.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return delegate.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return delegate.isCustomRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return delegate.getParticleIcon();
	}

	@Override
	public ItemOverrides getOverrides() {
		return delegate.getOverrides();
	}

	@Override
	public BakedModel handlePerspective(ItemTransforms.TransformType transform, PoseStack ms) {

		BakedModel modelBack = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getModelManager().getModel(backModelLocation);

		if (transform == ItemTransforms.TransformType.GUI) {

			Lighting.setupForFlatItems();
			MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
			ms.pushPose();

			ms.translate(0.0F, 0.0F, -1.5F);
			Minecraft.getInstance().getItemRenderer().render(item, ItemTransforms.TransformType.GUI, false, ms, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, ForgeHooksClient.handleCameraTransforms(ms, modelBack, transform, false));

			ms.popPose();

			ms.pushPose();

			ms.translate(0.0F, 0.0F, 2.0F);
			// Rotate the lunchbox if we're in the Gui. This is a setup for the next bit of rendering.
			ms.mulPose(Vector3f.XN.rotationDegrees(TFConfig.CLIENT_CONFIG.rotateTrophyHeadsGui.get() ? Mth.sin(TFClientEvents.rotationTicker * 0.125F) : 30));
			ms.mulPose(Vector3f.YN.rotationDegrees(TFConfig.CLIENT_CONFIG.rotateTrophyHeadsGui.get() ? TFClientEvents.rotationTicker : 45));
			ms.mulPose(Vector3f.ZN.rotationDegrees(TFConfig.CLIENT_CONFIG.rotateTrophyHeadsGui.get() ? Mth.sin(TFClientEvents.rotationTicker * 0.125F) : 0));
			ms.translate(0.0F, -0.1F, 0.0F);
			ms.scale(1.25F, 1.25F, 1.25F);

			// Render the lunchbox
			Minecraft.getInstance().getItemRenderer().render(item, ItemTransforms.TransformType.GUI, false, ms, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, ForgeHooksClient.handleCameraTransforms(ms, delegate, transform, false));

			ms.popPose();

			bufferSource.endBatch();
			Lighting.setupFor3DItems();

			// Render the star burst
			ms.pushPose();
			// Since we're in a new stack different than above rendering calls, we'll also need to re-translate.
			// Z value -1 puts us behind the lunchbox but in front of trophy sprite
			ms.translate(0.5F, 0.5F, -1.0F);

			Tesselator tessellator = Tesselator.getInstance();
			BufferBuilder buffer = tessellator.getBuilder();

			// Bind the star burst mask tex
			RenderSystem.setShaderTexture(0, bg);

			// Just gonna borrow your code for a sec blu, thnx
			int c = ClientUtils.getDarkenedTextColour(item.getRarity().color.getColor());

			// unpack colors
			float r = (c >> 16 & 0xFF) / 255.0f;
			float g = (c >> 8 & 0xFF) / 255.0f;
			float b = (c & 0xFF) / 255.0f;

			buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
			buffer.vertex(-1, 1, 0)
					.uv(0, 1)
					.color(r, g, b, 1f)
					.endVertex();
			buffer.vertex(1, 1, 0)
					.uv(1, 1)
					.color(r, g, b, 1f)
					.endVertex();
			buffer.vertex(1, -1, 0)
					.uv(1, 0)
					.color(r, g, b, 1f)
					.endVertex();
			buffer.vertex(-1, -1, 0)
					.uv(0, 0)
					.color(r, g, b, 1f)
					.endVertex();

			// Shader, engage!
			ShaderManager.useShader(ShaderManager.starburstShader, ShaderManager.Uniforms.TIME);
			// Blur the star burst mask
			GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			tessellator.end();
			// Deblur, so we don't blur all of the textures in rendering calls afterwards
			GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			// Disengage shader
			ShaderManager.releaseShader();

			// reset color
			RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

			ms.popPose();

			return ForgeHooksClient.handlePerspective(new DummyModel(), transform, ms);
		} else {
			return ForgeHooksClient.handlePerspective(delegate, transform, ms);
		}
	}

	//dummy class to kill off item rendering in the GUI
	public static class DummyModel implements BakedModel {
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pSide, Random pRand) { return Collections.emptyList(); }
		@Override
		public boolean useAmbientOcclusion() {return false;}
		@Override
		public boolean isGui3d() {return false;}
		@Override
		public boolean usesBlockLight() {return false;}
		@Override
		public boolean isCustomRenderer() {return false;}
		@Override
		public TextureAtlasSprite getParticleIcon() {return null;}
		@Override
		public ItemOverrides getOverrides() {return null;}
	}
}
