package twilightforest.enums;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.item.TFItems;

import java.util.function.Supplier;

public enum TwilightArmorMaterial implements IArmorMaterial {
	ARMOR_NAGA("naga_scale", 21, new int[]{3, 6, 7, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.5F, () -> Ingredient.fromItems(TFItems.naga_scale.get())),
	ARMOR_IRONWOOD("ironwood", 20, new int[]{2, 5, 7, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0F, () -> Ingredient.fromItems(TFItems.ironwood_ingot.get())),
	ARMOR_FIERY("fiery", 25, new int[]{4, 7, 9, 4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.5F, () -> Ingredient.fromItems(TFItems.fiery_ingot.get())),
	ARMOR_STEELEAF("steeleaf", 10, new int[]{3, 6, 8, 3}, 9, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0F, () -> Ingredient.fromItems(TFItems.steeleaf_ingot.get())),
	ARMOR_KNIGHTLY("knightly", 20, new int[]{3, 6, 8, 3}, 8, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, () -> Ingredient.fromItems(TFItems.knightmetal_ingot.get())),
	ARMOR_PHANTOM("phantom", 30, new int[]{3, 6, 8, 3}, 8, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 2.5F, () -> Ingredient.fromItems(TFItems.knightmetal_ingot.get())),
	ARMOR_YETI("yetiarmor", 20, new int[]{3, 6, 7, 4}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 3F, () -> Ingredient.fromItems(TFItems.alpha_fur.get())),
	ARMOR_ARCTIC("arcticarmor", 10, new int[]{2, 5, 7, 2}, 8, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 2F, () -> Ingredient.fromItems(TFItems.arctic_fur.get()));

	private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
	private final String name;
	private final int durability;
	private final int[] damageReduction;
	private final int enchantability;
	private final SoundEvent equipSound;
	private final float toughness;
	private final Supplier<Ingredient> repairMaterial;

	TwilightArmorMaterial(String name, int durability, int damageReduction[], int enchantability, SoundEvent sound, float toughness, Supplier<Ingredient> repairMaterial) {
		this.name = name;
		this.durability = durability;
		this.damageReduction = damageReduction;
		this.enchantability = enchantability;
		this.equipSound = sound;
		this.toughness = toughness;
		this.repairMaterial = repairMaterial;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getDurability(EquipmentSlotType slotIn) {
		return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * durability;
	}

	@Override
	public int getDamageReductionAmount(EquipmentSlotType slotIn) {
		return damageReduction[slotIn.getIndex()];
	}

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public SoundEvent getSoundEvent() {
		return equipSound;
	}

	@Override
	public float getToughness() {
		return toughness;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return repairMaterial.get();
	}

	@Override
	public float getKnockbackResistance() {
		return 0.0F; //Discuss use in other sets
	}
}
