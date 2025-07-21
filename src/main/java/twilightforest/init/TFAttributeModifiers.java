package twilightforest.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import twilightforest.TwilightForestMod;

public class TFAttributeModifiers {
	public static final AttributeModifier TRAVELLERS_HIGH_STEP_ACTIVE = new AttributeModifier(TwilightForestMod.prefix("travellers_gear.boots_high_step_active"), 0.5F, AttributeModifier.Operation.ADD_VALUE);
	public static final AttributeModifier TRAVELLERS_HIGH_STEP_DEACTIVATED = new AttributeModifier(TwilightForestMod.prefix("travellers_gear.boots_high_step_deactivated"), 0F, AttributeModifier.Operation.ADD_VALUE);
	public static final AttributeModifier TRAVELLERS_SWIFT_SWIM_ACTIVATE = new AttributeModifier(TwilightForestMod.prefix("travellers_gear.vest_fast_swimming_active"), 1F, AttributeModifier.Operation.ADD_VALUE);
	public static final AttributeModifier TRAVELLERS_SWIFT_SWIM_DEACTIVATED = new AttributeModifier(TwilightForestMod.prefix("travellers_gear.vest_fast_swimming_deactivated"), 0F, AttributeModifier.Operation.ADD_VALUE);

	public static final AttributeModifier TRAVELLERS_AQUATIC_AGILITY_OXYGEN_ACTIVE = new AttributeModifier(TwilightForestMod.prefix("travellers_gear.goggles_aquatic_agility_oxygen_active"), 3.0F, AttributeModifier.Operation.ADD_VALUE);
	public static final AttributeModifier TRAVELLERS_AQUATIC_AGILITY_OXYGEN_DEACTIVATED = new AttributeModifier(TwilightForestMod.prefix("travellers_gear.goggles_aquatic_agility_oxygen_deactivated"), 0.0F, AttributeModifier.Operation.ADD_VALUE);
	public static final AttributeModifier TRAVELLERS_AQUATIC_AGILITY_MINING_ACTIVE = new AttributeModifier(TwilightForestMod.prefix("travellers_gear.goggles_aquatic_agility_mining_active"), 4.0F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	public static final AttributeModifier TRAVELLERS_AQUATIC_AGILITY_MINING_DEACTIVATED = new AttributeModifier(TwilightForestMod.prefix("travellers_gear.goggles_aquatic_agility_mining_deactivated"), 0.0F, AttributeModifier.Operation.ADD_VALUE);

	public static final ResourceLocation FORWARD_BOOTS_ATTRIBUTE_MODIFIER_LOCATION = TwilightForestMod.prefix("travellers_gear.boots_forward_boost");
}
