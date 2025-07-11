package twilightforest.compat.jei.subtype;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFDataComponents;

public class CasketSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {

	public static final CasketSubtypeInterpreter INSTANCE = new CasketSubtypeInterpreter();

	@Override
	public @Nullable Object getSubtypeData(ItemStack stack, @NotNull UidContext context) {
		Integer damage = stack.get(TFDataComponents.CASKET_DAMAGE);
		if (damage == null)
			return null;
		return damage.toString();
	}

	@Override
	public @NotNull String getLegacyStringSubtypeInfo(ItemStack ingredient, @NotNull UidContext context) {
		Integer damage = ingredient.get(TFDataComponents.CASKET_DAMAGE);
		if (damage == null)
			return "";

		return damage.toString();
	}
}
