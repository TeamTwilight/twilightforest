package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

public class EmiScepterRepairRecipe extends EmiPatternCraftingRecipe {

	public EmiScepterRepairRecipe(List<EmiIngredient> input, EmiStack output, ResourceLocation id) {
		super(input, output, id);
	}

	@Override
	public SlotWidget getInputWidget(int slot, int x, int y) {
		if (slot == 0) {
			return new GeneratedSlotWidget(r -> EmiStack.of(this.damageScepter(r)), this.unique, x, y);
		} else {
			if (slot <= this.input.size()) {
				return new SlotWidget(this.input.get(slot - 1), x, y);
			}
		}
		return new SlotWidget(EmiStack.EMPTY, x, y);
	}

	@Override
	public SlotWidget getOutputWidget(int x, int y) {
		return new SlotWidget(this.output, x, y);
	}

	private ItemStack damageScepter(Random random) {
		ItemStack stack = this.output.getItemStack();
		if (stack.getMaxDamage() <= 0) {
			return stack;
		}
		int d = random.nextInt(stack.getMaxDamage());
		stack.setDamageValue(d);
		return stack;
	}
}
