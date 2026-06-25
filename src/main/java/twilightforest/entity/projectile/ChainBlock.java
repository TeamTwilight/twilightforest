package twilightforest.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.tags.TFBlockTags;

public class ChainBlock extends ThrowableProjectile implements IEntityWithComplexSpawn {

	private static final int MAX_STUCK_TICKS = 100;
	private static final int MAX_CHAIN = 16;

	private static final EntityDataAccessor<Boolean> HAND = SynchedEntityData.defineId(ChainBlock.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> IS_FOIL = SynchedEntityData.defineId(ChainBlock.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> IS_RETURNING = SynchedEntityData.defineId(ChainBlock.class, EntityDataSerializers.BOOLEAN);
	private boolean hitEntity = false;
	private int stuckTime;
	@Nullable
	private ItemStack stack;
	private double velX;
	private double velY;
	private double velZ;

	public ChainBlock(EntityType<? extends ChainBlock> type, Level level) {
		super(type, level);
	}

	public ChainBlock(EntityType<? extends ChainBlock> type, Level level, LivingEntity thrower, InteractionHand hand, ItemStack stack) {
		super(type, thrower.getX(), thrower.getEyeY() - 0.1F, thrower.getZ(), level);
		this.stack = stack;
		this.setHand(hand);
		this.setOwner(thrower);
		this.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0.0F, 1.5F, 1.0F);
		this.getEntityData().set(IS_FOIL, stack.hasFoil());
	}

	private void setHand(InteractionHand hand) {
		this.getEntityData().set(HAND, hand == InteractionHand.MAIN_HAND);
	}

	public InteractionHand getHand() {
		return this.getEntityData().get(HAND) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
	}

	public boolean isFoil() {
		return this.getEntityData().get(IS_FOIL);
	}

	public void setIsReturning(boolean returning) {
		this.getEntityData().set(IS_RETURNING, returning);
	}

	public boolean isReturning() {
		return this.getEntityData().get(IS_RETURNING);
	}

	@Override
	public boolean canUsePortal(boolean p_352918_) {
		return false;
	}

	@Override
	public void shoot(double x, double y, double z, float speed, float accuracy) {
		super.shoot(x, y, z, speed, accuracy);

		// save velocity
		this.velX = this.getDeltaMovement().x();
		this.velY = this.getDeltaMovement().y();
		this.velZ = this.getDeltaMovement().z();
	}

	@Override
	@SuppressWarnings("SuspiciousNameCombination")
	protected void updateRotation() {
		Vec3 vec3 = this.getDeltaMovement();
		if (this.isReturning() && this.getOwner() instanceof LivingEntity living) {
			// Use this vec3 when returning. It's supposed to be pulled, so turning to face you doesn't make sense
			vec3 = vec3.normalize().scale(-1.0D).lerp(this.getEyePosition().subtract(living.getEyePosition()).normalize(), 0.5);
		}
		double d0 = vec3.horizontalDistance();
		this.setXRot(lerpRotation(this.xRotO, (float)(Mth.atan2(vec3.y, d0) * 180.0F / (float)Math.PI)));
		this.setYRot(lerpRotation(this.yRotO, (float)(Mth.atan2(vec3.x, vec3.z) * 180.0F / (float)Math.PI)));
	}

	@Override
	protected double getDefaultGravity() {
		return 0.05F;
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		// only hit living things & inside the world border
		Level level = this.level();
		if (!level.isClientSide() && result.getEntity() != this.getOwner() && level.getWorldBorder().isWithinBounds(result.getEntity().blockPosition())) {
			float damage = 10.0F;
			DamageSource source = TFDamageTypes.getIndirectEntityDamageSource(level, TFDamageTypes.SPIKED, this, this.getOwner());
			if (stack != null) {
				if (result.getEntity() instanceof LivingEntity living) {
					damage = EnchantmentHelper.modifyDamage((ServerLevel) level, this.stack, living, source, damage);
				} else if (result.getEntity() instanceof PartEntity<?> part && part.getParent() instanceof LivingEntity living) {
					damage = EnchantmentHelper.modifyDamage((ServerLevel) level, this.stack, living, source, damage);
				}
			}

			//properly disable shields
			if (result.getEntity() instanceof Player player && player.isUsingItem() && player.getUseItem().has(DataComponents.BLOCKS_ATTACKS)) {
				player.getUseItem().hurtAndBreak(5, player, (player.getUsedItemHand()));
				player.getUseItem().get(DataComponents.BLOCKS_ATTACKS).disable((ServerLevel) level, player, 100, player.getUseItem());
			}

			if (damage > 0.0F) {
				if (result.getEntity().hurtServer((ServerLevel) level, source, damage)) {
					this.playSound(TFSounds.BLOCK_AND_CHAIN_HIT.get(), 1.0f, this.random.nextFloat());
					// age when we hit a monster so that we go back to the player faster
					this.hitEntity = true;
					this.setIsReturning(true);
					this.tickCount += 60;
					if (this.getOwner() instanceof LivingEntity living) {
						this.stack.hurtAndBreak(1, living, this.getHand());
					}
				}
			}
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		BlockPos pos = result.getBlockPos();
		if (this.level() instanceof ServerLevel level && this.stack != null) {
			BlockState state = level.getBlockState(pos);
			if (!state.isAir()) {
				boolean restrictedPlaceMode = this.getOwner() instanceof ServerPlayer player && player.gameMode.getGameModeForPlayer().isBlockPlacingRestricted();
				if (!canBreakBlockAt(level, pos, state, this.stack, restrictedPlaceMode) || this.getData(TFDataAttachments.SMASH_BLOCKS).getBlocksSmashed() >= 12) {
					this.bounce(result.getDirection());
				}

				Vec3 vec3 = result.getBlockPos().clampLocationWithin(result.getLocation().add(-0.5D, 0D, 0D));
				EnchantmentHelper.onHitBlock(
					level,
					this.stack,
					this.getOwner() instanceof LivingEntity livingentity ? livingentity : null,
					this,
					null,
					vec3,
					level.getBlockState(result.getBlockPos()),
					item -> this.kill(level)
				);
			}
		}
	}

	public void bounce(Direction direction) {
		if (!this.isReturning() && !this.hitEntity) {
			this.playSound(TFSounds.BLOCK_AND_CHAIN_COLLIDE.get(), 0.125F, this.random.nextFloat());
			this.gameEvent(GameEvent.HIT_GROUND);
		}

		this.setIsReturning(true);

		// riccochet
		double bounce = 0.6;
		this.velX *= bounce;
		this.velY *= bounce;
		this.velZ *= bounce;


		switch (direction) {
			case DOWN:
				if (this.velY > 0) {
					this.velY *= -bounce;
				}
				break;
			case UP:
				if (this.velY < 0) {
					this.velY *= -bounce;
				}
				break;
			case NORTH:
				if (this.velZ > 0) {
					this.velZ *= -bounce;
				}
				break;
			case SOUTH:
				if (this.velZ < 0) {
					this.velZ *= -bounce;
				}
				break;
			case WEST:
				if (this.velX > 0) {
					this.velX *= -bounce;
				}
				break;
			case EAST:
				if (this.velX < 0) {
					this.velX *= -bounce;
				}
				break;
		}
	}

	public static boolean canBreakBlockAt(Level level, BlockPos pos, BlockState state, ItemStack stack, boolean restrictedPlaceMode) {
		return level.getWorldBorder().isWithinBounds(pos) && stack.isCorrectToolForDrops(state) && !state.is(TFBlockTags.BLOCK_AND_CHAIN_NEVER_BREAKS)
			&& (!restrictedPlaceMode || stack.canBreakBlockInAdventureMode(new BlockInWorld(level, pos, false)));
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.level().isClientSide()) {
			if (this.getOwner() == null) {
				this.discard();
			} else {
				if (!this.isReturning()) {
					if (this.xOld != this.getX() || this.zOld != this.getZ()) {
						double d0 = Math.abs(this.getX() - this.xOld);
						double d1 = Math.abs(this.getZ() - this.zOld);
						if (d0 < 0.003F && d1 < 0.003F) {
							this.stuckTime++;
						}
					}

					if (this.stuckTime >= MAX_STUCK_TICKS) {
						this.setIsReturning(true);
					}
				}

				double distToPlayer = this.distanceTo(this.getOwner());
				// return if far enough away
				if (!this.isReturning() && distToPlayer > MAX_CHAIN) {
					this.setIsReturning(true);
				}

				if (this.isReturning()) {
					// despawn if close enough
					if (distToPlayer < 2F) {
						if (this.stack != null && this.getOwner() instanceof LivingEntity living && living.getData(TFDataAttachments.SMASH_BLOCKS).getBlocksSmashed() > 0) {
							this.stack.hurtAndBreak(Math.min(living.getData(TFDataAttachments.SMASH_BLOCKS).getBlocksSmashed(), 3), living, this.getHand());
						}
						this.discard();
					}

					LivingEntity returnTo = (LivingEntity) this.getOwner();

					Vec3 back = new Vec3(returnTo.getX(), returnTo.getY() + returnTo.getEyeHeight(), returnTo.getZ()).subtract(this.position()).normalize();
					float age = Math.min(this.tickCount * 0.03F, 1.0F);

					// separate the return velocity from the normal bouncy velocity
					this.setDeltaMovement(new Vec3(
						this.velX * (1.0 - age) + (back.x() * 2F * age),
						this.velY * (1.0 - age) + (back.y() * 2F * age) - this.getGravity(),
						this.velZ * (1.0 - age) + (back.z() * 2F * age)
					));
				}
			}
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(HAND, true);
		builder.define(IS_FOIL, false);
		builder.define(IS_RETURNING, false);
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		LivingEntity thrower = (LivingEntity) this.getOwner();
		if (thrower != null && thrower.getUseItem().is(TFItems.BLOCK_AND_CHAIN.get())) {
			thrower.stopUsingItem();
		}
	}

	@Override
	protected void readAdditionalSaveData(ValueInput pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.stack = pCompound.read("BlockAndChainStack", ItemStack.CODEC).orElse(ItemStack.EMPTY);
		this.setIsReturning(pCompound.getBooleanOr("IsReturning", false));
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput pCompound) {
		super.addAdditionalSaveData(pCompound);
		if (this.stack != null) {
			pCompound.store("BlockAndChainStack", ItemStack.CODEC, this.stack); //TODO: I don't think we should be checking if this is null, ItemStacks should never be null.
		}
		pCompound.putBoolean("IsReturning", this.isReturning());
	}

	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
		buffer.writeInt(this.getOwner() != null ? this.getOwner().getId() : -1);
		buffer.writeBoolean(this.getHand() == InteractionHand.MAIN_HAND);
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf buf) {
		Entity e = this.level().getEntity(buf.readInt());
		if (e instanceof LivingEntity) {
			this.setOwner(e);
		}
		this.setHand(buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
	}
}
