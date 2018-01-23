package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.entity.EntityTFMoonwormShot;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemTFMoonwormQueen extends ItemTF {
	private static final int FIRING_TIME = 12;

	protected ItemTFMoonwormQueen() {
		this.setCreativeTab(TFItems.creativeTab);
		this.maxStackSize = 1;
		this.setMaxDamage(256);
		addPropertyOverride(new ResourceLocation(TwilightForestMod.ID, "alt"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			@Override
			public float apply(@Nonnull ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				if (entityIn != null && entityIn.getActiveItemStack() == stack) {
					int useTime = stack.getMaxItemUseDuration() - entityIn.getItemInUseCount();
					if (useTime >= FIRING_TIME && (useTime >>> 1) % 2 == 0) {
						return 1;
					}
				}

				return 0;
			}
		});
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getItemDamage() >= stack.getMaxDamage() - 1) {
			return ActionResult.newResult(EnumActionResult.FAIL, stack);
		} else {
			player.setActiveHand(hand);
			return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}
	}

	//	[VanillaCopy] ItemBlock.onItemUse, harcoding the block
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if (!block.isReplaceable(worldIn, pos)) {
			pos = pos.offset(facing);
		}

		ItemStack itemstack = player.getHeldItem(hand);

		if (itemstack.getItemDamage() < itemstack.getMaxDamage() && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(TFBlocks.moonworm, pos, false, facing, (Entity) null)) {
			int i = this.getMetadata(itemstack.getMetadata());
			IBlockState iblockstate1 = TFBlocks.moonworm.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);

			if (placeMoonwormAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1)) {
				SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, player);
				worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				// TF - damage stack instead of shrinking
				itemstack.damageItem(1, player);
				player.resetActiveHand();
			}

			return EnumActionResult.SUCCESS;
		} else {
			return EnumActionResult.FAIL;
		}
	}

	//	[VanillaCopy] ItemBlock.placeBlockAt
	private boolean placeMoonwormAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState state) {
		if (!world.setBlockState(pos, state, 11)) return false;

		IBlockState real = world.getBlockState(pos);
		if (real.getBlock() == TFBlocks.moonworm) {
			TFBlocks.moonworm.onBlockPlacedBy(world, pos, state, player, stack);
		}

		return true;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int useRemaining) {
		int useTime = this.getMaxItemUseDuration(stack) - useRemaining;

		if (!world.isRemote && useTime > FIRING_TIME && (stack.getItemDamage() + 1) < stack.getMaxDamage()) {
			boolean fired = world.spawnEntity(new EntityTFMoonwormShot(world, living));

			if (fired) {
				stack.damageItem(2, living);

				world.playSound(null, living.posX, living.posY, living.posZ, SoundEvents.BLOCK_SLIME_HIT, living instanceof EntityPlayer ? SoundCategory.PLAYERS : SoundCategory.NEUTRAL, 1, 1);
			}
		}

	}

	@Nonnull
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

}
