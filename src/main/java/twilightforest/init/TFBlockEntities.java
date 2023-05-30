package twilightforest.init;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.*;
import twilightforest.init.TFBlocks;
import twilightforest.block.entity.spawner.*;
import twilightforest.client.renderer.tileentity.*;

public class TFBlockEntities {

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TwilightForestMod.ID);

	public static final RegistryObject<BlockEntityType<AntibuilderBlockEntity>> ANTIBUILDER = BLOCK_ENTITIES.register("antibuilder", () ->
			BlockEntityType.Builder.of(AntibuilderBlockEntity::new, TFBlocks.ANTIBUILDER.get()).build(null));
	public static final RegistryObject<BlockEntityType<CinderFurnaceBlockEntity>> CINDER_FURNACE = BLOCK_ENTITIES.register("cinder_furnace", () ->
			BlockEntityType.Builder.of(CinderFurnaceBlockEntity::new, TFBlocks.CINDER_FURNACE.get()).build(null));
	public static final RegistryObject<BlockEntityType<CarminiteReactorBlockEntity>> CARMINITE_REACTOR = BLOCK_ENTITIES.register("carminite_reactor", () ->
			BlockEntityType.Builder.of(CarminiteReactorBlockEntity::new, TFBlocks.CARMINITE_REACTOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<FireJetBlockEntity>> FLAME_JET = BLOCK_ENTITIES.register("flame_jet", () ->
			BlockEntityType.Builder.of(FireJetBlockEntity::new, TFBlocks.FIRE_JET.get(), TFBlocks.ENCASED_FIRE_JET.get()).build(null));
	public static final RegistryObject<BlockEntityType<GhastTrapBlockEntity>> GHAST_TRAP = BLOCK_ENTITIES.register("ghast_trap", () ->
			BlockEntityType.Builder.of(GhastTrapBlockEntity::new, TFBlocks.GHAST_TRAP.get()).build(null));
	public static final RegistryObject<BlockEntityType<TFSmokerBlockEntity>> SMOKER = BLOCK_ENTITIES.register("smoker", () ->
			BlockEntityType.Builder.of(TFSmokerBlockEntity::new, TFBlocks.SMOKER.get(), TFBlocks.ENCASED_SMOKER.get()).build(null));
	public static final RegistryObject<BlockEntityType<CarminiteBuilderBlockEntity>> TOWER_BUILDER = BLOCK_ENTITIES.register("tower_builder", () ->
			BlockEntityType.Builder.of(CarminiteBuilderBlockEntity::new, TFBlocks.CARMINITE_BUILDER.get()).build(null));
	public static final RegistryObject<BlockEntityType<TrophyBlockEntity>> TROPHY = BLOCK_ENTITIES.register("trophy", () ->
			BlockEntityType.Builder.of(TrophyBlockEntity::new, TFBlocks.NAGA_TROPHY.get(), TFBlocks.LICH_TROPHY.get(), TFBlocks.MINOSHROOM_TROPHY.get(),
					TFBlocks.HYDRA_TROPHY.get(), TFBlocks.KNIGHT_PHANTOM_TROPHY.get(), TFBlocks.UR_GHAST_TROPHY.get(), TFBlocks.ALPHA_YETI_TROPHY.get(),
					TFBlocks.SNOW_QUEEN_TROPHY.get(), TFBlocks.QUEST_RAM_TROPHY.get(), TFBlocks.NAGA_WALL_TROPHY.get(), TFBlocks.LICH_WALL_TROPHY.get(),
					TFBlocks.MINOSHROOM_WALL_TROPHY.get(), TFBlocks.HYDRA_WALL_TROPHY.get(), TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.get(), TFBlocks.UR_GHAST_WALL_TROPHY.get(),
					TFBlocks.ALPHA_YETI_WALL_TROPHY.get(), TFBlocks.SNOW_QUEEN_WALL_TROPHY.get(), TFBlocks.QUEST_RAM_WALL_TROPHY.get()).build(null));
	public static final RegistryObject<BlockEntityType<AlphaYetiSpawnerBlockEntity>> ALPHA_YETI_SPAWNER = BLOCK_ENTITIES.register("alpha_yeti_spawner", () ->
			BlockEntityType.Builder.of(AlphaYetiSpawnerBlockEntity::new, TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get()).build(null));
	public static final RegistryObject<BlockEntityType<FinalBossSpawnerBlockEntity>> FINAL_BOSS_SPAWNER = BLOCK_ENTITIES.register("final_boss_spawner", () ->
			BlockEntityType.Builder.of(FinalBossSpawnerBlockEntity::new, TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get()).build(null));
	public static final RegistryObject<BlockEntityType<HydraSpawnerBlockEntity>> HYDRA_SPAWNER = BLOCK_ENTITIES.register("hydra_boss_spawner", () ->
			BlockEntityType.Builder.of(HydraSpawnerBlockEntity::new, TFBlocks.HYDRA_BOSS_SPAWNER.get()).build(null));
	public static final RegistryObject<BlockEntityType<KnightPhantomSpawnerBlockEntity>> KNIGHT_PHANTOM_SPAWNER = BLOCK_ENTITIES.register("knight_phantom_spawner", () ->
			BlockEntityType.Builder.of(KnightPhantomSpawnerBlockEntity::new, TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get()).build(null));
	public static final RegistryObject<BlockEntityType<LichSpawnerBlockEntity>> LICH_SPAWNER = BLOCK_ENTITIES.register("lich_spawner", () ->
			BlockEntityType.Builder.of(LichSpawnerBlockEntity::new, TFBlocks.LICH_BOSS_SPAWNER.get()).build(null));
	public static final RegistryObject<BlockEntityType<MinoshroomSpawnerBlockEntity>> MINOSHROOM_SPAWNER = BLOCK_ENTITIES.register("minoshroom_spawner", () ->
			BlockEntityType.Builder.of(MinoshroomSpawnerBlockEntity::new, TFBlocks.MINOSHROOM_BOSS_SPAWNER.get()).build(null));
	public static final RegistryObject<BlockEntityType<NagaSpawnerBlockEntity>> NAGA_SPAWNER = BLOCK_ENTITIES.register("naga_spawner", () ->
			BlockEntityType.Builder.of(NagaSpawnerBlockEntity::new, TFBlocks.NAGA_BOSS_SPAWNER.get()).build(null));
	public static final RegistryObject<BlockEntityType<SnowQueenSpawnerBlockEntity>> SNOW_QUEEN_SPAWNER = BLOCK_ENTITIES.register("snow_queen_spawner", () ->
			BlockEntityType.Builder.of(SnowQueenSpawnerBlockEntity::new, TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get()).build(null));
	public static final RegistryObject<BlockEntityType<UrGhastSpawnerBlockEntity>> UR_GHAST_SPAWNER = BLOCK_ENTITIES.register("tower_boss_spawner", () ->
			BlockEntityType.Builder.of(UrGhastSpawnerBlockEntity::new, TFBlocks.UR_GHAST_BOSS_SPAWNER.get()).build(null));

	public static final RegistryObject<BlockEntityType<CicadaBlockEntity>> CICADA = BLOCK_ENTITIES.register("cicada", () ->
			BlockEntityType.Builder.of(CicadaBlockEntity::new, TFBlocks.CICADA.get()).build(null));
	public static final RegistryObject<BlockEntityType<FireflyBlockEntity>> FIREFLY = BLOCK_ENTITIES.register("firefly", () ->
			BlockEntityType.Builder.of(FireflyBlockEntity::new, TFBlocks.FIREFLY.get()).build(null));
	public static final RegistryObject<BlockEntityType<MoonwormBlockEntity>> MOONWORM = BLOCK_ENTITIES.register("moonworm", () ->
			BlockEntityType.Builder.of(MoonwormBlockEntity::new, TFBlocks.MOONWORM.get()).build(null));

	public static final RegistryObject<BlockEntityType<KeepsakeCasketBlockEntity>> KEEPSAKE_CASKET = BLOCK_ENTITIES.register("keepsake_casket", () ->
			BlockEntityType.Builder.of(KeepsakeCasketBlockEntity::new, TFBlocks.KEEPSAKE_CASKET.get()).build(null));

	public static final RegistryObject<BlockEntityType<TFSignBlockEntity>> TF_SIGN = BLOCK_ENTITIES.register("tf_sign", () ->
			BlockEntityType.Builder.of(TFSignBlockEntity::new,
					TFBlocks.TWILIGHT_OAK_SIGN.get(), TFBlocks.TWILIGHT_WALL_SIGN.get(),
					TFBlocks.CANOPY_SIGN.get(), TFBlocks.CANOPY_WALL_SIGN.get(),
					TFBlocks.MANGROVE_SIGN.get(), TFBlocks.MANGROVE_WALL_SIGN.get(),
					TFBlocks.DARK_SIGN.get(), TFBlocks.DARK_WALL_SIGN.get(),
					TFBlocks.TIME_SIGN.get(), TFBlocks.TIME_WALL_SIGN.get(),
					TFBlocks.TRANSFORMATION_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_SIGN.get(),
					TFBlocks.MINING_SIGN.get(), TFBlocks.MINING_WALL_SIGN.get(),
					TFBlocks.SORTING_SIGN.get(), TFBlocks.SORTING_WALL_SIGN.get()).build(null));

	public static final RegistryObject<BlockEntityType<TwilightChestEntity>> TF_CHEST = BLOCK_ENTITIES.register("tf_chest", () ->
			BlockEntityType.Builder.of(TwilightChestEntity::new,
					TFBlocks.TWILIGHT_OAK_CHEST.get(), TFBlocks.CANOPY_CHEST.get(), TFBlocks.MANGROVE_CHEST.get(),
					TFBlocks.DARK_CHEST.get(), TFBlocks.TIME_CHEST.get(), TFBlocks.TRANSFORMATION_CHEST.get(),
					TFBlocks.MINING_CHEST.get(), TFBlocks.SORTING_CHEST.get()).build(null));

	public static final RegistryObject<BlockEntityType<SkullCandleBlockEntity>> SKULL_CANDLE = BLOCK_ENTITIES.register("skull_candle", () ->
			BlockEntityType.Builder.of(SkullCandleBlockEntity::new,
					TFBlocks.ZOMBIE_SKULL_CANDLE.get(), TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get(),
					TFBlocks.SKELETON_SKULL_CANDLE.get(), TFBlocks.SKELETON_WALL_SKULL_CANDLE.get(),
					TFBlocks.WITHER_SKELE_SKULL_CANDLE.get(), TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.get(),
					TFBlocks.CREEPER_SKULL_CANDLE.get(), TFBlocks.CREEPER_WALL_SKULL_CANDLE.get(),
					TFBlocks.PLAYER_SKULL_CANDLE.get(), TFBlocks.PLAYER_WALL_SKULL_CANDLE.get()).build(null));

	public static final RegistryObject<BlockEntityType<TomeSpawnerBlockEntity>> TOME_SPAWNER = BLOCK_ENTITIES.register("tome_spawner", () ->
			BlockEntityType.Builder.of(TomeSpawnerBlockEntity::new, TFBlocks.DEATH_TOME_SPAWNER.get()).build(null));

	public static final RegistryObject<BlockEntityType<GrowingBeanstalkBlockEntity>> BEANSTALK_GROWER = BLOCK_ENTITIES.register("beanstalk_grower", () ->
			BlockEntityType.Builder.of(GrowingBeanstalkBlockEntity::new, TFBlocks.BEANSTALK_GROWER.get()).build(null));

	public static final RegistryObject<BlockEntityType<RedThreadBlockEntity>> RED_THREAD = BLOCK_ENTITIES.register("red_thread", () ->
			BlockEntityType.Builder.of(RedThreadBlockEntity::new, TFBlocks.RED_THREAD.get()).build(null));

	@OnlyIn(Dist.CLIENT)
	public static void registerTileEntityRenders() {
		// tile entities
		BlockEntityRenderers.register(FIREFLY.get(), FireflyTileEntityRenderer::new);
		BlockEntityRenderers.register(CICADA.get(), CicadaTileEntityRenderer::new);
		BlockEntityRenderers.register(MOONWORM.get(), MoonwormTileEntityRenderer::new);
		BlockEntityRenderers.register(TROPHY.get(), TrophyTileEntityRenderer::new);
		BlockEntityRenderers.register(TF_SIGN.get(), SignRenderer::new);
		BlockEntityRenderers.register(TF_CHEST.get(), TwilightChestRenderer::new);
		BlockEntityRenderers.register(KEEPSAKE_CASKET.get(), CasketTileEntityRenderer::new);
		BlockEntityRenderers.register(SKULL_CANDLE.get(), SkullCandleTileEntityRenderer::new);
		BlockEntityRenderers.register(RED_THREAD.get(), RedThreadRenderer::new);
	}
}
