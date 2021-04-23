package twilightforest.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.IItemTier;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.block.BlockTFGiantBlock;
import twilightforest.block.TFBlocks;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTFGiantPick extends PickaxeItem {

	protected ItemTFGiantPick(IItemTier material, Properties props) {
		super(material, 8, -3.5F, props);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(new TranslationTextComponent(getTranslationKey() + ".tooltip"));
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		float destroySpeed = super.getDestroySpeed(stack, state);
		// extra 64X strength vs giant obsidian
		destroySpeed *= (state.getBlock() == TFBlocks.giant_obsidian.get()) ? 64 : 1;
		// 64x strength vs giant blocks
		return state.getBlock() instanceof BlockTFGiantBlock ? destroySpeed * 64 : destroySpeed;
	}

	//TODO: It's actually an ImmutableMap, so this can't work
//	@Override
//	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
//		Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);
//
//		if (equipmentSlot == EquipmentSlotType.MAINHAND) {
//			multimap.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(TFItems.GIANT_REACH_MODIFIER, "Tool modifier", 2.5, AttributeModifier.Operation.ADDITION));
//		}
//
//		return multimap;
//	}
}
