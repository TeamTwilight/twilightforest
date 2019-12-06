package twilightforest.entity.passive;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.LookRandomlyGoal;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.PanicGoal;
import net.minecraft.entity.ai.SwimGoal;
import net.minecraft.entity.ai.TemptGoal;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.LookAtGoal;
import net.minecraft.entity.ai.LookAtGoal2;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

public class EntityTFPenguin extends EntityTFBird {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/penguin");

	public EntityTFPenguin(World world) {
		super(world);
		this.setSize(0.5F, 0.9F);
	}

	@Override
	protected void registerGoals() {
		tasks.addTask(0, new SwimGoal(this));
		tasks.addTask(1, new PanicGoal(this, 1.75F));
		tasks.addTask(2, new EntityAIMate(this, 1.0F));
		tasks.addTask(3, new TemptGoal(this, 0.75F, Items.FISH, false));
		tasks.addTask(4, new EntityAIFollowParent(this, 1.15F));
		tasks.addTask(5, new EntityAIWander(this, 1.0F));
		tasks.addTask(6, new LookAtGoal(this, EntityPlayer.class, 6F));
		tasks.addTask(7, new LookAtGoal2(this, twilightforest.entity.passive.EntityTFPenguin.class, 5F, 0.02F));
		tasks.addTask(8, new LookRandomlyGoal(this));
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

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}
