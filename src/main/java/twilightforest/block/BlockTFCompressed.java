package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class BlockTFCompressed extends Block {

	public BlockTFCompressed(Properties props) {
		super(props);
	}

	@Override
	@Deprecated
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
		if (this == TFBlocks.fiery_block.get()) {
			return 1.0F;
		}
		return super.getAmbientOcclusionLightValue(state, worldIn, pos);
	}

	@Override
	@Deprecated
	public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
		// ItemShears#getDestroySpeed is really dumb and doesn't check IShearable so we have to do it this way to try to match the wool break speed with shears
		return state.getBlock() == TFBlocks.arctic_fur_block.get() && player.getHeldItemMainhand().getItem() instanceof ShearsItem ? 0.2F : super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
	}

	//TODO 1.16: Move to supports_beacon Tag
	@Override
	public boolean isBeaconBase(BlockState state, IWorldReader world, BlockPos pos, BlockPos beacon) {
		return true;
	}

	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		if ((!entityIn.isImmuneToFire())
				&& entityIn instanceof LivingEntity
				&& (!EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn))
				&& this == TFBlocks.fiery_block.get()) {
			entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
		}

		super.onEntityWalk(worldIn, pos, entityIn);
	}

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		if (this == TFBlocks.steeleaf_block.get()) {
			entityIn.onLivingFall(fallDistance, 0.75F);
		} else if (this == TFBlocks.arctic_fur_block.get()) {
			entityIn.onLivingFall(fallDistance, 0.1F);
		}
	}

	@Override
	public boolean isFireSource(BlockState state, IWorldReader world, BlockPos pos, Direction side) {
		return this == TFBlocks.fiery_block.get();
	}

	@Override
	public boolean isStickyBlock(BlockState state) {
		return this == TFBlocks.carminite_block.get();
	}
}
