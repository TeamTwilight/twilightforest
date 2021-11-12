package twilightforest;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.items.ItemHandlerHelper;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.*;
import twilightforest.block.entity.KeepsakeCasketBlockEntity;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.data.BlockTagGenerator;
import twilightforest.enchantment.TFEnchantment;
import twilightforest.entity.CharmEffect;
import twilightforest.entity.IHostileMount;
import twilightforest.entity.TFEntities;
import twilightforest.entity.monster.Kobold;
import twilightforest.entity.passive.Bighorn;
import twilightforest.entity.passive.DwarfRabbit;
import twilightforest.entity.passive.Squirrel;
import twilightforest.entity.passive.TinyBird;
import twilightforest.entity.projectile.ITFProjectile;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.item.PhantomArmorItem;
import twilightforest.item.TFItems;
import twilightforest.network.AreaProtectionPacket;
import twilightforest.network.EnforceProgressionStatusPacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateShieldPacket;
import twilightforest.potions.TFPotions;
import twilightforest.util.TFItemStackUtils;
import twilightforest.util.TFStats;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.TFGenerationSettings;

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

	private static final Map<UUID, Inventory> playerKeepsMap = new HashMap<>();
	//private static final Map<UUID, NonNullList<ItemStack>> playerKeepsMapBaubles = new HashMap<>();

	private static boolean isBreakingWithGiantPick = false;
	private static boolean shouldMakeGiantCobble = false;
	private static int amountOfCobbleToReplace = 0;

	@SubscribeEvent
	public static void addReach(ItemAttributeModifierEvent event) {
		Item item = event.getItemStack().getItem();
		if((item == TFItems.GIANT_PICKAXE.get() || item == TFItems.GIANT_SWORD.get()) && event.getSlotType() == EquipmentSlot.MAINHAND) {
			event.addModifier(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(TFItems.GIANT_REACH_MODIFIER, "Tool modifier", 2.5, AttributeModifier.Operation.ADDITION));
		}
	}

	@SubscribeEvent
	public static void addPrey(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		EntityType<?> type = entity.getType();
		if(entity instanceof Mob mob) {
			if (type == EntityType.CAT) {
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal)entity, DwarfRabbit.class, false, null));
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal)entity, Squirrel.class, false, null));
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal)entity, TinyBird.class, false, null));
			} else if(type == EntityType.OCELOT) {
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, DwarfRabbit.class, false));
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, Squirrel.class, false));
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, TinyBird.class, false));
			} else if (type == EntityType.FOX) {
				mob.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mob, DwarfRabbit.class, false));
				mob.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mob, Squirrel.class, false));
			} else if(type == EntityType.WOLF) {
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal)entity, DwarfRabbit.class, false, null));
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal)entity, Squirrel.class, false, null));
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal)entity, Bighorn.class, false, null));
			}
		}
	}

	@SubscribeEvent
	public static void onCrafting(PlayerEvent.ItemCraftedEvent event) {
		ItemStack itemStack = event.getCrafting();
		Player player = event.getPlayer();

		// if we've crafted 64 planks from a giant log, sneak 192 more planks into the player's inventory or drop them nearby
		//TODO: Can this be an Ingredient?
		if (itemStack.getItem() == Item.byBlock(Blocks.OAK_PLANKS) && itemStack.getCount() == 64 && doesCraftMatrixHaveGiantLog(event.getInventory())) {
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Blocks.OAK_PLANKS, 64));
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Blocks.OAK_PLANKS, 64));
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Blocks.OAK_PLANKS, 64));
		}
	}

	private static boolean doesCraftMatrixHaveGiantLog(Container inv) {
		Item giantLogItem = Item.byBlock(TFBlocks.GIANT_LOG.get());
		for (int i = 0; i < inv.getContainerSize(); i++) {
			if (inv.getItem(i).getItem() == giantLogItem) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Also check if we need to transform 64 cobbles into a giant cobble
	 */
	public static class ManipulateDrops extends LootModifier {

		protected ManipulateDrops(LootItemCondition[] conditionsIn) {
			super(conditionsIn);
		}

		@Nonnull
		@Override
		protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
			List<ItemStack> newLoot = new ArrayList<>();
			boolean flag = false;
			if (shouldMakeGiantCobble && generatedLoot.size() > 0) {
				// turn the next 64 cobblestone drops into one giant cobble
				if (generatedLoot.get(0).getItem() == Item.byBlock(Blocks.COBBLESTONE)) {
					generatedLoot.remove(0);
					if (amountOfCobbleToReplace == 64) {
						newLoot.add(new ItemStack(TFBlocks.GIANT_COBBLESTONE.get()));
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
		public ManipulateDrops read(ResourceLocation name, JsonObject json, LootItemCondition[] conditionsIn) {
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
		String damageType = damageSource.getMsgId();
		Entity trueSource = damageSource.getEntity();

		// fire aura
		if (living instanceof Player && (damageType.equals("mob") || damageType.equals("player")) && trueSource != null) {
			Player player = (Player) living;
			int fireLevel = TFEnchantment.getFieryAuraLevel(player.getInventory(), damageSource);

			if (fireLevel > 0 && player.getRandom().nextInt(25) < fireLevel) {
				trueSource.setSecondsOnFire(fireLevel / 2);
			}
		}

		// chill aura
		if (living instanceof Player && (damageType.equals("mob") || damageType.equals("player")) && trueSource instanceof LivingEntity) {
			Player player = (Player) living;
			int chillLevel = TFEnchantment.getChillAuraLevel(player.getInventory(), damageSource);

			if (chillLevel > 0) {
				((LivingEntity) trueSource).addEffect(new MobEffectInstance(TFPotions.frosty.get(), chillLevel * 5 + 5, chillLevel));
			}
		}

		// triple bow strips hurtResistantTime
		if (damageType.equals("arrow") && trueSource instanceof Player) {
			Player player = (Player) trueSource;

			if (player.getMainHandItem().getItem() == TFItems.TRIPLE_BOW.get() || player.getOffhandItem().getItem() == TFItems.TRIPLE_BOW.get()) {
				living.invulnerableTime = 0;
			}
		}

		// lets not make the player take suffocation damage if riding something
		if (living instanceof Player && isRidingUnfriendly(living) && damageSource == DamageSource.IN_WALL) {
			event.setCanceled(true);
		}
	}

	//I wanted to make sure absolutely nothing broke, so I also check against the namespaces of the item to make sure theyre vanilla.
	//Worst case some stupid mod adds their own stuff to the minecraft namespace and breaks this, then you can disable this via config.
	@SubscribeEvent
	public static void createSkullCandle(PlayerInteractEvent.RightClickBlock event) {
		ItemStack stack = event.getItemStack();
		Level world = event.getWorld();
		BlockPos pos = event.getPos();
		BlockState state = world.getBlockState(pos);
		if(!TFConfig.COMMON_CONFIG.disableSkullCandles.get()) {
			if (stack.is(ItemTags.CANDLES) && stack.getItem().getRegistryName().getNamespace().equals("minecraft") && !event.getPlayer().isShiftKeyDown()) {
				if (state.getBlock() instanceof AbstractSkullBlock && state.getBlock().getRegistryName().getNamespace().equals("minecraft")) {
					SkullBlock.Types type = (SkullBlock.Types) ((AbstractSkullBlock) state.getBlock()).getType();
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
						default -> {
							return;
						}
					}
					if(!event.getPlayer().getAbilities().instabuild) stack.shrink(1);
					event.getPlayer().swing(event.getHand());
					if(event.getPlayer() instanceof ServerPlayer) event.getPlayer().awardStat(TFStats.SKULL_CANDLES_MADE);
					//this is to prevent anything from being placed afterwords
					event.setCanceled(true);
				}
			}
		}
	}

	private static void makeFloorSkull(PlayerInteractEvent.RightClickBlock event, Block newBlock) {
		GameProfile profile = null;
		if(event.getWorld().getBlockEntity(event.getPos()) instanceof SkullBlockEntity skull) profile = skull.getOwnerProfile();
		event.getWorld().playSound(null, event.getPos(), SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
		event.getWorld().setBlockAndUpdate(event.getPos(), newBlock.defaultBlockState()
				.setValue(AbstractSkullCandleBlock.LIGHTING, AbstractLightableBlock.Lighting.NONE)
				.setValue(SkullCandleBlock.ROTATION, event.getWorld().getBlockState(event.getPos()).getValue(SkullBlock.ROTATION)));
		event.getWorld().setBlockEntity(new SkullCandleBlockEntity(event.getPos(),
				newBlock.defaultBlockState()
						.setValue(AbstractSkullCandleBlock.LIGHTING, AbstractLightableBlock.Lighting.NONE)
						.setValue(SkullCandleBlock.ROTATION, event.getWorld().getBlockState(event.getPos()).getValue(SkullBlock.ROTATION)),
				AbstractSkullCandleBlock.candleToCandleColor(event.getItemStack().getItem()).getValue(), 1));
		if(event.getWorld().getBlockEntity(event.getPos()) instanceof SkullCandleBlockEntity sc) sc.setOwner(profile);
	}

	private static void makeWallSkull(PlayerInteractEvent.RightClickBlock event, Block newBlock) {
		GameProfile profile = null;
		if(event.getWorld().getBlockEntity(event.getPos()) instanceof SkullBlockEntity skull) profile = skull.getOwnerProfile();
		event.getWorld().playSound(null, event.getPos(), SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
		event.getWorld().setBlockAndUpdate(event.getPos(), newBlock.defaultBlockState()
				.setValue(AbstractSkullCandleBlock.LIGHTING, AbstractLightableBlock.Lighting.NONE)
				.setValue(WallSkullCandleBlock.FACING, event.getWorld().getBlockState(event.getPos()).getValue(WallSkullBlock.FACING)));
		event.getWorld().setBlockEntity(new SkullCandleBlockEntity(event.getPos(),
				newBlock.defaultBlockState()
						.setValue(AbstractSkullCandleBlock.LIGHTING, AbstractLightableBlock.Lighting.NONE)
						.setValue(WallSkullCandleBlock.FACING, event.getWorld().getBlockState(event.getPos()).getValue(WallSkullBlock.FACING)),
				AbstractSkullCandleBlock.candleToCandleColor(event.getItemStack().getItem()).getValue(), 1));
		if(event.getWorld().getBlockEntity(event.getPos()) instanceof SkullCandleBlockEntity sc) sc.setOwner(profile);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	// For when the player dies
	public static void applyDeathItems(LivingDeathEvent event) {
		LivingEntity living = event.getEntityLiving();

		if (living.level.isClientSide || !(living instanceof Player player) || living instanceof FakePlayer ||
				player.isCreative()) return;

		if (charmOfLife(player)) {
			event.setCanceled(true); // Executes if the player had charms
		} else if (!living.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
			// Did the player recover? No? Let's give them their stuff based on the keeping charms
			charmOfKeeping(player);

			// Then let's store the rest of their stuff in the casket
			keepsakeCasket(player);
		}
	}

	private static boolean casketExpiration = false;
	private static void keepsakeCasket(Player player) {
		boolean casketConsumed = TFItemStackUtils.consumeInventoryItem(player, TFBlocks.KEEPSAKE_CASKET.get().asItem());

		if (casketConsumed) {
			Level world = player.getCommandSenderWorld();
			BlockPos.MutableBlockPos pos = player.blockPosition().mutable();

			if (pos.getY() < 2) {
				pos.setY(2);
			} else {
				int logicalHeight = player.getCommandSenderWorld().dimensionType().logicalHeight();

				if (pos.getY() > logicalHeight) {
					pos.setY(logicalHeight - 1);
				}
			}

			// TODO determine if block was air or better yet make a tag list of blocks that are OK to place the casket in
			BlockPos immutablePos = pos.immutable();
			FluidState fluidState = world.getFluidState(immutablePos);

			if (world.setBlockAndUpdate(immutablePos, TFBlocks.KEEPSAKE_CASKET.get().defaultBlockState().setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.getFromFluid(fluidState.getType())).setValue(KeepsakeCasketBlock.BREAKAGE, TFItemStackUtils.damage))) {
				BlockEntity te = world.getBlockEntity(immutablePos);

				if (te instanceof KeepsakeCasketBlockEntity) {
					KeepsakeCasketBlockEntity casket = (KeepsakeCasketBlockEntity) te;

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
					casket.setCustomName(new TextComponent(modifiedName + "'s " + (world.random.nextInt(10000) == 0 ? "Costco Casket" : casket.getDisplayName().getString())));
					int damage = world.getBlockState(immutablePos).getValue(KeepsakeCasketBlock.BREAKAGE);
					if (world.random.nextFloat() <= 0.15F) {
						if (damage >= 2) {
							player.getInventory().dropAll();
							world.setBlockAndUpdate(immutablePos, Blocks.AIR.defaultBlockState());
							casketExpiration = true;
							TwilightForestMod.LOGGER.debug("{}'s Casket damage value was too high, alerting the player and dropping extra items", player.getName().getString());
						} else {
							damage = damage + 1;
							world.setBlockAndUpdate(immutablePos, TFBlocks.KEEPSAKE_CASKET.get().defaultBlockState().setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.getFromFluid(fluidState.getType())).setValue(KeepsakeCasketBlock.BREAKAGE, damage));
							TwilightForestMod.LOGGER.debug("{}'s Casket was randomly damaged, applying new damage", player.getName().getString());
						}
					}
					int casketCapacity = casket.getContainerSize();
					List<ItemStack> list = new ArrayList<>(casketCapacity);
					NonNullList<ItemStack> filler = NonNullList.withSize(4, ItemStack.EMPTY);

					// lets add our inventory exactly how it was on us
					list.addAll(TFItemStackUtils.sortArmorForCasket(player));
					player.getInventory().armor.clear();
					list.addAll(filler);
					list.addAll(player.getInventory().offhand);
					player.getInventory().offhand.clear();
					list.addAll(TFItemStackUtils.sortInvForCasket(player));
					player.getInventory().items.clear();

					casket.setItems(NonNullList.of(ItemStack.EMPTY, list.toArray(new ItemStack[casketCapacity])));
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
		Player player = event.getPlayer();
		BlockEntity te = event.getWorld().getBlockEntity(event.getPos());
		UUID checker;
		if(block == TFBlocks.KEEPSAKE_CASKET.get()) {
			if(te instanceof KeepsakeCasketBlockEntity) {
				KeepsakeCasketBlockEntity casket = (KeepsakeCasketBlockEntity) te;
				 checker = casket.playeruuid;
			} else checker = null;
			if(checker != null) {
				if (!((KeepsakeCasketBlockEntity) te).isEmpty()) {
					if(!player.hasPermissions(3) || !player.getGameProfile().getId().equals(checker)) {
						event.setCanceled(true);
					}
				}
			}
		}
	}

	private static boolean charmOfLife(Player player) {
		boolean charm2 = TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_LIFE_2.get());
		boolean charm1 = !charm2 && TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_LIFE_1.get());

		if (charm2 || charm1) {
			if (charm1) {
				player.setHealth(8);
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
			}

			if (charm2) {
				player.setHealth((float) player.getAttribute(Attributes.MAX_HEALTH).getBaseValue()); //Max Health

				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 3));
				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 0));
				player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 0));
			}

			// spawn effect thingers
			CharmEffect effect = new CharmEffect(TFEntities.CHARM_EFFECT, player.level, player, charm1 ? TFItems.CHARM_OF_LIFE_1.get() : TFItems.CHARM_OF_LIFE_2.get());
			player.level.addFreshEntity(effect);

			CharmEffect effect2 = new CharmEffect(TFEntities.CHARM_EFFECT, player.level, player, charm1 ? TFItems.CHARM_OF_LIFE_1.get() : TFItems.CHARM_OF_LIFE_2.get());
			effect2.offset = (float) Math.PI;
			player.level.addFreshEntity(effect2);

			player.level.playSound(null, player.getX(), player.getY(), player.getZ(), TFSounds.CHARM_LIFE, player.getSoundSource(), 1, 1);

			if(player instanceof ServerPlayer) player.awardStat(TFStats.LIFE_CHARMS_ACTIVATED);

			return true;
		}

		return false;
	}

	private static void charmOfKeeping(Player player) {
		// drop any existing held items, just in case
		dropStoredItems(player);

		// TODO also consider situations where the actual slots may be empty, and charm gets consumed anyway. Usually won't happen.
		boolean tier3 = TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_KEEPING_3.get());
		boolean tier2 = tier3 || TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_KEEPING_2.get());
		boolean tier1 = tier2 || TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_KEEPING_1.get());

		Inventory keepInventory = new Inventory(null);

		UUID playerUUID = player.getUUID();


		if (tier1) {
			keepAllArmor(player, keepInventory);
			keepOffHand(player, keepInventory);
		}

		if (tier3) {
			for (int i = 0; i < player.getInventory().items.size(); i++) {
				keepInventory.items.set(i, player.getInventory().items.get(i).copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
			keepInventory.setPickedItem(new ItemStack(TFItems.CHARM_OF_KEEPING_3.get()));

		} else if (tier2) {
			for (int i = 0; i < 9; i++) {
				keepInventory.items.set(i, player.getInventory().items.get(i).copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
			keepInventory.setPickedItem(new ItemStack(TFItems.CHARM_OF_KEEPING_2.get()));

		} else if (tier1) {
			int i = player.getInventory().selected;
			if (Inventory.isHotbarSlot(i)) {
				keepInventory.items.set(i, player.getInventory().items.get(i).copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
			keepInventory.setPickedItem(new ItemStack(TFItems.CHARM_OF_KEEPING_1.get()));
		}

		//TODO: Baubles is dead, replace with curios
		/*if (tier1 && TFCompat.BAUBLES.isActivated()) {
			playerKeepsMapBaubles.put(playerUUID, Baubles.keepBaubles(player));
		}*/

		// always keep tower keys and held phantom armor
		for (int i = 0; i < player.getInventory().items.size(); i++) {
			ItemStack stack = player.getInventory().items.get(i);
			if (stack.getItem() == TFItems.TOWER_KEY.get()) {
				keepInventory.items.set(i, stack.copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
			if (stack.getItem() instanceof PhantomArmorItem) {
				keepInventory.items.set(i, stack.copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
		}

		// Keep phantom equipment
		for (int i = 0; i < player.getInventory().armor.size(); i++) {
			ItemStack armor = player.getInventory().armor.get(i);
			if (armor.getItem() instanceof PhantomArmorItem) {
				keepInventory.armor.set(i, armor.copy());
				player.getInventory().armor.set(i, ItemStack.EMPTY);
			}
		}

		playerKeepsMap.put(playerUUID, keepInventory);
	}

	/**
	 * Move the full armor inventory to the keep pile
	 */
	private static void keepAllArmor(Player player, Inventory keepInventory) {
		for (int i = 0; i < player.getInventory().armor.size(); i++) {
			keepInventory.armor.set(i, player.getInventory().armor.get(i).copy());
			player.getInventory().armor.set(i, ItemStack.EMPTY);
		}
	}

	private static void keepOffHand(Player player, Inventory keepInventory) {
		for (int i = 0; i < player.getInventory().offhand.size(); i++) {
			keepInventory.offhand.set(i, player.getInventory().offhand.get(i).copy());
			player.getInventory().offhand.set(i, ItemStack.EMPTY);
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (event.isEndConquered()) {
			updateCapabilities((ServerPlayer) event.getPlayer(), event.getPlayer());
		} else {
			if(casketExpiration) {
				event.getPlayer().sendMessage(new TranslatableComponent("block.twilightforest.casket.broken").withStyle(ChatFormatting.DARK_RED), event.getPlayer().getUUID());
			}
			returnStoredItems(event.getPlayer());
		}
	}

	/**
	 * Maybe we kept some stuff for the player!
	 */
	private static void returnStoredItems(Player player) {
		Inventory keepInventory = playerKeepsMap.remove(player.getUUID());
		if (keepInventory != null) {
			TwilightForestMod.LOGGER.debug("Player {} ({}) respawned and received items held in storage", player.getName().getString(), player.getUUID());

			NonNullList<ItemStack> displaced = NonNullList.create();

			for (int i = 0; i < player.getInventory().armor.size(); i++) {
				ItemStack kept = keepInventory.armor.get(i);
				if (!kept.isEmpty()) {
					ItemStack existing = player.getInventory().armor.set(i, kept);
					if (!existing.isEmpty()) {
						displaced.add(existing);
					}
				}
			}
			for (int i = 0; i < player.getInventory().offhand.size(); i++) {
				ItemStack kept = keepInventory.offhand.get(i);
				if (!kept.isEmpty()) {
					ItemStack existing = player.getInventory().offhand.set(i, kept);
					if (!existing.isEmpty()) {
						displaced.add(existing);
					}
				}
			}
			for (int i = 0; i < player.getInventory().items.size(); i++) {
				ItemStack kept = keepInventory.items.get(i);
				if (!kept.isEmpty()) {
					ItemStack existing = player.getInventory().items.set(i, kept);
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
			if (!keepInventory.getSelected().isEmpty()) {
				CharmEffect effect = new CharmEffect(TFEntities.CHARM_EFFECT, player.level, player, keepInventory.getSelected().getItem());
				player.level.addFreshEntity(effect);

				CharmEffect effect2 = new CharmEffect(TFEntities.CHARM_EFFECT, player.level, player, keepInventory.getSelected().getItem());
				effect2.offset = (float) Math.PI;
				player.level.addFreshEntity(effect2);

				player.level.playSound(null, player.getX(), player.getY(), player.getZ(), TFSounds.CHARM_KEEP, player.getSoundSource(), 1.5F, 1.0F);
				keepInventory.getSelected().shrink(1);

				if(player instanceof ServerPlayer) player.awardStat(TFStats.KEEPING_CHARMS_ACTIVATED);
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

	private static void dropStoredItems(Player player) {
		Inventory keepInventory = playerKeepsMap.remove(player.getUUID());
		if (keepInventory != null) {
			TwilightForestMod.LOGGER.warn("Dropping inventory items previously held in reserve for player {} ({})", player.getName().getString(), player.getUUID());
			keepInventory.player = player;
			keepInventory.dropAll();
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
		//FIXME keep an eye on https://github.com/MinecraftForge/MinecraftForge/pull/8114, switch to the MountEvent to properly fix dismounting
		if (living instanceof Player && living.isShiftKeyDown() && isRidingUnfriendly(living)) {
			living.setShiftKeyDown(false);
		}
	}

	public static boolean isRidingUnfriendly(LivingEntity entity) {
		return entity.isPassenger() && entity.getVehicle() instanceof IHostileMount;
	}

	/**
	 * Check if the player is trying to break a block in a structure that's considered unbreakable for progression reasons
	 * Also check for breaking blocks with the giant's pickaxe and maybe break nearby blocks
	 */
	@SubscribeEvent
	public static void breakBlock(BreakEvent event) {
		Player player = event.getPlayer();
		BlockPos pos = event.getPos();
		BlockState state = event.getState();

		if (!(event.getWorld() instanceof Level world) || ((Level) event.getWorld()).isClientSide) return;

		if (isBlockProtectedFromBreaking(world, pos) && isAreaProtected(world, player, pos)) {
			event.setCanceled(true);

		} else if (!isBreakingWithGiantPick && canHarvestWithGiantPick(player, state)) {

			isBreakingWithGiantPick = true;

			// check nearby blocks for same block or same drop

			// pre-check for cobble!
			Item cobbleItem = Blocks.COBBLESTONE.asItem();
			boolean allCobble = state.getBlock().asItem() == cobbleItem;

			if (allCobble) {
				for (BlockPos dPos : GiantBlock.getVolume(pos)) {
					if (dPos.equals(pos))
						continue;
					BlockState stateThere = world.getBlockState(dPos);
					if (stateThere.getBlock().asItem() != cobbleItem) {
						allCobble = false;
						break;
					}
				}
			}

			if (allCobble && !player.getAbilities().instabuild) {
				shouldMakeGiantCobble = true;
				amountOfCobbleToReplace = 64;
			} else {
				shouldMakeGiantCobble = false;
				amountOfCobbleToReplace = 0;
			}

			// break all nearby blocks
			if (player instanceof ServerPlayer playerMP) {
				for (BlockPos dPos : GiantBlock.getVolume(pos)) {
					if (!dPos.equals(pos) && state.getBlock() == world.getBlockState(dPos).getBlock()) {
						// try to break that block too!
						playerMP.gameMode.destroyBlock(dPos);
					}
				}
			}

			isBreakingWithGiantPick = false;
		}
	}

	private static boolean canHarvestWithGiantPick(Player player, BlockState state) {
		ItemStack heldStack = player.getMainHandItem();
		Item heldItem = heldStack.getItem();
		return heldItem == TFItems.GIANT_PICKAXE.get() && heldItem.isCorrectToolForDrops(state);
	}

	@SubscribeEvent
	public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {

		Player player = event.getPlayer();
		Level world = player.level;

		if (!world.isClientSide && isBlockProtectedFromInteraction(world, event.getPos()) && isAreaProtected(world, player, event.getPos())) {
			event.setUseBlock(Event.Result.DENY);
		}
	}

	/**
	 * Stop the player from interacting with blocks that could produce treasure or open doors in a protected area
	 */
	private static boolean isBlockProtectedFromInteraction(Level world, BlockPos pos) {
		return world.getBlockState(pos).is(BlockTagGenerator.STRUCTURE_BANNED_INTERACTIONS);
	}

	private static boolean isBlockProtectedFromBreaking(Level world, BlockPos pos) {
		// todo improve
		return !world.getBlockState(pos).getBlock().getRegistryName().getPath().contains("grave") || !world.getBlockState(pos).is(TFBlocks.KEEPSAKE_CASKET.get());
	}

	/**
	 * Return if the area at the coordinates is considered protected for that player.
	 * Currently, if we return true, we also send the area protection packet here.
	 */
	private static boolean isAreaProtected(Level world, Player player, BlockPos pos) {

		if (player.getAbilities().instabuild || !TFGenerationSettings.isProgressionEnforced(world) || player instanceof FakePlayer) {
			return false;
		}

		ChunkGeneratorTwilight chunkGenerator = WorldUtil.getChunkGenerator(world);


		if (chunkGenerator != null) {
			Optional<StructureStart<?>> struct = TFGenerationSettings.locateTFStructureInRange((ServerLevel) world, pos, 0);
			if(struct.isPresent()) {
				StructureStart<?> structure = struct.get();
				if(structure.getBoundingBox().isInside(pos)) {
					// what feature is nearby?  is it one the player has not unlocked?
					TFFeature nearbyFeature = TFFeature.getFeatureAt(pos.getX(), pos.getZ(), (ServerLevel) world);

					if (!nearbyFeature.doesPlayerHaveRequiredAdvancements(player)/* && chunkGenerator.isBlockProtected(pos)*/) {

						// TODO: This is terrible but *works* for now.. proper solution is to figure out why the stronghold bounding box is going so high
						if (nearbyFeature == TFFeature.KNIGHT_STRONGHOLD && pos.getY() >= TFGenerationSettings.SEALEVEL)
							return false;

						// send protection packet
						BoundingBox bb = structure.getBoundingBox();//new MutableBoundingBox(pos, pos.add(16, 16, 16)); // todo 1.15 get from structure
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

	private static void sendAreaProtectionPacket(Level world, BlockPos pos, BoundingBox sbb) {
		PacketDistributor.TargetPoint targetPoint = new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 64, world.dimension());
		TFPacketHandler.CHANNEL.send(PacketDistributor.NEAR.with(() -> targetPoint), new AreaProtectionPacket(sbb, pos));
	}

	@SubscribeEvent
	public static void livingAttack(LivingAttackEvent event) {
		LivingEntity living = event.getEntityLiving();
		// cancel attacks in protected areas
		if (!living.level.isClientSide && living instanceof Enemy && event.getSource().getEntity() instanceof Player && !(living instanceof Kobold)
				&& isAreaProtected(living.level, (Player) event.getSource().getEntity(), new BlockPos(living.blockPosition()))) {

			event.setCanceled(true);
			return;
		}
		// shields
		if (!living.level.isClientSide && !SHIELD_DAMAGE_BLACKLIST.contains(event.getSource().msgId)) {
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
		if (!event.getPlayer().level.isClientSide && event.getPlayer() instanceof ServerPlayer) {
			sendEnforcedProgressionStatus((ServerPlayer) event.getPlayer(), TFGenerationSettings.isProgressionEnforced(event.getPlayer().level));
			updateCapabilities((ServerPlayer) event.getPlayer(), event.getPlayer());
			banishNewbieToTwilightZone(event.getPlayer());
		}
	}

	/**
	 * When player changes dimensions, send the rule status if they're moving to the Twilight Forest
	 */
	@SubscribeEvent
	public static void playerPortals(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (!event.getPlayer().level.isClientSide && event.getPlayer() instanceof ServerPlayer) {
			if (event.getTo().location().toString().equals(TFConfig.COMMON_CONFIG.DIMENSION.portalDestinationID.get())) {
				sendEnforcedProgressionStatus((ServerPlayer) event.getPlayer(), TFGenerationSettings.isProgressionEnforced(event.getPlayer().level));
			}

			updateCapabilities((ServerPlayer) event.getPlayer(), event.getPlayer());
		}
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		updateCapabilities((ServerPlayer) event.getPlayer(), event.getTarget());
	}

	// send any capabilities that are needed client-side
	private static void updateCapabilities(ServerPlayer player, Entity entity) {
		entity.getCapability(CapabilityList.SHIELDS).ifPresent(cap -> {
			if (cap.shieldsLeft() > 0) {
				TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new UpdateShieldPacket(entity, cap));
			}
		});
	}

	private static void sendEnforcedProgressionStatus(ServerPlayer player, boolean isEnforced) {
		TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new EnforceProgressionStatusPacket(isEnforced));
	}

	// Teleport first-time players to Twilight Forest

	private static final String NBT_TAG_TWILIGHT = "twilightforest_banished";

	private static void banishNewbieToTwilightZone(Player player) {
		CompoundTag tagCompound = player.getPersistentData();
		CompoundTag playerData = tagCompound.getCompound(Player.PERSISTED_NBT_TAG);

		// getBoolean returns false, if false or didn't exist
		boolean shouldBanishPlayer = TFConfig.COMMON_CONFIG.DIMENSION.newPlayersSpawnInTF.get() && !playerData.getBoolean(NBT_TAG_TWILIGHT);

		playerData.putBoolean(NBT_TAG_TWILIGHT, true); // set true once player has spawned either way
		tagCompound.put(Player.PERSISTED_NBT_TAG, playerData); // commit

		if (shouldBanishPlayer) TFPortalBlock.attemptSendEntity(player, true, TFConfig.COMMON_CONFIG.DIMENSION.portalForNewPlayerSpawn.get()); // See ya hate to be ya
	}

	// Advancement Trigger
	@SubscribeEvent
	public static void onAdvancementGet(AdvancementEvent event) {
		Player player = event.getPlayer();
		if (player instanceof ServerPlayer) {
			TFAdvancements.ADVANCEMENT_UNLOCKED.trigger((ServerPlayer) player, event.getAdvancement());
		}
	}

	@SubscribeEvent
	public static void armorChanged(LivingEquipmentChangeEvent event) {
		LivingEntity living = event.getEntityLiving();
		if (!living.level.isClientSide && living instanceof ServerPlayer) {
			TFAdvancements.ARMOR_CHANGED.trigger((ServerPlayer) living, event.getFrom(), event.getTo());
		}
	}

	// Parrying

	private static boolean globalParry = !ModList.get().isLoaded("parry");

	/*@SubscribeEvent
	public static void arrowParry(ProjectileImpactEvent<AbstractArrow> event) {
		final AbstractArrow projectile = event.getProjectile();

		if (!projectile.getCommandSenderWorld().isClientSide && globalParry &&
				(TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get()
						|| projectile instanceof ITFProjectile)) {

			if (event.getRayTraceResult() instanceof EntityHitResult) {
				Entity entity = ((EntityHitResult) event.getRayTraceResult()).getEntity();

				if (event.getEntity() != null && entity instanceof LivingEntity) {
					LivingEntity entityBlocking = (LivingEntity) entity;

					if (entityBlocking.isDamageSourceBlocked(new DamageSource("parry_this") {
						@Override
						public Vec3 getSourcePosition() {
							return projectile.position();
						}
					}) && (entityBlocking.getUseItem().getItem().getUseDuration(entityBlocking.getUseItem()) - entityBlocking.getUseItemRemainingTicks()) <= TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.shieldParryTicksArrow.get()) {
						Vec3 playerVec3 = entityBlocking.getLookAngle();

						projectile.shoot(playerVec3.x, playerVec3.y, playerVec3.z, 1.1F, 0.1F);  // reflect faster and more accurately

						projectile.setOwner(entityBlocking); //TODO: Verify

						event.setCanceled(true);
					}
				}
			}
		}
	}*/

	/*@SubscribeEvent
	public static void fireballParry(ProjectileImpactEvent<Fireball> event) {
		final AbstractHurtingProjectile projectile = event.getProjectile();

		if (!projectile.getCommandSenderWorld().isClientSide && globalParry &&
				(TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get()
						|| projectile instanceof ITFProjectile)) {

			if (event.getRayTraceResult() instanceof EntityHitResult) {
				Entity entity = ((EntityHitResult) event.getRayTraceResult()).getEntity();

				if (event.getEntity() != null && entity instanceof LivingEntity) {
					LivingEntity entityBlocking = (LivingEntity) entity;

					if (entityBlocking.isDamageSourceBlocked(new DamageSource("parry_this") {
						@Override
						public Vec3 getSourcePosition() {
							return projectile.position();
						}
					}) && (entityBlocking.getUseItem().getItem().getUseDuration(entityBlocking.getUseItem()) - entityBlocking.getUseItemRemainingTicks()) <= TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.shieldParryTicksFireball.get()) {
						Vec3 playerVec3 = entityBlocking.getLookAngle();

						projectile.setDeltaMovement(new Vec3(playerVec3.x, playerVec3.y, playerVec3.z));
						projectile.xPower = projectile.getDeltaMovement().x() * 0.1D;
						projectile.yPower = projectile.getDeltaMovement().y() * 0.1D;
						projectile.zPower = projectile.getDeltaMovement().z() * 0.1D;

						projectile.setOwner(entityBlocking); //TODO: Verify

						event.setCanceled(true);
					}
				}
			}
		}
	}*/

	@SubscribeEvent
	public static void throwableParry(ProjectileImpactEvent event) {
		final Projectile projectile = event.getProjectile();

		if (!projectile.getCommandSenderWorld().isClientSide && globalParry &&
				(TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get()
						|| projectile instanceof ITFProjectile)) {

			if (event.getRayTraceResult() instanceof EntityHitResult) {
				Entity entity = ((EntityHitResult) event.getRayTraceResult()).getEntity();


				if (event.getEntity() != null && entity instanceof LivingEntity entityBlocking) {
					if (entityBlocking.isDamageSourceBlocked(new DamageSource("parry_this") {
						@Override
						public Vec3 getSourcePosition() {
							return projectile.position();
						}
					}) && (entityBlocking.getUseItem().getItem().getUseDuration(entityBlocking.getUseItem()) - entityBlocking.getUseItemRemainingTicks()) <= TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.shieldParryTicksThrowable.get()) {
						Vec3 playerVec3 = entityBlocking.getLookAngle();

						projectile.shoot(playerVec3.x, playerVec3.y, playerVec3.z, 1.1F, 0.1F);  // reflect faster and more accurately

						projectile.setOwner(entityBlocking); //TODO: Verify

						event.setCanceled(true);
					}
				}
			}
		}
	}
}
