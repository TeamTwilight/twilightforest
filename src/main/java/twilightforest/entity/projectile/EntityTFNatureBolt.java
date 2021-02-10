package twilightforest.entity.projectile;

import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.TFEntities;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class EntityTFNatureBolt extends EntityTFThrowable implements ITFProjectile, IRendersAsItem {

	public EntityTFNatureBolt(EntityType<? extends EntityTFNatureBolt> type, World world) {
		super(type, world);
	}

	public EntityTFNatureBolt(World world, LivingEntity owner) {
		super(TFEntities.nature_bolt, world, owner);
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
			double dx = getPosX() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dy = getPosY() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dz = getPosZ() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			world.addParticle(ParticleTypes.HAPPY_VILLAGER, dx, dy, dz, 0.0D, 0.0D, 0.0D);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.OAK_LEAVES.getDefaultState()), false, this.getPosX(), this.getPosY(), this.getPosZ(), rand.nextGaussian() * 0.05D, rand.nextDouble() * 0.2D, rand.nextGaussian() * 0.05D);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Override
	protected void onImpact(RayTraceResult ray) {
		if (!this.world.isRemote) {
			if (ray.getType() == RayTraceResult.Type.BLOCK) {
				BlockPos blockPosHit = ((BlockRayTraceResult) ray).getPos();
				Material materialHit = world.getBlockState(blockPosHit).getMaterial();

				if (materialHit == Material.ORGANIC) {
					ItemStack dummy = new ItemStack(Items.BONE_MEAL, 1);
					if (BoneMealItem.applyBonemeal(dummy, world, blockPosHit)) {
						world.playEvent(2005, blockPosHit, 0);
					}
				} else if (materialHit.isSolid() && canReplaceBlock(world, blockPosHit)) {
					world.setBlockState(blockPosHit, Blocks.BIRCH_LEAVES.getDefaultState());
				}
			}

			if (ray instanceof EntityRayTraceResult) {
				Entity owner = func_234616_v_();
				Entity entityHit = ((EntityRayTraceResult) ray).getEntity();
				if (entityHit instanceof LivingEntity && (owner == null || (entityHit != owner && entityHit != owner.getRidingEntity()))) {
					if (entityHit.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.func_234616_v_()), 2)
							&& world.getDifficulty() != Difficulty.PEACEFUL) {
						int poisonTime = world.getDifficulty() == Difficulty.HARD ? 7 : 3;
						((LivingEntity) entityHit).addPotionEffect(new EffectInstance(Effects.POISON, poisonTime * 20, 0));
					}
				}
			}

			this.world.setEntityState(this, (byte) 3);
			this.remove();
		}
	}

	private boolean canReplaceBlock(World world, BlockPos pos) {
		float hardness = world.getBlockState(pos).getBlockHardness(world, pos);
		return hardness >= 0 && hardness < 50F;
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.WHEAT_SEEDS);
	}
}
