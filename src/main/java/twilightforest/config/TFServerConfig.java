package twilightforest.config;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class TFServerConfig {

	final UncraftingStuff UNCRAFTING_STUFFS = new UncraftingStuff();

	public TFServerConfig(ModConfigSpec.Builder builder) {
		builder.comment(ConfigComments.UNCRAFTING_TABLE).translation(TFConfig.CONFIG_ID + "uncrafting").push("Uncrafting Table");
		{
			UNCRAFTING_STUFFS.uncraftingXpCostMultiplier = builder
				.translation(TFConfig.CONFIG_ID + "uncrafting_xp_cost")
				.comment(ConfigComments.UNCRAFTING_MULTIPLIER)
				.defineInRange("uncraftingXpCostMultiplier", 1.0D, 0.0D, Double.MAX_VALUE);
			UNCRAFTING_STUFFS.repairingXpCostMultiplier = builder
				.translation(TFConfig.CONFIG_ID + "repairing_xp_cost")
				.comment(ConfigComments.REPAIR_MULTIPLIER)
				.defineInRange("repairingXpCostMultiplier", 1.0D, 0.0D, Double.MAX_VALUE);
			UNCRAFTING_STUFFS.disableUncraftingRecipes = builder
				.translation(TFConfig.CONFIG_ID + "uncrafting_recipes")
				.comment(ConfigComments.RECIPE_WHITELIST)
				.defineListAllowEmpty("disableUncraftingRecipes", List.of("twilightforest:giant_log_to_oak_planks"), () -> "", s -> s instanceof String);
			UNCRAFTING_STUFFS.reverseRecipeBlacklist = builder
				.translation(TFConfig.CONFIG_ID + "uncrafting_recipes_flip")
				.comment(ConfigComments.RECIPE_BLACKLIST)
				.define("flipRecipeList", false);
			UNCRAFTING_STUFFS.blacklistedUncraftingModIds = builder
				.translation(TFConfig.CONFIG_ID + "uncrafting_mod_ids")
				.comment(ConfigComments.MOD_ID_WHITELIST)
				.defineListAllowEmpty("blacklistedUncraftingModIds", new ArrayList<>(), () -> "", s -> s instanceof String string && Identifier.isValidNamespace(string));
			UNCRAFTING_STUFFS.flipUncraftingModIdList = builder
				.translation(TFConfig.CONFIG_ID + "uncrafting_mod_id_flip")
				.comment(ConfigComments.MOD_ID_BLACKLIST)
				.define("flipIdList", false);
			UNCRAFTING_STUFFS.allowShapelessUncrafting = builder
				.translation(TFConfig.CONFIG_ID + "shapeless_uncrafting")
				.comment(ConfigComments.SHAPELESS_UNCRAFTING)
				.define("enableShapelessCrafting", false);
			UNCRAFTING_STUFFS.disableIngredientSwitching = builder
				.translation(TFConfig.CONFIG_ID + "ingredient_switching")
				.comment(ConfigComments.INGREDIENT_SWITCHING)
				.define("disableIngredientSwitching", false);
			UNCRAFTING_STUFFS.disableUncraftingOnly = builder
				.translation(TFConfig.CONFIG_ID + "disable_uncrafting")
				.comment(ConfigComments.DISABLE_UNCRAFTING)
				.define("disableUncrafting", false);
			UNCRAFTING_STUFFS.disableEntireTable = builder
				.translation(TFConfig.CONFIG_ID + "disable_uncrafting_table")
				.comment(ConfigComments.DISABLE_TABLE)
				.define("disableUncraftingTable", false);
		}
		builder.pop();
	}

	static class UncraftingStuff {
		ModConfigSpec.DoubleValue uncraftingXpCostMultiplier;
		ModConfigSpec.DoubleValue repairingXpCostMultiplier;
		ModConfigSpec.BooleanValue allowShapelessUncrafting;
		ModConfigSpec.BooleanValue disableIngredientSwitching;
		ModConfigSpec.ConfigValue<List<? extends String>> disableUncraftingRecipes;
		ModConfigSpec.BooleanValue reverseRecipeBlacklist;
		ModConfigSpec.ConfigValue<List<? extends String>> blacklistedUncraftingModIds;
		ModConfigSpec.BooleanValue flipUncraftingModIdList;
		ModConfigSpec.BooleanValue disableUncraftingOnly;
		ModConfigSpec.BooleanValue disableEntireTable;
	}
}
