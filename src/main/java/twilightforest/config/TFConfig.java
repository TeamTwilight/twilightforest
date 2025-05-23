package twilightforest.config;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.TranslatableEnum;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.network.SyncUncraftingTableConfigPacket;
import twilightforest.util.PlayerHelper;

import java.net.Proxy;
import java.util.*;

public class TFConfig {

	public static final String CONFIG_ID = "config." + TwilightForestMod.ID + ".";
	@Nullable
	private static ResourceLocation portalLockingAdvancement;
	private static final List<Holder<Biome>> VALID_AURORA_BIOMES = new ArrayList<>();
	public static final List<GameProfile> GAME_PROFILES = new ArrayList<>();

	/// --- CLIENT ---
	public static boolean silentCicadas = false;
	public static boolean silentCicadasOnHead = false;
	public static boolean firstPersonEffects = true;
	public static boolean rotateTrophyHeadsGui = true;
	public static boolean disableOptifineNagScreen = false;
	public static boolean disableLockedBiomeToasts = false;
	public static boolean showQuestRamCrosshairIndicator = true;
	public static boolean showFortificationShieldIndicator = true;
	public static boolean showFortificationShieldIndicatorInCreative = false;
	private static int clientCloudBlockPrecipitationDistance = 32;
	public static boolean prettifyOreMeterGui = true;
	public static boolean spawnCharmAnimationAsTotem = false;

	// --- COMMON ---
	public static boolean casketUUIDLocking = false;
	public static boolean disableSkullCandles = false;
	public static boolean defaultItemEnchants = true;
	public static boolean bossDropChests = true;
	public static MultiplayerFightAdjuster multiplayerFightAdjuster = MultiplayerFightAdjuster.NONE;
	public static int commonCloudBlockPrecipitationDistance = 32;

	// -- Dimension --
	public static boolean newPlayersSpawnInTF = false;
	public static boolean portalForNewPlayerSpawn = true;

	// -- Portal --
	public static String originDimension = Level.OVERWORLD.location().toString();
	public static boolean allowPortalsInOtherDimensions = false;
	public static int portalCreationPermission = 0;
	public static boolean disablePortalCreation = false;
	public static boolean checkPortalPlacement = true;
	public static boolean destructivePortalLightning = true;
	public static boolean shouldReturnPortalBeUsable = true;
	public static int maxPortalSize = 64;

	// -- Uncrafting Table --
	public static double uncraftingXpCostMultiplier = 1.0D;
	public static double repairingXpCostMultiplier = 1.0D;
	public static boolean allowShapelessUncrafting = false;
	public static boolean disableIngredientSwitching = false;
	public static List<? extends String> disableUncraftingRecipes = new ArrayList<>();
	public static boolean reverseRecipeBlacklist = false;
	public static List<? extends String> blacklistedUncraftingModIds = new ArrayList<>();
	public static boolean flipUncraftingModIdList = false;
	public static boolean disableUncraftingOnly = false;
	public static boolean disableEntireTable = false;

	// -- Magic Trees --
	public static boolean disableTimeCore = false;
	public static int timeCoreRange = 16;
	public static boolean disableTransformationCore = false;
	public static int transformationCoreRange = 16;
	public static boolean disableMiningCore = false;
	public static int miningCoreRange = 16;
	public static boolean disableSortingCore = false;
	public static int sortingCoreRange = 16;

	// -- Shield Parrying --
	public static boolean parryNonTwilightAttacks = false;
	public static int shieldParryTicks = 40;

	public static int getClientCloudBlockPrecipitationDistance() {
		return clientCloudBlockPrecipitationDistance == -1 ? commonCloudBlockPrecipitationDistance : clientCloudBlockPrecipitationDistance;
	}

	@Nullable
	public static ResourceLocation getPortalLockingAdvancement(Player player) {
		//only run assigning logic if the config has an advancement set and the RL is null
		if (portalLockingAdvancement == null && !ConfigSetup.COMMON_CONFIG.PORTAL.portalAdvancementLock.get().isEmpty()) {

			ResourceLocation lock = ResourceLocation.tryParse(ConfigSetup.COMMON_CONFIG.PORTAL.portalAdvancementLock.get());
			if (lock == null || PlayerHelper.getAdvancement(player, lock) == null) {
				//if the RL is not a valid advancement fail us
				TwilightForestMod.LOGGER.fatal("The portal locking advancement is not a valid advancement! Setting to null!");
				ConfigSetup.COMMON_CONFIG.PORTAL.portalAdvancementLock.set("");
			} else {
				portalLockingAdvancement = ResourceLocation.tryParse(ConfigSetup.COMMON_CONFIG.PORTAL.portalAdvancementLock.get());
				TwilightForestMod.LOGGER.debug("Portal locking advancement reloaded. Current advancement to check for is: {}", portalLockingAdvancement);
			}
		}
		//always return the RL, even if its null. We can use this to run logic less often
		return portalLockingAdvancement;
	}

	//Forge's biome registry doesn't contain biomes done via datapacks, so we have to use registryaccess
	public static List<Holder<Biome>> getValidAuroraBiomes(RegistryAccess access) {
		if (VALID_AURORA_BIOMES.isEmpty() && !ConfigSetup.CLIENT_CONFIG.auroraBiomes.get().isEmpty()) {
			ConfigSetup.CLIENT_CONFIG.auroraBiomes.get().forEach(s -> {
				Optional<Holder<Biome>> holder = Optional.ofNullable(ResourceLocation.tryParse(s)).flatMap(key -> access.lookupOrThrow(Registries.BIOME).get(key));
				if (holder.isEmpty()) {
					TwilightForestMod.LOGGER.warn("Biome {} in Twilight Forest's validAuroraBiomes config option is not a valid biome. Skipping!", s);
				} else {
					VALID_AURORA_BIOMES.add(holder.get());
				}
			});
		}
		return VALID_AURORA_BIOMES;
	}

	protected static void rebakeCommonOptions(TFCommonConfig config) {
		casketUUIDLocking = config.casketUUIDLocking.get();
		disableSkullCandles = config.disableSkullCandles.get();
		defaultItemEnchants = config.defaultItemEnchants.get();
		bossDropChests = config.bossDropChests.get();
		commonCloudBlockPrecipitationDistance = config.cloudBlockPrecipitationDistance.get();
		multiplayerFightAdjuster = config.multiplayerFightAdjuster.get();

		//Dimension
		newPlayersSpawnInTF = config.DIMENSION.newPlayersSpawnInTF.get();
		portalForNewPlayerSpawn = config.DIMENSION.portalForNewPlayerSpawn.get();

		//Portal
		originDimension = config.PORTAL.originDimension.get();
		allowPortalsInOtherDimensions = config.PORTAL.allowPortalsInOtherDimensions.get();
		portalCreationPermission = config.PORTAL.portalCreationPermission.get();
		disablePortalCreation = config.PORTAL.disablePortalCreation.get();
		checkPortalPlacement = config.PORTAL.checkPortalPlacement.get();
		destructivePortalLightning = config.PORTAL.destructivePortalLightning.get();
		shouldReturnPortalBeUsable = config.PORTAL.shouldReturnPortalBeUsable.get();
		maxPortalSize = config.PORTAL.maxPortalSize.get();

		//Uncrafting Table
		uncraftingXpCostMultiplier = config.UNCRAFTING_STUFFS.uncraftingXpCostMultiplier.get();
		repairingXpCostMultiplier = config.UNCRAFTING_STUFFS.repairingXpCostMultiplier.get();
		allowShapelessUncrafting = config.UNCRAFTING_STUFFS.allowShapelessUncrafting.get();
		disableIngredientSwitching = config.UNCRAFTING_STUFFS.disableIngredientSwitching.get();
		disableUncraftingOnly = config.UNCRAFTING_STUFFS.disableUncraftingOnly.get();
		disableEntireTable = config.UNCRAFTING_STUFFS.disableEntireTable.get();
		disableUncraftingRecipes = config.UNCRAFTING_STUFFS.disableUncraftingRecipes.get();
		reverseRecipeBlacklist = config.UNCRAFTING_STUFFS.reverseRecipeBlacklist.get();
		blacklistedUncraftingModIds = config.UNCRAFTING_STUFFS.blacklistedUncraftingModIds.get();
		flipUncraftingModIdList = config.UNCRAFTING_STUFFS.flipUncraftingModIdList.get();

		// Tree Cores
		disableTimeCore = config.MAGIC_TREES.timeRange.get() <= 0;
		timeCoreRange = config.MAGIC_TREES.timeRange.get();
		disableTransformationCore = config.MAGIC_TREES.transformationRange.get() <= 0;
		transformationCoreRange = config.MAGIC_TREES.transformationRange.get();
		disableMiningCore = config.MAGIC_TREES.miningRange.get() <= 0;
		miningCoreRange = config.MAGIC_TREES.miningRange.get();
		disableSortingCore = config.MAGIC_TREES.sortingRange.get() <= 0;
		sortingCoreRange = config.MAGIC_TREES.sortingRange.get();

		//Parrying
		parryNonTwilightAttacks = config.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get();
		shieldParryTicks = config.SHIELD_INTERACTIONS.shieldParryTicks.get();

		//resends uncrafting settings to all players when the config is reloaded. This ensures all players have matching configs so things don't desync.
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server != null && server.isDedicatedServer()) {
			PacketDistributor.sendToAllPlayers(new SyncUncraftingTableConfigPacket(
				uncraftingXpCostMultiplier, repairingXpCostMultiplier,
				allowShapelessUncrafting, disableIngredientSwitching,
				disableUncraftingOnly, disableEntireTable,
				disableUncraftingRecipes, reverseRecipeBlacklist,
				blacklistedUncraftingModIds, flipUncraftingModIdList));
		}
		//sets cached portal locking advancement to null just in case it changed
		portalLockingAdvancement = null;
	}

	protected static void rebakeClientOptions(TFClientConfig config) {
		reloadGiantSkins(config);
		VALID_AURORA_BIOMES.clear();
		silentCicadas = config.silentCicadas.get();
		silentCicadasOnHead = config.silentCicadasOnHead.get();
		firstPersonEffects = config.firstPersonEffects.get();
		rotateTrophyHeadsGui = config.rotateTrophyHeadsGui.get();
		disableOptifineNagScreen = config.disableOptifineNagScreen.get();
		disableLockedBiomeToasts = config.disableLockedBiomeToasts.get();
		showFortificationShieldIndicator = config.showFortificationShieldIndicator.get();
		showFortificationShieldIndicatorInCreative = config.showFortificationShieldIndicatorInCreative.get();
		showQuestRamCrosshairIndicator = config.showQuestRamCrosshairIndicator.get();
		clientCloudBlockPrecipitationDistance = config.cloudBlockPrecipitationDistance.get();
		prettifyOreMeterGui = config.prettifyOreMeterGui.get();
		spawnCharmAnimationAsTotem = config.spawnCharmAnimationAsTotem.get();
	}

	private static void reloadGiantSkins(TFClientConfig config) {
		if (!config.giantSkinUUIDs.get().isEmpty()) {
			new Thread() {
				@Override
				public void run() {
					GAME_PROFILES.clear();
					YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY);
					MinecraftSessionService session = service.createMinecraftSessionService();
					for (String stringUUID : config.giantSkinUUIDs.get()) {
						try {
							ProfileResult result = session.fetchProfile(UUID.fromString(stringUUID), false);
							if (result != null) {
								GAME_PROFILES.add(result.profile());
							}
						} catch (IllegalArgumentException e) {
							TwilightForestMod.LOGGER.error("\"{}\" is not a valid UUID!", stringUUID);
						}
					}
					super.run();
				}
			}.start();
		}
	}

	public enum MultiplayerFightAdjuster implements TranslatableEnum {
		NONE(false, false),
		MORE_LOOT(true, false),
		MORE_HEALTH(false, true),
		MORE_LOOT_AND_HEALTH(true, true);

		private final boolean moreLoot;
		private final boolean moreHealth;

		MultiplayerFightAdjuster(boolean loot, boolean health) {
			this.moreLoot = loot;
			this.moreHealth = health;
		}

		public boolean adjustsLootRolls() {
			return this.moreLoot;
		}

		public boolean adjustsHealth() {
			return this.moreHealth;
		}

		@Override
		public Component getTranslatedName() {
			return Component.translatable("config.twilightforest.multiplayer_fight_adjuster." + this.name().toLowerCase(Locale.ROOT));
		}
	}
}
