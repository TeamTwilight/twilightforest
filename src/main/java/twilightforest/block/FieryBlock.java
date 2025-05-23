package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.fml.ModList;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFItems;

public class FieryBlock extends Block {
	public FieryBlock(Properties properties) {
		super(properties);
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter getter, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean skipRendering(BlockState state, BlockState otherState, Direction direction) {
		return ModList.get().isLoaded("ctm") && otherState.getBlock() instanceof FieryBlock;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state) {
		return Shapes.empty();
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (level instanceof ServerLevel serverLevel && !entity.fireImmune()
			&& entity instanceof LivingEntity living
			&& !living.getItemBySlot(EquipmentSlot.FEET).is(TFItems.FIERY_BOOTS.get())) {
			living.hurtServer(serverLevel, serverLevel.damageSources().source(TFDamageTypes.FIERY), 1.0F);
		}

		super.stepOn(level, pos, state, entity);
	}

	@Override
	public boolean isFireSource(BlockState state, LevelReader level, BlockPos pos, Direction direction) {
		return true;
	}
}
