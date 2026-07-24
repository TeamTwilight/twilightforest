package twilightforest.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;
import twilightforest.network.ParticlePacket;
import twilightforest.tags.TFBlockTags;
import twilightforest.util.iterators.VoxelBresenhamIterator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class OreMagnetItem extends Item {

	// Switch over to ConcurrentHashMap if we run into any concurrency problems
	public static final HashMap<Block, Block> MAGNET_ORE_TO_BLOCK_REPLACEMENTS = new HashMap<>();
	public static final HashMap<Block, Block> TREE_ORE_TO_BLOCK_REPLACEMENTS = new HashMap<>();
	private static final float WIGGLE = 10F;

	public OreMagnetItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResult.SUCCESS;
	}

	@Override
	public boolean releaseUsing(ItemStack stack, Level level, LivingEntity living, int useRemaining) {
		int useTime = this.getUseDuration(stack, living) - useRemaining;

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
				stack.hurtAndBreak(moved, living, living.getUsedItemHand());
				level.playSound(null, living.getX(), living.getY(), living.getZ(), TFSounds.MAGNET_GRAB.get(), living.getSoundSource(), 1.0F, 1.0F);
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
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
			} else if (foundPos == null && searchState.getBlock() != Blocks.AIR && isOre(searchState.getBlock(), sourceIsMineCore) && level.getBlockEntity(coord) == null) {
				attactedOreBlock = searchState;
				replacementBlock = (sourceIsMineCore ? TREE_ORE_TO_BLOCK_REPLACEMENTS : MAGNET_ORE_TO_BLOCK_REPLACEMENTS).getOrDefault(attactedOreBlock.getBlock(), Blocks.STONE).defaultBlockState();
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
					level.setBlock(coord, replacementBlock, Block.UPDATE_CLIENTS);

					if (sourceIsMineCore && level instanceof ServerLevel serverLevel) {
						Vec3 xyz = Vec3.atCenterOf(replacePos);
						for (ServerPlayer serverplayer : serverLevel.players()) { // This is just particle math, we send a particle packet to every player in range
							if (serverplayer.distanceToSqr(xyz) < 4096.0D) {
								ParticlePacket particlePacket = new ParticlePacket();
								for (int i = 0; i < 16; i++) {
									Vec3 offset = new Vec3((level.getRandom().nextDouble() - 0.5D) * 1.25D, (level.getRandom().nextDouble() - 0.5D) * 1.25D, (level.getRandom().nextDouble() - 0.5D) * 1.25D);
									particlePacket.queueParticle(TFParticleType.LOG_CORE_PARTICLE.get(), false, false, xyz.add(offset), new Vec3(0.8, 0.9, 0.2));
								}
								PacketDistributor.sendToPlayer(serverplayer, particlePacket);
							}
						}
					}

					// set close to ore material
					level.setBlock(replacePos, attactedOreBlock, Block.UPDATE_CLIENTS);
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
		return state.is(TFBlockTags.ORE_MAGNET_SAFE_REPLACE_BLOCK);
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

	private static boolean isOre(Block ore, boolean core) {
		return (core ? TREE_ORE_TO_BLOCK_REPLACEMENTS : MAGNET_ORE_TO_BLOCK_REPLACEMENTS).containsKey(ore);
	}
}