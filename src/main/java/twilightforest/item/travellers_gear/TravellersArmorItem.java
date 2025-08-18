package twilightforest.item.travellers_gear;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.model.armor.TravellersWingsModel;
import twilightforest.client.renderer.armor.TFArmorRenderer;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.*;

import javax.annotation.Nullable;
import java.util.List;

public class TravellersArmorItem extends ArmorItem implements TravellersModifiable {

	private final int insertableModifierSlots;
	@Nullable
	private ItemAttributeModifiers attributeModifiers;

	public TravellersArmorItem(ArmorItem.Type equipmentType, Properties properties, int insertableModifierSlots) {
		super(TFArmorMaterials.TRAVELLERS, equipmentType, properties.component(TFDataComponents.IS_TRAVELLERS_GEAR, Unit.INSTANCE));
		this.insertableModifierSlots = insertableModifierSlots;
		this.attributeModifiers = this.components().get(DataComponents.ATTRIBUTE_MODIFIERS);
		if (this.attributeModifiers == null)
			return;

		for (ItemAttributeModifiers.Entry modifier : this.getDefaultAttributeModifiers().modifiers()) {
			this.attributeModifiers = this.attributeModifiers.withModifierAdded(modifier.attribute(), modifier.modifier(), modifier.slot());
		}
	}

	public TravellersArmorItem(ArmorItem.Type equipmentType, Properties properties, int insertableModifierSlots, int durability) {
		this(equipmentType, properties.durability(equipmentType.getDurability(durability)), insertableModifierSlots);
	}

	@Override
	public Component getName(ItemStack stack) {
		if (isTravellersArmorAndBroken(stack)) {
			return Component.translatable(this.getDescriptionId(stack)).append(Component.translatable("travellers_gear.broken").withStyle(ChatFormatting.GRAY));
		}
		return super.getName(stack);
	}

	@Override
	public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers() {
		return this.attributeModifiers == null ? super.getDefaultAttributeModifiers() : this.attributeModifiers;
	}

	@Override
	public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		return !innerModel && entity instanceof LocalPlayer && TFKeyBinds.ZOOM_KEY.isDown() ?
			TwilightForestMod.prefix("textures/models/armor/travellers_layer_1_down.png") :
			super.getArmorTexture(stack, entity, slot, layer, innerModel);
	}

	public static Properties gogglesProperties(Properties properties) {
		return properties
			.attributes(defaultArmorProperties(Type.HELMET).build())
			.component(TFDataComponents.ZOOM_ABILITY_MODIFIER, 0.3F);
	}

	public static Properties chestProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE, Unit.INSTANCE)
			.component(TFDataComponents.SWIFT_SWIM, Unit.INSTANCE)
			.attributes(defaultArmorProperties(Type.CHESTPLATE)
				.add(Attributes.WATER_MOVEMENT_EFFICIENCY, TFAttributeModifiers.TRAVELLERS_SWIFT_SWIM, EquipmentSlotGroup.CHEST)
				.build());
	}

	public static Properties glovesProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_GLOVES, Unit.INSTANCE);
	}

	public static Properties wingsProperties(Properties properties) {
		return properties
			.attributes(defaultArmorProperties(Type.LEGGINGS).build())
			.component(TFDataComponents.TRAVELLERS_HAS_WINGS, Unit.INSTANCE)
			.component(TFDataComponents.HIGH_JUMP_AMPLIFIER, 1);
	}

	public static Properties bootsProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_BOOTS, Unit.INSTANCE)
			.component(TFDataComponents.HIGH_STEP, Unit.INSTANCE)
			.attributes(defaultArmorProperties(Type.BOOTS)
				.add(Attributes.STEP_HEIGHT, TFAttributeModifiers.TRAVELLERS_HIGH_STEP, EquipmentSlotGroup.FEET)
				.build());
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flags) {
		super.appendHoverText(stack, context, tooltip, flags);
		if (context.registries() != null) {
			TravellersModifiersManager.findAllAbilityModifiers(context.registries(), stack).forEach(modifier ->
				tooltip.add(Component.translatable("travellers_gear.ability", this.getModifierTooltipComponent(modifier)).withStyle(ChatFormatting.GOLD)));

			List<Holder.Reference<TravellersModifier>> insertableModifiers = TravellersModifiersManager.findAllInsertableModifiers(context.registries(), stack);
			insertableModifiers.forEach(modifier ->
				tooltip.add(Component.literal("- ").append(this.getModifierTooltipComponent(modifier))));

			for (int i = insertableModifiers.size(); i < getModifierSlots(); i++) {
				tooltip.add(Component.literal("- ").append(Component.translatable("travellers_gear.modifier.empty").withStyle(ChatFormatting.DARK_GRAY)));
			}
		}
	}

	@Override
	public boolean isEnchantable(@NotNull ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(@NotNull ItemStack stack, @NotNull ItemStack book) {
		return true;
	}

	@Override
	public boolean isPrimaryItemFor(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
		return false;
	}

	@Override
	public boolean supportsEnchantment(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
		return false;
	}

	@Override
	public boolean isRepairable(@NotNull ItemStack stack) {
		return false;
	}

	@Override
	public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
		return this.material.value().repairIngredient().get().test(repair);
	}

	private Component getModifierTooltipComponent(Holder.Reference<TravellersModifier> modifier) {
		return TooltipStringInterpolator.render(modifier.getKey().location().toLanguageKey(modifier.value().getPrefix())).withStyle(ChatFormatting.GRAY);
	}

	public static boolean isTravellersArmorAndBroken(ItemStack stack) {
		return stack.has(TFDataComponents.IS_TRAVELLERS_GEAR) && stack.isDamageableItem() && stack.getMaxDamage() - 1 <= stack.getDamageValue();
	}

	// [VanillaCopy] modified ArmorItem constructor to just return default attribute modifiers
	public static ItemAttributeModifiers.Builder defaultArmorProperties(Type type) {
		int defense = TFArmorMaterials.TRAVELLERS.value().getDefense(type);
		float toughness = TFArmorMaterials.TRAVELLERS.value().toughness();
		ItemAttributeModifiers.Builder defaultArmorModifiers = ItemAttributeModifiers.builder();
		EquipmentSlotGroup equipmentslotgroup = EquipmentSlotGroup.bySlot(type.getSlot());
		ResourceLocation resourcelocation = ResourceLocation.withDefaultNamespace("armor." + type.getName());
		defaultArmorModifiers.add(
			Attributes.ARMOR, new AttributeModifier(resourcelocation, defense, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup
		);
		defaultArmorModifiers.add(
			Attributes.ARMOR_TOUGHNESS, new AttributeModifier(resourcelocation, toughness, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup
		);
		float knockbackResistance = TFArmorMaterials.TRAVELLERS.value().knockbackResistance();
		if (knockbackResistance > 0.0F) {
			defaultArmorModifiers.add(
				Attributes.KNOCKBACK_RESISTANCE,
				new AttributeModifier(resourcelocation, knockbackResistance, AttributeModifier.Operation.ADD_VALUE),
				equipmentslotgroup
			);
		}
		return defaultArmorModifiers;
	}

	public int getModifierSlots() {
		return insertableModifierSlots;
	}

	public static final class ArmorRender extends TFArmorRenderer {

		public ArmorRender() {
			super(TFModelLayers.TRAVELLERS_ARMOR_HELMET, TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES, TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS, TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
		}

		@Override
		public @NotNull HumanoidModel<?> getHumanoidArmorModel(@NotNull LivingEntity living, @NotNull ItemStack stack, EquipmentSlot slot, @NotNull HumanoidModel<?> model) {
			ModelPart root = switch (slot) {
				case HEAD -> this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_HELMET);
				case CHEST -> {
					ModelPart chestLayer = this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES);
					chestLayer.getAllParts().forEach(part -> part.skipDraw = true);
					boolean hasChestplate = stack.has(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE);
					boolean hasGloves = stack.has(TFDataComponents.TRAVELLERS_HAS_GLOVES);
					chestLayer.getChild("body").skipDraw = !hasChestplate;
					chestLayer.getChild("left_arm").skipDraw = !hasGloves;
					chestLayer.getChild("right_arm").skipDraw = !hasGloves;

					yield chestLayer;
				}
				case LEGS -> {
					ModelPart leggingsLayer = this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS);
					leggingsLayer.getAllParts().forEach(part -> part.skipDraw = true);
					boolean hasWings = stack.has(TFDataComponents.TRAVELLERS_HAS_WINGS);
					boolean hasBelt = stack.has(TFDataComponents.TRAVELLERS_HAS_BELT);

					TravellersWingsModel.skipBelt(leggingsLayer, !hasBelt);
					TravellersWingsModel.skipWings(leggingsLayer, !hasWings);

					yield leggingsLayer;
				}
				case FEET -> this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
				default -> throw new IllegalArgumentException("Unexpected slot: " + slot + ": " + stack + ". Please report to https://github.com/TeamTwilight/twilightforest/issues");
			};

			if (slot == EquipmentSlot.LEGS)
				return new TravellersWingsModel(root);
			return new TFArmorModel(root);
		}

		@Override
		public void setupModelAnimations(@NotNull LivingEntity livingEntity, @NotNull ItemStack itemStack, @NotNull EquipmentSlot equipmentSlot, @NotNull Model model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
			if (model instanceof TravellersWingsModel wingsModel)
				wingsModel.setupModelAnimations(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
	}

	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return this == TFItems.TRAVELLERS_GOGGLES.get() || stack.has(TFDataComponents.TRAVELLERS_HAS_WINGS);
	}
}
