package twilightforest.client.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleLeaf extends SpriteTexturedParticle {

	private final Vec3d target;

	public ParticleLeaf(World world, double x, double y, double z, double vx, double vy, double vz) {
		this(world, x, y, z, vx, vy, vz, 1.0F);
	}

	public ParticleLeaf(World world, double x, double y, double z, double vx, double vy, double vz, float scale) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		target = new Vec3d(x, y, z);
		this.motionX *= 0.10000000149011612D;
		this.motionY *= 0.10000000149011612D;
		this.motionZ *= 0.10000000149011612D;
		this.motionX += vx * 0.4D;
		this.motionY += vy * 0.4D;
		this.motionZ += vz * 0.4D;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleAlpha = 0F;
		this.particleScale *= 0.75F * (rand.nextBoolean() ? -1F : 1F);
		this.particleScale *= scale;
		this.maxAge = 160 + ((int) (rand.nextFloat() * 30F));
		this.maxAge = (int) ((float) this.maxAge * scale);
		this.canCollide = true;
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void tick() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.age++ >= this.maxAge) {
			this.setExpired();
		}

		this.move(this.motionX, this.motionY, this.motionZ);

		this.motionY *= 0.699999988079071D;
		this.motionY -= 0.019999999552965164D;

		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		} else {
			if (motionX == 0)
				motionX += (rand.nextBoolean() ? 1 : -1) * 0.001F;
			if (motionZ == 0)
				motionZ += (rand.nextBoolean() ? 1 : -1) * 0.001F;
			if (rand.nextInt(5) == 0)
				motionX += Math.signum(target.x - posX) * rand.nextFloat() * 0.005F;
			if (rand.nextInt(5) == 0)
				motionZ += Math.signum(target.z - posZ) * rand.nextFloat() * 0.005F;
		}
	}

	@Override
	public void buildGeometry(IVertexBuilder buffer, ActiveRenderInfo entity, float partialTicks) {
		particleAlpha = Math.min(MathHelper.clamp(age, 0, 20) / 20F, MathHelper.clamp(maxAge - age, 0, 20) / 20F);
		super.buildGeometry(buffer, entity, partialTicks);
	}

	@Override
	public int getBrightnessForRender(float partialTicks) {
		return 240 | 240 << 16;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<BasicParticleType> {
		private final IAnimatedSprite spriteSet;

		public Factory(IAnimatedSprite sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle makeParticle(BasicParticleType typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			ParticleLeaf particle = new ParticleLeaf(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.selectSpriteRandomly(this.spriteSet);
			return particle;
		}
	}
}
