package twilightforest.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.SectionPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.server.ServerWorld;
import twilightforest.TFConfig;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.potions.TFPotions;
import twilightforest.structures.start.TFStructure;
import twilightforest.util.PlayerHelper;
import twilightforest.worldgen.biomes.BiomeKeys;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class TFGenerationSettings /*extends GenerationSettings*/ {

	private static final Map<ResourceLocation, ResourceLocation[]> BIOME_ADVANCEMENTS = new HashMap<>();
	private static final Map<ResourceLocation, BiConsumer<PlayerEntity, World>> BIOME_PROGRESSION_ENFORCEMENT = new HashMap<>();

	static {
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
			if (!world.isRemote && player.ticksExisted % 60 == 0) {
				player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100, 0));
				trySpawnHintMonster(player, world, TFFeature.KNIGHT_STRONGHOLD);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.DARK_FOREST_CENTER, (player, world) -> {
			if (!world.isRemote && player.ticksExisted % 60 == 0) {
				player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100, 0));
				trySpawnHintMonster(player, world, TFFeature.DARK_TOWER);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.FINAL_PLATEAU, (player, world) -> {
			if (!world.isRemote && player.ticksExisted % 5 == 0) {
				player.attackEntityFrom(DamageSource.MAGIC, 1.5F);
				world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS, 1.0F, 1.0F);
				// TODO: change this when there's a book for the castle
				trySpawnHintMonster(player, world, TFFeature.TROLL_CAVE);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.FIRE_SWAMP, (player, world) -> {
			if (!world.isRemote && player.ticksExisted % 60 == 0) {
				player.setFire(8);
			}
			trySpawnHintMonster(player, world, TFFeature.HYDRA_LAIR);
		});
		registerBiomeProgressionEnforcement(BiomeKeys.GLACIER, (player, world) -> {
			if (!world.isRemote && player.ticksExisted % 60 == 0) {
				player.addPotionEffect(new EffectInstance(TFPotions.frosty.get(), 100, 3));
			}
			trySpawnHintMonster(player, world, TFFeature.ICE_TOWER);
		});
		registerBiomeProgressionEnforcement(BiomeKeys.HIGHLANDS, (player, world) -> {
			if (!world.isRemote && player.ticksExisted % 5 == 0) {
				player.attackEntityFrom(DamageSource.MAGIC, 0.5F);
				world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS, 1.0F, 1.0F);
				trySpawnHintMonster(player, world, TFFeature.TROLL_CAVE);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.SNOWY_FOREST, (player, world) -> {
			if (!world.isRemote && player.ticksExisted % 60 == 0) {
				player.addPotionEffect(new EffectInstance(TFPotions.frosty.get(), 100, 2));
				trySpawnHintMonster(player, world, TFFeature.YETI_CAVE);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.SWAMP, (player, world) -> {
			if (!world.isRemote && player.ticksExisted % 60 == 0) {
				EffectInstance currentHunger = player.getActivePotionEffect(Effects.HUNGER);

				int hungerLevel = currentHunger != null ? currentHunger.getAmplifier() + 1 : 1;

				player.addPotionEffect(new EffectInstance(Effects.HUNGER, 100, hungerLevel));

				trySpawnHintMonster(player, world, TFFeature.LABYRINTH);
			}
		});
		registerBiomeProgressionEnforcement(BiomeKeys.THORNLANDS, (player, world) -> {
			if (!world.isRemote && player.ticksExisted % 5 == 0) {
				player.attackEntityFrom(DamageSource.MAGIC, 1.0F);
				world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS, 1.0F, 1.0F);

				// hint monster?
				trySpawnHintMonster(player, world, TFFeature.TROLL_CAVE);
			}
		});
	}

	private static void registerBiomeAdvancementRestriction(RegistryKey<Biome> biome, ResourceLocation... advancements) {
		BIOME_ADVANCEMENTS.put(biome.getLocation(), advancements);
	}

	private static void registerBiomeProgressionEnforcement(RegistryKey<Biome> biome, BiConsumer<PlayerEntity, World> exec) {
		BIOME_PROGRESSION_ENFORCEMENT.put(biome.getLocation(), exec);
	}

	public static void enforceBiomeProgression(PlayerEntity player, World world) {
		Biome currentBiome = world.getBiome(player.getPosition());
		if (isBiomeSafeFor(currentBiome, player))
			return;
		BiConsumer<PlayerEntity, World> exec = BIOME_PROGRESSION_ENFORCEMENT.get(currentBiome.getRegistryName());
		if (exec != null)
			exec.accept(player, world);
	}

	private static void trySpawnHintMonster(PlayerEntity player, World world, TFFeature feature) {
		if (world.rand.nextInt(4) == 0) {
			feature.trySpawnHintMonster(world, player);
		}
	}

	public static final int SEALEVEL = 31;
	public static final int CHUNKHEIGHT = 256; // more like world generation height
	public static final int MAXHEIGHT = 256; // actual max height

	@Nullable
	public static ChunkGeneratorTwilightBase getChunkGenerator(World world) {
		if (world instanceof ServerWorld) {
			ChunkGenerator chunkGenerator = ((ServerWorld) world).getChunkProvider().generator;
			return chunkGenerator instanceof ChunkGeneratorTwilightBase ? (ChunkGeneratorTwilightBase) chunkGenerator : null;
		}
		return null;
	}

	public static boolean isStrictlyTwilightForest(World world) {
		return world.getDimensionKey().getLocation().toString().equals(TFConfig.COMMON_CONFIG.DIMENSION.twilightForestID.get());
	}

	public static boolean isTwilightChunk(ServerWorld world) {
		return world.getChunkProvider().generator instanceof ChunkGeneratorTwilightBase;
	}

	public static boolean isProgressionEnforced(World world) {
		return world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE);
	}

	public static boolean isBiomeSafeFor(Biome biome, Entity entity) {
		ResourceLocation[] advancements = BIOME_ADVANCEMENTS.get(entity.world.isRemote() ? entity.world.func_241828_r().getRegistry(Registry.BIOME_KEY).getKey(biome) : biome.getRegistryName());
		if (advancements != null && entity instanceof PlayerEntity)
			return PlayerHelper.doesPlayerHaveRequiredAdvancements((PlayerEntity) entity, advancements);
		return true;
	}

	public static void markStructureConquered(World world, BlockPos pos, TFFeature feature) {
		ChunkGeneratorTwilightBase generator = getChunkGenerator(world);
		if (generator != null && TFFeature.getFeatureAt(pos.getX(), pos.getZ(), (ServerWorld) world) == feature) {
			//generator.setStructureConquered(pos, true);
		}
	}

	public static Optional<StructureStart<?>> locateTFStructureInRange(ISeedReader world, BlockPos pos, int range) {
		int cx1 = MathHelper.floor((pos.getX() - range) >> 4);
		int cx2 = MathHelper.ceil((pos.getX() + range) >> 4);
		int cz1 = MathHelper.floor((pos.getZ() - range) >> 4);
		int cz2 = MathHelper.ceil((pos.getZ() + range) >> 4);

		TFFeature featureCheck = TFFeature.getFeatureForRegionPos(pos.getX(), pos.getZ(), world);
		for (Structure<?> structureFeature : net.minecraftforge.registries.ForgeRegistries.STRUCTURE_FEATURES) {
			if (!(structureFeature instanceof TFStructure))
				continue;
			TFFeature feature = ((TFStructure<?>) structureFeature).getFeature();
			if (feature != featureCheck)
				continue;

			for (int x = cx1; x <= cx2; ++x) {
				for (int z = cz1; z <= cz2; ++z) {
					Optional<StructureStart<?>> structure = world.getChunk(x, z, ChunkStatus.STRUCTURE_STARTS).func_230346_b_(structureFeature).stream().
							map((longVal) -> SectionPos.from(new ChunkPos(longVal), 0)).<StructureStart<?>>map((sectionPos) -> world.
							chunkExists(sectionPos.getSectionX(), sectionPos.getSectionZ()) ? world.
							getChunk(sectionPos.getSectionX(), sectionPos.getSectionZ(), ChunkStatus.STRUCTURE_STARTS).func_230342_a_(structureFeature) : null).
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
