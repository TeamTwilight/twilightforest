package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

public class BurntThornsBlock extends ThornsBlock {

	public BurntThornsBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter getter, BlockPos pos, @Nullable Mob entity) {
		return null;
	}

	@Override
	@Deprecated
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		// dissolve
		if (!level.isClientSide() && (entity instanceof LivingEntity || entity instanceof Projectile)) {
			level.destroyBlock(pos, false);
		}
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		state.getBlock().playerWillDestroy(level, pos, state, player);
		return level.setBlock(pos, fluid.createLegacyBlock(), level.isClientSide ? 11 : 3);
	}
}
