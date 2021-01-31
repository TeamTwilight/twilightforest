package twilightforest.entity.finalcastle;

import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityTFCastleGuardian extends CreatureEntity { // Not exactly living but requires the logic >.>
    public EntityTFCastleGuardian(EntityType<? extends EntityTFCastleGuardian> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, EntityPlayer.class, 10.0F, 0.5F, 0.5F));
        //this.goalSelector.addGoal(1, new WaterAvoidingRandomWalkingGoal(this, 0.5, 0.5f));
        //this.goalSelector.addGoal(2, new LookRandomlyGoal(this));
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

    /* FIXME
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getBoundingBox();
    }*/

    // To go with the fix me above. I think this is the solution?
    @Override
    public boolean func_241845_aY() {
        return true;
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
    public float getEyeHeight(Pose pose) {
        return 1.865f;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return false;
    }
}
