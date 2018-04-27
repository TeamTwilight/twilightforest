package twilightforest.entity;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.EntityAITFRedcapPlantTNT;
import twilightforest.item.TFItems;
import twilightforest.util.PlayerHelper;

public class EntityTFRedcapSapper extends EntityTFRedcap {

	public EntityTFRedcapSapper(World world) {
		super(world);
		this.heldPick = new ItemStack(TFItems.ironwood_pickaxe);
		this.heldTNT.setCount(3);
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
		this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(TFItems.ironwood_boots));
		return data;
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(4, new EntityAITFRedcapPlantTNT(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		if (source.getTrueSource() instanceof EntityPlayerMP) {
			// are we in a level 2 hill?
			int chunkX = MathHelper.floor(posX) >> 4;
			int chunkZ = MathHelper.floor(posZ) >> 4;
			if (TFFeature.getNearestFeature(chunkX, chunkZ, world) == TFFeature.hill2) {
				PlayerHelper.grantCriterion((EntityPlayerMP) source.getTrueSource(), new ResourceLocation(TwilightForestMod.ID, "hill2"), "redcap_sapper");
			}
		}
	}
}
