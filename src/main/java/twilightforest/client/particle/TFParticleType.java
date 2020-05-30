package twilightforest.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TFParticleType {

	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = new DeferredRegister<>(ForgeRegistries.PARTICLE_TYPES, TwilightForestMod.ID);

	//TODO: the boolean value detemines if a Particle should always show. Find out which ones should be true
	public static final RegistryObject<BasicParticleType> LARGE_FLAME = PARTICLE_TYPES.register("large_flame", () -> new BasicParticleType(false));
	public static final RegistryObject<BasicParticleType> LEAF_RUNE = PARTICLE_TYPES.register("leaf_rune", () -> new BasicParticleType(false));
	public static final RegistryObject<BasicParticleType> BOSS_TEAR = PARTICLE_TYPES.register("boss_tear", () -> new BasicParticleType(false));
	public static final RegistryObject<BasicParticleType> GHAST_TRAP = PARTICLE_TYPES.register("ghast_trap", () -> new BasicParticleType(false));
	public static final RegistryObject<BasicParticleType> PROTECTION = PARTICLE_TYPES.register("protection", () -> new BasicParticleType(false));
	public static final RegistryObject<BasicParticleType> SNOW = PARTICLE_TYPES.register("snow", () -> new BasicParticleType(false));
	public static final RegistryObject<BasicParticleType> SNOW_WARNING = PARTICLE_TYPES.register("snow_warning", () -> new BasicParticleType(false));
	public static final RegistryObject<BasicParticleType> SNOW_GUARDIAN = PARTICLE_TYPES.register("snow_guardian", () -> new BasicParticleType(false));
	public static final RegistryObject<BasicParticleType> ICE_BEAM = PARTICLE_TYPES.register("ice_beam", () -> new BasicParticleType(false));
	public static final RegistryObject<BasicParticleType> ANNIHILATE = PARTICLE_TYPES.register("annihilate", () -> new BasicParticleType(false));
	public static final RegistryObject<BasicParticleType> HUGE_SMOKE = PARTICLE_TYPES.register("huge_smoke", () -> new BasicParticleType(false));
	public static final RegistryObject<BasicParticleType> FIREFLY = PARTICLE_TYPES.register("firefly", () -> new BasicParticleType(false));
	public static final RegistryObject<BasicParticleType> FALLEN_LEAF = PARTICLE_TYPES.register("fallen_leaf", () -> new BasicParticleType(false));

	@SubscribeEvent
	public static void registerFactories(ParticleFactoryRegisterEvent event) {
		ParticleManager particles = Minecraft.getInstance().particles;

		particles.registerFactory(TFParticleType.LARGE_FLAME.get(), ParticleLargeFlame.Factory::new);
		particles.registerFactory(TFParticleType.LEAF_RUNE.get(), ParticleLeafRune.Factory::new);
		particles.registerFactory(TFParticleType.BOSS_TEAR.get(), new ParticleGhastTear.Factory());
		particles.registerFactory(TFParticleType.GHAST_TRAP.get(), ParticleGhastTrap.Factory::new);
		particles.registerFactory(TFParticleType.PROTECTION.get(), ParticleProtection.Factory::new); //probably not a good idea, but worth a shot
		particles.registerFactory(TFParticleType.SNOW.get(), ParticleSnow.Factory::new);
		particles.registerFactory(TFParticleType.SNOW_GUARDIAN.get(), ParticleSnowGuardian.Factory::new);
		particles.registerFactory(TFParticleType.SNOW_WARNING.get(), ParticleSnowWarning.Factory::new);
		particles.registerFactory(TFParticleType.ICE_BEAM.get(), ParticleIceBeam.Factory::new);
		particles.registerFactory(TFParticleType.ANNIHILATE.get(), ParticleAnnihilate.Factory::new);
		particles.registerFactory(TFParticleType.HUGE_SMOKE.get(), ParticleSmokeScale.Factory::new);
		particles.registerFactory(TFParticleType.FIREFLY.get(), ParticleFirefly.Factory::new);
		particles.registerFactory(TFParticleType.FALLEN_LEAF.get(), ParticleLeaf.Factory::new);
	}
}
