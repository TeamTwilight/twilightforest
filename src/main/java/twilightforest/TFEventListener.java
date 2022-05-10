package twilightforest;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
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
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.*;
import twilightforest.block.entity.KeepsakeCasketBlockEntity;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.data.tags.BlockTagGenerator;
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
import twilightforest.potions.TFMobEffects;
import twilightforest.util.TFItemStackUtils;
import twilightforest.util.TFStats;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.TFGenerationSettings;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * So much of the mod logic in this one class
 */
@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFEventListener {

	public static final String CHARM_INV_TAG = "TFCharmInventory";
	private static final ImmutableSet<String> SHIELD_DAMAGE_BLACKLIST = ImmutableSet.of(
			"inWall", "cramming", "drown", "starve", "fall", "flyIntoWall", "outOfWorld", "fallingBlock"
	);

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
				((LivingEntity) trueSource).addEffect(new MobEffectInstance(TFMobEffects.FROSTY.get(), chillLevel * 5 + 5, chillLevel));
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
					if(event.getPlayer() instanceof ServerPlayer) event.getPlayer().awardStat(TFStats.SKULL_CANDLES_MADE.get());
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

	private static boolean hasCharmCurio(Item item, Player player) {
		if(ModList.get().isLoaded("curios")) {
			ItemStack stack = CuriosApi.getCuriosHelper().findEquippedCurio(item, player).map(ImmutableTriple::getRight).orElse(ItemStack.EMPTY);

			if (!stack.isEmpty()) {
				stack.shrink(1);
				return true;
			}
		}

		return false;
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

				if (te instanceof KeepsakeCasketBlockEntity casket) {

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
				TwilightForestMod.LOGGER.error("Could not place Keepsake Casket at " + pos);
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
			if(te instanceof KeepsakeCasketBlockEntity casket) {
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
		boolean charm2 = TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_LIFE_2.get()) || hasCharmCurio(TFItems.CHARM_OF_LIFE_2.get(), player);
		boolean charm1 = !charm2 && (TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_LIFE_1.get()) || hasCharmCurio(TFItems.CHARM_OF_LIFE_1.get(), player));

		if (charm2 || charm1) {
			if (charm1) {
				player.setHealth(8);
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
			}

			if (charm2) {
				player.setHealth(player.getMaxHealth());

				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 3));
				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 0));
				player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 0));
			}

			// spawn effect thingers
			CharmEffect effect = new CharmEffect(TFEntities.CHARM_EFFECT.get(), player.level, player, charm1 ? TFItems.CHARM_OF_LIFE_1.get() : TFItems.CHARM_OF_LIFE_2.get());
			player.level.addFreshEntity(effect);

			CharmEffect effect2 = new CharmEffect(TFEntities.CHARM_EFFECT.get(), player.level, player, charm1 ? TFItems.CHARM_OF_LIFE_1.get() : TFItems.CHARM_OF_LIFE_2.get());
			effect2.offset = (float) Math.PI;
			player.level.addFreshEntity(effect2);

			player.level.playSound(null, player.getX(), player.getY(), player.getZ(), TFSounds.CHARM_LIFE, player.getSoundSource(), 1, 1);

			if(player instanceof ServerPlayer) player.awardStat(TFStats.LIFE_CHARMS_ACTIVATED.get());

			return true;
		}

		return false;
	}

	public static CompoundTag getPlayerData(Player player) {
		if (!player.getPersistentData().contains(Player.PERSISTED_NBT_TAG)) {
			player.getPersistentData().put(Player.PERSISTED_NBT_TAG, new CompoundTag());
		}
		return player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
	}

	//stores the charm that was used for the effect later
	private static ItemStack charmUsed;
	private static void charmOfKeeping(Player player) {

		//check our inventory for any charms of keeping. We also want to check curio slots (if the mod is installed)
		// TODO also consider situations where the actual slots may be empty, and charm gets consumed anyway. Usually won't happen.
		boolean tier3 = TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_KEEPING_3.get()) || hasCharmCurio(TFItems.CHARM_OF_KEEPING_3.get(), player);
		boolean tier2 = tier3 || TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_KEEPING_2.get()) || hasCharmCurio(TFItems.CHARM_OF_KEEPING_2.get(), player);
		boolean tier1 = tier2 || TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_KEEPING_1.get()) || hasCharmCurio(TFItems.CHARM_OF_KEEPING_1.get(), player);

		//create a fake inventory to organize our kept inventory in
		Inventory keepInventory = new Inventory(null);
		ListTag tagList = new ListTag();

		//if we have any charm of keeping, all armor and offhand items are kept, so add those
		if (tier1) {
			keepWholeList(keepInventory.armor, player.getInventory().armor);
			keepWholeList(keepInventory.offhand, player.getInventory().offhand);
		}

		if (tier3) {
			//tier 3 keeps our entire inventory
			keepWholeList(keepInventory.items, player.getInventory().items);
			charmUsed = new ItemStack(TFItems.CHARM_OF_KEEPING_3.get());
		} else if (tier2) {
			//tier 2 keeps our hotbar only
			for (int i = 0; i < 9; i++) {
				keepInventory.items.set(i, player.getInventory().items.get(i).copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
			charmUsed = new ItemStack(TFItems.CHARM_OF_KEEPING_2.get());
		} else if (tier1) {
			//tier 1 keeps our selected item only
			int i = player.getInventory().selected;
			if (Inventory.isHotbarSlot(i)) {
				keepInventory.items.set(i, player.getInventory().items.get(i).copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
			charmUsed = new ItemStack(TFItems.CHARM_OF_KEEPING_1.get());
		}

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

		//take our fake inventory and save it to the persistent player data.
		//by saving it there we can guarantee we will always get all of our items back, even if the player logs out and back in.
		keepInventory.save(tagList);
		getPlayerData(player).put(CHARM_INV_TAG, tagList);
	}

	//transfers a list of items to another
	private static void keepWholeList(NonNullList<ItemStack> transferTo, NonNullList<ItemStack> transferFrom) {
		for (int i = 0; i < transferFrom.size(); i++) {
			transferTo.set(i, transferFrom.get(i).copy());
		}
		transferFrom.clear();
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (!(event.getPlayer() instanceof ServerPlayer serverPlayer)) return;
		if (event.isEndConquered()) {
			updateCapabilities(serverPlayer, serverPlayer);
		} else {
			if(casketExpiration) {
				serverPlayer.sendMessage(new TranslatableComponent("block.twilightforest.casket.broken").withStyle(ChatFormatting.DARK_RED), serverPlayer.getUUID());
			}
			returnStoredItems(serverPlayer);
		}

		if (TFConfig.COMMON_CONFIG.DIMENSION.newPlayersSpawnInTF.get() && serverPlayer.getRespawnPosition() == null) {
			CompoundTag tagCompound = serverPlayer.getPersistentData();
			CompoundTag playerData = tagCompound.getCompound(Player.PERSISTED_NBT_TAG);
			playerData.putBoolean(NBT_TAG_TWILIGHT, false); // set to false so that the method works
			tagCompound.put(Player.PERSISTED_NBT_TAG, playerData); // commit
			banishNewbieToTwilightZone(serverPlayer);
		}
	}

	/**
	 * Maybe we kept some stuff for the player!
	 */
	private static void returnStoredItems(Player player) {

		TwilightForestMod.LOGGER.debug("Player {} ({}) respawned and received items held in storage", player.getName().getString(), player.getUUID());

		//check if our tag is in the persistent player data. If so, copy that inventory over to our own. Cloud storage at its finest!
		CompoundTag playerData = getPlayerData(player);
		if (!player.level.isClientSide && playerData.contains(CHARM_INV_TAG)) {
			ListTag tagList = playerData.getList(CHARM_INV_TAG, 10);
			player.getInventory().load(tagList);
			getPlayerData(player).getList(CHARM_INV_TAG, 10).clear();
			getPlayerData(player).remove(CHARM_INV_TAG);
		}

		// spawn effect thingers
		if (charmUsed != null) {
			CharmEffect effect = new CharmEffect(TFEntities.CHARM_EFFECT.get(), player.level, player, charmUsed.getItem());
			player.level.addFreshEntity(effect);

			CharmEffect effect2 = new CharmEffect(TFEntities.CHARM_EFFECT.get(), player.level, player, charmUsed.getItem());
			effect2.offset = (float) Math.PI;
			player.level.addFreshEntity(effect2);

			player.level.playSound(null, player.getX(), player.getY(), player.getZ(), TFSounds.CHARM_KEEP, player.getSoundSource(), 1.5F, 1.0F);
			if(player instanceof ServerPlayer) player.awardStat(TFStats.KEEPING_CHARMS_ACTIVATED.get());
			charmUsed = null;
		}

	}

	@SubscribeEvent
	public static void livingUpdate(LivingUpdateEvent event) {
		event.getEntityLiving().getCapability(CapabilityList.SHIELDS).ifPresent(IShieldCapability::update);
		if (event.getEntityLiving() instanceof IHostileMount)
			event.getEntityLiving().getPassengers().forEach(e -> e.setShiftKeyDown(false));
	}

	public static volatile boolean allowDismount = false;

	@SubscribeEvent
	public static void preventMountDismount(EntityMountEvent event) {
		if (!event.getEntityBeingMounted().level.isClientSide() && !event.isMounting() && event.getEntityBeingMounted().isAlive() && event.getEntityMounting() instanceof LivingEntity living && isRidingUnfriendly(living) && !allowDismount)
			event.setCanceled(true);
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
		return heldItem == TFItems.GIANT_PICKAXE.get() && ForgeHooks.isCorrectToolForDrops(state, player);
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
			Optional<StructureStart> struct = TFGenerationSettings.locateTFStructureInRange((ServerLevel) world, pos, 0);
			if(struct.isPresent()) {
				StructureStart structure = struct.get();
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
		if (!event.getPlayer().level.isClientSide && event.getPlayer() instanceof ServerPlayer player) {
			if (TFGenerationSettings.usesTwilightChunkGenerator(player.getLevel())) {
				sendEnforcedProgressionStatus((ServerPlayer) event.getPlayer(), TFGenerationSettings.isProgressionEnforced(player.getLevel()));
			}

			updateCapabilities(player, event.getPlayer());
		}
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		updateCapabilities((ServerPlayer) event.getPlayer(), event.getTarget());
	}

	// send any capabilities that are needed client-side
	private static void updateCapabilities(ServerPlayer clientTarget, Entity shielded) {
		shielded.getCapability(CapabilityList.SHIELDS).ifPresent(cap -> {
			if (cap.shieldsLeft() > 0) {
				TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> clientTarget), new UpdateShieldPacket(shielded, cap));
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

	private static final boolean globalParry = !ModList.get().isLoaded("parry");

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
