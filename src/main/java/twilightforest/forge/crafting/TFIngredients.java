package twilightforest.forge.crafting;

import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;

/** Les types d'ingredients que NeoForge fournissait et que Forge n'a pas. */
public final class TFIngredients {

	public static final DeferredRegister<IIngredientSerializer<?>> INGREDIENT_SERIALIZERS =
			DeferredRegister.create(ForgeRegistries.Keys.INGREDIENT_SERIALIZERS, TwilightForestMod.ID);

	public static final RegistryObject<IIngredientSerializer<ComponentsIngredient>> COMPONENTS =
			INGREDIENT_SERIALIZERS.register("components", () -> ComponentsIngredient.SERIALIZER);

	private TFIngredients() {
	}
}
