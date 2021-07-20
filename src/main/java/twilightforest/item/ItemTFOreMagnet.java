package twilightforest.item;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.data.BlockTagGenerator;
import twilightforest.util.FeatureUtil;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class ItemTFOreMagnet extends Item {

	private static final float WIGGLE = 10F;

	protected ItemTFOreMagnet(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setActiveHand(hand);
		return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity living, int useRemaining) {
		int useTime = this.getUseDuration(stack) - useRemaining;

		if (!world.isRemote && useTime > 10) {
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
				stack.damageItem(moved, living, user -> user.sendBreakAnimation(living.getActiveHand()));
				world.playSound(null, living.getPosX(), living.getPosY(), living.getPosZ(), TFSounds.MAGNET_GRAB, living.getSoundCategory(), 1.0F, 1.0F);
			}
		}
	}

	@Override
	public float getXpRepairRatio(ItemStack stack) {
		return 0.1f;
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	/**
	 * Magnet from the player's position and facing to the specified offset
	 */
	private int doMagnet(World world, LivingEntity living, float yawOffset, float pitchOffset) {
		// find vector 32 blocks from look
		double range = 32.0D;
		Vector3d srcVec = new Vector3d(living.getPosX(), living.getPosY() + living.getEyeHeight(), living.getPosZ());
		Vector3d lookVec = getOffsetLook(living, yawOffset, pitchOffset);
		Vector3d destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);

		return doMagnet(world, new BlockPos(srcVec), new BlockPos(destVec));
	}

	public static int doMagnet(World world, BlockPos usePos, BlockPos destPos) {
		// FIXME Find a better place to invoke this!
		initOre2BlockMap();

		int blocksMoved = 0;
		// get blocks in line from src to dest
		BlockPos[] lineArray = FeatureUtil.getBresenhamArrays(usePos, destPos);

		// find some ore?
		BlockState attactedOreBlock = Blocks.AIR.getDefaultState();
		BlockState replacementBlock = Blocks.AIR.getDefaultState();
		BlockPos foundPos = null;
        BlockPos basePos = null;

		for (BlockPos coord : lineArray) {
			BlockState searchState = world.getBlockState(coord);

			// keep track of where the dirt/stone we first find is.
			if (basePos == null) {
				if (isReplaceable(searchState)) {
					basePos = coord;
				}
				// This ordering is so that the base pos is found first before we pull ores - pushing ores away is a baaaaad idea!
			} else if (foundPos == null && searchState.getBlock() != Blocks.AIR && isOre(searchState.getBlock()) && world.getTileEntity(coord) == null) {
				attactedOreBlock = searchState;
				replacementBlock = ORE_TO_BLOCK_REPLACEMENTS.getOrDefault(attactedOreBlock.getBlock(), Blocks.STONE).getDefaultState();
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
				BlockPos replacePos = coord.add(offX, offY, offZ);
				BlockState replaceState = world.getBlockState(replacePos);

				if (isReplaceable(replaceState) || replaceState.getBlock() instanceof AirBlock) {
					world.setBlockState(coord, replacementBlock, 2);

					// set close to ore material
					world.setBlockState(replacePos, attactedOreBlock, 2);
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
	private Vector3d getOffsetLook(LivingEntity living, float yawOffset, float pitchOffset) {
		float var2 = MathHelper.cos(-(living.rotationYaw + yawOffset) * 0.017453292F - (float) Math.PI);
		float var3 = MathHelper.sin(-(living.rotationYaw + yawOffset) * 0.017453292F - (float) Math.PI);
		float var4 = -MathHelper.cos(-(living.rotationPitch + pitchOffset) * 0.017453292F);
		float var5 = MathHelper.sin(-(living.rotationPitch + pitchOffset) * 0.017453292F);
		return new Vector3d(var3 * var4, var5, var2 * var4);
	}

	@Deprecated
	private static boolean isReplaceable(BlockState state) {
        Block block = state.getBlock();

		return BlockTagGenerator.ORE_MAGNET_SAFE_REPLACE_BLOCK.contains(block);
	}

	private static boolean findVein(World world, BlockPos here, BlockState oreState, Set<BlockPos> veinBlocks) {
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
				findVein(world, here.offset(e), oreState, veinBlocks);
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

		for (Block blockReplaceOre : BlockTagGenerator.ORE_MAGNET_BLOCK_REPLACE_ORE.getAllElements()) {
			ResourceLocation rl = blockReplaceOre.getRegistryName();
			ITag<Block> tag = BlockTags.getCollection().getTagByID(TwilightForestMod.prefix("ore_magnet/" + rl.getNamespace() + "/" + rl.getPath()));

			for (Block oreBlock : tag.getAllElements()) {
				ORE_TO_BLOCK_REPLACEMENTS.put(oreBlock, blockReplaceOre);
			}
		}

		Set<Block> remainingOres = new HashSet<>(Tags.Blocks.ORES.getAllElements());
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

			return stage.markCompleteAwaitingOthers(null).thenRun(() -> {}); // Nothing to do here
		});
	}
}
