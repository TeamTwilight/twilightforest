package twilightforest.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleGhastTear extends Particle {

	public ParticleGhastTear(World world, double x, double y, double z, Item item) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		this.setParticleTexture(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(item));
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleGravity = Blocks.SNOW.blockParticleGravity * 2F;
		this.particleScale = 16.0F;

		this.particleMaxAge = 20 + rand.nextInt(40);
		this.canCollide = true;
	}

	public ParticleGhastTear(World world, double x, double y, double z, double vx, double vy, double vz, Item item) {
		this(world, x, y, z, item);
		this.motionX *= 0.10000000149011612D;
		this.motionY *= 0.10000000149011612D;
		this.motionZ *= 0.10000000149011612D;
		this.motionX += vx;
		this.motionY += vy;
		this.motionZ += vz;

		//TwilightForestMod.LOGGER.info("creating tear particle {}, isremote {}", this, world.isRemote);
	}

	@Override
	public int getFXLayer() {
		return 1;
	}

	@Override
	public void onUpdate() {
		if (this.onGround && rand.nextBoolean()) {
			world.playSound(null, this.posX, this.posY + 1D, this.posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.HOSTILE, 0.5F, 1.0F);

			int itemID = Item.getIdFromItem(Items.GHAST_TEAR);
			for (int i = 0; i < 20; ++i) {
				double gaussX = rand.nextGaussian() * 0.1D;
				double gaussY = rand.nextGaussian() * 0.2D;
				double gaussZ = rand.nextGaussian() * 0.1D;

				//TwilightForestMod.LOGGER.info("tear impact {}, isremote {}", this, world.isRemote);

				world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX + rand.nextFloat() - rand.nextFloat(), this.posY + 0.5F, this.posZ + rand.nextFloat(), gaussX, gaussY, gaussZ, itemID);
				world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);

			}
			this.setExpired();
		}
		super.onUpdate();
	}
}
