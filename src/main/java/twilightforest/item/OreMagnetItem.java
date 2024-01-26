package twilightforest.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;
import twilightforest.network.ParticlePacket;
import twilightforest.util.VoxelBresenhamIterator;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class OreMagnetItem extends Item {

	private static final float WIGGLE = 10F;

	public OreMagnetItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		AtomicBoolean badEnchant = new AtomicBoolean();
		EnchantmentHelper.getEnchantments(book).forEach((enchantment, integer) -> {
			if (!Objects.equals(Enchantments.UNBREAKING, enchantment)) {
				badEnchant.set(true);
			}
		});

		return !badEnchant.get() && super.isBookEnchantable(stack, book);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {
		player.startUsingItem(hand);
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int useRemaining) {
		int useTime = this.getUseDuration(stack) - useRemaining;

		if (!level.isClientSide() && useTime > 10) {
			int moved = this.doMagnet(level, living, 0, 0);

			if (moved == 0) {
				moved = this.doMagnet(level, living, WIGGLE, 0);
			}
			if (moved == 0) {
				moved = this.doMagnet(level, living, WIGGLE, WIGGLE);
			}
			if (moved == 0) {
				moved = this.doMagnet(level, living, 0, WIGGLE);
			}
			if (moved == 0) {
				moved = this.doMagnet(level, living, -WIGGLE, WIGGLE);
			}
			if (moved == 0) {
				moved = this.doMagnet(level, living, -WIGGLE, 0);
			}
			if (moved == 0) {
				moved = this.doMagnet(level, living, -WIGGLE, -WIGGLE);
			}
			if (moved == 0) {
				moved = this.doMagnet(level, living, 0, -WIGGLE);
			}
			if (moved == 0) {
				moved = this.doMagnet(level, living, WIGGLE, -WIGGLE);
			}

			if (moved > 0) {
				stack.hurtAndBreak(moved, living, user -> user.broadcastBreakEvent(living.getUsedItemHand()));
				level.playSound(null, living.getX(), living.getY(), living.getZ(), TFSounds.MAGNET_GRAB.get(), living.getSoundSource(), 1.0F, 1.0F);
			}
		}
	}

	@Nonnull
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	/**
	 * Magnet from the player's position and facing to the specified offset
	 */
	private int doMagnet(Level level, LivingEntity living, float yawOffset, float pitchOffset) {
		// find vector 32 blocks from look
		double range = 32.0D;
		Vec3 srcVec = new Vec3(living.getX(), living.getY() + living.getEyeHeight(), living.getZ());
		Vec3 lookVec = getOffsetLook(living, yawOffset, pitchOffset);
		Vec3 destVec = srcVec.add(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range);

		return doMagnet(level, BlockPos.containing(srcVec), BlockPos.containing(destVec), false);
	}

	public static int doMagnet(Level level, BlockPos usePos, BlockPos destPos, boolean sourceIsMineCore) {
		// FIXME Find a better place to invoke this!
		initOre2BlockMap();

		int blocksMoved = 0;

		// find some ore?
		BlockState attactedOreBlock = Blocks.AIR.defaultBlockState();
		BlockState replacementBlock = Blocks.AIR.defaultBlockState();
		BlockPos foundPos = null;
		BlockPos basePos = null;

		for (BlockPos coord : new VoxelBresenhamIterator(usePos, destPos)) {
			BlockState searchState = level.getBlockState(coord);

			// keep track of where the dirt/stone we first find is.
			if (basePos == null) {
				if (isReplaceable(searchState)) {
					basePos = coord;
				}
				// This ordering is so that the base pos is found first before we pull ores - pushing ores away is a baaaaad idea!
			} else if (foundPos == null && searchState.getBlock() != Blocks.AIR && isOre(searchState.getBlock()) && level.getBlockEntity(coord) == null) {
				attactedOreBlock = searchState;
				replacementBlock = ORE_TO_BLOCK_REPLACEMENTS.getOrDefault(attactedOreBlock.getBlock(), Blocks.STONE).defaultBlockState();
				foundPos = coord;
			}
		}

		if (basePos != null && foundPos != null && attactedOreBlock.getBlock() != Blocks.AIR) {
			// find the whole vein
			Set<BlockPos> veinBlocks = new HashSet<>();
			findVein(level, foundPos, attactedOreBlock, veinBlocks);

			// move it up into minable blocks or dirt
			int offX = basePos.getX() - foundPos.getX();
			int offY = basePos.getY() - foundPos.getY();
			int offZ = basePos.getZ() - foundPos.getZ();

			for (BlockPos coord : veinBlocks) {
				BlockPos replacePos = coord.offset(offX, offY, offZ);
				BlockState replaceState = level.getBlockState(replacePos);

				if (isReplaceable(replaceState) || replaceState.canBeReplaced() || replaceState.isAir()) {
					level.setBlock(coord, replacementBlock, 2);

					if (sourceIsMineCore && level instanceof ServerLevel serverLevel) {
						Vec3 xyz = Vec3.atCenterOf(replacePos);
						for (ServerPlayer serverplayer : serverLevel.players()) { // This is just particle math, we send a particle packet to every player in range
							if (serverplayer.distanceToSqr(xyz) < 4096.0D) {
								ParticlePacket particlePacket = new ParticlePacket();
								for (int i = 0; i < 16; i++) {
									Vec3 offset = new Vec3((level.random.nextDouble() - 0.5D) * 1.25D, (level.random.nextDouble() - 0.5D) * 1.25D, (level.random.nextDouble() - 0.5D) * 1.25D);
									particlePacket.queueParticle(TFParticleType.LOG_CORE_PARTICLE.get(), false, xyz.add(offset), new Vec3(0.8, 0.9, 0.2));
								}
								PacketDistributor.PLAYER.with(serverplayer).send(particlePacket);
							}
						}
					}

					// set close to ore material
					level.setBlock(replacePos, attactedOreBlock, 2);
					blocksMoved++;
				}
			}
		}

		return blocksMoved;
	}

	/**
	 * Get the player look vector, but offset by the specified parameters.  We use to scan the area around where the player is looking
	 * in the likely case there's no ore in the exact look direction.
	 */
	private Vec3 getOffsetLook(LivingEntity living, float yawOffset, float pitchOffset) {
		float var2 = Mth.cos(-(living.getYRot() + yawOffset) * 0.017453292F - (float) Math.PI);
		float var3 = Mth.sin(-(living.getYRot() + yawOffset) * 0.017453292F - (float) Math.PI);
		float var4 = -Mth.cos(-(living.getXRot() + pitchOffset) * 0.017453292F);
		float var5 = Mth.sin(-(living.getXRot() + pitchOffset) * 0.017453292F);
		return new Vec3(var3 * var4, var5, var2 * var4);
	}

	@Deprecated
	private static boolean isReplaceable(BlockState state) {
		return state.is(BlockTagGenerator.ORE_MAGNET_SAFE_REPLACE_BLOCK);
	}

	private static boolean findVein(Level level, BlockPos here, BlockState oreState, Set<BlockPos> veinBlocks) {
		// is this already on the list?
		if (veinBlocks.contains(here)) {
			return false;
		}

		// let's limit it to 24 blocks at a time
		if (veinBlocks.size() >= 24) {
			return false;
		}

		// otherwise, check if we're still in the vein
		if (level.getBlockState(here) == oreState) {
			veinBlocks.add(here);

			// recurse in 6 directions
			for (Direction e : Direction.values()) {
				findVein(level, here.relative(e), oreState, veinBlocks);
			}

			return true;
		} else {
			return false;
		}
	}

	private static boolean isOre(Block ore) {
		return ORE_TO_BLOCK_REPLACEMENTS.containsKey(ore);
	}

	// So it looks like we can't really access BlockTag contents until Datapack loading is complete (see `buildOreMagnetCache` method below)
	// Instead let's opt for only clearing that particular cache on reload, and lazy-init the map when the magnet is used

	// Switch over to ConcurrentHashMap if we run into any concurrency problems
	private static boolean cacheNeedsBuild = true;
	private static final HashMap<Block, Block> ORE_TO_BLOCK_REPLACEMENTS = new HashMap<>();

	private static void initOre2BlockMap() {
		if (!cacheNeedsBuild)
			return;

		TwilightForestMod.LOGGER.info("GENERATING ORE TO BLOCK MAPPING");

		//collect all tags
		for (TagKey<Block> tag : BuiltInRegistries.BLOCK.getTagNames().filter(location -> location.location().getNamespace().equals("forge")).toList()) {
			//check if the tag is a valid ore tag
			if (tag.location().getPath().contains("ores_in_ground/")) {
				//grab the part after the slash for use later
				String oreground = tag.location().getPath().substring(15);
				//check if a tag for ore grounds matches up with our ores in ground tag
				if (BuiltInRegistries.BLOCK.getTagNames().filter(location -> location.location().getNamespace().equals("forge")).anyMatch(blockTagKey -> blockTagKey.location().getPath().equals("ore_bearing_ground/" + oreground))) {
					//add each ground type to each ore
					BuiltInRegistries.BLOCK.getTag(TagKey.create(Registries.BLOCK, new ResourceLocation("forge", "ore_bearing_ground/" + oreground))).get().forEach(ground ->
							BuiltInRegistries.BLOCK.getTag(tag).get().forEach(ore -> {
								//exclude ignored ores
								if (!ore.value().defaultBlockState().is(BlockTagGenerator.ORE_MAGNET_IGNORE)) {
									ORE_TO_BLOCK_REPLACEMENTS.put(ore.value(), ground.value());
								}
							}));
				}
			}
		}

		cacheNeedsBuild = false;
	}

	@SubscribeEvent
	public static void buildOreMagnetCache(AddReloadListenerEvent event) {
		event.addListener((stage, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> {
			if (!cacheNeedsBuild) {
				ORE_TO_BLOCK_REPLACEMENTS.clear();
				cacheNeedsBuild = true;
			}

			return stage.wait(null).thenRun(() -> {
			}); // Nothing to do here
		});
	}
}