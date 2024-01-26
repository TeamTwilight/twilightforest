package twilightforest.enums;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum TwilightArmorMaterial implements ArmorMaterial {
	ARMOR_NAGA("naga_scale", 21, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 3);
		map.put(ArmorItem.Type.LEGGINGS, 6);
		map.put(ArmorItem.Type.CHESTPLATE, 7);
		map.put(ArmorItem.Type.HELMET, 2);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 0.5F, () -> Ingredient.of(TFItems.NAGA_SCALE.get())),
	ARMOR_IRONWOOD("ironwood", 20, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 2);
		map.put(ArmorItem.Type.LEGGINGS, 5);
		map.put(ArmorItem.Type.CHESTPLATE, 7);
		map.put(ArmorItem.Type.HELMET, 2);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 0F, () -> Ingredient.of(TFItems.IRONWOOD_INGOT.get())),
	ARMOR_FIERY("fiery", 25, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 4);
		map.put(ArmorItem.Type.LEGGINGS, 7);
		map.put(ArmorItem.Type.CHESTPLATE, 9);
		map.put(ArmorItem.Type.HELMET, 4);
	}), 10, SoundEvents.ARMOR_EQUIP_GENERIC, 1.5F, () -> Ingredient.of(TFItems.FIERY_INGOT.get())),
	ARMOR_STEELEAF("steeleaf", 10, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 3);
		map.put(ArmorItem.Type.LEGGINGS, 6);
		map.put(ArmorItem.Type.CHESTPLATE, 8);
		map.put(ArmorItem.Type.HELMET, 3);
	}), 9, SoundEvents.ARMOR_EQUIP_GENERIC, 0F, () -> Ingredient.of(TFItems.STEELEAF_INGOT.get())),
	ARMOR_KNIGHTLY("knightly", 20, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 3);
		map.put(ArmorItem.Type.LEGGINGS, 6);
		map.put(ArmorItem.Type.CHESTPLATE, 8);
		map.put(ArmorItem.Type.HELMET, 3);
	}), 8, TFSounds.KNIGHTMETAL_EQUIP.get(), 1.0F, () -> Ingredient.of(TFItems.KNIGHTMETAL_INGOT.get())),
	ARMOR_PHANTOM("phantom", 30, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 3);
		map.put(ArmorItem.Type.LEGGINGS, 6);
		map.put(ArmorItem.Type.CHESTPLATE, 8);
		map.put(ArmorItem.Type.HELMET, 3);
	}), 8, SoundEvents.ARMOR_EQUIP_GENERIC, 2.5F, () -> Ingredient.of(TFItems.KNIGHTMETAL_INGOT.get())),
	ARMOR_YETI("yetiarmor", 20, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 3);
		map.put(ArmorItem.Type.LEGGINGS, 6);
		map.put(ArmorItem.Type.CHESTPLATE, 7);
		map.put(ArmorItem.Type.HELMET, 4);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 3F, () -> Ingredient.of(TFItems.ALPHA_YETI_FUR.get())),
	ARMOR_ARCTIC("arcticarmor", 10, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 2);
		map.put(ArmorItem.Type.LEGGINGS, 5);
		map.put(ArmorItem.Type.CHESTPLATE, 7);
		map.put(ArmorItem.Type.HELMET, 2);
	}), 8, SoundEvents.ARMOR_EQUIP_GENERIC, 2F, () -> Ingredient.of(TFItems.ARCTIC_FUR.get()));

	private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 13);
		map.put(ArmorItem.Type.LEGGINGS, 15);
		map.put(ArmorItem.Type.CHESTPLATE, 16);
		map.put(ArmorItem.Type.HELMET, 11);
	});
	private final String name;
	private final int durability;
	private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
	private final int enchantability;
	private final SoundEvent equipSound;
	private final float toughness;
	private final Supplier<Ingredient> repairMaterial;

	TwilightArmorMaterial(String name, int durability, EnumMap<ArmorItem.Type, Integer> protectionFunctionForType, int enchantability, SoundEvent sound, float toughness, Supplier<Ingredient> repairMaterial) {
		this.name = name;
		this.durability = durability;
		this.protectionFunctionForType = protectionFunctionForType;
		this.enchantability = enchantability;
		this.equipSound = sound;
		this.toughness = toughness;
		this.repairMaterial = repairMaterial;
	}

	@Override
	public String getName() {
		return name;
	}

	public int getDurabilityForType(ArmorItem.Type type) {
		return HEALTH_FUNCTION_FOR_TYPE.get(type) * this.durability;
	}

	public int getDefenseForType(ArmorItem.Type type) {
		return this.protectionFunctionForType.get(type);
	}

	@Override
	public int getEnchantmentValue() {
		return enchantability;
	}

	@Override
	public SoundEvent getEquipSound() {
		return equipSound;
	}

	@Override
	public float getToughness() {
		return toughness;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairMaterial.get();
	}

	@Override
	public float getKnockbackResistance() {
		return 0.0F; //Discuss use in other sets
	}
}
