package twilightforest.compat.jei.subtype;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFDataComponents;

public class CasketSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {

	public static final CasketSubtypeInterpreter INSTANCE = new CasketSubtypeInterpreter();

	@Override
	public String apply(ItemStack stack, UidContext context) {
		Integer damage = stack.get(TFDataComponents.CASKET_DAMAGE);
		if (damage == null) {
			return IIngredientSubtypeInterpreter.NONE;
		}

		return damage.toString();
	}
}
