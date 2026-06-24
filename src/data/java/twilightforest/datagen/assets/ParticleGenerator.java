package twilightforest.datagen.assets;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.data.ParticleDescriptionProvider;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFParticleType;

import java.util.Iterator;

public class ParticleGenerator extends ParticleDescriptionProvider {

	public ParticleGenerator(PackOutput output) {
		super(output);
	}

	@Override
	protected void addDescriptions() {
		this.spriteSet(TFParticleType.ANNIHILATE.get(), TwilightForestMod.prefix("annihilate_particle"));
		this.spriteSet(TFParticleType.CLOUD_PUFF.get(), Identifier.withDefaultNamespace("generic"), 8, true);
		this.spriteSet(TFParticleType.DIM_FLAME.get(), TwilightForestMod.prefix("dim_flame"));
		this.spriteSet(TFParticleType.EXTENDED_SNOW_WARNING.get(), TwilightForestMod.prefix("snow"), 4, false);
		this.spriteSet(TFParticleType.FALLEN_LEAF.get(), TwilightForestMod.prefix("fallen_leaf"));
		this.spriteSet(TFParticleType.FIREFLY.get(), TwilightForestMod.prefix("firefly"));
		this.spriteSet(TFParticleType.GHAST_TRAP.get(), Identifier.withDefaultNamespace("generic"), 8, true);
		this.spriteSet(TFParticleType.HUGE_SMOKE.get(), Identifier.withDefaultNamespace("generic"), 8, true);
		this.spriteSet(TFParticleType.ICE_BEAM.get(), TwilightForestMod.prefix("snow"), 4, false);
		this.spriteSet(TFParticleType.LARGE_FLAME.get(), Identifier.withDefaultNamespace("flame"));
		this.spriteSet(TFParticleType.LEAF_RUNE.get(), () -> new Iterator<>() {
			private int counter = 0;

			@Override
			public boolean hasNext() {
				return this.counter < 26;
			}

			@Override
			public Identifier next() {
				Identifier texture = Identifier.withDefaultNamespace("sga_" + Character.toString('a' + this.counter));
				this.counter++;
				return texture;
			}
		});
		this.spriteSet(TFParticleType.LOG_CORE_PARTICLE.get(), TwilightForestMod.prefix("log_core"));
		this.spriteSet(TFParticleType.OMINOUS_FLAME.get(), TwilightForestMod.prefix("ominous_flame"));
		this.spriteSet(TFParticleType.PARTICLE_SPAWNER_FIREFLY.get(), TwilightForestMod.prefix("firefly"));
		this.spriteSet(TFParticleType.PROTECTION.get(), Identifier.withDefaultNamespace("glint"));
		this.spriteSet(TFParticleType.SNOW.get(), TwilightForestMod.prefix("snow"), 4, false);
		this.spriteSet(TFParticleType.SNOW_GUARDIAN.get(), TwilightForestMod.prefix("snow"), 4, false);
		this.spriteSet(TFParticleType.SNOW_WARNING.get(), TwilightForestMod.prefix("snow"), 4, false);
		this.spriteSet(TFParticleType.SORTING_PARTICLE.get(), TwilightForestMod.prefix("log_core"));
		this.spriteSet(TFParticleType.TRANSFORMATION_PARTICLE.get(), TwilightForestMod.prefix("log_core"));
		this.spriteSet(TFParticleType.WANDERING_FIREFLY.get(), TwilightForestMod.prefix("firefly"));
		this.spriteSet(TFParticleType.MAGIC_EFFECT.get(), Identifier.withDefaultNamespace("effect"), 8, true);
		this.spriteSet(TFParticleType.ANGRY_LICH.get(), Identifier.withDefaultNamespace("angry"));
		this.spriteSet(TFParticleType.TWILIGHT_ORB.get(), TwilightForestMod.prefix("twilight_orb"));
		this.spriteSet(TFParticleType.SHIELD_BREAK.get(), TwilightForestMod.prefix("shield_break"));
	}
}
