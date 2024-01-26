package twilightforest.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.ToolActions;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.level.BlockEvent;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEnchantments;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.util.WorldUtil;

public class ChainBlock extends ThrowableProjectile implements IEntityWithComplexSpawn {

	private static final int MAX_SMASH = 12;
	private static final int MAX_CHAIN = 16;

	private static final EntityDataAccessor<Boolean> HAND = SynchedEntityData.defineId(ChainBlock.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> IS_FOIL = SynchedEntityData.defineId(ChainBlock.class, EntityDataSerializers.BOOLEAN);
	private boolean isReturning = false;
	private boolean canSmashBlocks;
	private boolean hitEntity = false;
	private ItemStack stack;
	private int blocksSmashed = 0;
	private double velX;
	private double velY;
	private double velZ;

	public ChainBlock(EntityType<? extends ChainBlock> type, Level world) {
		super(type, world);
	}

	public ChainBlock(EntityType<? extends ChainBlock> type, Level world, LivingEntity thrower, InteractionHand hand, ItemStack stack) {
		super(type, thrower, world);
		this.isReturning = false;
		this.canSmashBlocks = EnchantmentHelper.getTagEnchantmentLevel(TFEnchantments.DESTRUCTION.get(), stack) > 0 && !thrower.hasEffect(MobEffects.DIG_SLOWDOWN);
		this.stack = stack;
		this.setHand(hand);
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

	@Override
	public boolean canChangeDimensions() {
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
	protected float getGravity() {
		return 0.05F;
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		// only hit living things
		if (!this.level().isClientSide() && result.getEntity() != this.getOwner()) {
			float damage = 0.0F;
			if (result.getEntity() instanceof LivingEntity living) {
				damage = 10 + EnchantmentHelper.getDamageBonus(this.stack, living.getMobType());
			} else if (result.getEntity() instanceof PartEntity<?> part && part.getParent() instanceof LivingEntity living) {
				damage = 10 + EnchantmentHelper.getDamageBonus(this.stack, living.getMobType());
			}

			//properly disable shields
			if (result.getEntity() instanceof Player player && player.isUsingItem() && player.getUseItem().canPerformAction(ToolActions.SHIELD_BLOCK)) {
				player.getUseItem().hurtAndBreak(5, player, event -> event.broadcastBreakEvent(player.getUsedItemHand()));
				player.disableShield(true);
			}

			if (damage > 0.0F) {
				if (result.getEntity().hurt(TFDamageTypes.getIndirectEntityDamageSource(this.level(), TFDamageTypes.SPIKED, this, this.getOwner()), damage)) {
					this.playSound(TFSounds.BLOCK_AND_CHAIN_HIT.get(), 1.0f, this.random.nextFloat());
					// age when we hit a monster so that we go back to the player faster
					this.hitEntity = true;
					this.isReturning = true;
					this.tickCount += 60;
					if (this.getOwner() instanceof LivingEntity living) {
						this.stack.hurtAndBreak(1, living, user -> user.broadcastBreakEvent(this.getHand()));
					}
				}
			}
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		BlockPos pos = result.getBlockPos();
		Level level = this.level();
		if (!level.isClientSide()) {
			BlockState state = level.getBlockState(pos);
			boolean restrictedPlaceMode = this.getOwner() instanceof ServerPlayer player && player.gameMode.getGameModeForPlayer().isBlockPlacingRestricted();
			if (!state.isAir() && !this.canBreakBlockAt(pos, state, restrictedPlaceMode)) {
				if (!this.isReturning && !this.hitEntity) {
					this.playSound(TFSounds.BLOCK_AND_CHAIN_COLLIDE.get(), 0.125f, this.random.nextFloat());
					this.gameEvent(GameEvent.HIT_GROUND);
				}

				this.isReturning = true;

				// riccochet
				double bounce = 0.6;
				this.velX *= bounce;
				this.velY *= bounce;
				this.velZ *= bounce;


				switch (result.getDirection()) {
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

			if (this.canSmashBlocks) {
				// demolish some blocks
				AABB aabb = this.getBoundingBox().inflate(0.25D);
				//I don't know why, and I can't find out why, but we have to subtract the coordinates by 0.5 here.
				//Otherwise, it will sometimes not break blocks when hitting west, and always break too many when hitting east.
				this.affectBlocksInAABB(aabb.move(result.getLocation().subtract(aabb.getCenter()).add(-0.5D, 0D, 0D)));
			}
		}

		// if we have smashed enough, add to ticks so that we go back faster
		if (this.blocksSmashed > MAX_SMASH) {
			this.isReturning = true;
			if (this.tickCount < 60) {
				this.tickCount += 60;
			}
		}
	}

	private boolean canBreakBlockAt(BlockPos pos, BlockState state, boolean restrictedPlaceMode) {
		return this.stack.isCorrectToolForDrops(state)
				&& (!restrictedPlaceMode || this.stack.hasAdventureModeBreakTagForBlock(this.level().registryAccess().registryOrThrow(Registries.BLOCK), new BlockInWorld(this.level(), pos, false)));
	}

	private void affectBlocksInAABB(AABB box) {
		if (this.getOwner() instanceof ServerPlayer player) {
			boolean creative = player.getAbilities().instabuild;

			for (BlockPos pos : WorldUtil.getAllInBB(box)) {
				BlockState state = this.level().getBlockState(pos);
				Block block = state.getBlock();

				if (!state.isAir() && this.canBreakBlockAt(pos, state, player.gameMode.getGameModeForPlayer().isBlockPlacingRestricted()) && block.canEntityDestroy(state, this.level(), pos, this)) {
					if (!NeoForge.EVENT_BUS.post(new BlockEvent.BreakEvent(this.level(), pos, state, player)).isCanceled()) {
						if (EventHooks.doPlayerHarvestCheck(player, state, !state.requiresCorrectToolForDrops() || player.getItemInHand(this.getHand()).isCorrectToolForDrops(state))) {
							this.level().destroyBlock(pos, false);
							if (!creative) block.playerDestroy(this.level(), player, pos, state, this.level().getBlockEntity(pos), player.getItemInHand(this.getHand()));
							this.blocksSmashed++;
							if (this.blocksSmashed > MAX_SMASH) {
								break;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.level().isClientSide()) {
			if (this.getOwner() == null) {
				this.discard();
			} else {
				double distToPlayer = this.distanceTo(this.getOwner());
				// return if far enough away
				if (!this.isReturning && distToPlayer > MAX_CHAIN) {
					this.isReturning = true;
				}

				if (this.isReturning) {
					// despawn if close enough
					if (distToPlayer < 2F) {
						if (this.getOwner() instanceof LivingEntity living && this.blocksSmashed > 0) {
							this.stack.hurtAndBreak(Math.min(this.blocksSmashed, 3), living, user -> user.broadcastBreakEvent(this.getHand()));
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
	protected void defineSynchedData() {
		this.getEntityData().define(HAND, true);
		this.getEntityData().define(IS_FOIL, false);
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		LivingEntity thrower = (LivingEntity) this.getOwner();
		if (thrower != null && thrower.getUseItem().is(TFItems.BLOCK_AND_CHAIN)) {
			thrower.stopUsingItem();
		}
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		if (pCompound.contains("BlockAndChainStack", 10)) {
			this.stack = ItemStack.of(pCompound.getCompound("BlockAndChainStack"));
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.put("BlockAndChainStack", this.stack.save(new CompoundTag()));
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeInt(this.getOwner() != null ? this.getOwner().getId() : -1);
		buffer.writeBoolean(this.getHand() == InteractionHand.MAIN_HAND);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf buf) {
		Entity e = this.level().getEntity(buf.readInt());
		if (e instanceof LivingEntity) {
			this.setOwner(e);
		}
		this.setHand(buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
	}
}
