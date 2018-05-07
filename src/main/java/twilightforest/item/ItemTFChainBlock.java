package twilightforest.item;

import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.entity.EntityTFChainBlock;

import javax.annotation.Nullable;
import java.util.UUID;

public class ItemTFChainBlock extends ItemTool implements ModelRegisterCallback {
	private static final String THROWN_UUID_KEY = "chainEntity";

	protected ItemTFChainBlock() {
		super(6, 1.6F, TFItems.TOOL_KNIGHTLY, Sets.newHashSet(Blocks.STONE)); // todo 1.9 attack speed
		this.maxStackSize = 1;
		this.setMaxDamage(99);
		this.setCreativeTab(TFItems.creativeTab);

		this.addPropertyOverride(new ResourceLocation(TwilightForestMod.ID, "thrown"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			@Override
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				return getThrownUuid(stack) != null ? 1 : 0;
			}
		});
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity holder, int slot, boolean isSelected) {
		if (!world.isRemote && getThrownUuid(stack) != null && getThrownEntity(world, stack) == null) {
			stack.getTagCompound().removeTag(THROWN_UUID_KEY + "Most");
			stack.getTagCompound().removeTag(THROWN_UUID_KEY + "Least");
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (getThrownUuid(stack) != null)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		player.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F));

		if (!world.isRemote) {
			EntityTFChainBlock launchedBlock = new EntityTFChainBlock(world, player, hand);
			world.spawnEntity(launchedBlock);
			setThrownEntity(stack, launchedBlock);

			stack.damageItem(1, player);
		}

		player.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Nullable
	private static UUID getThrownUuid(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasUniqueId(THROWN_UUID_KEY)) {
			return stack.getTagCompound().getUniqueId(THROWN_UUID_KEY);
		}

		return null;
	}

	@Nullable
	private static EntityTFChainBlock getThrownEntity(World world, ItemStack stack) {
		if (world instanceof WorldServer) {
			UUID id = getThrownUuid(stack);
			if (id != null) {
				Entity e = ((WorldServer) world).getEntityFromUuid(id);
				if (e instanceof EntityTFChainBlock) {
					return (EntityTFChainBlock) e;
				}
			}
		}

		return null;
	}

	private static void setThrownEntity(ItemStack stack, EntityTFChainBlock cube) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setUniqueId(THROWN_UUID_KEY, cube.getUniqueID());
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.BLOCK;
	}

	@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker) {
		return true;
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState state) {
		if ("pickaxe".equals(toolClass)) {
			return 2;
		} else {
			return -1;
		}
	}
}
