package twilightforest.world.registration;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.potions.TFMobEffects;
import twilightforest.util.PlayerHelper;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.components.structures.start.LegacyStructureFeature;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.world.registration.biomes.BiomeKeys;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import static twilightforest.TFConstants.MOD_ID;

public class TFGenerationSettings /*extends GenerationSettings*/ {

	private static final Map<ResourceLocation, ResourceLocation[]> BIOME_ADVANCEMENTS = new HashMap<>();
	private static final Map<ResourceLocation, BiConsumer<Player, Level>> BIOME_PROGRESSION_ENFORCEMENT = new HashMap<>();

	static {
		// TODO make this data-driven
		registerBiomeAdvancementRestriction(BiomeKeys.DARK_FOREST, TwilightForestMod.prefix("progress_lich"));
		registerBiomeAdvancementRestriction(BiomeKeys.DARK_FOREST_CENTER, TwilightForestMod.prefix("progress_knights"));
		registerBiomeAdvancementRestriction(BiomeKeys.FINAL_PLATEAU, TwilightForestMod.prefix("progress_troll"));
		registerBiomeAdvancementRestriction(BiomeKeys.FIRE_SWAMP, TwilightForestMod.prefix("progress_labyrinth"));
		registerBiomeAdvancementRestriction(BiomeKeys.GLACIER, TwilightForestMod.prefix("progress_yeti"));
		registerBiomeAdvancementRestriction(BiomeKeys.HIGHLANDS, TwilightForestMod.prefix("progress_merge"));
		registerBiomeAdvancementRestriction(BiomeKeys.SNOWY_FOREST, TwilightForestMod.prefix("progress_lich"));
		registerBiomeAdvancementRestriction(BiomeKeys.SWAMP, TwilightForestMod.prefix("progress_lich"));
		registerBiomeAdvancementRestriction(BiomeKeys.THORNLANDS, TwilightForestMod.prefix("progress_troll"));

		registerBiomeProgressionEnforcement(BiomeKeys.DARK_FOREST, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, false, true));
				trySpawnHintMonster(player, world, TFFeature.KNIGHT_STRONGHOLD);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.DARK_FOREST_CENTER, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, false, true));
				trySpawnHintMonster(player, world, TFFeature.DARK_TOWER);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.FINAL_PLATEAU, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 5 == 0) {
				player.hurt(DamageSource.MAGIC, 1.5F);
				world.playSound(null, player.getX(), player.getY(), player.getZ(), TFSounds.ACID_RAIN_BURNS, SoundSource.PLAYERS, 1.0F, 1.0F);
				// TODO: change this when there's a book for the castle
				trySpawnHintMonster(player, world, TFFeature.TROLL_CAVE);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.FIRE_SWAMP, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				player.setSecondsOnFire(8);
			}
			trySpawnHintMonster(player, world, TFFeature.HYDRA_LAIR);
		});
		registerBiomeProgressionEnforcement(BiomeKeys.GLACIER, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				player.addEffect(new MobEffectInstance(TFMobEffects.FROSTY.get(), 100, 3, false, true));
			}
			trySpawnHintMonster(player, world, TFFeature.ICE_TOWER);
		});
		registerBiomeProgressionEnforcement(BiomeKeys.HIGHLANDS, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 5 == 0) {
				player.hurt(DamageSource.MAGIC, 0.5F);
				world.playSound(null, player.getX(), player.getY(), player.getZ(), TFSounds.ACID_RAIN_BURNS, SoundSource.PLAYERS, 1.0F, 1.0F);
				trySpawnHintMonster(player, world, TFFeature.TROLL_CAVE);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.SNOWY_FOREST, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				player.addEffect(new MobEffectInstance(TFMobEffects.FROSTY.get(), 100, 2, false, true));
				trySpawnHintMonster(player, world, TFFeature.YETI_CAVE);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.SWAMP, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				MobEffectInstance currentHunger = player.getEffect(MobEffects.HUNGER);

				int hungerLevel = currentHunger != null ? currentHunger.getAmplifier() + 1 : 1;

				player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, hungerLevel, false, true));

				trySpawnHintMonster(player, world, TFFeature.LABYRINTH);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.THORNLANDS, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 5 == 0) {
				player.hurt(DamageSource.MAGIC, 1.0F);
				world.playSound(null, player.getX(), player.getY(), player.getZ(), TFSounds.ACID_RAIN_BURNS, SoundSource.PLAYERS, 1.0F, 1.0F);

				// hint monster?
				trySpawnHintMonster(player, world, TFFeature.TROLL_CAVE);
			}
		});
	}

	private static void registerBiomeAdvancementRestriction(ResourceKey<Biome> biome, ResourceLocation... advancements) {
		BIOME_ADVANCEMENTS.put(biome.location(), advancements);
	}

	private static void registerBiomeProgressionEnforcement(ResourceKey<Biome> biome, BiConsumer<Player, Level> exec) {
		BIOME_PROGRESSION_ENFORCEMENT.put(biome.location(), exec);
	}

	public static void enforceBiomeProgression(Player player, Level world) {
		Biome currentBiome = world.getBiome(player.blockPosition()).value();
		if (isBiomeSafeFor(currentBiome, player))
			return;
		BiConsumer<Player, Level> exec = BIOME_PROGRESSION_ENFORCEMENT.get(currentBiome.getRegistryName());
		if (exec != null)
			exec.accept(player, world);
	}

	private static void trySpawnHintMonster(Player player, Level world, TFFeature feature) {
		if (world.random.nextInt(4) == 0) {
			feature.trySpawnHintMonster(world, player);
		}
	}

	@Deprecated // Used in places where we can't access the sea level FIXME Resolve
	public static final int SEALEVEL = 0;

	public static final ResourceLocation DIMENSION = TwilightForestMod.prefix("twilight_forest");
	public static final ResourceKey<LevelStem> WORLDGEN_KEY = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, DIMENSION);
	public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(Registry.DIMENSION_REGISTRY, DIMENSION);

	// Checks if the world is linked by the default Twilight Portal.
	// If you want to check if the world is a Twilight world, use usesTwilightChunkGenerator instead
	// Only use this method if you need to know if a world is a destination for portals!
	public static boolean isTwilightPortalDestination(Level world) {
		return DIMENSION.equals(world.dimension().location());
	}

	// Checks if the world is a qualified Twilight world by checking against its namespace or if it's a portal destination
	@OnlyIn(Dist.CLIENT)
	public static boolean isTwilightWorldOnClient(Level world) {
		return MOD_ID.equals(Minecraft.getInstance().level.dimension().location().getNamespace()) || isTwilightPortalDestination(world);
	}

	// Checks if the world is *a* Twilight world on the Server side.
	public static boolean usesTwilightChunkGenerator(ServerLevel world) {
		return world.getChunkSource().getGenerator() instanceof ChunkGeneratorTwilight;
	}

	public static boolean isProgressionEnforced(Level world) {
		return world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE);
	}

	public static boolean isBiomeSafeFor(Biome biome, Entity entity) {
		ResourceLocation[] advancements = BIOME_ADVANCEMENTS.get(entity.level.isClientSide() ? entity.level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome) : biome.getRegistryName());
		if (advancements != null && entity instanceof Player)
			return PlayerHelper.doesPlayerHaveRequiredAdvancements((Player) entity, advancements);
		return true;
	}

	public static void markStructureConquered(Level world, BlockPos pos, TFFeature feature) {
		ChunkGeneratorTwilight generator = WorldUtil.getChunkGenerator(world);
		if (generator != null && TFFeature.getFeatureAt(pos.getX(), pos.getZ(), (ServerLevel) world) == feature) {
			locateTFStructureInRange((ServerLevel) world, feature, pos, 0).ifPresent(start -> {
				if (start instanceof TFStructureStart<?> s)
					s.setConquered(true);
			});
		}
	}

	public static Optional<StructureStart> locateTFStructureInRange(WorldGenLevel world, BlockPos pos, int range) {
		TFFeature featureCheck = TFFeature.getFeatureForRegionPos(pos.getX(), pos.getZ(), world);

		return locateTFStructureInRange(world, featureCheck, pos, range);
	}

	public static Optional<StructureStart> locateTFStructureInRange(WorldGenLevel world, TFFeature featureCheck, BlockPos pos, int range) {
		int cx1 = Mth.floor((pos.getX() - range) >> 4);
		int cx2 = Mth.ceil((pos.getX() + range) >> 4);
		int cz1 = Mth.floor((pos.getZ() - range) >> 4);
		int cz2 = Mth.ceil((pos.getZ() + range) >> 4);

		for (ConfiguredStructureFeature<?, ?> structureFeature : world.registryAccess().ownedRegistryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY).stream().toList()) {
			if (!(structureFeature.feature instanceof LegacyStructureFeature legacyData))
				continue;
			TFFeature feature = legacyData.feature;
			if (feature != featureCheck)
				continue;

			for (int x = cx1; x <= cx2; ++x) {
				for (int z = cz1; z <= cz2; ++z) {
					Optional<StructureStart> structure = world.getChunk(x, z, ChunkStatus.STRUCTURE_STARTS).getReferencesForFeature(structureFeature).stream().
							map((longVal) -> SectionPos.of(new ChunkPos(longVal), 0)).map((sectionPos) -> world.
									hasChunk(sectionPos.x(), sectionPos.z()) ? world.
									getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_STARTS).getStartForFeature(structureFeature) : null).
							filter((structureStart) -> structureStart != null && structureStart.isValid()).
							findFirst();
					if (structure.isPresent())
						return structure;
				}
			}
		}
		return Optional.empty();
	}
}
