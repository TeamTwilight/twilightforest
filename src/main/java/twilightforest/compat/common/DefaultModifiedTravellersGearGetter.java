package twilightforest.compat.common;

import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFItems;
import twilightforest.init.custom.TravellersModifiersManager;

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
		TravellersModifiersManager.addModifier(goggles, TravellersModifiersManager.AUTO_REPAIR_MODIFIER);
		TravellersModifiersManager.addModifier(goggles, TravellersModifiersManager.RED_THREAD_VISION_MODIFIER);
		TravellersModifiersManager.addModifier(goggles, TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER);
		return goggles;
	}

	private static ItemStack getDefaultVestGloves() {
		ItemStack vestGloves = new ItemStack(TFItems.TRAVELLERS_CHESTPLATE_GLOVES.get());
		TravellersModifiersManager.addModifier(vestGloves, TravellersModifiersManager.AUTO_REPAIR_MODIFIER);
		TravellersModifiersManager.addModifier(vestGloves, TravellersModifiersManager.HASTE_MODIFIER);
		TravellersModifiersManager.addModifier(vestGloves, TravellersModifiersManager.ARROW_MAGNETISM_MODIFIER);
		return vestGloves;
	}

	private static ItemStack getDefaultVest() {
		ItemStack vest = new ItemStack(TFItems.TRAVELLERS_CHESTPLATE.get());
		TravellersModifiersManager.addModifier(vest, TravellersModifiersManager.AUTO_REPAIR_MODIFIER);
		TravellersModifiersManager.addModifier(vest, TravellersModifiersManager.PERFECT_DODGE_MODIFIER);
		TravellersModifiersManager.addModifier(vest, TravellersModifiersManager.STEALTH_MODIFIER);
		return vest;
	}

	private static ItemStack getDefaultWingsBelt() {
		ItemStack wingsBelt = new ItemStack(TFItems.TRAVELLERS_WINGS_BELT.get());
		TravellersModifiersManager.addModifier(wingsBelt, TravellersModifiersManager.SWAP_HOTBAR_MODIFIER);
		TravellersModifiersManager.addModifier(wingsBelt, TravellersModifiersManager.CONTROLLED_FALL_MODIFIER);
		TravellersModifiersManager.addModifier(wingsBelt, TravellersModifiersManager.DOUBLE_JUMP_MODIFIER);
		return wingsBelt;
	}

	private static ItemStack getDefaultWings() {
		ItemStack wings = new ItemStack(TFItems.TRAVELLERS_WINGS.get());
		TravellersModifiersManager.addModifier(wings, TravellersModifiersManager.AUTO_REPAIR_MODIFIER);
		TravellersModifiersManager.addModifier(wings, TravellersModifiersManager.AGILE_RANGER_MODIFIER);
		TravellersModifiersManager.addModifier(wings, TravellersModifiersManager.SIDESTEP_MODIFIER);
		return wings;
	}

	private static ItemStack getDefaultBoots() {
		ItemStack boots = new ItemStack(TFItems.TRAVELLERS_BOOTS.get());
		TravellersModifiersManager.addModifier(boots, TravellersModifiersManager.AUTO_REPAIR_MODIFIER);
		TravellersModifiersManager.addModifier(boots, TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER);
		TravellersModifiersManager.addModifier(boots, TravellersModifiersManager.WATER_WALK_MODIFIER);
		return boots;
	}
}
