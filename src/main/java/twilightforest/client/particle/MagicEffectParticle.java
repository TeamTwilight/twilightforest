package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.RandomSource;

public class MagicEffectParticle extends SpellParticle {

	protected MagicEffectParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
	}

	@Override
	public int getLightCoords(float partialTick) {
		return LightCoordsUtil.FULL_BRIGHT;
	}

	public static class Factory implements ParticleProvider<ColorParticleOption> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprite) {
			this.sprite = sprite;
		}

		@Override
		public Particle createParticle(ColorParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			MagicEffectParticle particle = new MagicEffectParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite);
			particle.setColor(type.getRed(), type.getGreen(), type.getBlue());
			particle.setAlpha(type.getAlpha());
			return particle;
		}
	}
}
