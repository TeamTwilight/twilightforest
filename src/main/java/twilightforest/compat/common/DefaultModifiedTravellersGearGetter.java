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
			getDefaultVest(registries),
			getDefaultWings(registries),
			getDefaultBoots(registries)
		);
	}
	public static ItemStack getDemodifiedStack(ItemStack modifiedStack) {
		return new ItemStack(modifiedStack.getItem(), modifiedStack.getCount());
	}

	private static ItemStack getDefaultGoggles(HolderLookup.Provider registries) {
		ItemStack goggles = new ItemStack(TFItems.TRAVELLERS_GOGGLES.get());
		TravellersModifiersManager.addModifier(registries, goggles, TravellersModifiersManager.AUTO_REPAIR_MODIFIER);
		TravellersModifiersManager.addModifier(registries, goggles, TravellersModifiersManager.RED_THREAD_VISION_MODIFIER);
		TravellersModifiersManager.addModifier(registries, goggles, TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER);
		return goggles;
	}

	private static ItemStack getDefaultVest(HolderLookup.Provider registries) {
		ItemStack vest = new ItemStack(TFItems.TRAVELLERS_VEST.get());
		TravellersModifiersManager.addModifier(registries, vest, TravellersModifiersManager.AUTO_REPAIR_MODIFIER);
		TravellersModifiersManager.addModifier(registries, vest, TravellersModifiersManager.PERFECT_DODGE_MODIFIER);
		TravellersModifiersManager.addModifier(registries, vest, TravellersModifiersManager.STEALTH_MODIFIER);
		return vest;
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
