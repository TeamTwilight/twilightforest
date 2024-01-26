package twilightforest.events;

import com.mojang.authlib.GameProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;
import twilightforest.block.entity.KeepsakeCasketBlockEntity;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.enchantment.ChillAuraEnchantment;
import twilightforest.entity.projectile.ITFProjectile;
import twilightforest.init.*;
import twilightforest.item.FieryArmorItem;
import twilightforest.item.OreMeterItem;
import twilightforest.item.YetiArmorItem;
import twilightforest.network.WipeOreMeterPacket;
import twilightforest.world.components.structures.TFStructureComponent;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.world.components.structures.type.HollowHillStructure;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.finalcastle.FinalCastleBossGazeboComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class EntityEvents {

	private static final boolean SHIELD_PARRY_MOD_LOADED = ModList.get().isLoaded("parry");

	@SubscribeEvent
	public static void alertPlayerCastleIsWIP(AdvancementEvent.AdvancementEarnEvent event) {
		if (event.getAdvancement().id().equals(TwilightForestMod.prefix("progression_end"))) {
			event.getEntity().sendSystemMessage(Component.translatable("gui.twilightforest.progression_end.message", Component.translatable("gui.twilightforest.progression_end.discord").withStyle(style -> style.withColor(ChatFormatting.BLUE).applyFormat(ChatFormatting.UNDERLINE).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/twilightforest")))));
		}
	}

	@SubscribeEvent
	public static void attachLeadToWroughtFence(PlayerInteractEvent.RightClickBlock event) {
		Player player = event.getEntity();
		ItemStack stack = player.getItemInHand(event.getHand());
		if (stack.is(Items.LEAD)) {
			BlockPos pos = event.getPos();
			BlockState state = event.getLevel().getBlockState(pos);
			if (state.is(TFBlocks.WROUGHT_IRON_FENCE) && state.getValue(WroughtIronFenceBlock.POST) != WroughtIronFenceBlock.PostState.NONE) {
				if (!event.getLevel().isClientSide()) {
					LeadItem.bindPlayerMobs(player, event.getLevel(), event.getPos());
					event.setCanceled(true);
					event.setCancellationResult(InteractionResult.SUCCESS);
				}
			}
		}
	}

	@SubscribeEvent
	public static void wipeOreMeterOnLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
		if (event.getItemStack().is(TFItems.ORE_METER.get()) && (!OreMeterItem.getScanInfo(event.getItemStack()).isEmpty() || OreMeterItem.getAssignedBlock(event.getItemStack()) != null)) {
			OreMeterItem.saveScanInfo(event.getItemStack(), new HashMap<>(), 0L, 0);
			OreMeterItem.clearAssignedBlock(event.getItemStack());
			event.getLevel().playSound(event.getEntity(), event.getEntity().blockPosition(), TFSounds.ORE_METER_CLEAR.get(), SoundSource.PLAYERS, 1.25F, event.getLevel().getRandom().nextFloat() * 0.2F + 0.6F);
			PacketDistributor.SERVER.noArg().send(new WipeOreMeterPacket(event.getEntity().getInventory().selected + 36, event.getHand()));
		}
	}

	@SubscribeEvent
	public static void entityHurts(LivingHurtEvent event) {
		LivingEntity living = event.getEntity();
		DamageSource source = event.getSource();
		Entity trueSource = source.getEntity();

		// fire react and chill aura
		if (source.getEntity() != null && trueSource != null && event.getAmount() > 0) {
			int fireLevel = getGearCoverage(living, false) * 5;
			int chillLevel = getGearCoverage(living, true);

			if (fireLevel > 0 && living.getRandom().nextInt(25) < fireLevel && !trueSource.fireImmune()) {
				trueSource.setSecondsOnFire(fireLevel / 2);
			}

			if (trueSource instanceof LivingEntity target) {
				ChillAuraEnchantment.doChillAuraEffect(target, chillLevel * 5 + 5, chillLevel, chillLevel > 0);
			}
		}

		// triple bow strips invulnerableTime
		if (source.getMsgId().equals("arrow") && trueSource instanceof Player player) {

			if (player.getItemInHand(player.getUsedItemHand()).is(TFItems.TRIPLE_BOW)) {
				living.invulnerableTime = 0;
			}
		}
	}

	@SubscribeEvent
	//if our casket is owned by someone and that player isnt the one breaking it, stop them
	public static void onCasketBreak(BlockEvent.BreakEvent event) {
		Block block = event.getState().getBlock();
		Player player = event.getPlayer();
		BlockEntity te = event.getLevel().getBlockEntity(event.getPos());
		UUID checker;
		if (block == TFBlocks.KEEPSAKE_CASKET.get()) {
			if (te instanceof KeepsakeCasketBlockEntity casket) {
				checker = casket.playeruuid;
			} else checker = null;
			if (checker != null) {
				if (!((KeepsakeCasketBlockEntity) te).isEmpty()) {
					if (!player.hasPermissions(3) || !player.getGameProfile().getId().equals(checker)) {
						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onCrafting(PlayerEvent.ItemCraftedEvent event) {
		ItemStack itemStack = event.getCrafting();

		// if we've crafted 64 planks from a giant log, sneak 192 more planks into the player's inventory or drop them nearby
		if (itemStack.is(Items.OAK_PLANKS) && itemStack.getCount() == 64 && event.getInventory().countItem(TFBlocks.GIANT_LOG.get().asItem()) > 0) {
			Player player = event.getEntity();
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.OAK_PLANKS, 64));
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.OAK_PLANKS, 64));
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.OAK_PLANKS, 64));
		}
	}

	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event) {
		LivingEntity living = event.getEntity();
		if (living != null) {
			Optional.ofNullable(living.getEffect(TFMobEffects.FROSTY.get())).ifPresent(mobEffectInstance -> {
				if (event.getSource().is(DamageTypes.FREEZE)) {
					event.setAmount(event.getAmount() + (float)(mobEffectInstance.getAmplifier() / 2));
				} else if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
					living.removeEffect(TFMobEffects.FROSTY.get());
					mobEffectInstance.amplifier -= 1;
					if (mobEffectInstance.amplifier >= 0) living.addEffect(mobEffectInstance);
				}
			});
		}
	}

	// Parrying
	@SubscribeEvent
	public static void onParryProjectile(ProjectileImpactEvent event) {
		final Projectile projectile = event.getProjectile();

		if (!projectile.getCommandSenderWorld().isClientSide() && !SHIELD_PARRY_MOD_LOADED && (TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get() || projectile instanceof ITFProjectile)) {
			if (event.getRayTraceResult() instanceof EntityHitResult result) {
				Entity entity = result.getEntity();

				if (event.getEntity() != null && entity instanceof LivingEntity entityBlocking) {
					if (entityBlocking.isBlocking() && entityBlocking.getUseItem().getUseDuration() - entityBlocking.getUseItemRemainingTicks() <= TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.shieldParryTicks.get()) {
						projectile.setOwner(entityBlocking);
						Vec3 rebound = entityBlocking.getLookAngle();
						projectile.shoot(rebound.x(), rebound.y(), rebound.z(), 1.1F, 0.1F);  // reflect faster and more accurately
						if (projectile instanceof AbstractHurtingProjectile hurting) {
							hurting.xPower = rebound.x() * 0.1D;
							hurting.yPower = rebound.y() * 0.1D;
							hurting.zPower = rebound.z() * 0.1D;
						}

						event.setCanceled(true);
					}
				}
			}
		}
	}

	/**
	 * Checks if the player is attempting to create a skull candle
	 */
	// I wanted to make sure absolutely nothing broke, so I also check against the namespaces of the item to make sure theyre vanilla.
	// Worst case some stupid mod adds their own stuff to the minecraft namespace and breaks this, then you can disable this via config.
	@SubscribeEvent
	public static void createSkullCandle(PlayerInteractEvent.RightClickBlock event) {
		ItemStack stack = event.getItemStack();
		Level level = event.getLevel();
		BlockPos pos = event.getPos();
		BlockState state = level.getBlockState(pos);
		if (!TFConfig.COMMON_CONFIG.disableSkullCandles.get()) {
			if (stack.is(ItemTags.CANDLES) && BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals("minecraft") && !event.getEntity().isShiftKeyDown()) {
				if (state.getBlock() instanceof AbstractSkullBlock skull && BuiltInRegistries.BLOCK.getKey(state.getBlock()).getNamespace().equals("minecraft")) {
					SkullBlock.Types type = (SkullBlock.Types) skull.getType();
					boolean wall = state.getBlock() instanceof WallSkullBlock;
					switch (type) {
						case SKELETON -> {
							if (wall) makeWallSkull(event, TFBlocks.SKELETON_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(event, TFBlocks.SKELETON_SKULL_CANDLE.get());
						}
						case WITHER_SKELETON -> {
							if (wall) makeWallSkull(event, TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(event, TFBlocks.WITHER_SKELE_SKULL_CANDLE.get());
						}
						case PLAYER -> {
							if (wall) makeWallSkull(event, TFBlocks.PLAYER_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(event, TFBlocks.PLAYER_SKULL_CANDLE.get());
						}
						case ZOMBIE -> {
							if (wall) makeWallSkull(event, TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(event, TFBlocks.ZOMBIE_SKULL_CANDLE.get());
						}
						case CREEPER -> {
							if (wall) makeWallSkull(event, TFBlocks.CREEPER_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(event, TFBlocks.CREEPER_SKULL_CANDLE.get());
						}
						case PIGLIN -> {
							if (wall) makeWallSkull(event, TFBlocks.PIGLIN_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(event, TFBlocks.PIGLIN_SKULL_CANDLE.get());
						}
						default -> {
							return;
						}
					}
					if (!event.getEntity().getAbilities().instabuild) stack.shrink(1);
					event.getEntity().swing(event.getHand());
					if (event.getEntity() instanceof ServerPlayer)
						event.getEntity().awardStat(TFStats.SKULL_CANDLES_MADE.get());
					//this is to prevent anything from being placed afterwords
					event.setCanceled(true);
				}
			}
		}
	}

	private static void makeFloorSkull(PlayerInteractEvent.RightClickBlock event, Block newBlock) {
		GameProfile profile = null;
		Level level = event.getLevel();
		if (level.getBlockEntity(event.getPos()) instanceof SkullBlockEntity skull)
			profile = skull.getOwnerProfile();
		level.playSound(null, event.getPos(), SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
		level.setBlockAndUpdate(event.getPos(), newBlock.defaultBlockState()
				.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)
				.setValue(SkullCandleBlock.ROTATION, level.getBlockState(event.getPos()).getValue(SkullBlock.ROTATION)));
		level.setBlockEntity(new SkullCandleBlockEntity(event.getPos(),
				newBlock.defaultBlockState()
						.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)
						.setValue(SkullCandleBlock.ROTATION, level.getBlockState(event.getPos()).getValue(SkullBlock.ROTATION)),
				AbstractSkullCandleBlock.candleToCandleColor(event.getItemStack().getItem()).getValue(), 1));
		if (level.getBlockEntity(event.getPos()) instanceof SkullCandleBlockEntity sc) sc.setOwner(profile);
	}

	private static void makeWallSkull(PlayerInteractEvent.RightClickBlock event, Block newBlock) {
		GameProfile profile = null;
		Level level = event.getLevel();
		if (level.getBlockEntity(event.getPos()) instanceof SkullBlockEntity skull)
			profile = skull.getOwnerProfile();
		level.playSound(null, event.getPos(), SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
		level.setBlockAndUpdate(event.getPos(), newBlock.defaultBlockState()
				.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)
				.setValue(WallSkullCandleBlock.FACING, level.getBlockState(event.getPos()).getValue(WallSkullBlock.FACING)));
		level.setBlockEntity(new SkullCandleBlockEntity(event.getPos(),
				newBlock.defaultBlockState()
						.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)
						.setValue(WallSkullCandleBlock.FACING, level.getBlockState(event.getPos()).getValue(WallSkullBlock.FACING)),
				AbstractSkullCandleBlock.candleToCandleColor(event.getItemStack().getItem()).getValue(), 1));
		if (level.getBlockEntity(event.getPos()) instanceof SkullCandleBlockEntity sc) sc.setOwner(profile);
	}

	/**
	 * Add up the number of armor pieces the player is wearing (either fiery or yeti)
	 */
	public static int getGearCoverage(LivingEntity entity, boolean yeti) {
		int amount = 0;

		for (ItemStack armor : entity.getArmorSlots()) {
			if (!armor.isEmpty() && (yeti ? armor.getItem() instanceof YetiArmorItem : armor.getItem() instanceof FieryArmorItem)) {
				amount++;
			}
		}

		return amount;
	}

	@SubscribeEvent
	public static void onLivingTickEvent(LivingEvent.LivingTickEvent event) {
		LivingEntity living = event.getEntity();
		if (living != null && canSpawnCloudParticles(living)) {
			CloudBlock.addEntityMovementParticles(living.level(), living.getOnPos(), living, false);
		}
	}

	public static boolean canSpawnCloudParticles(LivingEntity living) {
		if (living.getDeltaMovement().x == 0.0D && living.getDeltaMovement().z == 0.0D && living.getRandom().nextInt(20) != 0) return false;
		return living.tickCount % 2 == 0 && !living.isSpectator() && living.level().getBlockState(living.getOnPos()).getBlock() instanceof CloudBlock;
	}

	@SubscribeEvent
	public static void onLivingJumpEvent(LivingEvent.LivingJumpEvent event) {
		LivingEntity living = event.getEntity();
		if (living != null && living.level().isClientSide() && !living.isSpectator() && living.level().getBlockState(living.getOnPos()).getBlock() instanceof CloudBlock) {
			for (int i = 0; i < 12; i++) CloudBlock.addEntityMovementParticles(living.level(), living.getOnPos(), living, true);
		}
	}

	private static int getSpawnListIndexAt(StructureStart start, BlockPos pos) {
		int highestFoundIndex = -1;
		for (StructurePiece component : start.getPieces()) {
			if (component.getBoundingBox().isInside(pos)) {
				if (component instanceof TFStructureComponent tfComponent) {
					if (tfComponent.spawnListIndex > highestFoundIndex)
						highestFoundIndex = tfComponent.spawnListIndex;
				} else
					return 0;
			}
		}
		return highestFoundIndex;
	}

	@Nullable
	public static List<MobSpawnSettings.SpawnerData> gatherPotentialSpawns(StructureManager structureManager, MobCategory classification, BlockPos pos) {
		List<StructureStart> structureStarts = structureManager.startsForStructure(new ChunkPos(pos), s -> s instanceof ControlledSpawns);

		// This is wretched FIXME make this method return void instead, make one of parameters the SpawnerData consumer (eg LevelEvent.PotentialSpawns::addSpawnerData or List::add)
		for (StructureStart start : structureStarts) {
			if (start.getStructure() instanceof ControlledSpawns landmark) {

				if (!start.isValid())
					continue;

				if (classification != MobCategory.MONSTER)
					return landmark.getSpawnableList(classification);

				if (start instanceof TFStructureStart s && s.isConquered())
					return null;

				// FIXME Make interface for this method?
				if (landmark instanceof HollowHillStructure hollowHill && !hollowHill.canSpawnMob(pos, start.getBoundingBox()))
					return null;

				final int index = getSpawnListIndexAt(start, pos);
				if (index < 0)
					return null;
				return landmark.getSpawnableMonsterList(index);
			}
		}

		return null;
	}

	@SubscribeEvent
	public static void structureSpecialSpawns(LevelEvent.PotentialSpawns event) {
		if (!(event.getLevel() instanceof ServerLevel serverLevel))
			return;

        List<MobSpawnSettings.SpawnerData> potentialStructureSpawns = gatherPotentialSpawns(serverLevel.structureManager(), event.getMobCategory(), event.getPos());
		if (potentialStructureSpawns != null) {
			List.copyOf(event.getSpawnerDataList()).forEach(event::removeSpawnerData);
			potentialStructureSpawns.forEach(event::addSpawnerData);
    }
  }

	@SubscribeEvent
	public static void onAttackEvent(AttackEntityEvent event) {
		// For clearing our Display text entities at the Final Castle Gazebo, there's no other way to remove them otherwise
		// The tag distinguishes our Interaction entities from other Mods' utilization
        if (event.getTarget().level() instanceof ServerLevel level && event.getTarget() instanceof Interaction interaction
				&& interaction.getTags().contains(FinalCastleBossGazeboComponent.INTERACTION_TAG)) {
			AABB bounds = interaction.getBoundingBox();
			level.getEntities(interaction, bounds, e -> e instanceof Display).forEach(Entity::discard);
			interaction.discard();
		}
	}
}
