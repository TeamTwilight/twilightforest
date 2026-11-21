package twilightforest.compat;

import net.minecraft.world.item.crafting.RecipeMap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import org.jspecify.annotations.Nullable;
import tamaized.beanification.Component;
import tamaized.beanification.connector.fml.EventBusPostConstruct;
import tamaized.beanification.connector.fml.RequiresDist;

@Component
@RequiresDist(Dist.CLIENT)
public class RecipeViewerRecipes {

	@Nullable
	private RecipeMap recipeMap;

	@EventBusPostConstruct(EventBusPostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(this::cacheRecipes);
	}

	private void cacheRecipes(RecipesReceivedEvent event) {
		this.recipeMap = event.getRecipeMap();
	}

	public RecipeMap getRecipeMap() {
		return this.recipeMap == null ? RecipeMap.EMPTY : this.recipeMap;
	}
}
