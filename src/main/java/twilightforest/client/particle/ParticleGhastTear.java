package twilightforest.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFSounds;

@OnlyIn(Dist.CLIENT)
public class ParticleGhastTear extends SpriteTexturedParticle {

	ParticleGhastTear(ClientWorld world, double x, double y, double z, Item item) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		this.sprite = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getParticleIcon(item);
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleScale = 2.0F;
		this.particleGravity = 0.6F;

		this.maxAge = 20 + rand.nextInt(40);
		this.canCollide = true;
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.TERRAIN_SHEET;
	}

	@Override
	public void tick() {
		if (this.onGround && rand.nextBoolean()) {
			world.playSound(null, this.posX, this.posY + 1D, this.posZ, TFSounds.TEAR_BREAK, SoundCategory.HOSTILE, 0.5F, 1.0F);

			ItemStack itemID = new ItemStack(Items.GHAST_TEAR);
			for (int i = 0; i < 20; ++i) {
				double gaussX = rand.nextGaussian() * 0.1D;
				double gaussY = rand.nextGaussian() * 0.2D;
				double gaussZ = rand.nextGaussian() * 0.1D;

				world.addParticle(new ItemParticleData(ParticleTypes.ITEM, itemID), this.posX + rand.nextFloat() - rand.nextFloat(), this.posY + 0.5F, this.posZ + rand.nextFloat(), gaussX, gaussY, gaussZ);
				world.addParticle(ParticleTypes.EXPLOSION, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);

			}
			this.setExpired();
		}
		super.tick();
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<BasicParticleType> {
		@Override
		public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new ParticleGhastTear(worldIn, x, y, z, Items.GHAST_TEAR);
		}
	}
}
