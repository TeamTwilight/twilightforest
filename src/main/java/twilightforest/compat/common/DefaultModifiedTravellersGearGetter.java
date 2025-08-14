package twilightforest.compat.common;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFItems;
import twilightforest.init.custom.TravellersModifiersManager;

import java.util.List;

public class DefaultModifiedTravellersGearGetter {
	public static List<ItemStack> getDefaultModifiedTravellersGear(HolderLookup.Provider registries) {
		return List.of(
			getDefaultGoggles(registries),
			getDefaultVestGloves(registries),
			getDefaultVest(registries),
			getDefaultWingsBelt(registries),
			getDefaultWings(registries),
			getDefaultBoots(registries)
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

	private static ItemStack getDefaultGoggles(HolderLookup.Provider registries) {
		ItemStack goggles = new ItemStack(TFItems.TRAVELLERS_GOGGLES.get());
		TravellersModifiersManager.addModifier(registries, goggles, TravellersModifiersManager.AUTO_REPAIR_MODIFIER);
		TravellersModifiersManager.addModifier(registries, goggles, TravellersModifiersManager.RED_THREAD_VISION_MODIFIER);
		TravellersModifiersManager.addModifier(registries, goggles, TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER);
		return goggles;
	}

	private static ItemStack getDefaultVestGloves(HolderLookup.Provider registries) {
		ItemStack vestGloves = new ItemStack(TFItems.TRAVELLERS_CHESTPLATE_GLOVES.get());
		TravellersModifiersManager.addModifier(registries, vestGloves, TravellersModifiersManager.AUTO_REPAIR_MODIFIER);
		TravellersModifiersManager.addModifier(registries, vestGloves, TravellersModifiersManager.HASTE_MODIFIER);
		TravellersModifiersManager.addModifier(registries, vestGloves, TravellersModifiersManager.ARROW_MAGNETISM_MODIFIER);
		return vestGloves;
	}

	private static ItemStack getDefaultVest(HolderLookup.Provider registries) {
		ItemStack vest = new ItemStack(TFItems.TRAVELLERS_CHESTPLATE.get());
		TravellersModifiersManager.addModifier(registries, vest, TravellersModifiersManager.AUTO_REPAIR_MODIFIER);
		TravellersModifiersManager.addModifier(registries, vest, TravellersModifiersManager.PERFECT_DODGE_MODIFIER);
		TravellersModifiersManager.addModifier(registries, vest, TravellersModifiersManager.STEALTH_MODIFIER);
		return vest;
	}

	private static ItemStack getDefaultWingsBelt(HolderLookup.Provider registries) {
		ItemStack wingsBelt = new ItemStack(TFItems.TRAVELLERS_WINGS_BELT.get());
		TravellersModifiersManager.addModifier(registries, wingsBelt, TravellersModifiersManager.SWAP_HOTBAR_MODIFIER);
		TravellersModifiersManager.addModifier(registries, wingsBelt, TravellersModifiersManager.CONTROLLED_FALL_MODIFIER);
		TravellersModifiersManager.addModifier(registries, wingsBelt, TravellersModifiersManager.DOUBLE_JUMP_MODIFIER);
		return wingsBelt;
	}

	private static ItemStack getDefaultWings(HolderLookup.Provider registries) {
		ItemStack wings = new ItemStack(TFItems.TRAVELLERS_WINGS.get());
		TravellersModifiersManager.addModifier(registries, wings, TravellersModifiersManager.AUTO_REPAIR_MODIFIER);
		TravellersModifiersManager.addModifier(registries, wings, TravellersModifiersManager.AGILE_RANGER_MODIFIER);
		TravellersModifiersManager.addModifier(registries, wings, TravellersModifiersManager.SIDESTEP_MODIFIER);
		return wings;
	}

	private static ItemStack getDefaultBoots(HolderLookup.Provider registries) {
		ItemStack boots = new ItemStack(TFItems.TRAVELLERS_BOOTS.get());
		TravellersModifiersManager.addModifier(registries, boots, TravellersModifiersManager.AUTO_REPAIR_MODIFIER);
		TravellersModifiersManager.addModifier(registries, boots, TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER);
		TravellersModifiersManager.addModifier(registries, boots, TravellersModifiersManager.WATER_WALK_MODIFIER);
		return boots;
	}
}
