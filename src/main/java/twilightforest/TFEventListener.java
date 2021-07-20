package twilightforest;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemHandlerHelper;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.*;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.data.BlockTagGenerator;
import twilightforest.enchantment.TFEnchantment;
import twilightforest.entity.EntityTFCharmEffect;
import twilightforest.entity.IHostileMount;
import twilightforest.entity.TFEntities;
import twilightforest.entity.projectile.ITFProjectile;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.item.ItemTFPhantomArmor;
import twilightforest.item.TFItems;
import twilightforest.network.*;
import twilightforest.potions.TFPotions;
import twilightforest.tileentity.TileEntityKeepsakeCasket;
import twilightforest.util.TFItemStackUtils;
import twilightforest.world.ChunkGeneratorTwilightBase;
import twilightforest.world.TFGenerationSettings;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * So much of the mod logic in this one class
 */
@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFEventListener {

	private static final ImmutableSet<String> SHIELD_DAMAGE_BLACKLIST = ImmutableSet.of(
			"inWall", "cramming", "drown", "starve", "fall", "flyIntoWall", "outOfWorld", "fallingBlock"
	);

	private static final Map<UUID, PlayerInventory> playerKeepsMap = new HashMap<>();
	//private static final Map<UUID, NonNullList<ItemStack>> playerKeepsMapBaubles = new HashMap<>();

	private static boolean isBreakingWithGiantPick = false;
	private static boolean shouldMakeGiantCobble = false;
	private static int amountOfCobbleToReplace = 0;

	@SubscribeEvent
	public static void addReach(ItemAttributeModifierEvent event) {
		Item item = event.getItemStack().getItem();
		if((item == TFItems.giant_pickaxe.get() || item == TFItems.giant_sword.get()) && event.getSlotType() == EquipmentSlotType.MAINHAND) {
			event.addModifier(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(TFItems.GIANT_REACH_MODIFIER, "Tool modifier", 2.5, AttributeModifier.Operation.ADDITION));
		}
	}

	@SubscribeEvent
	public static void onCrafting(PlayerEvent.ItemCraftedEvent event) {
		ItemStack itemStack = event.getCrafting();
		PlayerEntity player = event.getPlayer();

		// if we've crafted 64 planks from a giant log, sneak 192 more planks into the player's inventory or drop them nearby
		//TODO: Can this be an Ingredient?
		if (itemStack.getItem() == Item.getItemFromBlock(Blocks.OAK_PLANKS) && itemStack.getCount() == 64 && doesCraftMatrixHaveGiantLog(event.getInventory())) {
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Blocks.OAK_PLANKS, 64));
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Blocks.OAK_PLANKS, 64));
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Blocks.OAK_PLANKS, 64));
		}
	}

	private static boolean doesCraftMatrixHaveGiantLog(IInventory inv) {
		Item giantLogItem = Item.getItemFromBlock(TFBlocks.giant_log.get());
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i).getItem() == giantLogItem) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Also check if we need to transform 64 cobbles into a giant cobble
	 */
	public static class ManipulateDrops extends LootModifier {

		protected ManipulateDrops(ILootCondition[] conditionsIn) {
			super(conditionsIn);
		}

		@Nonnull
		@Override
		protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
			List<ItemStack> newLoot = new ArrayList<>();
			boolean flag = false;
			if (shouldMakeGiantCobble && generatedLoot.size() > 0) {
				// turn the next 64 cobblestone drops into one giant cobble
				if (generatedLoot.get(0).getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE)) {
					generatedLoot.remove(0);
					if (amountOfCobbleToReplace == 64) {
						newLoot.add(new ItemStack(TFBlocks.giant_cobblestone.get()));
						flag = true;
					}
					amountOfCobbleToReplace--;
					if (amountOfCobbleToReplace <= 0) {
						shouldMakeGiantCobble = false;
					}
				}
			}
			return flag ? newLoot : generatedLoot;
		}
	}

	public static class Serializer extends GlobalLootModifierSerializer<ManipulateDrops> {

		@Override
		public ManipulateDrops read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
			return new ManipulateDrops(conditionsIn);
		}

		@Override
		public JsonObject write(ManipulateDrops instance) {
			return null;
		}
	}

	@SubscribeEvent
	public static void entityHurts(LivingHurtEvent event) {

		LivingEntity living = event.getEntityLiving();
		DamageSource damageSource = event.getSource();
		String damageType = damageSource.getDamageType();
		Entity trueSource = damageSource.getTrueSource();

		// fire aura
		if (living instanceof PlayerEntity && damageType.equals("mob") && trueSource != null) {
			PlayerEntity player = (PlayerEntity) living;
			int fireLevel = TFEnchantment.getFieryAuraLevel(player.inventory, damageSource);

			if (fireLevel > 0 && player.getRNG().nextInt(25) < fireLevel) {
				trueSource.setFire(fireLevel / 2);
			}
		}

		// chill aura
		if (living instanceof PlayerEntity && damageType.equals("mob") && trueSource instanceof LivingEntity) {
			PlayerEntity player = (PlayerEntity) living;
			int chillLevel = TFEnchantment.getChillAuraLevel(player.inventory, damageSource);

			if (chillLevel > 0) {
				((LivingEntity) trueSource).addPotionEffect(new EffectInstance(TFPotions.frosty.get(), chillLevel * 5 + 5, chillLevel));
			}
		}

		// triple bow strips hurtResistantTime
		if (damageType.equals("arrow") && trueSource instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) trueSource;

			if (player.getHeldItemMainhand().getItem() == TFItems.triple_bow.get() || player.getHeldItemOffhand().getItem() == TFItems.triple_bow.get()) {
				living.hurtResistantTime = 0;
			}
		}

		// enderbow teleports

		// Smashing!
		if (damageSource != DamageSource.FALL && damageSource != DamageSource.DROWN && damageSource != DamageSource.SWEET_BERRY_BUSH) {
			ItemStack stack = living.getItemStackFromSlot(EquipmentSlotType.HEAD);
			Block block = Block.getBlockFromItem(stack.getItem());
			if (block instanceof BlockTFCritter) {
				BlockTFCritter poorBug = (BlockTFCritter) block;
				living.setItemStackToSlot(EquipmentSlotType.HEAD, poorBug.getSquishResult());
				living.world.playSound(null, living.getPosX(), living.getPosY(), living.getPosZ(), TFSounds.BUG_SQUISH, living.getSoundCategory(), 1, 1);
			}
		}

		// lets not make the player take suffocation damage if riding something
		if (living instanceof PlayerEntity && isRidingUnfriendly(living) && damageSource == DamageSource.IN_WALL) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	// For when the player dies
	public static void applyDeathItems(LivingDeathEvent event) {
		LivingEntity living = event.getEntityLiving();

		if (living.world.isRemote || !(living instanceof PlayerEntity) || living instanceof FakePlayer) return;

		PlayerEntity player = (PlayerEntity) living; // To avoid triple-casting

		if (charmOfLife(player)) {
			event.setCanceled(true); // Executes if the player had charms
		} else if (!living.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
			// Did the player recover? No? Let's give them their stuff based on the keeping charms
			charmOfKeeping(player);

			// Then let's store the rest of their stuff in the casket
			keepsakeCasket(player);
		}
	}

	private static boolean casketExpiration = false;
	private static void keepsakeCasket(PlayerEntity player) {
		boolean casketConsumed = TFItemStackUtils.consumeInventoryItem(player, TFBlocks.keepsake_casket.get().asItem());

		if (casketConsumed) {
			World world = player.getEntityWorld();
			BlockPos.Mutable pos = player.getPosition().toMutable();

			if (pos.getY() < 2) {
				pos.setY(2);
			} else {
				int logicalHeight = player.getEntityWorld().getDimensionType().getLogicalHeight();

				if (pos.getY() > logicalHeight) {
					pos.setY(logicalHeight - 1);
				}
			}

			// TODO determine if block was air or better yet make a tag list of blocks that are OK to place the casket in
			BlockPos immutablePos = pos.toImmutable();
			FluidState fluidState = world.getFluidState(immutablePos);

			if (world.setBlockState(immutablePos, TFBlocks.keepsake_casket.get().getDefaultState().with(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.getFromFluid(fluidState.getFluid())).with(BlockKeepsakeCasket.BREAKAGE, TFItemStackUtils.damage))) {
				TileEntity te = world.getTileEntity(immutablePos);

				if (te instanceof TileEntityKeepsakeCasket) {
					TileEntityKeepsakeCasket casket = (TileEntityKeepsakeCasket) te;

					if (TFConfig.COMMON_CONFIG.casketUUIDLocking.get()) {
						//make it so only the player who died can open the chest if our config allows us
						casket.playeruuid = player.getGameProfile().getId();
					} else {
						casket.playeruuid = null;
					}

					//some names are way too long for the casket so we'll cut them down
					String modifiedName;
					if (player.getName().getString().length() > 12)
						modifiedName = player.getName().getString().substring(0, 12);
					else modifiedName = player.getName().getString();
					casket.name = player.getName().getString();
					casket.casketname = modifiedName;
					casket.setCustomName(new StringTextComponent(modifiedName + "'s " + (world.rand.nextInt(10000) == 0 ? "Costco Casket" : casket.getDisplayName().getString())));
					int damage = world.getBlockState(immutablePos).get(BlockKeepsakeCasket.BREAKAGE);
					if (world.rand.nextFloat() <= 0.15F) {
						if (damage >= 2) {
							player.inventory.dropAllItems();
							world.setBlockState(immutablePos, Blocks.AIR.getDefaultState());
							casketExpiration = true;
							TwilightForestMod.LOGGER.debug("{}'s Casket damage value was too high, alerting the player and dropping extra items", player.getName().getString());
						} else {
							damage = damage + 1;
							world.setBlockState(immutablePos, TFBlocks.keepsake_casket.get().getDefaultState().with(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.getFromFluid(fluidState.getFluid())).with(BlockKeepsakeCasket.BREAKAGE, damage));
							TwilightForestMod.LOGGER.debug("{}'s Casket was randomly damaged, applying new damage", player.getName().getString());
						}
					}
					int casketCapacity = casket.getSizeInventory();
					List<ItemStack> list = new ArrayList<>(casketCapacity);
					NonNullList<ItemStack> filler = NonNullList.withSize(4, ItemStack.EMPTY);

					// lets add our inventory exactly how it was on us
					list.addAll(TFItemStackUtils.sortArmorForCasket(player));
					player.inventory.armorInventory.clear();
					list.addAll(filler);
					list.addAll(player.inventory.offHandInventory);
					player.inventory.offHandInventory.clear();
					list.addAll(TFItemStackUtils.sortInvForCasket(player));
					player.inventory.mainInventory.clear();

					casket.setItems(NonNullList.from(ItemStack.EMPTY, list.toArray(new ItemStack[casketCapacity])));
				}
			} else {
				TwilightForestMod.LOGGER.error("Could not place Keepsake Casket at " + pos.toString());
			}
		}
	}

	@SubscribeEvent
	//if our casket is owned by someone and that player isnt the one breaking it, stop them
	public static void onCasketBreak(BreakEvent event) {
		Block block = event.getState().getBlock();
		PlayerEntity player = event.getPlayer();
		TileEntity te = event.getWorld().getTileEntity(event.getPos());
		UUID checker;
		if(block == TFBlocks.keepsake_casket.get()) {
			if(te instanceof TileEntityKeepsakeCasket) {
				TileEntityKeepsakeCasket casket = (TileEntityKeepsakeCasket) te;
				 checker = casket.playeruuid;
			} else checker = null;
			if(checker != null) {
				if (!((TileEntityKeepsakeCasket) te).isEmpty()) {
					if(!player.hasPermissionLevel(3) || !player.getGameProfile().getId().equals(checker)) {
						event.setCanceled(true);
					}
				}
			}
		}
	}

	private static boolean charmOfLife(PlayerEntity player) {
		boolean charm2 = TFItemStackUtils.consumeInventoryItem(player, TFItems.charm_of_life_2.get());
		boolean charm1 = !charm2 && TFItemStackUtils.consumeInventoryItem(player, TFItems.charm_of_life_1.get());

		if (charm2 || charm1) {
			if (charm1) {
				player.setHealth(8);
				player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 100, 0));
			}

			if (charm2) {
				player.setHealth((float) player.getAttribute(Attributes.MAX_HEALTH).getBaseValue()); //Max Health

				player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 600, 3));
				player.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 600, 0));
				player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 600, 0));
			}

			// spawn effect thingers
			EntityTFCharmEffect effect = new EntityTFCharmEffect(TFEntities.charm_effect, player.world, player, charm1 ? TFItems.charm_of_life_1.get() : TFItems.charm_of_life_2.get());
			player.world.addEntity(effect);

			EntityTFCharmEffect effect2 = new EntityTFCharmEffect(TFEntities.charm_effect, player.world, player, charm1 ? TFItems.charm_of_life_1.get() : TFItems.charm_of_life_2.get());
			effect2.offset = (float) Math.PI;
			player.world.addEntity(effect2);

			player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), TFSounds.CHARM_LIFE, player.getSoundCategory(), 1, 1);

			return true;
		}

		return false;
	}

	private static void charmOfKeeping(PlayerEntity player) {
		// drop any existing held items, just in case
		dropStoredItems(player);

		// TODO also consider situations where the actual slots may be empty, and charm gets consumed anyway. Usually won't happen.
		boolean tier3 = TFItemStackUtils.consumeInventoryItem(player, TFItems.charm_of_keeping_3.get());
		boolean tier2 = tier3 || TFItemStackUtils.consumeInventoryItem(player, TFItems.charm_of_keeping_2.get());
		boolean tier1 = tier2 || TFItemStackUtils.consumeInventoryItem(player, TFItems.charm_of_keeping_1.get());

		PlayerInventory keepInventory = new PlayerInventory(null);

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
			keepInventory.setItemStack(new ItemStack(TFItems.charm_of_keeping_3.get()));

		} else if (tier2) {
			for (int i = 0; i < 9; i++) {
				keepInventory.mainInventory.set(i, player.inventory.mainInventory.get(i).copy());
				player.inventory.mainInventory.set(i, ItemStack.EMPTY);
			}
			keepInventory.setItemStack(new ItemStack(TFItems.charm_of_keeping_2.get()));

		} else if (tier1) {
			int i = player.inventory.currentItem;
			if (PlayerInventory.isHotbar(i)) {
				keepInventory.mainInventory.set(i, player.inventory.mainInventory.get(i).copy());
				player.inventory.mainInventory.set(i, ItemStack.EMPTY);
			}
			keepInventory.setItemStack(new ItemStack(TFItems.charm_of_keeping_1.get()));
		}

		//TODO: Baubles is dead, replace with curios
		/*if (tier1 && TFCompat.BAUBLES.isActivated()) {
			playerKeepsMapBaubles.put(playerUUID, Baubles.keepBaubles(player));
		}*/

		// always keep tower keys and held phantom armor
		for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
			ItemStack stack = player.inventory.mainInventory.get(i);
			if (stack.getItem() == TFItems.tower_key.get()) {
				keepInventory.mainInventory.set(i, stack.copy());
				player.inventory.mainInventory.set(i, ItemStack.EMPTY);
			}
			if (stack.getItem() instanceof ItemTFPhantomArmor) {
				keepInventory.mainInventory.set(i, stack.copy());
				player.inventory.mainInventory.set(i, ItemStack.EMPTY);
			}
		}

		// Keep phantom equipment
		for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
			ItemStack armor = player.inventory.armorInventory.get(i);
			if (armor.getItem() instanceof ItemTFPhantomArmor) {
				keepInventory.armorInventory.set(i, armor.copy());
				player.inventory.armorInventory.set(i, ItemStack.EMPTY);
			}
		}

		playerKeepsMap.put(playerUUID, keepInventory);
	}

	/**
	 * Move the full armor inventory to the keep pile
	 */
	private static void keepAllArmor(PlayerEntity player, PlayerInventory keepInventory) {
		for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
			keepInventory.armorInventory.set(i, player.inventory.armorInventory.get(i).copy());
			player.inventory.armorInventory.set(i, ItemStack.EMPTY);
		}
	}

	private static void keepOffHand(PlayerEntity player, PlayerInventory keepInventory) {
		for (int i = 0; i < player.inventory.offHandInventory.size(); i++) {
			keepInventory.offHandInventory.set(i, player.inventory.offHandInventory.get(i).copy());
			player.inventory.offHandInventory.set(i, ItemStack.EMPTY);
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (event.isEndConquered()) {
			updateCapabilities((ServerPlayerEntity) event.getPlayer(), event.getPlayer());
		} else {
			if(casketExpiration) {
				event.getPlayer().sendMessage(new TranslationTextComponent("block.twilightforest.casket.broken").mergeStyle(TextFormatting.DARK_RED), event.getPlayer().getUniqueID());
			}
			returnStoredItems(event.getPlayer());
		}
	}

	/**
	 * Maybe we kept some stuff for the player!
	 */
	private static void returnStoredItems(PlayerEntity player) {
		PlayerInventory keepInventory = playerKeepsMap.remove(player.getUniqueID());
		if (keepInventory != null) {
			TwilightForestMod.LOGGER.debug("Player {} ({}) respawned and received items held in storage", player.getName().getString(), player.getUniqueID());

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
				EntityTFCharmEffect effect = new EntityTFCharmEffect(TFEntities.charm_effect, player.world, player, keepInventory.getItemStack().getItem());
				player.world.addEntity(effect);

				EntityTFCharmEffect effect2 = new EntityTFCharmEffect(TFEntities.charm_effect, player.world, player, keepInventory.getItemStack().getItem());
				effect2.offset = (float) Math.PI;
				player.world.addEntity(effect2);

				player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), TFSounds.CHARM_KEEP, player.getSoundCategory(), 1.5F, 1.0F);
			}
		}

		//TODO: Baubles is dead, replace with curios
		/*if (TFCompat.BAUBLES.isActivated()) {
			NonNullList<ItemStack> baubles = playerKeepsMapBaubles.remove(player.getUniqueID());
			if (baubles != null) {
				TwilightForestMod.LOGGER.debug("Player {} respawned and received baubles held in storage", player.getName());
				Baubles.returnBaubles(player, baubles);
			}
		}*/
	}

	/**
	 * Dump stored items if player logs out
	 */
	@SubscribeEvent
	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		dropStoredItems(event.getPlayer());
	}

	private static void dropStoredItems(PlayerEntity player) {
		PlayerInventory keepInventory = playerKeepsMap.remove(player.getUniqueID());
		if (keepInventory != null) {
			TwilightForestMod.LOGGER.warn("Dropping inventory items previously held in reserve for player {} ({})", player.getName().getString(), player.getUniqueID());
			keepInventory.player = player;
			keepInventory.dropAllItems();
		}
		//TODO: Baubles is dead, replace with curios
		/*if (TFCompat.BAUBLES.isActivated()) {
			NonNullList<ItemStack> baubles = playerKeepsMapBaubles.remove(player.getUniqueID());
			if (baubles != null) {
				TwilightForestMod.LOGGER.warn("Dropping baubles previously held in reserve for player {}", player.getName());
				for (ItemStack itemStack : baubles) {
					if (!itemStack.isEmpty()) {
						player.dropItem(itemStack, true, false);
					}
				}
			}
		}*/
	}

	@SubscribeEvent
	public static void livingUpdate(LivingUpdateEvent event) {
		LivingEntity living = event.getEntityLiving();

		living.getCapability(CapabilityList.SHIELDS).ifPresent(IShieldCapability::update);

		// Stop the player from sneaking while riding an unfriendly creature
		if (living instanceof PlayerEntity && living.isSneaking() && isRidingUnfriendly(living)) {
			living.setSneaking(false);
		}
	}

	public static boolean isRidingUnfriendly(LivingEntity entity) {
		return entity.isPassenger() && entity.getRidingEntity() instanceof IHostileMount;
	}

	/**
	 * Check if the player is trying to break a block in a structure that's considered unbreakable for progression reasons
	 * Also check for breaking blocks with the giant's pickaxe and maybe break nearby blocks
	 */
	@SubscribeEvent
	public static void breakBlock(BreakEvent event) {
		PlayerEntity player = event.getPlayer();
		BlockPos pos = event.getPos();
		BlockState state = event.getState();

		if (!(event.getWorld() instanceof World) || ((World) event.getWorld()).isRemote) return;

		World world = (World) event.getWorld();

		if (isBlockProtectedFromBreaking(world, pos) && isAreaProtected(world, player, pos)) {
			event.setCanceled(true);

		} else if (!isBreakingWithGiantPick && canHarvestWithGiantPick(player, state)) {

			isBreakingWithGiantPick = true;

			// check nearby blocks for same block or same drop

			// pre-check for cobble!
			Item cobbleItem = Blocks.COBBLESTONE.asItem();
			boolean allCobble = state.getBlock().asItem() == cobbleItem;

			if (allCobble) {
				for (BlockPos dPos : BlockTFGiantBlock.getVolume(pos)) {
					if (dPos.equals(pos))
						continue;
					BlockState stateThere = world.getBlockState(dPos);
					if (stateThere.getBlock().asItem() != cobbleItem) {
						allCobble = false;
						break;
					}
				}
			}

			if (allCobble && !player.abilities.isCreativeMode) {
				shouldMakeGiantCobble = true;
				amountOfCobbleToReplace = 64;
			} else {
				shouldMakeGiantCobble = false;
				amountOfCobbleToReplace = 0;
			}

			// break all nearby blocks
			if (player instanceof ServerPlayerEntity) {
				ServerPlayerEntity playerMP = (ServerPlayerEntity) player;
				for (BlockPos dPos : BlockTFGiantBlock.getVolume(pos)) {
					if (!dPos.equals(pos) && state == world.getBlockState(dPos)) {
						// try to break that block too!
						playerMP.interactionManager.tryHarvestBlock(dPos);
					}
				}
			}

			isBreakingWithGiantPick = false;
		}
	}

	private static boolean canHarvestWithGiantPick(PlayerEntity player, BlockState state) {
		ItemStack heldStack = player.getHeldItemMainhand();
		Item heldItem = heldStack.getItem();
		return heldItem == TFItems.giant_pickaxe.get() && heldItem.canHarvestBlock(heldStack, state);
	}

	@SubscribeEvent
	public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {

		PlayerEntity player = event.getPlayer();
		World world = player.world;

		if (!world.isRemote && isBlockProtectedFromInteraction(world, event.getPos()) && isAreaProtected(world, player, event.getPos())) {
			event.setUseBlock(Event.Result.DENY);
		}
	}

	/**
	 * Stop the player from interacting with blocks that could produce treasure or open doors in a protected area
	 */
	private static boolean isBlockProtectedFromInteraction(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		return block.isIn(BlockTagGenerator.STRUCTURE_BANNED_INTERACTIONS);
	}

	private static boolean isBlockProtectedFromBreaking(World world, BlockPos pos) {
		// todo improve
		return !world.getBlockState(pos).getBlock().getRegistryName().getPath().contains("grave") || !world.getBlockState(pos).getBlock().matchesBlock(TFBlocks.keepsake_casket.get());
	}

	/**
	 * Return if the area at the coordinates is considered protected for that player.
	 * Currently, if we return true, we also send the area protection packet here.
	 */
	private static boolean isAreaProtected(World world, PlayerEntity player, BlockPos pos) {

		if (player.abilities.isCreativeMode || !TFGenerationSettings.isProgressionEnforced(world) || player instanceof FakePlayer) {
			return false;
		}

		ChunkGeneratorTwilightBase chunkGenerator = TFGenerationSettings.getChunkGenerator(world);


		if (chunkGenerator != null) {
			Optional<StructureStart<?>> struct = TFGenerationSettings.locateTFStructureInRange((ServerWorld) world, pos, 0);
			if(struct.isPresent()) {
				StructureStart<?> structure = struct.get();
				if(structure.getBoundingBox().isVecInside(pos)) {
					// what feature is nearby?  is it one the player has not unlocked?
					TFFeature nearbyFeature = TFFeature.getFeatureAt(pos.getX(), pos.getZ(), (ServerWorld) world);

					if (!nearbyFeature.doesPlayerHaveRequiredAdvancements(player)/* && chunkGenerator.isBlockProtected(pos)*/) {

						// TODO: This is terrible but *works* for now.. proper solution is to figure out why the stronghold bounding box is going so high
						if (nearbyFeature == TFFeature.KNIGHT_STRONGHOLD && pos.getY() >= 33)
							return false;

						// send protection packet
						MutableBoundingBox bb = structure.getBoundingBox();//new MutableBoundingBox(pos, pos.add(16, 16, 16)); // todo 1.15 get from structure
						sendAreaProtectionPacket(world, pos, bb);

						// send a hint monster?
						nearbyFeature.trySpawnHintMonster(world, player, pos);

						return true;
					}
				}
			}
		}
		return false;
	}

	private static void sendAreaProtectionPacket(World world, BlockPos pos, MutableBoundingBox sbb) {
		PacketDistributor.TargetPoint targetPoint = new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 64, world.getDimensionKey());
		TFPacketHandler.CHANNEL.send(PacketDistributor.NEAR.with(() -> targetPoint), new PacketAreaProtection(sbb, pos));
	}

	@SubscribeEvent
	public static void livingAttack(LivingAttackEvent event) {
		LivingEntity living = event.getEntityLiving();
		// cancel attacks in protected areas
		if (!living.world.isRemote && living instanceof IMob && event.getSource().getTrueSource() instanceof PlayerEntity
				&& isAreaProtected(living.world, (PlayerEntity) event.getSource().getTrueSource(), new BlockPos(living.getPosition()))) {

			event.setCanceled(true);
			return;
		}
		// shields
		if (!living.world.isRemote && !SHIELD_DAMAGE_BLACKLIST.contains(event.getSource().damageType)) {
			living.getCapability(CapabilityList.SHIELDS).ifPresent(cap -> {
				if (cap.shieldsLeft() > 0) {
					cap.breakShield();
					event.setCanceled(true);
				}
			});
		}
	}

	/**
	 * When player logs in, report conflict status, set enforced_progression rule
	 */
	@SubscribeEvent
	public static void playerLogsIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.getPlayer().world.isRemote && event.getPlayer() instanceof ServerPlayerEntity) {
			sendEnforcedProgressionStatus((ServerPlayerEntity) event.getPlayer(), TFGenerationSettings.isProgressionEnforced(event.getPlayer().world));
			updateCapabilities((ServerPlayerEntity) event.getPlayer(), event.getPlayer());
			banishNewbieToTwilightZone(event.getPlayer());
		}
	}

	/**
	 * When player changes dimensions, send the rule status if they're moving to the Twilight Forest
	 */
	@SubscribeEvent
	public static void playerPortals(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (!event.getPlayer().world.isRemote && event.getPlayer() instanceof ServerPlayerEntity) {
			if (event.getTo().getLocation().toString().equals(TFConfig.COMMON_CONFIG.DIMENSION.twilightForestID.get())) {
				sendEnforcedProgressionStatus((ServerPlayerEntity) event.getPlayer(), TFGenerationSettings.isProgressionEnforced(event.getPlayer().world));
			}

			updateCapabilities((ServerPlayerEntity) event.getPlayer(), event.getPlayer());
		}
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		updateCapabilities((ServerPlayerEntity) event.getPlayer(), event.getTarget());
	}

	// send any capabilities that are needed client-side
	private static void updateCapabilities(ServerPlayerEntity player, Entity entity) {
		entity.getCapability(CapabilityList.SHIELDS).ifPresent(cap -> {
			if (cap.shieldsLeft() > 0) {
				TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PacketUpdateShield(entity, cap));
			}
		});
	}

	private static void sendEnforcedProgressionStatus(ServerPlayerEntity player, boolean isEnforced) {
		TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PacketEnforceProgressionStatus(isEnforced));
	}

	private static void sendSkylightEnabled(ServerPlayerEntity player, boolean skylightEnabled) {
		TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PacketSetSkylightEnabled(skylightEnabled));
	}

	// TODO: This previously used the network-connection connected events
	// TODO: which no longer seem to be present.
	@SubscribeEvent
	public static void onClientConnect(PlayerEvent.PlayerLoggedInEvent event) {
		// This event is only ever fired on the server side
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
//		sendSkylightEnabled(player, TwilightForestDimension.isSkylightEnabled(TFGenerationSettings.getDimensionData(player.world)));
	}

	/**
	 * When world is loaded, check if the game rule is defined
	 */
	@SubscribeEvent
	public static void worldLoaded(WorldEvent.Load event) {
		IWorld world = event.getWorld();

		if (!world.isRemote() && world instanceof World && !((World) world).getGameRules().get(TwilightForestMod.ENFORCED_PROGRESSION_RULE).get()) {
			TwilightForestMod.LOGGER.info("Loaded a world with the {} game rule not defined. Defining it.", TwilightForestMod.ENFORCED_PROGRESSION_RULE);
			((World) world).getGameRules().get(TwilightForestMod.ENFORCED_PROGRESSION_RULE).set(TFConfig.COMMON_CONFIG.progressionRuleDefault.get(), ((World) world).getServer());
		}
	}

	// Teleport first-time players to Twilight Forest

	private static final String NBT_TAG_TWILIGHT = "twilightforest_banished";

	private static void banishNewbieToTwilightZone(PlayerEntity player) {
		CompoundNBT tagCompound = player.getPersistentData();
		CompoundNBT playerData = tagCompound.getCompound(PlayerEntity.PERSISTED_NBT_TAG);

		// getBoolean returns false, if false or didn't exist
		boolean shouldBanishPlayer = TFConfig.COMMON_CONFIG.DIMENSION.newPlayersSpawnInTF.get() && !playerData.getBoolean(NBT_TAG_TWILIGHT);

		playerData.putBoolean(NBT_TAG_TWILIGHT, true); // set true once player has spawned either way
		tagCompound.put(PlayerEntity.PERSISTED_NBT_TAG, playerData); // commit

		if (shouldBanishPlayer) BlockTFPortal.attemptSendPlayer(player, true); // See ya hate to be ya
	}

	// Advancement Trigger
	@SubscribeEvent
	public static void onAdvancementGet(AdvancementEvent event) {
		PlayerEntity player = event.getPlayer();
		if (player instanceof ServerPlayerEntity) {
			TFAdvancements.ADVANCEMENT_UNLOCKED.trigger((ServerPlayerEntity) player, event.getAdvancement());
		}
	}

	@SubscribeEvent
	public static void armorChanged(LivingEquipmentChangeEvent event) {
		LivingEntity living = event.getEntityLiving();
		if (!living.world.isRemote && living instanceof ServerPlayerEntity) {
			TFAdvancements.ARMOR_CHANGED.trigger((ServerPlayerEntity) living, event.getFrom(), event.getTo());
		}
	}

	// Parrying

	private static boolean globalParry = !ModList.get().isLoaded("parry");

	@SubscribeEvent
	public static void arrowParry(ProjectileImpactEvent.Arrow event) {
		final AbstractArrowEntity projectile = event.getArrow();

		if (!projectile.getEntityWorld().isRemote && globalParry &&
				(TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get()
						|| projectile instanceof ITFProjectile)) {

			if (event.getRayTraceResult() instanceof EntityRayTraceResult) {
				Entity entity = ((EntityRayTraceResult) event.getRayTraceResult()).getEntity();

				if (event.getEntity() != null && entity instanceof LivingEntity) {
					LivingEntity entityBlocking = (LivingEntity) entity;

					if (entityBlocking.canBlockDamageSource(new DamageSource("parry_this") {
						@Override
						public Vector3d getDamageLocation() {
							return projectile.getPositionVec();
						}
					}) && (entityBlocking.getActiveItemStack().getItem().getUseDuration(entityBlocking.getActiveItemStack()) - entityBlocking.getItemInUseCount()) <= TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.shieldParryTicksArrow.get()) {
						Vector3d playerVec3 = entityBlocking.getLookVec();

						projectile.shoot(playerVec3.x, playerVec3.y, playerVec3.z, 1.1F, 0.1F);  // reflect faster and more accurately

						projectile.setShooter(entityBlocking); //TODO: Verify

						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void fireballParry(ProjectileImpactEvent.Fireball event) {
		final DamagingProjectileEntity projectile = event.getFireball();

		if (!projectile.getEntityWorld().isRemote && globalParry &&
				(TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get()
						|| projectile instanceof ITFProjectile)) {

			if (event.getRayTraceResult() instanceof EntityRayTraceResult) {
				Entity entity = ((EntityRayTraceResult) event.getRayTraceResult()).getEntity();

				if (event.getEntity() != null && entity instanceof LivingEntity) {
					LivingEntity entityBlocking = (LivingEntity) entity;

					if (entityBlocking.canBlockDamageSource(new DamageSource("parry_this") {
						@Override
						public Vector3d getDamageLocation() {
							return projectile.getPositionVec();
						}
					}) && (entityBlocking.getActiveItemStack().getItem().getUseDuration(entityBlocking.getActiveItemStack()) - entityBlocking.getItemInUseCount()) <= TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.shieldParryTicksFireball.get()) {
						Vector3d playerVec3 = entityBlocking.getLookVec();

						projectile.setMotion(new Vector3d(playerVec3.x, playerVec3.y, playerVec3.z));
						projectile.accelerationX = projectile.getMotion().getX() * 0.1D;
						projectile.accelerationY = projectile.getMotion().getY() * 0.1D;
						projectile.accelerationZ = projectile.getMotion().getZ() * 0.1D;

						projectile.setShooter(entityBlocking); //TODO: Verify

						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void throwableParry(ProjectileImpactEvent.Throwable event) {
		final ThrowableEntity projectile = event.getThrowable();

		if (!projectile.getEntityWorld().isRemote && globalParry &&
				(TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get()
						|| projectile instanceof ITFProjectile)) {

			if (event.getRayTraceResult() instanceof EntityRayTraceResult) {
				Entity entity = ((EntityRayTraceResult) event.getRayTraceResult()).getEntity();


				if (event.getEntity() != null && entity instanceof LivingEntity) {
					LivingEntity entityBlocking = (LivingEntity) entity;

					if (entityBlocking.canBlockDamageSource(new DamageSource("parry_this") {
						@Override
						public Vector3d getDamageLocation() {
							return projectile.getPositionVec();
						}
					}) && (entityBlocking.getActiveItemStack().getItem().getUseDuration(entityBlocking.getActiveItemStack()) - entityBlocking.getItemInUseCount()) <= TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.shieldParryTicksThrowable.get()) {
						Vector3d playerVec3 = entityBlocking.getLookVec();

						projectile.shoot(playerVec3.x, playerVec3.y, playerVec3.z, 1.1F, 0.1F);  // reflect faster and more accurately

						projectile.setShooter(entityBlocking); //TODO: Verify

						event.setCanceled(true);
					}
				}
			}
		}
	}
}
