package twilightforest.entity.boss;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.entity.PartEntity;
import twilightforest.TFSounds;

public class EntityTFSnowQueenIceShield extends PartEntity<EntityTFSnowQueen> {

    public EntityTFSnowQueenIceShield(EntityTFSnowQueen parent) {
        super(parent);
        size = EntitySize.flexible(0.75F, 0.75F);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        playSound(TFSounds.SNOW_QUEEN_BREAK, 1.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        return false;
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    protected void registerData() {

    }

    @Override
    public void tick() {
        super.tick();

        ++this.ticksExisted;

        lastTickPosX = getPosX();
        lastTickPosY = getPosY();
        lastTickPosZ = getPosZ();

        for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) {
        }
        for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) {
        }
        for (; rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) {
        }
        for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) {
        }
    }
}
