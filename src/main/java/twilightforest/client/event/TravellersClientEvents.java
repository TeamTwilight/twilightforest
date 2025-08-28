package twilightforest.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import twilightforest.beans.Component;
import twilightforest.beans.PostConstruct;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.TravellersArmorItem;
import twilightforest.item.travellers_gear.TravellersGearLogic;
import twilightforest.network.PerformDoubleJumpPacket;
import twilightforest.network.PerformSidestepPacket;
import twilightforest.network.SwapHotbarPacket;

@Component(dist = Dist.CLIENT)
public class TravellersClientEvents {

	@PostConstruct
	private void setup() {
		NeoForge.EVENT_BUS.addListener(this::tickEntityGearEffects);
		NeoForge.EVENT_BUS.addListener(this::tickPlayerGearEffects);
		NeoForge.EVENT_BUS.addListener(this::handleAgileRanger);
		NeoForge.EVENT_BUS.addListener(this::handleForwardBoost);
		NeoForge.EVENT_BUS.addListener(this::handleSidestep);
		NeoForge.EVENT_BUS.addListener(this::handleStealth);
		NeoForge.EVENT_BUS.addListener(this::updateZoomFOV);
		NeoForge.EVENT_BUS.addListener(this::slowZoomSensitivity);
		NeoForge.EVENT_BUS.addListener(this::swapHotbar);
		NeoForge.EVENT_BUS.addListener(this::toggleItemDisplayVisibility);
		NeoForge.EVENT_BUS.addListener(this::toggleRedThreadVision);
		NeoForge.EVENT_BUS.addListener(this::playZoomSounds);
	}

	private void tickEntityGearEffects(LevelTickEvent.Post event) {
		Level level = event.getLevel();
		if (level instanceof ClientLevel clientLevel) {
			clientLevel.entitiesForRendering().forEach(entity -> {
				if (!(entity instanceof LivingEntity livingEntity))
					return;
				TravellersGearLogic.travellersWingsControlledFall(livingEntity);
			});
		}
	}

	private void tickPlayerGearEffects(PlayerTickEvent.Pre event) {
		if (!(event.getEntity() instanceof LocalPlayer localPlayer))
			return;
		long lastJumpKeyPressTime = localPlayer.getData(TFDataAttachments.LAST_JUMP_KEY_PRESS_TIME);
		long currentJumpKeyPressTime = localPlayer.level().getGameTime();
		boolean holdsJumpKey = currentJumpKeyPressTime - lastJumpKeyPressTime <= 1;
		boolean pressedKey = Minecraft.getInstance().options.keyJump.isDown();
		if (pressedKey)
			localPlayer.setData(TFDataAttachments.LAST_JUMP_KEY_PRESS_TIME, currentJumpKeyPressTime);

		if (pressedKey && !holdsJumpKey && TravellersModifiersManager.isModifierActive(localPlayer, localPlayer.getItemBySlot(EquipmentSlot.LEGS), TravellersModifiersManager.DOUBLE_JUMP_MODIFIER)) {
			if (TravellersGearLogic.performDoubleJump(localPlayer)) {
				localPlayer.getData(TFDataAttachments.TRAVELLERS_WINGS_ANIM).doubleJump = true;
				localPlayer.connection.send(new PerformDoubleJumpPacket());
			}
		}
	}

	private void handleAgileRanger(MovementInputUpdateEvent event) {
		if (!(event.getEntity() instanceof LocalPlayer localPlayer))
			return;
		ItemStack leggingsStack = localPlayer.getItemBySlot(EquipmentSlot.LEGS);
		Float agileRangerModifier = leggingsStack.get(TFDataComponents.AGILE_RANGER_MODIFIER);
		if (!TravellersModifiersManager.isModifierActive(localPlayer, leggingsStack, TravellersModifiersManager.AGILE_RANGER_MODIFIER) || agileRangerModifier == null)
			return;
		if (localPlayer.isUsingItem() && !localPlayer.isPassenger() && localPlayer.getUseItem().getItem() instanceof ProjectileWeaponItem) {
			Input input = event.getInput();
			input.leftImpulse *= agileRangerModifier;
			input.forwardImpulse *= agileRangerModifier;
		}
	}

	private void handleForwardBoost(MovementInputUpdateEvent event) {
		if (!(event.getEntity() instanceof LocalPlayer localPlayer))
			return;
		ItemStack bootsStack = localPlayer.getItemBySlot(EquipmentSlot.FEET);
		Double multiplier = bootsStack.get(TFDataComponents.FORWARD_BOOST_MULTIPLIER);
		AttributeInstance attributeInstance = localPlayer.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if (attributeInstance == null)
			return;

		Input input = localPlayer.input;
		if (!TravellersModifiersManager.isModifierActive(localPlayer, bootsStack, TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER) || multiplier == null || input.forwardImpulse <= 0 || localPlayer.isInLiquid())
			multiplier = 1D;
		attributeInstance.addOrUpdateTransientModifier(new AttributeModifier(TFAttributeModifiers.FORWARD_BOOTS_ATTRIBUTE_MODIFIER_LOCATION, multiplier - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		input.leftImpulse /= multiplier;
	}

	private void handleSidestep(MovementInputUpdateEvent event) {
		if (!(event.getEntity() instanceof LocalPlayer localPlayer) || !localPlayer.onGround())
			return;

		Input input = localPlayer.input;
		boolean lastImpulseZero = localPlayer.getData(TFDataAttachments.LAST_HORIZONTAL_IMPULSE) == 0;
		boolean sameImpulseDirection = Math.signum(localPlayer.getData(TFDataAttachments.LAST_NON_ZERO_HORIZONTAL_IMPULSE)) == Math.signum(input.leftImpulse);
		long currentTime = localPlayer.level().getGameTime();
		long lastWalkingTime = localPlayer.getData(TFDataAttachments.LAST_HORIZONTAL_WALKING_TIME);
		boolean hasDoubleTapped = currentTime - lastWalkingTime < 4;

		if (lastImpulseZero && sameImpulseDirection && hasDoubleTapped) {
			boolean isLeftSidestep = input.leftImpulse > 0;
			if (TravellersGearLogic.tryPerformSidestep(localPlayer, isLeftSidestep)) {
				localPlayer.connection.send(new PerformSidestepPacket(isLeftSidestep));
			}
		}

		localPlayer.setData(TFDataAttachments.LAST_HORIZONTAL_IMPULSE, input.leftImpulse);
		if (input.leftImpulse != 0) {
			localPlayer.setData(TFDataAttachments.LAST_HORIZONTAL_WALKING_TIME, currentTime);
			localPlayer.setData(TFDataAttachments.LAST_NON_ZERO_HORIZONTAL_IMPULSE, input.leftImpulse);
		}
	}

	private void handleStealth(RenderFrameEvent.Pre event) {
		Player player = Minecraft.getInstance().player;
		if (player == null) return;

		TravellersGearLogic.travellersStealth(player, player1 -> player1.setInvisible(true));  // call it on client to make player invisible instantly
	}

	private void updateZoomFOV(ComputeFovModifierEvent event) {
		Player player = event.getPlayer();
		ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
		if (TFKeyBinds.ZOOM_KEY.isDown() && !player.isScoping() && TravellersModifiersManager.isModifierActive(player, headStack, TravellersModifiersManager.ZOOM_ABILITY)) {
			Float zoomModifier = headStack.get(TFDataComponents.ZOOM_ABILITY_MODIFIER);
			if (zoomModifier != null) {
				event.setNewFovModifier(event.getNewFovModifier() * zoomModifier);
			}
		}
	}

	private void slowZoomSensitivity(CalculatePlayerTurnEvent event) {
		if (event.getCinematicCameraEnabled())
			return;

		Player player = Minecraft.getInstance().player;
		if (player != null) { //won't ever be null by the time this event fires but this is just to make intellij happy
			Float zoomModifier = player.getInventory().getArmor(EquipmentSlot.HEAD.getIndex()).get(TFDataComponents.ZOOM_ABILITY_MODIFIER);
			if (TFKeyBinds.ZOOM_KEY.isDown() && !player.isScoping() && zoomModifier != null) {
				double mouseSensitivity = event.getMouseSensitivity();
				//vanilla math for turning is (m * 0.6 + 0.2)³ * 8; where m is the mouse sensitivity
				//vanilla spyglasses avoid using the "* 8" part, so we probably want to as well
				//the mod value to reverse that was borrowed from IE since they also have zoom functionality
				//we can then divide by our zoom modifier (and add 0.05 to slow it down slightly) to set the sensitivity to a reasonable value when zooming
				double mod = 0.5D - 1 / (6 * mouseSensitivity);
				double fovMod = zoomModifier + 0.05F;
				event.setMouseSensitivity(mod * mouseSensitivity / fovMod);
			}
		}
	}

	private void swapHotbar(InputEvent.Key event) {
		if (TFKeyBinds.SWAP_HOTBAR_KEY.matches(event.getKey(), event.getScanCode())) {
			Player player = Minecraft.getInstance().player;
			if (!(player instanceof LocalPlayer localPlayer)) return;
			ItemStack legArmor = localPlayer.getItemBySlot(EquipmentSlot.LEGS);
			ItemContainerContents containerContents = legArmor.get(DataComponents.CONTAINER);
			if (!TravellersArmorBeltItem.hasSwapHotbar(player, legArmor) || containerContents == null)
				return;

			boolean isClicked = false;
			while (TFKeyBinds.SWAP_HOTBAR_KEY.consumeClick()) {
				isClicked = !isClicked;  // clickCount can be even, so we may not swap hotbar
			}
			boolean hasClicked = isClicked;
			if (!hasClicked)
				return;
			localPlayer.connection.send(SwapHotbarPacket.INSTANCE);
		}
	}

	private void toggleRedThreadVision(InputEvent.Key event) {
		if (TFKeyBinds.RED_THREAD_VISION_KEY.matches(event.getKey(), event.getScanCode())) {
			Player player = Minecraft.getInstance().player;
			if (player == null)
				return;

			boolean current = player.getData(TFDataAttachments.TRAVELLERS_GOGGLES_RED_THREAD_VISION);
			boolean isClicked = false;
			while (TFKeyBinds.RED_THREAD_VISION_KEY.consumeClick()) {
				isClicked = !isClicked;  // clickCount can be even, so we may not toggle Red Thread Vision
			}

			player.setData(TFDataAttachments.TRAVELLERS_GOGGLES_RED_THREAD_VISION, isClicked != current);
		}
	}

	private void toggleItemDisplayVisibility(InputEvent.Key event) {
		if (TFKeyBinds.ITEM_DISPLAY_KEY.matches(event.getKey(), event.getScanCode())) {
			Player player = Minecraft.getInstance().player;
			if (player == null)
				return;

			boolean current = player.getData(TFDataAttachments.TRAVELLERS_GOGGLES_ITEM_DISPLAY);
			boolean isClicked = false;
			while (TFKeyBinds.ITEM_DISPLAY_KEY.consumeClick()) {
				isClicked = !isClicked;  // clickCount can be even, so we may not toggle Red Thread Vision
			}

			player.setData(TFDataAttachments.TRAVELLERS_GOGGLES_ITEM_DISPLAY, isClicked != current);
		}
	}

	private void playZoomSounds(InputEvent.Key event) {
		if (TFKeyBinds.ZOOM_KEY.matches(event.getKey(), event.getScanCode()) && Minecraft.getInstance().screen == null && event.getAction() != InputConstants.REPEAT) {
			Player player = Minecraft.getInstance().player;

			if (player == null || TravellersArmorItem.isTravellersArmorAndBroken(player.getItemBySlot(EquipmentSlot.HEAD)))
				return;

			Float zoomModifier = player.getItemBySlot(EquipmentSlot.HEAD).get(TFDataComponents.ZOOM_ABILITY_MODIFIER);
			if (zoomModifier != null) {
				if (event.getAction() == InputConstants.PRESS) {
					player.playSound(TFSounds.GOGGLES_ZOOM_IN.get());
				} else if (event.getAction() == InputConstants.RELEASE) {
					player.playSound(TFSounds.GOGGLES_ZOOM_OUT.get());
				}
			}
		}
	}
}
