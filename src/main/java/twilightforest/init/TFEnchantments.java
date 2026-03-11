package twilightforest.init;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.EnchantmentLevelProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.enchantment.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TFEnchantments {
	public static final ResourceKey<Enchantment> FIRE_REACT = registerKey("fire_react");
	public static final ResourceKey<Enchantment> CHILL_AURA = registerKey("chill_aura");
	public static final ResourceKey<Enchantment> DESTRUCTION = registerKey("destruction");
	public static final ResourceKey<Enchantment> RENEWAL = registerKey("renewal");

	private static ResourceKey<Enchantment> registerKey(String name) {
		return ResourceKey.create(Registries.ENCHANTMENT, TwilightForestMod.prefix(name));
	}

	public static void bootstrap(BootstrapContext<Enchantment> context) {
		HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
		HolderGetter<Item> items = context.lookup(Registries.ITEM);
		HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);

		register(context, FIRE_REACT, new Enchantment.Builder(Enchantment.definition(
			items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
			items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
			1,
			3,
			Enchantment.dynamicCost(5, 9),
			Enchantment.dynamicCost(20, 9),
			8,
			EquipmentSlotGroup.ARMOR)
		).exclusiveWith(HolderSet.direct(enchantments.getOrThrow(Enchantments.THORNS), enchantments.getOrThrow(CHILL_AURA)))
			.withEffect(EnchantmentEffectComponents.POST_ATTACK,
				EnchantmentTarget.VICTIM,
				EnchantmentTarget.ATTACKER,
				AllOf.entityEffects(
					new Ignite(LevelBasedValue.perLevel(2.0F, 3.0F)),
					new DamageItem(LevelBasedValue.constant(2.0F))),
				LootItemRandomChanceCondition.randomChance(EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(0.15F)))));

		register(context, CHILL_AURA, new Enchantment.Builder(Enchantment.definition(
			items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
			items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
			1,
			3,
			Enchantment.dynamicCost(5, 9),
			Enchantment.dynamicCost(20, 9),
			8,
			EquipmentSlotGroup.ARMOR)
		).exclusiveWith(HolderSet.direct(enchantments.getOrThrow(Enchantments.THORNS), enchantments.getOrThrow(FIRE_REACT)))
			.withEffect(EnchantmentEffectComponents.POST_ATTACK,
				EnchantmentTarget.VICTIM,
				EnchantmentTarget.ATTACKER,
				AllOf.entityEffects(
					new ApplyFrostedEffect(LevelBasedValue.constant(200), LevelBasedValue.perLevel(0.0F, 1.0F)),
					new DamageItem(LevelBasedValue.constant(2.0F))),
				LootItemRandomChanceCondition.randomChance(EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(0.15F)))));

		register(context, DESTRUCTION, new Enchantment.Builder(Enchantment.definition(
			items.getOrThrow(ItemTagGenerator.BLOCK_AND_CHAIN_ENCHANTABLE),
			1,
			3,
			Enchantment.dynamicCost(5, 9),
			Enchantment.dynamicCost(20, 9),
			8,
			EquipmentSlotGroup.HAND))
			.withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(LevelBasedValue.perLevel(-1.5F)))
			.withEffect(EnchantmentEffectComponents.HIT_BLOCK, new SmashBlocksEffect(
				LevelBasedValue.constant(12.0F),
				LevelBasedValue.constant(1.0F),
				Optional.of(blocks.get(BlockTagGenerator.BLOCK_AND_CHAIN_NEVER_BREAKS).orElseThrow()),
				Optional.empty(),
				Optional.empty())));

		register(context, RENEWAL, new Enchantment.Builder(Enchantment.definition(
			items.getOrThrow(ItemTagGenerator.SCEPTERS),
			1, 1,
			Enchantment.dynamicCost(5, 9),
			Enchantment.dynamicCost(20, 9),
			8,
			EquipmentSlotGroup.HAND))
			.withEffect(EnchantmentEffectComponents.TICK, new RechargeScepterEffect()));

	}

	private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
		context.register(key, builder.build(key.location()));
	}
}
