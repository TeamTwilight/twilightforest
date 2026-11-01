package twilightforest.events;

import twilightforest.forge.attachment.Attachments;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.forge.attachment.AttachmentType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSpawnPhantomsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import twilightforest.forge.network.PacketDistributor;
import twilightforest.forge.registries.DeferredHolder;
import net.minecraftforge.server.ServerLifecycleHooks;
import twilightforest.forge.beans.Component;
import twilightforest.forge.beans.PostConstruct;
import twilightforest.components.entity.SlimySolesAttachment;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersGearLogic;
import twilightforest.item.travellers_gear.modifiers.InsertableTravellersModifier;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;
import twilightforest.network.GradualGlidePacket;
import twilightforest.network.ParticlePacket;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
public class TravellersGearEvents {
	// PORTAGE : chez NeoForge les attachements passent par un registre, donc par un DeferredHolder.
	// Ici ce sont de simples objets declares au demarrage : on garde les types eux-memes.
	private static final List<AttachmentType<?>> ATTACHMENTS_TO_PRESERVE_ON_DEATH = List.of(
		TFDataAttachments.TRAVELLERS_GOGGLES_RED_THREAD_VISION
	);

	@PostConstruct
	private void setup() {
		MinecraftForge.EVENT_BUS.addListener(this::magnetizeArrows);
		MinecraftForge.EVENT_BUS.addListener(this::performPerfectDodge);
		MinecraftForge.EVENT_BUS.addListener(this::reduceSlimySolesFallDamage);
		MinecraftForge.EVENT_BUS.addListener(this::tickMovementModifiers);
		MinecraftForge.EVENT_BUS.addListener(this::performStealth);
		MinecraftForge.EVENT_BUS.addListener(this::disableHighStepWhileSneaking);
		MinecraftForge.EVENT_BUS.addListener(this::updateOtherModifiers);
		MinecraftForge.EVENT_BUS.addListener(this::cancelSlimySolesJump);
		// PORTAGE : gestionnaire « activateAndDeactivateTravellersModifiers » supprime — l'evenement NeoForge
		// correspondant n'existe pas chez Forge (voir PORTAGE-twilightforest-crochets.txt).
		MinecraftForge.EVENT_BUS.addListener(this::cancelCombiningTravellersGear);
		MinecraftForge.EVENT_BUS.addListener(this::cancelPhantomSpawns);
		MinecraftForge.EVENT_BUS.addListener(this::fireCraftingModifierTrigger);
		// PORTAGE : gestionnaire « extractItemsFromSwapHotbarModifier » supprime — l'evenement NeoForge
		// correspondant n'existe pas chez Forge (voir PORTAGE-twilightforest-crochets.txt).
		MinecraftForge.EVENT_BUS.addListener(this::removeModifiersFromTravellersGear);
		// PORTAGE : gestionnaire « stopDamagingTravellersGear » supprime — l'evenement NeoForge
		// correspondant n'existe pas chez Forge (voir PORTAGE-twilightforest-crochets.txt).
		// PORTAGE : gestionnaire « setLastDamageArmorTime » supprime — l'evenement NeoForge
		// correspondant n'existe pas chez Forge (voir PORTAGE-twilightforest-crochets.txt).
		MinecraftForge.EVENT_BUS.addListener(this::keepAttachmentsOnDeath);
	}

	private void magnetizeArrows(ProjectileImpactEvent event) {
		Projectile projectile = event.getProjectile();
		Entity entity = projectile.getOwner();
		if (!(entity instanceof LivingEntity livingEntity) || !event.getRayTraceResult().getType().equals(HitResult.Type.BLOCK) || projectile.tickCount >= 200)
			return;

		if (!TravellersModifiersManager.isModifierActive(livingEntity, TravellersModifiersManager.ARROW_MAGNETISM_MODIFIER)
			|| !(projectile instanceof AbstractArrow arrow) || projectile.level().isClientSide())
			return;

		if (!(livingEntity instanceof Player player)) {
			projectile.discard();
			return;
		}
		AbstractArrow.Pickup pickup = arrow.pickup;
		if (!player.hasInfiniteMaterials() && pickup.equals(AbstractArrow.Pickup.ALLOWED)) {
			ItemHandlerHelper.giveItemToPlayer(player, arrow.getPickupItemStackOrigin());
			player.getInventory().setChanged();
		}
		if (pickup.equals(AbstractArrow.Pickup.ALLOWED) || pickup.equals(AbstractArrow.Pickup.CREATIVE_ONLY) && player.isCreative())
			projectile.discard();
	}

	private void performPerfectDodge(ProjectileImpactEvent event) {
		HitResult rayResult = event.getRayTraceResult();
		if (!(rayResult instanceof EntityHitResult entityHitResult) || !(entityHitResult.getEntity() instanceof LivingEntity livingEntity))
			return;
		ItemStack chest = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
		Float probability = chest.get(TFDataComponents.PERFECT_DODGE_PROBABILITY.get());
		Level level = livingEntity.level();
		if (!TravellersModifiersManager.isModifierActive(livingEntity, chest, TravellersModifiersManager.PERFECT_DODGE_MODIFIER) || probability == null)
			return;
		if (level.isClientSide()) {
			event.setCanceled(true); // always cancel on the client side because the game sends a damage packet when it hits the player
			return;
		}
		if (probability <= level.random.nextFloat())
			return;
		Entity projectile = event.getEntity();
		Vec3 hitPosition = projectile.position().add(projectile.getDeltaMovement());
		level.playSound(null, hitPosition.x(), hitPosition.y(), hitPosition.z(), TFSounds.PERFECT_DODGE.get(), livingEntity.getSoundSource(), 1.5F, livingEntity.getVoicePitch());
		event.setCanceled(true);
		ParticlePacket particlePacket = new ParticlePacket();
		for (int particleNumber = 0; particleNumber < 20; particleNumber++) {
			Vec3 particleVelocity = new Vec3(
				(level.random.nextDouble() - 0.5),
				(level.random.nextDouble() - 0.5),
				(level.random.nextDouble() - 0.5)
			);
			ParticleOptions type = TFParticleType.PERFECT_DODGE.get();
			particlePacket.queueParticle(type, false, hitPosition, particleVelocity);
		}
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(livingEntity, particlePacket);
	}

	private void reduceSlimySolesFallDamage(LivingFallEvent event) {
		LivingEntity livingEntity = event.getEntity();
		ItemStack boots = livingEntity.getItemBySlot(EquipmentSlot.FEET);
		Float coefficient = boots.get(TFDataComponents.SLIMY_SOLES_COEFFICIENT.get());
		SlimySolesAttachment slimySolesAttachment = Attachments.get(livingEntity, TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO);
		if (!livingEntity.isShiftKeyDown() && TravellersModifiersManager.isModifierActive(livingEntity, boots, TravellersModifiersManager.SLIMY_SOLES_MODIFIER) && coefficient != null && (calculateFallDamage(event) > 0 || slimySolesAttachment.forceBounce)) {
			event.setCanceled(true);
			slimySolesAttachment.bounceVelocity = -livingEntity.getDeltaMovement().y() * Math.sqrt(coefficient);
			slimySolesAttachment.doubleJumpBoostVelocity = slimySolesAttachment.bounceVelocity;
			slimySolesAttachment.hasBounced = false;
			Attachments.set(livingEntity, TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO, slimySolesAttachment);
		}
	}

	// [VanillaCopy]
	private double calculateFallDamage(LivingFallEvent event) {
		LivingEntity livingEntity = event.getEntity();
		double safeFallDistance = livingEntity.getAttributeValue(Attributes.SAFE_FALL_DISTANCE);
		double unsafeFallDistance = event.getDistance() - safeFallDistance;
		return Mth.ceil(unsafeFallDistance * event.getDamageMultiplier() * livingEntity.getAttributeValue(Attributes.FALL_DAMAGE_MULTIPLIER));
	}

	private void cancelSlimySolesJump(LivingEvent.LivingJumpEvent event) {
		LivingEntity livingEntity = event.getEntity();
		SlimySolesAttachment slimySolesAttachment = Attachments.get(livingEntity, TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO);
		slimySolesAttachment.bounceVelocity = 0;
		slimySolesAttachment.forceBounce = false;
		Attachments.set(livingEntity, TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO, slimySolesAttachment);
	}

	private void tickMovementModifiers(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.START) {
			return;
		}

		Player player = event.player;
		Boolean hasDoubleJump = null;
		if (!TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.DOUBLE_JUMP_MODIFIER))
			hasDoubleJump = false;
		else if (player.onGround() || player.isInLiquid() || player.onClimbable())
			hasDoubleJump = true;

		if (hasDoubleJump != null && hasDoubleJump != Attachments.get(player, TFDataAttachments.HAS_DOUBLE_JUMP)) {
			Attachments.set(player, TFDataAttachments.HAS_DOUBLE_JUMP, hasDoubleJump);
			Attachments.set(player, TFDataAttachments.DOUBLE_JUMP_VALIDATOR, 0);
			AttributeInstance instance = player.getAttribute(Attributes.SAFE_FALL_DISTANCE);
			if (instance != null)
				instance.removeModifier(TFAttributeModifiers.TRAVELLERS_DOUBLE_JUMP_SAFE_FALL_DISTANCE);
		}

		if (!player.level().isClientSide()) {
			boolean modifierActive = TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.GRADUAL_GLIDE_MODIFIER);
			if (!modifierActive && Attachments.get(player, TFDataAttachments.IS_GRADUALLY_GLIDING)) {
				Attachments.set(player, TFDataAttachments.IS_GRADUALLY_GLIDING, false);
				PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new GradualGlidePacket(false, player.getUUID()));
			}
		}

		//reset double jump wing anim if on the ground
		if (event.player.level().isClientSide()) {
			if (Attachments.get(player, TFDataAttachments.TRAVELLERS_WINGS_ANIM).doubleJump && player.onGround()) {
				Attachments.get(player, TFDataAttachments.TRAVELLERS_WINGS_ANIM).doubleJump = false;
			}
		}

		TravellersGearLogic.travellersWingsSidestepCooldownSound(player);
	}

	private void performStealth(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}

		if (!event.player.level().isClientSide()) {
			TravellersGearLogic.travellersStealth(event.player, player1 -> player1.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 2, 0, false, false, false)));
		}
	}

	private void disableHighStepWhileSneaking(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.START) {
			return;
		}

		Player player = event.player;
		if (!TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.STEP_UP_ABILITY))
			return;
		AttributeInstance attribute = player.getAttributes().getInstance(Attributes.STEP_HEIGHT);
		if (attribute == null)
			return;

		boolean shouldHaveHighStepModifier = !player.isCrouching();
		boolean hasHighStepModifier = attribute.hasModifier(TFAttributeModifiers.TRAVELLERS_HIGH_STEP.id());
		if (!shouldHaveHighStepModifier && hasHighStepModifier)
			attribute.removeModifier(TFAttributeModifiers.TRAVELLERS_HIGH_STEP);
		if (shouldHaveHighStepModifier && !hasHighStepModifier)
			attribute.addPermanentModifier(TFAttributeModifiers.TRAVELLERS_HIGH_STEP);
	}

	private void updateOtherModifiers(LivingEvent.LivingTickEvent event) {
		if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;
		TravellersGearLogic.travellersWingsGradualGlide(livingEntity);
		TravellersGearLogic.travellersBootsUnrestrained(livingEntity);
		TravellersGearLogic.travellersBootsSlimySolesBounce(livingEntity);

		if (livingEntity.level().isClientSide()) return;
		TravellersGearLogic.travellersVestHaste(livingEntity);
		TravellersGearLogic.travellersWingsHighJump(livingEntity);
		TravellersGearLogic.travellersGearAutoRepair(livingEntity);
		TravellersGearLogic.travellersBootsStraightAhead(livingEntity);
		TravellersGearLogic.determineWingState(livingEntity);
	}


	// PORTAGE : gestionnaire supprime, l'evenement correspondant n'existe pas chez Forge
	// (voir PORTAGE-twilightforest-crochets.txt).

	// PORTAGE : gestionnaire supprime, l'API NeoForge correspondante n'existe pas
	// chez Forge (voir PORTAGE-twilightforest-crochets.txt).


	// PORTAGE : gestionnaire supprime, l'evenement correspondant n'existe pas chez Forge
	// (voir PORTAGE-twilightforest-crochets.txt).


	private void cancelCombiningTravellersGear(AnvilUpdateEvent event) {
		if (event.getLeft().has(TFDataComponents.IS_TRAVELLERS_GEAR.get()) && event.getRight().has(TFDataComponents.IS_TRAVELLERS_GEAR.get())) {
			event.setCanceled(true);
		}
	}

	private void removeModifiersFromTravellersGear(GrindstoneEvent.OnPlaceItem event) {
		if (ServerLifecycleHooks.getCurrentServer() == null)
			return;
		RegistryAccess access = ServerLifecycleHooks.getCurrentServer().registryAccess();
		List<ItemStack> travellersItemStacks = Stream.of(event.getTopItem(), event.getBottomItem())
				.filter(stack -> stack.has(TFDataComponents.IS_TRAVELLERS_GEAR.get()))
				.toList();

		if (travellersItemStacks.isEmpty())
			return; // Delegate to vanilla logic
		if (travellersItemStacks.size() > 1) {
			event.setCanceled(true);
			return;
		}
		ItemStack inputStack = travellersItemStacks.getFirst();
		List<Holder.Reference<TravellersModifier>> modifiers = TravellersModifiersManager.findAllInsertableModifiers(access, inputStack);
		if (modifiers.isEmpty()) {
			event.setCanceled(true);
			return;
		}

		ItemStack unmodifiedStack = inputStack.copy();
		modifiers.forEach(modifier -> ((InsertableTravellersModifier) modifier.value()).removeModifier(unmodifiedStack));
		event.setOutput(unmodifiedStack.copy());
	}

	// PORTAGE : gestionnaire supprime, l'API NeoForge correspondante n'existe pas
	// chez Forge (voir PORTAGE-twilightforest-crochets.txt).

	// PORTAGE : gestionnaire supprime, l'API NeoForge correspondante n'existe pas
	// chez Forge (voir PORTAGE-twilightforest-crochets.txt).

	private Optional<ItemStack> getUniqueTravellersGear(ItemStack top, ItemStack bottom, Predicate<ItemStack> predicate) {
		List<ItemStack> travellersItemStacks = Stream.of(top, bottom)
			.filter(stack -> stack.has(TFDataComponents.IS_TRAVELLERS_GEAR.get()))
			.filter(predicate)
			.toList();
		return travellersItemStacks.size() == 1 ? Optional.of(travellersItemStacks.getFirst()) : Optional.empty();
	}

	private void cancelPhantomSpawns(PlayerSpawnPhantomsEvent event) {
		if (TravellersModifiersManager.isModifierActive(event.getEntity(), TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER)) {
			event.setResult(PlayerSpawnPhantomsEvent.Result.DENY);
		}
	}

	private void fireCraftingModifierTrigger(PlayerEvent.ItemCraftedEvent event) {
		if (event.getEntity() instanceof ServerPlayer player && event.getCrafting().has(TFDataComponents.IS_TRAVELLERS_GEAR.get())) {
			ItemStack compareStack = ItemStack.EMPTY;
			for (int i = 0; i < event.getInventory().getContainerSize(); i++) {
				if (event.getInventory().getItem(i).is(event.getCrafting().getItem())) compareStack = event.getInventory().getItem(i);
			}

			if (!compareStack.isEmpty()) {
				var oldMods = TravellersModifiersManager.findAllInsertableModifiers(player, compareStack);
				TravellersModifiersManager.findAllInsertableModifiers(player, event.getCrafting()).stream()
					.filter(modifier -> !oldMods.contains(modifier)).toList()
						.forEach(modifier -> TFAdvancements.ADD_MODIFIER.get().trigger(player, modifier.key().location()));
			}
		}
	}

	public void keepAttachmentsOnDeath(PlayerEvent.Clone event) {
		if (event.isWasDeath()) {
			for (AttachmentType<?> attachmentType : ATTACHMENTS_TO_PRESERVE_ON_DEATH) {
				copyAttachmentData(event.getOriginal(), event.getEntity(), attachmentType);
			}
		}
	}

	private <T> void copyAttachmentData(Player source, Player target, AttachmentType<T> type) {
		if (Attachments.has(source, type)) {
			Attachments.set(target, type, Attachments.get(source, type));
		}
	}
}
