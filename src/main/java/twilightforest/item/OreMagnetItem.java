package twilightforest.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.data.BlockTagGenerator;
import twilightforest.util.FeatureLogic;
import twilightforest.util.VoxelBresenhamIterator;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class OreMagnetItem extends Item {

	private static final float WIGGLE = 10F;

	protected OreMagnetItem(Properties props) {
		super(props);
	}

	@Override
	public boolean isEnchantable(ItemStack pStack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		player.startUsingItem(hand);
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
	}

	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity living, int useRemaining) {
		int useTime = this.getUseDuration(stack) - useRemaining;

		if (!world.isClientSide && useTime > 10) {
			int moved = doMagnet(world, living, 0, 0);

			if (moved == 0) {
				moved = doMagnet(world, living, WIGGLE, 0);
			}
			if (moved == 0) {
				moved = doMagnet(world, living, WIGGLE, WIGGLE);
			}
			if (moved == 0) {
				moved = doMagnet(world, living, 0, WIGGLE);
			}
			if (moved == 0) {
				moved = doMagnet(world, living, -WIGGLE, WIGGLE);
			}
			if (moved == 0) {
				moved = doMagnet(world, living, -WIGGLE, 0);
			}
			if (moved == 0) {
				moved = doMagnet(world, living, -WIGGLE, -WIGGLE);
			}
			if (moved == 0) {
				moved = doMagnet(world, living, 0, -WIGGLE);
			}
			if (moved == 0) {
				moved = doMagnet(world, living, WIGGLE, -WIGGLE);
			}

			if (moved > 0) {
				stack.hurtAndBreak(moved, living, user -> user.broadcastBreakEvent(living.getUsedItemHand()));
				world.playSound(null, living.getX(), living.getY(), living.getZ(), TFSounds.MAGNET_GRAB, living.getSoundSource(), 1.0F, 1.0F);
			}
		}
	}

	@Override
	public float getXpRepairRatio(ItemStack stack) {
		return 0.1f;
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
	private int doMagnet(Level world, LivingEntity living, float yawOffset, float pitchOffset) {
		// find vector 32 blocks from look
		double range = 32.0D;
		Vec3 srcVec = new Vec3(living.getX(), living.getY() + living.getEyeHeight(), living.getZ());
		Vec3 lookVec = getOffsetLook(living, yawOffset, pitchOffset);
		Vec3 destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);

		return doMagnet(world, new BlockPos(srcVec), new BlockPos(destVec));
	}

	public static int doMagnet(Level world, BlockPos usePos, BlockPos destPos) {
		// FIXME Find a better place to invoke this!
		initOre2BlockMap();

		int blocksMoved = 0;

		// find some ore?
		BlockState attactedOreBlock = Blocks.AIR.defaultBlockState();
		BlockState replacementBlock = Blocks.AIR.defaultBlockState();
		BlockPos foundPos = null;
        BlockPos basePos = null;

		for (BlockPos coord : new VoxelBresenhamIterator(usePos, destPos)) {
			BlockState searchState = world.getBlockState(coord);

			// keep track of where the dirt/stone we first find is.
			if (basePos == null) {
				if (isReplaceable(searchState)) {
					basePos = coord;
				}
				// This ordering is so that the base pos is found first before we pull ores - pushing ores away is a baaaaad idea!
			} else if (foundPos == null && searchState.getBlock() != Blocks.AIR && isOre(searchState.getBlock()) && world.getBlockEntity(coord) == null) {
				attactedOreBlock = searchState;
				replacementBlock = ORE_TO_BLOCK_REPLACEMENTS.getOrDefault(attactedOreBlock.getBlock(), Blocks.STONE).defaultBlockState();
				foundPos = coord;
			}
		}

		if (basePos != null && foundPos != null && attactedOreBlock.getBlock() != Blocks.AIR) {
			// find the whole vein
			Set<BlockPos> veinBlocks = new HashSet<>();
			findVein(world, foundPos, attactedOreBlock, veinBlocks);

			// move it up into minable blocks or dirt
			int offX = basePos.getX() - foundPos.getX();
			int offY = basePos.getY() - foundPos.getY();
			int offZ = basePos.getZ() - foundPos.getZ();

			for (BlockPos coord : veinBlocks) {
				BlockPos replacePos = coord.offset(offX, offY, offZ);
				BlockState replaceState = world.getBlockState(replacePos);

				if (isReplaceable(replaceState) || replaceState.getBlock() instanceof AirBlock) {
					world.setBlock(coord, replacementBlock, 2);

					// set close to ore material
					world.setBlock(replacePos, attactedOreBlock, 2);
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
        Block block = state.getBlock();

		return BlockTagGenerator.ORE_MAGNET_SAFE_REPLACE_BLOCK.contains(block);
	}

	private static boolean findVein(Level world, BlockPos here, BlockState oreState, Set<BlockPos> veinBlocks) {
		// is this already on the list?
		if (veinBlocks.contains(here)) {
			return false;
		}

		// let's limit it to 24 blocks at a time
		if (veinBlocks.size() >= 24) {
			return false;
		}

		// otherwise, check if we're still in the vein
		if (world.getBlockState(here) == oreState) {
			veinBlocks.add(here);

			// recurse in 6 directions
			for (Direction e : Direction.values()) {
				findVein(world, here.relative(e), oreState, veinBlocks);
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

		for (Block blockReplaceOre : BlockTagGenerator.ORE_MAGNET_BLOCK_REPLACE_ORE.getValues()) {
			ResourceLocation rl = blockReplaceOre.getRegistryName();
			Tag<Block> tag = BlockTags.getAllTags().getTagOrEmpty(TwilightForestMod.prefix("ore_magnet/" + rl.getNamespace() + "/" + rl.getPath()));

			for (Block oreBlock : tag.getValues()) {
				ORE_TO_BLOCK_REPLACEMENTS.put(oreBlock, blockReplaceOre);
			}
		}

		Set<Block> remainingOres = new HashSet<>(Tags.Blocks.ORES.getValues());
		remainingOres.removeAll(ORE_TO_BLOCK_REPLACEMENTS.keySet());
		remainingOres.removeIf(b -> "minecraft".equals(b.getRegistryName().getNamespace()));
		if (!remainingOres.isEmpty()) {
			TwilightForestMod.LOGGER.warn(remainingOres
					.stream()
					.peek(ore -> ORE_TO_BLOCK_REPLACEMENTS.put(ore, Blocks.STONE))
					.map(Block::getRegistryName)
					.map(ResourceLocation::toString)
					.collect(Collectors.joining(", ", "Partially supported ores with Ore Magnet, [", "], will relate these to `minecraft:stone`. Mod packers/Mod devs are encouraged to add support for their ores to our ore magnet through block tag jsons"))
			);
		} else {
			// You're probably NEVER going to see this message on a modpack
			TwilightForestMod.LOGGER.info("No remaining ores to map!");
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

			return stage.wait(null).thenRun(() -> {}); // Nothing to do here
		});
	}
}
