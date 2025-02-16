package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.neoforged.neoforge.registries.DeferredItem;
import twilightforest.init.TFDamageTypes;

public class OreBerryBlock extends TFBushBlock {
	protected boolean surviveInLight;

	public OreBerryBlock(DeferredItem<Item> harvestItem, boolean surviveInLight) {
		super(harvestItem, BlockBehaviour.Properties.of().sound(SoundType.METAL));
		this.surviveInLight = surviveInLight;
	}

	public OreBerryBlock(DeferredItem<Item> harvestItem) {
		this(harvestItem, false);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (!(entity instanceof ItemEntity)) {
			entity.hurt(TFDamageTypes.getDamageSource(level, TFDamageTypes.OREBERRY), 1.0F);
		}
		super.entityInside(state, level, pos, entity);
	}

	@Override
	protected int getNumberOfBerries(RandomSource random) {
		return random.nextInt(3) + 1;
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return surviveInLight || level.getRawBrightness(pos, 0) < 13;
	}

	@Override
	protected boolean canGrow(ServerLevel level, BlockPos pos) {
		return surviveInLight || level.getRawBrightness(pos, 0) < 10;
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
		return false;
	}
}
