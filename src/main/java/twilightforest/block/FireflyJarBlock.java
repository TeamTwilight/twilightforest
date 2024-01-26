package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFParticleType;

public class FireflyJarBlock extends JarBlock {

	public FireflyJarBlock(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		ItemEntity firefly = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(TFBlocks.FIREFLY));
		if (player.isShiftKeyDown()) {
			level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			firefly.spawnAtLocation(firefly.getItem());
			firefly.spawnAtLocation(Items.GLASS_BOTTLE);
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
			return InteractionResult.sidedSuccess(level.isClientSide());
		} else {
			if (player.getItemInHand(hand).is(Items.POPPY)) {
				level.setBlockAndUpdate(pos, TFBlocks.FIREFLY_SPAWNER.get().defaultBlockState().setValue(AbstractParticleSpawnerBlock.RADIUS, 1));
				level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
				return InteractionResult.sidedSuccess(level.isClientSide());
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		for (int i = 0; i < 2; i++) {
			double dx = pos.getX() + ((random.nextFloat() - random.nextFloat()) * 0.2F + 0.5F);
			double dy = pos.getY() + 0.4F + ((random.nextFloat() - random.nextFloat()) * 0.3F);
			double dz = pos.getZ() + ((random.nextFloat() - random.nextFloat()) * 0.2F + 0.5F);
			level.addParticle(TFParticleType.FIREFLY.get(), dx, dy, dz, 0, 0, 0);
		}
	}
}
