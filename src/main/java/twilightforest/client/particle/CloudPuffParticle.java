package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CloudPuffParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    CloudPuffParticle(ClientLevel level, double X, double Y, double Z, double XSpeed, double YSpeed, double ZSpeed, SpriteSet sprites) {
        super(level, X, Y, Z, 0.0D, 0.0D, 0.0D);
        this.friction = 0.96F;
        this.sprites = sprites;
        this.xd *= 0.1D;
        this.yd *= 0.1D;
        this.zd *= 0.1D;
        this.xd += XSpeed;
        this.yd += YSpeed;
        this.zd += ZSpeed;
        float f1 = 1.0F - (float)(Math.random() * (double)0.3F);
        this.rCol = f1;
        this.gCol = f1;
        this.bCol = f1;
        this.quadSize *= 0.875F;
        int i = (int)(8.0D / (Math.random() * 0.8D + 0.3D));
        this.lifetime = (int)Math.max((float)i * 2.5F, 1.0F);
        this.hasPhysics = false;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float partialTick) {
        return this.quadSize * Mth.clamp(((float)this.age + partialTick) / (float)this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            this.setSpriteFromAge(this.sprites);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public record Factory(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new CloudPuffParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }
}