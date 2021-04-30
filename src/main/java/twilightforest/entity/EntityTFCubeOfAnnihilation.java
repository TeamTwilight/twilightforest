package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MinecraftVersion;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import twilightforest.client.particle.TFParticleType;
import twilightforest.item.ItemTFCubeOfAnnihilation;
import twilightforest.item.TFItems;
import twilightforest.util.WorldUtil;

import java.util.Random;

import static twilightforest.TwilightForestMod.prefix;

public class EntityTFCubeOfAnnihilation extends ThrowableEntity {

	private static final ITag.INamedTag<Block> WHITELIST = BlockTags.makeWrapperTag(prefix("annihilation_whitelist").toString());
	private boolean hasHitObstacle = false;

	public EntityTFCubeOfAnnihilation(EntityType<? extends EntityTFCubeOfAnnihilation> type, World world) {
		super(type, world);
		this.isImmuneToFire();
	}

	public EntityTFCubeOfAnnihilation(EntityType<? extends EntityTFCubeOfAnnihilation> type, World world, LivingEntity thrower) {
		super(type, thrower, world);
		this.isImmuneToFire();
		this.func_234612_a_(thrower, thrower.rotationPitch, thrower.rotationYaw, 0F, 1.5F, 1F);
	}

	@Override
	protected void registerData() {
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

	@Override
	protected void onImpact(RayTraceResult ray) {
		if (world.isRemote)
			return;

		// only hit living things
		if (ray instanceof EntityRayTraceResult) {
			if (((EntityRayTraceResult) ray).getEntity() instanceof LivingEntity && ((EntityRayTraceResult) ray).getEntity().attackEntityFrom(this.getDamageSource(), 10)) {
				this.ticksExisted += 60;
			}
		}

		if (ray instanceof BlockRayTraceResult) {
			if (((BlockRayTraceResult)ray).getPos() != null && !this.world.isAirBlock(((BlockRayTraceResult)ray).getPos())) {
				this.affectBlocksInAABB(this.getBoundingBox().grow(0.2F, 0.2F, 0.2F));
			}
		}
	}

	private DamageSource getDamageSource() {
		LivingEntity thrower = (LivingEntity) this.func_234616_v_();
		if (thrower instanceof PlayerEntity) {
			return DamageSource.causePlayerDamage((PlayerEntity) thrower);
		} else if (thrower != null) {
			return DamageSource.causeMobDamage(thrower);
		} else {
			return DamageSource.causeThrownDamage(this, null);
		}
	}

	private void affectBlocksInAABB(AxisAlignedBB box) {
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			BlockState state = world.getBlockState(pos);
			if (!state.getBlock().isAir(state, world, pos)) {
				if (canAnnihilate(pos, state)) {
					this.world.removeBlock(pos, false);
					this.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.125f, this.rand.nextFloat() * 0.25F + 0.75F);
					this.annihilateParticles(world, pos);
				} else {
					this.hasHitObstacle = true;
				}
			}
		}
	}

	private boolean canAnnihilate(BlockPos pos, BlockState state) {
		// whitelist many castle blocks
		Block block = state.getBlock();
		return block.isIn(WHITELIST) || block.getExplosionResistance() < 8F && state.getBlockHardness(world, pos) >= 0;
	}

	private void annihilateParticles(World world, BlockPos pos) {
		Random rand = world.getRandom();
		if(world instanceof ServerWorld) {
			for (int dx = 0; dx < 3; ++dx) {
				for (int dy = 0; dy < 3; ++dy) {
					for (int dz = 0; dz < 3; ++dz) {

						double x = pos.getX() + (dx + 0.5D) / 4;
						double y = pos.getY() + (dy + 0.5D) / 4;
						double z = pos.getZ() + (dz + 0.5D) / 4;

						double speed = rand.nextGaussian() * 0.2D;

						((ServerWorld)world).spawnParticle(TFParticleType.ANNIHILATE.get(), x, y, z, 1, 0, 0, 0, speed);
					}
				}
			}
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.world.isRemote) {
			if (this.func_234616_v_() == null) {
				this.remove();
				return;
			}

			// always head towards either the point or towards the player
			Vector3d destPoint = new Vector3d(this.func_234616_v_().getPosX(), this.func_234616_v_().getPosY() + this.func_234616_v_().getEyeHeight(), this.func_234616_v_().getPosZ());

			double distToPlayer = this.getDistance(this.func_234616_v_());

			if (this.isReturning()) {
				// if we are returning, and are near enough to the player, then we are done
				if (distToPlayer < 2F) {
					this.remove();
				}
			} else {
				destPoint = destPoint.add(func_234616_v_().getLookVec().scale(16F));
			}

			// set motions
			Vector3d velocity = new Vector3d(this.getPosX() - destPoint.getX(), (this.getPosY() + this.getHeight() / 2F) - destPoint.getY(), this.getPosZ() - destPoint.getZ());

			setMotion(-velocity.getX(), -velocity.getY(), -velocity.getZ());

			// normalize speed
			float currentSpeed = MathHelper.sqrt(this.getMotion().getX() * this.getMotion().getX() + this.getMotion().getY() * this.getMotion().getY() + this.getMotion().getZ() * this.getMotion().getZ());

			float maxSpeed = 0.5F;

			if (currentSpeed > maxSpeed) {
				this.setMotion(new Vector3d(
						this.getMotion().getX() / (currentSpeed / maxSpeed),
						this.getMotion().getY() / (currentSpeed / maxSpeed),
						this.getMotion().getZ() / (currentSpeed / maxSpeed)));
			} else {
				float slow = 0.5F;
				this.getMotion().mul(slow, slow, slow);
			}

			// demolish some blocks
			this.affectBlocksInAABB(this.getBoundingBox().grow(0.2F, 0.2F, 0.2F));
		}
	}

	@Override
	public void remove() {
		super.remove();
		LivingEntity thrower = (LivingEntity) this.func_234616_v_();
		if (thrower != null && thrower.getActiveItemStack().getItem() == TFItems.cube_of_annihilation.get()) {
			thrower.resetActiveHand();
		}
	}

	private boolean isReturning() {
		if (this.hasHitObstacle || this.func_234616_v_() == null || !(this.func_234616_v_() instanceof PlayerEntity)) {
			return true;
		} else {
			PlayerEntity player = (PlayerEntity) this.func_234616_v_();
			return !player.isHandActive();
		}
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
