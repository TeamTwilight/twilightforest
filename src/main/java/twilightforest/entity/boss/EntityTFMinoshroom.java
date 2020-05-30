package twilightforest.entity.boss;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.entity.EntityTFMinotaur;
import twilightforest.entity.ai.EntityAITFGroundAttack;
import twilightforest.enums.BossVariant;
import twilightforest.item.TFItems;
import twilightforest.world.TFWorld;

public class EntityTFMinoshroom extends EntityTFMinotaur {
	private static final DataParameter<Boolean> GROUND_ATTACK = EntityDataManager.createKey(EntityTFMinoshroom.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> GROUND_CHARGE = EntityDataManager.createKey(EntityTFMinoshroom.class, DataSerializers.VARINT);
	private float prevClientSideChargeAnimation;
	private float clientSideChargeAnimation;
	private boolean groundSmashState = false;

	public EntityTFMinoshroom(EntityType<? extends EntityTFMinoshroom> type, World world) {
		super(type, world);
		this.experienceValue = 100;
		this.setDropChance(EquipmentSlotType.MAINHAND, 1.1F); // > 1 means it is not randomly damaged when dropped
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new EntityAITFGroundAttack(this));
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(GROUND_ATTACK, false);
		dataManager.register(GROUND_CHARGE, 0);
	}

	public boolean isGroundAttackCharge() {
		return dataManager.get(GROUND_ATTACK);
	}

	public void setGroundAttackCharge(boolean flag) {
		dataManager.set(GROUND_ATTACK, flag);
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(120.0D);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.world.isRemote) {
			this.prevClientSideChargeAnimation = this.clientSideChargeAnimation;
			if (this.isGroundAttackCharge()) {
				this.clientSideChargeAnimation = MathHelper.clamp(this.clientSideChargeAnimation + (1F / ((float) dataManager.get(GROUND_CHARGE)) * 6F), 0.0F, 6.0F);
				groundSmashState = true;
			} else {
				this.clientSideChargeAnimation = MathHelper.clamp(this.clientSideChargeAnimation - 1.0F, 0.0F, 6.0F);
				if (groundSmashState) {
					BlockState block = world.getBlockState(getPosition().down());

					for (int i = 0; i < 80; i++) {
						double cx = getPosition().getX() + world.rand.nextFloat() * 10F - 5F;
						double cy = getBoundingBox().minY + 0.1F + world.rand.nextFloat() * 0.3F;
						double cz = getPosition().getZ() + world.rand.nextFloat() * 10F - 5F;

						world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, block), cx, cy, cz, 0D, 0D, 0D);
					}
					groundSmashState = false;
				}
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public float getChargeAnimationScale(float p_189795_1_) {
		return (this.prevClientSideChargeAnimation + (this.clientSideChargeAnimation - this.prevClientSideChargeAnimation) * p_189795_1_) / 6.0F;
	}

	public void setMaxCharge(int charge) {
		dataManager.set(GROUND_CHARGE, charge);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);
		this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(TFItems.minotaur_axe.get()));
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!world.isRemote) {
			TFWorld.markStructureConquered(world, new BlockPos(this), TFFeature.LABYRINTH);
		}
	}

	@Override
	public boolean canDespawn(double distance) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (world.getDifficulty() == Difficulty.PEACEFUL) {
			if (detachHome()) {
				world.setBlockState(getHomePosition(), TFBlocks.boss_spawner.get().getDefaultState().with(BlockTFBossSpawner.VARIANT, BossVariant.MINOSHROOM));
			}
			remove();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}
}
