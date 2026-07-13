package twilightforest.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.TravellersGearLogic;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;
import twilightforest.network.*;
import twilightforest.tags.TFItemTags;

@Component(dist = Dist.CLIENT)
public class TravellersClientEvents {

	private static boolean isZoomKeyHeld(Player player) {
		return TFKeyBinds.ZOOM_KEY.isDown() && !player.isScoping();
	}

	@PostConstruct
	private void setup() {
		NeoForge.EVENT_BUS.addListener(this::handleDoubleJump);
		NeoForge.EVENT_BUS.addListener(this::handleAgileRanger);
		NeoForge.EVENT_BUS.addListener(this::handleStraightAhead);
		NeoForge.EVENT_BUS.addListener(this::speedUpControlledWhileSneaking);
		NeoForge.EVENT_BUS.addListener(this::handleSidestep);
		NeoForge.EVENT_BUS.addListener(this::handleStealth);
		NeoForge.EVENT_BUS.addListener(this::updateZoomState);
		NeoForge.EVENT_BUS.addListener(this::updateGradualGlideState);
		NeoForge.EVENT_BUS.addListener(this::cycleItemDisplayMap);
		NeoForge.EVENT_BUS.addListener(this::slowZoomSensitivity);
		NeoForge.EVENT_BUS.addListener(this::swapHotbar);
		NeoForge.EVENT_BUS.addListener(this::toggleRedThreadVision);
		NeoForge.EVENT_BUS.addListener(this::renderGlovesInFirstPerson);
	}

	private static final Identifier AGILE_RANGER_ID = Identifier.fromNamespaceAndPath("twilightforest", "agile_ranger_speed");

	private void handleAgileRanger(MovementInputUpdateEvent event) {
		if (!(event.getEntity() instanceof LocalPlayer localPlayer))
			return;

		var speedAttribute = localPlayer.getAttribute(Attributes.MOVEMENT_SPEED);
		if (speedAttribute == null)
			return;

		ItemStack leggingsStack = localPlayer.getItemBySlot(EquipmentSlot.LEGS);
		Float agileRangerModifier = leggingsStack.get(TFDataComponents.AGILE_RANGER_MODIFIER);

		boolean isModifierActive = TravellersModifiersManager.isModifierActive(localPlayer, leggingsStack, TravellersModifiersManager.AGILE_RANGER_MODIFIER) && agileRangerModifier != null;
		ItemStack stack = localPlayer.getUseItem();
		boolean isLegalItem = (stack.getItem() instanceof ProjectileWeaponItem || stack.is(TFItemTags.TRAVELLERS_AGILE_RANGER_WHITELISTED)) && !stack.is(TFItemTags.TRAVELLERS_AGILE_RANGER_BLACKLISTED);
		if (localPlayer.isUsingItem() && !localPlayer.isPassenger() && isLegalItem) {
			Input input = event.getInput();
			input.leftImpulse *= agileRangerModifier;
			input.forwardImpulse *= agileRangerModifier;
		}
	}

	private static final Identifier STRAIGHT_AHEAD_ID = Identifier.fromNamespaceAndPath("twilightforest", "straight_ahead_speed");

	private void handleStraightAhead(MovementInputUpdateEvent event) {
		if (!(event.getEntity() instanceof LocalPlayer localPlayer))
			return;

		AttributeInstance attributeInstance = localPlayer.getAttribute(Attributes.MOVEMENT_SPEED);
		if (attributeInstance == null)
			return;

		ItemStack bootsStack = localPlayer.getItemBySlot(EquipmentSlot.FEET);
		Double multiplier = bootsStack.get(TFDataComponents.STRAIGHT_AHEAD_MULTIPLIER);

		ClientInput input = event.getInput();

		boolean isMovingForward = input.keyPresses.forward();
		boolean isStrafing = input.keyPresses.left() || input.keyPresses.right();
		boolean isModifierActive = TravellersModifiersManager.isModifierActive(localPlayer, bootsStack, TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER) && multiplier != null;

		if (isModifierActive && isMovingForward) {
			if (!attributeInstance.hasModifier(STRAIGHT_AHEAD_ID)) {
				attributeInstance.addTransientModifier(new AttributeModifier(
					STRAIGHT_AHEAD_ID,
					multiplier - 1.0,
					AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
				));
			}

			if (isStrafing) {
				Vec3 currentVelocity = localPlayer.getDeltaMovement();

				Vec3 lookDirection = localPlayer.getLookAngle();
				Vec3 rightVector = new Vec3(-lookDirection.z, 0, lookDirection.x).normalize();

				double sideVelocity = currentVelocity.dot(rightVector);

				double reductionFactor = 1.0 - (1.0 / multiplier);
				Vec3 counterImpulse = rightVector.scale(-sideVelocity * reductionFactor);

				localPlayer.setDeltaMovement(currentVelocity.add(counterImpulse));
			}
		} else {
			if (attributeInstance.hasModifier(STRAIGHT_AHEAD_ID)) {
				attributeInstance.removeModifier(STRAIGHT_AHEAD_ID);
			}
		}
	}

	private static final Identifier GLIDE_SNEAK_SPEED_ID = Identifier.fromNamespaceAndPath("twilightforest", "glide_sneak_speed");

	private void speedUpControlledWhileSneaking(MovementInputUpdateEvent event) {
		if (!(event.getEntity() instanceof LocalPlayer localPlayer))
			return;

		var sneakSpeedAttribute = localPlayer.getAttribute(Attributes.SNEAKING_SPEED);
		if (sneakSpeedAttribute == null)
			return;

		boolean isGlidingAndSneaking = localPlayer.getData(TFDataAttachments.IS_GRADUALLY_GLIDING) && localPlayer.isShiftKeyDown();

		if (isGlidingAndSneaking) {
			if (!sneakSpeedAttribute.hasModifier(GLIDE_SNEAK_SPEED_ID)) {
				sneakSpeedAttribute.addTransientModifier(new AttributeModifier(
					GLIDE_SNEAK_SPEED_ID,
					2.333333F,
					AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
				));
			}
		} else {
			if (sneakSpeedAttribute.hasModifier(GLIDE_SNEAK_SPEED_ID)) {
				sneakSpeedAttribute.removeModifier(GLIDE_SNEAK_SPEED_ID);
			}
		}
	}

	private void handleSidestep(MovementInputUpdateEvent event) {
		if (!(event.getEntity() instanceof LocalPlayer localPlayer) || !localPlayer.onGround())
			return;

		ClientInput input = localPlayer.input;
		boolean lastImpulseZero = localPlayer.getData(TFDataAttachments.LAST_HORIZONTAL_IMPULSE) == 0;
		boolean sameImpulseDirection = Math.signum(localPlayer.getData(TFDataAttachments.LAST_NON_ZERO_HORIZONTAL_IMPULSE)) == Math.signum(input.getMoveVector().x);
		int currentTime = localPlayer.tickCount;
		int lastWalkingTime = localPlayer.getData(TFDataAttachments.LAST_HORIZONTAL_WALKING_TIME);
		boolean hasDoubleTapped = currentTime - lastWalkingTime < 4;

		if (lastImpulseZero && sameImpulseDirection && hasDoubleTapped && input.getMoveVector().x != 0) {
			boolean isLeftSidestep = input.getMoveVector().x > 0;
			if (TravellersGearLogic.tryPerformSidestep(localPlayer, isLeftSidestep)) {
				localPlayer.connection.send(new PerformSidestepPacket(isLeftSidestep));
			}
		}

		localPlayer.setData(TFDataAttachments.LAST_HORIZONTAL_IMPULSE, input.getMoveVector().x);
		if (input.getMoveVector().x != 0) {
			localPlayer.setData(TFDataAttachments.LAST_HORIZONTAL_WALKING_TIME, currentTime);
			localPlayer.setData(TFDataAttachments.LAST_NON_ZERO_HORIZONTAL_IMPULSE, input.getMoveVector().x);
		}
	}

	private void handleStealth(RenderFrameEvent.Pre event) {
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
		int lastJumpKeyPressTime = localPlayer.getData(TFDataAttachments.LAST_JUMP_KEY_PRESS_TIME);
		boolean pressedKey = event.getAction() == InputConstants.PRESS;
		if (pressedKey)
			localPlayer.setData(TFDataAttachments.LAST_JUMP_KEY_PRESS_TIME, localPlayer.tickCount);
		boolean avoidCreativeFly = localPlayer.mayFly() && localPlayer.tickCount - lastJumpKeyPressTime <= 6;
		if (pressedKey && !avoidCreativeFly && TravellersModifiersManager.isModifierActive(localPlayer, TravellersModifiersManager.DOUBLE_JUMP_MODIFIER)) {
			if (TravellersGearLogic.performDoubleJump(localPlayer)) {
				localPlayer.connection.send(new PerformDoubleJumpPacket());
			}
		}
	}

	private void updateZoomState(ComputeFovModifierEvent event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;
		boolean wasUsingZoom = player.getData(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER);
		ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
		Float zoomModifier = headStack.get(TFDataComponents.ZOOM_ABILITY_MODIFIER);
		boolean isUsingZoom = isZoomKeyHeld(player) && TravellersModifiersManager.isModifierActive(player, headStack, TravellersModifiersManager.ZOOM_ABILITY) && zoomModifier != null;
		if (isUsingZoom)
			event.setNewFovModifier(event.getNewFovModifier() * zoomModifier);
		if (isUsingZoom == wasUsingZoom)
			return;

		player.setData(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER, isUsingZoom);
		player.playSound(isUsingZoom ? TFSounds.GOGGLES_ZOOM_IN.get() : TFSounds.GOGGLES_ZOOM_OUT.get());
		player.connection.send(new GogglesZoomPacket(isUsingZoom, player.getUUID()));
	}

	private void updateGradualGlideState(RenderFrameEvent.Pre event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;
		boolean wasGraduallyGliding = player.getData(TFDataAttachments.IS_GRADUALLY_GLIDING);
		boolean shiftHeld = player.isShiftKeyDown();
		boolean isGraduallyGliding = TFConfig.manualTravellersWingsGradualGlideDefault == shiftHeld && player.getKnownMovement().y() < 0 && !player.onGround();
		if (isGraduallyGliding == wasGraduallyGliding)
			return;

		player.setData(TFDataAttachments.IS_GRADUALLY_GLIDING, isGraduallyGliding);
		player.connection.send(new GradualGlidePacket(isGraduallyGliding, player.getUUID()));
	}

	private void cycleItemDisplayMap(InputEvent.Key event) {
		if (!(Minecraft.getInstance().player instanceof LocalPlayer localPlayer) || !TFKeyBinds.ITEM_DISPLAY_MAP_CYCLE_KEY.consumeClick())
			return;
		localPlayer.connection.send(CycleMapSlotPacket.INSTANCE);
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
		localPlayer.connection.send(SwapHotbarPacket.INSTANCE);
	}

	private void toggleRedThreadVision(InputEvent.Key event) {
		this.toggleBooleanDataAttachment(TFKeyBinds.RED_THREAD_VISION_KEY.consumeClick(), TravellersModifiersManager.RED_THREAD_VISION_MODIFIER, TFDataAttachments.TRAVELLERS_GOGGLES_RED_THREAD_VISION);
	}

	private void toggleBooleanDataAttachment(boolean pressed, ResourceKey<TravellersModifier> modifier, DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> attachment) {
		if (!pressed)
			return;

		Player player = Minecraft.getInstance().player;
		if (player == null || !TravellersModifiersManager.isModifierActive(player, modifier))
			return;

		boolean current = player.getData(attachment.get());
		player.setData(attachment.get(), !current);
	}

	private void slowZoomSensitivity(CalculatePlayerTurnEvent event) {
		Player player = Minecraft.getInstance().player; // Player is never null but we need to check for null to avoid warnings
		if (event.getCinematicCameraEnabled() || player == null)
			return;

		ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
		Float zoomModifier = headStack.get(TFDataComponents.ZOOM_ABILITY_MODIFIER);
		if (zoomModifier == null || !isZoomKeyHeld(player))
			return;

		double mouseSensitivity = event.getMouseSensitivity();
		// vanilla math for turning is (m * 0.6 + 0.2)³ * 8; where m is the mouse sensitivity
		// vanilla spyglasses avoid using the "* 8" part, so we probably want to as well
		// the mod value to reverse that was borrowed from IE since they also have zoom functionality
		// we can then divide by our zoom modifier (and add 0.05 to slow it down slightly) to set the sensitivity to a reasonable value when zooming
		double mod = 0.5D - 1 / (6 * mouseSensitivity);
		double fovMod = zoomModifier + 0.05F;
		event.setMouseSensitivity(mod * mouseSensitivity / fovMod);
	}

	private boolean ignoreKeyEvent(InputEvent.Key event, KeyMapping key) {
		return !key.matches(new KeyEvent(event.getKey(), event.getScanCode(), event.getModifiers())) || event.getAction() != InputConstants.PRESS || Minecraft.getInstance().screen != null;
	}

	@SuppressWarnings("unchecked")
	private void renderGlovesInFirstPerson(RenderArmEvent event) {
        if (!TFConfig.firstPersonGloveOverlay)
			return;

        AbstractClientPlayer player = event.getPlayer();
        ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!chestStack.has(TFDataComponents.TRAVELLERS_HAS_GLOVES) || chestStack.has(TFDataComponents.EMPERORS_CLOTH))
            return;

		Minecraft minecraft = Minecraft.getInstance();
		EntityRenderDispatcher renderDispatcher = minecraft.getEntityRenderDispatcher();

		if (!(renderDispatcher.getRenderer(player) instanceof AvatarRenderer avatarRenderer))
            return;

		if (!(IClientItemExtensions.of(TFItems.TRAVELLERS_GLOVES.get()).getHumanoidArmorModel(chestStack, EquipmentClientInfo.LayerType.HUMANOID, avatarRenderer.getModel()) instanceof HumanoidModel model))
			return;

		if (!(avatarRenderer.createRenderState(player, minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false)) instanceof AvatarRenderState renderState))
			return;

		renderState.attackTime = 0.0F;
		renderState.isCrouching = false;
		renderState.swimAmount = 0.0F;
        model.setupAnim(renderState);

		ModelPart armPart = event.getArm() == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
        armPart.xRot = 0.0F;

        Identifier gloveLocation = TwilightForestMod.prefix("textures/models/armor/travellers_layer_1.png");
		event.getSubmitNodeCollector().submitModelPart(armPart, event.getPoseStack(), RenderTypes.armorCutoutNoCull(gloveLocation), event.getPackedLight(), OverlayTexture.NO_OVERLAY, null);
    }
}
