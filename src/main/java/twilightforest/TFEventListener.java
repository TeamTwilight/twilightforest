package twilightforest;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandGameRule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.items.ItemHandlerHelper;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.BlockTFCritter;
import twilightforest.block.BlockTFGiantBlock;
import twilightforest.block.BlockTFPortal;
import twilightforest.block.TFBlocks;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.client.particle.TFParticleType;
import twilightforest.compat.Baubles;
import twilightforest.compat.TFCompat;
import twilightforest.enchantment.TFEnchantment;
import twilightforest.entity.EntityIceArrow;
import twilightforest.entity.EntitySeekerArrow;
import twilightforest.entity.EntityTFCharmEffect;
import twilightforest.entity.EntityTFIceSnowball;
import twilightforest.entity.EntityTFMoonwormShot;
import twilightforest.entity.EntityTFNatureBolt;
import twilightforest.entity.EntityTFPinchBeetle;
import twilightforest.entity.EntityTFSlimeProjectile;
import twilightforest.entity.EntityTFTomeBolt;
import twilightforest.entity.EntityTFTwilightWandBolt;
import twilightforest.entity.EntityTFYeti;
import twilightforest.entity.boss.EntityTFIceBomb;
import twilightforest.entity.boss.EntityTFLichBolt;
import twilightforest.entity.boss.EntityTFLichBomb;
import twilightforest.entity.boss.EntityTFThrownWep;
import twilightforest.entity.boss.EntityTFUrGhastFireball;
import twilightforest.item.ItemTFPhantomArmor;
import twilightforest.item.TFItems;
import twilightforest.network.PacketAreaProtection;
import twilightforest.network.PacketEnforceProgressionStatus;
import twilightforest.network.TFPacketHandler;
import twilightforest.potions.TFPotions;
import twilightforest.util.TFItemStackUtils;
import twilightforest.world.ChunkGeneratorTFBase;
import twilightforest.world.TFWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * So much of the mod logic in this one class
 */
@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFEventListener {

	private static final ImmutableSet<String> SHIELD_DAMAGE_BLACKLIST = ImmutableSet.of(
			"inWall", "cramming", "drown", "starve", "fall", "flyIntoWall", "outOfWorld", "fallingBlock"
	);

	private static final Map<UUID, InventoryPlayer> playerKeepsMap = new HashMap<>();
	private static final Map<UUID, NonNullList<ItemStack>> playerKeepsMapBaubles = new HashMap<>();

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

			if (stack.getItem() == Item.getItemFromBlock(TFBlocks.giant_log)) {
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
					event.getDrops().add(new ItemStack(TFBlocks.giant_cobblestone));
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
		EntityLivingBase living = event.getEntityLiving();
		
		// fire aura
		if (living instanceof EntityPlayer && event.getSource().damageType.equals("mob") && event.getSource().getTrueSource() != null) {
			EntityPlayer player = (EntityPlayer) living;
			int fireLevel = TFEnchantment.getFieryAuraLevel(player.inventory, event.getSource());

			if (fireLevel > 0 && player.getRNG().nextInt(25) < fireLevel) {
				event.getSource().getTrueSource().setFire(fireLevel / 2);
			}
		}

		// chill aura
		if (living instanceof EntityPlayer && event.getSource().damageType.equals("mob") && event.getSource().getTrueSource() instanceof EntityLivingBase) {
			EntityPlayer player = (EntityPlayer) living;
			int chillLevel = TFEnchantment.getChillAuraLevel(player.inventory, event.getSource());

			if (chillLevel > 0) {
				((EntityLivingBase) event.getSource().getTrueSource()).addPotionEffect(new PotionEffect(TFPotions.frosty, chillLevel * 5 + 5, chillLevel));
			}
		}

		// triple bow strips hurtResistantTime
		if (event.getSource().damageType.equals("arrow") && event.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();

			if (player.getHeldItemMainhand().getItem() == TFItems.triple_bow || player.getHeldItemOffhand().getItem() == TFItems.triple_bow) {
				living.hurtResistantTime = 0;
			}
		}

		// enderbow teleports
		if (event.getSource().damageType.equals("arrow") && event.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();

			if (player.getHeldItemMainhand().getItem() == TFItems.ender_bow || player.getHeldItemOffhand().getItem() == TFItems.ender_bow) {

				double sourceX = player.posX;
				double sourceY = player.posY;
				double sourceZ = player.posZ;
				float sourceYaw = player.rotationYaw;
				float sourcePitch = player.rotationPitch;

				// this is the only method that will move the player properly
				player.rotationYaw = living.rotationYaw;
				player.rotationPitch = living.rotationPitch;
				player.setPositionAndUpdate(living.posX, living.posY, living.posZ);
				player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);


				// monsters are easy to move
				living.setPositionAndRotation(sourceX, sourceY, sourceZ, sourceYaw, sourcePitch);
				living.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
			}
		}

		// Smashing!
		Item item = living.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem();
		if (item instanceof ItemBlock && ((ItemBlock)item).getBlock() instanceof BlockTFCritter) {
			BlockTFCritter poorBug = (BlockTFCritter)((ItemBlock) item).getBlock();

			if (poorBug == TFBlocks.firefly)
				living.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.GLOWSTONE_DUST));

			if (poorBug == TFBlocks.cicada)
				living.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DYE, 1, 8));

			if (poorBug == TFBlocks.moonworm)
				living.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DYE, 1, 10));

			living.world.playSound(null, living.posX, living.posY, living.posZ, poorBug.getSoundType().getBreakSound(), living.getSoundCategory(), 1, 1);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void charmOfLife(LivingDeathEvent event) {

		EntityLivingBase living = event.getEntityLiving();
		if (living.world.isRemote) return;

		boolean charm1 = false;
		boolean charm2 = TFItemStackUtils.consumeInventoryItem(living, s -> s.getItem() == TFItems.charm_of_life_2, 1);
		if (!charm2) {
			charm1 = TFItemStackUtils.consumeInventoryItem(living, s -> s.getItem() == TFItems.charm_of_life_1, 1);
		}

		if (charm2 || charm1) {

			if (charm1) {
				living.setHealth(8);
				living.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 0));
			}

			if (charm2) {
				living.setHealth((float) living.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());

				living.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 600, 3));
				living.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 600, 0));
				living.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 600, 0));
			}

			// spawn effect thingers
			EntityTFCharmEffect effect = new EntityTFCharmEffect(living.world, living, charm1 ? TFItems.charm_of_life_1 : TFItems.charm_of_life_2);
			living.world.spawnEntity(effect);

			EntityTFCharmEffect effect2 = new EntityTFCharmEffect(living.world, living, charm1 ? TFItems.charm_of_life_1 : TFItems.charm_of_life_2);
			effect2.offset = (float) Math.PI;
			living.world.spawnEntity(effect2);

			living.world.playSound(null, living.posX, living.posY, living.posZ, SoundEvents.ITEM_TOTEM_USE, living.getSoundCategory(), 1, 1);

			event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void charmOfKeeping(LivingDeathEvent event) {

		EntityLivingBase living = event.getEntityLiving();
		if (living.world.isRemote) return;

		if (living instanceof EntityPlayer && !living.world.getGameRules().getBoolean("keepInventory")) {
			EntityPlayer player = (EntityPlayer) living;

			// drop any existing held items, just in case
			dropStoredItems(player);

			// TODO also consider situations where the actual slots may be empty, and charm gets consumed anyway. Usually won't happen.
			boolean tier3 =          TFItemStackUtils.consumeInventoryItem(player, s -> s.getItem() == TFItems.charm_of_keeping_3, 1);
			boolean tier2 = tier3 || TFItemStackUtils.consumeInventoryItem(player, s -> s.getItem() == TFItems.charm_of_keeping_2, 1);
			boolean tier1 = tier2 || TFItemStackUtils.consumeInventoryItem(player, s -> s.getItem() == TFItems.charm_of_keeping_1, 1);

			InventoryPlayer keepInventory = new InventoryPlayer(null);

			UUID playerUUID = player.getUniqueID();

			if (tier1) {
				keepAllArmor(player, keepInventory);
				keepOffHand(player, keepInventory);
			}

			if (tier3) {
				for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
					keepInventory.mainInventory.set(i, player.inventory.mainInventory.get(i).copy());
					player.inventory.mainInventory.set(i, ItemStack.EMPTY);
				}
				keepInventory.setItemStack(new ItemStack(TFItems.charm_of_keeping_3));

			} else if (tier2) {
				for (int i = 0; i < 9; i++) {
					keepInventory.mainInventory.set(i, player.inventory.mainInventory.get(i).copy());
					player.inventory.mainInventory.set(i, ItemStack.EMPTY);
				}
				keepInventory.setItemStack(new ItemStack(TFItems.charm_of_keeping_2));

			} else if (tier1) {
				int i = player.inventory.currentItem;
				if (InventoryPlayer.isHotbar(i)) {
					keepInventory.mainInventory.set(i, player.inventory.mainInventory.get(i).copy());
					player.inventory.mainInventory.set(i, ItemStack.EMPTY);
				}
				keepInventory.setItemStack(new ItemStack(TFItems.charm_of_keeping_1));
			}

			// always keep tower keys
			for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
				ItemStack stack = player.inventory.mainInventory.get(i);
				if (stack.getItem() == TFItems.tower_key) {
					keepInventory.mainInventory.set(i, stack.copy());
					player.inventory.mainInventory.set(i, ItemStack.EMPTY);
				}
			}

			if (tier1 && TFCompat.BAUBLES.isActivated()) {
				NonNullList<ItemStack> baubles = NonNullList.withSize(Baubles.getSlotAmount(player), ItemStack.EMPTY);
				Baubles.keepBaubles(player, baubles);
				playerKeepsMapBaubles.put(playerUUID, baubles);
			}

			for (int i = 0; i < player.inventory.armorInventory.size(); i++) { // TODO also consider Phantom tools, when those get added
				ItemStack armor = player.inventory.armorInventory.get(i);
				if (armor.getItem() instanceof ItemTFPhantomArmor) {
					keepInventory.armorInventory.set(i, armor.copy());
					player.inventory.armorInventory.set(i, ItemStack.EMPTY);
				}
			}

			playerKeepsMap.put(playerUUID, keepInventory);
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

	private static void keepOffHand(EntityPlayer player, InventoryPlayer keepInventory) {
		for (int i = 0; i < player.inventory.offHandInventory.size(); i++) {
			keepInventory.offHandInventory.set(i, player.inventory.offHandInventory.get(i).copy());
			player.inventory.offHandInventory.set(i, ItemStack.EMPTY);
		}
	}

	/**
	 * Maybe we kept some stuff for the player!
	 */
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerRespawnEvent event) {

		if (event.isEndConquered()) return;

		EntityPlayer player = event.player;
		InventoryPlayer keepInventory = playerKeepsMap.remove(player.getUniqueID());
		if (keepInventory != null) {
			TwilightForestMod.LOGGER.debug("Player {} respawned and received items held in storage", player.getName());

			NonNullList<ItemStack> displaced = NonNullList.create();

			for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
				ItemStack kept = keepInventory.armorInventory.get(i);
				if (!kept.isEmpty()) {
					ItemStack existing = player.inventory.armorInventory.set(i, kept);
					if (!existing.isEmpty()) {
						displaced.add(existing);
					}
				}
			}
			for (int i = 0; i < player.inventory.offHandInventory.size(); i++) {
				ItemStack kept = keepInventory.offHandInventory.get(i);
				if (!kept.isEmpty()) {
					ItemStack existing = player.inventory.offHandInventory.set(i, kept);
					if (!existing.isEmpty()) {
						displaced.add(existing);
					}
				}
			}
			for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
				ItemStack kept = keepInventory.mainInventory.get(i);
				if (!kept.isEmpty()) {
					ItemStack existing = player.inventory.mainInventory.set(i, kept);
					if (!existing.isEmpty()) {
						displaced.add(existing);
					}
				}
			}

			// try to give player any displaced items
			for (ItemStack extra : displaced) {
				ItemHandlerHelper.giveItemToPlayer(player, extra);
			}

			// spawn effect thingers
			if (!keepInventory.getItemStack().isEmpty()) {
				EntityTFCharmEffect effect = new EntityTFCharmEffect(player.world, player, keepInventory.getItemStack().getItem());
				player.world.spawnEntity(effect);

				EntityTFCharmEffect effect2 = new EntityTFCharmEffect(player.world, player, keepInventory.getItemStack().getItem());
				effect2.offset = (float) Math.PI;
				player.world.spawnEntity(effect2);

				player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, player.getSoundCategory(), 1.5F, 1.0F);
			}
		}

		if (TFCompat.BAUBLES.isActivated()) {
			NonNullList<ItemStack> baubles = playerKeepsMapBaubles.remove(player.getUniqueID());
			if (baubles != null) {
				TwilightForestMod.LOGGER.debug("Player {} respawned and received baubles held in storage", player.getName());
				Baubles.respawnBaubles(player, baubles);
			}
		}
	}

	/**
	 * Dump stored items if player logs out
	 */
	@SubscribeEvent
	public static void onPlayerLogout(PlayerLoggedOutEvent event) {
		dropStoredItems(event.player);
	}

	private static void dropStoredItems(EntityPlayer player) {
		InventoryPlayer keepInventory = playerKeepsMap.remove(player.getUniqueID());
		if (keepInventory != null) {
			TwilightForestMod.LOGGER.warn("Dropping inventory items previously held in reserve for player {}", player.getName());
			keepInventory.player = player;
			keepInventory.dropAllItems();
		}
		if (TFCompat.BAUBLES.isActivated()) {
			NonNullList<ItemStack> baubles = playerKeepsMapBaubles.remove(player.getUniqueID());
			if (baubles != null) {
				TwilightForestMod.LOGGER.warn("Dropping baubles previously held in reserve for player {}", player.getName());
				for (ItemStack itemStack : baubles) {
					if (!itemStack.isEmpty()) {
						player.dropItem(itemStack, true, false);
					}
				}
			}
		}
	}

	/**
	 * Stop the player from sneaking while riding an unfriendly creature
	 */
	@SubscribeEvent
	public static boolean livingUpdate(LivingUpdateEvent event) {
		if (event.getEntityLiving().hasCapability(CapabilityList.SHIELDS, null)) {
			IShieldCapability cap = event.getEntityLiving().getCapability(CapabilityList.SHIELDS, null);
			if (cap != null)
				cap.update();
		}
		if (event.getEntityLiving() instanceof EntityPlayer && event.getEntityLiving().isSneaking() && isRidingUnfriendly(event.getEntityLiving())) {
			event.getEntityLiving().setSneaking(false);
		}
		return true;
	}

	public static boolean isRidingUnfriendly(EntityLivingBase entity) {
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
				&& event.getPlayer().getHeldItemMainhand().getItem() == TFItems.giant_pickaxe
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

		if (block == TFBlocks.tower_device || block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST
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
		if (world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE) && TFWorld.getChunkGenerator(world) instanceof ChunkGeneratorTFBase) {
			ChunkGeneratorTFBase chunkProvider = (ChunkGeneratorTFBase) TFWorld.getChunkGenerator(world);
			
			if (chunkProvider != null && chunkProvider.isBlockInStructureBB(pos)) {
				// what feature is nearby?  is it one the player has not unlocked?
				TFFeature nearbyFeature = TFFeature.getFeatureAt(pos.getX(), pos.getZ(), world);

				if (!nearbyFeature.doesPlayerHaveRequiredAdvancements(player) && chunkProvider.isBlockProtected(pos)) {
					
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

	@SubscribeEvent
	public static void livingAttack(LivingAttackEvent event) {
		EntityLivingBase living = event.getEntityLiving();
		// cancel attacks in protected areas
		if (living instanceof IMob && event.getSource().getTrueSource() instanceof EntityPlayerMP && !((EntityPlayer) event.getSource().getTrueSource()).capabilities.isCreativeMode && TFWorld.getChunkGenerator(living.world) instanceof ChunkGeneratorTFBase && living.world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE)) {

			ChunkGeneratorTFBase chunkProvider = (ChunkGeneratorTFBase) TFWorld.getChunkGenerator(living.getEntityWorld());

			BlockPos pos = new BlockPos(living);

			if (chunkProvider != null && chunkProvider.isBlockInStructureBB(pos) && chunkProvider.isBlockProtected(pos)) {
				// what feature is nearby?  is it one the player has not unlocked?
				TFFeature nearbyFeature = TFFeature.getFeatureAt(pos.getX(), pos.getZ(), living.world);

				if (!nearbyFeature.doesPlayerHaveRequiredAdvancements((EntityPlayer) event.getSource().getTrueSource())) {
					event.setResult(Result.DENY);
					event.setCanceled(true);
					for (int i = 0; i < 20; i++) {
						TwilightForestMod.proxy.spawnParticle(living.world, TFParticleType.PROTECTION, living.posX, living.posY, living.posZ, 0, 0, 0);
					}
				}
			}
		}
		// shields
		if (!living.world.isRemote && living.hasCapability(CapabilityList.SHIELDS, null) && !SHIELD_DAMAGE_BLACKLIST.contains(event.getSource().damageType)) {
			IShieldCapability cap = living.getCapability(CapabilityList.SHIELDS, null);
			if (cap != null && cap.shieldsLeft() > 0) {
				cap.breakShield();
				event.setCanceled(true);
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

	// Teleport first-time players to Twilight Forest

	private static final String NBT_TAG_TWILIGHT = "twilightforest_banished";
	@SubscribeEvent
	public static void banishNewbieToTwilightZone(PlayerEvent.PlayerLoggedInEvent event) {
		NBTTagCompound tagCompound = event.player.getEntityData();
		NBTTagCompound playerData = tagCompound.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

		// getBoolean returns false, if false or didn't exist
		boolean shouldBanishPlayer = TFConfig.dimension.newPlayersSpawnInTF && !playerData.getBoolean(NBT_TAG_TWILIGHT);

		playerData.setBoolean(NBT_TAG_TWILIGHT, true); // set true once player has spawned either way
		tagCompound.setTag(EntityPlayer.PERSISTED_NBT_TAG, playerData); // commit

		if (shouldBanishPlayer) BlockTFPortal.attemptSendPlayer(event.player, true); // See ya hate to be ya
	}

	// Advancement Trigger
	@SubscribeEvent
	public static void onAdvancementGet(AdvancementEvent event) {
		EntityPlayer player = event.getEntityPlayer();

		if (player instanceof EntityPlayerMP)
			TFAdvancements.ADVANCEMENT_UNLOCKED.trigger((EntityPlayerMP) player);
	}

	// Parrying

	private static boolean globalParry = !Loader.isModLoaded("parry");

	@SubscribeEvent
	public static void arrowParry(ProjectileImpactEvent.Arrow event) {
		final EntityArrow projectile = event.getArrow();

		if (!projectile.getEntityWorld().isRemote && globalParry &&
				(TFConfig.shieldInteractions.parryNonTwilightAttacks
				|| projectile instanceof EntityIceArrow)
				|| projectile instanceof EntitySeekerArrow) {

			Entity entity = event.getRayTraceResult().entityHit;

			if (event.getEntity() != null && entity instanceof EntityLivingBase) {
				EntityLivingBase entityBlocking = (EntityLivingBase) entity;

				if (entityBlocking.canBlockDamageSource(new DamageSource("parry_this") {
					@Override
					public Vec3d getDamageLocation() { return projectile.getPositionVector(); }
				}) && (entityBlocking.getActiveItemStack().getItem().getMaxItemUseDuration(entityBlocking.getActiveItemStack()) - entityBlocking.getItemInUseCount()) <= TFConfig.shieldInteractions.shieldParryTicksArrow) {
					Vec3d playerVec3 = entityBlocking.getLookVec();

					projectile.shoot(playerVec3.x, playerVec3.y, playerVec3.z, 1.1F, 0.1F);  // reflect faster and more accurately

					projectile.shootingEntity = entityBlocking;

					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void fireballParry(ProjectileImpactEvent.Fireball event) {
		final EntityFireball projectile = event.getFireball();

		if (!projectile.getEntityWorld().isRemote && globalParry &&
				(TFConfig.shieldInteractions.parryNonTwilightAttacks
				|| projectile instanceof EntityTFUrGhastFireball)) {

			Entity entity = event.getRayTraceResult().entityHit;

			if (event.getEntity() != null && entity instanceof EntityLivingBase) {
				EntityLivingBase entityBlocking = (EntityLivingBase) entity;

				if (entityBlocking.canBlockDamageSource(new DamageSource("parry_this") {
					@Override
					public Vec3d getDamageLocation() { return projectile.getPositionVector(); }
				}) && (entityBlocking.getActiveItemStack().getItem().getMaxItemUseDuration(entityBlocking.getActiveItemStack()) - entityBlocking.getItemInUseCount()) <= TFConfig.shieldInteractions.shieldParryTicksFireball) {
					Vec3d playerVec3 = entityBlocking.getLookVec();

					projectile.motionX = playerVec3.x;
					projectile.motionY = playerVec3.y;
					projectile.motionZ = playerVec3.z;
					projectile.accelerationX = projectile.motionX * 0.1D;
					projectile.accelerationY = projectile.motionY * 0.1D;
					projectile.accelerationZ = projectile.motionZ * 0.1D;

					projectile.shootingEntity = entityBlocking;

					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void throwableParry(ProjectileImpactEvent.Throwable event) {
		final EntityThrowable projectile = event.getThrowable();

		if (!projectile.getEntityWorld().isRemote && globalParry &&
				(TFConfig.shieldInteractions.parryNonTwilightAttacks
				|| projectile instanceof EntityTFTwilightWandBolt
				|| projectile instanceof EntityTFLichBolt
				|| projectile instanceof EntityTFLichBomb
				|| projectile instanceof EntityTFThrownWep
				|| projectile instanceof EntityTFIceBomb
				|| projectile instanceof EntityTFIceSnowball
				|| projectile instanceof EntityTFSlimeProjectile
				|| projectile instanceof EntityTFMoonwormShot
				|| projectile instanceof EntityTFTomeBolt
				|| projectile instanceof EntityTFNatureBolt)) {

			Entity entity = event.getRayTraceResult().entityHit;

			if (event.getEntity() != null && entity instanceof EntityLivingBase) {
				EntityLivingBase entityBlocking = (EntityLivingBase) entity;

				if (entityBlocking.canBlockDamageSource(new DamageSource("parry_this") {
					@Override
					public Vec3d getDamageLocation() { return projectile.getPositionVector(); }
				}) && (entityBlocking.getActiveItemStack().getItem().getMaxItemUseDuration(entityBlocking.getActiveItemStack()) - entityBlocking.getItemInUseCount()) <= TFConfig.shieldInteractions.shieldParryTicksThrowable) {
					Vec3d playerVec3 = entityBlocking.getLookVec();

					projectile.shoot(playerVec3.x, playerVec3.y, playerVec3.z, 1.1F, 0.1F);  // reflect faster and more accurately

					projectile.thrower = entityBlocking;

					event.setCanceled(true);
				}
			}
		}
	}
}
