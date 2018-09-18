package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityTFNatureBolt extends EntityThrowable implements ITFProjectile {

	public EntityTFNatureBolt(World world) {
		super(world);
	}

	public EntityTFNatureBolt(World world, EntityLivingBase thrower) {
		super(world, thrower);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
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
			world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, dx, dy, dz, 0.0D, 0.0D, 0.0D);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX, this.posY, this.posZ, rand.nextGaussian() * 0.05D, rand.nextDouble() * 0.2D, rand.nextGaussian() * 0.05D, Block.getStateId(Blocks.LEAVES.getDefaultState()));
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

				if (materialHit == Material.GRASS) {
					ItemStack dummy = new ItemStack(Items.DYE, 1, 15);
					if (ItemDye.applyBonemeal(dummy, world, ray.getBlockPos())) {
						world.playEvent(2005, ray.getBlockPos(), 0);
					}
				} else if (materialHit.isSolid() && canReplaceBlock(world, ray.getBlockPos())) {
					world.setBlockState(ray.getBlockPos(), Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH));
				}
			}

			if (ray.entityHit instanceof EntityLivingBase && (thrower == null || (ray.entityHit != thrower && ray.entityHit != thrower.getRidingEntity()))) {
				if (ray.entityHit.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.getThrower()), 2)
						&& world.getDifficulty() != EnumDifficulty.PEACEFUL) {
					int poisonTime = world.getDifficulty() == EnumDifficulty.HARD ? 7 : 3;
					((EntityLivingBase) ray.entityHit).addPotionEffect(new PotionEffect(MobEffects.POISON, poisonTime * 20, 0));
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
