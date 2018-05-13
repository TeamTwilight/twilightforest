package twilightforest.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import twilightforest.item.TFItems;
import twilightforest.util.WorldUtil;


public class EntityTFChainBlock extends EntityThrowable implements IEntityMultiPart, IEntityAdditionalSpawnData {
	private static final int MAX_SMASH = 12;
	private static final int MAX_CHAIN = 16;

	private EnumHand hand = EnumHand.MAIN_HAND;
	private boolean isReturning = false;
	private int blocksSmashed = 0;
	private double velX;
	private double velY;
	private double velZ;

	public final EntityTFGoblinChain chain1 = new EntityTFGoblinChain(this);
	public final EntityTFGoblinChain chain2 = new EntityTFGoblinChain(this);
	public final EntityTFGoblinChain chain3 = new EntityTFGoblinChain(this);
	public final EntityTFGoblinChain chain4 = new EntityTFGoblinChain(this);
	public final EntityTFGoblinChain chain5 = new EntityTFGoblinChain(this);
	private final Entity[] partsArray = { chain1, chain2, chain3, chain4, chain5 };

	public EntityTFChainBlock(World par1World) {
		super(par1World);
		this.setSize(0.6F, 0.6F);
	}

	public EntityTFChainBlock(World world, EntityLivingBase thrower, EnumHand hand) {
		super(world, thrower);
		this.setSize(0.6F, 0.6F);
		this.isReturning = false;
		this.hand = hand;
		this.setHeadingFromThrower(thrower, thrower.rotationPitch, thrower.rotationYaw, 0F, 1.5F, 1F);
	}

	@Override
	public void setThrowableHeading(double x, double y, double z, float speed, float accuracy) {
		super.setThrowableHeading(x, y, z, speed, accuracy);

		// save velocity
		this.velX = this.motionX;
		this.velY = this.motionY;
		this.velZ = this.motionZ;
	}

	@Override
	protected float getGravityVelocity() {
		return 0.05F;
	}

	@Override
	protected void onImpact(RayTraceResult mop) {
		if (world.isRemote) {
			return;
		}

		// only hit living things
		if (mop.entityHit instanceof EntityLivingBase && mop.entityHit != this.getThrower()) {
			if (mop.entityHit.attackEntityFrom(this.getDamageSource(), 10)) {
				// age when we hit a monster so that we go back to the player faster
				this.ticksExisted += 60;
			}
		}

		if (mop.getBlockPos() != null && !this.world.isAirBlock(mop.getBlockPos())) {
			if (!this.isReturning) {
				playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.125f, this.rand.nextFloat());
			}

			if (this.blocksSmashed < MAX_SMASH) {
				if (this.world.getBlockState(mop.getBlockPos()).getBlockHardness(world, mop.getBlockPos()) > 0.3F) {
					// riccochet
					double bounce = 0.6;
					this.velX *= bounce;
					this.velY *= bounce;
					this.velZ *= bounce;


					switch (mop.sideHit) {
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

				// demolish some blocks
				this.affectBlocksInAABB(this.getEntityBoundingBox());
			}

			this.isReturning = true;

			// if we have smashed enough, add to ticks so that we go back faster
			if (this.blocksSmashed > MAX_SMASH && this.ticksExisted < 60) {
				this.ticksExisted += 60;
			}
		}
	}

	private DamageSource getDamageSource() {
		EntityLivingBase thrower = this.getThrower();
		if (thrower instanceof EntityPlayer) {
			return DamageSource.causePlayerDamage((EntityPlayer) thrower);
		} else if (thrower != null) {
			return DamageSource.causeMobDamage(thrower);
		} else {
			return DamageSource.causeThrownDamage(this, null);
		}
	}

	private void affectBlocksInAABB(AxisAlignedBB box) {
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();

			if (block != Blocks.AIR && block.getExplosionResistance(this) < 7F && state.getBlockHardness(world, pos) >= 0) {

				if (getThrower() instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) getThrower();

					if (ForgeHooks.canHarvestBlock(block, player, world, pos)) {
						block.harvestBlock(this.world, player, pos, state, world.getTileEntity(pos), player.getHeldItem(hand));
					}
				}

				world.destroyBlock(pos, false);
				this.blocksSmashed++;
			}
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (world.isRemote) {
			chain1.onUpdate();
			chain2.onUpdate();
			chain3.onUpdate();
			chain4.onUpdate();
			chain5.onUpdate();

			// set chain positions
			if (this.getThrower() != null) {
				// interpolate chain position
				Vec3d handVec = this.getThrower().getLookVec().rotateYaw(hand == EnumHand.MAIN_HAND ? -0.4F : 0.4F);

				double sx = this.getThrower().posX + handVec.x;
				double sy = this.getThrower().posY + handVec.y - 0.4F + this.getThrower().getEyeHeight();
				double sz = this.getThrower().posZ + handVec.z;

				double ox = sx - this.posX;
				double oy = sy - this.posY - 0.25F;
				double oz = sz - this.posZ;

				this.chain1.setPosition(sx - ox * 0.05, sy - oy * 0.05, sz - oz * 0.05);
				this.chain2.setPosition(sx - ox * 0.25, sy - oy * 0.25, sz - oz * 0.25);
				this.chain3.setPosition(sx - ox * 0.45, sy - oy * 0.45, sz - oz * 0.45);
				this.chain4.setPosition(sx - ox * 0.65, sy - oy * 0.65, sz - oz * 0.65);
				this.chain5.setPosition(sx - ox * 0.85, sy - oy * 0.85, sz - oz * 0.85);
			}
		} else {
			if (getThrower() == null) {
				setDead();
			} else {
				double distToPlayer = this.getDistance(this.getThrower().posX, this.getThrower().posY + this.getThrower().getEyeHeight(), this.getThrower().posZ);
				// return if far enough away
				if (!this.isReturning && distToPlayer > MAX_CHAIN) {
					this.isReturning = true;
				}

				if (this.isReturning) {
					// despawn if close enough
					if (distToPlayer < 2F) {
						this.setDead();
					}

					EntityLivingBase returnTo = this.getThrower();

					Vec3d back = new Vec3d(returnTo.posX, returnTo.posY + returnTo.getEyeHeight(), returnTo.posZ).subtract(this.getPositionVector()).normalize();
					float age = Math.min(this.ticksExisted * 0.03F, 1.0F);

					// separate the return velocity from the normal bouncy velocity
					this.motionX = this.velX * (1.0 - age) + (back.x * 2F * age);
					this.motionY = this.velY * (1.0 - age) + (back.y * 2F * age) - this.getGravityVelocity();
					this.motionZ = this.velZ * (1.0 - age) + (back.z * 2F * age);
				}
			}
		}
	}

	@Override
	public void setDead() {
		super.setDead();
		EntityLivingBase thrower = this.getThrower();
		if (thrower != null && thrower.getActiveItemStack().getItem() == TFItems.block_and_chain) {
			thrower.resetActiveHand();
		}
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart p_70965_1_, DamageSource p_70965_2_, float p_70965_3_) {
		return false;
	}

	@Override
	public Entity[] getParts() {
		return partsArray;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(getThrower() != null ? getThrower().getEntityId() : -1);
		buffer.writeBoolean(hand == EnumHand.MAIN_HAND);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		Entity e = world.getEntityByID(additionalData.readInt());
		if (e instanceof EntityLivingBase) {
			thrower = (EntityLivingBase) e;
		}
		hand = additionalData.readBoolean() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
	}
}
