package twilightforest.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.init.Items;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

public class TFParticleFactory {

	private static final Map<TFParticleType, IParticleFactory> factories = new EnumMap<>(TFParticleType.class);

	static {
		factories.put(TFParticleType.LARGE_FLAME, ParticleLargeFlame::new);
		factories.put(TFParticleType.LEAF_RUNE, ParticleLeafRune::new);
		factories.put(TFParticleType.BOSS_TEAR, (world, x, y, z, vx, vy, vz)
				-> new ParticleGhastTear(world, x, y, z, vx, vy, vz, Items.GHAST_TEAR)
		);
		factories.put(TFParticleType.GHAST_TRAP, ParticleGhastTrap::new);
		factories.put(TFParticleType.PROTECTION, ParticleProtection::new);
		factories.put(TFParticleType.SNOW, ParticleSnow::new);
		factories.put(TFParticleType.SNOW_GUARDIAN, (world, x, y, z, vx, vy, vz)
				-> new ParticleSnowGuardian(world, x, y, z, vx, vy, vz, 0.75F)
		);
		factories.put(TFParticleType.SNOW_WARNING, (world, x, y, z, vx, vy, vz)
				-> new ParticleSnowWarning(world, x, y, z, vx, vy, vz, 1F)
		);
		factories.put(TFParticleType.ICE_BEAM, (world, x, y, z, vx, vy, vz)
				-> new ParticleIceBeam(world, x, y, z, vx, vy, vz, 0.75F)
		);
		factories.put(TFParticleType.ANNIHILATE, (world, x, y, z, vx, vy, vz)
				-> new ParticleAnnihilate(world, x, y, z, vx, vy, vz, 0.75F)
		);
		factories.put(TFParticleType.HUGE_SMOKE, (world, x, y, z, vx, vy, vz)
				-> new ParticleSmokeScale(world, x, y, z, vx, vy, vz, 4.0F + world.rand.nextFloat())
		);
		factories.put(TFParticleType.FIREFLY, ParticleFirefly::new);
		factories.put(TFParticleType.FALLEN_LEAF, ParticleLeaf::new);
	}

	@Nullable
	public static Particle createParticle(TFParticleType particleType, World world, double x, double y, double z, double vx, double vy, double vz) {
		IParticleFactory factory = factories.get(particleType);
		return factory != null ? factory.createParticle(world, x, y, z, vx, vy, vz) : null;
	}

	@FunctionalInterface
	private interface IParticleFactory {

		Particle createParticle(World world, double x, double y, double z, double vx, double vy, double vz);
	}
}
