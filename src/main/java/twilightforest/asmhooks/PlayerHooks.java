package twilightforest.asmhooks;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.phys.Vec3;
import twilightforest.asm.transformers.player.GetFieldOfViewModifierTransformer;
import twilightforest.asm.transformers.player.ReduceMovementFoodExhaustionTransformer;
import twilightforest.init.TFAttributeModifiers;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;

@SuppressWarnings({"JavadocReference", "unused"})
public class PlayerHooks {

	/**
	 * {@link twilightforest.asm.transformers.player.MaybeBackOffFromEdgeTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.entity.player.Player#maybeBackOffFromEdge(Vec3 vec, MoverType mover)}
	 */
	public static float cancelHighStepModifierForStepDownDuringSneaking(Player player, float f) {
		for (ItemAttributeModifiers.Entry modifier : player.getInventory().getArmor(EquipmentSlot.FEET.getIndex()).getAttributeModifiers().modifiers()) {
			if (modifier.matches(Attributes.STEP_HEIGHT, TFAttributeModifiers.TRAVELLERS_HIGH_STEP.id()))
				return (float) (f - modifier.modifier().amount());  // TODO: use multiply modifiers to this one if they are present
		}
		return f;
	}

	/**
	 * {@link ReduceMovementFoodExhaustionTransformer ()}<p/>
	 *
	 * Injection Points:<br/>
	 * {@link net.minecraft.server.level.ServerPlayer#checkMovementStatistics(double dx, double dy, double dz)}
	 * {@link net.minecraft.world.entity.player.Player#jumpFromGround()}
	 */

	public static float getFoodExhaustion(float f, Player player) {
		ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
		Float divisor = chestStack.get(TFDataComponents.EFFICIENT_EATER);
		if (!TravellersModifiersManager.isModifierActive(player.registryAccess(), chestStack, TravellersModifiersManager.FOOD_EFFICIENCY_MODIFIER) || divisor == null)
			return f;
		return f * (1 / divisor);
	}

	/**
	 * {@link GetFieldOfViewModifierTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.player.AbstractClientPlayer#getFieldOfViewModifier()} ()}
	 */
	public static void forwardBoostNullify(AbstractClientPlayer player) {
		AttributeInstance attributeInstance = player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if (attributeInstance == null)
			return;
		AttributeModifier modifier = attributeInstance.getModifier(TFAttributeModifiers.FORWARD_BOOTS_ATTRIBUTE_MODIFIER_LOCATION);
		double multiplier = modifier == null ? 1 : modifier.amount() + 1;
		player.setData(TFDataAttachments.TEMPORARY_SAVED_FORWARD_BOOST, multiplier);
		attributeInstance.removeModifier(TFAttributeModifiers.FORWARD_BOOTS_ATTRIBUTE_MODIFIER_LOCATION);
	}

	/**
	 * {@link GetFieldOfViewModifierTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.player.AbstractClientPlayer#getFieldOfViewModifier()} ()}
	 */
	public static void forwardBoostRestore(AbstractClientPlayer player) {
		if (!(player instanceof LocalPlayer))
			return;
		AttributeInstance attributeInstance = player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if (attributeInstance == null)
			return;
		double multiplier = player.getData(TFDataAttachments.TEMPORARY_SAVED_FORWARD_BOOST);
		attributeInstance.addTransientModifier(new AttributeModifier(TFAttributeModifiers.FORWARD_BOOTS_ATTRIBUTE_MODIFIER_LOCATION, multiplier - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
	}
}
