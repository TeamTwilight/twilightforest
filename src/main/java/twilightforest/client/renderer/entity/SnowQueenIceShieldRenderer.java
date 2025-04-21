package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.data.ModelData;
import twilightforest.entity.boss.SnowQueenIceShield;

public class SnowQueenIceShieldRenderer extends EntityRenderer<SnowQueenIceShield, EntityRenderState> {

	private final BlockRenderDispatcher dispatcher;

	public SnowQueenIceShieldRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.dispatcher = context.getBlockRenderDispatcher();
	}

	@Override
	public void render(EntityRenderState state, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();
		stack.translate(-0.5D, 0.0, -0.5D);
		this.dispatcher.renderSingleBlock(Blocks.PACKED_ICE.defaultBlockState(), stack, buffer, light, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
		stack.popPose();
		super.render(state, stack, buffer, light);
	}

	@Override
	public EntityRenderState createRenderState() {
		return new EntityRenderState();
	}
}
