package twilightforest.entity.boss;

import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;

public class EntityTFSnowQueenIceShield extends MultiPartEntityPart {
	public EntityTFSnowQueenIceShield(EntityTFSnowQueen goblin) {
		super(goblin, "stronghold_shield", 0.75F, 0.75F);
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		return false;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.ticksExisted++;

		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;

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
