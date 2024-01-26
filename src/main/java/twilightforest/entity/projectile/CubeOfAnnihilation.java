package twilightforest.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFItems;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;
import twilightforest.util.WorldUtil;

public class CubeOfAnnihilation extends ThrowableProjectile {

	private boolean hasHitObstacle = false;
	private ItemStack stack;

	public CubeOfAnnihilation(EntityType<? extends CubeOfAnnihilation> type, Level world) {
		super(type, world);
	}

	public CubeOfAnnihilation(EntityType<? extends CubeOfAnnihilation> type, Level world, LivingEntity thrower, ItemStack stack) {
		super(type, thrower, world);
		this.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0.0F, 1.5F, 1.0F);
		this.stack = stack;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	protected void defineSynchedData() {
	}

	@Override
	protected float getGravity() {
		return 0F;
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (result.getEntity() instanceof LivingEntity && result.getEntity().hurt(this.getDamageSource(), 10)) {
			this.tickCount += 60;
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		if (!this.level().isEmptyBlock(result.getBlockPos())) {
			this.affectBlocksInAABB(this.getBoundingBox().inflate(0.2F, 0.2F, 0.2F));
		}
	}

	@Override
	protected void onHit(HitResult result) {
		HitResult.Type hitresult$type = result.getType();
		if (hitresult$type == HitResult.Type.ENTITY) {
			this.onHitEntity((EntityHitResult) result);
		} else if (hitresult$type == HitResult.Type.BLOCK) {
			this.onHitBlock((BlockHitResult) result);
		}
	}

	private DamageSource getDamageSource() {
		LivingEntity thrower = (LivingEntity) this.getOwner();
		if (thrower instanceof Player) {
			return this.damageSources().playerAttack((Player) thrower);
		} else if (thrower != null) {
			return this.damageSources().mobAttack(thrower);
		} else {
			return this.damageSources().thrown(this, null);
		}
	}

	private void affectBlocksInAABB(AABB box) {
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			BlockState state = this.level().getBlockState(pos);
			if (!state.isAir()) {
				if (this.getOwner() instanceof ServerPlayer player) {
					if (!NeoForge.EVENT_BUS.post(new BlockEvent.BreakEvent(this.level(), pos, state, player)).isCanceled()) {
						if (this.canAnnihilate(pos, state, player.gameMode.getGameModeForPlayer().isBlockPlacingRestricted())) {
							this.level().removeBlock(pos, false);
							this.playSound(TFSounds.BLOCK_ANNIHILATED.get(), 0.125f, this.random.nextFloat() * 0.25F + 0.75F);
							this.annihilateParticles(this.level(), pos);
							this.gameEvent(GameEvent.BLOCK_DESTROY);
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

	private boolean canAnnihilate(BlockPos pos, BlockState state, boolean restrictedPlaceMode) {
		// whitelist many castle blocks
		Block block = state.getBlock();
		return (state.is(BlockTagGenerator.ANNIHILATION_INCLUSIONS) || block.getExplosionResistance() < 8F && state.getDestroySpeed(this.level(), pos) >= 0)
				&& (!restrictedPlaceMode || this.stack.hasAdventureModeBreakTagForBlock(this.level().registryAccess().registryOrThrow(Registries.BLOCK), new BlockInWorld(this.level(), pos, false)));
	}

	private void annihilateParticles(Level level, BlockPos pos) {
		RandomSource rand = level.getRandom();
		if (level instanceof ServerLevel server) {
			for (int dx = 0; dx < 3; dx++) {
				for (int dy = 0; dy < 3; dy++) {
					for (int dz = 0; dz < 3; dz++) {

						double x = pos.getX() + (dx + 0.5D) / 4;
						double y = pos.getY() + (dy + 0.5D) / 4;
						double z = pos.getZ() + (dz + 0.5D) / 4;

						double speed = rand.nextGaussian() * 0.2D;

						server.sendParticles(TFParticleType.ANNIHILATE.get(), x, y, z, 1, 0, 0, 0, speed);
					}
				}
			}
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.level().isClientSide) {
			if (this.getOwner() == null) {
				this.remove(RemovalReason.KILLED);
				return;
			}

			// always head towards either the point or towards the player
			Vec3 destPoint = new Vec3(this.getOwner().getX(), this.getOwner().getY() + this.getOwner().getEyeHeight(), this.getOwner().getZ());

			double distToPlayer = this.distanceTo(this.getOwner());

			if (this.isReturning()) {
				// if we are returning, and are near enough to the player, then we are done
				if (distToPlayer < 2.0F) {
					this.remove(RemovalReason.KILLED);
				}
			} else {
				destPoint = destPoint.add(this.getOwner().getLookAngle().scale(16F));
			}

			// set motions
			Vec3 velocity = new Vec3(this.getX() - destPoint.x(), (this.getY() + this.getBbHeight() / 2F) - destPoint.y(), this.getZ() - destPoint.z());

			this.setDeltaMovement(-velocity.x(), -velocity.y(), -velocity.z());

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
			this.affectBlocksInAABB(this.getBoundingBox().inflate(0.2F));
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		LivingEntity thrower = (LivingEntity) this.getOwner();
		if (thrower != null && thrower.getUseItem().is(TFItems.CUBE_OF_ANNIHILATION)) {
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
	protected void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		if (pCompound.contains("CubeOfAnnihilationStack", 10)) {
			this.stack = ItemStack.of(pCompound.getCompound("CubeOfAnnihilationStack"));
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.put("CubeOfAnnihilationStack", this.stack.save(new CompoundTag()));
	}
}
