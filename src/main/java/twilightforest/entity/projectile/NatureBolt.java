package twilightforest.entity.projectile;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.TFEntities;
import twilightforest.util.TFDamageSources;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class NatureBolt extends TFThrowable implements ITFProjectile, ItemSupplier {

	public NatureBolt(EntityType<? extends NatureBolt> type, Level world) {
		super(type, world);
	}

	public NatureBolt(Level world, LivingEntity owner) {
		super(TFEntities.NATURE_BOLT, world, owner);
	}

	@Override
	public void tick() {
		super.tick();
		makeTrail();
	}

	@Override
	protected float getGravity() {
		return 0.003F;
	}

	private void makeTrail() {
		for (int i = 0; i < 5; i++) {
			double dx = getX() + 0.5 * (random.nextDouble() - random.nextDouble());
			double dy = getY() + 0.5 * (random.nextDouble() - random.nextDouble());
			double dz = getZ() + 0.5 * (random.nextDouble() - random.nextDouble());
			level.addParticle(ParticleTypes.HAPPY_VILLAGER, dx, dy, dz, 0.0D, 0.0D, 0.0D);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OAK_LEAVES.defaultBlockState()), false, this.getX(), this.getY(), this.getZ(), random.nextGaussian() * 0.05D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.05D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void onHit(HitResult ray) {
		if (!this.level.isClientSide) {
			if (ray.getType() == HitResult.Type.BLOCK) {
				BlockPos blockPosHit = ((BlockHitResult) ray).getBlockPos();
				Material materialHit = level.getBlockState(blockPosHit).getMaterial();

				if (materialHit == Material.GRASS) {
					ItemStack dummy = new ItemStack(Items.BONE_MEAL, 1);
					if (BoneMealItem.growCrop(dummy, level, blockPosHit)) {
						level.levelEvent(2005, blockPosHit, 0);
					}
				} else if (materialHit.isSolid() && canReplaceBlock(level, blockPosHit)) {
					level.setBlockAndUpdate(blockPosHit, Blocks.BIRCH_LEAVES.defaultBlockState());
				}
			}

			if (ray instanceof EntityHitResult) {
				Entity owner = getOwner();
				Entity entityHit = ((EntityHitResult) ray).getEntity();
				if (entityHit instanceof LivingEntity && (owner == null || (entityHit != owner && entityHit != owner.getVehicle()))) {
					if (entityHit.hurt(TFDamageSources.leafBrain(this, (LivingEntity)this.getOwner()), 2)
							&& level.getDifficulty() != Difficulty.PEACEFUL) {
						int poisonTime = level.getDifficulty() == Difficulty.HARD ? 7 : 3;
						((LivingEntity) entityHit).addEffect(new MobEffectInstance(MobEffects.POISON, poisonTime * 20, 0));
					}
				}
			}

			this.level.broadcastEntityEvent(this, (byte) 3);
			this.discard();
		}
	}

	private boolean canReplaceBlock(Level world, BlockPos pos) {
		float hardness = world.getBlockState(pos).getDestroySpeed(world, pos);
		return hardness >= 0 && hardness < 50F;
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.WHEAT_SEEDS);
	}
}
