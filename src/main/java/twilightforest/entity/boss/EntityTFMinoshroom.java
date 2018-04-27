package twilightforest.entity.boss;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFMinotaur;
import twilightforest.item.TFItems;

public class EntityTFMinoshroom extends EntityTFMinotaur {
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(TwilightForestMod.ID, "entities/minoshroom");

	public EntityTFMinoshroom(World par1World) {
		super(par1World);
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
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.minotaur_axe));
		return data;
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
	public boolean isNonBoss() {
		return false;
	}

}
