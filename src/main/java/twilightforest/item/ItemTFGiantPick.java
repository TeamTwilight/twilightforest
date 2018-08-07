package twilightforest.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.block.BlockTFGiantBlock;
import twilightforest.block.TFBlocks;
import twilightforest.client.ModelRegisterCallback;

import java.util.List;

public class ItemTFGiantPick extends ItemPickaxe implements ModelRegisterCallback {

	protected ItemTFGiantPick(Item.ToolMaterial material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
		this.attackDamage = 8 + material.getAttackDamage();
		this.attackSpeed = -3.5F;
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.RARE;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(I18n.format(getUnlocalizedName() + ".tooltip"));
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		float destroySpeed = super.getDestroySpeed(stack, state);
		// extra 64X strength vs giant obsidian
		destroySpeed *= (state.getBlock() == TFBlocks.giant_obsidian) ? 64 : 1;
		// 64x strength vs giant blocks
		return state.getBlock() instanceof BlockTFGiantBlock ? destroySpeed * 64 : destroySpeed;
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(EntityPlayer.REACH_DISTANCE.getName(), new AttributeModifier(TFItems.GIANT_REACH_MODIFIER, "Tool modifier", 2.5, 0));
		}

		return multimap;
	}
}
