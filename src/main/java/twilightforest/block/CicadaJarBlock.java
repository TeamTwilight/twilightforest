package twilightforest.block;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import twilightforest.block.entity.JarBlockEntity;
import twilightforest.components.item.JarLid;
import twilightforest.config.TFConfig;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFSounds;

public class CicadaJarBlock extends JarBlock {
	public CicadaJarBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
		if (player.isShiftKeyDown() && level.getBlockEntity(pos) instanceof JarBlockEntity jarBE) {
			ItemEntity cicada = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(TFBlocks.CICADA));
			level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			cicada.spawnAtLocation(cicada.getItem());
			cicada.spawnAtLocation(Util.make(new ItemStack(TFBlocks.MASON_JAR.get()), jar -> jar.set(TFDataComponents.JAR_LID.get(), new JarLid(jarBE.lid))));
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
			return InteractionResult.sidedSuccess(level.isClientSide());
		}
		return super.useWithoutItem(state, level, pos, player, result);
	}

	@Override
	public Item getDefaultLid() {
		return TFBlocks.CANOPY_LOG.asItem();
	}

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
