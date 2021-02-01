package twilightforest.entity.boss;

import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.entity.MultiPartEntityPart;

public class EntityTFSnowQueenIceShield extends MultiPartEntityPart {

    public EntityTFSnowQueenIceShield(EntityType<? extends EntityTFSnowQueenIceShield> type, World world) {
        super(type, world);
        this.ignoreFrustumCheck = true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        playSound(TFSounds.SNOW_QUEEN_BREAK, 1.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        return false;
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

    @Override
    public boolean canRemove() {
        return super.canRemove();
    }
}
