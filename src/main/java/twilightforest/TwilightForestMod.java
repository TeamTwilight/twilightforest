package twilightforest;

import com.google.common.base.Optional;
import com.google.common.collect.*;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.advancements.TFAdvancements;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.capabilities.shield.ShieldCapabilityHandler;
import twilightforest.capabilities.shield.ShieldCapabilityStorage;
import twilightforest.compat.TFCompat;
import twilightforest.item.TFItems;
import twilightforest.loot.TFTreasure;
import twilightforest.network.TFPacketHandler;
import twilightforest.structures.hollowtree.TFHollowTreePieces;
import twilightforest.structures.start.StructureStartNothing;
import twilightforest.tileentity.*;
import twilightforest.world.WorldProviderTwilightForest;

@SuppressWarnings({"Guava", "unchecked"})
@Mod( modid = TwilightForestMod.ID,
		name = "The Twilight Forest",
		version = TwilightForestMod.VERSION,
		acceptedMinecraftVersions = "[1.12.2]",
		dependencies = "after:ctm@[MC1.12-0.3.2.18,);before:immersiveengineering@[0.12-83,);after:tconstruct;required-after:forge@[14.23.3.2655,);after:thaumcraft@[1.12.2-6.1.BETA20,)",
		updateJSON = "https://raw.githubusercontent.com/TeamTwilight/twilightforest/1.12.x/update.json"
)
public class TwilightForestMod {
	public static final String ID = "twilightforest";
	public static final String VERSION = "@VERSION@";

	public static final String MODEL_DIR = "twilightforest:textures/model/";
	public static final String GUI_DIR = "twilightforest:textures/gui/";
	public static final String ENVRIO_DIR = "twilightforest:textures/environment/";
	public static final String ARMOR_DIR = "twilightforest:textures/armor/";
	public static final String ENFORCED_PROGRESSION_RULE = "tfEnforcedProgression";

	public static final int GUI_ID_UNCRAFTING = 1;
	public static final int GUI_ID_FURNACE = 2;

	public static DimensionType dimType;
	public static int backupdimensionID = -777;

	public static final Logger LOGGER = LogManager.getLogger(ID);

	private static final EnumRarity rarity = EnumHelper.addRarity("TWILIGHT", TextFormatting.DARK_GREEN, "Twilight");

	private static boolean compat = true;

	@Instance(ID)
	public static TwilightForestMod instance;

	@SidedProxy(clientSide = "twilightforest.client.TFClientProxy", serverSide = "twilightforest.TFCommonProxy")
	public static TFCommonProxy proxy;

	@SuppressWarnings("unused")
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		if (Loader.isModLoaded("sponge"))
			LOGGER.info("It looks like you have Sponge installed! You may notice Hydras spawning incorrectly with floating heads.\n" +
					"If so, please update Sponge to resolve this issue. Have fun!");

		registerTileEntities();
		dimType = DimensionType.register("twilight_forest", "_twilightforest", TFConfig.dimension.dimensionID, WorldProviderTwilightForest.class, false);

		// sounds on client, and whatever else needs to be registered pre-load
		proxy.preInit();

		CapabilityManager.INSTANCE.register(IShieldCapability.class, new ShieldCapabilityStorage(), ShieldCapabilityHandler.class);

		TFTreasure.init();

		// just call this so that we register structure IDs correctly
		LOGGER.debug("There are " + TFFeature.values().length + " entries in TTFeature enum. Maximum structure size is " + TFFeature.getMaxSize());

		MapGenStructureIO.registerStructure(StructureStartNothing.class,                  				 "TFNothing");
		TFHollowTreePieces.registerPieces();

		compat = TFConfig.doCompat;

		if (compat) {
			try {
				TFCompat.preInitCompat();
			} catch (Exception e) {
				compat = false;
				TwilightForestMod.LOGGER.error(ID + " had an error loading preInit compatibility!");
				TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
			}
		} else {
			TwilightForestMod.LOGGER.warn(ID + " is skipping! compatibility!");
		}
	}

	@SuppressWarnings("unused")
	@EventHandler
	public void init(FMLInitializationEvent evt) {
		TFItems.initRepairMaterials();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		TFPacketHandler.init();
		proxy.init();
		TFAdvancements.init();

		if (compat) {
			try {
				TFCompat.initCompat();
			} catch (Exception e) {
				compat = false;
				TwilightForestMod.LOGGER.error(ID + " had an error loading init compatibility!");
				TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
			}
		}

		TFDataFixers.init();
	}

	@SuppressWarnings("unused")
	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		if (!DimensionManager.isDimensionRegistered(TFConfig.dimension.dimensionID)) {
			DimensionManager.registerDimension(TFConfig.dimension.dimensionID, TwilightForestMod.dimType);
		} else {
			TwilightForestMod.LOGGER.warn("Detected that the configured dimension id '{}' is being used. Using backup ID. It is recommended that you configure this mod to use a unique dimension ID.", TFConfig.dimension.dimensionID);
			DimensionManager.registerDimension(TwilightForestMod.backupdimensionID, TwilightForestMod.dimType);
			TFConfig.dimension.dimensionID = TwilightForestMod.backupdimensionID;
		}

		if (compat) {
			try {
				TFCompat.postInitCompat();
			} catch (Exception e) {
				TwilightForestMod.LOGGER.error(ID + " had an error loading postInit compatibility!");
				TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
			}
		}
	}

	private static final ImmutableSet.Builder<IBlockState> BLACKLIST_BUILDER = ImmutableSet.builder();
	private static final ImmutableList.Builder<IBlockState> HILL_BLOCKS_BUILDER = ImmutableList.builder();
	private static final ImmutableList.Builder<ItemStack> LOADING_ICONS_BUILDER = ImmutableList.builder();
	private static final ImmutableMultimap.Builder<IBlockState, IBlockState> CRUMBLE_BLOCKS_BUILDER = ImmutableMultimap.builder();

	/**
	 IMC NBT Format: You can send all of your requests as one big NBT list rather than needing to shotgun a ton of tiny NBT messages.

	 root:
	 	• "Blacklist"                               - NBTTagList     : List of blockstates to blacklist from blockbreaking (antibuilders, naga, hydra, etc)
	 		• List Entry                            - NBTTagCompound : An IBlockState
	 			• "Name"                            - String         : Resource location of block. Is not allowed to be Air.
	 			• "Properties"                      - NBTTagCompound : Additional blockstate modifications to apply to block
	 				• [String Property Key]         - String         : Key is nameable to a property key, and the string value attached to it is value to property.

	 	• "Hollow_Hill"                             - NBTTagList     : List of blockstates to add to hollow hills - May chance this to a function in the future
	 		• List Entry                            - NBTTagCompound : An IBlockState
	 			• "Name"                            - String         : Resource location of block. Is not allowed to be Air.
	 			• "Properties"                      - NBTTagCompound : Additional blockstate modifications to apply to block
	 				• [String Property Key]         - String         : Key is nameable to a property key, and the string value attached to it is value to property.

	 	• "Crumbling"                               - NBTTagList     : List of blockstates to add to hollow hills - May chance this to a function in the future
	 		• List Entry                            - NBTTagCompound : An IBlockState
	 			• "Name"                            - String         : Resource location of block. Is not allowed to be Air.
	 			• "Properties"                      - NBTTagCompound : Additional blockstate modifications to apply to block
	 			• "Crumbles"                        - NBTTagList     : List of different blockstates that the blockstate can crumble into
	 				• List Entry                    - NBTTagCompound : An IBlockState.
	 					• "Name"                    - String         : Resource location of block. Can be Air.
	 					• "Properties"              - NBTTagCompound : Additional blockstate modifications to apply to block
	 						• [String Property Key] - String         : Key is nameable to a property key, and the string value attached to it is value to property.
	 */

	@EventHandler
	public void onIMC(FMLInterModComms.IMCEvent event) {
		for (FMLInterModComms.IMCMessage message : event.getMessages()) {
			if (message.isNBTMessage()) {
				NBTTagCompound imcCompound = message.getNBTValue();

				deserializeBlockstatesFromTagList(imcCompound.getTagList("Blacklist"  , Constants.NBT.TAG_COMPOUND), BLACKLIST_BUILDER     );
				deserializeBlockstatesFromTagList(imcCompound.getTagList("Hollow_Hill", Constants.NBT.TAG_COMPOUND), HILL_BLOCKS_BUILDER   );

				deserializeBlockstatesFromTagList(imcCompound.getTagList("Crumbling"  , Constants.NBT.TAG_COMPOUND), CRUMBLE_BLOCKS_BUILDER);
			}

			if (message.isItemStackMessage()) {
				LOADING_ICONS_BUILDER.add(message.getItemStackValue());
			}
		}
	}

	private void deserializeBlockstatesFromTagList(NBTTagList list, ImmutableMultimap.Builder<IBlockState, IBlockState> builder) {
		for (int blockAt = 0; blockAt < list.tagCount(); blockAt++) {
			NBTTagCompound main = list.getCompoundTagAt(blockAt);
			IBlockState key = NBTUtil.readBlockState(main);

			if (key.getBlock() != Blocks.AIR) {
				NBTTagList crumbles = main.getTagList("Crumbling", Constants.NBT.TAG_COMPOUND);

				for (int crumble = 0; crumble < crumbles.tagCount(); crumble++) {
					IBlockState value = NBTUtil.readBlockState(crumbles.getCompoundTagAt(crumble));

					builder.put(key, value);
				}
			}
		}
	}

	private void deserializeBlockstatesFromTagList(NBTTagList list, ImmutableCollection.Builder<IBlockState> builder) {
		for (int blockAt = 0; blockAt < list.tagCount(); blockAt++) {
			IBlockState state = NBTUtil.readBlockState(list.getCompoundTagAt(blockAt));

			if (state.getBlock() != Blocks.AIR)
				builder.add(state);

			//Block block = Block.REGISTRY.getObject(new ResourceLocation(compound.getString("name")));

			//if (block != Blocks.AIR) {
			//	IBlockState blockState = block.getStateFromMeta(compound.getInteger("meta"));

			//	BlockStateContainer stateContainer = block.getBlockState();

			//	NBTTagList properties = compound.getTagList("state", Constants.NBT.TAG_COMPOUND);
			//	for (int stateAt = 0; stateAt < properties.tagCount(); stateAt++) {
			//		NBTTagCompound property = properties.getCompoundTagAt(stateAt);

			//		IProperty prop = stateContainer.getProperty(property.getString("property"));

			//		if (prop != null)
			//			blockState = applyBlockStateProperty(blockState, prop, prop.getValueClass(), prop.parseValue(property.getString("value")));
			//	}

			//	builder.add(blockState);
			//}
		}
	}

	/*private <V extends Comparable<V>> IBlockState applyBlockStateProperty(IBlockState state, IProperty<V> property, Class<V> target, Optional optional) {
		if (optional.isPresent() && target.isInstance(optional.get()))
			return state.withProperty(property, (V) optional.get());
		else
			return state;
	}*/

	public static ImmutableSet<IBlockState> getBlacklistedBlocksFromIMC() {
		return BLACKLIST_BUILDER.build();
	}

	public static ImmutableList<IBlockState> getHollowHillBlocksFromIMC() {
		return HILL_BLOCKS_BUILDER.build();
	}

	public static ImmutableList<ItemStack> getLoadingIconStacksFromIMC() {
		return LOADING_ICONS_BUILDER.build();
	}

	public static ImmutableMultimap<IBlockState, IBlockState> getCrumblingBlocksFromIMC() {
		return CRUMBLE_BLOCKS_BUILDER.build();
	}

	@SuppressWarnings("unused")
	@EventHandler
	public void startServer(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandTFFeature());
	}

	private void registerTileEntities() {
		proxy.registerCritterTileEntities();

		GameRegistry.registerTileEntity(TileEntityTFNagaSpawner          .class, new ResourceLocation("twilightforest", "naga_spawner"             ));
		GameRegistry.registerTileEntity(TileEntityTFLichSpawner          .class, new ResourceLocation("twilightforest", "lich_spawner"             ));
		GameRegistry.registerTileEntity(TileEntityTFHydraSpawner         .class, new ResourceLocation("twilightforest", "hydra_spawner"            ));
		GameRegistry.registerTileEntity(TileEntityTFSmoker               .class, new ResourceLocation("twilightforest", "smoker"                   ));
		GameRegistry.registerTileEntity(TileEntityTFPoppingJet           .class, new ResourceLocation("twilightforest", "popping_jet"              ));
		GameRegistry.registerTileEntity(TileEntityTFFlameJet             .class, new ResourceLocation("twilightforest", "flame_jet"                ));
		GameRegistry.registerTileEntity(TileEntityTFTowerBuilder         .class, new ResourceLocation("twilightforest", "tower_builder"            ));
		GameRegistry.registerTileEntity(TileEntityTFAntibuilder          .class, new ResourceLocation("twilightforest", "tower_reverter"           ));
		GameRegistry.registerTileEntity(TileEntityTFTrophy               .class, new ResourceLocation("twilightforest", "trophy"                   ));
		GameRegistry.registerTileEntity(TileEntityTFTowerBossSpawner     .class, new ResourceLocation("twilightforest", "tower_boss_spawner"       ));
		GameRegistry.registerTileEntity(TileEntityTFGhastTrapInactive    .class, new ResourceLocation("twilightforest", "ghast_trap_inactive"      ));
		GameRegistry.registerTileEntity(TileEntityTFGhastTrapActive      .class, new ResourceLocation("twilightforest", "ghast_trap_active"        ));
		GameRegistry.registerTileEntity(TileEntityTFCReactorActive       .class, new ResourceLocation("twilightforest", "carminite_reactor_active" ));
		GameRegistry.registerTileEntity(TileEntityTFKnightPhantomsSpawner.class, new ResourceLocation("twilightforest", "knight_phantom_spawner"   ));
		GameRegistry.registerTileEntity(TileEntityTFSnowQueenSpawner     .class, new ResourceLocation("twilightforest", "snow_queen_spawner"       ));
		GameRegistry.registerTileEntity(TileEntityTFCinderFurnace        .class, new ResourceLocation("twilightforest", "cinder_furnace"           ));
		GameRegistry.registerTileEntity(TileEntityTFMinoshroomSpawner    .class, new ResourceLocation("twilightforest", "minoshroom_spawner"       ));
		GameRegistry.registerTileEntity(TileEntityTFAlphaYetiSpawner     .class, new ResourceLocation("twilightforest", "alpha_yeti_spawner"       ));
	}

	public static EnumRarity getRarity() {
		return rarity != null ? rarity : EnumRarity.EPIC;
	}
}
