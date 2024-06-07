package twilightforest.block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.config.TFConfig;
import twilightforest.init.TFSounds;

public class CicadaJarBlock extends JarBlock {
	public CicadaJarBlock(Properties properties) {
		super(properties);
	}

	/*@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
		if (player.isShiftKeyDown()) {
			ItemEntity cicada = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(TFBlocks.CICADA));
			level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			cicada.spawnAtLocation(cicada.getItem());
			cicada.spawnAtLocation(Items.GLASS_BOTTLE);
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
			return InteractionResult.sidedSuccess(level.isClientSide());
		}
		return InteractionResult.PASS;
	}*/

	@Override
	public void destroy(LevelAccessor accessor, BlockPos pos, BlockState state) {
		super.destroy(accessor, pos, state);
		if (accessor.isClientSide())
			Minecraft.getInstance().getSoundManager().stop(TFSounds.CICADA.get().getLocation(), SoundSource.BLOCKS);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		double dx = pos.getX() + ((random.nextFloat() - random.nextFloat()) * 0.2F + 0.5F);
		double dy = pos.getY() + 0.4F + ((random.nextFloat() - random.nextFloat()) * 0.2F);
		double dz = pos.getZ() + ((random.nextFloat() - random.nextFloat()) * 0.2F + 0.5F);
		level.addParticle(ParticleTypes.NOTE, dx, dy, dz, 0, 0, 0);
		if (level.getRandom().nextInt(75) == 0 && !TFConfig.silentCicadas) {
			level.playLocalSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, TFSounds.CICADA.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
		}
	}
}
