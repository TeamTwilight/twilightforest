package twilightforest.item.recipe;

import com.google.gson.JsonObject;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import twilightforest.TFConfig;

import java.util.function.BooleanSupplier;

public class ConditionFactories {

	public static class UncraftingEnabled implements IConditionFactory {
		@Override
		public BooleanSupplier parse(JsonContext context, JsonObject json) {
			return () -> !TFConfig.disableUncrafting;
		}
	}
}
