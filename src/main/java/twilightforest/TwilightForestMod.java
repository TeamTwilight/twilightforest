package twilightforest;

import com.google.common.base.Suppliers;
import com.google.common.reflect.Reflection;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameRules;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import twilightforest.beans.ProcessBeanAnnotationsEvent;
import twilightforest.beans.TFBeanContext;
import twilightforest.compat.CosmeticArmorCompat;
import twilightforest.compat.curios.CuriosCompat;
import twilightforest.config.ConfigSetup;
import twilightforest.entity.passive.quest.QuestReloadListener;
import twilightforest.init.*;
import twilightforest.init.custom.*;
import twilightforest.network.*;
import twilightforest.util.TFRemapper;

import java.util.Locale;
import java.util.function.Supplier;

@Mod(TwilightForestMod.ID)
public final class TwilightForestMod {

	public static final String ID = "twilightforest";

	private static final String MODEL_DIR = "textures/entity/";
	private static final String GUI_DIR = "textures/gui/";
	private static final String ENVIRO_DIR = "textures/environment/";

	public static final Supplier<GameRules.Key<GameRules.BooleanValue>> ENFORCED_PROGRESSION_RULE = Suppliers.memoize(() -> GameRules.register("tfEnforcedProgression",
		GameRules.Category.UPDATES,  //Putting it in UPDATES since other world stuff is here
		GameRules.BooleanValue.create(true, (server, enforced) ->
			//sends a packet to every player online when this changes so weather effects update accordingly
			PacketDistributor.sendToAllPlayers(new EnforceProgressionStatusPacket(enforced.get()))
		)
	));

	public static final Logger LOGGER = LogManager.getLogger(ID);

	@Nullable
	private static QuestReloadListener QUEST_INSTANCE;

	static { // Load as early as possible
		TFBeanContext.init();
	}


	public TwilightForestMod(IEventBus bus, Dist dist) {
		bus.post(new ProcessBeanAnnotationsEvent(this)); // Enables @Autowired
		Reflection.initialize(ConfigSetup.class);
		ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> ConfigurationScreen::new);
		// Get main thread and use it to register our gamerule early
		Util.backgroundExecutor().execute(ENFORCED_PROGRESSION_RULE::get);

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
		TFMapDecorations.DECORATIONS.register(bus);
		TFParticleType.PARTICLE_TYPES.register(bus);
		TravellersModifierTypes.TYPES.register(bus);
		TFBlockEntities.BLOCK_ENTITIES.register(bus);
		TFLootModifiers.LOOT_MODIFIERS.register(bus);
		TFArmorMaterials.ARMOR_MATERIALS.register(bus);
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

		TFRemapper.addRegistryAliases();

		if (ModList.get().isLoaded("curios")) loadCuriosCompat(bus);
		if (ModList.get().isLoaded("cosmeticarmorreworked")) NeoForge.EVENT_BUS.addListener(CosmeticArmorCompat::keepCosmeticArmor);
	}

	private static void loadCuriosCompat(IEventBus bus) {
		NeoForge.EVENT_BUS.addListener(CuriosCompat::keepCurios);
		bus.addListener(CuriosCompat::registerCuriosCapabilities);
		bus.addListener(CuriosCompat::registerCurioRenderers);
		bus.addListener(CuriosCompat::registerCurioLayers);
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
