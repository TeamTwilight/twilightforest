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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.block.KeepsakeCasketBlock;
import twilightforest.block.entity.KeepsakeCasketBlockEntity;
import twilightforest.client.model.TFModelLayers;
import twilightforest.init.TFBlocks;

/**
 * Keepsake Casket Model - MCVinnyq
 * Created using Tabula 8.0.0
 */
//Most of the other stuff is derived from ChestRenderer
public class KeepsakeCasketRenderer<T extends KeepsakeCasketBlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {

	private final ModelPart base;
	private final ModelPart lid;

	public KeepsakeCasketRenderer(BlockEntityRendererProvider.Context context) {
		var root = context.bakeLayer(TFModelLayers.KEEPSAKE_CASKET);

		this.base = root.getChild("base");
		this.lid = root.getChild("lid");
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
		Level world = entity.getLevel();
		boolean flag = world != null;
		BlockState blockstate = flag ? entity.getBlockState() : TFBlocks.KEEPSAKE_CASKET.get().defaultBlockState();
		Block block = blockstate.getBlock();
		if (block instanceof KeepsakeCasketBlock) {
			int damage = blockstate.getValue(KeepsakeCasketBlock.BREAKAGE);
			Direction facing = blockstate.getValue(HorizontalDirectionalBlock.FACING);

			stack.pushPose();

			stack.translate(0.5F, 0.0F, 0.5F);
			stack.mulPose(facing.getRotation());
			stack.mulPose(Axis.XP.rotationDegrees(90.0F));

			DoubleBlockCombiner.NeighborCombineResult<? extends KeepsakeCasketBlockEntity> result = DoubleBlockCombiner.Combiner::acceptNone;
			float f1 = result.apply(KeepsakeCasketBlock.getLidRotationCallback(entity)).get(partialTicks);
			f1 = 1.0F - f1;
			f1 = 1.0F - f1 * f1 * f1;

			ResourceLocation casket = TwilightForestMod.getModelTexture("casket/keepsake_casket_" + damage + ".png");
			this.renderModels(stack, buffer.getBuffer(RenderType.entityCutoutNoCull(casket)), this.lid, this.base, f1, light, overlay);
			stack.popPose();
		}
	}

	private void renderModels(PoseStack stack, VertexConsumer buffer, ModelPart lid, ModelPart base, float lidAngle, int light, int overlay) {
		lid.xRot = -(lidAngle * Mth.HALF_PI);
		lid.render(stack, buffer, light, overlay);
		base.render(stack, buffer, light, overlay);
	}
}
