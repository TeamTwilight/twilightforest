package twilightforest.entity.boss;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.entity.EntityTFMinotaur;
import twilightforest.enums.BossVariant;
import twilightforest.item.TFItems;

public class EntityTFMinoshroom extends EntityTFMinotaur {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/minoshroom");

	public EntityTFMinoshroom(World world) {
		super(world);
		this.setSize(1.49F, 2.9F);
		this.experienceValue = 100;
		this.setDropChance(EntityEquipmentSlot.MAINHAND, 1.1F); // > 1 means it is not randomly damaged when dropped
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(120.0D);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.minotaur_axe));
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
