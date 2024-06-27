package twilightforest.init;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;

import java.util.EnumMap;
import java.util.List;

public class TFArmorMaterials {

	public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, TwilightForestMod.ID);

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> NAGA = ARMOR_MATERIALS.register("naga_scale", () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 3);
		map.put(ArmorItem.Type.LEGGINGS, 6);
		map.put(ArmorItem.Type.CHESTPLATE, 7);
		map.put(ArmorItem.Type.HELMET, 2);
		map.put(ArmorItem.Type.BODY, 8);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.of(TFItems.NAGA_SCALE.get()), List.of(new ArmorMaterial.Layer(TwilightForestMod.prefix("naga_scale"))), 0.5F, 0.0F));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> IRONWOOD = ARMOR_MATERIALS.register("ironwood", () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 2);
		map.put(ArmorItem.Type.LEGGINGS, 5);
		map.put(ArmorItem.Type.CHESTPLATE, 7);
		map.put(ArmorItem.Type.HELMET, 2);
		map.put(ArmorItem.Type.BODY, 5);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.of(TFItems.IRONWOOD_INGOT.get()), List.of(new ArmorMaterial.Layer(TwilightForestMod.prefix("ironwood"))), 0.0F, 0.0F));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> FIERY = ARMOR_MATERIALS.register("fiery", () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 4);
		map.put(ArmorItem.Type.LEGGINGS, 7);
		map.put(ArmorItem.Type.CHESTPLATE, 9);
		map.put(ArmorItem.Type.HELMET, 4);
		map.put(ArmorItem.Type.BODY, 13);
	}), 10, SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.of(TFItems.FIERY_INGOT.get()), List.of(new ArmorMaterial.Layer(TwilightForestMod.prefix("fiery"))), 1.5F, 0.0F));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> STEELEAF = ARMOR_MATERIALS.register("steeleaf", () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 3);
		map.put(ArmorItem.Type.LEGGINGS, 6);
		map.put(ArmorItem.Type.CHESTPLATE, 8);
		map.put(ArmorItem.Type.HELMET, 3);
		map.put(ArmorItem.Type.BODY, 11);
	}), 9, SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.of(TFItems.STEELEAF_INGOT.get()), List.of(new ArmorMaterial.Layer(TwilightForestMod.prefix("steeleaf"))), 0.0F, 0.0F));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> KNIGHTMETAL = ARMOR_MATERIALS.register("knightmetal", () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 3);
		map.put(ArmorItem.Type.LEGGINGS, 6);
		map.put(ArmorItem.Type.CHESTPLATE, 8);
		map.put(ArmorItem.Type.HELMET, 3);
		map.put(ArmorItem.Type.BODY, 11);
	}), 8, TFSounds.KNIGHTMETAL_EQUIP, () -> Ingredient.of(TFItems.KNIGHTMETAL_INGOT.get()), List.of(new ArmorMaterial.Layer(TwilightForestMod.prefix("knightmetal"))), 1.0F, 0.0F));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PHANTOM = ARMOR_MATERIALS.register("phantom", () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 3);
		map.put(ArmorItem.Type.LEGGINGS, 6);
		map.put(ArmorItem.Type.CHESTPLATE, 8);
		map.put(ArmorItem.Type.HELMET, 3);
		map.put(ArmorItem.Type.BODY, 10);
	}), 8, SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.of(TFItems.KNIGHTMETAL_INGOT.get()), List.of(new ArmorMaterial.Layer(TwilightForestMod.prefix("phantom"))), 2.5F, 0.0F));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> YETI = ARMOR_MATERIALS.register("yeti", () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 3);
		map.put(ArmorItem.Type.LEGGINGS, 6);
		map.put(ArmorItem.Type.CHESTPLATE, 7);
		map.put(ArmorItem.Type.HELMET, 4);
		map.put(ArmorItem.Type.BODY, 11);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.of(TFItems.ALPHA_YETI_FUR.get()), List.of(new ArmorMaterial.Layer(TwilightForestMod.prefix("yeti"))), 3.0F, 0.0F));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ARCTIC = ARMOR_MATERIALS.register("arctic", () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 2);
		map.put(ArmorItem.Type.LEGGINGS, 5);
		map.put(ArmorItem.Type.CHESTPLATE, 7);
		map.put(ArmorItem.Type.HELMET, 2);
		map.put(ArmorItem.Type.BODY, 7);
	}), 8, SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.of(TFItems.ARCTIC_FUR.get()), List.of(new ArmorMaterial.Layer(TwilightForestMod.prefix("arctic"), "_dyed", true), new ArmorMaterial.Layer(TwilightForestMod.prefix("arctic"), "_overlay", false)), 2.0F, 0.0F));
}
