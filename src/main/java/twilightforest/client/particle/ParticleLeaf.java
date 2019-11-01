package twilightforest.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;

@SideOnly(Side.CLIENT)
public class ParticleLeaf extends Particle {

	private final Vec3d target;
	private float rot;

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
		this.particleMaxAge = 160 + ((int) (rand.nextFloat() * 30F));
		this.particleMaxAge = (int) ((float) this.particleMaxAge * scale);
		this.canCollide = true;

		this.setParticleTexture(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TwilightForestMod.ID + ":particles/fallen_leaf"));

		this.onUpdate();
	}

	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge) {
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
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		particleAlpha = Math.min(MathHelper.clamp(particleAge, 0, 20) / 20F, MathHelper.clamp(particleMaxAge - particleAge, 0, 20) / 20F);
		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ + MathHelper.cos((float) Math.toRadians((rot + partialTicks) % 360F)), rotationYZ, rotationXY, rotationXZ);
	}

	@Override
	public int getBrightnessForRender(float partialTicks) {
		return 240 | 240 << 16;
	}

	@Override
	public int getFXLayer() {
		return 1;
	}
}
