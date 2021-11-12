package twilightforest.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.data.LeafParticleData;
import twilightforest.client.particle.data.PinnedFireflyData;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TFParticleType {

	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, TwilightForestMod.ID);

	public static final RegistryObject<SimpleParticleType> LARGE_FLAME = PARTICLE_TYPES.register("large_flame", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> LEAF_RUNE = PARTICLE_TYPES.register("leaf_rune", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> BOSS_TEAR = PARTICLE_TYPES.register("boss_tear", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> GHAST_TRAP = PARTICLE_TYPES.register("ghast_trap", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> PROTECTION = PARTICLE_TYPES.register("protection", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> SNOW = PARTICLE_TYPES.register("snow", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> SNOW_WARNING = PARTICLE_TYPES.register("snow_warning", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> SNOW_GUARDIAN = PARTICLE_TYPES.register("snow_guardian", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> ICE_BEAM = PARTICLE_TYPES.register("ice_beam", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> ANNIHILATE = PARTICLE_TYPES.register("annihilate", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> HUGE_SMOKE = PARTICLE_TYPES.register("huge_smoke", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> FIREFLY = PARTICLE_TYPES.register("firefly", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> WANDERING_FIREFLY = PARTICLE_TYPES.register("wandering_firefly", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> JAR_WANDERING_FIREFLY = PARTICLE_TYPES.register("jar_wandering_firefly", () -> new SimpleParticleType(false));
	public static final RegistryObject<ParticleType<PinnedFireflyData>> FIREFLY_PINNED = PARTICLE_TYPES.register("firefly_pinned", () -> new ParticleType<PinnedFireflyData>(false, new PinnedFireflyData.Deserializer()) {
		@Override
		public Codec<PinnedFireflyData> codec() {
			return PinnedFireflyData.codecFirefly();
		}
	});
	public static final RegistryObject<ParticleType<LeafParticleData>> FALLEN_LEAF = PARTICLE_TYPES.register("fallen_leaf", () -> new ParticleType<LeafParticleData>(false, new LeafParticleData.Deserializer()) {
		@Override
		public Codec<LeafParticleData> codec() {
			return LeafParticleData.codecLeaf();
		}
	});
	public static final RegistryObject<SimpleParticleType> OMINOUS_FLAME = PARTICLE_TYPES.register("ominous_flame", () -> new SimpleParticleType(false));

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerFactories(ParticleFactoryRegisterEvent event) {
		ParticleEngine particles = Minecraft.getInstance().particleEngine;

		particles.register(TFParticleType.LARGE_FLAME.get(), LargeFlameParticle.Factory::new);
		particles.register(TFParticleType.LEAF_RUNE.get(), LeafRuneParticle.Factory::new);
		particles.register(TFParticleType.BOSS_TEAR.get(), new GhastTearParticle.Factory());
		particles.register(TFParticleType.GHAST_TRAP.get(), GhastTrapParticle.Factory::new);
		particles.register(TFParticleType.PROTECTION.get(), ProtectionParticle.Factory::new); //probably not a good idea, but worth a shot
		particles.register(TFParticleType.SNOW.get(), SnowParticle.Factory::new);
		particles.register(TFParticleType.SNOW_GUARDIAN.get(), SnowGuardianParticle.Factory::new);
		particles.register(TFParticleType.SNOW_WARNING.get(), SnowWarningParticle.Factory::new);
		particles.register(TFParticleType.ICE_BEAM.get(), IceBeamParticle.Factory::new);
		particles.register(TFParticleType.ANNIHILATE.get(), AnnihilateParticle.Factory::new);
		particles.register(TFParticleType.HUGE_SMOKE.get(), SmokeScaleParticle.Factory::new);
		particles.register(TFParticleType.FIREFLY.get(), FireflyParticle.Factory::new);
		particles.register(TFParticleType.WANDERING_FIREFLY.get(), WanderingFireflyParticle.Factory::new);
		particles.register(TFParticleType.JAR_WANDERING_FIREFLY.get(), WanderingFireflyParticle.FromJarFactory::new);
		particles.register(TFParticleType.FIREFLY_PINNED.get(), PinnedFireflyParticle.Factory::new);
		particles.register(TFParticleType.FALLEN_LEAF.get(), LeafParticle.Factory::new);
		particles.register(TFParticleType.OMINOUS_FLAME.get(), FlameParticle.SmallFlameProvider::new);
	}
}
