package twilightforest.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import twilightforest.TFSounds;
import twilightforest.client.particle.TFParticleType;
import twilightforest.data.BlockTagGenerator;
import twilightforest.item.TFItems;
import twilightforest.util.WorldUtil;

import java.util.Random;

public class CubeOfAnnihilation extends ThrowableProjectile {

	private boolean hasHitObstacle = false;

	public CubeOfAnnihilation(EntityType<? extends CubeOfAnnihilation> type, Level world) {
		super(type, world);
		this.fireImmune();
	}

	public CubeOfAnnihilation(EntityType<? extends CubeOfAnnihilation> type, Level world, LivingEntity thrower) {
		super(type, thrower, world);
		this.fireImmune();
		this.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0F, 1.5F, 1F);
	}

	@Override
	protected void defineSynchedData() {
	}

	@Override
	protected float getGravity() {
		return 0F;
	}

	@Override
	protected void onHit(HitResult ray) {
		if (level.isClientSide)
			return;

		// only hit living things
		if (ray instanceof EntityHitResult) {
			if (((EntityHitResult) ray).getEntity() instanceof LivingEntity && ((EntityHitResult) ray).getEntity().hurt(this.getDamageSource(), 10)) {
				this.tickCount += 60;
			}
		}

		if (ray instanceof BlockHitResult raytrace) {
			if (raytrace.getBlockPos() != null && !this.level.isEmptyBlock(raytrace.getBlockPos())) {
				this.affectBlocksInAABB(this.getBoundingBox().inflate(0.2F, 0.2F, 0.2F));
			}
		}
	}

	private DamageSource getDamageSource() {
		LivingEntity thrower = (LivingEntity) this.getOwner();
		if (thrower instanceof Player) {
			return DamageSource.playerAttack((Player) thrower);
		} else if (thrower != null) {
			return DamageSource.mobAttack(thrower);
		} else {
			return DamageSource.thrown(this, null);
		}
	}

	private void affectBlocksInAABB(AABB box) {
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			BlockState state = level.getBlockState(pos);
			if (!state.isAir()) {
				if (getOwner() instanceof Player player) {
					if (!MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(level, pos, state, player))) {
						if (canAnnihilate(pos, state)) {
							this.level.removeBlock(pos, false);
							this.playSound(TFSounds.BLOCK_ANNIHILATED, 0.125f, this.random.nextFloat() * 0.25F + 0.75F);
							this.annihilateParticles(level, pos);
						} else {
							this.hasHitObstacle = true;
						}
					} else {
						this.hasHitObstacle = true;
					}
				}
			}
		}
	}

	private boolean canAnnihilate(BlockPos pos, BlockState state) {
		// whitelist many castle blocks
		Block block = state.getBlock();
		return state.is(BlockTagGenerator.ANNIHILATION_INCLUSIONS) || block.getExplosionResistance() < 8F && state.getDestroySpeed(level, pos) >= 0;
	}

	private void annihilateParticles(Level world, BlockPos pos) {
		Random rand = world.getRandom();
		if(world instanceof ServerLevel) {
			for (int dx = 0; dx < 3; ++dx) {
				for (int dy = 0; dy < 3; ++dy) {
					for (int dz = 0; dz < 3; ++dz) {

						double x = pos.getX() + (dx + 0.5D) / 4;
						double y = pos.getY() + (dy + 0.5D) / 4;
						double z = pos.getZ() + (dz + 0.5D) / 4;

						double speed = rand.nextGaussian() * 0.2D;

						((ServerLevel)world).sendParticles(TFParticleType.ANNIHILATE.get(), x, y, z, 1, 0, 0, 0, speed);
					}
				}
			}
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.level.isClientSide) {
			if (this.getOwner() == null) {
				this.remove(RemovalReason.KILLED);
				return;
			}

			// always head towards either the point or towards the player
			Vec3 destPoint = new Vec3(this.getOwner().getX(), this.getOwner().getY() + this.getOwner().getEyeHeight(), this.getOwner().getZ());

			double distToPlayer = this.distanceTo(this.getOwner());

			if (this.isReturning()) {
				// if we are returning, and are near enough to the player, then we are done
				if (distToPlayer < 2F) {
					this.remove(RemovalReason.KILLED);
				}
			} else {
				destPoint = destPoint.add(getOwner().getLookAngle().scale(16F));
			}

			// set motions
			Vec3 velocity = new Vec3(this.getX() - destPoint.x(), (this.getY() + this.getBbHeight() / 2F) - destPoint.y(), this.getZ() - destPoint.z());

			setDeltaMovement(-velocity.x(), -velocity.y(), -velocity.z());

			// normalize speed
			float currentSpeed = Mth.sqrt((float) (this.getDeltaMovement().x() * this.getDeltaMovement().x() + this.getDeltaMovement().y() * this.getDeltaMovement().y() + this.getDeltaMovement().z() * this.getDeltaMovement().z()));

			float maxSpeed = 0.5F;

			if (currentSpeed > maxSpeed) {
				this.setDeltaMovement(new Vec3(
						this.getDeltaMovement().x() / (currentSpeed / maxSpeed),
						this.getDeltaMovement().y() / (currentSpeed / maxSpeed),
						this.getDeltaMovement().z() / (currentSpeed / maxSpeed)));
			} else {
				float slow = 0.5F;
				this.getDeltaMovement().multiply(slow, slow, slow);
			}

			// demolish some blocks
			this.affectBlocksInAABB(this.getBoundingBox().inflate(0.2F, 0.2F, 0.2F));
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		LivingEntity thrower = (LivingEntity) this.getOwner();
		if (thrower != null && thrower.getUseItem().getItem() == TFItems.CUBE_OF_ANNIHILATION.get()) {
			thrower.stopUsingItem();
		}
	}

	private boolean isReturning() {
		if (this.hasHitObstacle || this.getOwner() == null || !(this.getOwner() instanceof Player player)) {
			return true;
		} else {
			return !player.isUsingItem();
		}
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
