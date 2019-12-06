package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityTFNatureBolt extends EntityTFThrowable implements ITFProjectile {

	public EntityTFNatureBolt(World world) {
		super(world);
	}

	public EntityTFNatureBolt(World world, LivingEntity thrower) {
		super(world, thrower);
	}

	@Override
	public void tick() {
		super.tick();
		makeTrail();
	}

	@Override
	protected float getGravityVelocity() {
		return 0.003F;
	}

	private void makeTrail() {
		for (int i = 0; i < 5; i++) {
			double dx = posX + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dy = posY + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dz = posZ + 0.5 * (rand.nextDouble() - rand.nextDouble());
			world.addParticle(ParticleTypes.HAPPY_VILLAGER, dx, dy, dz, 0.0D, 0.0D, 0.0D);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			int stateId = Block.getStateId(Blocks.OAK_LEAVES.getDefaultState());
			for (int i = 0; i < 8; ++i) {
				this.world.addParticle(EnumParticleTypes.BLOCK_CRACK, this.posX, this.posY, this.posZ, rand.nextGaussian() * 0.05D, rand.nextDouble() * 0.2D, rand.nextGaussian() * 0.05D, stateId);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Override
	protected void onImpact(RayTraceResult ray) {
		if (!this.world.isRemote) {
			if (ray.getBlockPos() != null) {
				Material materialHit = world.getBlockState(ray.getBlockPos()).getMaterial();

				if (materialHit == Material.ORGANIC) {
					ItemStack dummy = new ItemStack(Items.DYE, 1, 15);
					if (BoneMealItem.applyBonemeal(dummy, world, ray.getBlockPos())) {
						world.playEvent(2005, ray.getBlockPos(), 0);
					}
				} else if (materialHit.isSolid() && canReplaceBlock(world, ray.getBlockPos())) {
					world.setBlockState(ray.getBlockPos(), Blocks.BIRCH_LEAVES.getDefaultState());
				}
			}

			if (ray.hitInfo instanceof LivingEntity && (thrower == null || (ray.entityHit != thrower && ray.entityHit != thrower.getRidingEntity()))) {
				if (ray.entityHit.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.getThrower()), 2)
						&& world.getDifficulty() != Difficulty.PEACEFUL) {
					int poisonTime = world.getDifficulty() == Difficulty.HARD ? 7 : 3;
					((LivingEntity) ray.entityHit).addPotionEffect(new EffectInstance(Effects.POISON, poisonTime * 20, 0));
				}
			}

			this.world.setEntityState(this, (byte) 3);
			this.setDead();
		}
	}

	private boolean canReplaceBlock(World world, BlockPos pos) {
		float hardness = world.getBlockState(pos).getBlockHardness(world, pos);
		return hardness >= 0 && hardness < 50F;
	}
}
