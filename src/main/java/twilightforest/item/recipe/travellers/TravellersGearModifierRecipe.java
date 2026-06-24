package twilightforest.item.recipe.travellers;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiable;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.StreamSupport;

public abstract class TravellersGearModifierRecipe extends CustomRecipe {
	protected final ResourceKey<TravellersModifier> travellersModifierKey;
	public TravellersGearModifierRecipe(ResourceKey<TravellersModifier> travellersModifier) {
		super();
		this.travellersModifierKey = travellersModifier;
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
			&& !TravellersModifiersManager.hasTravellersModifier(level.registryAccess(), stack, this.travellersModifierKey)
			&& TravellersModifiersManager.getModifierDataComponentProviders(input.items().stream().map((s) -> Ingredient.of(s.getItem())).toList(), this.travellersModifierKey) <= 1;
	}

	@Override
	public ItemStack assemble(CraftingInput input) {
		ItemStack travellerArmorStack = getModifiableArmor(input);
		if (travellerArmorStack == null)
			return ItemStack.EMPTY;  // Should never happen

		ItemStack stack = travellerArmorStack.copy();
		return applyModifier(stack, input.items().stream().map((itemStack) -> Ingredient.of(itemStack.getItem())).toList());
	}

	public ItemStack applyModifier(ItemStack stack, List<Ingredient> inputs) {
		if (TravellersModifiersManager.transferModifier(stack, inputs, this.travellersModifierKey))
			return stack;
		boolean modifierAdded = TravellersModifiersManager.addModifier(stack, this.travellersModifierKey);
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
		return new ItemStack(StreamSupport.stream(ingredients.spliterator(), false)
			.flatMap(ingredient -> ingredient.getValues().stream())
			.filter(stack -> (stack).value() instanceof TravellersModifiable).findFirst().orElseThrow().value());
	}

	public Identifier getId() {
		return travellersModifierKey.identifier()
			.withPrefix(StringUtils.substringAfterLast(getModifiableArmorFromIngredients(placementInfo().ingredients()).getItemName().getString(), '.') + "/")
			.withPrefix("add_modifier_to_travellers_gear/")
			.withSuffix("_modifier");
	}

	public ResourceKey<TravellersModifier> getTravellersModifierKey() {
		return travellersModifierKey;
	}
}
