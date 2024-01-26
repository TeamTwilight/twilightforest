package twilightforest.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import twilightforest.init.TFSounds;

@OnlyIn(Dist.CLIENT)
public class GhastTearParticle extends TextureSheetParticle {

	public GhastTearParticle(ClientLevel level, double x, double y, double z, Item item) {
		super(level, x, y, z, 0.0D, 0.0D, 0.0D);
		this.sprite = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(item).getParticleIcon(ModelData.EMPTY);
		this.rCol = this.gCol = this.bCol = 1.0F;
		this.quadSize = 2.0F;
		this.gravity = 0.6F;

		this.lifetime = 60 + random.nextInt(40);
		this.hasPhysics = true;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.TERRAIN_SHEET;
	}

	@Override
	public void tick() {
		if (this.onGround) {
			if (this.random.nextBoolean()) {
				this.level.playLocalSound(this.x, this.y + 1.0D, this.z, TFSounds.TEAR_BREAK.get(), SoundSource.AMBIENT, 0.5F, 1.65F, false);
			}

			ItemStack itemID = new ItemStack(Items.GHAST_TEAR);
			for (int i = 0; i < 50; ++i) {
				double gaussX = this.random.nextGaussian() * 0.1D;
				double gaussY = this.random.nextGaussian() * 0.2D;
				double gaussZ = this.random.nextGaussian() * 0.1D;

				this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemID), this.x + this.random.nextFloat() - this.random.nextFloat(), this.y + 0.5F, this.z + this.random.nextFloat(), gaussX, gaussY, gaussZ);
			}
			this.remove();
		}
		super.tick();
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new GhastTearParticle(level, x, y, z, Items.GHAST_TEAR);
		}
	}
}
