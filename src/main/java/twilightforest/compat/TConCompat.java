package twilightforest.compat;

import net.minecraft.data.DataGenerator;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import slimeknights.mantle.registration.ModelFluidAttributes;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import twilightforest.TwilightForestMod;
import twilightforest.compat.tcon.TConDataGenerator;
import twilightforest.compat.tcon.traits.*;

public class TConCompat extends TFCompat {
	protected TConCompat() {
		super("Tinkers Construct");
	}

	@Override
	protected boolean preInit() {
		return true;
	}

	@Override
	protected void init(FMLCommonSetupEvent event) {

	}

	@Override
	protected void postInit() {

	}

	@Override
	protected void handleIMCs() {

	}

	@Override
	protected void initItems(RegistryEvent.Register<Item> evt) {

	}

	public static void tConDatagen(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		generator.addProvider(new TConDataGenerator.TConFluidTagGenerator(generator, helper));

		generator.addProvider(new TConDataGenerator.TFMaterialRecipes(generator));
		generator.addProvider(new TConDataGenerator.TFSmelteryRecipes(generator));

		AbstractMaterialSpriteProvider sprites = new TConDataGenerator.TFMaterialSprites();
		generator.addProvider(new TConDataGenerator.TFMaterialRenders(generator, sprites));

		AbstractMaterialDataProvider materials = new TConDataGenerator.TFMaterialDatagen(generator);
		generator.addProvider(materials);
		generator.addProvider(new TConDataGenerator.TFMaterialStats(generator, materials));
		generator.addProvider(new TConDataGenerator.TFMaterialTraits(generator, materials));
	}

	//Fluids
	public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(TwilightForestMod.ID);

	public static final FluidObject<ForgeFlowingFluid> MOLTEN_KNIGHTMETAL = FLUIDS.register("molten_knightmetal", ModelFluidAttributes.builder().temperature(1000).luminosity(15).sound(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA), Material.LAVA, 15);
	public static final FluidObject<ForgeFlowingFluid> MOLTEN_FIERY = FLUIDS.register("molten_fiery", ModelFluidAttributes.builder().temperature(1000).luminosity(15).sound(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA), Material.LAVA, 15);
	public static final FluidObject<ForgeFlowingFluid> FIERY_ESSENCE = FLUIDS.register("fiery_essence", ModelFluidAttributes.builder().temperature(1000).sound(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA), Material.LAVA, 0);


	//Modifiers
	public static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(TwilightForestMod.ID);

	public static final StaticModifier<PrecipitateModifier> PRECIPITATE = MODIFIERS.register("precipitate", PrecipitateModifier::new);
	public static final StaticModifier<StalwartModifier> STALWART = MODIFIERS.register("stalwart", StalwartModifier::new);
	public static final StaticModifier<SuperheatModifier> SUPERHEAT = MODIFIERS.register("superheat", SuperheatModifier::new);
	public static final StaticModifier<SynergyModifier> SYNERGY = MODIFIERS.register("synergy", SynergyModifier::new);
	public static final StaticModifier<TwilitModifier> TWILIT = MODIFIERS.register("twilit", TwilitModifier::new);

	//Materials
	public static final MaterialId FIERY = id("fiery");
	public static final MaterialId KNIGHTMETAL = id("knightmetal");
	public static final MaterialId NAGASCALE = id("nagascale");
	public static final MaterialId RAVEN_FEATHER = id("raven_feather"); //unused, no tinkers bowstrings yet
	public static final MaterialId STEELEAF = id("steeleaf");

	private static MaterialId id(String name) {
		return new MaterialId(TwilightForestMod.ID, name);
	}
}
