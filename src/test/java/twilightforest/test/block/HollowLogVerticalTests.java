package twilightforest.test.block;

import twilightforest.block.ClimbableHollowLogBlock;
import twilightforest.block.VerticalHollowLogBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tamaized.beanification.junit.MockitoFixer;
import twilightforest.enums.HollowLogVariants;
import twilightforest.init.TFBlocks;
import twilightforest.util.DirectionUtil;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoFixer.class)
public class HollowLogVerticalTests {

	@Mock
	private DirectionUtil directionUtil;

	@InjectMocks
	private VerticalHollowLogBlock instance;

	@BeforeEach
	public void setup() {
		instance = TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL.value();
		MockitoAnnotations.openMocks(this);
	}

	private InteractionResult callUseItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) throws Exception {
		Method method = VerticalHollowLogBlock.class.getDeclaredMethod("useItemOn", ItemStack.class, BlockState.class, Level.class, BlockPos.class, Player.class, InteractionHand.class, BlockHitResult.class);
		method.setAccessible(true);
		return (InteractionResult) method.invoke(instance, stack, state, level, pos, player, hand, hitResult);
	}

	@Test
	public void useItemOnNotInside() throws Exception {
		ItemStack stack = mock(ItemStack.class);
		BlockState state = mock(BlockState.class);
		Level level = mock(Level.class);
		Player player = mock(Player.class);
		BlockHitResult hitResult = mock(BlockHitResult.class);

		when(hitResult.getLocation()).thenReturn(Vec3.ZERO);

		InteractionResult result = callUseItemOn(stack, state, level, BlockPos.ZERO, player, InteractionHand.MAIN_HAND, hitResult);

		assertSame(InteractionResult.TRY_WITH_EMPTY_HAND, result);
		verify(stack, never()).is(any(Item.class));
	}

	@Test
	public void useItemOnInside() throws Exception {
		ItemStack stack = mock(ItemStack.class);
		BlockState state = mock(BlockState.class);
		Level level = mock(Level.class);
		Player player = mock(Player.class);
		BlockHitResult hitResult = mock(BlockHitResult.class);

		when(hitResult.getLocation()).thenReturn(new Vec3(0.5, 0.5, 0.5));
		when(stack.is(any(Item.class))).thenReturn(false);

		InteractionResult result = callUseItemOn(stack, state, level, BlockPos.ZERO, player, InteractionHand.MAIN_HAND, hitResult);

		assertSame(InteractionResult.TRY_WITH_EMPTY_HAND, result);
		verify(stack, times(1)).is(Blocks.VINE.asItem());
		verify(stack, times(1)).is(Blocks.LADDER.asItem());
	}

	@Test
	public void useItemOnVine() throws Exception {
		ItemStack stack = mock(ItemStack.class);
		BlockState state = mock(BlockState.class);
		Level level = mock(Level.class);
		Player player = mock(Player.class);
		BlockHitResult hitResult = mock(BlockHitResult.class);

		when(hitResult.getLocation()).thenReturn(new Vec3(0.5, 0.5, 0.5));
		when(stack.is(Blocks.VINE.asItem())).thenReturn(true);
		when(stack.is(Blocks.LADDER.asItem())).thenReturn(false);

		when(hitResult.getDirection()).thenReturn(Direction.NORTH);
		when(player.getDirection()).thenReturn(Direction.NORTH);
		when(directionUtil.horizontalOrElse(any(Direction.class), any(Direction.class))).thenReturn(Direction.NORTH);
		when(level.isClientSide()).thenReturn(false);

		InteractionResult result = callUseItemOn(stack, state, level, BlockPos.ZERO, player, InteractionHand.MAIN_HAND, hitResult);

		assertSame(InteractionResult.SUCCESS, result);

		verify(stack, times(1)).is(Blocks.VINE.asItem());
		ArgumentCaptor<BlockState> climbable = ArgumentCaptor.captor();
		verify(level, times(1)).setBlock(eq(BlockPos.ZERO), climbable.capture(), eq(Block.UPDATE_ALL));
		Object vineVariant = capturedGetValue(climbable.getValue(), ClimbableHollowLogBlock.VARIANT);
		assertSame(HollowLogVariants.Climbable.VINE, vineVariant);
		Object northFacing = capturedGetValue(climbable.getValue(), ClimbableHollowLogBlock.FACING);
		assertSame(Direction.NORTH, northFacing);
		verify(level, times(1)).playSound(isNull(), eq(BlockPos.ZERO), any(SoundEvent.class), eq(SoundSource.BLOCKS), anyFloat(), anyFloat());
		verify(stack, times(1)).consume(1, (LivingEntity) (Object) player);

		verify(stack, never()).is(Blocks.LADDER.asItem());
	}

	@Test
	public void useItemOnLadder() throws Exception {
		ItemStack stack = mock(ItemStack.class);
		BlockState state = mock(BlockState.class);
		Level level = mock(Level.class);
		Player player = mock(Player.class);
		BlockHitResult hitResult = mock(BlockHitResult.class);

		when(hitResult.getLocation()).thenReturn(new Vec3(0.5, 0.5, 0.5));
		when(stack.is(Blocks.VINE.asItem())).thenReturn(false);
		when(stack.is(Blocks.LADDER.asItem())).thenReturn(true);

		doReturn(false).when(state).getValue((Property<?>) (Object) VerticalHollowLogBlock.WATERLOGGED);
		when(hitResult.getDirection()).thenReturn(Direction.NORTH);
		when(player.getDirection()).thenReturn(Direction.NORTH);
		when(directionUtil.horizontalOrElse(any(Direction.class), any(Direction.class))).thenReturn(Direction.NORTH);
		when(level.isClientSide()).thenReturn(false);

		InteractionResult result = callUseItemOn(stack, state, level, BlockPos.ZERO, player, InteractionHand.MAIN_HAND, hitResult);

		assertSame(InteractionResult.SUCCESS, result);

		verify(stack, times(1)).is(Blocks.VINE.asItem());
		verify(stack, times(1)).is(Blocks.LADDER.asItem());
		ArgumentCaptor<BlockState> climbable = ArgumentCaptor.captor();
		verify(level, times(1)).setBlock(eq(BlockPos.ZERO), climbable.capture(), eq(Block.UPDATE_ALL));
		Object ladderVariant = capturedGetValue(climbable.getValue(), ClimbableHollowLogBlock.VARIANT);
		assertSame(HollowLogVariants.Climbable.LADDER, ladderVariant);
		Object northFacing = capturedGetValue(climbable.getValue(), ClimbableHollowLogBlock.FACING);
		assertSame(Direction.NORTH, northFacing);
		verify(level, times(1)).playSound(isNull(), eq(BlockPos.ZERO), any(SoundEvent.class), eq(SoundSource.BLOCKS), anyFloat(), anyFloat());
		verify(stack, times(1)).consume(1, (LivingEntity) (Object) player);
	}

	@Test
	public void useItemOnLadderWaterlogged() throws Exception {
		ItemStack stack = mock(ItemStack.class);
		BlockState state = mock(BlockState.class);
		Level level = mock(Level.class);
		Player player = mock(Player.class);
		BlockHitResult hitResult = mock(BlockHitResult.class);

		when(hitResult.getLocation()).thenReturn(new Vec3(0.5, 0.5, 0.5));
		when(stack.is(Blocks.VINE.asItem())).thenReturn(false);
		when(stack.is(Blocks.LADDER.asItem())).thenReturn(true);

		doReturn(true).when(state).getValue((Property<?>) (Object) VerticalHollowLogBlock.WATERLOGGED);
		when(hitResult.getDirection()).thenReturn(Direction.NORTH);
		when(player.getDirection()).thenReturn(Direction.NORTH);
		when(directionUtil.horizontalOrElse(any(Direction.class), any(Direction.class))).thenReturn(Direction.NORTH);
		when(level.isClientSide()).thenReturn(false);

		InteractionResult result = callUseItemOn(stack, state, level, BlockPos.ZERO, player, InteractionHand.MAIN_HAND, hitResult);

		assertSame(InteractionResult.SUCCESS, result);

		verify(stack, times(1)).is(Blocks.VINE.asItem());
		verify(stack, times(1)).is(Blocks.LADDER.asItem());
		ArgumentCaptor<BlockState> climbable = ArgumentCaptor.captor();
		verify(level, times(1)).setBlock(eq(BlockPos.ZERO), climbable.capture(), eq(Block.UPDATE_ALL));
		Object ladderWaterloggedVariant = capturedGetValue(climbable.getValue(), ClimbableHollowLogBlock.VARIANT);
		assertSame(HollowLogVariants.Climbable.LADDER_WATERLOGGED, ladderWaterloggedVariant);
		Object northFacing = capturedGetValue(climbable.getValue(), ClimbableHollowLogBlock.FACING);
		assertSame(Direction.NORTH, northFacing);
		verify(level, times(1)).playSound(isNull(), eq(BlockPos.ZERO), any(SoundEvent.class), eq(SoundSource.BLOCKS), anyFloat(), anyFloat());
		verify(stack, times(1)).consume(1, (LivingEntity) (Object) player);
	}

	private static Object capturedGetValue(BlockState state, Object property) {
		return state.getValue((Property<?>) property);
	}

}
