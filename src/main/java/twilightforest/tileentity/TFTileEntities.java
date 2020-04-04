package twilightforest.tileentity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.renderer.tileentity.TileEntityTFCicadaRenderer;
import twilightforest.client.renderer.tileentity.TileEntityTFFireflyRenderer;
import twilightforest.client.renderer.tileentity.TileEntityTFMoonwormRenderer;
import twilightforest.client.renderer.tileentity.TileEntityTFTrophyRenderer;
import twilightforest.tileentity.critters.*;
import twilightforest.tileentity.spawner.*;

public class TFTileEntities {

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, TwilightForestMod.ID);

	public static final RegistryObject<TileEntityType<TileEntityTFAntibuilder>> ANTIBUILDER               = TILE_ENTITIES.register("antibuilder", () ->
			TileEntityType.Builder.create(TileEntityTFAntibuilder::new, TFBlocks.antibuilder.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFCinderFurnace>> CINDER_FURNACE          = TILE_ENTITIES.register("cinder_furnace", () ->
			TileEntityType.Builder.create(TileEntityTFCinderFurnace::new, TFBlocks.cinder_furnace.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFCReactorActive>> CARMINITE_REACTOR      = TILE_ENTITIES.register("carminite_reactor", () ->
			TileEntityType.Builder.create(TileEntityTFCReactorActive::new, TFBlocks.carminite_reactor.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFFlameJet>> FLAME_JET                    = TILE_ENTITIES.register("flame_jet", () ->
			TileEntityType.Builder.create(TileEntityTFFlameJet::new, TFBlocks.fire_jet.get(), TFBlocks.encased_fire_jet.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFGhastTrapActive>> GHAST_TRAP_ACTIVE     = TILE_ENTITIES.register("ghast_trap_active", () ->
			TileEntityType.Builder.create(TileEntityTFGhastTrapActive::new, TFBlocks.ghast_trap.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFGhastTrapInactive>> GHAST_TRAP_INACTIVE = TILE_ENTITIES.register("ghast_trap_inactive", () ->
			TileEntityType.Builder.create(TileEntityTFGhastTrapInactive::new, TFBlocks.ghast_trap.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFPoppingJet>> POPPING_JET                = TILE_ENTITIES.register("popping_jet", () ->
			TileEntityType.Builder.create(TileEntityTFPoppingJet::new, TFBlocks.fire_jet.get(), TFBlocks.encased_fire_jet.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFSmoker>> SMOKER                           = TILE_ENTITIES.register("smoker", () ->
			TileEntityType.Builder.create(TileEntityTFSmoker::new, TFBlocks.smoker.get(), TFBlocks.encased_smoker.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFTowerBuilder>> TOWER_BUILDER            = TILE_ENTITIES.register("tower_builder", () ->
			TileEntityType.Builder.create(TileEntityTFTowerBuilder::new, TFBlocks.carminite_builder.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFTrophy>> TROPHY                         = TILE_ENTITIES.register("trophy", () ->
			TileEntityType.Builder.create(TileEntityTFTrophy::new, TFBlocks.naga_trophy.get(), TFBlocks.lich_trophy.get(), TFBlocks.minoshroom_trophy.get(), TFBlocks.hydra_trophy.get(), TFBlocks.knight_phantom_trophy.get(), TFBlocks.ur_ghast_trophy.get(), TFBlocks.snow_queen_trophy.get(), TFBlocks.quest_ram_trophy.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityTFAlphaYetiSpawner>> ALPHA_YETI_SPAWNER     = TILE_ENTITIES.register("alpha_yeti_spawner", () ->
			TileEntityType.Builder.create(TileEntityTFAlphaYetiSpawner::new, TFBlocks.boss_spawner.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFFinalBossSpawner>> FINAL_BOSS_SPAWNER     = TILE_ENTITIES.register("final_boss_spawner", () ->
			TileEntityType.Builder.create(TileEntityTFFinalBossSpawner::new, TFBlocks.boss_spawner.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFHydraSpawner>> HYDRA_SPAWNER          = TILE_ENTITIES.register("hydra_boss_spawner", () ->
			TileEntityType.Builder.create(TileEntityTFHydraSpawner::new, TFBlocks.boss_spawner.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFKnightPhantomsSpawner>> KNIGHT_PHANTOM_SPAWNER = TILE_ENTITIES.register("knight_phantom_spawner", () ->
			TileEntityType.Builder.create(TileEntityTFKnightPhantomsSpawner::new, TFBlocks.boss_spawner.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFLichSpawner>> LICH_SPAWNER           = TILE_ENTITIES.register("lich_spawner", () ->
			TileEntityType.Builder.create(TileEntityTFLichSpawner::new, TFBlocks.boss_spawner.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFMinoshroomSpawner>> MINOSHROOM_SPAWNER     = TILE_ENTITIES.register("minoshroom_spawner", () ->
			TileEntityType.Builder.create(TileEntityTFMinoshroomSpawner::new, TFBlocks.boss_spawner.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFNagaSpawner>> NAGA_SPAWNER           = TILE_ENTITIES.register("naga_spawner", () ->
			TileEntityType.Builder.create(TileEntityTFNagaSpawner::new, TFBlocks.boss_spawner.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFSnowQueenSpawner>> SNOW_QUEEN_SPAWNER     = TILE_ENTITIES.register("snow_queen_spawner", () ->
			TileEntityType.Builder.create(TileEntityTFSnowQueenSpawner::new, TFBlocks.boss_spawner.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFTowerBossSpawner>> TOWER_BOSS_SPAWNER     = TILE_ENTITIES.register("tower_boss_spawner", () ->
			TileEntityType.Builder.create(TileEntityTFTowerBossSpawner::new, TFBlocks.boss_spawner.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityTFCicada>> CICADA     = TILE_ENTITIES.register("cicada", () ->
			TileEntityType.Builder.create(FMLEnvironment.dist.isClient() ? TileEntityTFCicadaTicking::new : TileEntityTFCicada::new, TFBlocks.cicada.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFFirefly>> FIREFLY   = TILE_ENTITIES.register("firefly", () ->
			TileEntityType.Builder.create(FMLEnvironment.dist.isClient() ? TileEntityTFFireflyTicking::new : TileEntityTFFirefly::new, TFBlocks.firefly.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFMoonworm>> MOONWORM = TILE_ENTITIES.register("moonworm", () ->
			TileEntityType.Builder.create(FMLEnvironment.dist.isClient() ? TileEntityTFMoonwormTicking::new : TileEntityTFMoonworm::new, TFBlocks.moonworm.get()).build(null));

	@OnlyIn(Dist.CLIENT)
	public static void registerTileEntityRenders() {
		// tile entities
		ClientRegistry.bindTileEntityRenderer(FIREFLY.get(), TileEntityTFFireflyRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CICADA.get(), TileEntityTFCicadaRenderer::new);
//		ClientRegistry.bindTileEntityRenderer(TileEntityTFNagaSpawner.class, new TileEntityMobSpawnerRenderer());
//		ClientRegistry.bindTileEntityRenderer(TileEntityTFLichSpawner.class, new TileEntityMobSpawnerRenderer());
//		ClientRegistry.bindTileEntityRenderer(TileEntityTFHydraSpawner.class, new TileEntityMobSpawnerRenderer());
		ClientRegistry.bindTileEntityRenderer(MOONWORM.get(), TileEntityTFMoonwormRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TROPHY.get(), TileEntityTFTrophyRenderer::new);
	}
}
