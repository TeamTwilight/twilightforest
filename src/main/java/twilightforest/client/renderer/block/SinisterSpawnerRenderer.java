package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.SpawnerRenderState;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import twilightforest.block.entity.spawner.SinisterSpawnerBlockEntity;

// [VANILLA COPY] SpawnerRenderer (Type bound changed to CursedSpawnerEntity)
public class SinisterSpawnerRenderer implements BlockEntityRenderer<SinisterSpawnerBlockEntity, SpawnerRenderState> {
    private final EntityRenderDispatcher entityRenderer;

    public SinisterSpawnerRenderer(BlockEntityRendererProvider.Context context) {
        this.entityRenderer = context.entityRenderer();
    }

	@Override
	public SpawnerRenderState createRenderState() {
		return new SpawnerRenderState();
	}

	@Override
	public void extractRenderState(SinisterSpawnerBlockEntity blockEntity, SpawnerRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		if (blockEntity.getLevel() != null) {
			BaseSpawner spawner = blockEntity.getSpawner();
			Entity displayEntity = spawner.getOrCreateDisplayEntity(blockEntity.getLevel(), blockEntity.getBlockPos());
			extractSpawnerData(state, partialTicks, displayEntity, this.entityRenderer, spawner.getOSpin(), spawner.getSpin());
		}
	}

	static void extractSpawnerData(SpawnerRenderState state, float partialTicks, @Nullable Entity displayEntity, EntityRenderDispatcher entityRenderer, double oSpin, double spin) {
		if (displayEntity != null) {
			state.displayEntity = entityRenderer.extractEntity(displayEntity, partialTicks);
			state.displayEntity.lightCoords = state.lightCoords;
			state.spin = (float) Mth.lerp(partialTicks, oSpin, spin) * 10.0F;
			state.scale = 0.53125F;
			float maxLength = Math.max(displayEntity.getBbWidth(), displayEntity.getBbHeight());
			if (maxLength > 1.0D) {
				state.scale /= maxLength;
			}
		}
	}

	public void submit(SpawnerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		if (state.displayEntity != null) {
			submitEntityInSpawner(poseStack, submitNodeCollector, state.displayEntity, this.entityRenderer, state.spin, state.scale, camera);
		}
	}

	public static void submitEntityInSpawner(PoseStack stack, SubmitNodeCollector collector, EntityRenderState displayEntity, EntityRenderDispatcher entityRenderer, float spin, float scale, CameraRenderState camera) {
		stack.pushPose();
		stack.translate(0.5F, 0.4F, 0.5F);
		stack.mulPose(Axis.YP.rotationDegrees(spin));
		stack.translate(0.0F, -0.2F, 0.0F);
		stack.mulPose(Axis.XP.rotationDegrees(-30.0F));
		stack.scale(scale, scale, scale);
		entityRenderer.submit(displayEntity, camera, 0.0D, 0.0D, 0.0D, stack, collector);
		stack.popPose();
	}

	@Override
	public AABB getRenderBoundingBox(SinisterSpawnerBlockEntity entity) {
		BlockPos pos = entity.getBlockPos();
		return new AABB(pos.getX() - 1.0D, pos.getY() - 1.0D, pos.getZ() - 1.0D, pos.getX() + 2.0D, pos.getY() + 2.0D, pos.getZ() + 2.0D);
	}
}
