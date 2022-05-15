package twilightforest.compat.tcon;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.data.BaseRecipeProvider;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialRenderInfoProvider;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.library.data.recipe.ICommonRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuelBuilder;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.stats.ExtraMaterialStats;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;
import twilightforest.TwilightForestMod;
import twilightforest.compat.TConCompat;
import twilightforest.compat.TFCompat;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.item.TFItems;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class TConDataGenerator {

	public static class TConFluidTagGenerator extends FluidTagsProvider {

		public TConFluidTagGenerator(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
			super(generator, TwilightForestMod.ID, existingFileHelper);
		}

		@Override
		protected void addTags() {
			tagAll(TConCompat.MOLTEN_FIERY);
			tagAll(TConCompat.MOLTEN_KNIGHTMETAL);
			tagLocal(TConCompat.FIERY_ESSENCE);

			this.tag(TinkerTags.Fluids.METAL_TOOLTIPS)
					.addTag(TConCompat.MOLTEN_FIERY.getForgeTag())
					.addTag(TConCompat.MOLTEN_KNIGHTMETAL.getForgeTag());

			this.tag(TinkerTags.Fluids.CHEAP_METAL_SPILLING)
					.addTag(TConCompat.MOLTEN_FIERY.getForgeTag())
					.addTag(TConCompat.MOLTEN_KNIGHTMETAL.getLocalTag());
		}

		private void tagLocal(FluidObject<?> fluid) {
			tag(fluid.getLocalTag())
					.addOptional(fluid.getStill().getRegistryName())
					.addOptional(fluid.getFlowing().getRegistryName());
		}

		private void tagAll(FluidObject<?> fluid) {
			tagLocal(fluid);
			tag(fluid.getForgeTag()).addOptionalTag(fluid.getLocalTag().location());
		}
	}

	public static class TFMaterialDatagen extends AbstractMaterialDataProvider {

		public TFMaterialDatagen(DataGenerator gen) {
			super(gen);
		}

		@Override
		protected void addMaterials() {
			addMaterial(TConCompat.FIERY, 3, ORDER_WEAPON, false);
			addMaterial(TConCompat.KNIGHTMETAL, 3, ORDER_WEAPON, false);
			addMaterial(TConCompat.NAGASCALE, 1, ORDER_WEAPON, true);
			addMaterial(TConCompat.STEELEAF, 1, ORDER_WEAPON, true);
		}

		@Override
		public String getName() {
			return "Twilight Forest Materials";
		}
	}

	public static class TFMaterialRecipes extends BaseRecipeProvider implements IMaterialRecipeHelper {

		public TFMaterialRecipes(DataGenerator generator) {
			super(generator);
		}

		@Override
		protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
			materialMeltingCasting(withCondition(consumer, new ModLoadedCondition(TFCompat.TCON_ID)), TConCompat.FIERY, TConCompat.MOLTEN_FIERY, true, "tools/materials/");
			materialMeltingCasting(withCondition(consumer, new ModLoadedCondition(TFCompat.TCON_ID)), TConCompat.KNIGHTMETAL, TConCompat.MOLTEN_KNIGHTMETAL, true, "tools/materials/");

			metalMaterialRecipe(withCondition(consumer, new ModLoadedCondition(TFCompat.TCON_ID)), TConCompat.FIERY, "tools/materials/", "fiery", false);
			metalMaterialRecipe(withCondition(consumer, new ModLoadedCondition(TFCompat.TCON_ID)), TConCompat.KNIGHTMETAL, "tools/materials/", "knightmetal", false);
			materialRecipe(withCondition(consumer, new ModLoadedCondition(TFCompat.TCON_ID)), TConCompat.NAGASCALE, Ingredient.of(TFItems.NAGA_SCALE.get()), 1, 1, "tools/materials/nagascale");
			materialRecipe(withCondition(consumer, new ModLoadedCondition(TFCompat.TCON_ID)), TConCompat.STEELEAF, Ingredient.of(ItemTagGenerator.STEELEAF_INGOTS), 1, 1, "tools/materials/steeleaf");
		}


		@Override
		public String getName() {
			return "Twilight Forest Material Recipes";
		}
	}

	public static class TFMaterialRenders extends AbstractMaterialRenderInfoProvider {

		public TFMaterialRenders(DataGenerator gen, @Nullable AbstractMaterialSpriteProvider materialSprites) {
			super(gen, materialSprites);
		}

		@Override
		protected void addMaterialRenderInfo() {
			buildRenderInfo(TConCompat.FIERY).luminosity(15).fallbacks("metal");
			buildRenderInfo(TConCompat.KNIGHTMETAL).fallbacks("metal");
			buildRenderInfo(TConCompat.NAGASCALE);
			buildRenderInfo(TConCompat.STEELEAF);
		}

		@Override
		public String getName() {
			return "Twilight Forest Material Renders";
		}
	}

	public static class TFMaterialSprites extends AbstractMaterialSpriteProvider {

		@Override
		public String getName() {
			return "Twilight Forest Material Sprites";
		}

		@Override
		protected void addAllMaterials() {
			//TODO give the fiery parts a glow, for some reason setting the outline colors to them doesnt actually do jack shit
			buildMaterial(TConCompat.FIERY)
					.meleeHarvest()
					.colorMapper(GreyToColorMapping.builder()
							.addARGB(63, 0xFFFFD83A)
							.addARGB(102, 0xFFDA7600)
							.addARGB(140, 0xFF0B0507)
							.addARGB(178, 0xFF191313)
							.addARGB(216, 0xFF3A2424)
							.addARGB(255, 0xFF413333)
							.build());

			buildMaterial(TConCompat.KNIGHTMETAL)
					.meleeHarvest()
					.colorMapper(GreyToColorMapping.builderFromBlack()
							.addARGB(63, 0xFF505E4C)
							.addARGB(102, 0xFF5D7667)
							.addARGB(140, 0xFF81887D)
							.addARGB(178, 0xFFAFBAA1)
							.addARGB(216, 0xFFC4D6AE)
							.addARGB(255, 0xFFF9FFD6)
							.build());

			buildMaterial(TConCompat.NAGASCALE)
					.statType(HeadMaterialStats.ID)
					.colorMapper(GreyToColorMapping.builderFromBlack()
							.addARGB(63, 0xFF0C1708)
							.addARGB(102, 0x172911)
							.addARGB(140, 0xFF1B4B4E)
							.addARGB(178, 0xFF254B29)
							.addARGB(216, 0xFF325D25)
							.addARGB(255, 0xFF575E1C)
							.build());

			buildMaterial(TConCompat.STEELEAF)
					.meleeHarvest()
					.colorMapper(GreyToColorMapping.builderFromBlack()
							.addARGB(63, 0xFF081608)
							.addARGB(102, 0xFF1A2B11)
							.addARGB(140, 0xFF27401D)
							.addARGB(178, 0xFF345E27)
							.addARGB(216, 0xFF569243)
							.addARGB(255, 0xFF8CB05A)
							.build());
		}
	}

	public static class TFMaterialStats extends AbstractMaterialStatsDataProvider {

		public TFMaterialStats(DataGenerator gen, AbstractMaterialDataProvider materials) {
			super(gen, materials);
		}

		@Override
		public String getName() {
			return "Twilight Forest Material Stats";
		}

		@Override
		protected void addMaterialStats() {
			addMaterialStats(TConCompat.FIERY,
					new HeadMaterialStats(720, 7.2F, Tiers.DIAMOND, 6.6F),
					ExtraMaterialStats.DEFAULT,
					HandleMaterialStats.DEFAULT);

			addMaterialStats(TConCompat.KNIGHTMETAL,
					new HeadMaterialStats(900, 7.0F, Tiers.NETHERITE, 6.0F),
					ExtraMaterialStats.DEFAULT,
					HandleMaterialStats.DEFAULT);

			addMaterialStats(TConCompat.NAGASCALE,
					new HeadMaterialStats(460, 8.9F, Tiers.STONE, 4.3F));

			addMaterialStats(TConCompat.STEELEAF,
					new HeadMaterialStats(180, 7.0F, Tiers.IRON, 6.0F),
					ExtraMaterialStats.DEFAULT,
					HandleMaterialStats.DEFAULT);
		}
	}

	public static class TFMaterialTraits extends AbstractMaterialTraitDataProvider {

		public TFMaterialTraits(DataGenerator gen, AbstractMaterialDataProvider materials) {
			super(gen, materials);
		}

		@Override
		protected void addMaterialTraits() {
			addDefaultTraits(TConCompat.FIERY, TConCompat.TWILIT, TConCompat.SUPERHEAT, TinkerModifiers.autosmelt, TinkerModifiers.fiery);
			addDefaultTraits(TConCompat.KNIGHTMETAL, TConCompat.TWILIT, TConCompat.STALWART);
			addDefaultTraits(TConCompat.NAGASCALE, TConCompat.TWILIT, TConCompat.PRECIPITATE);
			addDefaultTraits(TConCompat.STEELEAF, TConCompat.TWILIT, TConCompat.SYNERGY);
		}

		@Override
		public String getName() {
			return "Twilight Forest Traits";
		}
	}

	public static class TFSmelteryRecipes extends BaseRecipeProvider implements ISmelteryRecipeHelper, ICommonRecipeHelper {

		public TFSmelteryRecipes(DataGenerator generator) {
			super(generator);
		}

		@Override
		protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
			this.metalTagCasting(withCondition(consumer, new ModLoadedCondition(TFCompat.TCON_ID)), TConCompat.MOLTEN_KNIGHTMETAL, "knightmetal", "smeltery/casting/metal/", true);
			this.metalMelting(withCondition(consumer, new ModLoadedCondition(TFCompat.TCON_ID)), TConCompat.MOLTEN_KNIGHTMETAL.get(), "knightmetal", false, "smeltery/melting/metal", false);
			this.metalTagCasting(withCondition(consumer, new ModLoadedCondition(TFCompat.TCON_ID)), TConCompat.MOLTEN_FIERY, "fiery", "smeltery/casting/metal/", true);
			this.metalMelting(withCondition(consumer, new ModLoadedCondition(TFCompat.TCON_ID)), TConCompat.MOLTEN_FIERY.get(), "fiery", false, "smeltery/melting/metal", false);

			MeltingFuelBuilder.fuel(new FluidStack(TConCompat.FIERY_ESSENCE.get(), 50), 300)
					.save(withCondition(consumer, new ModLoadedCondition(TFCompat.TCON_ID)), modResource("smeltery/melting/fuel/fiery_essence"));
		}

		@Override
		public String getModId() {
			return TwilightForestMod.ID;
		}

		@Override
		public String getName() {
			return "Twilight Forest Smeltery Recipes";
		}
	}
}
