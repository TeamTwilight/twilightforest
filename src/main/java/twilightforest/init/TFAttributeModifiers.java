package twilightforest.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import twilightforest.TwilightForestMod;

public class TFAttributeModifiers {
	public static final AttributeModifier TRAVELLERS_HIGH_STEP = new AttributeModifier(TwilightForestMod.prefix("travellers_gear.boots_high_step"), 0.5F, AttributeModifier.Operation.ADD_VALUE);
	public static final AttributeModifier TRAVELLERS_SWIFT_SWIM = new AttributeModifier(TwilightForestMod.prefix("travellers_gear.vest_fast_swimming"), 1F, AttributeModifier.Operation.ADD_VALUE);

	public static final AttributeModifier TRAVELLERS_AQUATIC_AGILITY_OXYGEN = new AttributeModifier(TwilightForestMod.prefix("travellers_gear.goggles_aquatic_agility_oxygen"), 3.0F, AttributeModifier.Operation.ADD_VALUE);
	public static final AttributeModifier TRAVELLERS_AQUATIC_AGILITY_MINING = new AttributeModifier(TwilightForestMod.prefix("travellers_gear.goggles_aquatic_agility_mining"), 4.0F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

	public static final ResourceLocation FORWARD_BOOTS_ATTRIBUTE_MODIFIER_LOCATION = TwilightForestMod.prefix("travellers_gear.boots_forward_boost");
}
