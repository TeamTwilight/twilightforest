package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.block.BlockTFLeaves3;
import twilightforest.block.TFBlocks;
import twilightforest.enums.Leaves3Variant;
import twilightforest.world.WorldProviderTwilightForest;

import javax.annotation.Nonnull;

public class ItemTFMagicBeans extends ItemTF {

	public ItemTFMagicBeans() {
		this.makeRare();
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		Block blockAt = world.getBlockState(pos).getBlock();

		int minY = pos.getY() + 1;
		int maxY = Math.max(pos.getY() + 100, (int) (getCloudHeight(world) + 25));
		if (pos.getY() < maxY && blockAt == TFBlocks.uberous_soil) {
			if (!world.isRemote) {
				player.getHeldItem(hand).shrink(1);
				makeHugeStalk(world, pos, minY, maxY);
			}

			return EnumActionResult.SUCCESS;
		} else {
			return EnumActionResult.PASS;
		}
	}

	@SuppressWarnings("RedundantCast")
	private float getCloudHeight(World world) {
		if (world.provider instanceof WorldProviderTwilightForest) {
			// WorldProviderTwilightForest has this method on both server and client
			return ((WorldProviderTwilightForest) world.provider).getCloudHeight(); // This cast is actually needed for some reason, else this will toss a Method Not Found on dedicated servers.
		} else {
			// otherwise, world.provider.getCloudHeight() is client only. guess 128
			return 128;
		}
	}


	private void makeHugeStalk(World world, BlockPos pos, int minY, int maxY) {
		float x = pos.getX();
		float z = pos.getZ();

		int yOffset = world.rand.nextInt(100);

		float cScale = world.rand.nextFloat() * 0.25F + 0.125F; // spiral tightness scaling
		float rScale = world.rand.nextFloat() * 0.25F + 0.125F; // radius change scaling

		// offset x and z to make stalk start at origin
		float radius = 4F + MathHelper.sin((minY + yOffset) * rScale) * 3F; // make radius a little wavy
		x -= MathHelper.sin((minY + yOffset) * cScale) * radius;
		z -= MathHelper.cos((minY + yOffset) * cScale) * radius;

		// leaves!
		int nextLeafY = minY + 10 + world.rand.nextInt(20);

		// make stalk
		boolean isClear = true;
		for (int dy = minY; dy < maxY && isClear; dy++) {
			// make radius a little wavy
			radius = 5F + MathHelper.sin((dy + yOffset) * rScale) * 2.5F;

			// find center of stalk
			float cx = x + MathHelper.sin((dy + yOffset) * cScale) * radius;
			float cz = z + MathHelper.cos((dy + yOffset) * cScale) * radius;


			float stalkThickness = 2.5F;

			// reduce thickness near top
			if (maxY - dy < 5) {
				stalkThickness *= (maxY - dy) / 5F;
			}

			int minX = MathHelper.floor(x - radius - stalkThickness);
			int maxX = MathHelper.ceil(x + radius + stalkThickness);
			int minZ = MathHelper.floor(z - radius - stalkThickness);
			int maxZ = MathHelper.ceil(z + radius + stalkThickness);

			// generate stalk
			for (int dx = minX; dx < maxX; dx++) {
				for (int dz = minZ; dz < maxZ; dz++) {
					if ((dx - cx) * (dx - cx) + (dz - cz) * (dz - cz) < stalkThickness * stalkThickness) {
						isClear &= this.tryToPlaceStalk(world, new BlockPos(dx, dy, dz));
					}
				}
			}

			// leaves?
			if (dy == nextLeafY) {
				// make leaf blob

				int lx = (int) (x + MathHelper.sin((dy + yOffset) * cScale) * (radius + stalkThickness));
				int lz = (int) (z + MathHelper.cos((dy + yOffset) * cScale) * (radius + stalkThickness));

				this.placeLeaves(world, new BlockPos(lx, dy, lz));

				nextLeafY = dy + 5 + world.rand.nextInt(10);
			}
		}
	}

	private void placeLeaves(World world, BlockPos pos) {
		// stalk at center
		world.setBlockState(pos, TFBlocks.huge_stalk.getDefaultState());

		// small squares
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				this.tryToPlaceLeaves(world, pos.add(dx, -1, dz));
				this.tryToPlaceLeaves(world, pos.add(dx, 1, dz));
			}
		}
		// larger square
		for (int dx = -2; dx <= 2; dx++) {
			for (int dz = -2; dz <= 2; dz++) {
				if (!((dx == 2 || dx == -2) && (dz == 2 || dz == -2))) {
					this.tryToPlaceLeaves(world, pos.add(dx, 0, dz));
				}
			}
		}
	}

	/**
	 * Place the stalk block only if the destination is clear.  Return false if blocked.
	 */
	private boolean tryToPlaceStalk(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock().isAir(state, world, pos) || state.getBlock().isReplaceable(world, pos) || state.getBlock().canBeReplacedByLeaves(state, world, pos) || state.getBlock().isLeaves(state, world, pos) || state.getBlock().canSustainLeaves(state, world, pos)) {
			world.setBlockState(pos, TFBlocks.huge_stalk.getDefaultState());
			return true;
		} else {
			return false;
		}
	}


	private void tryToPlaceLeaves(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock().isAir(state, world, pos) || state.getBlock().canBeReplacedByLeaves(state, world, pos)) {
			world.setBlockState(pos, TFBlocks.twilight_leaves_3.getDefaultState().withProperty(BlockTFLeaves3.VARIANT, Leaves3Variant.BEANSTALK), 2);
		}
	}

}
