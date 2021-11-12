package twilightforest.entity.projectile;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFEntities;

import javax.annotation.Nullable;

//railgun time
//falling blocks dont work well with bugs, so I made this
public class CicadaShot extends TFThrowable {

    public CicadaShot(EntityType<? extends CicadaShot> type, Level world) {
        super(type, world);
    }

    public CicadaShot(Level worldIn, @Nullable LivingEntity living, double x, double y, double z) {
        super(TFEntities.CICADA_SHOT, worldIn);
        float yaw = living != null ? living.getYRot() : 0;
        float pitch = living != null ? living.getXRot() : 0;
        this.moveTo(living.getX(), living.getY() + living.getEyeHeight(), living.getZ(), yaw, pitch);
        this.setPos(this.getX(), this.getY(), this.getZ());
        setDeltaMovement(x, y, z);
        setOwner(living);
        Vec3 motion = getDeltaMovement();
        this.shoot(motion.x, motion.y, motion.z, 2*1.5F, 1.0F);
    }



    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public float getPickRadius() {
        return 1.0F;
    }

    @Override
    protected float getGravity() {
        return 0.03F;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, TFBlocks.CICADA.get().defaultBlockState()), false, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void onHit(HitResult ray) {
        if (!level.isClientSide) {
            if (ray instanceof BlockHitResult) {
                BlockHitResult blockray = (BlockHitResult) ray;
                BlockPos pos = blockray.getBlockPos().relative(blockray.getDirection());
                BlockState currentState = level.getBlockState(pos);

                DirectionalPlaceContext context = new DirectionalPlaceContext(level, pos, blockray.getDirection(), ItemStack.EMPTY, blockray.getDirection().getOpposite());
                if (currentState.canBeReplaced(context)) {
                    level.setBlockAndUpdate(pos, TFBlocks.CICADA.get().defaultBlockState().setValue(DirectionalBlock.FACING, ((BlockHitResult) ray).getDirection()));
                } else {
                    ItemEntity squish = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), Items.GRAY_DYE.getDefaultInstance());
                    squish.spawnAtLocation(squish.getItem());
                }
            }

            if (ray instanceof EntityHitResult) {
                if (((EntityHitResult)ray).getEntity() != null) {
                    ((EntityHitResult)ray).getEntity().hurt(new IndirectEntityDamageSource("cicada", this, null), 2);
                }
            }

            this.level.broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }
}
