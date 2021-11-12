package twilightforest.entity.projectile;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.boss.AlphaYeti;

import java.util.List;

public class FallingIce extends FallingBlockEntity {

	private static final int HANG_TIME = 100;

	public FallingIce(EntityType<? extends FallingIce> type, Level world) {
		super(type, world); // set falling tile on client to prevent crash
	}

	public FallingIce(Level world, int x, int y, int z) {
		super(world, x, y, z, Blocks.PACKED_ICE.defaultBlockState());
		this.fallDamagePerDistance = 10.0F;
		this.fallDamageMax = 30;
		this.hurtEntities = true;
	}

	@Override
	public void tick() {
		if (time > HANG_TIME) {
			setNoGravity(true);
		}

		super.tick();

		// kill other nearby blocks if they are not as old as this one
		if (!this.level.isClientSide) {
			List<FallingIce> nearby = this.level.getEntitiesOfClass(FallingIce.class, this.getBoundingBox());

			for (FallingIce entity : nearby) {
				if (entity != this) {
					if (entity.time < this.time) {
						entity.discard();
					}
				}
			}

			destroyIceInAABB(this.getBoundingBox().inflate(0.5, 0, 0.5));
		} else {
			makeTrail();
		}
	}

	private void makeTrail() {
		for (int i = 0; i < 2; i++) {
			double dx = this.getX() + 2F * (random.nextFloat() - random.nextFloat());
			double dy = this.getY() - 3F + 3F * (random.nextFloat() - random.nextFloat());
			double dz = this.getZ() + 2F * (random.nextFloat() - random.nextFloat());

			level.addParticle(TFParticleType.SNOW_WARNING.get(), dx, dy, dz, 0, -1, 0);
		}
	}

	// [VanillaCopy] Like super, but without anvil cases and with extra stuff
	@Override
	public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
		if (this.hurtEntities) {
			int i = Mth.ceil(distance - 1.0F);

			if (i > 0) {
				List<Entity> list = this.level.getEntities(this, this.getBoundingBox());
				DamageSource damagesource = DamageSource.FALLING_BLOCK;

				for (Entity entity : list) {
					if (!(entity instanceof AlphaYeti)) {
						entity.hurt(damagesource, Math.min(Mth.floor(i * this.fallDamagePerDistance), this.fallDamageMax));
					}
				}
			}
		}

		BlockState stateId = Blocks.PACKED_ICE.defaultBlockState();
		for (int i = 0; i < 200; i++) {
			double dx = this.getX() + 3F * (random.nextFloat() - random.nextFloat());
			double dy = this.getY() + 2 + 3F * (random.nextFloat() - random.nextFloat());
			double dz = this.getZ() + 3F * (random.nextFloat() - random.nextFloat());

			this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, stateId), dx, dy, dz, 0, 0, 0);
		}

		this.playSound(Blocks.PACKED_ICE.getSoundType(Blocks.PACKED_ICE.defaultBlockState(), level, blockPosition(), null).getBreakSound(), 3F, 0.5F);
		return false;
	}

	private void destroyIceInAABB(AABB aabb) {
		int minX = Mth.floor(aabb.minX);
		int minY = Mth.floor(aabb.minY);
		int minZ = Mth.floor(aabb.minZ);
		int maxX = Mth.floor(aabb.maxX);
		int maxY = Mth.floor(aabb.maxY);
		int maxZ = Mth.floor(aabb.maxZ);

		for (int dx = minX; dx <= maxX; ++dx) {
			for (int dy = minY; dy <= maxY; ++dy) {
				for (int dz = minZ; dz <= maxZ; ++dz) {
					BlockPos pos = new BlockPos(dx, dy, dz);
					Block block = this.level.getBlockState(pos).getBlock();

					if (block == Blocks.ICE || block == Blocks.PACKED_ICE || block == Blocks.STONE) {
						this.level.destroyBlock(pos, false);
					}
				}
			}
		}
	}
}
