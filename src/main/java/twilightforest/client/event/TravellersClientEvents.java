package twilightforest.client.event;

import twilightforest.forge.network.PacketDistributor;
import net.minecraftforge.event.TickEvent;
import twilightforest.forge.attachment.Attachments;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraftforge.api.distmarker.Dist;
import twilightforest.forge.attachment.AttachmentType;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.MinecraftForge;
import twilightforest.forge.registries.DeferredHolder;
import twilightforest.forge.beans.Component;
import twilightforest.forge.beans.PostConstruct;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.TravellersGearLogic;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;
import twilightforest.network.*;

@Component
public class TravellersClientEvents {

	private static boolean isZoomKeyHeld(Player player) {
		return TFKeyBinds.ZOOM_KEY.isDown() && !player.isScoping();
	}

	@PostConstruct
	private void setup() {
		MinecraftForge.EVENT_BUS.addListener(this::handleDoubleJump);
		MinecraftForge.EVENT_BUS.addListener(this::handleAgileRanger);
		MinecraftForge.EVENT_BUS.addListener(this::handleStraightAhead);
		MinecraftForge.EVENT_BUS.addListener(this::speedUpControlledWhileSneaking);
		MinecraftForge.EVENT_BUS.addListener(this::handleSidestep);
		MinecraftForge.EVENT_BUS.addListener(this::handleStealth);
		MinecraftForge.EVENT_BUS.addListener(this::updateZoomState);
		MinecraftForge.EVENT_BUS.addListener(this::updateGradualGlideState);
		MinecraftForge.EVENT_BUS.addListener(this::cycleItemDisplayMap);
		// PORTAGE : gestionnaire « slowZoomSensitivity » supprime — l'evenement NeoForge
		// correspondant n'existe pas chez Forge (voir PORTAGE-twilightforest-crochets.txt).
		MinecraftForge.EVENT_BUS.addListener(this::swapHotbar);
		MinecraftForge.EVENT_BUS.addListener(this::toggleRedThreadVision);
		MinecraftForge.EVENT_BUS.addListener(this::renderGlovesInFirstPerson);
	}

	private void handleAgileRanger(MovementInputUpdateEvent event) {
		if (!(event.getEntity() instanceof LocalPlayer localPlayer))
			return;
		ItemStack leggingsStack = localPlayer.getItemBySlot(EquipmentSlot.LEGS);
		Float agileRangerModifier = leggingsStack.get(TFDataComponents.AGILE_RANGER_MODIFIER.get());
		if (!TravellersModifiersManager.isModifierActive(localPlayer, leggingsStack, TravellersModifiersManager.AGILE_RANGER_MODIFIER) || agileRangerModifier == null)
			return;
		ItemStack stack = localPlayer.getUseItem();
		boolean isLegalItem = (stack.getItem() instanceof ProjectileWeaponItem || stack.is(ItemTagGenerator.TRAVELLERS_AGILE_RANGER_WHITELISTED)) && !stack.is(ItemTagGenerator.TRAVELLERS_AGILE_RANGER_BLACKLISTED);
		if (localPlayer.isUsingItem() && !localPlayer.isPassenger() && isLegalItem) {
			Input input = event.getInput();
			input.leftImpulse *= agileRangerModifier;
			input.forwardImpulse *= agileRangerModifier;
		}
	}

	private void handleStraightAhead(MovementInputUpdateEvent event) {
		if (!(event.getEntity() instanceof LocalPlayer localPlayer))
			return;
		ItemStack bootsStack = localPlayer.getItemBySlot(EquipmentSlot.FEET);
		Double multiplier = bootsStack.get(TFDataComponents.STRAIGHT_AHEAD_MULTIPLIER.get());
		AttributeInstance attributeInstance = localPlayer.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if (attributeInstance == null)
			return;

		Input input = localPlayer.input;
		if (!TravellersModifiersManager.isModifierActive(localPlayer, bootsStack, TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER) || multiplier == null || input.forwardImpulse <= 0)
			multiplier = 1D;
		attributeInstance.addOrUpdateTransientModifier(new AttributeModifier(TFAttributeModifiers.STRAIGHT_AHEAD_ATTRIBUTE_MODIFIER_LOCATION, multiplier - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		input.leftImpulse /= multiplier;
	}

	private void speedUpControlledWhileSneaking(MovementInputUpdateEvent event) {
		if (!(event.getEntity() instanceof LocalPlayer localPlayer) || !Attachments.get(localPlayer, TFDataAttachments.IS_GRADUALLY_GLIDING) || !localPlayer.isShiftKeyDown())
			return;
		localPlayer.input.forwardImpulse /= 0.2F;
		localPlayer.input.leftImpulse /= 0.2F;
	}

	private void handleSidestep(MovementInputUpdateEvent event) {
		if (!(event.getEntity() instanceof LocalPlayer localPlayer) || !localPlayer.onGround())
			return;

		Input input = localPlayer.input;
		boolean lastImpulseZero = Attachments.get(localPlayer, TFDataAttachments.LAST_HORIZONTAL_IMPULSE) == 0;
		boolean sameImpulseDirection = Math.signum(Attachments.get(localPlayer, TFDataAttachments.LAST_NON_ZERO_HORIZONTAL_IMPULSE)) == Math.signum(input.leftImpulse);
		int currentTime = localPlayer.tickCount;
		int lastWalkingTime = Attachments.get(localPlayer, TFDataAttachments.LAST_HORIZONTAL_WALKING_TIME);
		boolean hasDoubleTapped = currentTime - lastWalkingTime < 4;

		if (lastImpulseZero && sameImpulseDirection && hasDoubleTapped && input.leftImpulse != 0) {
			boolean isLeftSidestep = input.leftImpulse > 0;
			if (TravellersGearLogic.tryPerformSidestep(localPlayer, isLeftSidestep)) {
				PacketDistributor.sendToServer(new PerformSidestepPacket(isLeftSidestep));
			}
		}

		Attachments.set(localPlayer, TFDataAttachments.LAST_HORIZONTAL_IMPULSE, input.leftImpulse);
		if (input.leftImpulse != 0) {
			Attachments.set(localPlayer, TFDataAttachments.LAST_HORIZONTAL_WALKING_TIME, currentTime);
			Attachments.set(localPlayer, TFDataAttachments.LAST_NON_ZERO_HORIZONTAL_IMPULSE, input.leftImpulse);
		}
	}

	private void handleStealth(TickEvent.RenderTickEvent event) {
		if (Minecraft.getInstance().level == null)
			return;
		for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
			if (!(entity instanceof Player player)) continue;
			TravellersGearLogic.travellersStealth(player, player1 -> player1.setInvisible(true));  // call it on client to make player invisible instantly
		}
	}

	private void handleDoubleJump(InputEvent.Key event) {
		if (!(Minecraft.getInstance().player instanceof LocalPlayer localPlayer) || ignoreKeyEvent(event, Minecraft.getInstance().options.keyJump))
			return;
		int lastJumpKeyPressTime = Attachments.get(localPlayer, TFDataAttachments.LAST_JUMP_KEY_PRESS_TIME);
		boolean pressedKey = event.getAction() == InputConstants.PRESS;
		if (pressedKey)
			Attachments.set(localPlayer, TFDataAttachments.LAST_JUMP_KEY_PRESS_TIME, localPlayer.tickCount);
		boolean avoidCreativeFly = localPlayer.getAbilities().mayfly && localPlayer.tickCount - lastJumpKeyPressTime <= 6;
		if (pressedKey && !avoidCreativeFly && TravellersModifiersManager.isModifierActive(localPlayer, TravellersModifiersManager.DOUBLE_JUMP_MODIFIER)) {
			if (TravellersGearLogic.performDoubleJump(localPlayer)) {
				PacketDistributor.sendToServer(new PerformDoubleJumpPacket());
			}
		}
	}

	private void updateZoomState(ComputeFovModifierEvent event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;
		boolean wasUsingZoom = Attachments.get(player, TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER);
		ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
		Float zoomModifier = headStack.get(TFDataComponents.ZOOM_ABILITY_MODIFIER.get());
		boolean isUsingZoom = isZoomKeyHeld(player) && TravellersModifiersManager.isModifierActive(player, headStack, TravellersModifiersManager.ZOOM_ABILITY) && zoomModifier != null;
		if (isUsingZoom)
			event.setNewFovModifier(event.getNewFovModifier() * zoomModifier);
		if (isUsingZoom == wasUsingZoom)
			return;

		Attachments.set(player, TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER, isUsingZoom);
		player.playSound(isUsingZoom ? TFSounds.GOGGLES_ZOOM_IN.get() : TFSounds.GOGGLES_ZOOM_OUT.get());
		PacketDistributor.sendToServer(new GogglesZoomPacket(isUsingZoom, player.getUUID()));
	}

	private void updateGradualGlideState(TickEvent.RenderTickEvent event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;
		boolean wasGraduallyGliding = Attachments.get(player, TFDataAttachments.IS_GRADUALLY_GLIDING);
		boolean shiftHeld = player.isShiftKeyDown();
		boolean isGraduallyGliding = TFConfig.manualTravellersWingsGradualGlideDefault == shiftHeld && player.getKnownMovement().y() < 0 && !player.onGround();
		if (isGraduallyGliding == wasGraduallyGliding)
			return;

		Attachments.set(player, TFDataAttachments.IS_GRADUALLY_GLIDING, isGraduallyGliding);
		PacketDistributor.sendToServer(new GradualGlidePacket(isGraduallyGliding, player.getUUID()));
	}

	private void cycleItemDisplayMap(InputEvent.Key event) {
		if (!(Minecraft.getInstance().player instanceof LocalPlayer localPlayer) || !TFKeyBinds.ITEM_DISPLAY_MAP_CYCLE_KEY.consumeClick())
			return;
		PacketDistributor.sendToServer(CycleMapSlotPacket.INSTANCE);
	}

	private void swapHotbar(InputEvent.Key event) {
		if (!TFKeyBinds.SWAP_HOTBAR_KEY.consumeClick())
			return;
		Player player = Minecraft.getInstance().player;
		if (!(player instanceof LocalPlayer localPlayer)) return;
		ItemStack legArmor = localPlayer.getItemBySlot(EquipmentSlot.LEGS);
		ItemContainerContents containerContents = legArmor.get(DataComponents.CONTAINER);
		if (!TravellersArmorBeltItem.hasSwapHotbar(player, legArmor) || containerContents == null)
			return;
		PacketDistributor.sendToServer(SwapHotbarPacket.INSTANCE);
	}

	private void toggleRedThreadVision(InputEvent.Key event) {
		this.toggleBooleanDataAttachment(TFKeyBinds.RED_THREAD_VISION_KEY.consumeClick(), TravellersModifiersManager.RED_THREAD_VISION_MODIFIER, TFDataAttachments.TRAVELLERS_GOGGLES_RED_THREAD_VISION);
	}

	private void toggleBooleanDataAttachment(boolean pressed, ResourceKey<TravellersModifier> modifier, AttachmentType<Boolean> attachment) {
		if (!pressed)
			return;

		Player player = Minecraft.getInstance().player;
		if (player == null || !TravellersModifiersManager.isModifierActive(player, modifier))
			return;

		boolean current = Attachments.get(player, attachment);
		Attachments.set(player, attachment, !current);
	}


	// PORTAGE : gestionnaire supprime, l'evenement correspondant n'existe pas chez Forge
	// (voir PORTAGE-twilightforest-crochets.txt).

	private boolean ignoreKeyEvent(InputEvent.Key event, KeyMapping key) {
		return !key.matches(event.getKey(), event.getScanCode()) || event.getAction() != InputConstants.PRESS || Minecraft.getInstance().screen != null;
	}

	@SuppressWarnings("unchecked") //meh
	private void renderGlovesInFirstPerson(RenderArmEvent event) {
		if (TFConfig.firstPersonGloveOverlay) {
			AbstractClientPlayer player = event.getPlayer();
			ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
			if (chestStack.has(TFDataComponents.TRAVELLERS_HAS_GLOVES.get()) && !chestStack.has(TFDataComponents.EMPERORS_CLOTH.get())) {
				PlayerRenderer renderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
				HumanoidModel<AbstractClientPlayer> model = (HumanoidModel<AbstractClientPlayer>) IClientItemExtensions.of(TFItems.TRAVELLERS_GLOVES.get()).getHumanoidArmorModel(player, chestStack, EquipmentSlot.CHEST, renderer.getModel());
				ModelPart armPart = event.getArm() == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
				model.attackTime = 0.0F;
				model.crouching = false;
				model.swimAmount = 0.0F;
				model.setupAnim(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
				armPart.xRot = 0.0F;
				ResourceLocation gloveLocation = TwilightForestMod.prefix("textures/models/armor/travellers_layer_1.png");
				armPart.render(event.getPoseStack(), event.getMultiBufferSource().getBuffer(RenderType.armorCutoutNoCull(gloveLocation)), event.getPackedLight(), OverlayTexture.NO_OVERLAY);
			}
		}
	}
}
