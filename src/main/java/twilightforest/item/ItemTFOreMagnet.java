package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.UseAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeatureUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class ItemTFOreMagnet extends Item {

	private static final float WIGGLE = 10F;

	protected ItemTFOreMagnet(Properties props) {
		super(props);
		// [VanillaCopy] ItemBow with our item
		this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
			@Override
			@OnlyIn(Dist.CLIENT)
			public float call(ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entityIn) {
				if (entityIn == null) {
					return 0.0F;
				} else {
					ItemStack itemstack = entityIn.getActiveItemStack();
					return !itemstack.isEmpty() && itemstack.getItem() == TFItems.ore_magnet.get() ? (float) (stack.getUseDuration() - entityIn.getItemInUseCount()) / 20.0F : 0.0F;
				}
			}
		});
		this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
			@Override
			@OnlyIn(Dist.CLIENT)
			public float call(ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entityIn) {
				return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
			}
		});
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
				stack.damageItem(moved, living, (user) -> user.sendBreakAnimation(living.getActiveHand()));
				world.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, living.getSoundCategory(), 1.0F, 1.0F);
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
		Vec3d srcVec = new Vec3d(living.getX(), living.getY() + living.getEyeHeight(), living.getZ());
		Vec3d lookVec = getOffsetLook(living, yawOffset, pitchOffset);
		Vec3d destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);

		return doMagnet(world, new BlockPos(srcVec), new BlockPos(destVec));
	}

	public static int doMagnet(World world, BlockPos usePos, BlockPos destPos) {
		int blocksMoved = 0;
		// get blocks in line from src to dest
		BlockPos[] lineArray = FeatureUtil.getBresehnamArrays(usePos, destPos);

		// find some ore?
		BlockState foundState = Blocks.AIR.getDefaultState();
		BlockPos foundPos = null;
        BlockPos basePos = null;

        boolean isNetherrack = false;

		for (BlockPos coord : lineArray) {
			BlockState searchState = world.getBlockState(coord);

			// keep track of where the dirt/stone we first find is.s
			if (basePos == null) {
				if (isReplaceable(world, searchState, coord)) {
					basePos = coord;
				} else if (isNetherReplaceable(world, searchState, coord)) {
					isNetherrack = true;
					basePos = coord;
				}
				// This ordering is so that the base pos is found first before we pull ores - pushing ores away is a baaaaad idea!
			} else if (foundPos == null && searchState.getBlock() != Blocks.AIR && isOre(searchState) && (world.getTileEntity(coord) == null)) {
                foundState = searchState;
                foundPos = coord;
            }
		}

		if (basePos != null && foundState.getBlock() != Blocks.AIR) {
			// find the whole vein
			Set<BlockPos> veinBlocks = new HashSet<BlockPos>();
			findVein(world, foundPos, foundState, veinBlocks);

			// move it up into minable blocks or dirt
			int offX = basePos.getX() - foundPos.getX();
			int offY = basePos.getY() - foundPos.getY();
			int offZ = basePos.getZ() - foundPos.getZ();

			for (BlockPos coord : veinBlocks) {
				BlockPos replacePos = coord.add(offX, offY, offZ);
				BlockState replaceState = world.getBlockState(replacePos);

				if ((isNetherrack ? isNetherReplaceable(world, replaceState, replacePos) : isReplaceable(world, replaceState, replacePos)) || replaceState.getBlock() == Blocks.AIR) {
					// set vein to stone / netherrack
					world.setBlockState(coord, isNetherrack ? Blocks.NETHERRACK.getDefaultState() : Blocks.STONE.getDefaultState(), 2);

					// set close to ore material
					world.setBlockState(replacePos, foundState, 2);
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
	private Vec3d getOffsetLook(LivingEntity living, float yawOffset, float pitchOffset) {
		float var2 = MathHelper.cos(-(living.rotationYaw + yawOffset) * 0.017453292F - (float) Math.PI);
		float var3 = MathHelper.sin(-(living.rotationYaw + yawOffset) * 0.017453292F - (float) Math.PI);
		float var4 = -MathHelper.cos(-(living.rotationPitch + pitchOffset) * 0.017453292F);
		float var5 = MathHelper.sin(-(living.rotationPitch + pitchOffset) * 0.017453292F);
		return new Vec3d(var3 * var4, var5, var2 * var4);
	}

	private static boolean isReplaceable(World world, BlockState state, BlockPos pos) {
        Block block = state.getBlock();

	    if (block == Blocks.DIRT
                || block == Blocks.GRASS
                || block == Blocks.GRAVEL
                || (block != Blocks.AIR && block.isReplaceableOreGen(state, world, pos, BlockMatcher.forBlock(Blocks.STONE)))) {
			return true;
		}

		return false;
	}

	private static boolean isNetherReplaceable(World world, BlockState state, BlockPos pos) {
		if (state.getBlock() == Blocks.NETHERRACK) {
			return true;
		}
		if (state.getBlock() != Blocks.AIR && state.getBlock().isReplaceableOreGen(state, world, pos, BlockMatcher.forBlock(Blocks.NETHERRACK))) {
			return true;
		}

		return false;
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

	private static boolean isOre(BlockState state) {
        Block block = state.getBlock();

		if (block == Blocks.COAL_ORE) return false;

		if (block == Blocks.IRON_ORE
                || block == Blocks.DIAMOND_ORE
                || block == Blocks.EMERALD_ORE
                || block == Blocks.GOLD_ORE
                || block == Blocks.LAPIS_ORE
                || block == Blocks.REDSTONE_ORE
                || block == Blocks.NETHER_QUARTZ_ORE
                || block == TFBlocks.liveroot.get()
                // todo 1.9 oh god
				// National treasure -Drullkus
				// TODO 1.14: noh, we are use tag instead
                || state.getBlock().isIn(Tags.Blocks.ORES))
		    return true;

		return false;
	}
}
