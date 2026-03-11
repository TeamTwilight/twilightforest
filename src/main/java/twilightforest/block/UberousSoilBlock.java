package twilightforest.block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
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
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.common.util.TriState;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UberousSoilBlock extends Block implements BonemealableBlock {

	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	public UberousSoilBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos().above());
		return state.isSolid() && !(state.getBlock() instanceof BonemealableBlock && !state.is(this)) ? Blocks.DIRT.defaultBlockState() : super.getStateForPlacement(ctx);
	}

	@Override
	public TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos soilPosition, Direction facing, BlockState plant) {
		if (facing.getAxis() != Direction.Axis.Y) return TriState.FALSE;
		if (plant.is(BlockTags.CROPS)) return TriState.TRUE;
		return super.canSustainPlant(state, level, soilPosition, facing, plant);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
		if (!newState.is(state.getBlock()) && !level.getBlockState(pos.above()).isAir()) this.neighborChanged(state, level, pos, this, pos.above(), moving);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (fromPos.getY() == pos.getY() + 1) {
			BlockState above = level.getBlockState(fromPos);
			if (!(above.getBlock() instanceof BonemealableBlock bonemealableBlock && !above.is(this))) {
				if (above.isSolid()) FarmBlock.turnToDirt(null, state, level, pos);
				return;
			}

			BlockState newState = Blocks.DIRT.defaultBlockState();

			if (above.is(BlockTags.CROPS))
				newState = Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, 7);
			else if (bonemealableBlock instanceof MushroomBlock)
				newState = Blocks.MYCELIUM.defaultBlockState();
			else if (bonemealableBlock instanceof BushBlock)
				newState = Blocks.GRASS_BLOCK.defaultBlockState();
			else if (bonemealableBlock instanceof MossBlock mossBlock)
				newState = mossBlock.defaultBlockState();

			if (level instanceof ServerLevel serverLevel) {
				if (bonemealableBlock instanceof MushgloomBlock mushgloomBlock) {
					//This seems a bit hacky, but it's the easiest way of letting the mushgloom only be grown by uberous soil
					//If we make it growable by bonemeal as well, just delete this if statement and update the appropriate method inside the mushgloom class
					level.setBlockAndUpdate(pos, pushEntitiesUp(state, newState, level, pos));
					mushgloomBlock.growMushroom(serverLevel, fromPos, above, serverLevel.random);
					level.levelEvent(2005, fromPos, 0); // FIXME Nothing happens, used to call BoneMealItem.addGrowthParticles on client
					return;
				}
				level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, fromPos, 15); // Bonemeal particles
			}


			//The block must be set to a new one before we attempt to bonemeal the plant, otherwise, we can end up with an infinite block update loop
			//For example, if we try to grow a mushroom but there isn't enough room for it to grow. (For some reason mushroom code does a block update when failing to grow)
			level.setBlockAndUpdate(pos, pushEntitiesUp(state, newState, level, pos));

			if (level instanceof ServerLevel serverLevel) {
				MinecraftServer server = serverLevel.getServer();
				FakePlayer fakePlayer = FakePlayerFactory.getMinecraft(serverLevel);
				server.tell(new TickTask(server.getTickCount(), () -> {
					//We need to use a tick task so that plants that grow into tall variants don't just break upon growth
					for (int i = 0; i < 15; i++)
						BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), serverLevel, fromPos, fakePlayer);
				}));
			}

			level.levelEvent(2005, fromPos, 0); // FIXME Nothing happens, used to call BoneMealItem.addGrowthParticles on client
		} else if (fromPos.getY() + 1 == pos.getY()) {
			BlockState below = level.getBlockState(fromPos);
			if (!(below.getBlock() instanceof BonemealableBlock)) return;

			level.setBlockAndUpdate(pos, pushEntitiesUp(state, Blocks.DIRT.defaultBlockState(), level, pos));

			if (level instanceof ServerLevel serverLevel) {
				MinecraftServer server = serverLevel.getServer();
				FakePlayer fakePlayer = FakePlayerFactory.getMinecraft(serverLevel);
				server.tell(new TickTask(server.getTickCount(), () -> {
					for (int i = 0; i < 15; i++) BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), serverLevel, fromPos, fakePlayer);
				}));

				level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, fromPos, 15); // Bonemeal particles
			}

			level.levelEvent(2005, fromPos, 0); // FIXME Nothing happens, used to call BoneMealItem.addGrowthParticles on client
		}
	}

	@Override
	protected boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		if (level.isClientSide() && rand.nextInt(5) == 0) {
			if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isHolding(TFItems.MAGIC_BEANS.get())) {
				for (int i = 0; i < 2; i++) {
					level.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + rand.nextDouble(), pos.getY() + 1.25D, pos.getZ() + rand.nextDouble(), 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	//check each side of the block, as well as above and below each of those positions for valid spots
	public boolean isValidBonemealTarget(LevelReader getter, BlockPos pos, BlockState state) {
		for (Direction dir : Direction.values()) {
			if (dir != Direction.UP && dir != Direction.DOWN) {
				BlockState blockAt = getter.getBlockState(pos.relative(dir));
				if (
					!getter.getBlockState(pos.relative(dir).above()).isSolid() &&
						(blockAt.is(BlockTags.DIRT) || blockAt.is(Blocks.FARMLAND)) &&
						!blockAt.is(TFBlocks.UBEROUS_SOIL)) {
					return true;

				} else if (
					!getter.getBlockState(pos.relative(dir).above().above()).isSolid() &&
						(getter.getBlockState(pos.relative(dir).above()).is(BlockTags.DIRT) || getter.getBlockState(pos.relative(dir).above()).is(Blocks.FARMLAND)) &&
						!getter.getBlockState(pos.relative(dir).above()).is(TFBlocks.UBEROUS_SOIL)) {
					return true;

				} else if (
					!getter.getBlockState(pos.relative(dir)).isSolid() &&
						(getter.getBlockState(pos.relative(dir).below()).is(BlockTags.DIRT) || getter.getBlockState(pos.relative(dir).below()).is(Blocks.FARMLAND)) &&
						!getter.getBlockState(pos.relative(dir).below()).is(TFBlocks.UBEROUS_SOIL)) {
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
	@SuppressWarnings("deprecation")
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
						!blockAt.is(TFBlocks.UBEROUS_SOIL)) {

					this.spreadTo(level, pos.relative(dir));
					break;
				} else if (
					!level.getBlockState(pos.relative(dir).above().above()).isSolid() &&
						(level.getBlockState(pos.relative(dir).above()).is(BlockTags.DIRT) || level.getBlockState(pos.relative(dir).above()).is(Blocks.FARMLAND)) &&
						!level.getBlockState(pos.relative(dir).above()).is(TFBlocks.UBEROUS_SOIL)) {

					this.spreadTo(level, pos.relative(dir).above());
					break;
				} else if (
					!level.getBlockState(pos.relative(dir)).isSolid() &&
						(level.getBlockState(pos.relative(dir).below()).is(BlockTags.DIRT) || level.getBlockState(pos.relative(dir).below()).is(Blocks.FARMLAND)) &&
						!level.getBlockState(pos.relative(dir).below()).is(TFBlocks.UBEROUS_SOIL)) {

					this.spreadTo(level, pos.relative(dir).below());
					break;
				}
			}
		}
	}

	public void spreadTo(ServerLevel level, BlockPos pos) {
		level.setBlockAndUpdate(pos, this.defaultBlockState());
		if (!level.getBlockState(pos.above()).isAir()) this.neighborChanged(this.defaultBlockState(), level, pos, this, pos.above(), false);
	}
}
