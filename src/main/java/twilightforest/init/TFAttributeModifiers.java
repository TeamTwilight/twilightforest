package twilightforest.init;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import twilightforest.TwilightForestMod;

public class TFAttributeModifiers {
	public static final AttributeModifier TRAVELLERS_HIGH_STEP = new AttributeModifier(TwilightForestMod.prefix("travellers_gear.boots_high_step"), 0.5F, AttributeModifier.Operation.ADD_VALUE);
}
