package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.block.GiantBlock;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;

import java.util.function.Consumer;

public class GiantPickItem extends Item {

	public GiantPickItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, builder, flag);
		builder.accept(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
	}

	public static Properties createGiantSwordAttributes(Item.Properties properties, ToolMaterial material, int damage, float speed) {
		return material.applySwordProperties(properties, damage, speed)
			.attributes(ItemAttributeModifiers.builder()
				.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, damage + material.attackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, speed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(TwilightForestMod.prefix("reach_modifier"), 2.5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
				.add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(TwilightForestMod.prefix("range_modifier"), 2.5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
				.build());
	}

	public static Properties createGiantPickAttributes(Item.Properties properties, ToolMaterial material, int damage, float speed) {
		return material.applyToolProperties(properties, BlockTags.MINEABLE_WITH_PICKAXE, damage, speed, 0.0F)
			.attributes(ItemAttributeModifiers.builder()
				.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, damage + material.attackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, speed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(TwilightForestMod.prefix("reach_modifier"), 2.5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
				.add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(TwilightForestMod.prefix("range_modifier"), 2.5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
				.build());
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
	public boolean canDestroyBlock(ItemStack stack, BlockState state, Level level, BlockPos pos, LivingEntity user) {
		boolean ret = super.canDestroyBlock(stack, state, level, pos, user);
		if (ret && user instanceof Player player) {
			var attachment = player.getData(TFDataAttachments.GIANT_PICKAXE_MINING);
			if (attachment.getMining() != level.getGameTime()) {
				attachment.setMining(level.getGameTime());
				attachment.setBreaking(false);
				attachment.setGiantBlockConversion(0);
			}
		}
		return ret;
	}
}