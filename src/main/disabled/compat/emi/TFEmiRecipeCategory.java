package twilightforest.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ItemLike;
import twilightforest.TwilightForestMod;

public class TFEmiRecipeCategory extends EmiRecipeCategory {
	public final String name;
	public final Component title;

	public TFEmiRecipeCategory(String name, ItemLike icon) {
		super(TwilightForestMod.prefix(name), EmiStack.of(icon));
		this.name = name;
		this.title = Component.translatable("gui.twilightforest.%s_jei".formatted(name));
	}

	@Override
	public Component getName() {
		return this.title;
	}
}
