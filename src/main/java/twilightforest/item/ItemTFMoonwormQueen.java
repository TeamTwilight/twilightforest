package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.entity.EntityTFMoonwormShot;

import javax.annotation.Nullable;

public class ItemTFMoonwormQueen extends ItemTF
{
	private static final int FIRING_TIME = 12;

	protected ItemTFMoonwormQueen() {
		this.setCreativeTab(TFItems.creativeTab);
		this.maxStackSize = 1;
        this.setMaxDamage(256);
        addPropertyOverride(new ResourceLocation(TwilightForestMod.ID, "alt"), new IItemPropertyGetter() {
        	@SideOnly(Side.CLIENT)
			@Override
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
        		if (entityIn != null) {
					int useTime = stack.getMaxItemUseDuration() - entityIn.getItemInUseCount();
					if (useTime >= FIRING_TIME && (useTime >>> 1) % 2 == 0)
					{
						return 1;
					}
				}

				return 0;
			}
		});
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (stack.getItemDamage() < getMaxDamage(stack))
		{
			player.setActiveHand(hand);
		}
		else 
		{
			player.resetActiveHand();
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

//	[VanillaCopy] ItemBlock.onItemUse
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState currentState = world.getBlockState(pos);
		Block currentBlock = currentState.getBlock();

		if (currentBlock == TFBlocks.moonworm) return EnumActionResult.FAIL;

		if (stack != null && stack.getItemDamage() == getMaxDamage(stack)) return EnumActionResult.FAIL;

		if (!currentBlock.isReplaceable(world, pos))
		{
			pos = pos.offset(facing);
		}

		if (player.canPlayerEdit(pos, facing, stack) && world.canBlockBePlaced(TFBlocks.moonworm, pos, false, facing, null, stack))
		{
			IBlockState state = TFBlocks.moonworm.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, 0, player, null);

			if (placeMoonwormAt(player, world, pos, facing, hitX, hitY, hitZ, state))
			{
				IBlockState real = world.getBlockState(pos);
				SoundType type = real.getBlock().getSoundType(real, world, pos, player);
				world.playSound(player, pos, type.getPlaceSound(), SoundCategory.BLOCKS, (type.getVolume() + 1f) / 2f, type.getPitch() * 0.8f);

				if (stack != null)
				{
					stack.damageItem(1, player);
					player.stopActiveHand();
				}
			}

			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}

//	[VanillaCopy] ItemBlock.placeBlockAt
	private boolean placeMoonwormAt(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState state) {
		if (!world.setBlockState(pos, state, 3)) return false;

		IBlockState real = world.getBlockState(pos);
		if (real.getBlock() == TFBlocks.moonworm)
		{
			TFBlocks.moonworm.onBlockPlacedBy(world, pos, state, player, null);
		}

		return true;
	}

    @Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int useRemaining)
    {
    	int useTime = this.getMaxItemUseDuration(stack) - useRemaining;


    	if (!world.isRemote && useTime > FIRING_TIME && (stack.getItemDamage() + 1) < this.getMaxDamage())
    	{
    		boolean fired = world.spawnEntity(new EntityTFMoonwormShot(world, living));

    		if (fired)
    		{
    			stack.damageItem(2, living);

				world.playSound(null, living.posX, living.posY, living.posZ, SoundEvents.BLOCK_SLIME_HIT, living instanceof EntityPlayer ? SoundCategory.PLAYERS : SoundCategory.NEUTRAL, 1, 1);
    		}
    	}

    }

    @Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.BOW;
    }
    
    @Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }

}
