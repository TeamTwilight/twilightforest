package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TransformationParticle extends TextureSheetParticle {
    private final double xStart;
    private final double yStart;
    private final double zStart;

    public TransformationParticle(ClientLevel level, double x, double y, double z, double x2, double y2, double z2) {
        super(level, x, y, z);
        this.xd = x2;
        this.yd = y2;
        this.zd = z2;
        this.xStart = x;
        this.yStart = y;
        this.zStart = z;
        this.xo = x + x2;
        this.yo = y + y2;
        this.zo = z + z2;
        this.x = this.xo;
        this.y = this.yo;
        this.z = this.zo;
        this.quadSize = 0.5F * (this.random.nextFloat() * 0.3F + 0.2F);
        this.hasPhysics = false;
        this.lifetime = ((int)(Math.random() * 10.0D) + 10) * 2;
        this.alpha = 0;
        this.rCol = 0.38F;
        this.gCol = 0.82F;
        this.bCol = 0.82F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    @Override
    public int getLightColor(float partialTicks) {
        float f = ((float)this.age + partialTicks) / (float)this.lifetime;
        f = Mth.clamp(f, 0.0F, 1.0F);
        int i = super.getLightColor(partialTicks);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            float f = (float)this.age / (float)this.lifetime;
            this.alpha = Math.min(f * 1.35F, 1F);
            f = 1.0F - f;
            float f1 = 1.0F - f;
            f1 *= f1;
            f1 *= f1;
            this.x = this.xStart + this.xd * (double)f;
            this.y = this.yStart + this.yd * (double)f - (double)(f1 * 1.2F);
            this.z = this.zStart + this.zd * (double)f;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double x2, double y2, double z2) {
            TransformationParticle sortingParticle = new TransformationParticle(level, x, y, z, x2, y2, z2);
            sortingParticle.pickSprite(this.sprite);
            return sortingParticle;
        }
    }

    @Override
    public boolean shouldCull() {
        return false;
    }
}