package twilightforest.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityTFBoneShard extends EntityArrow
{
	public EntityTFBoneShard(World worldIn)
	{
		super(worldIn);
	}

	public EntityTFBoneShard(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityTFBoneShard(World worldIn, EntityLivingBase shooter)
	{
		super(worldIn, shooter);
	}


	protected ItemStack getArrowStack()
	{
		return new ItemStack(Items.BONE);

	}

}