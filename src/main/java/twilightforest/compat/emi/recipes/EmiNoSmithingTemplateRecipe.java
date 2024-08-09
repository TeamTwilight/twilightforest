package twilightforest.compat.emi.recipes;

import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.recipe.EmiSmithingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;

import java.util.Random;

public class EmiNoSmithingTemplateRecipe extends EmiSmithingRecipe {
	private final SmithingRecipe recipe;
	private final int uniq;

	public EmiNoSmithingTemplateRecipe(EmiIngredient input, EmiIngredient addition, EmiStack output, SmithingRecipe recipe) {
		super(EmiStack.EMPTY, input, addition, output, EmiPort.getId(recipe));
		this.uniq = EmiUtil.RANDOM.nextInt();
		this.recipe = recipe;
	}

	@Override
	public boolean supportsRecipeTree() {
		return false;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 62, 1);
		widgets.addSlot(EmiStack.EMPTY, 0, 0);
		widgets.addGeneratedSlot((r) -> this.getStack(r, 0), this.uniq, 18, 0).appendTooltip(() -> EmiTooltipComponents.getIngredientTooltipComponent(this.input.getEmiStacks()));
		widgets.addGeneratedSlot((r) -> this.getStack(r, 1), this.uniq, 36, 0).appendTooltip(() -> EmiTooltipComponents.getIngredientTooltipComponent(this.addition.getEmiStacks()));
		widgets.addGeneratedSlot((r) -> this.getStack(r, 2), this.uniq, 94, 0).recipeContext(this);
	}

	private EmiStack getStack(Random r, int i) {
		EmiStack input = this.input.getEmiStacks().get(r.nextInt(this.input.getEmiStacks().size()));
		EmiStack addition = this.addition.getEmiStacks().get(r.nextInt(this.addition.getEmiStacks().size()));
		SmithingRecipeInput inv = new SmithingRecipeInput(ItemStack.EMPTY, input.getItemStack(), addition.getItemStack());
		Minecraft client = Minecraft.getInstance();
		return new EmiStack[]{input, addition, EmiStack.of(this.recipe.assemble(inv, client.level.registryAccess()))}[i];
	}
}
