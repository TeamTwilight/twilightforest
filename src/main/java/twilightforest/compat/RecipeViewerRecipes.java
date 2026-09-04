package twilightforest.compat;

import net.minecraft.world.item.crafting.RecipeMap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jspecify.annotations.Nullable;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;

@Component(dist = Dist.CLIENT)
public class RecipeViewerRecipes {

	@Nullable
	private RecipeMap recipeMap;

	@PostConstruct
	private void setup() {
		NeoForge.EVENT_BUS.addListener(this::cacheRecipes);
	}

	private void cacheRecipes(RecipesReceivedEvent event) {
		this.recipeMap = event.getRecipeMap();
	}

	public RecipeMap getRecipeMap() {
		return this.recipeMap == null ? RecipeMap.EMPTY : this.recipeMap;
	}
}
