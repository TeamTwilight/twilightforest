package twilightforest.item;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTFArcticArmor extends ItemArmor implements ModelRegisterCallback {
	public ItemTFArcticArmor(ItemArmor.ArmorMaterial par2EnumArmorMaterial, EntityEquipmentSlot armorType) {
		super(par2EnumArmorMaterial, 0, armorType);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.UNCOMMON;
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EntityEquipmentSlot slot, String layer) {
		if (slot == EntityEquipmentSlot.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "arcticarmor_2" + (layer == null ? "_dyed" : "_overlay") + ".png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "arcticarmor_1" + (layer == null ? "_dyed" : "_overlay") + ".png";
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tab)) {
			ItemStack istack = new ItemStack(this);
			//istack.addEnchantment(TFEnchantment.reactFire, 2);
			list.add(istack);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped oldM) {
		return TwilightForestMod.proxy.getArcticArmorModel(armorSlot);
	}

	@Override
	public boolean hasOverlay(ItemStack stack) {
		return getColor(stack) != 0xFFFFFF;
	}

	@Override
	public boolean hasColor(ItemStack stack) {
		NBTTagCompound nbttagcompound = stack.getTagCompound();
		return (nbttagcompound != null && nbttagcompound.hasKey("display", 10)) && nbttagcompound.getCompoundTag("display").hasKey("color", 3);
	}

	@Override
	public int getColor(ItemStack stack) {
		return this.getColor(stack, 1);
	}

	@Override
	public void removeColor(ItemStack stack) {
		this.removeColor(stack, 1);
	}

	@Override
	public void setColor(ItemStack stack, int color) {
		this.setColor(stack, color, 1);
	}

	public int getColor(ItemStack stack, int type) {
		String string = "";//type == 0 ? "" : ("" + type);
		NBTTagCompound stackTagCompound = stack.getTagCompound();

		int color = 0xBDCFD9;

		if (stackTagCompound != null) {
			NBTTagCompound displayCompound = stackTagCompound.getCompoundTag("display");

			if (displayCompound.hasKey("color" + string, 3))
				color = displayCompound.getInteger("color" + string);
		}

		switch (type) {
			//case 0:
				//return stack.getItem() != TFItems.arctic_helmet ? 0x793828 : 0xFFFFFF;
			case 0:
				return 0xFFFFFF;
			default:
				return color;
		}
	}

	public void removeColor(ItemStack stack, int type) {
		String string = "";//type == 0 ? "" : ("" + type);
		NBTTagCompound stackTagCompound = stack.getTagCompound();

		if (stackTagCompound != null) {
			NBTTagCompound displayCompound = stackTagCompound.getCompoundTag("display");

			if (displayCompound.hasKey("color" + string))
				displayCompound.removeTag("color" + string);

			if (displayCompound.hasKey("hasColor"))
				displayCompound.setBoolean("hasColor", false);
		}
	}

	public void setColor(ItemStack stack, int color, int type) {
		String string = "";//type == 0 ? "" : ("" + type);
		NBTTagCompound stackTagCompound = stack.getTagCompound();

		if (stackTagCompound == null) {
			stackTagCompound = new NBTTagCompound();
			stack.setTagCompound(stackTagCompound);
		}

		NBTTagCompound displayCompound = stackTagCompound.getCompoundTag("display");

		if (!stackTagCompound.hasKey("display", 10))
			stackTagCompound.setTag("display", displayCompound);

		displayCompound.setInteger("color" + string, color);
		displayCompound.setBoolean("hasColor", true);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (this.hasColor(stack)) {
			IBlockState blockAt = world.getBlockState(pos);

			if (blockAt.getBlock() instanceof BlockCauldron && blockAt.getValue(BlockCauldron.LEVEL) > 0) {
				removeColor(stack);
				player.addStat(StatList.ARMOR_CLEANED);

				((BlockCauldron) blockAt.getBlock()).setWaterLevel(world, pos, blockAt, blockAt.getValue(BlockCauldron.LEVEL) - 1);
				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.PASS;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(I18n.format("item.arctic_armor.tooltip"));
	}
}