package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.KeepsakeCasketModel;
import twilightforest.init.TFBlocks;

public class SkullChestRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {
	public static final ResourceLocation SKULL_CHEST_TEXTURE = TwilightForestMod.getModelTexture("casket/skull_chest.png");

	private final KeepsakeCasketModel model;

	public SkullChestRenderer(BlockEntityRendererProvider.Context context) {
		this(context, TFModelLayers.SKULL_CHEST);
	}

	public SkullChestRenderer(BlockEntityRendererProvider.Context context, ModelLayerLocation layer) {
		this.model = new KeepsakeCasketModel(context.bakeLayer(layer));
	}

	@Override
	public void render(T entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		BlockState blockstate = entity.getBlockState();

		ResourceLocation textureLocation = this.getTextureLocation(blockstate);
		Direction facing = blockstate.getValue(HorizontalDirectionalBlock.FACING);

		renderCasket(entity.getOpenNess(partialTicks), stack, buffer, light, overlay, textureLocation, facing, this.model);
	}

	public static void renderCasket(float lidRotation, PoseStack stack, MultiBufferSource buffer, int light, int overlay, ResourceLocation texture, Direction facing, KeepsakeCasketModel model) {
		stack.pushPose();
		stack.translate(0.5F, 0.0F, 0.5F);
		stack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
		stack.scale(1.0F, -1.0F, -1.0F);

		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;

		model.setupAnim(lidRotation);
		model.renderToBuffer(stack, buffer.getBuffer(model.renderType(texture)), light, overlay);
		stack.popPose();
	}

	protected ResourceLocation getTextureLocation(BlockState state) {
		return SKULL_CHEST_TEXTURE;
	}
}
