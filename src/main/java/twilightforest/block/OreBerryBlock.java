package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredItem;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFDamageTypes;

public class OreBerryBlock extends TFBushBlock {
	private static final VoxelShape SMALL_BUSH_SHAPE = Block.box(4.0, 0.001, 4.0, 12.0, 8.0, 12.0);
	private static final VoxelShape MEDIUM_BUSH_SHAPE = Block.box(2.0, 0.001, 2.0, 14.0, 12.0, 14.0);
	private static final VoxelShape LARGE_BUSH_SHAPE = Block.box(0.001, 0.001, 0.001, 15.999, 15.999, 15.999);
	protected boolean surviveInLight;

	protected OreBerryBlock(DeferredItem<Item> harvestItem, TagKey<Block> surviveBlockTag, boolean surviveInLight) {
		super(harvestItem, BlockBehaviour.Properties.of().sound(SoundType.METAL), surviveBlockTag, SMALL_BUSH_SHAPE, MEDIUM_BUSH_SHAPE, LARGE_BUSH_SHAPE, 1, 3);
		this.surviveInLight = surviveInLight;
	}

	public OreBerryBlock(DeferredItem<Item> harvestItem, boolean surviveInLight) {
		this(harvestItem, BlockTagGenerator.OREBERRY_BUSHES_SURVIVE, surviveInLight);
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
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return super.canSurvive(state, level, pos) && (surviveInLight || level.getRawBrightness(pos, 0) < 13);
	}

	@Override
	protected boolean canGrowAt(BlockState state, LevelReader level, BlockPos pos) {
		return super.canGrowAt(state, level, pos) && (surviveInLight || level.getRawBrightness(pos, 0) < 10);
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
		return false;
	}
}
