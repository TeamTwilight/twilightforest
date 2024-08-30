package twilightforest.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import twilightforest.client.particle.data.LeafParticleData;

public class LeafParticle extends TextureSheetParticle {

	private final Vec3 target;
	private float rot;

	public LeafParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz) {
		this(world, x, y, z, vx, vy, vz, 1.0F);
	}

	public LeafParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, float scale) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		target = new Vec3(x, y, z);
		this.xd *= 0.1D;
		this.yd *= 0.1D;
		this.zd *= 0.1D;
		this.xd += vx * 0.4D;
		this.yd += vy * 0.4D;
		this.zd += vz * 0.4D;
		this.rCol = this.gCol = this.bCol = 1.0F;
		this.alpha = 0.0F;
		this.quadSize *= 0.75F * (random.nextBoolean() ? -1.0F : 1.0F);
		this.quadSize *= scale;
		this.lifetime = 160 + ((int) (random.nextFloat() * 30.0F));
		this.lifetime = (int) (this.lifetime * scale);
		this.hasPhysics = true;
		this.oRoll = this.roll = random.nextFloat() * 2.0F - 1.0F;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		if (this.age++ >= this.lifetime) {
			this.remove();
		}

		this.move(this.xd, this.yd, this.zd);

		this.yd *= 0.7D;
		this.yd -= 0.02D;

		if (this.onGround) {
			this.xd *= 0.7D;
			this.zd *= 0.7D;
		} else {
			rot += 5F;
			if (xd == 0)
				xd += (random.nextBoolean() ? 1 : -1) * 0.001F;
			if (zd == 0)
				zd += (random.nextBoolean() ? 1 : -1) * 0.001F;
			if (random.nextInt(5) == 0)
				xd += Math.signum(target.x - x) * random.nextFloat() * 0.005F;
			if (random.nextInt(5) == 0)
				zd += Math.signum(target.z - z) * random.nextFloat() * 0.005F;
		}
	}

	@Override
	public void render(VertexConsumer buffer, Camera entity, float partialTicks) {
		this.alpha = Math.min(Mth.clamp(this.age, 0, 20) / 20.0F, Mth.clamp(this.lifetime - this.age, 0, 20) / 20.0F);
		Quaternionf quaternion = new Quaternionf();
		if (this.roll != 0.0F) {
			quaternion.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
		}
		quaternion.rotateY(Mth.cos((float) Math.toRadians(this.rot % 360.0F)));
		this.renderRotatedQuad(buffer, entity, quaternion, partialTicks);
		quaternion.rotateY(-Mth.PI).rotateZ(Mth.HALF_PI);
		this.renderRotatedQuad(buffer, entity, quaternion, partialTicks);
	}

	@Override
	public int getLightColor(float partialTicks) {
		return 240 | 240 << 16;
	}

	public record Factory(SpriteSet sprite) implements ParticleProvider<LeafParticleData> {

		@Override
		public Particle createParticle(LeafParticleData data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			LeafParticle particle = new LeafParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.setColor(data.r() / 255.0F, data.g() / 255.0F, data.b() / 255.0F);
			particle.pickSprite(this.sprite);
			return particle;
		}
	}
}
