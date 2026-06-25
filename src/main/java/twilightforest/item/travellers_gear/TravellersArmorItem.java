package twilightforest.item.travellers_gear;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.common.util.ConcatenatedListView;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.model.armor.TravellersWingsModel;
import twilightforest.client.renderer.armor.TFArmorRenderer;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiable;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TravellersArmorItem extends Item implements TravellersModifiable {
	private static final MutableComponent GLOVES_TOOLTIP = Component.translatable("item.twilightforest.travellers_gloves.desc").withStyle(ChatFormatting.GRAY);
	private final int insertableModifierSlots;

	public TravellersArmorItem(int insertableModifierSlots, Properties properties) {
		super(properties.component(TFDataComponents.IS_TRAVELLERS_GEAR, Unit.INSTANCE));
		this.insertableModifierSlots = insertableModifierSlots;
	}

	@Override
	public Component getName(ItemStack stack) {
		if (isTravellersArmorAndBroken(stack)) {
			return super.getName(stack).copy().append(Component.translatable("travellers_gear.broken").withStyle(ChatFormatting.GRAY));
		}
		return super.getName(stack);
	}

	public static Properties gogglesProperties(Properties properties) {
		return properties
			.attributes(defaultArmorProperties(ArmorType.HELMET).build())
			.component(TFDataComponents.ZOOM_ABILITY_MODIFIER, 0.3F);
	}

	public static Properties chestProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE, Unit.INSTANCE)
			.component(TFDataComponents.SWIFT_SWIM, Unit.INSTANCE)
			.attributes(defaultArmorProperties(ArmorType.CHESTPLATE)
				.add(Attributes.WATER_MOVEMENT_EFFICIENCY, TFAttributeModifiers.TRAVELLERS_SWIFT_SWIM, EquipmentSlotGroup.CHEST)
				.build());
	}

	public static Properties glovesProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_GLOVES, Unit.INSTANCE);
	}

	public static Properties wingsProperties(Properties properties) {
		return properties
			.attributes(defaultArmorProperties(ArmorType.LEGGINGS).build())
			.component(TFDataComponents.TRAVELLERS_HAS_WINGS, Unit.INSTANCE)
			.component(TFDataComponents.HIGH_JUMP_AMPLIFIER, 1);
	}

	public static Properties bootsProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_BOOTS, Unit.INSTANCE)
			.component(TFDataComponents.HIGH_STEP, Unit.INSTANCE)
			.attributes(defaultArmorProperties(ArmorType.BOOTS)
				.add(Attributes.STEP_HEIGHT, TFAttributeModifiers.TRAVELLERS_HIGH_STEP, EquipmentSlotGroup.FEET)
				.build());
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, builder, flag);
		HolderLookup.Provider registries = context.registries();
		if (registries == null)
			return;

		List<Holder.Reference<TravellersModifier>> abilityModifiers = TravellersModifiersManager.findAllAbilityModifiers(registries, stack);
		for (Holder.Reference<TravellersModifier> travellersModifierReference : abilityModifiers) {
			builder.accept(Component.translatable("travellers_gear.ability", TravellersModifiersManager.getModifierTooltipComponent(travellersModifierReference).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.GOLD));
		}

		List<Holder.Reference<TravellersModifier>> insertableModifiers = TravellersModifiersManager.findAllInsertableModifiers(registries, stack);
		for (Holder.Reference<TravellersModifier> modifier : insertableModifiers) {
			builder.accept(Component.literal("- ").append(TravellersModifiersManager.getModifierTooltipComponent(modifier).withStyle(ChatFormatting.GRAY)));
			if (flag.hasShiftDown()) {
				for (Component description : modifier.value().getDescription()) {
					// FIXME There has to be a better way to bold only the indent and arrow and not the information component
					builder.accept(Component.literal("").append(Component.translatable("travellers_gear.info_indent").withStyle(ChatFormatting.BOLD)).append(description));
				}
			}
		}

		for (int i = insertableModifiers.size(); i < getModifierSlots(); i++) {
			builder.accept(Component.literal("- ").append(Component.translatable("travellers_gear.modifier.empty").withStyle(ChatFormatting.DARK_GRAY)));
		}

		if (TFItems.TRAVELLERS_GLOVES.get() == this) {
			builder.accept(GLOVES_TOOLTIP);
		}

		if (!flag.hasShiftDown()) {
			ConcatenatedListView<Holder.Reference<TravellersModifier>> modifiers = ConcatenatedListView.of(abilityModifiers, insertableModifiers);
			boolean hasHiddenDescriptions = modifiers.stream().map(Holder::value).map(TravellersModifier::getDescription).anyMatch(Predicate.not(List::isEmpty));
			if (hasHiddenDescriptions) {
				builder.accept(Component.translatable("travellers_gear.shift_info", Component.literal("Shift").withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.WHITE));
			}
		}
	}

	@Override
	public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
		return false;
	}

	@Override
	public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
		return false;
	}

	@Override
	public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
		return stack.is(TFItems.TRAVELLERS_BOOTS);
	}

	public static boolean isTravellersArmorAndBroken(ItemStack stack) {
		return stack.has(TFDataComponents.IS_TRAVELLERS_GEAR) && stack.isDamageableItem() && stack.getMaxDamage() > 0 && stack.getMaxDamage() - 1 <= stack.getDamageValue();
	}

	// [VanillaCopy] modified ArmorItem constructor to just return default attribute modifiers
	public static ItemAttributeModifiers.Builder defaultArmorProperties(ArmorType type) {
		ArmorMaterial material = TFArmorMaterials.TRAVELLERS_GEAR;
		int defense = material.defense().getOrDefault(type, 0);
		ItemAttributeModifiers.Builder modifiers = ItemAttributeModifiers.builder();
		EquipmentSlotGroup slotGroup = EquipmentSlotGroup.bySlot(type.getSlot());
		Identifier modifierId = Identifier.withDefaultNamespace("armor." + type.getName());
		modifiers.add(Attributes.ARMOR, new AttributeModifier(modifierId, defense, AttributeModifier.Operation.ADD_VALUE), slotGroup);
		modifiers.add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(modifierId, material.toughness(), AttributeModifier.Operation.ADD_VALUE), slotGroup);
		if (material.knockbackResistance() > 0.0F) {
			modifiers.add(
				Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(modifierId, material.knockbackResistance(), AttributeModifier.Operation.ADD_VALUE), slotGroup
			);
		}

		return modifiers;
	}

	public int getModifierSlots() {
		return insertableModifierSlots;
	}

	public static final class ArmorRender extends TFArmorRenderer {

		public ArmorRender() {
			super(TFModelLayers.TRAVELLERS_ARMOR_HELMET, TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES, TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM, TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS, TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
		}

		@Nullable
		@Override
		public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer, Identifier def) {
			LivingEntity entity = net.minecraft.client.Minecraft.getInstance().player;
			return type != EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS && entity != null && entity.getData(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER) ?
				TwilightForestMod.prefix("textures/models/armor/travellers_layer_1_down.png") :
				super.getArmorTexture(stack, type, layer, def);
		}

		@Override
		@SuppressWarnings("rawtypes")
		public Model<?> getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType layerType, Model model) {
			if (stack.has(DataComponents.EQUIPPABLE)) {
				EquipmentSlot slot = stack.get(DataComponents.EQUIPPABLE).slot();
				ModelPart root = switch (slot) {
					case HEAD -> {
						ModelPart headLayer = this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_HELMET);
						headLayer.getAllParts().forEach(part -> part.skipDraw = true);
						headLayer.getChild("head").skipDraw = false;
						yield headLayer;
					}
					case CHEST -> {
						ModelPart chestLayer = this.getModelPart(this.isModelSlim(model) ? TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM : TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES);
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
						boolean hasBelt = stack.has(TFDataComponents.TRAVELLERS_HAS_BELT) || TravellersModifiersManager.hasTravellersModifier(Minecraft.getInstance().level.registryAccess(), stack, TravellersModifiersManager.SWAP_HOTBAR_MODIFIER);

						TravellersWingsModel.skipBelt(leggingsLayer, !hasBelt);
						TravellersWingsModel.skipWings(leggingsLayer, !hasWings);

						yield leggingsLayer;
					}
					case FEET -> {
						ModelPart bootsLayer = this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
						bootsLayer.getAllParts().forEach(part -> part.skipDraw = true);
						bootsLayer.getChild("right_leg").skipDraw = false;
						bootsLayer.getChild("left_leg").skipDraw = false;
						yield bootsLayer;
					}
					default -> null;
				};


				if (slot == EquipmentSlot.LEGS) {
					return new TravellersWingsModel(root);
				} else if (root != null) {
					return new TFArmorModel(root);
				}
			}
			return super.getHumanoidArmorModel(stack, layerType, model);
		}

		@Override
		@SuppressWarnings("rawtypes")
		public void setupModelAnimations(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, Model model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
			if (model instanceof TravellersWingsModel wingsModel)
				wingsModel.setupModelAnimations(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}

		private boolean isModelSlim(Model<?> model) {
			if (model instanceof PlayerModel player) return player.slim;
			return false;
		}
	}

	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return this == TFItems.TRAVELLERS_GOGGLES.get() || stack.has(TFDataComponents.TRAVELLERS_HAS_WINGS);
	}
}
