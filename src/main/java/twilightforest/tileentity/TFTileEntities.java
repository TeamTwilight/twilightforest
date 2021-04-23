package twilightforest.tileentity;

import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.renderer.tileentity.TileEntityTFCicadaRenderer;
import twilightforest.client.renderer.tileentity.TileEntityTFFireflyRenderer;
import twilightforest.client.renderer.tileentity.TileEntityTFMoonwormRenderer;
import twilightforest.client.renderer.tileentity.TileEntityTFTrophyRenderer;
import twilightforest.tileentity.spawner.*;

public class TFTileEntities {

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, TwilightForestMod.ID);

	public static final RegistryObject<TileEntityType<TileEntityTFAntibuilder>> ANTIBUILDER               = TILE_ENTITIES.register("antibuilder", () ->
			TileEntityType.Builder.create(TileEntityTFAntibuilder::new, TFBlocks.antibuilder.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFCinderFurnace>> CINDER_FURNACE          = TILE_ENTITIES.register("cinder_furnace", () ->
			TileEntityType.Builder.create(TileEntityTFCinderFurnace::new, TFBlocks.cinder_furnace.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFCReactorActive>> CARMINITE_REACTOR      = TILE_ENTITIES.register("carminite_reactor", () ->
			TileEntityType.Builder.create(TileEntityTFCReactorActive::new, TFBlocks.carminite_reactor.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFFlameJet>> FLAME_JET                    = TILE_ENTITIES.register("flame_jet", () ->
			TileEntityType.Builder.create(TileEntityTFFlameJet::new, TFBlocks.fire_jet.get(), TFBlocks.encased_fire_jet.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFGhastTrapActive>> GHAST_TRAP = TILE_ENTITIES.register("ghast_trap", () ->
			TileEntityType.Builder.create(TileEntityTFGhastTrapActive::new, TFBlocks.ghast_trap.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFSmoker>> SMOKER                           = TILE_ENTITIES.register("smoker", () ->
			TileEntityType.Builder.create(TileEntityTFSmoker::new, TFBlocks.smoker.get(), TFBlocks.encased_smoker.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFTowerBuilder>> TOWER_BUILDER            = TILE_ENTITIES.register("tower_builder", () ->
			TileEntityType.Builder.create(TileEntityTFTowerBuilder::new, TFBlocks.carminite_builder.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFTrophy>> TROPHY                         = TILE_ENTITIES.register("trophy", () ->
			TileEntityType.Builder.create(TileEntityTFTrophy::new, TFBlocks.naga_trophy.get(), TFBlocks.lich_trophy.get(), TFBlocks.minoshroom_trophy.get(), 
					TFBlocks.hydra_trophy.get(), TFBlocks.knight_phantom_trophy.get(), TFBlocks.ur_ghast_trophy.get(), TFBlocks.snow_queen_trophy.get(), 
					TFBlocks.quest_ram_trophy.get(), TFBlocks.naga_wall_trophy.get(), TFBlocks.lich_wall_trophy.get(), TFBlocks.minoshroom_wall_trophy.get(), 
					TFBlocks.hydra_wall_trophy.get(), TFBlocks.knight_phantom_wall_trophy.get(), TFBlocks.ur_ghast_wall_trophy.get(), TFBlocks.snow_queen_wall_trophy.get(), 
					TFBlocks.quest_ram_wall_trophy.get()).build(null));
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

	public static final RegistryObject<TileEntityType<TileEntityTFCicadaTicking>> CICADA     = TILE_ENTITIES.register("cicada", () ->
			TileEntityType.Builder.create(TileEntityTFCicadaTicking::new, TFBlocks.cicada.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFFireflyTicking>> FIREFLY   = TILE_ENTITIES.register("firefly", () ->
			TileEntityType.Builder.create(TileEntityTFFireflyTicking::new, TFBlocks.firefly.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEntityTFMoonwormTicking>> MOONWORM = TILE_ENTITIES.register("moonworm", () ->
			TileEntityType.Builder.create(TileEntityTFMoonwormTicking::new, TFBlocks.moonworm.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityKeepsakeCasket>> KEEPSAKE_CASKET          = TILE_ENTITIES.register("keepsake_casket", () ->
			TileEntityType.Builder.create(TileEntityKeepsakeCasket::new, TFBlocks.keepsake_casket.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityTFSign>> TF_SIGN = TILE_ENTITIES.register("tf_sign", () ->
			TileEntityType.Builder.create(TileEntityTFSign::new,
					TFBlocks.twilight_oak_sign.get(), TFBlocks.twilight_wall_sign.get(),
					TFBlocks.canopy_sign.get(), TFBlocks.canopy_wall_sign.get(),
					TFBlocks.mangrove_sign.get(), TFBlocks.mangrove_wall_sign.get(),
					TFBlocks.darkwood_sign.get(), TFBlocks.darkwood_wall_sign.get(),
					TFBlocks.time_sign.get(), TFBlocks.time_wall_sign.get(),
					TFBlocks.trans_sign.get(), TFBlocks.trans_wall_sign.get(),
					TFBlocks.mine_sign.get(), TFBlocks.mine_wall_sign.get(),
					TFBlocks.sort_sign.get(), TFBlocks.sort_wall_sign.get()).build(null));

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
		ClientRegistry.bindTileEntityRenderer(TF_SIGN.get(), SignTileEntityRenderer::new);
	}
}
