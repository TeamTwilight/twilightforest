package twilightforest.init;

import com.mojang.serialization.Codec;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.*;
import twilightforest.client.particle.data.LeafParticleData;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TFParticleType {

	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LARGE_FLAME = PARTICLE_TYPES.register("large_flame", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LEAF_RUNE = PARTICLE_TYPES.register("leaf_rune", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BOSS_TEAR = PARTICLE_TYPES.register("boss_tear", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GHAST_TRAP = PARTICLE_TYPES.register("ghast_trap", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PROTECTION = PARTICLE_TYPES.register("protection", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SNOW = PARTICLE_TYPES.register("snow", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SNOW_WARNING = PARTICLE_TYPES.register("snow_warning", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> EXTENDED_SNOW_WARNING = PARTICLE_TYPES.register("extended_snow_warning", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SNOW_GUARDIAN = PARTICLE_TYPES.register("snow_guardian", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ICE_BEAM = PARTICLE_TYPES.register("ice_beam", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ANNIHILATE = PARTICLE_TYPES.register("annihilate", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HUGE_SMOKE = PARTICLE_TYPES.register("huge_smoke", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FIREFLY = PARTICLE_TYPES.register("firefly", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WANDERING_FIREFLY = PARTICLE_TYPES.register("wandering_firefly", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PARTICLE_SPAWNER_FIREFLY = PARTICLE_TYPES.register("particle_spawner_firefly", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, ParticleType<LeafParticleData>> FALLEN_LEAF = PARTICLE_TYPES.register("fallen_leaf", () -> new ParticleType<>(false, new LeafParticleData.Deserializer()) {
		@Override
		public Codec<LeafParticleData> codec() {
			return LeafParticleData.codecLeaf();
		}
	});
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DIM_FLAME = PARTICLE_TYPES.register("dim_flame", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> OMINOUS_FLAME = PARTICLE_TYPES.register("ominous_flame", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SORTING_PARTICLE = PARTICLE_TYPES.register("sorting_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TRANSFORMATION_PARTICLE = PARTICLE_TYPES.register("transformation_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LOG_CORE_PARTICLE = PARTICLE_TYPES.register("log_core_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CLOUD_PUFF = PARTICLE_TYPES.register("cloud_puff", () -> new SimpleParticleType(false));

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerFactories(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(TFParticleType.LARGE_FLAME.get(), LargeFlameParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.LEAF_RUNE.get(), LeafRuneParticle.Factory::new);
		event.registerSpecial(TFParticleType.BOSS_TEAR.get(), new GhastTearParticle.Factory());
		event.registerSpriteSet(TFParticleType.GHAST_TRAP.get(), GhastTrapParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.PROTECTION.get(), ProtectionParticle.Factory::new); //probably not a good idea, but worth a shot
		event.registerSpriteSet(TFParticleType.SNOW.get(), SnowParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.SNOW_GUARDIAN.get(), SnowGuardianParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.SNOW_WARNING.get(), SnowWarningParticle.SimpleFactory::new);
		event.registerSpriteSet(TFParticleType.EXTENDED_SNOW_WARNING.get(), SnowWarningParticle.ExtendedFactory::new);
		event.registerSpriteSet(TFParticleType.ICE_BEAM.get(), IceBeamParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.ANNIHILATE.get(), AnnihilateParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.HUGE_SMOKE.get(), SmokeScaleParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.FIREFLY.get(), FireflyParticle.StationaryProvider::new);
		event.registerSpriteSet(TFParticleType.WANDERING_FIREFLY.get(), FireflyParticle.WanderingProvider::new);
		event.registerSpriteSet(TFParticleType.PARTICLE_SPAWNER_FIREFLY.get(), FireflyParticle.ParticleSpawnerProvider::new);
		event.registerSpriteSet(TFParticleType.FALLEN_LEAF.get(), LeafParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.DIM_FLAME.get(), FlameParticle.SmallFlameProvider::new);
		event.registerSpriteSet(TFParticleType.OMINOUS_FLAME.get(), FlameParticle.SmallFlameProvider::new);
		event.registerSpriteSet(TFParticleType.SORTING_PARTICLE.get(), SortingParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.TRANSFORMATION_PARTICLE.get(), TransformationParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.LOG_CORE_PARTICLE.get(), LogCoreParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.CLOUD_PUFF.get(), CloudPuffParticle.Factory::new);
	}
}
