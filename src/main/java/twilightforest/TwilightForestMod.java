package twilightforest;

import com.google.common.reflect.Reflection;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tamaized.beanification.BeanContext;
import tamaized.beanification.Configurable;
// TODO: Port compat modules
//import twilightforest.compat.CosmeticArmorCompat;
//import twilightforest.compat.curios.CuriosCompat;
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

	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	static {
		BeanContext.init(ID);
		LOGGER.info("[TF-DEBUG] TwilightForestMod static init called!");
	}


	public TwilightForestMod(IEventBus bus, Dist dist) {
		LOGGER.info("[TF-DEBUG] TwilightForestMod constructor called!");
		Reflection.initialize(ConfigSetup.class);
		ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> ConfigurationScreen::new);

		TFItems.ITEMS.register(bus);
		TFStats.STATS.register(bus);
		TFLoot.NUMBERS.register(bus);
		TFBlocks.BLOCKS.register(bus);
		TFPOITypes.POIS.register(bus);
		TFSounds.SOUNDS.register(bus);
		TFLoot.FUNCTIONS.register(bus);
		TFLoot.CONDITIONS.register(bus);
		TFGameRules.RULES.register(bus);
		TFFeatures.FEATURES.register(bus);
		TFCreativeTabs.TABS.register(bus);
		TFLoot.CONDITIONALS.register(bus);
		ItemDisplays.DISPLAYS.register(bus);
		TFMenuTypes.CONTAINERS.register(bus);
		TFRecipes.RECIPE_TYPES.register(bus);
		TFEntities.ENTITY_TYPES.register(bus);
		TFAttributes.ATTRIBUTES.register(bus);
		TFAdvancements.TRIGGERS.register(bus);
		TFMobEffects.MOB_EFFECTS.register(bus);
		//TFItemSubPredicates.TYPES.register(bus); TODO: check comment
		Enforcements.ENFORCEMENTS.register(bus);
		TFCaveCarvers.CARVER_TYPES.register(bus);
		TFDataComponents.COMPONENTS.register(bus);
		TFRecipes.RECIPE_SERIALIZERS.register(bus);
		TFMapDecorations.DECORATIONS.register(bus);
		TFParticleType.PARTICLE_TYPES.register(bus);
		TravellersModifierTypes.TYPES.register(bus);
		TFBlockEntities.BLOCK_ENTITIES.register(bus);
		TFLootModifiers.LOOT_MODIFIERS.register(bus);
		TFConsumeEffects.CONSUME_EFFECTS.register(bus);
		TFStructureTypes.STRUCTURE_TYPES.register(bus);
		TFFeatureModifiers.TRUNK_PLACERS.register(bus);
		BiomeLayerTypes.BIOME_LAYER_TYPES.register(bus);
		TFDataAttachments.ATTACHMENT_TYPES.register(bus);
		TFDataSerializers.DATA_SERIALIZERS.register(bus);
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

		if (dist.isClient()) {
			bus.addListener(net.neoforged.neoforge.client.event.ModelEvent.ModifyBakingResult.class, twilightforest.client.event.ClockModelHandler::onModifyBakingResult);
		}

		//TODO: Port compat modules
		//if (ModList.get().isLoaded("curios")) loadCuriosCompat(bus);
		//if (ModList.get().isLoaded("cosmeticarmorreworked")) NeoForge.EVENT_BUS.addListener(CosmeticArmorCompat::keepCosmeticArmor);
	}

//	private static void loadCuriosCompat(IEventBus bus) {
//		NeoForge.EVENT_BUS.addListener(CuriosCompat::keepCurios);
//		bus.addListener(CuriosCompat::registerCuriosCapabilities);
//		bus.addListener(CuriosCompat::registerCurioRenderers);
//		bus.addListener(CuriosCompat::registerCurioLayers);
//	}

	public static Identifier prefix(String name) {
		return Identifier.fromNamespaceAndPath(ID, name.toLowerCase(Locale.ROOT));
	}

	public static Identifier getModelTexture(String name) {
		return Identifier.fromNamespaceAndPath(ID, MODEL_DIR + name);
	}

	public static Identifier getGuiTexture(String name) {
		return Identifier.fromNamespaceAndPath(ID, GUI_DIR + name);
	}

	public static Identifier getEnvTexture(String name) {
		return Identifier.fromNamespaceAndPath(ID, ENVIRO_DIR + name);
	}
}
