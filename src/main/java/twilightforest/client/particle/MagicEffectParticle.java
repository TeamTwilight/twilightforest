package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ColorParticleOption;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MagicEffectParticle extends SpellParticle {
	protected MagicEffectParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
	}

	@Override
	public int getLightColor(float partialTick) {
		return 0xF000F0;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<ColorParticleOption> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprite) {
			this.sprite = sprite;
		}

		public Particle createParticle(ColorParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			MagicEffectParticle particle = new MagicEffectParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite);
			particle.setColor(type.getRed(), type.getGreen(), type.getBlue());
			particle.setAlpha(type.getAlpha());
			return particle;
		}
	}
}
