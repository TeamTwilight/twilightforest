package twilightforest.entity.projectile;

import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFConfig;
import twilightforest.TFSounds;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFEntities;

import javax.annotation.Nullable;

//railgun time
//falling blocks dont work well with bugs, so I made this
public class EntityTFCicadaShot extends EntityTFThrowable {

    public EntityTFCicadaShot(EntityType<? extends EntityTFCicadaShot> type, World world) {
        super(type, world);
    }

    public EntityTFCicadaShot(World worldIn, @Nullable LivingEntity living, double x, double y, double z) {
        super(TFEntities.cicada_shot, worldIn);
        float yaw = living != null ? living.rotationYaw : 0;
        float pitch = living !=null ? living.rotationPitch : 0;
        this.setLocationAndAngles(living.getPosX(), living.getPosY() + living.getEyeHeight(), living.getPosZ(), yaw, pitch);
        this.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
        setMotion(x, y, z);
        setShooter(living);
        Vector3d motion = getMotion();
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
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public float getCollisionBorderSize() {
        return 1.0F;
    }

    @Override
    protected float getGravityVelocity() {
        return 0.03F;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, TFBlocks.cicada.get().getDefaultState()), false, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
            }
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @Override
    protected void onImpact(RayTraceResult ray) {
        if (!world.isRemote) {
            if (ray instanceof BlockRayTraceResult) {
                BlockRayTraceResult blockray = (BlockRayTraceResult) ray;
                BlockPos pos = blockray.getPos().offset(blockray.getFace());
                BlockState currentState = world.getBlockState(pos);

                DirectionalPlaceContext context = new DirectionalPlaceContext(world, pos, blockray.getFace(), ItemStack.EMPTY, blockray.getFace().getOpposite());
                if (currentState.isReplaceable(context)) {
                    world.setBlockState(pos, TFBlocks.cicada.get().getDefaultState().with(DirectionalBlock.FACING, ((BlockRayTraceResult) ray).getFace()));
                } else {
                    ItemEntity squish = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ());
                    squish.entityDropItem(Items.GRAY_DYE);
                }
            }

            if (ray instanceof EntityRayTraceResult) {
                if (((EntityRayTraceResult)ray).getEntity() != null) {
                    ((EntityRayTraceResult)ray).getEntity().attackEntityFrom(new IndirectEntityDamageSource("cicada", this, null), 2);
                }
            }

            this.world.setEntityState(this, (byte) 3);
            this.remove();
        }
    }
}
