package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.*;
import twilightforest.block.entity.bookshelf.ChiseledCanopyShelfBlockEntity;
import twilightforest.block.entity.spawner.*;

@SuppressWarnings("DataFlowIssue")
public class TFBlockEntities {

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AntibuilderBlockEntity>> ANTIBUILDER = BLOCK_ENTITIES.register("antibuilder", () ->
		BlockEntityType.Builder.of(AntibuilderBlockEntity::new, TFBlocks.ANTIBUILDER.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CinderFurnaceBlockEntity>> CINDER_FURNACE = BLOCK_ENTITIES.register("cinder_furnace", () ->
		BlockEntityType.Builder.of(CinderFurnaceBlockEntity::new, TFBlocks.CINDER_FURNACE.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CarminiteReactorBlockEntity>> CARMINITE_REACTOR = BLOCK_ENTITIES.register("carminite_reactor", () ->
		BlockEntityType.Builder.of(CarminiteReactorBlockEntity::new, TFBlocks.CARMINITE_REACTOR.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ReactorDebrisBlockEntity>> REACTOR_DEBRIS = BLOCK_ENTITIES.register("reactor_debris", () ->
		BlockEntityType.Builder.of(ReactorDebrisBlockEntity::new, TFBlocks.REACTOR_DEBRIS.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FireJetBlockEntity>> FLAME_JET = BLOCK_ENTITIES.register("flame_jet", () ->
		BlockEntityType.Builder.of(FireJetBlockEntity::new, TFBlocks.FIRE_JET.get(), TFBlocks.ENCASED_FIRE_JET.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GhastTrapBlockEntity>> GHAST_TRAP = BLOCK_ENTITIES.register("ghast_trap", () ->
		BlockEntityType.Builder.of(GhastTrapBlockEntity::new, TFBlocks.GHAST_TRAP.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TFSmokerBlockEntity>> SMOKER = BLOCK_ENTITIES.register("smoker", () ->
		BlockEntityType.Builder.of(TFSmokerBlockEntity::new, TFBlocks.SMOKER.get(), TFBlocks.ENCASED_SMOKER.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CarminiteBuilderBlockEntity>> TOWER_BUILDER = BLOCK_ENTITIES.register("tower_builder", () ->
		BlockEntityType.Builder.of(CarminiteBuilderBlockEntity::new, TFBlocks.CARMINITE_BUILDER.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TrophyBlockEntity>> TROPHY = BLOCK_ENTITIES.register("trophy", () ->
		BlockEntityType.Builder.of(TrophyBlockEntity::new, TFBlocks.NAGA_TROPHY.get(), TFBlocks.LICH_TROPHY.get(), TFBlocks.MINOSHROOM_TROPHY.get(),
			TFBlocks.HYDRA_TROPHY.get(), TFBlocks.KNIGHT_PHANTOM_TROPHY.get(), TFBlocks.UR_GHAST_TROPHY.get(), TFBlocks.ALPHA_YETI_TROPHY.get(),
			TFBlocks.SNOW_QUEEN_TROPHY.get(), TFBlocks.QUEST_RAM_TROPHY.get(), TFBlocks.NAGA_WALL_TROPHY.get(), TFBlocks.LICH_WALL_TROPHY.get(),
			TFBlocks.MINOSHROOM_WALL_TROPHY.get(), TFBlocks.HYDRA_WALL_TROPHY.get(), TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.get(), TFBlocks.UR_GHAST_WALL_TROPHY.get(),
			TFBlocks.ALPHA_YETI_WALL_TROPHY.get(), TFBlocks.SNOW_QUEEN_WALL_TROPHY.get(), TFBlocks.QUEST_RAM_WALL_TROPHY.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AlphaYetiSpawnerBlockEntity>> ALPHA_YETI_SPAWNER = BLOCK_ENTITIES.register("alpha_yeti_spawner", () ->
		BlockEntityType.Builder.of(AlphaYetiSpawnerBlockEntity::new, TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FinalBossSpawnerBlockEntity>> FINAL_BOSS_SPAWNER = BLOCK_ENTITIES.register("final_boss_spawner", () ->
		BlockEntityType.Builder.of(FinalBossSpawnerBlockEntity::new, TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HydraSpawnerBlockEntity>> HYDRA_SPAWNER = BLOCK_ENTITIES.register("hydra_boss_spawner", () ->
		BlockEntityType.Builder.of(HydraSpawnerBlockEntity::new, TFBlocks.HYDRA_BOSS_SPAWNER.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KnightPhantomSpawnerBlockEntity>> KNIGHT_PHANTOM_SPAWNER = BLOCK_ENTITIES.register("knight_phantom_spawner", () ->
		BlockEntityType.Builder.of(KnightPhantomSpawnerBlockEntity::new, TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LichSpawnerBlockEntity>> LICH_SPAWNER = BLOCK_ENTITIES.register("lich_spawner", () ->
		BlockEntityType.Builder.of(LichSpawnerBlockEntity::new, TFBlocks.LICH_BOSS_SPAWNER.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MinoshroomSpawnerBlockEntity>> MINOSHROOM_SPAWNER = BLOCK_ENTITIES.register("minoshroom_spawner", () ->
		BlockEntityType.Builder.of(MinoshroomSpawnerBlockEntity::new, TFBlocks.MINOSHROOM_BOSS_SPAWNER.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NagaSpawnerBlockEntity>> NAGA_SPAWNER = BLOCK_ENTITIES.register("naga_spawner", () ->
		BlockEntityType.Builder.of(NagaSpawnerBlockEntity::new, TFBlocks.NAGA_BOSS_SPAWNER.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SnowQueenSpawnerBlockEntity>> SNOW_QUEEN_SPAWNER = BLOCK_ENTITIES.register("snow_queen_spawner", () ->
		BlockEntityType.Builder.of(SnowQueenSpawnerBlockEntity::new, TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<UrGhastSpawnerBlockEntity>> UR_GHAST_SPAWNER = BLOCK_ENTITIES.register("tower_boss_spawner", () ->
		BlockEntityType.Builder.of(UrGhastSpawnerBlockEntity::new, TFBlocks.UR_GHAST_BOSS_SPAWNER.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CicadaBlockEntity>> CICADA = BLOCK_ENTITIES.register("cicada", () ->
		BlockEntityType.Builder.of(CicadaBlockEntity::new, TFBlocks.CICADA.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FireflyBlockEntity>> FIREFLY = BLOCK_ENTITIES.register("firefly", () ->
		BlockEntityType.Builder.of(FireflyBlockEntity::new, TFBlocks.FIREFLY.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MoonwormBlockEntity>> MOONWORM = BLOCK_ENTITIES.register("moonworm", () ->
		BlockEntityType.Builder.of(MoonwormBlockEntity::new, TFBlocks.MOONWORM.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KeepsakeCasketBlockEntity>> SKULL_CHEST = BLOCK_ENTITIES.register("skull_chest", () ->
		BlockEntityType.Builder.of(KeepsakeCasketBlockEntity::createSkullChestBE, TFBlocks.SKULL_CHEST.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KeepsakeCasketBlockEntity>> KEEPSAKE_CASKET = BLOCK_ENTITIES.register("keepsake_casket", () ->
		BlockEntityType.Builder.of(KeepsakeCasketBlockEntity::createKeepsakeCasketBE, TFBlocks.KEEPSAKE_CASKET.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BrazierBlockEntity>> BRAZIER = BLOCK_ENTITIES.register("brazier", () ->
		BlockEntityType.Builder.of(BrazierBlockEntity::new, TFBlocks.BRAZIER.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TFChestBlockEntity>> TF_CHEST = BLOCK_ENTITIES.register("tf_chest", () ->
		BlockEntityType.Builder.of(TFChestBlockEntity::new,
			TFBlocks.TWILIGHT_OAK_CHEST.get(), TFBlocks.CANOPY_CHEST.get(), TFBlocks.MANGROVE_CHEST.get(),
			TFBlocks.DARK_CHEST.get(), TFBlocks.TIME_CHEST.get(), TFBlocks.TRANSFORMATION_CHEST.get(),
			TFBlocks.MINING_CHEST.get(), TFBlocks.SORTING_CHEST.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TFTrappedChestBlockEntity>> TF_TRAPPED_CHEST = BLOCK_ENTITIES.register("tf_trapped_chest", () ->
		BlockEntityType.Builder.of(TFTrappedChestBlockEntity::new,
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(), TFBlocks.CANOPY_TRAPPED_CHEST.get(), TFBlocks.MANGROVE_TRAPPED_CHEST.get(),
			TFBlocks.DARK_TRAPPED_CHEST.get(), TFBlocks.TIME_TRAPPED_CHEST.get(), TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(),
			TFBlocks.MINING_TRAPPED_CHEST.get(), TFBlocks.SORTING_TRAPPED_CHEST.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SkullCandleBlockEntity>> SKULL_CANDLE = BLOCK_ENTITIES.register("skull_candle", () ->
		BlockEntityType.Builder.of(SkullCandleBlockEntity::new,
			TFBlocks.ZOMBIE_SKULL_CANDLE.get(), TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get(),
			TFBlocks.SKELETON_SKULL_CANDLE.get(), TFBlocks.SKELETON_WALL_SKULL_CANDLE.get(),
			TFBlocks.WITHER_SKELE_SKULL_CANDLE.get(), TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.get(),
			TFBlocks.CREEPER_SKULL_CANDLE.get(), TFBlocks.CREEPER_WALL_SKULL_CANDLE.get(),
			TFBlocks.PLAYER_SKULL_CANDLE.get(), TFBlocks.PLAYER_WALL_SKULL_CANDLE.get(),
			TFBlocks.PIGLIN_SKULL_CANDLE.get(), TFBlocks.PIGLIN_WALL_SKULL_CANDLE.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ChiseledCanopyShelfBlockEntity>> CHISELED_CANOPY_BOOKSHELF = BLOCK_ENTITIES.register("chiseled_canopy_bookshelf", () ->
		BlockEntityType.Builder.of(ChiseledCanopyShelfBlockEntity::new, TFBlocks.CHISELED_CANOPY_BOOKSHELF.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GrowingBeanstalkBlockEntity>> BEANSTALK_GROWER = BLOCK_ENTITIES.register("beanstalk_grower", () ->
		BlockEntityType.Builder.of(GrowingBeanstalkBlockEntity::new, TFBlocks.BEANSTALK_GROWER.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RedThreadBlockEntity>> RED_THREAD = BLOCK_ENTITIES.register("red_thread", () ->
		BlockEntityType.Builder.of(RedThreadBlockEntity::new, TFBlocks.RED_THREAD.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CandelabraBlockEntity>> CANDELABRA = BLOCK_ENTITIES.register("candelabra", () ->
		BlockEntityType.Builder.of(CandelabraBlockEntity::new, TFBlocks.CANDELABRA.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<JarBlockEntity>> JAR = BLOCK_ENTITIES.register("jar", () ->
		BlockEntityType.Builder.of(JarBlockEntity::new, TFBlocks.FIREFLY_JAR.get(), TFBlocks.CICADA_JAR.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MasonJarBlockEntity>> MASON_JAR = BLOCK_ENTITIES.register("mason_jar", () ->
		BlockEntityType.Builder.of(MasonJarBlockEntity::new, TFBlocks.MASON_JAR.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SinisterSpawnerBlockEntity>> SINISTER_SPAWNER = BLOCK_ENTITIES.register("sinister_spawner", () ->
		BlockEntityType.Builder.of(SinisterSpawnerBlockEntity::new, TFBlocks.SINISTER_SPAWNER.get()).build(null));
}
