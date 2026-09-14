package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import org.jetbrains.annotations.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UberousSoilBlock extends Block implements BonemealableBlock {

	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	// applyBonemeal is private on vanilla BoneMealItem and relies on a Forge Access Transformer
	// to be publicly callable. Environments that don't apply that AT at runtime (e.g. Sinytra
	// Connector) would throw IllegalAccessError on a direct call, so we invoke it reflectively instead.
	private static final Method APPLY_BONEMEAL;

	static {
		try {
			APPLY_BONEMEAL = BoneMealItem.class.getDeclaredMethod("applyBonemeal", ItemStack.class, Level.class, BlockPos.class, Player.class);
			APPLY_BONEMEAL.setAccessible(true);
		} catch (NoSuchMethodException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public UberousSoilBlock(Properties properties) {
		super(properties);
	}

	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return ctx.getLevel().getBlockState(ctx.getClickedPos().above()).isSolid() ? Blocks.DIRT.defaultBlockState() : super.getStateForPlacement(ctx);
	}

	@Override
	public boolean canSustainPlant(BlockState state, BlockGetter getter, BlockPos pos, Direction direction, IPlantable plantable) {
		if (direction != Direction.UP)
			return false;
		PlantType plantType = plantable.getPlantType(getter, pos.relative(direction));
		return plantType == PlantType.CROP || plantType == PlantType.PLAINS || plantType == PlantType.CAVE;
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (fromPos.getY() == pos.getY() + 1) {
			BlockState above = level.getBlockState(fromPos);
			if (!(above.getBlock() instanceof BonemealableBlock bonemealableBlock && !above.is(TFBlocks.UBEROUS_SOIL.get()))) {
				if (above.isSolid())
					level.setBlockAndUpdate(pos, pushEntitiesUp(state, Blocks.DIRT.defaultBlockState(), level, pos));
				return;
			}

			BlockState newState = Blocks.DIRT.defaultBlockState();

			if (bonemealableBlock instanceof IPlantable iPlantable && iPlantable.getPlantType(level, fromPos) == PlantType.CROP)
				newState = Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, 7);
			else if (bonemealableBlock instanceof MushroomBlock)
				newState = Blocks.MYCELIUM.defaultBlockState();
			else if (bonemealableBlock instanceof BushBlock)
				newState = Blocks.GRASS_BLOCK.defaultBlockState();
			else if (bonemealableBlock instanceof MossBlock mossBlock)
				newState = mossBlock.defaultBlockState();

			if (level instanceof ServerLevel serverLevel && bonemealableBlock instanceof MushgloomBlock mushgloomBlock) {
				/*
				  This seems a bit hacky, but it's the easiest way of letting the mushgloom only be grown by uberous soil
				  If we make it growable by bonemeal as well, just delete this if statement and update the appropriate method inside the mushgloom class
				 */
				level.setBlockAndUpdate(pos, pushEntitiesUp(state, newState, level, pos));
				mushgloomBlock.growMushroom(serverLevel, fromPos, above, serverLevel.random);
				level.levelEvent(2005, fromPos, 0);
				return;
			}

			/*
			 The block must be set to a new one before we attempt to bonemeal the plant, otherwise, we can end up with an infinite block update loop
			 For example, if we try to grow a mushroom but there isn't enough room for it to grow. (For some reason mushroom code does a block update when failing to grow)
			 */
			level.setBlockAndUpdate(pos, pushEntitiesUp(state, newState, level, pos));

			if (level instanceof ServerLevel serverLevel) {
				MinecraftServer server = serverLevel.getServer();
				FakePlayer fakePlayer = FakePlayerFactory.getMinecraft(serverLevel);
				server.tell(new TickTask(server.getTickCount(), () -> {
					//We need to use a tick task so that plants that grow into tall variants don't just break upon growth
					for (int i = 0; i < 15; i++) {
						try {
							APPLY_BONEMEAL.invoke(null, new ItemStack(Items.BONE_MEAL), serverLevel, fromPos, fakePlayer);
						} catch (IllegalAccessException | InvocationTargetException e) {
							throw new RuntimeException(e);
						}
					}
				}));
			}

			level.levelEvent(2005, fromPos, 0);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		if (level.isClientSide() && rand.nextInt(5) == 0) {
			for (Player player : level.players()) {
				if (player.getMainHandItem().getItem().equals(TFItems.MAGIC_BEANS.get()) || player.getOffhandItem().getItem().equals(TFItems.MAGIC_BEANS.get())) {
					for (int i = 0; i < 2; i++) {
						level.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + rand.nextDouble(), pos.getY() + 1.25D, pos.getZ() + rand.nextDouble(), 0.0D, 0.0D, 0.0D);
					}
					break;
				}
			}
		}
	}

	@Override
	//check each side of the block, as well as above and below each of those positions for valid spots
	public boolean isValidBonemealTarget(LevelReader getter, BlockPos pos, BlockState state, boolean isClient) {
		for (Direction dir : Direction.values()) {
			if (dir != Direction.UP && dir != Direction.DOWN) {
				BlockState blockAt = getter.getBlockState(pos.relative(dir));
				if (
						!getter.getBlockState(pos.relative(dir).above()).isSolid() &&
								(blockAt.is(BlockTags.DIRT) || blockAt.is(Blocks.FARMLAND)) &&
								!blockAt.is(TFBlocks.UBEROUS_SOIL.get())) {
					return true;

				} else if (
						!getter.getBlockState(pos.relative(dir).above().above()).isSolid() &&
								(getter.getBlockState(pos.relative(dir).above()).is(BlockTags.DIRT) || getter.getBlockState(pos.relative(dir).above()).is(Blocks.FARMLAND)) &&
								!getter.getBlockState(pos.relative(dir).above()).is(TFBlocks.UBEROUS_SOIL.get())) {
					return true;

				} else if (
						!getter.getBlockState(pos.relative(dir)).isSolid() &&
								(getter.getBlockState(pos.relative(dir).below()).is(BlockTags.DIRT) || getter.getBlockState(pos.relative(dir).below()).is(Blocks.FARMLAND)) &&
								!getter.getBlockState(pos.relative(dir).below()).is(TFBlocks.UBEROUS_SOIL.get())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	//check each side of the block, as well as above and below each of those positions to check for a place to put a block
	//the above and below checks allow the patch to jump to a new y level, makes spreading easier
	public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
		List<Direction> directions = Arrays.asList(Direction.values());
		Collections.shuffle(directions);
		for (Direction dir : directions) {
			if (dir != Direction.UP && dir != Direction.DOWN) {
				BlockState blockAt = level.getBlockState(pos.relative(dir));
				if (
						!level.getBlockState(pos.relative(dir).above()).isSolid() &&
								(blockAt.is(BlockTags.DIRT) || blockAt.is(Blocks.FARMLAND)) &&
								!blockAt.is(TFBlocks.UBEROUS_SOIL.get())) {

					level.setBlockAndUpdate(pos.relative(dir), this.defaultBlockState());
					break;
				} else if (
						!level.getBlockState(pos.relative(dir).above().above()).isSolid() &&
								(level.getBlockState(pos.relative(dir).above()).is(BlockTags.DIRT) || level.getBlockState(pos.relative(dir).above()).is(Blocks.FARMLAND)) &&
								!level.getBlockState(pos.relative(dir).above()).is(TFBlocks.UBEROUS_SOIL.get())) {

					level.setBlockAndUpdate(pos.relative(dir).above(), this.defaultBlockState());
					break;
				} else if (
						!level.getBlockState(pos.relative(dir)).isSolid() &&
								(level.getBlockState(pos.relative(dir).below()).is(BlockTags.DIRT) || level.getBlockState(pos.relative(dir).below()).is(Blocks.FARMLAND)) &&
								!level.getBlockState(pos.relative(dir).below()).is(TFBlocks.UBEROUS_SOIL.get())) {

					level.setBlockAndUpdate(pos.relative(dir).below(), this.defaultBlockState());
					break;
				}
			}
		}
	}
}
