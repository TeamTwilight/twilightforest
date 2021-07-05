package twilightforest.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import twilightforest.block.TFBlocks;
import twilightforest.util.TFDamageSources;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class EntityTFGiantMiner extends MonsterEntity {

	public EntityTFGiantMiner(EntityType<? extends EntityTFGiantMiner> type, World world) {
		super(type, world);

		for (EquipmentSlotType slot : EquipmentSlotType.values()) {
			setDropChance(slot, 0);
		}
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false) {
			@Override
			protected double getAttackReachSqr(LivingEntity attackTarget) {
				return this.attacker.getWidth() * this.attacker.getHeight();
			}
		});
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 80.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 40.0D);
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		ILivingEntityData data = super.onInitialSpawn(worldIn, difficulty, reason, spawnDataIn, dataTag);
		setEquipmentBasedOnDifficulty(difficulty);
		setEnchantmentBasedOnDifficulty(difficulty);

		return data;
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_PICKAXE));
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		entityIn.attackEntityFrom(TFDamageSources.ANT(this), (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
		return super.attackEntityAsMob(entityIn);
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 1;
	}

	@Override
	public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
		List<EntityTFGiantMiner> giantsNearby = worldIn.getEntitiesWithinAABB(EntityTFGiantMiner.class, this.getBoundingBox().grow(50));
		return giantsNearby.size() < 7;
	}

	public static boolean canSpawn(EntityType<? extends EntityTFGiantMiner> type, IWorld world, SpawnReason reason, BlockPos pos, Random rand) {
		return pos.getY() > 60 && (MobEntity.canSpawnOn(type, world, reason, pos, rand) || world.getBlockState(pos).getBlock() == TFBlocks.wispy_cloud.get() || world.getBlockState(pos).getBlock() == TFBlocks.fluffy_cloud.get());
	}

	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return false;
	}
}
