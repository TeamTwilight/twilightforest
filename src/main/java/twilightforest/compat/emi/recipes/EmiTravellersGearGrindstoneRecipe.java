package twilightforest.compat.emi.recipes;

import com.google.common.collect.Lists;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiGrindstoneRecipe;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFItems;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiers;

import java.util.List;
import java.util.Random;

public class EmiTravellersGearGrindstoneRecipe extends EmiGrindstoneRecipe {
	protected ItemStack modifiedStack;
	public EmiTravellersGearGrindstoneRecipe(ItemStack modifiedStack) {
		super(modifiedStack.getItem(), TwilightForestMod.prefix("/" + modifiedStack.getItem().getDescriptionId()));
		 this.modifiedStack = modifiedStack;
	}

	@Override
	protected EmiStack getItem(Random random, int item) {
		List<ItemStack> items = Lists.newArrayList();
		items.add(modifiedStack);
		items.add(ItemStack.EMPTY);
		ItemStack demodifiedStack;
		if (modifiedStack.is(TFItems.TRAVELLERS_WINGS_BELT))
			demodifiedStack = new ItemStack(TFItems.TRAVELLERS_WINGS.get(), 1);
		else
			demodifiedStack = modifiedStack.copy();
		items.add(new ItemStack(demodifiedStack.getItem(), demodifiedStack.getCount()));

		return EmiStack.of(items.get(item));
	}

	public static void register(EmiRegistry registry) {
		registry.addRecipe(new EmiTravellersGearGrindstoneRecipe(getDefaultGoggles()));
		registry.addRecipe(new EmiTravellersGearGrindstoneRecipe(getDefaultVestGloves()));
		registry.addRecipe(new EmiTravellersGearGrindstoneRecipe(getDefaultVest()));
		registry.addRecipe(new EmiTravellersGearGrindstoneRecipe(getDefaultWingsBelt()));
		registry.addRecipe(new EmiTravellersGearGrindstoneRecipe(getDefaultWings()));
		registry.addRecipe(new EmiTravellersGearGrindstoneRecipe(getDefaultBoots()));
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
		TravellersModifiers.HASTE_MODIFIER.addModifier(vest);
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
		TravellersModifiers.HASTE_MODIFIER.addModifier(wings);
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
