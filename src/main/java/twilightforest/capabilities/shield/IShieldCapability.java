package twilightforest.capabilities.shield;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public interface IShieldCapability {

	ResourceLocation ID = new ResourceLocation(TwilightForestMod.ID, "cap_shield");

	void setEntity(EntityLivingBase entity);

	void update();

	int shieldsLeft();

	void breakShield();

	void replenishShields();

	void setShields(int amount);

}
