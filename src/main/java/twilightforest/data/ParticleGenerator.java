package twilightforest.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFParticleType;

import java.util.Iterator;

public class ParticleGenerator extends ParticleDescriptionProvider {

	public ParticleGenerator(PackOutput output, ExistingFileHelper helper) {
		super(output, helper);
	}

	@Override
	protected void addDescriptions() {
		this.sprite(TFParticleType.ANNIHILATE.get(), TwilightForestMod.prefix("annihilate_particle"));
		this.spriteSet(TFParticleType.CLOUD_PUFF.get(), new ResourceLocation("generic"), 8, true);
		this.sprite(TFParticleType.DIM_FLAME.get(), TwilightForestMod.prefix("dim_flame"));
		this.spriteSet(TFParticleType.EXTENDED_SNOW_WARNING.get(), TwilightForestMod.prefix("snow"), 4, false);
		this.sprite(TFParticleType.FALLEN_LEAF.get(), TwilightForestMod.prefix("fallen_leaf"));
		this.sprite(TFParticleType.FIREFLY.get(), TwilightForestMod.prefix("firefly"));
		this.spriteSet(TFParticleType.GHAST_TRAP.get(), new ResourceLocation("generic"), 8, true);
		this.spriteSet(TFParticleType.HUGE_SMOKE.get(), new ResourceLocation("generic"), 8, true);
		this.spriteSet(TFParticleType.ICE_BEAM.get(), TwilightForestMod.prefix("snow"), 4, false);
		this.sprite(TFParticleType.LARGE_FLAME.get(), new ResourceLocation("flame"));
		this.spriteSet(TFParticleType.LEAF_RUNE.get(), () -> new Iterator<>() {
			private int counter = 0;

			@Override
			public boolean hasNext() {
				return this.counter < 26;
			}

			@Override
			public ResourceLocation next() {
				ResourceLocation texture = new ResourceLocation("sga_" + Character.toString('a' + this.counter));
				this.counter++;
				return texture;
			}
		});
		this.sprite(TFParticleType.LOG_CORE_PARTICLE.get(), TwilightForestMod.prefix("log_core"));
		this.sprite(TFParticleType.OMINOUS_FLAME.get(), TwilightForestMod.prefix("ominous_flame"));
		this.sprite(TFParticleType.PARTICLE_SPAWNER_FIREFLY.get(), TwilightForestMod.prefix("firefly"));
		this.sprite(TFParticleType.PROTECTION.get(), new ResourceLocation("glint"));
		this.spriteSet(TFParticleType.SNOW.get(), TwilightForestMod.prefix("snow"), 4, false);
		this.spriteSet(TFParticleType.SNOW_GUARDIAN.get(), TwilightForestMod.prefix("snow"), 4, false);
		this.spriteSet(TFParticleType.SNOW_WARNING.get(), TwilightForestMod.prefix("snow"), 4, false);
		this.sprite(TFParticleType.SORTING_PARTICLE.get(), TwilightForestMod.prefix("log_core"));
		this.sprite(TFParticleType.TRANSFORMATION_PARTICLE.get(), TwilightForestMod.prefix("log_core"));
		this.sprite(TFParticleType.WANDERING_FIREFLY.get(), TwilightForestMod.prefix("firefly"));
	}
}
