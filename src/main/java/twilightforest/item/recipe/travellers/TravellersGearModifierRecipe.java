package twilightforest.item.recipe.travellers;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiable;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import javax.annotation.Nullable;
import java.util.stream.StreamSupport;

public abstract class TravellersGearModifierRecipe extends CustomRecipe {
	protected final Holder<TravellersModifier> travellersModifierHolder;

	public TravellersGearModifierRecipe(Holder<TravellersModifier> travellersModifierHolder) {
		super();
		this.travellersModifierHolder = travellersModifierHolder;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		ItemStack stack = getModifiableArmor(input);
		if (stack == null)
			return false;
		int slots = 0;
		if (stack.getItem() instanceof TravellersModifiable travellersModifiableItem)
			slots = travellersModifiableItem.getModifierSlots();
		return TravellersModifiersManager.countInsertableModifiers(level.registryAccess(), stack) < slots
			&& !TravellersModifiersManager.hasTravellersModifier(stack, this.travellersModifierHolder)
			&& TravellersModifiersManager.getModifierDataComponentProviders(input, this.travellersModifierHolder) <= 1;
	}

	@Override
	public ItemStack assemble(CraftingInput input) {
		ItemStack travellerArmorStack = getModifiableArmor(input);
		if (travellerArmorStack == null)
			return ItemStack.EMPTY;  // Should never happen

		ItemStack stack = travellerArmorStack.copy();
		return applyModifier(stack, input);
	}

	@Override
	public CraftingBookCategory category() {
		return CraftingBookCategory.EQUIPMENT;
	}

	public ItemStack applyModifier(ItemStack stack, CraftingInput input) {
		if (TravellersModifiersManager.transferModifier(stack, input, this.travellersModifierHolder))
			return stack;
		boolean modifierAdded = TravellersModifiersManager.addModifier(stack, this.travellersModifierHolder);
		return modifierAdded ? stack : ItemStack.EMPTY;
	}

	public abstract boolean isShapeless();

	public abstract int getWidth();

	public abstract int getHeight();

	protected static @Nullable ItemStack getModifiableArmor(CraftingInput input) {
		return getModifiableArmor(input.items());
	}

	protected static @Nullable ItemStack getModifiableArmor(Iterable<ItemStack> items) {
		return StreamSupport.stream(items.spliterator(), false)
			.filter(stack -> stack.getItem() instanceof TravellersModifiable modifiable && modifiable.getModifierSlots() > 0).findFirst().orElse(null);
	}

	public static ItemStack getModifiableArmorFromIngredients(Iterable<Ingredient> ingredients) {
		return StreamSupport.stream(ingredients.spliterator(), false)
			.flatMap(Ingredient::items)
			.map(ItemStack::new)
			.filter(stack -> stack.getItem() instanceof TravellersModifiable)
			.findFirst()
			.orElseThrow();
	}

	public Identifier getId() {
		return travellersModifierHolder.unwrapKey().orElseThrow().identifier()
			.withPrefix(StringUtils.substringAfterLast(getModifiableArmorFromIngredients(placementInfo().ingredients()).getItem().getDescriptionId(), '.') + "/")
			.withPrefix("add_modifier_to_travellers_gear/")
			.withSuffix("_modifier");
	}

	public Holder<TravellersModifier> getTravellersModifierHolder() {
		return travellersModifierHolder;
	}
}
