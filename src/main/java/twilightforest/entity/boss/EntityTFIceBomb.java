package twilightforest.entity.boss;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import twilightforest.entity.projectile.EntityTFThrowable;
import twilightforest.entity.EntityTFYeti;
import twilightforest.potions.TFPotions;

import java.util.List;

public class EntityTFIceBomb extends EntityTFThrowable {

	private int zoneTimer = 80;
	private boolean hasHit;

	public EntityTFIceBomb(EntityType<? extends EntityTFIceBomb> type, World world) {
		super(type, world);
	}

	public EntityTFIceBomb(EntityType<? extends EntityTFIceBomb> type, World world, LivingEntity thrower) {
		super(type, world, thrower);
	}

	@Override
	protected void onImpact(RayTraceResult ray) {
		this.motionY = 0;
		this.hasHit = true;

		if (!world.isRemote)
			this.doTerrainEffects();
	}

	private void doTerrainEffects() {

		final int range = 3;

		int ix = MathHelper.floor(this.lastTickPosX);
		int iy = MathHelper.floor(this.lastTickPosY);
		int iz = MathHelper.floor(this.lastTickPosZ);

		for (int x = -range; x <= range; x++) {
			for (int y = -range; y <= range; y++) {
				for (int z = -range; z <= range; z++) {
					this.doTerrainEffect(new BlockPos(ix + x, iy + y, iz + z));
				}
			}
		}
	}

	/**
	 * Freeze water, put snow on snowable surfaces
	 */
	private void doTerrainEffect(BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.getMaterial() == Material.WATER) {
			this.world.setBlockState(pos, Blocks.ICE.getDefaultState());
		}
		if (state.getMaterial() == Material.LAVA) {
			this.world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
		}
		if (this.world.isAirBlock(pos) && Blocks.SNOW.canPlaceBlockAt(this.world, pos)) {
			this.world.setBlockState(pos, Blocks.SNOW.getDefaultState());
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (this.hasHit) {
//			this.motionX *= 0.1D;
//			this.motionY *= 0.1D;
//			this.motionZ *= 0.1D;
			this.getMotion().mul(0.1D, 0.1D, 0.1D);

			this.zoneTimer--;
			makeIceZone();

			if (!world.isRemote && this.zoneTimer <= 0) {
				world.playEvent(2001, new BlockPos(this), Block.getStateId(Blocks.ICE.getDefaultState()));
				remove();
			}
		} else {
			makeTrail();
		}
	}

	public void makeTrail() {
		BlockState stateId = Blocks.SNOW.getDefaultState();
		for (int i = 0; i < 10; i++) {
			double dx = getX() + 0.75F * (rand.nextFloat() - 0.5F);
			double dy = getY() + 0.75F * (rand.nextFloat() - 0.5F);
			double dz = getZ() + 0.75F * (rand.nextFloat() - 0.5F);

			world.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, stateId), dx, dy, dz, -getMotion().getX(), -getMotion().getY(), -getMotion().getZ());
		}
	}

	private void makeIceZone() {
		if (this.world.isRemote) {
			// sparkles
			BlockState stateId = Blocks.SNOW.getDefaultState();
			for (int i = 0; i < 20; i++) {
				double dx = this.getX() + (rand.nextFloat() - rand.nextFloat()) * 3.0F;
				double dy = this.getY() + (rand.nextFloat() - rand.nextFloat()) * 3.0F;
				double dz = this.getZ() + (rand.nextFloat() - rand.nextFloat()) * 3.0F;

				world.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, stateId), dx, dy, dz, 0, 0, 0);
			}
		} else {
			if (this.zoneTimer % 10 == 0) {
				hitNearbyEntities();
			}
		}
	}

	private void hitNearbyEntities() {
		List<LivingEntity> nearby = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(3, 2, 3));

		for (LivingEntity entity : nearby) {
			if (entity != this.getThrower()) {
				if (entity instanceof EntityTFYeti) {
					// TODO: make "frozen yeti" entity?
					BlockPos pos = new BlockPos(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ);
					world.setBlockState(pos, Blocks.ICE.getDefaultState());
					world.setBlockState(pos.up(), Blocks.ICE.getDefaultState());

					entity.remove();
				} else {
					entity.attackEntityFrom(DamageSource.MAGIC, 1);
					entity.addPotionEffect(new EffectInstance(TFPotions.frosty.get(), 20 * 5, 2));
				}
			}
		}
	}

	public BlockState getBlock() {
		return Blocks.PACKED_ICE.getDefaultState();
	}

	@Override
	protected float getGravityVelocity() {
		return this.hasHit ? 0F : 0.025F;
	}
}
