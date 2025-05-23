package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.enchantment.ApplyFrostedEffect;
import twilightforest.enchantment.RechargeScepterEffect;
import twilightforest.enchantment.SmashBlocksEffect;

public class TFEnchantmentEffects {

	public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENTITY_EFFECTS = DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<MapCodec<? extends EnchantmentEntityEffect>, MapCodec<ApplyFrostedEffect>> APPLY_FROSTED = ENTITY_EFFECTS.register("apply_frosted", () -> ApplyFrostedEffect.CODEC);
	public static final DeferredHolder<MapCodec<? extends EnchantmentEntityEffect>, MapCodec<RechargeScepterEffect>> RECHARGE_SCEPTER = ENTITY_EFFECTS.register("recharge_scepter", () -> RechargeScepterEffect.CODEC);
	public static final DeferredHolder<MapCodec<? extends EnchantmentEntityEffect>, MapCodec<SmashBlocksEffect>> SMASH_BLOCKS = ENTITY_EFFECTS.register("smash_blocks", () -> SmashBlocksEffect.CODEC);
}
