package twilightforest.entity.finalcastle;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityTFCastleGuardian extends EntityCreature { // Not exactly living but requires the logic >.>
    public EntityTFCastleGuardian(World worldIn) {
        super(worldIn);

        this.setSize(1.8f, 2.4f);
    }

    @Override
    protected void initEntityAI() {
        //this.tasks.addTask(0, new EntityAIAvoidEntity<>(this, EntityPlayer.class, 10.0F, 0.5F, 0.5F));
        //this.tasks.addTask(1, new EntityAIWanderAvoidWater(this, 0.5, 0.5f));
        //this.tasks.addTask(2, new EntityAILookIdle(this));
    }

    @Override
    protected float updateDistance(float renderYawOffset, float p_110146_2_) {
        float f = MathHelper.wrapDegrees(renderYawOffset - this.renderYawOffset);
        this.renderYawOffset += f * 0.5F;
        float f1 = MathHelper.wrapDegrees(this.rotationYaw - this.renderYawOffset);
        boolean flag = f1 < -90.0F || f1 >= 90.0F;

        if (f1 < -75.0F)
            f1 = -75.0F;

        if (f1 >= 75.0F)
            f1 = 75.0F;

        this.renderYawOffset = this.rotationYaw - f1;

        if (f1 * f1 > 2500.0F)
            this.renderYawOffset += f1 * 0.5F;

        if (flag)
            p_110146_2_ *= -1.0F;

        return p_110146_2_;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return false;
    }

    @Override // So it won't be allowed to be duplicated by Mod mob spawners
    public boolean isNonBoss() {
        return false;
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public float getEyeHeight() {
        return 1.865f;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return false;
    }


}
