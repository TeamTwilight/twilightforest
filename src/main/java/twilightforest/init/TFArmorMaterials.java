package twilightforest.init;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import twilightforest.tags.TFItemTags;

import java.util.EnumMap;

public class TFArmorMaterials {

	public static final ArmorMaterial NAGA = new ArmorMaterial(21, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 3);
		map.put(ArmorType.LEGGINGS, 6);
		map.put(ArmorType.CHESTPLATE, 7);
		map.put(ArmorType.HELMET, 2);
		map.put(ArmorType.BODY, 8);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 0.5F, 0.0F, TFItemTags.REPAIRS_NAGA_ARMOR, TFEquipmentAssets.NAGA);

	public static final ArmorMaterial IRONWOOD = new ArmorMaterial(20, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 2);
		map.put(ArmorType.LEGGINGS, 5);
		map.put(ArmorType.CHESTPLATE, 7);
		map.put(ArmorType.HELMET, 2);
		map.put(ArmorType.BODY, 5);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, TFItemTags.REPAIRS_IRONWOOD_ARMOR, TFEquipmentAssets.IRONWOOD);

	public static final ArmorMaterial FIERY = new ArmorMaterial(25, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 4);
		map.put(ArmorType.LEGGINGS, 7);
		map.put(ArmorType.CHESTPLATE, 9);
		map.put(ArmorType.HELMET, 4);
		map.put(ArmorType.BODY, 13);
	}), 10, SoundEvents.ARMOR_EQUIP_GENERIC, 1.5F, 0.0F, TFItemTags.REPAIRS_FIERY_ARMOR, TFEquipmentAssets.FIERY);

	public static final ArmorMaterial STEELEAF = new ArmorMaterial(10, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 3);
		map.put(ArmorType.LEGGINGS, 6);
		map.put(ArmorType.CHESTPLATE, 8);
		map.put(ArmorType.HELMET, 3);
		map.put(ArmorType.BODY, 11);
	}), 9, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, TFItemTags.REPAIRS_STEELEAF_ARMOR, TFEquipmentAssets.STEELEAF);

	public static final ArmorMaterial KNIGHTMETAL = new ArmorMaterial(20, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 3);
		map.put(ArmorType.LEGGINGS, 6);
		map.put(ArmorType.CHESTPLATE, 8);
		map.put(ArmorType.HELMET, 3);
		map.put(ArmorType.BODY, 11);
	}), 8, TFSounds.KNIGHTMETAL_EQUIP, 1.0F, 0.0F, TFItemTags.REPAIRS_KNIGHTMETAL_ARMOR, TFEquipmentAssets.KNIGHTMETAL);

	public static final ArmorMaterial PHANTOM = new ArmorMaterial(30, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 3);
		map.put(ArmorType.LEGGINGS, 6);
		map.put(ArmorType.CHESTPLATE, 8);
		map.put(ArmorType.HELMET, 3);
		map.put(ArmorType.BODY, 10);
	}), 8, SoundEvents.ARMOR_EQUIP_GENERIC, 2.5F, 0.0F, TFItemTags.REPAIRS_PHANTOM_ARMOR, TFEquipmentAssets.PHANTOM);

	public static final ArmorMaterial YETI = new ArmorMaterial(20, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 3);
		map.put(ArmorType.LEGGINGS, 6);
		map.put(ArmorType.CHESTPLATE, 7);
		map.put(ArmorType.HELMET, 4);
		map.put(ArmorType.BODY, 11);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 3.0F, 0.0F, TFItemTags.REPAIRS_YETI_ARMOR, TFEquipmentAssets.YETI);

	public static final ArmorMaterial ARCTIC = new ArmorMaterial(10, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 2);
		map.put(ArmorType.LEGGINGS, 5);
		map.put(ArmorType.CHESTPLATE, 7);
		map.put(ArmorType.HELMET, 2);
		map.put(ArmorType.BODY, 7);
	}), 8, SoundEvents.ARMOR_EQUIP_GENERIC, 2.0F, 0.0F, TFItemTags.REPAIRS_ARCTIC_ARMOR, TFEquipmentAssets.ARCTIC);

	public static final ArmorMaterial TRAVELLERS_GEAR = new ArmorMaterial(12, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 2);
		map.put(ArmorType.LEGGINGS, 3);
		map.put(ArmorType.CHESTPLATE, 4);
		map.put(ArmorType.HELMET, 2);
		map.put(ArmorType.BODY, 4);
	}), 1, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, TFItemTags.REPAIRS_TRAVELLERS_GEAR, TFEquipmentAssets.TRAVELLERS);
}
