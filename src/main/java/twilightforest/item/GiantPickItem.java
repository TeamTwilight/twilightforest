package twilightforest.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.GiantBlock;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;

import java.util.List;

public class GiantPickItem extends PickaxeItem implements GiantItem {

	public GiantPickItem(Tier material, Properties properties) {
		super(material, 8, -3.5F, properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, level, tooltip, flags);
		tooltip.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> attributeBuilder = ImmutableMultimap.builder();
		attributeBuilder.putAll(super.getDefaultAttributeModifiers(slot));
		attributeBuilder.put(NeoForgeMod.BLOCK_REACH.value(), new AttributeModifier(GIANT_REACH_MODIFIER, "Reach modifier", 2.5, AttributeModifier.Operation.ADDITION));
		attributeBuilder.put(NeoForgeMod.ENTITY_REACH.value(), new AttributeModifier(GIANT_RANGE_MODIFIER, "Range modifier", 2.5, AttributeModifier.Operation.ADDITION));
		return slot == EquipmentSlot.MAINHAND ? attributeBuilder.build() : super.getDefaultAttributeModifiers(slot);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		float destroySpeed = super.getDestroySpeed(stack, state);
		// extra 64X strength vs giant obsidian
		destroySpeed *= (state.is(TFBlocks.GIANT_OBSIDIAN)) ? 64 : 1;
		// 64x strength vs giant blocks
		return state.getBlock() instanceof GiantBlock ? destroySpeed * 64 : destroySpeed;
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
		ItemStack stack = player.getMainHandItem();
		if (stack.is(this)) {
			var attachment = player.getData(TFDataAttachments.GIANT_PICKAXE_MINING);
			if (attachment.getMining() != level.getGameTime()) {
				attachment.setMining(level.getGameTime());
				attachment.setBreaking(false);
				attachment.setGiantBlockConversion(0);
			}
		}
		return super.canAttackBlock(state, level, pos, player);
	}
}