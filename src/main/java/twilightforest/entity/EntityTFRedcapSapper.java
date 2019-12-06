package twilightforest.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.EntityAITFRedcapPlantTNT;
import twilightforest.item.TFItems;

public class EntityTFRedcapSapper extends EntityTFRedcap {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/redcap_sapper");

	public EntityTFRedcapSapper(World world) {
		super(world);
		this.heldPick = new ItemStack(TFItems.ironwood_pickaxe);
		this.heldTNT.setCount(3);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);
		this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(TFItems.ironwood_boots));
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.tasks.addTask(4, new EntityAITFRedcapPlantTNT(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}
