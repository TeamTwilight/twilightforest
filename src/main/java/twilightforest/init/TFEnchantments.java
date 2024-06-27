package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.enchantment.ChillAuraEnchantment;
import twilightforest.enchantment.DestructionEnchantment;
import twilightforest.enchantment.FireReactEnchantment;

public class TFEnchantments {
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, TwilightForestMod.ID);

	public static final DeferredHolder<Enchantment, Enchantment> FIRE_REACT = ENCHANTMENTS.register("fire_react", () -> new FireReactEnchantment(4));
	public static final DeferredHolder<Enchantment, Enchantment> CHILL_AURA = ENCHANTMENTS.register("chill_aura", () -> new ChillAuraEnchantment(4));
	public static final DeferredHolder<Enchantment, Enchantment> DESTRUCTION = ENCHANTMENTS.register("destruction", () -> new DestructionEnchantment(3));
}
