package twilightforest.compat;

import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import tamaized.beanification.Component;
import tamaized.beanification.connector.fml.EventBusPostConstruct;
import twilightforest.init.TFRecipes;

@Component
public class RecipeViewerSync {

	@EventBusPostConstruct(EventBusPostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(this::requestRecipeSync);
	}

	private void requestRecipeSync(OnDatapackSyncEvent event) {
		event.sendRecipes(RecipeType.CRAFTING, TFRecipes.UNCRAFTING_RECIPE.get(), TFRecipes.DRYING_RECIPE.get());
	}
}
