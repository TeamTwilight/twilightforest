package twilightforest.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.network.NetworkHooks;
import twilightforest.TFSounds;
import twilightforest.block.MazestoneBlock;
import twilightforest.enchantment.TFEnchantments;
import twilightforest.entity.monster.BlockChainGoblin;
import twilightforest.item.TFItems;
import twilightforest.util.TFDamageSources;
import twilightforest.util.WorldUtil;

public class ChainBlock extends ThrowableProjectile implements IEntityAdditionalSpawnData {

	private int MAX_SMASH;
	private static final int MAX_CHAIN = 16;

	private static final EntityDataAccessor<Boolean> HAND = SynchedEntityData.defineId(ChainBlock.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> IS_FOIL = SynchedEntityData.defineId(ChainBlock.class, EntityDataSerializers.BOOLEAN);
	private boolean isReturning = false;
	private boolean ignoreBlocks;
	private ItemStack stack;
	private int blocksSmashed = 0;
	private double velX;
	private double velY;
	private double velZ;

	public final Chain chain1;
	public final Chain chain2;
	public final Chain chain3;
	public final Chain chain4;
	public final Chain chain5;
	private final BlockChainGoblin.MultipartGenericsAreDumb[] partsArray;

	public ChainBlock(EntityType<? extends ChainBlock> type, Level world) {
		super(type, world);

		chain1 = new Chain(this);
		chain2 = new Chain(this);
		chain3 = new Chain(this);
		chain4 = new Chain(this);
		chain5 = new Chain(this);
		partsArray =  new BlockChainGoblin.MultipartGenericsAreDumb[]{ chain1, chain2, chain3, chain4, chain5 };
	}

	public ChainBlock(EntityType<? extends ChainBlock> type, Level world, LivingEntity thrower, InteractionHand hand, ItemStack stack) {
		super(type, thrower, world);
		this.isReturning = false;
		this.ignoreBlocks = EnchantmentHelper.getItemEnchantmentLevel(TFEnchantments.PRESERVATION.get(), stack) > 0;
		MAX_SMASH = 12 + (EnchantmentHelper.getItemEnchantmentLevel(TFEnchantments.DESTRUCTION.get(), stack) * 10);
		this.stack = stack;
		this.setHand(hand);
		chain1 = new Chain(this);
		chain2 = new Chain(this);
		chain3 = new Chain(this);
		chain4 = new Chain(this);
		chain5 = new Chain(this);
		partsArray =  new BlockChainGoblin.MultipartGenericsAreDumb[]{ chain1, chain2, chain3, chain4, chain5 };
		this.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0F, 1.5F, 1F);
		this.entityData.set(IS_FOIL, stack.hasFoil());
	}

	private void setHand(InteractionHand hand) {
		entityData.set(HAND, hand == InteractionHand.MAIN_HAND);
	}

	public InteractionHand getHand() {
		return entityData.get(HAND) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
	}

	public boolean isFoil() {
		return this.entityData.get(IS_FOIL);
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
		if (!level.isClientSide && result.getEntity() instanceof LivingEntity && result.getEntity() != this.getOwner()) {
			if (result.getEntity().hurt(TFDamageSources.spiked(this, (LivingEntity)this.getOwner()), 10)) {
				playSound(TFSounds.BLOCKCHAIN_HIT, 1.0f, this.random.nextFloat());
				// age when we hit a monster so that we go back to the player faster
				this.tickCount += 60;
			}
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!level.isClientSide && !this.level.isEmptyBlock(result.getBlockPos())) {

			if (!this.isReturning) {
				playSound(TFSounds.BLOCKCHAIN_COLLIDE, 0.125f, this.random.nextFloat());
			}

			if (this.blocksSmashed < MAX_SMASH) {
				if (this.level.getBlockState(result.getBlockPos()).getDestroySpeed(level, result.getBlockPos()) < 0.0F ||
						this.level.getBlockState(result.getBlockPos()).getDestroySpeed(level, result.getBlockPos()) > 0.3F) {
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

				if(!ignoreBlocks) {
					// demolish some blocks
					this.affectBlocksInAABB(this.getBoundingBox().inflate(0.5D + (EnchantmentHelper.getItemEnchantmentLevel(TFEnchantments.DESTRUCTION.get(), stack) * 0.5)));
				}
			}

			this.isReturning = true;

			// if we have smashed enough, add to ticks so that we go back faster
			if (this.blocksSmashed > MAX_SMASH && this.tickCount < 60) {
				this.tickCount += 60;
			}
		}
	}

	private void affectBlocksInAABB(AABB box) {
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			BlockState state = level.getBlockState(pos);
			Block block = state.getBlock();

			// TODO: The "explosion" parameter can't actually be null
			if (!state.isAir() && block.getExplosionResistance(state, level, pos, null) < (15F + (EnchantmentHelper.getItemEnchantmentLevel(TFEnchantments.BLOCK_STRENGTH.get(), stack) * 20F))
					&& state.getDestroySpeed(level, pos) >= 0 && block.canEntityDestroy(state, level, pos, this) && !(block instanceof MazestoneBlock)) {

				if (getOwner() instanceof Player player) {
					if (!MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(level, pos, state, player))) {
						if (ForgeEventFactory.doPlayerHarvestCheck(player, state, !state.requiresCorrectToolForDrops() || player.getItemInHand(getHand()).isCorrectToolForDrops(state))) {
							block.playerDestroy(level, player, pos, state, level.getBlockEntity(pos), player.getItemInHand(getHand()));

							level.destroyBlock(pos, false);
							this.blocksSmashed++;
						}
					}
				}
			}
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (level.isClientSide) {

			chain1.tick();
			chain2.tick();
			chain3.tick();
			chain4.tick();
			chain5.tick();

			// set chain positions
			if (this.getOwner() != null) {
				// interpolate chain position
				Vec3 handVec = this.getOwner().getLookAngle().yRot(getHand() == InteractionHand.MAIN_HAND ? -0.4F : 0.4F);

				double sx = this.getOwner().getX() + handVec.x;
				double sy = this.getOwner().getY() + handVec.y - 0.4F + this.getOwner().getEyeHeight();
				double sz = this.getOwner().getZ() + handVec.z;

				double ox = sx - this.getX();
				double oy = sy - this.getY() - 0.25F;
				double oz = sz - this.getZ();

				this.chain1.setPos(sx - ox * 0.05, sy - oy * 0.05, sz - oz * 0.05);
				this.chain2.setPos(sx - ox * 0.25, sy - oy * 0.25, sz - oz * 0.25);
				this.chain3.setPos(sx - ox * 0.45, sy - oy * 0.45, sz - oz * 0.45);
				this.chain4.setPos(sx - ox * 0.65, sy - oy * 0.65, sz - oz * 0.65);
				this.chain5.setPos(sx - ox * 0.85, sy - oy * 0.85, sz - oz * 0.85);
			}
		} else {
			if (getOwner() == null) {
				discard();
			} else {
				double distToPlayer = this.distanceTo(this.getOwner());
				// return if far enough away
				if (!this.isReturning && distToPlayer > MAX_CHAIN) {
					this.isReturning = true;
				}

				if (this.isReturning) {
					// despawn if close enough
					if (distToPlayer < 2F) {
						this.discard();
					}

					LivingEntity returnTo = (LivingEntity) this.getOwner();

					Vec3 back = new Vec3(returnTo.getX(), returnTo.getY() + returnTo.getEyeHeight(), returnTo.getZ()).subtract(this.position()).normalize();
					float age = Math.min(this.tickCount * 0.03F, 1.0F);

					// separate the return velocity from the normal bouncy velocity
					this.setDeltaMovement(new Vec3(
							this.velX * (1.0 - age) + (back.x * 2F * age),
							this.velY * (1.0 - age) + (back.y * 2F * age) - this.getGravity(),
							this.velZ * (1.0 - age) + (back.z * 2F * age)
					));
				}
			}
		}
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(HAND, true);
		entityData.define(IS_FOIL, false);
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		LivingEntity thrower = (LivingEntity) this.getOwner();
		if (thrower != null && thrower.getUseItem().getItem() == TFItems.BLOCK_AND_CHAIN.get()) {
			thrower.stopUsingItem();
		}
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeInt(getOwner() != null ? getOwner().getId() : -1);
		buffer.writeBoolean(getHand() == InteractionHand.MAIN_HAND);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		Entity e = level.getEntity(additionalData.readInt());
		if (e instanceof LivingEntity) {
			setOwner(e);
		}
		setHand(additionalData.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
