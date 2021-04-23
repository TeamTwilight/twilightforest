package twilightforest.client.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleLeaf extends SpriteTexturedParticle {

	private final Vector3d target;
	private float rot;

	ParticleLeaf(ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
		this(world, x, y, z, vx, vy, vz, 1.0F);
	}

	ParticleLeaf(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, float scale) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		target = new Vector3d(x, y, z);
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
		this.maxAge = (int) (this.maxAge * scale);
		this.canCollide = true;
		this.prevParticleAngle = this.particleAngle = rand.nextFloat() * 2F - 1F;
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
			rot += 5F;
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
	public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo entity, float partialTicks) {
		particleAlpha = Math.min(MathHelper.clamp(age, 0, 20) / 20F, MathHelper.clamp(maxAge - age, 0, 20) / 20F);
		Vector3d lvt_4_1_ = entity.getProjectedView();
		float lvt_5_1_ = (float)(MathHelper.lerp((double)partialTicks, this.prevPosX, this.posX) - lvt_4_1_.getX());
		float lvt_6_1_ = (float)(MathHelper.lerp((double)partialTicks, this.prevPosY, this.posY) - lvt_4_1_.getY());
		float lvt_7_1_ = (float)(MathHelper.lerp((double)partialTicks, this.prevPosZ, this.posZ) - lvt_4_1_.getZ());
		Quaternion lvt_8_2_ = new Quaternion(entity.getRotation());
		if (this.particleAngle != 0.0F) {
			float lvt_9_1_ = MathHelper.lerp(partialTicks, this.prevParticleAngle, this.particleAngle);
			lvt_8_2_.multiply(Vector3f.ZP.rotation(lvt_9_1_));
		}
		lvt_8_2_.multiply(Vector3f.YP.rotation(MathHelper.cos((float) Math.toRadians(rot % 360F))));
		Vector3f lvt_9_2_ = new Vector3f(-1.0F, -1.0F, 0.0F);
		lvt_9_2_.transform(lvt_8_2_);
		Vector3f[] lvt_10_1_ = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
		float lvt_11_1_ = this.getScale(partialTicks);

		for(int lvt_12_1_ = 0; lvt_12_1_ < 4; ++lvt_12_1_) {
			Vector3f lvt_13_1_ = lvt_10_1_[lvt_12_1_];
			lvt_13_1_.transform(lvt_8_2_);
			lvt_13_1_.mul(lvt_11_1_);
			lvt_13_1_.add(lvt_5_1_, lvt_6_1_, lvt_7_1_);
		}
		float lvt_12_2_ = this.getMinU();
		float lvt_13_2_ = this.getMaxU();
		float lvt_14_1_ = this.getMinV();
		float lvt_15_1_ = this.getMaxV();
		int lvt_16_1_ = this.getBrightnessForRender(partialTicks);
		buffer.pos((double)lvt_10_1_[0].getX(), (double)lvt_10_1_[0].getY(), (double)lvt_10_1_[0].getZ()).tex(lvt_13_2_, lvt_15_1_).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(lvt_16_1_).endVertex();
		buffer.pos((double)lvt_10_1_[1].getX(), (double)lvt_10_1_[1].getY(), (double)lvt_10_1_[1].getZ()).tex(lvt_13_2_, lvt_14_1_).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(lvt_16_1_).endVertex();
		buffer.pos((double)lvt_10_1_[2].getX(), (double)lvt_10_1_[2].getY(), (double)lvt_10_1_[2].getZ()).tex(lvt_12_2_, lvt_14_1_).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(lvt_16_1_).endVertex();
		buffer.pos((double)lvt_10_1_[3].getX(), (double)lvt_10_1_[3].getY(), (double)lvt_10_1_[3].getZ()).tex(lvt_12_2_, lvt_15_1_).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(lvt_16_1_).endVertex();
	}

	@Override
	public int getBrightnessForRender(float partialTicks) {
		return 240 | 240 << 16;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<LeafParticleData> {
		private final IAnimatedSprite spriteSet;

		public Factory(IAnimatedSprite sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle makeParticle(LeafParticleData data, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			ParticleLeaf particle = new ParticleLeaf(world, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.setColor(data.r / 255F, data.g / 255F, data.b / 255F);
			particle.selectSpriteRandomly(this.spriteSet);
			return particle;
		}
	}
}
