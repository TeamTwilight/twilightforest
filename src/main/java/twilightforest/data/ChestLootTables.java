package twilightforest.data;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.EnchantWithLevels;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.functions.SetNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;
import twilightforest.loot.*;

import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChestLootTables implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {


    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> register) {
        register.accept(TFTreasure.USELESS_LOOT,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(Items.POPPY).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(Items.DANDELION).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(Items.FEATHER).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
                                .addEntry(ItemLootEntry.builder(Items.WHEAT_SEEDS).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))))
                                .addEntry(ItemLootEntry.builder(Items.FLINT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))))
                                .addEntry(ItemLootEntry.builder(Items.CACTUS).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))))
                                .addEntry(ItemLootEntry.builder(Items.SUGAR_CANE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(Items.SAND).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(Items.FLOWER_POT))
                                .addEntry(ItemLootEntry.builder(Items.BONE_MEAL))));

        register.accept(TFTreasure.basement.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                .addEntry(TableLootEntry.builder(TFTreasure.USELESS_LOOT).weight(25))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:water")))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.ROTTEN_FLESH).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.POISONOUS_POTATO).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.WHEAT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.POTATO).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.CARROT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.MELON_SLICE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.WATER_BUCKET).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.TORCH).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.MUSHROOM_STEW).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.MILK_BUCKET).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.MELON_SEEDS).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))).weight(75)))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(Items.BREAD).acceptFunction(SetCount.builder(RandomValueRange.of(1, 8))))
                                .addEntry(ItemLootEntry.builder(Items.COOKED_BEEF).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(Items.COOKED_PORKCHOP).acceptFunction(SetCount.builder(RandomValueRange.of(1, 8))))
                                .addEntry(ItemLootEntry.builder(Items.BAKED_POTATO).acceptFunction(SetCount.builder(RandomValueRange.of(1, 8))))
                                .addEntry(ItemLootEntry.builder(Items.COOKED_CHICKEN).acceptFunction(SetCount.builder(RandomValueRange.of(1, 10))))
                                .addEntry(ItemLootEntry.builder(Items.COOKED_COD).acceptFunction(SetCount.builder(RandomValueRange.of(1, 8)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(Items.GLISTERING_MELON_SLICE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.APPLE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.MAP).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_keeping_1.get()).weight(75))
                                //ultrarare loot
                                .addEntry(ItemLootEntry.builder(Items.GOLDEN_APPLE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.GOLDEN_CARROT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.CAKE).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.OAK_BOAT).weight(25))
                                .addEntry(ItemLootEntry.builder(TFBlocks.hollow_oak_sapling.get().asItem()).weight(25))));

        register.accept(TFTreasure.hedgemaze.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                .addEntry(TableLootEntry.builder(TFTreasure.USELESS_LOOT).weight(25))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Blocks.OAK_PLANKS).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(Blocks.BROWN_MUSHROOM).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(Blocks.RED_MUSHROOM).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.WHEAT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.STRING).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.STICK).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75)))
                          .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(Items.MELON_SLICE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(Items.MELON_SEEDS).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(Items.PUMPKIN_SEEDS).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(Items.ARROW).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))))
                                .addEntry(ItemLootEntry.builder(TFBlocks.firefly.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(Items.COBWEB).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.APPLE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.SHEARS).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.SADDLE).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.BOW).weight(75))
                                //ultrarare loot
                                .addEntry(ItemLootEntry.builder(Items.GOLDEN_APPLE).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.MUSHROOM_STEW).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.DIAMOND_HOE).weight(25))));

        register.accept(TFTreasure.tree_cache.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                .addEntry(TableLootEntry.builder(TFTreasure.USELESS_LOOT).weight(25))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.POISONOUS_POTATO).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.WHEAT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.POTATO).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.CARROT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.MELON_SLICE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.MELON_SEEDS).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.WATER_BUCKET).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.MILK_BUCKET).weight(75)))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(TFBlocks.oak_sapling.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(TFBlocks.canopy_sapling.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(TFBlocks.mangrove_sapling.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(TFBlocks.darkwood_sapling.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(TFBlocks.firefly.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(Items.PUMPKIN_PIE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.APPLE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_keeping_1.get()).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_life_1.get()).weight(75))
                                //ultrarare loot
                                .addEntry(ItemLootEntry.builder(TFBlocks.hollow_oak_sapling.get().asItem()).weight(25))
                                .addEntry(ItemLootEntry.builder(TFBlocks.time_sapling.get().asItem()).weight(25))
                                .addEntry(ItemLootEntry.builder(TFBlocks.transformation_sapling.get().asItem()).weight(25))
                                .addEntry(ItemLootEntry.builder(TFBlocks.mining_sapling.get().asItem()).weight(25))
                                .addEntry(ItemLootEntry.builder(TFBlocks.sorting_sapling.get().asItem()).weight(25))));

        register.accept(TFTreasure.graveyard.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.PUMPKIN_SEEDS).acceptFunction(SetCount.builder(RandomValueRange.of(1, 32))))
                                .addEntry(ItemLootEntry.builder(Items.PUMPKIN_PIE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(TFItems.torchberries.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 16)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(Items.DIAMOND).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(TFItems.moon_dial.get())))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(TFItems.transformation_powder.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))))
                                .addEntry(ItemLootEntry.builder(TFBlocks.uncrafting_table.get().asItem()))
                                .addEntry(ItemLootEntry.builder(Items.GOLDEN_APPLE))));

        register.accept(TFTreasure.hill1.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                .addEntry(TableLootEntry.builder(TFTreasure.USELESS_LOOT).weight(25))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.IRON_INGOT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.WHEAT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.STRING).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.BUCKET).weight(75)))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(Items.TORCH).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))))
                                .addEntry(ItemLootEntry.builder(Items.ARROW).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))))
                                .addEntry(ItemLootEntry.builder(Items.GUNPOWDER).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(Items.BREAD))
                                .addEntry(ItemLootEntry.builder(TFItems.ore_magnet.get())))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(TFItems.liveroot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.IRON_PICKAXE).weight(75))
                                //ultrarare loot
                                .addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(25))
                                .addEntry(ItemLootEntry.builder(TFItems.transformation_powder.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(25))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(25))));

        register.accept(TFTreasure.hill2.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                .addEntry(TableLootEntry.builder(TFTreasure.USELESS_LOOT).weight(25))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.IRON_INGOT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.CARROT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.LADDER).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.BUCKET).weight(75)))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(Items.BAKED_POTATO).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))))
                                .addEntry(ItemLootEntry.builder(Items.ARROW).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))))
                                .addEntry(ItemLootEntry.builder(Items.TORCH).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))))
                                .addEntry(ItemLootEntry.builder(TFItems.ore_magnet.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(TFItems.naga_scale.get()).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.transformation_powder.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFBlocks.uncrafting_table.get().asItem()).weight(75))
                                //ultrarare loot
                                .addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.EMERALD).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(25))
                                .addEntry(ItemLootEntry.builder(TFItems.peacock_fan.get()).weight(25))
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_life_1.get()).weight(25))));

        register.accept(TFTreasure.hill3.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                .addEntry(TableLootEntry.builder(TFTreasure.USELESS_LOOT).weight(25))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.GOLD_NUGGET).acceptFunction(SetCount.builder(RandomValueRange.of(1, 9))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.POTATO).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.COD).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.torchberries.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))).weight(75)))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(Items.TORCH).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))))
                                .addEntry(ItemLootEntry.builder(Items.ARROW).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))))
                                .addEntry(ItemLootEntry.builder(Items.GUNPOWDER).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(Items.PUMPKIN_PIE))
                                .addEntry(ItemLootEntry.builder(TFItems.ore_magnet.get())))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(TFItems.transformation_powder.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_pickaxe.get()).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.EFFICIENCY, 1).apply(Enchantments.FORTUNE, 1)).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.naga_scale.get()).weight(75))
                                //ultrarare loot
                                .addEntry(ItemLootEntry.builder(Items.DIAMOND).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))).weight(20))
                                .addEntry(ItemLootEntry.builder(TFItems.moonworm_queen.get()).weight(20))
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_life_1.get()).weight(20))
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_keeping_1.get()).weight(20))
                                .addEntry(ItemLootEntry.builder(TFBlocks.mangrove_sapling.get().asItem()).weight(25))));

        register.accept(TFTreasure.tower_library.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                .addEntry(TableLootEntry.builder(TFTreasure.USELESS_LOOT).weight(25))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:water")))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.GLASS_BOTTLE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.LADDER).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.PAPER).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.BONE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.GOLD_NUGGET).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.CLAY_BALL).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75)))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(Items.IRON_LEGGINGS).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(5))))
                                .addEntry(ItemLootEntry.builder(Items.FIRE_CHARGE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
                                .addEntry(ItemLootEntry.builder(Items.BOOK).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))))
                                .addEntry(ItemLootEntry.builder(Items.MAP))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:poison")))))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:awkward")))))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:weakness"))))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(Items.STONE_SWORD).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(10))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.WOODEN_SWORD).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(15))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.BOW).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(5))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.SPLASH_POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:weakness")))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:strong_regeneration")))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:strong_healing")))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:strong_swiftness")))).weight(75))
                                //ultrarare loot
                                .addEntry(ItemLootEntry.builder(Items.GOLDEN_PICKAXE).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(10))).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.IRON_SWORD).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(20))).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.BOW).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(30))).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.BOOKSHELF).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.ENDER_PEARL).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.EXPERIENCE_BOTTLE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(25))));

        register.accept(TFTreasure.tower_room.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                .addEntry(TableLootEntry.builder(TFTreasure.USELESS_LOOT).weight(25))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:water")))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.GLASS_BOTTLE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.SUGAR).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.SPIDER_EYE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.GHAST_TEAR).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.MAGMA_CREAM).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.FERMENTED_SPIDER_EYE).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.GLISTERING_MELON_SLICE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.BLAZE_POWDER).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.PAPER).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75)))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(Items.GOLDEN_SWORD).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(10))))
                                .addEntry(ItemLootEntry.builder(Items.GOLDEN_SWORD).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(7))))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:swiftness")))))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:healing")))))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:fire_resistance")))))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:harming"))))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(Items.GOLDEN_HELMET).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(10))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.transformation_powder.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_keeping_1.get()).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_life_1.get()).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.SPLASH_POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:healing")))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.SPLASH_POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:strong_harming")))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:strong_swiftness")))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:strong_regeneration")))).weight(75))
                                //ultrarare loot
                                .addEntry(ItemLootEntry.builder(Items.GOLDEN_AXE).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(20))).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.ENDER_PEARL).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(25))
                                .addEntry(ItemLootEntry.builder(TFItems.moonworm_queen.get()).weight(25))
                                .addEntry(ItemLootEntry.builder(TFItems.peacock_fan.get()).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.OBSIDIAN).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(25))));

        register.accept(TFTreasure.labyrinth_deadend.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                .addEntry(TableLootEntry.builder(TFTreasure.USELESS_LOOT).weight(25))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.STICK).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.COAL).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.ARROW).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.MUSHROOM_STEW).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.maze_wafer.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 9))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.PAPER).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.LEATHER).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75)))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(Items.MILK_BUCKET))
                                .addEntry(ItemLootEntry.builder(Items.PAPER).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))))
                                .addEntry(ItemLootEntry.builder(Items.IRON_INGOT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 8))))
                                .addEntry(ItemLootEntry.builder(TFBlocks.firefly.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))))
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_keeping_1.get())))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(Items.GOLDEN_APPLE))
                                .addEntry(ItemLootEntry.builder(Items.BLAZE_ROD).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 8))))));

        register.accept(TFTreasure.labyrinth_room.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.IRON_INGOT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(Items.GUNPOWDER).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(Items.MILK_BUCKET))
                                .addEntry(ItemLootEntry.builder(TFItems.maze_wafer.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))))
                                .addEntry(ItemLootEntry.builder(TFBlocks.firefly.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_keeping_1.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_helmet.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_chestplate.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_leggings.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_boots.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_pickaxe.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_chestplate.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_sword.get())))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(TFItems.maze_map_focus.get()))
                                .addEntry(ItemLootEntry.builder(Items.TNT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:strong_healing")))))));

        register.accept(TFTreasure.labyrinth_vault.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.IRON_INGOT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 9))))
                                .addEntry(ItemLootEntry.builder(Items.EMERALD).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 9))))
                                .addEntry(ItemLootEntry.builder(TFItems.maze_wafer.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:strong_regeneration")))))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:strong_healing")))))
                                .addEntry(ItemLootEntry.builder(Items.POTION).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> nbt.putString("Potion", "minecraft:strong_swiftness"))))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(Items.BOW).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.INFINITY, 1).apply(Enchantments.PUNCH, 2)))
                                .addEntry(ItemLootEntry.builder(Items.BOW).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.POWER, 3).apply(Enchantments.FLAME, 1)))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_shovel.get()).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.EFFICIENCY, 4).apply(Enchantments.UNBREAKING, 2)))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_axe.get()).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.EFFICIENCY, 5)))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_chestplate.get()).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.PROTECTION, 3)))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_boots.get()).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.PROTECTION, 2)))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_leggings.get()).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.FIRE_PROTECTION, 4)))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_helmet.get()).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.RESPIRATION, 3))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(Items.EMERALD_BLOCK))
                                .addEntry(ItemLootEntry.builder(Items.ENDER_CHEST))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_pickaxe.get()).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.EFFICIENCY, 4).apply(Enchantments.SILK_TOUCH, 1)))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_sword.get()).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.SHARPNESS, 4).apply(Enchantments.KNOCKBACK, 2)))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_sword.get()).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.BANE_OF_ARTHROPODS, 5).apply(Enchantments.FIRE_ASPECT, 2)))
                                .addEntry(ItemLootEntry.builder(TFItems.mazebreaker_pickaxe.get()).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.EFFICIENCY, 4).apply(Enchantments.UNBREAKING, 3).apply(Enchantments.FORTUNE, 2)))));

        register.accept(TFTreasure.stronghold_cache.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.STICK).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))))
                                .addEntry(ItemLootEntry.builder(Items.COAL).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))))
                                .addEntry(ItemLootEntry.builder(Items.ARROW).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))))
                                .addEntry(ItemLootEntry.builder(TFItems.maze_wafer.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 9))))
                                .addEntry(ItemLootEntry.builder(Items.BLUE_WOOL))
                                .addEntry(ItemLootEntry.builder(Items.IRON_INGOT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(Items.BUCKET))
                                .addEntry(ItemLootEntry.builder(Items.IRON_INGOT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(TFBlocks.firefly.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))))
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_keeping_1.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.armor_shard.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(TFItems.knightmetal_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 8))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.BOW).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(20))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.IRON_SWORD).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(20))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_sword.get()).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(15))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_sword.get()).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(10))).weight(75))
                                //ultrarare loot
                                .addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.BANE_OF_ARTHROPODS, 4)).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.SHARPNESS, 4)).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.SMITE, 4)).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.UNBREAKING, 2)).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.PROTECTION, 3)).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.PROJECTILE_PROTECTION, 3)).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.FEATHER_FALLING, 3)).weight(25))));

        register.accept(TFTreasure.stronghold_room.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                .addEntry(TableLootEntry.builder(TFTreasure.USELESS_LOOT).weight(25))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.IRON_INGOT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.GUNPOWDER).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.MILK_BUCKET).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.maze_wafer.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFBlocks.firefly.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))).weight(75)))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_life_1.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_helmet.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_chestplate.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_leggings.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_boots.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_pickaxe.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_chestplate.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_sword.get())))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_sword.get()).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(25))))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_sword.get()).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(20))))
                                .addEntry(ItemLootEntry.builder(Items.IRON_SWORD).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(30))))
                                .addEntry(ItemLootEntry.builder(Items.BOW).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(30))))
                                .addEntry(ItemLootEntry.builder(Items.DIAMOND_SWORD).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(15))))
                                .addEntry(ItemLootEntry.builder(TFItems.maze_map_focus.get()))));

        register.accept(TFTreasure.stronghold_boss.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                //common loot
                                .addEntry(ItemLootEntry.builder(TFItems.knightmetal_sword.get()).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(20))))
                                .addEntry(ItemLootEntry.builder(TFItems.knightmetal_pickaxe.get()).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(20))))
                                .addEntry(ItemLootEntry.builder(TFItems.knightmetal_axe.get()).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(20)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(TFItems.phantom_helmet.get()).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(20))))
                                .addEntry(ItemLootEntry.builder(TFItems.phantom_chestplate.get()).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(20)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(TFItems.phantom_helmet.get()).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(30))))
                                .addEntry(ItemLootEntry.builder(TFItems.phantom_chestplate.get()).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(30)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(TFBlocks.knight_phantom_trophy.get().asItem())))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(TFItems.carminite.get())
                                        .acceptCondition(LootConditionModExists.builder("immersiveengineering"))
                                        .acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")), TFItems.knightmetal_ingot.get()))
                                        .acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> {
                                            nbt.putString("shader_name", "twilightforest:knight_phantom");
                                        })))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(TFItems.carminite.get())
                                        .acceptCondition(LootConditionModExists.builder("immersiveengineering"))
                                        .acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")), TFItems.knightmetal_ingot.get())))));

        register.accept(TFTreasure.darktower_cache.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                .addEntry(TableLootEntry.builder(TFTreasure.USELESS_LOOT).weight(25))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.STICK).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.CHARCOAL).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.ARROW).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.experiment_115.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 9))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.RED_WOOL).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.REDSTONE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75)))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(Items.REDSTONE_LAMP).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(Items.IRON_INGOT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 8))))
                                .addEntry(ItemLootEntry.builder(TFBlocks.firefly.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))))
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_keeping_1.get())))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 8))))
                                .addEntry(ItemLootEntry.builder(Items.DIAMOND).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))))));

        register.accept(TFTreasure.darktower_key.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                .addEntry(TableLootEntry.builder(TFTreasure.USELESS_LOOT).weight(25))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.IRON_INGOT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.GUNPOWDER).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFBlocks.firefly.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.experiment_115.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.GLOWSTONE_DUST).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.REDSTONE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(75)))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_keeping_1.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_helmet.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_chestplate.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_leggings.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_boots.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.steeleaf_pickaxe.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_chestplate.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_sword.get())))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_life_1.get()))
                                .addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.FEATHER_FALLING, 3)))
                                .addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.KNOCKBACK, 2)))
                                .addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.EFFICIENCY, 3))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(TFItems.tower_key.get()))));

        register.accept(TFTreasure.darktower_boss.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                .addEntry(ItemLootEntry.builder(TFItems.carminite.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                .addEntry(ItemLootEntry.builder(TFItems.fiery_tears.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(TFBlocks.ur_ghast_trophy.get().asItem())))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(TFItems.carminite.get())
                                        .acceptCondition(LootConditionModExists.builder("immersiveengineering"))
                                        .acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")), TFItems.carminite.get()))
                                        .acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> {
                                            nbt.putString("shader_name", "twilightforest:ur-ghast");
                                        })))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(TFItems.carminite.get())
                                        .acceptCondition(LootConditionModExists.builder("immersiveengineering"))
                                        .acceptFunction(LootFunctionModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")), TFItems.carminite.get())))));

        register.accept(TFTreasure.aurora_cache.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                .addEntry(TableLootEntry.builder(TFTreasure.USELESS_LOOT).weight(25))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.STICK).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.COAL).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.ARROW).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.ICE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(Items.PACKED_ICE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.maze_wafer.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 9))).weight(75)))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_keeping_1.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.ironwood_ingot.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(TFBlocks.firefly.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))))
                                .addEntry(ItemLootEntry.builder(TFBlocks.aurora_block.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))))
                                .addEntry(ItemLootEntry.builder(TFItems.arctic_fur.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(TFItems.arctic_fur.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 8))).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.ice_bow.get()).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.ender_bow.get()).weight(75))
                                .addEntry(ItemLootEntry.builder(TFItems.ice_sword.get()).weight(75))
                                //ultrarare loot
                                .addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.SHARPNESS, 4)).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.POWER, 4)).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.PUNCH, 2)).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.UNBREAKING, 2)).weight(25))
                                .addEntry(ItemLootEntry.builder(Items.ENCHANTED_BOOK).acceptFunction(LootFunctionEnchant.builder().apply(Enchantments.INFINITY, 1)).weight(25))));

        register.accept(TFTreasure.aurora_room.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                //common loot
                                .addEntry(ItemLootEntry.builder(TFItems.maze_wafer.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12))))
                                .addEntry(ItemLootEntry.builder(TFItems.ice_bomb.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(TFBlocks.firefly.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))))
                                .addEntry(ItemLootEntry.builder(Items.ICE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(Items.PACKED_ICE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(TFItems.arctic_fur.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(TFItems.arctic_helmet.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.arctic_chestplate.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.arctic_leggings.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.arctic_boots.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.knightmetal_chestplate.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.knightmetal_sword.get()))
                                .addEntry(ItemLootEntry.builder(TFItems.charm_of_life_1.get())))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(TFItems.ice_bow.get()).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(30))))
                                .addEntry(ItemLootEntry.builder(TFItems.ender_bow.get()).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(5))))
                                .addEntry(ItemLootEntry.builder(TFItems.ice_sword.get()).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(25))))
                                .addEntry(ItemLootEntry.builder(TFItems.glass_sword.get()).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(20))))));

        register.accept(TFTreasure.troll_garden.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.RED_MUSHROOM).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(Items.BROWN_MUSHROOM).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
                                .addEntry(ItemLootEntry.builder(Items.WHEAT_SEEDS).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(Items.CARROT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(Items.POTATO).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(Items.MELON_SEEDS).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(Items.BONE_MEAL).acceptFunction(SetCount.builder(RandomValueRange.of(1, 12)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(TFBlocks.uberous_soil.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(TFItems.magic_beans.get()))));

        register.accept(TFTreasure.troll_vault.lootTable,
                LootTable.builder()
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(4))
                                //common loot
                                .addEntry(ItemLootEntry.builder(Items.COAL).acceptFunction(SetCount.builder(RandomValueRange.of(1, 32))))
                                .addEntry(ItemLootEntry.builder(TFItems.torchberries.get()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 16))))
                                .addEntry(ItemLootEntry.builder(Items.EMERALD).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(2))
                                //uncommon loot
                                .addEntry(ItemLootEntry.builder(TFBlocks.trollsteinn.get().asItem()).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))))
                                .addEntry(ItemLootEntry.builder(Items.OBSIDIAN).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6)))))
                        .addLootPool(LootPool.builder()
                                .rolls(ConstantRange.of(1))
                                //rare loot
                                .addEntry(ItemLootEntry.builder(TFItems.lamp_of_cinders.get()))));
    }
}
