package twilightforest.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import twilightforest.init.TFSounds;

public class GhastTearParticle extends SingleQuadParticle {

	public GhastTearParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
		super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprite);
		this.rCol = this.gCol = this.bCol = 1.0F;
		this.quadSize = 2.0F;
		this.gravity = 0.6F;
		this.lifetime = 60 + this.random.nextInt(40);
		this.hasPhysics = true;
	}

	@Override
	protected Layer getLayer() {
		return Layer.OPAQUE_TERRAIN;
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

				this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(itemID)), this.x + this.random.nextFloat() - this.random.nextFloat(), this.y + 0.5F, this.z + this.random.nextFloat(), gaussX, gaussY, gaussZ);
			}
			this.remove();
		}
		super.tick();
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			Material.Baked material = this.calculateState(new ItemStack(Items.GHAST_TEAR), level).pickParticleMaterial(random);
			TextureAtlasSprite textureatlassprite;

			if (material != null) {
				textureatlassprite = material.sprite();
			} else {
				textureatlassprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(TextureAtlas.LOCATION_BLOCKS).getSprite(MissingTextureAtlasSprite.getLocation());
			}

			return new GhastTearParticle(level, x, y, z, textureatlassprite);
		}

		protected ItemStackRenderState calculateState(ItemStack stack, ClientLevel level) {
			var state = new ItemStackRenderState();
			Minecraft.getInstance()
				.getItemModelResolver()
				.updateForTopItem(state, stack, ItemDisplayContext.GROUND, level, null, 0);
			return state;
		}
	}
}
