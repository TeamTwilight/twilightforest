package twilightforest;

import twilightforest.forge.attachment.Attachments;
import com.google.common.reflect.Reflection;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.forge.beans.BeanContext;
import twilightforest.forge.beans.Configurable;
import twilightforest.forge.CuriosStub;
import twilightforest.config.ConfigSetup;
import twilightforest.init.*;
import twilightforest.init.custom.*;
import twilightforest.util.TFRemapper;

import java.util.Locale;

@Configurable
@Mod(TwilightForestMod.ID)
public final class TwilightForestMod {

	public static final String ID = "twilightforest";

	private static final String MODEL_DIR = "textures/entity/";
	private static final String GUI_DIR = "textures/gui/";
	private static final String ENVIRO_DIR = "textures/environment/";

	public static final Logger LOGGER = LogManager.getLogger(ID);



	// Forge 1.21.1 ne sait injecter que le contexte de chargement (ou rien) : il cherche un
	// constructeur (FMLJavaModLoadingContext), puis un constructeur vide. La signature NeoForge
	// (IEventBus, Dist) le faisait echouer sur NoSuchMethodException <init>().
	public TwilightForestMod(FMLJavaModLoadingContext context) {
		IEventBus bus = context.getModEventBus();
		// En tete, comme l'initialiseur statique ou Beanification tournait sous NeoForge : les
		// composants doivent exister avant le reste du constructeur.
		// Les constantes d'enums d'abord : les composants les lisent par valueOf.
		twilightforest.forge.TFForgeEnums.register();
		twilightforest.forge.datamap.NeoForgeDataMaps.init();
		BeanContext.init(ID, bus);
		Reflection.initialize(ConfigSetup.class);
		bus.addListener(TFGameRules::register);

		TFItems.ITEMS.register(bus);
		TFStats.STATS.register(bus);
		TFLoot.NUMBERS.register(bus);
		TFBlocks.BLOCKS.register(bus);
		TFPOITypes.POIS.register(bus);
		TFSounds.SOUNDS.register(bus);
		TFLoot.FUNCTIONS.register(bus);
		TFLoot.CONDITIONS.register(bus);
		TFEntities.ENTITIES.register(bus);
		TFFeatures.FEATURES.register(bus);
		TFCreativeTabs.TABS.register(bus);
		TFLoot.CONDITIONALS.register(bus);
		TFEntities.SPAWN_EGGS.register(bus);
		ItemDisplays.DISPLAYS.register(bus);
		TFMenuTypes.CONTAINERS.register(bus);
		TFRecipes.RECIPE_TYPES.register(bus);
		TFAttributes.ATTRIBUTES.register(bus);
		TFAdvancements.TRIGGERS.register(bus);
		TFMobEffects.MOB_EFFECTS.register(bus);
		TFItemSubPredicates.TYPES.register(bus);
		Enforcements.ENFORCEMENTS.register(bus);
		TFCaveCarvers.CARVER_TYPES.register(bus);
		TFDataComponents.COMPONENTS.register(bus);
		TFRecipes.RECIPE_SERIALIZERS.register(bus);
		twilightforest.forge.crafting.TFIngredients.INGREDIENT_SERIALIZERS.register(bus);
		TFMapDecorations.DECORATIONS.register(bus);
		TFParticleType.PARTICLE_TYPES.register(bus);
		TravellersModifierTypes.TYPES.register(bus);
		TFBlockEntities.BLOCK_ENTITIES.register(bus);
		TFLootModifiers.LOOT_MODIFIERS.register(bus);
		TFArmorMaterials.ARMOR_MATERIALS.register(bus);
		TFStructureTypes.STRUCTURE_TYPES.register(bus);
		TFFeatureModifiers.TRUNK_PLACERS.register(bus);
		BiomeLayerTypes.BIOME_LAYER_TYPES.register(bus);
		TFDataSerializers.DATA_SERIALIZERS.register(bus);

		// Les attachements passent par une capability : sa declaration est sur le bus du mod, son
		// accrochage aux entites et la recopie a la mort sur le bus general.
		twilightforest.events.RegistrationEvents.setupPackets();

		// PORTAGE : les six registres personnalises sont crees par TFRegistries lui-meme, faute
		// d'evenement Forge equivalent. Rien a ecouter ici.
		bus.addListener(Attachments::registerCapability);
		// AttachCapabilitiesEvent est generique sous Forge : addListener le refuse.
		MinecraftForge.EVENT_BUS.addGenericListener(net.minecraft.world.entity.Entity.class, Attachments::attach);
		MinecraftForge.EVENT_BUS.addListener(Attachments::copyOnClone);
		Attachments.registerSyncEvents();
		TFFeatureModifiers.FOLIAGE_PLACERS.register(bus);
		TFFeatureModifiers.TREE_DECORATORS.register(bus);
		TFEnchantmentEffects.ENTITY_EFFECTS.register(bus);
		TFFeatureModifiers.PLACEMENT_MODIFIERS.register(bus);
		TFDensityFunctions.DENSITY_FUNCTION_TYPES.register(bus);
		TFStructureProcessors.STRUCTURE_PROCESSORS.register(bus);
		TFStructurePieceTypes.STRUCTURE_PIECE_TYPES.register(bus);
		ChunkBlanketProcessors.CHUNK_BLANKETING_TYPES.register(bus);
		TFStructurePlacementTypes.STRUCTURE_PLACEMENT_TYPES.register(bus);
		TemplateMarkerHandlers.TEMPLATE_MARKER_HANDLER_TYPES.register(bus);

		TFRemapper.addRegistryAliases();

		if (ModList.get().isLoaded("curios")) loadCuriosCompat(bus);
	}

	private static void loadCuriosCompat(IEventBus bus) {
	}

	public static ResourceLocation prefix(String name) {
		return ResourceLocation.fromNamespaceAndPath(ID, name.toLowerCase(Locale.ROOT));
	}

	public static ResourceLocation getModelTexture(String name) {
		return ResourceLocation.fromNamespaceAndPath(ID, MODEL_DIR + name);
	}

	public static ResourceLocation getGuiTexture(String name) {
		return ResourceLocation.fromNamespaceAndPath(ID, GUI_DIR + name);
	}

	public static ResourceLocation getEnvTexture(String name) {
		return ResourceLocation.fromNamespaceAndPath(ID, ENVIRO_DIR + name);
	}
}
