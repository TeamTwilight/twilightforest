package twilightforest.capabilities.shield;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public interface IShieldCapability {

	ResourceLocation ID = TwilightForestMod.prefix("cap_shield");

	void setEntity(LivingEntity entity);

	void update();

	int shieldsLeft();

	int temporaryShieldsLeft();

	int permanentShieldsLeft();

	void breakShield();

	void replenishShields();

	void setShields(int amount, boolean temp);

    void addShields(int amount, boolean temp);
}
