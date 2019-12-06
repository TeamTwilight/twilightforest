package twilightforest.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.MeleeAttackGoal;
import net.minecraft.entity.ai.HurtByTargetGoal;
import net.minecraft.entity.ai.LookRandomlyGoal;
import net.minecraft.entity.ai.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.SwimGoal;
import net.minecraft.entity.ai.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.LookAtGoal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

public class EntityTFGiantMiner extends EntityMob {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/giant_miner");

	public EntityTFGiantMiner(World world) {
		super(world);
		this.setSize(this.width * 4.0F, this.height * 4.0F);

		for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
			setDropChance(slot, 0);
		}
	}

	@Override
	protected void registerGoals() {
		this.tasks.addTask(1, new SwimGoal(this));
		this.tasks.addTask(4, new MeleeAttackGoal(this, 1.0D, false) {
			@Override
			protected double getAttackReachSqr(EntityLivingBase attackTarget) {
				return this.attacker.width * this.attacker.height;
			}
		});
		this.tasks.addTask(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.tasks.addTask(6, new LookAtGoal(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new LookRandomlyGoal(this));
		this.targetTasks.addTask(1, new HurtByTargetGoal(this, false));
		this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
		setEquipmentBasedOnDifficulty(difficulty);
		setEnchantmentBasedOnDifficulty(difficulty);
		return data;
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_PICKAXE));
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}
