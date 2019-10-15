package twilightforest.entity.boss;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.entity.EntityTFMinotaur;
import twilightforest.entity.ai.EntityAITFGroundAttack;
import twilightforest.enums.BossVariant;
import twilightforest.item.TFItems;
import twilightforest.world.TFWorld;

public class EntityTFMinoshroom extends EntityTFMinotaur {
	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/minoshroom");
	private static final DataParameter<Boolean> GROUND_ATTACK = EntityDataManager.createKey(EntityTFMinoshroom.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> GROUND_CHARGE = EntityDataManager.createKey(EntityTFMinoshroom.class, DataSerializers.VARINT);
	private float prevClientSideChargeAnimation;
	private float clientSideChargeAnimation;
	private boolean groundSmashState = false;

	public EntityTFMinoshroom(World world) {
		super(world);
		this.setSize(1.49F, 2.9F);
		this.experienceValue = 100;
		this.setDropChance(EntityEquipmentSlot.MAINHAND, 1.1F); // > 1 means it is not randomly damaged when dropped
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(1, new EntityAITFGroundAttack(this));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
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
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(120.0D);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.world.isRemote) {
			this.prevClientSideChargeAnimation = this.clientSideChargeAnimation;
			if (this.isGroundAttackCharge()) {
				this.clientSideChargeAnimation = MathHelper.clamp(this.clientSideChargeAnimation + (1F / ((float) dataManager.get(GROUND_CHARGE)) * 6F), 0.0F, 6.0F);
				groundSmashState = true;
			} else {
				this.clientSideChargeAnimation = MathHelper.clamp(this.clientSideChargeAnimation - 1.0F, 0.0F, 6.0F);
				if (groundSmashState) {
					IBlockState block = world.getBlockState(getPosition().down());
					int stateId = Block.getStateId(block);

					for (int i = 0; i < 80; i++) {
						double cx = getPosition().getX() + world.rand.nextFloat() * 10F - 5F;
						double cy = getEntityBoundingBox().minY + 0.1F + world.rand.nextFloat() * 0.3F;
						double cz = getPosition().getZ() + world.rand.nextFloat() * 10F - 5F;

						world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, cx, cy, cz, 0D, 0D, 0D, stateId);
					}
					groundSmashState = false;
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public float getChargeAnimationScale(float p_189795_1_) {
		return (this.prevClientSideChargeAnimation + (this.clientSideChargeAnimation - this.prevClientSideChargeAnimation) * p_189795_1_) / 6.0F;
	}

	public void setMaxCharge(int charge) {
		dataManager.set(GROUND_CHARGE, charge);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.minotaur_axe));
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!world.isRemote) {
			TFWorld.markStructureConquered(world, new BlockPos(this), TFFeature.LABYRINTH);
		}
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected void despawnEntity() {
		if (world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			if (hasHome()) {
				world.setBlockState(getHomePosition(), TFBlocks.boss_spawner.getDefaultState().withProperty(BlockTFBossSpawner.VARIANT, BossVariant.MINOSHROOM));
			}
			setDead();
		} else {
			super.despawnEntity();
		}
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}
}
