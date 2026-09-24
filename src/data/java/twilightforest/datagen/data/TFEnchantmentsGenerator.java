package twilightforest.datagen.data;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.AllOf;
import net.minecraft.world.item.enchantment.effects.ChangeItemDamage;
import net.minecraft.world.item.enchantment.effects.Ignite;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.EnchantmentLevelProvider;
import twilightforest.enchantment.ApplyFrostedEffect;
import twilightforest.enchantment.RechargeScepterEffect;
import twilightforest.enchantment.SmashBlocksEffect;
import twilightforest.init.TFEnchantments;
import twilightforest.tags.TFBlockTags;
import twilightforest.tags.TFItemTags;

import java.util.Optional;

public class TFEnchantmentsGenerator {

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);

        register(context, TFEnchantments.FIRE_REACT, new Enchantment.Builder(Enchantment.definition(
            items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
            items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
            1,
            3,
            Enchantment.dynamicCost(5, 9),
            Enchantment.dynamicCost(20, 9),
            8,
            EquipmentSlotGroup.ARMOR)
        ).exclusiveWith(HolderSet.direct(enchantments.getOrThrow(Enchantments.THORNS), enchantments.getOrThrow(TFEnchantments.CHILL_AURA)))
            .withEffect(EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.VICTIM,
                EnchantmentTarget.ATTACKER,
                AllOf.entityEffects(
                    new Ignite(LevelBasedValue.perLevel(2.0F, 3.0F)),
                    new ChangeItemDamage(LevelBasedValue.constant(2.0F))),
                LootItemRandomChanceCondition.randomChance(EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(0.15F)))));

        register(context, TFEnchantments.CHILL_AURA, new Enchantment.Builder(Enchantment.definition(
            items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
            items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
            1,
            3,
            Enchantment.dynamicCost(5, 9),
            Enchantment.dynamicCost(20, 9),
            8,
            EquipmentSlotGroup.ARMOR)
        ).exclusiveWith(HolderSet.direct(enchantments.getOrThrow(Enchantments.THORNS), enchantments.getOrThrow(TFEnchantments.FIRE_REACT)))
            .withEffect(EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.VICTIM,
                EnchantmentTarget.ATTACKER,
                AllOf.entityEffects(
                    new ApplyFrostedEffect(LevelBasedValue.constant(200), LevelBasedValue.perLevel(0.0F, 1.0F)),
                    new ChangeItemDamage(LevelBasedValue.constant(2.0F))),
                LootItemRandomChanceCondition.randomChance(EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(0.15F)))));

        register(context, TFEnchantments.DESTRUCTION, new Enchantment.Builder(Enchantment.definition(
            items.getOrThrow(TFItemTags.BLOCK_AND_CHAIN_ENCHANTABLE),
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
                Optional.of(blocks.get(TFBlockTags.BLOCK_AND_CHAIN_NEVER_BREAKS).orElseThrow()),
                Optional.empty(),
                Optional.empty())));

        register(context, TFEnchantments.RENEWAL, new Enchantment.Builder(Enchantment.definition(
            items.getOrThrow(TFItemTags.SCEPTERS),
            1, 1,
            Enchantment.dynamicCost(5, 9),
            Enchantment.dynamicCost(20, 9),
            8,
            EquipmentSlotGroup.HAND))
            .withEffect(EnchantmentEffectComponents.TICK, new RechargeScepterEffect()));

    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.identifier()));
    }

}
