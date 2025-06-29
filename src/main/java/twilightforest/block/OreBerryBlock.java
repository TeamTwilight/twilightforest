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
import net.neoforged.neoforge.registries.DeferredItem;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFDamageTypes;

public class OreBerryBlock extends TFBushBlock {
	protected boolean surviveInLight;

	protected OreBerryBlock(DeferredItem<Item> harvestItem, TagKey<Block> surviveBlockTag, boolean surviveInLight) {
		super(harvestItem, BlockBehaviour.Properties.of().sound(SoundType.METAL), surviveBlockTag);
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
	protected int getNumberOfBerries(RandomSource random) {
		return random.nextInt(3) + 1;
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
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
