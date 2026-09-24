package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.*;
import twilightforest.TwilightForestMod;

public class TFEnchantments {
	public static final ResourceKey<Enchantment> FIRE_REACT = registerKey("fire_react");
	public static final ResourceKey<Enchantment> CHILL_AURA = registerKey("chill_aura");
	public static final ResourceKey<Enchantment> DESTRUCTION = registerKey("destruction");
	public static final ResourceKey<Enchantment> RENEWAL = registerKey("renewal");

	private static ResourceKey<Enchantment> registerKey(String name) {
		return ResourceKey.create(Registries.ENCHANTMENT, TwilightForestMod.prefix(name));
	}

}
