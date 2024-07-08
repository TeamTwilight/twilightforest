package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.block.GiantBlock;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;

import java.util.List;

public class GiantPickItem extends PickaxeItem {

	public GiantPickItem(Tier material, Properties properties) {
		super(material, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, context, tooltip, flags);
		tooltip.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
	}

	public static ItemAttributeModifiers createGiantAttributes(Tier tier, int damage, float speed) {
		return PickaxeItem.createAttributes(tier, damage, speed)
			.withModifierAdded(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(TwilightForestMod.prefix("reach_modifier"), 2.5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
			.withModifierAdded(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(TwilightForestMod.prefix("range_modifier"), 2.5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND);
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