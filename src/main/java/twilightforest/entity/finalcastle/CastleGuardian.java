package twilightforest.entity.finalcastle;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class CastleGuardian extends PathfinderMob { // Not exactly living but requires the logic >.>
    public CastleGuardian(EntityType<? extends CastleGuardian> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, EntityPlayer.class, 10.0F, 0.5F, 0.5F));
        //this.goalSelector.addGoal(1, new WaterAvoidingRandomWalkingGoal(this, 0.5, 0.5f));
        //this.goalSelector.addGoal(2, new LookRandomlyGoal(this));
    }

    @Override
    protected float tickHeadTurn(float renderYawOffset, float p_110146_2_) {
        float f = Mth.wrapDegrees(renderYawOffset - this.yBodyRot);
        this.yBodyRot += f * 0.5F;
        float f1 = Mth.wrapDegrees(this.getYRot() - this.yBodyRot);
        boolean flag = f1 < -90.0F || f1 >= 90.0F;

        if (f1 < -75.0F)
            f1 = -75.0F;

        if (f1 >= 75.0F)
            f1 = 75.0F;

        this.yBodyRot = this.getYRot() - f1;

        if (f1 * f1 > 2500.0F)
            this.yBodyRot += f1 * 0.5F;

        if (flag)
            p_110146_2_ *= -1.0F;

        return p_110146_2_;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected boolean canRide(Entity entityIn) {
        return false;
    }

    @Override // So it won't be allowed to be duplicated by Mod mob spawners
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
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
