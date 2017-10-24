package twilightforest.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;

@SideOnly(Side.CLIENT)
public class ParticleIceBeam extends Particle {
	float initialParticleScale;

	public ParticleIceBeam(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
		this(par1World, par2, par4, par6, par8, par10, par12, 1.0F);
	}

	public ParticleIceBeam(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, float par14) {
		super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
		this.motionX *= 0.10000000149011612D;
		this.motionY *= 0.10000000149011612D;
		this.motionZ *= 0.10000000149011612D;
		this.motionX += par8 * 0.4D;
		this.motionY += par10 * 0.4D;
		this.motionZ += par12 * 0.4D;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleScale *= 0.75F;
		this.particleScale *= par14;
		this.initialParticleScale = this.particleScale;
//        this.particleMaxAge = (int)(6.0D / (Math.random() * 0.8D + 0.6D));
//        this.particleMaxAge = (int)((float)this.particleMaxAge * par14);
		this.particleMaxAge = 50;
		this.canCollide = true;

		this.particleTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(new ResourceLocation(TwilightForestMod.ID, "particles/snow_" + (rand.nextInt(4))).toString());

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

		this.motionX *= 0.9599999785423279D;
		this.motionY *= 0.9599999785423279D;
		this.motionZ *= 0.9599999785423279D;
		//this.motionY -= 0.019999999552965164D;

		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}
	}

	@Override
	public int getBrightnessForRender(float par1) {
		return 240 | 240 << 16;
	}

	@Override
	public int getFXLayer() {
		return 1;
	}
}