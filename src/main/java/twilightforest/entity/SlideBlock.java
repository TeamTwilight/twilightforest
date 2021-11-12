package twilightforest.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import twilightforest.TFSounds;
import twilightforest.util.TFDamageSources;

import javax.annotation.Nonnull;
import java.util.List;

public class SlideBlock extends Entity implements IEntityAdditionalSpawnData {

	private static final int WARMUP_TIME = 20;
	private static final EntityDataAccessor<Direction> MOVE_DIRECTION = SynchedEntityData.defineId(SlideBlock.class, EntityDataSerializers.DIRECTION);

	private BlockState myState;
	private int slideTime;

	public SlideBlock(EntityType<? extends SlideBlock> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
		//TODO unsure if this is the right name for it
		this.flyDist = 1F;
	}

	public SlideBlock(EntityType<? extends SlideBlock> type, Level world, double x, double y, double z, BlockState state) {
		super(type, world);

		this.myState = state;
		this.blocksBuilding = true;
		this.flyDist = 1F;
		this.setPos(x, y, z);
		this.setDeltaMovement(new Vec3(0, 0, 0));
		this.xo = x;
		this.yo = y;
		this.zo = z;

		this.determineMoveDirection();
	}

	private void determineMoveDirection() {
		BlockPos pos = new BlockPos(this.blockPosition());

		Direction[] toCheck;

		switch (myState.getValue(RotatedPillarBlock.AXIS)) {
			case X: // horizontal blocks will go up or down if there is a block on one side and air on the other
				toCheck = new Direction[]{Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH};
				break;
			case Z: // horizontal blocks will go up or down if there is a block on one side and air on the other
				toCheck = new Direction[]{Direction.DOWN, Direction.UP, Direction.WEST, Direction.EAST};
				break;
			default:
			case Y: // vertical blocks priority is -x, +x, -z, +z
				toCheck = new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH};
				break;
		}

		for (Direction e : toCheck) {
			if (level.isEmptyBlock(pos.relative(e)) && !level.isEmptyBlock(pos.relative(e.getOpposite()))) {
				entityData.set(MOVE_DIRECTION, e);
				return;
			}
		}

		// if no wall, travel towards open air
		for (Direction e : toCheck) {
			if (level.isEmptyBlock(pos.relative(e))) {
				entityData.set(MOVE_DIRECTION, e);
				return;
			}
		}
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(MOVE_DIRECTION, Direction.DOWN);
	}

	@Override
	public boolean isSteppingCarefully() {
		return false;
	}

	@Override
	public boolean isPickable() {
		return this.isAlive();
	}

	@Override
	public void tick() {
		if (this.myState == null || this.myState.getMaterial() == Material.AIR) {
			this.discard();
		} else {
			this.xo = this.getX();
			this.yo = this.getY();
			this.zo = this.getZ();
			++this.slideTime;
			// start moving after warmup
			if (this.slideTime > WARMUP_TIME) {
				final double moveAcceleration = 0.04;
				Direction moveDirection = entityData.get(MOVE_DIRECTION);
				setDeltaMovement(this.getDeltaMovement().add(moveDirection.getStepX() * moveAcceleration, moveDirection.getStepY() * moveAcceleration, moveDirection.getStepZ() * moveAcceleration));
				this.move(MoverType.SELF, new Vec3(this.getDeltaMovement().x(), this.getDeltaMovement().y(), this.getDeltaMovement().z()));
			}
			this.getDeltaMovement().multiply(0.98, 0.98, 0.98);

			if (!this.level.isClientSide) {
				if (this.slideTime % 5 == 0) {
					playSound(TFSounds.SLIDER, 1.0F, 0.9F + (this.random.nextFloat() * 0.4F));
				}

				BlockPos pos = new BlockPos(this.blockPosition());

				if (this.slideTime == 1) {
					if (this.level.getBlockState(pos) != this.myState) {
						this.discard();
						return;
					}

					this.level.removeBlock(pos, false);
				}

				if (this.slideTime == WARMUP_TIME + 40) {
					this.setDeltaMovement(new Vec3(0, 0, 0));

					entityData.set(MOVE_DIRECTION, entityData.get(MOVE_DIRECTION).getOpposite());
				}

				if (this.verticalCollision || this.horizontalCollision) {
					this.setDeltaMovement(this.getDeltaMovement().multiply(0.699999988079071D, 0.699999988079071D, 0.699999988079071D));

					this.discard();

					if (this.level.isUnobstructed(myState, pos, CollisionContext.empty())) {
						level.setBlockAndUpdate(pos, myState);
					} else {
						// TODO: This and the below item might not be correctly reflecting the state
						this.spawnAtLocation(new ItemStack(myState.getBlock()), 0.0F);
					}
				} else if (this.slideTime > 100 && (pos.getY() < 1 || pos.getY() > 256) || this.slideTime > 600) {
					this.spawnAtLocation(new ItemStack(this.myState.getBlock()), 0.0F);
					this.discard();
				}

				// push things out and damage them
				this.damageKnockbackEntities(this.level.getEntities(this, this.getBoundingBox()));
			}
		}
	}

	private void damageKnockbackEntities(List<Entity> entities) {
		for (Entity entity : entities) {
			if (entity instanceof LivingEntity) {
				entity.hurt(TFDamageSources.SLIDER, 5);

				double kx = (this.getX() - entity.getX()) * 2.0;
				double kz = (this.getZ() - entity.getZ()) * 2.0;

				((LivingEntity) entity).knockback(2, kx, kz);
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(@Nonnull CompoundTag compound) {
		this.slideTime = compound.getInt("Time");
		entityData.set(MOVE_DIRECTION, Direction.from3DDataValue(compound.getByte("Direction")));
	}

	@Override
	protected void addAdditionalSaveData(@Nonnull CompoundTag compound) {
		compound.putInt("Time", this.slideTime);
		compound.putByte("Direction", (byte) entityData.get(MOVE_DIRECTION).get3DDataValue());
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeInt(Block.getId(myState));
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		myState = Block.stateById(additionalData.readInt());
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public BlockState getBlockState() {
		return myState;
	}
}
