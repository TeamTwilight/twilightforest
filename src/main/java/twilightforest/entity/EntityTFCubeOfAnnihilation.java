package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import twilightforest.TFPacketHandler;
import twilightforest.block.BlockTFCastleMagic;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;
import twilightforest.network.PacketAnnihilateBlock;
import twilightforest.util.WorldUtil;

import java.util.List;


public class EntityTFCubeOfAnnihilation extends EntityThrowable {

	private boolean hasHitObstacle = false;

	public EntityTFCubeOfAnnihilation(World par1World) {
		super(par1World);
		this.setSize(1F, 1F);
		this.isImmuneToFire = true;
	}

	public EntityTFCubeOfAnnihilation(World world, EntityLivingBase thrower) {
		super(world, thrower);
		this.setSize(1F, 1F);
		this.isImmuneToFire = true;
		this.setHeadingFromThrower(thrower, thrower.rotationPitch, thrower.rotationYaw, 0F, 1.5F, 1F);
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

	@Override
	protected void onImpact(RayTraceResult mop) {
		if (world.isRemote)
			return;

		// only hit living things
		if (mop.entityHit instanceof EntityLivingBase && mop.entityHit.attackEntityFrom(this.getDamageSource(), 10)) {
			this.ticksExisted += 60;
		}

		if (mop.getBlockPos() != null && !this.world.isAirBlock(mop.getBlockPos())) {
			this.affectBlocksInAABB(this.getEntityBoundingBox().grow(0.2F, 0.2F, 0.2F));
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
			if (!state.getBlock().isAir(state, world, pos)) {
				if (canAnnihilate(pos, state)) {
					this.world.setBlockToAir(pos);
					this.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.125f, this.rand.nextFloat() * 0.25F + 0.75F);
					this.sendAnnihilateBlockPacket(world, pos);
				} else {
					this.hasHitObstacle = true;
				}
			}
		}
	}

	private boolean canAnnihilate(BlockPos pos, IBlockState state) {
		// whitelist many castle blocks
		Block block = state.getBlock();
		if (block == TFBlocks.deadrock || block == TFBlocks.castle_brick || (block == TFBlocks.castle_rune_brick && state.getValue(BlockTFCastleMagic.COLOR) != EnumDyeColor.PURPLE) || block == TFBlocks.force_field || block == TFBlocks.thorns) {
			return true;
		}

		return block.getExplosionResistance(this) < 8F && state.getBlockHardness(world, pos) >= 0;
	}


	private void sendAnnihilateBlockPacket(World world, BlockPos pos) {
		// send packet
		IMessage message = new PacketAnnihilateBlock(pos);

		NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64);

		TFPacketHandler.CHANNEL.sendToAllAround(message, targetPoint);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!this.world.isRemote) {
			if (this.getThrower() == null) {
				this.setDead();
				return;
			}

			// always head towards either the point or towards the player
			Vec3d destPoint = new Vec3d(this.getThrower().posX, this.getThrower().posY + this.getThrower().getEyeHeight(), this.getThrower().posZ);

			if (this.isReturning()) {
				// if we are returning, and are near enough to the player, then we are done
				List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().offset(this.motionX, this.motionY, this.motionZ).grow(1.0D, 1.0D, 1.0D));

				if (list.contains(this.getThrower())) {
					this.setDead();
				}
			} else {
				destPoint = destPoint.add(getThrower().getLookVec().scale(16F));
			}

			// set motions
			Vec3d velocity = new Vec3d(this.posX - destPoint.x, (this.posY + this.height / 2F) - destPoint.y, this.posZ - destPoint.z);

			this.motionX -= velocity.x;
			this.motionY -= velocity.y;
			this.motionZ -= velocity.z;

			// normalize speed
			float currentSpeed = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);

			float maxSpeed = 0.5F;


			if (currentSpeed > maxSpeed) {
				this.motionX /= currentSpeed / maxSpeed;
				this.motionY /= currentSpeed / maxSpeed;
				this.motionZ /= currentSpeed / maxSpeed;
			} else {
				float slow = 0.5F;
				this.motionX *= slow;
				this.motionY *= slow;
				this.motionZ *= slow;
			}

			// demolish some blocks
			this.affectBlocksInAABB(this.getEntityBoundingBox().grow(0.2F, 0.2F, 0.2F));
		}
	}

	@Override
	public void setDead() {
		super.setDead();
		EntityLivingBase thrower = this.getThrower();
		if (thrower != null && thrower.getActiveItemStack().getItem() == TFItems.cube_of_annihilation) {
			thrower.resetActiveHand();
		}
	}

	private boolean isReturning() {
		if (this.hasHitObstacle || this.getThrower() == null || !(this.getThrower() instanceof EntityPlayer)) {
			return true;
		} else {
			EntityPlayer player = (EntityPlayer) this.getThrower();
			return !player.isHandActive();
		}
	}
}
