package twilightforest.capabilities.shield;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public interface IShieldCapability {

	ResourceLocation ID = TwilightForestMod.prefix("cap_shield");

	void setEntity(EntityLivingBase entity);

	void update();

	int shieldsLeft();

	int temporaryShieldsLeft();

	int permamentShieldsLeft();

	void breakShield();

	void replenishShields();

	void setShields(int amount, boolean temp);

    void addShields(int amount, boolean temp);
}
