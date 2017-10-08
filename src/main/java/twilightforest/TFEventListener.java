package twilightforest;

import javafx.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandGameRule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CombatEntry;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import twilightforest.block.BlockTFCritter;
import twilightforest.block.BlockTFGiantBlock;
import twilightforest.block.TFBlocks;
import twilightforest.client.particle.TFParticleType;
import twilightforest.enchantment.TFEnchantment;
import twilightforest.entity.EntityTFCharmEffect;
import twilightforest.entity.EntityTFPinchBeetle;
import twilightforest.entity.EntityTFYeti;
import twilightforest.entity.ISavedCombatEntriesOnDeath;
import twilightforest.item.TFItems;
import twilightforest.network.PacketAreaProtection;
import twilightforest.network.PacketEnforceProgressionStatus;
import twilightforest.util.TFItemStackUtils;
import twilightforest.world.ChunkGeneratorTwilightForest;
import twilightforest.world.TFWorld;

import java.util.*;

/**
 * So much of the mod logic in this one class
 */
@Mod.EventBusSubscriber
public class TFEventListener {

	private static Map<UUID, InventoryPlayer> playerKeepsMap = new HashMap<>();
	private static boolean isBreakingWithGiantPick = false;
	private static boolean shouldMakeGiantCobble = false;
	private static int amountOfCobbleToReplace = 0;

	@SubscribeEvent
	public static void onCrafting(ItemCraftedEvent event) {
		ItemStack itemStack = event.crafting;
		EntityPlayer player = event.player;

		// if we've crafted 64 planks from a giant log, sneak 192 more planks into the player's inventory or drop them nearby
		if (itemStack.getItem() == Item.getItemFromBlock(Blocks.PLANKS) && itemStack.getCount() == 64 && doesCraftMatrixHaveGiantLog(event.craftMatrix)) {
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Blocks.PLANKS, 64));
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Blocks.PLANKS, 64));
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Blocks.PLANKS, 64));
		}
	}

	private static boolean doesCraftMatrixHaveGiantLog(IInventory inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);

			if (!stack.isEmpty() && stack.getItem() == Item.getItemFromBlock(TFBlocks.giantLog)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Also check if we need to transform 64 cobbles into a giant cobble
	 */
	@SubscribeEvent
	public static void harvestDrops(HarvestDropsEvent event) {
		// this flag is set in reaction to the breakBlock event, but we need to remove the drops in this event
		if (shouldMakeGiantCobble && event.getDrops().size() > 0) {
			// turn the next 64 cobblestone drops into one giant cobble
			if (event.getDrops().get(0).getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE)) {
				event.getDrops().remove(0);
				if (amountOfCobbleToReplace == 64) {
					event.getDrops().add(new ItemStack(TFBlocks.giantCobble));
				}

				amountOfCobbleToReplace--;

				if (amountOfCobbleToReplace <= 0) {
					shouldMakeGiantCobble = false;
				}
			}
		}
	}

	/**
	 * We wait for a player wearing armor with the fire react enchantment to get hurt, and if that happens, we react
	 */
	@SubscribeEvent
	public static void entityHurts(LivingHurtEvent event) {
		// fire aura
		if (event.getEntityLiving() instanceof EntityPlayer && event.getSource().damageType.equals("mob") && event.getSource().getTrueSource() != null) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			int fireLevel = TFEnchantment.getFieryAuraLevel(player.inventory, event.getSource());


			if (fireLevel > 0 && player.getRNG().nextInt(25) < fireLevel) {
				event.getSource().getTrueSource().setFire(fireLevel / 2);
			}
		}

		// chill aura
		if (event.getEntityLiving() instanceof EntityPlayer && event.getSource().damageType.equals("mob")
				&& event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityLivingBase) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			int chillLevel = TFEnchantment.getChillAuraLevel(player.inventory, event.getSource());

			if (chillLevel > 0) {
				((EntityLivingBase) event.getSource().getTrueSource()).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, chillLevel * 5 + 5, chillLevel));

			}
		}

		// triple bow strips hurtResistantTime
		if (event.getSource().damageType.equals("arrow") && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();

			if (!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() == TFItems.tripleBow
					|| !player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() == TFItems.tripleBow) {
				event.getEntityLiving().hurtResistantTime = 0;
			}
		}

		// enderbow teleports
		if (event.getSource().damageType.equals("arrow") && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();

			if (!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() == TFItems.enderBow ||
					!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() == TFItems.enderBow) {

				double sourceX = player.posX;
				double sourceY = player.posY;
				double sourceZ = player.posZ;
				float sourceYaw = player.rotationYaw;
				float sourcePitch = player.rotationPitch;

				// this is the only method that will move the player properly
				player.rotationYaw = event.getEntityLiving().rotationYaw;
				player.rotationPitch = event.getEntityLiving().rotationPitch;
				player.setPositionAndUpdate(event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ);
				player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);


				// monsters are easy to move
				event.getEntityLiving().setPositionAndRotation(sourceX, sourceY, sourceZ, sourceYaw, sourcePitch);
				event.getEntityLiving().playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
			}
		}

		// charm of life?
		if (event.getEntityLiving() instanceof EntityPlayer && willEntityDie(event)) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();

			boolean charm1 = false;
			boolean charm2 = TFItemStackUtils.consumeInventoryItem(player, s -> !s.isEmpty() && s.getItem() == TFItems.charmOfLife2, 1);
			if (!charm2) {
				charm1 = TFItemStackUtils.consumeInventoryItem(player, s -> !s.isEmpty() && s.getItem() == TFItems.charmOfLife1, 1);
			}

			// do they have a charm of life?  OM NOM NOM!
			if (charm2 || charm1) {
				//player.sendMessage("Charm of Life saves you!!!");

				// cancel damage
				event.setResult(Result.DENY);
				event.setCanceled(true);
				event.setAmount(0);

				if (charm1) {
					player.setHealth(8);
					player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 0));
				}

				if (charm2) {
					player.setHealth((float) player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());

					player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 600, 3));
					player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 600, 0));
					player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 600, 0));
				}

				// spawn effect thingers
				EntityTFCharmEffect effect = new EntityTFCharmEffect(player.world, player, charm1 ? TFItems.charmOfLife1 : TFItems.charmOfLife2);
				player.world.spawnEntity(effect);

				EntityTFCharmEffect effect2 = new EntityTFCharmEffect(player.world, player, charm1 ? TFItems.charmOfLife1 : TFItems.charmOfLife2);
				effect2.offset = (float) Math.PI;
				player.world.spawnEntity(effect2);

				// sound
				player.world.setEntityState(player, (byte) 35);
			}
		}

		// Smashing!
		Item item = event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem();
		if (item instanceof ItemBlock && ((ItemBlock)item).getBlock() instanceof BlockTFCritter) {
			BlockTFCritter poorBug = (BlockTFCritter)((ItemBlock) item).getBlock();

			if (poorBug == TFBlocks.firefly)
				event.getEntityLiving().setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.GLOWSTONE_DUST));

			if (poorBug == TFBlocks.cicada)
				event.getEntityLiving().setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DYE, 1, 8));

			if (poorBug == TFBlocks.moonworm)
				event.getEntityLiving().setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DYE, 1, 10));
		}
	}

	// TODO modernize the calculations
	private static boolean willEntityDie(LivingHurtEvent event) {
		float amount = event.getAmount();
		DamageSource source = event.getSource();
		EntityLivingBase living = event.getEntityLiving();
		// reduce damage by armor
		if (!source.isUnblockable()) {
			int armor = 25 - living.getTotalArmorValue();
			amount = (amount * armor) / 25F;
		}

		// maybe also potions?
		if (living.isPotionActive(MobEffects.RESISTANCE)) {
			int resistance = 25 - (living.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
			amount = amount * resistance / 25F;
		}
		//System.out.printf("I think the player is going to take %f damage and they have %f health.\n", Math.ceil(amount), living.getHealth());

		return Math.ceil(amount) >= Math.floor(living.getHealth());
	}

	/**
	 * If a player dies with a charm of keeping, consume the charm and then keep track of what items we need to keep
	 * <p>
	 * Also keep tower keys
	 */
	@SubscribeEvent
	public static void livingDies(LivingDeathEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer && !event.getEntityLiving().world.getGameRules().getBoolean("keepInventory")) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			boolean tier3 = TFItemStackUtils.consumeInventoryItem(player, s -> !s.isEmpty() && s.getItem() == TFItems.charmOfKeeping3, 1);
			boolean tier2 = tier3 || TFItemStackUtils.consumeInventoryItem(player, s -> !s.isEmpty() && s.getItem() == TFItems.charmOfKeeping2, 1);
			boolean tier1 = tier2 || TFItemStackUtils.consumeInventoryItem(player, s -> !s.isEmpty() && s.getItem() == TFItems.charmOfKeeping1, 1);

			InventoryPlayer keepInventory = new InventoryPlayer(null);

			if (tier1) {
				keepAllArmor(player, keepInventory);
				if (!tier2 && !player.inventory.getCurrentItem().isEmpty()) {
					keepInventory.mainInventory.set(player.inventory.currentItem, player.inventory.mainInventory.get(player.inventory.currentItem).copy());
					player.inventory.mainInventory.set(player.inventory.currentItem, ItemStack.EMPTY);
				}
			}

			if (tier3) {
				for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
					keepInventory.mainInventory.set(i, player.inventory.mainInventory.get(i).copy());
					player.inventory.mainInventory.set(i, ItemStack.EMPTY);
				}
			} else if (tier2) {
				for (int i = 0; i < 9; i++) {
					keepInventory.mainInventory.set(i, player.inventory.mainInventory.get(i).copy());
					player.inventory.mainInventory.set(i, ItemStack.EMPTY);
				}
			}

			// always keep tower keys
			for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
				if (!player.inventory.mainInventory.get(i).isEmpty() && player.inventory.mainInventory.get(i).getItem() == TFItems.towerKey) {
					keepInventory.mainInventory.set(i, player.inventory.mainInventory.get(i).copy());
					player.inventory.mainInventory.set(i, ItemStack.EMPTY);
				}
			}


			playerKeepsMap.put(player.getUniqueID(), keepInventory);
		}

		if (event.getEntityLiving() instanceof ISavedCombatEntriesOnDeath) {
			List<CombatEntry> tracker = ((ISavedCombatEntriesOnDeath) event.getEntityLiving()).getCombatList();
			ArrayList<Pair<EntityPlayerMP,ArrayList<CombatEntry>>> victors = new ArrayList<>();

			for (CombatEntry entry : tracker != null ? tracker : event.getEntityLiving().getCombatTracker().combatEntries) {
				Entity attacker = entry.getDamageSrc().getTrueSource();
				System.out.println("" + attacker);
				if (attacker instanceof EntityPlayerMP && !(attacker instanceof FakePlayer)) {
					boolean playerFound = false;
					for (Pair<EntityPlayerMP, ArrayList<CombatEntry>> victor : victors) {
						if (victor.getKey() == attacker) {
							victor.getValue().add(entry);
							playerFound = true;
							break;
						}
					}
					if (!playerFound) victors.add(new Pair<>((EntityPlayerMP) attacker, new ArrayList<>(Collections.singleton(entry))));
				}
			}

			for (Pair<EntityPlayerMP,ArrayList<CombatEntry>> victor : victors)
				TFAdvancements.GANG_BANG.trigger(victor.getKey(), event.getEntityLiving(), victor.getValue(), victors.size());
		}
	}

	/**
	 * Move the full armor inventory to the keep pile
	 */
	private static void keepAllArmor(EntityPlayer player, InventoryPlayer keepInventory) {
		for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
			keepInventory.armorInventory.set(i, player.inventory.armorInventory.get(i).copy());
			player.inventory.armorInventory.set(i, ItemStack.EMPTY);
		}
	}

	/**
	 * Maybe we kept some stuff for the player!
	 */
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerRespawnEvent event) {
		EntityPlayer player = event.player;
		InventoryPlayer keepInventory = playerKeepsMap.remove(player.getUniqueID());
		if (keepInventory != null) {
			TwilightForestMod.LOGGER.debug("Player {} respawned and received items held in storage", player.getName());

			for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
				if (!keepInventory.armorInventory.get(i).isEmpty()) {
					player.inventory.armorInventory.set(i, keepInventory.armorInventory.get(i));
				}
			}
			for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
				if (!keepInventory.mainInventory.get(i).isEmpty()) {
					player.inventory.mainInventory.set(i, keepInventory.mainInventory.get(i));
				}
			}

			// spawn effect thingers
			if (!keepInventory.getItemStack().isEmpty()) {
				EntityTFCharmEffect effect = new EntityTFCharmEffect(player.world, player, keepInventory.getItemStack().getItem());
				player.world.spawnEntity(effect);

				EntityTFCharmEffect effect2 = new EntityTFCharmEffect(player.world, player, keepInventory.getItemStack().getItem());
				effect2.offset = (float) Math.PI;
				player.world.spawnEntity(effect2);

				player.world.playSound(player.posX + 0.5D, player.posY + 0.5D, player.posZ + 0.5D, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.HOSTILE, 1.5F, 1.0F, true);
			}
		}
	}

	/**
	 * Dump stored items if player logs out
	 */
	@SubscribeEvent
	public static void onPlayerLogout(PlayerLoggedOutEvent event) {
		EntityPlayer player = event.player;
		InventoryPlayer keepInventory = playerKeepsMap.remove(player.getUniqueID());
		if (keepInventory != null) {
			TwilightForestMod.LOGGER.warn("Mod was keeping inventory items in reserve for player %s but they logged out!  Items are being dropped.", player.getName());

			// set player to the player logging out
			keepInventory.player = player;
			keepInventory.dropAllItems();
		}
	}

	/**
	 * Stop the game from rendering the mount health for unfriendly creatures
	 */
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static boolean preOverlay(RenderGameOverlayEvent.Pre event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTHMOUNT) {
			if (isRidingUnfriendly(Minecraft.getMinecraft().player)) {
				event.setCanceled(true);
				return false;

			}
		}
		return true;
	}


	/**
	 * Stop the player from sneaking while riding an unfriendly creature
	 */
	@SubscribeEvent
	public static boolean livingUpdate(LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer && event.getEntityLiving().isSneaking() && isRidingUnfriendly(event.getEntityLiving())) {
			event.getEntityLiving().setSneaking(false);
		}
		return true;
	}

	private static boolean isRidingUnfriendly(EntityLivingBase entity) {
		return entity.isRiding() && (entity.getRidingEntity() instanceof EntityTFPinchBeetle || entity.getRidingEntity() instanceof EntityTFYeti);
	}

	/**
	 * Check if the player is trying to break a block in a structure that's considered unbreakable for progression reasons
	 * Also check for breaking blocks with the giant's pickaxe and maybe break nearby blocks
	 */
	@SubscribeEvent
	public static void breakBlock(BreakEvent event) {
		if (!event.getWorld().isRemote && !event.getPlayer().capabilities.isCreativeMode && isAreaProtected(event.getWorld(), event.getPlayer(), event.getPos()) && isBlockProtectedFromBreaking(event.getWorld(), event.getPos())) {
			event.setCanceled(true);
		} else if (!isBreakingWithGiantPick
				&& !event.getPlayer().getHeldItemMainhand().isEmpty()
				&& event.getPlayer().getHeldItemMainhand().getItem() == TFItems.giantPick
				&& event.getPlayer().getHeldItemMainhand().getItem().canHarvestBlock(event.getState(), event.getPlayer().getHeldItemMainhand())) {

			isBreakingWithGiantPick = true;


			// check nearby blocks for same block or same drop
			BlockPos bPos = BlockTFGiantBlock.roundCoords(event.getPos());

			// pre-check for cobble!
			boolean allCobble = event.getState().getBlock().getItemDropped(event.getState(), event.getWorld().rand, 0) == Item.getItemFromBlock(Blocks.COBBLESTONE);
			for (int dx = 0; dx < 4; dx++) {
				for (int dy = 0; dy < 4; dy++) {
					for (int dz = 0; dz < 4; dz++) {
						BlockPos dPos = bPos.add(dx, dy, dz);
						IBlockState stateThere = event.getWorld().getBlockState(dPos);
						Block blockThere = stateThere.getBlock();

						allCobble &= blockThere.getItemDropped(stateThere, event.getWorld().rand, 0) == Item.getItemFromBlock(Blocks.COBBLESTONE);
					}
				}
			}

			if (allCobble && !event.getPlayer().capabilities.isCreativeMode) {
				shouldMakeGiantCobble = true;
				amountOfCobbleToReplace = 64;
			} else {
				shouldMakeGiantCobble = false;
				amountOfCobbleToReplace = 0;
			}

			// break all nearby blocks
			for (int dx = 0; dx < 4; dx++) {
				for (int dy = 0; dy < 4; dy++) {
					for (int dz = 0; dz < 4; dz++) {
						BlockPos dPos = bPos.add(dx, dy, dz);

						if (!dPos.equals(event.getPos()) && event.getState() == event.getWorld().getBlockState(dPos)) {
							// try to break that block too!
							if (event.getPlayer() instanceof EntityPlayerMP) {
								EntityPlayerMP playerMP = (EntityPlayerMP) event.getPlayer();

								playerMP.interactionManager.tryHarvestBlock(dPos);
							}
						}
					}
				}
			}

			isBreakingWithGiantPick = false;

		}
	}

	@SubscribeEvent
	public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
		if (!event.getEntityPlayer().capabilities.isCreativeMode) {

			World world = event.getEntityPlayer().world;
			EntityPlayer player = event.getEntityPlayer();

			if (!world.isRemote && isBlockProtectedFromInteraction(world, event.getPos()) && isAreaProtected(world, player, event.getPos())) {
				event.setUseBlock(Result.DENY);
			}
		}
	}

	/**
	 * Stop the player from interacting with blocks that could produce treasure or open doors in a protected area
	 */
	private static boolean isBlockProtectedFromInteraction(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();

		if (block == TFBlocks.towerDevice || block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST
				|| block == Blocks.STONE_BUTTON || block == Blocks.WOODEN_BUTTON || block == Blocks.LEVER) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isBlockProtectedFromBreaking(World world, BlockPos pos) {
		// todo improve
		return !world.getBlockState(pos).getBlock().getRegistryName().getResourcePath().contains("grave");
	}

	/**
	 * Return if the area at the coordinates is considered protected for that player.
	 * Currently, if we return true, we also send the area protection packet here.
	 */
	private static boolean isAreaProtected(World world, EntityPlayer player, BlockPos pos) {
		if (world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE) && TFWorld.getChunkGenerator(world) instanceof ChunkGeneratorTwilightForest) {
			ChunkGeneratorTwilightForest chunkProvider = (ChunkGeneratorTwilightForest) TFWorld.getChunkGenerator(world);
			
			if (chunkProvider != null && chunkProvider.isBlockInStructureBB(pos)) {
				// what feature is nearby?  is it one the player has not unlocked?
				TFFeature nearbyFeature = TFFeature.getFeatureAt(pos.getX(), pos.getZ(), world);

				if (!nearbyFeature.doesPlayerHaveRequiredAchievement(player) && chunkProvider.isBlockProtected(pos)) {
					
					// send protection packet
					StructureBoundingBox sbb = chunkProvider.getSBBAt(pos);
					sendAreaProtectionPacket(world, pos, sbb);
					
					// send a hint monster?
					nearbyFeature.trySpawnHintMonster(world, player, pos.getX(), pos.getY(), pos.getZ());

					return true;
				}
			}
		}
		return false;
	}

	private static void sendAreaProtectionPacket(World world, BlockPos pos, StructureBoundingBox sbb) {
		// send packet
		IMessage message = new PacketAreaProtection(sbb, pos);

		NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64);

		TFPacketHandler.CHANNEL.sendToAllAround(message, targetPoint);
	}

	/**
	 * Cancel attacks in protected areas
	 */
	@SubscribeEvent
	public static void livingAttack(LivingAttackEvent event) {
		// area protection check
		if (event.getEntityLiving() instanceof IMob && event.getSource().getTrueSource() instanceof EntityPlayerMP && !((EntityPlayer)event.getSource().getTrueSource()).capabilities.isCreativeMode && TFWorld.getChunkGenerator(event.getEntityLiving().world) instanceof ChunkGeneratorTwilightForest && event.getEntityLiving().world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE)) {

			ChunkGeneratorTwilightForest chunkProvider = (ChunkGeneratorTwilightForest) TFWorld.getChunkGenerator(event.getEntityLiving().getEntityWorld());

			BlockPos pos = new BlockPos(event.getEntityLiving());

			if (chunkProvider != null && chunkProvider.isBlockInStructureBB(pos) && chunkProvider.isBlockProtected(pos)) {
				// what feature is nearby?  is it one the player has not unlocked?
				TFFeature nearbyFeature = TFFeature.getFeatureAt(pos.getX(), pos.getZ(), event.getEntityLiving().world);

				if (!nearbyFeature.doesPlayerHaveRequiredAchievement((EntityPlayer) event.getSource().getTrueSource())) {
					event.setResult(Result.DENY);
					event.setCanceled(true);
					
					
					for (int i = 0; i < 20; i++) {
						TwilightForestMod.proxy.spawnParticle(event.getEntityLiving().world, TFParticleType.PROTECTION, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, 0, 0, 0);
					}
				}
			}
		}
	}

	/**
	 * When player logs in, report conflict status, set enforced_progression rule
	 */
	@SubscribeEvent
	public static void playerLogsIn(PlayerLoggedInEvent event) {
		// check enforced progression
		if (!event.player.world.isRemote && event.player instanceof EntityPlayerMP) {
			sendEnforcedProgressionStatus((EntityPlayerMP) event.player, event.player.world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE));
		}
	}

	/**
	 * When player changes dimensions, send the rule status if they're moving to the Twilight Forest
	 */
	@SubscribeEvent
	public static void playerPortals(PlayerChangedDimensionEvent event) {
		// check enforced progression
		if (!event.player.world.isRemote && event.player instanceof EntityPlayerMP && event.toDim == TFConfig.dimension.dimensionID) {
			sendEnforcedProgressionStatus((EntityPlayerMP) event.player, event.player.world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE));
		}
	}

	private static void sendEnforcedProgressionStatus(EntityPlayerMP player, boolean isEnforced) {
		TFPacketHandler.CHANNEL.sendTo(new PacketEnforceProgressionStatus(isEnforced), player);
	}

	/**
	 * When world is loaded, check if the game rule is defined
	 */
	@SubscribeEvent
	public static void worldLoaded(WorldEvent.Load event) {
		// check rule
		if (!event.getWorld().isRemote && !event.getWorld().getGameRules().hasRule(TwilightForestMod.ENFORCED_PROGRESSION_RULE)) {
			TwilightForestMod.LOGGER.info("Loaded a world with the tfEnforcedProgression game rule not defined.  Defining it.");

			event.getWorld().getGameRules().addGameRule(TwilightForestMod.ENFORCED_PROGRESSION_RULE, "true", GameRules.ValueType.BOOLEAN_VALUE);
		}
	}

	/**
	 * When a command is used, check if someone's changing the progression game rule
	 */
	@SubscribeEvent
	public static void commandSent(CommandEvent event) {
		if (event.getCommand() instanceof CommandGameRule && event.getParameters().length > 1 && TwilightForestMod.ENFORCED_PROGRESSION_RULE.equals(event.getParameters()[0])) {
			boolean isEnforced = Boolean.valueOf(event.getParameters()[1]);
			TFPacketHandler.CHANNEL.sendToAll(new PacketEnforceProgressionStatus(isEnforced));
		}
	}

}
