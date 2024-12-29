package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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
import twilightforest.init.TFBlocks;

/**
 * Keepsake Casket Model - MCVinnyq
 * Created using Tabula 8.0.0
 */
//Most of the other stuff is derived from ChestRenderer
public class SkullChestRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {
	private static final ResourceLocation SKULL_CHEST_TEXTURE = TwilightForestMod.getModelTexture("casket/skull_chest.png");

	private final ModelPart base;
	private final ModelPart lid;

	private final BlockState defaultState;

	public SkullChestRenderer(BlockEntityRendererProvider.Context context) {
		this(context, TFBlocks.SKULL_CHEST.get().defaultBlockState());
	}

	public SkullChestRenderer(BlockEntityRendererProvider.Context context, BlockState defaultState) {
		var root = context.bakeLayer(TFModelLayers.KEEPSAKE_CASKET);

		this.base = root.getChild("base");
		this.lid = root.getChild("lid");
		this.defaultState = defaultState;
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("lid",
			CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-8.0F, -8.0F, -13.0F, 16.0F, 10.0F, 14.0F)
				.texOffs(0, 46)
				.addBox(-8.0F, -10.0F, -13.0F, 16.0F, 2.0F, 0.0F)
				.texOffs(2, 34)
				.addBox(-7.99F, -10.0F, -12.0F, 0.0F, 2.0F, 14.0F)
				.texOffs(2, 36)
				.addBox(7.99F, -10.0F, -12.0F, 0.0F, 2.0F, 14.0F),
			PartPose.offset(0.0F, -6.0F, 6.0F));
		partdefinition.addOrReplaceChild("base",
			CubeListBuilder.create()
				.texOffs(1, 28)
				.addBox(-7.0F, -10.0F, -2.0F, 14.0F, 10.0F, 8.0F)
				.texOffs(0, 26)
				.addBox(-7.0F, -10.0F, -6.0F, 1.0F, 6.0F, 4.0F)
				.texOffs(40, 26)
				.addBox(6.0F, -10.0F, -6.0F, 1.0F, 6.0F, 4.0F)
				.texOffs(0, 56)
				.addBox(-7.0F, -4.0F, -6.0F, 14.0F, 4.0F, 4.0F),
			PartPose.offset(0.0F, -0.01F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}


	@Override
	public void render(T entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		BlockState blockstate = entity.getLevel() != null ? entity.getBlockState() : this.defaultState;

		ResourceLocation textureLocation = this.getTextureLocation(blockstate);
		Direction facing = blockstate.getValue(HorizontalDirectionalBlock.FACING);

		this.renderCasket(entity.getOpenNess(partialTicks), stack, buffer, light, overlay, textureLocation, facing);
	}

	public void renderCasket(float lidRotation, PoseStack stack, MultiBufferSource buffer, int light, int overlay, ResourceLocation texture, Direction facing) {
		stack.pushPose();
		stack.translate(0.5F, 0.0F, 0.5F);
		stack.mulPose(facing.getRotation());
		stack.mulPose(Axis.XP.rotationDegrees(90.0F));

		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;

		this.renderModels(stack, buffer.getBuffer(RenderType.entityCutoutNoCull(texture)), this.lid, this.base, lidRotation, light, overlay);
		stack.popPose();
	}

	private void renderModels(PoseStack stack, VertexConsumer buffer, ModelPart lid, ModelPart base, float lidAngle, int light, int overlay) {
		lid.xRot = lidAngle * -Mth.HALF_PI;
		lid.render(stack, buffer, light, overlay);
		base.render(stack, buffer, light, overlay);
	}

	@NotNull
	protected ResourceLocation getTextureLocation(BlockState blockstate) {
		return SKULL_CHEST_TEXTURE;
	}

	@NotNull
	public ResourceLocation getTextureLocation(int damage) {
		return SKULL_CHEST_TEXTURE;
	}
}
