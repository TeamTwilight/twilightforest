package twilightforest.compat.common;

import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFItems;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiers;

import java.util.List;

public class DefaultModifiedTravellersGearGetter {
	public static List<ItemStack> getDefaultModifiedTravellersGear() {
		return List.of(
			getDefaultGoggles(),
			getDefaultVestGloves(),
			getDefaultVest(),
			getDefaultWingsBelt(),
			getDefaultWings(),
			getDefaultBoots()
		);
	}
	public static ItemStack getDemodifiedStack(ItemStack modifiedStack) {
		ItemStack demodifiedStack;
		if (modifiedStack.is(TFItems.TRAVELLERS_WINGS_BELT))
			demodifiedStack = new ItemStack(TFItems.TRAVELLERS_WINGS.get(), 1);
		else
			demodifiedStack = modifiedStack.copy();
		demodifiedStack = new ItemStack(demodifiedStack.getItem(), demodifiedStack.getCount());

		return demodifiedStack;
	}

	private static ItemStack getDefaultGoggles() {
		ItemStack goggles = new ItemStack(TFItems.TRAVELLERS_GOGGLES.get());
		TravellersModifiers.AUTO_REPAIR_MODIFIER.addModifier(goggles);
		TravellersModifiers.RED_THREAD_VISION_MODIFIER	.addModifier(goggles);
		TravellersModifiers.ALL_NIGHT_GOGGLES_MODIFIER.addModifier(goggles);
		return goggles;
	}

	private static ItemStack getDefaultVestGloves() {
		ItemStack vestGloves = new ItemStack(TFItems.TRAVELLERS_CHESTPLATE_GLOVES.get());
		TravellersModifiers.AUTO_REPAIR_MODIFIER.addModifier(vestGloves);
		TravellersModifiers.HASTE_MODIFIER.addModifier(vestGloves);
		TravellersModifiers.ARROW_MAGNETISM_MODIFIER.addModifier(vestGloves);
		return vestGloves;
	}

	private static ItemStack getDefaultVest() {
		ItemStack vest = new ItemStack(TFItems.TRAVELLERS_CHESTPLATE.get());
		TravellersModifiers.AUTO_REPAIR_MODIFIER.addModifier(vest);
		TravellersModifiers.PERFECT_DODGE_MODIFIER.addModifier(vest);
		TravellersModifiers.STEALTH_MODIFIER.addModifier(vest);
		return vest;
	}

	private static ItemStack getDefaultWingsBelt() {
		ItemStack wingsBelt = new ItemStack(TFItems.TRAVELLERS_WINGS_BELT.get());
		TravellersModifiers.SWAP_HOTBAR_MODIFIER.addModifier(wingsBelt);
		TravellersModifiers.CONTROLLED_FALL_MODIFIER.addModifier(wingsBelt);
		TravellersModifiers.DOUBLE_JUMP_MODIFIER.addModifier(wingsBelt);

		return wingsBelt;
	}

	private static ItemStack getDefaultWings() {
		ItemStack wings = new ItemStack(TFItems.TRAVELLERS_WINGS.get());
		TravellersModifiers.AUTO_REPAIR_MODIFIER.addModifier(wings);
		TravellersModifiers.AGILE_RANGER_MODIFIER.addModifier(wings);
		TravellersModifiers.SIDESTEP_MODIFIER.addModifier(wings);
		return wings;
	}

	private static ItemStack getDefaultBoots() {
		ItemStack boots = new ItemStack(TFItems.TRAVELLERS_BOOTS.get());
		TravellersModifiers.AUTO_REPAIR_MODIFIER.addModifier(boots);
		TravellersModifiers.STRAIGHT_AHEAD_MODIFIER.addModifier(boots);
		TravellersModifiers.WATER_WALK_MODIFIER.addModifier(boots);
		return boots;
	}
}
