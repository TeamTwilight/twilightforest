package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;

public class TFAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, TwilightForestMod.ID);

	public static final DeferredHolder<Attribute, Attribute> CLONE_COUNT = ATTRIBUTES.register("clone_count", () -> new RangedAttribute("attribute.name.lich.clone_count", 2, 0, 1024).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> SHIELD_STRENGTH = ATTRIBUTES.register("max_shield_strength", () -> new RangedAttribute("attribute.name.lich.shield_strength", 6, 0, 1024).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> MINION_COUNT = ATTRIBUTES.register("minion_count", () -> new RangedAttribute("attribute.name.lich.minion_count", 9, 0, 1024).setSyncable(true));
}
