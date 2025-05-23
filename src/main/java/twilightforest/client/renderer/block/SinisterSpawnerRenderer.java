package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SpawnerRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.block.entity.spawner.SinisterSpawnerBlockEntity;

// [VANILLA COPY] SpawnerRenderer (Type bound changed to CursedSpawnerEntity)
@OnlyIn(Dist.CLIENT)
public class SinisterSpawnerRenderer implements BlockEntityRenderer<SinisterSpawnerBlockEntity> {
    private final EntityRenderDispatcher entityRenderer;

    public SinisterSpawnerRenderer(BlockEntityRendererProvider.Context context) {
        this.entityRenderer = context.getEntityRenderer();
    }

    @Override
	public void render(SinisterSpawnerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();
        if (level != null) {
            BaseSpawner basespawner = blockEntity.getSpawner();
            Entity entity = basespawner.getOrCreateDisplayEntity(level, blockEntity.getBlockPos());
            if (entity != null) {
				SpawnerRenderer.renderEntityInSpawner(partialTick, poseStack, bufferSource, packedLight, entity, this.entityRenderer, basespawner.getoSpin(), basespawner.getSpin());
            }
        }
    }

    @Override
    public AABB getRenderBoundingBox(SinisterSpawnerBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX() - 1.0, pos.getY() - 1.0, pos.getZ() - 1.0, pos.getX() + 2.0, pos.getY() + 2.0, pos.getZ() + 2.0);
    }
}
