package twilightforest.entity.passive;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class EntityTFPenguin extends EntityTFBird {

	public EntityTFPenguin(World world) {
		super(world);
		this.setSize(0.5F, 0.9F);
	}

	@Override
	protected void initEntityAI() {
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 1.75F));
		tasks.addTask(2, new EntityAIMate(this, 1.0F));
		tasks.addTask(3, new EntityAITempt(this, 0.75F, Items.FISH, false));
		tasks.addTask(4, new EntityAIFollowParent(this, 1.15F));
		tasks.addTask(5, new EntityAIWander(this, 1.0F));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6F));
		tasks.addTask(7, new EntityAIWatchClosest2(this, twilightforest.entity.passive.EntityTFPenguin.class, 5F, 0.02F));
		tasks.addTask(8, new EntityAILookIdle(this));
	}

	@Override
	public EntityAnimal createChild(EntityAgeable entityanimal) {
		return new EntityTFPenguin(world);
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == Items.FISH;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
	}
}
