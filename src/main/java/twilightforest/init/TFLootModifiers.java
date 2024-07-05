package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.loot.modifiers.FieryToolSmeltingModifier;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;

public class TFLootModifiers {

	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, TwilightForestMod.ID);

	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<FieryToolSmeltingModifier>> FIERY_PICK_SMELTING = LOOT_MODIFIERS.register("fiery_pick_smelting", () -> FieryToolSmeltingModifier.CODEC);
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<GiantToolGroupingModifier>> GIANT_PICK_GROUPING = LOOT_MODIFIERS.register("giant_block_grouping", () -> GiantToolGroupingModifier.CODEC);
}
