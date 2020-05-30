package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.UseAction;
import net.minecraft.item.Rarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.TFBlocks;
import twilightforest.util.WorldUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ItemTFCrumbleHorn extends Item {

	private static final int CHANCE_HARVEST = 20;
	private static final int CHANCE_CRUMBLE = 5;

	private final List<Pair<Predicate<BlockState>, UnaryOperator<BlockState>>> crumbleTransforms = new ArrayList<>();
	private final List<Predicate<BlockState>> harvestedStates = new ArrayList<>();

	ItemTFCrumbleHorn(Properties props) {
		super(props);
		this.addCrumbleTransforms();
	}

	private void addCrumbleTransforms() {
		addCrumble(() -> Blocks.STONE, () -> Blocks.COBBLESTONE.getDefaultState());
		addCrumble(() -> Blocks.STONE_BRICKS, () -> Blocks.CRACKED_STONE_BRICKS.getDefaultState());
		addCrumble(() -> TFBlocks.maze_stone_brick.get(), () -> TFBlocks.maze_stone_cracked.get().getDefaultState());
		addCrumble(() -> TFBlocks.underbrick.get(), () -> TFBlocks.underbrick_cracked.get().getDefaultState());
		addCrumble(() -> TFBlocks.tower_wood.get(), () -> TFBlocks.tower_wood_cracked.get().getDefaultState());
		addCrumble(() -> Blocks.COBBLESTONE, () -> Blocks.GRAVEL.getDefaultState());
		addCrumble(() -> Blocks.SANDSTONE, () -> Blocks.SAND.getDefaultState());
		addCrumble(() -> Blocks.RED_SANDSTONE, () -> Blocks.RED_SAND.getDefaultState());
		addCrumble(() -> Blocks.GRASS, () -> Blocks.DIRT.getDefaultState());
		addCrumble(() -> Blocks.MYCELIUM, () -> Blocks.DIRT.getDefaultState());
		addHarvest(() -> Blocks.GRAVEL);
		addHarvest(() -> Blocks.DIRT);
		addHarvest(() -> Blocks.SAND);
		addHarvest(() -> Blocks.CLAY);
	}

	private void addCrumble(Supplier<Block> block, Supplier<BlockState> result) {
		addCrumble(state -> state.getBlock() == block.get(), state -> result.get());
	}

	private void addCrumble(Predicate<BlockState> test, UnaryOperator<BlockState> transform) {
		crumbleTransforms.add(Pair.of(test, transform));
	}

	private void addHarvest(Supplier<Block> block) {
		addHarvest(state -> state.getBlock() == block.get());
	}

	private void addHarvest(Predicate<BlockState> test) {
		harvestedStates.add(test);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		player.setActiveHand(hand);
		player.playSound(SoundEvents.ENTITY_SHEEP_AMBIENT, 1.0F, 0.8F);
		return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity living, int count) {
		if (count > 10 && count % 5 == 0 && !living.world.isRemote) {
			int crumbled = doCrumble(stack, living.world, living);

			if (crumbled > 0) {
				stack.damageItem(crumbled, living, (user) -> user.sendBreakAnimation(living.getActiveHand()));
			}

			living.world.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.ENTITY_SHEEP_AMBIENT, living.getSoundCategory(), 1.0F, 0.8F);
		}
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
		return oldStack.getItem() == newStack.getItem();
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || newStack.getItem() != oldStack.getItem();
	}

	private int doCrumble(ItemStack stack, World world, LivingEntity living) {

		final double range = 3.0D;
		final double radius = 2.0D;

		Vec3d srcVec = new Vec3d(living.getX(), living.getY() + living.getEyeHeight(), living.getZ());
		Vec3d lookVec = living.getLookVec().scale(range);
		Vec3d destVec = srcVec.add(lookVec);

		AxisAlignedBB crumbleBox = new AxisAlignedBB(destVec.x - radius, destVec.y - radius, destVec.z - radius, destVec.x + radius, destVec.y + radius, destVec.z + radius);

		return crumbleBlocksInAABB(stack, world, living, crumbleBox);
	}

	private int crumbleBlocksInAABB(ItemStack stack, World world, LivingEntity living, AxisAlignedBB box) {
		int crumbled = 0;
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			if (crumbleBlock(stack, world, living, pos)) crumbled++;
		}
		return crumbled;
	}

	private boolean crumbleBlock(ItemStack stack, World world, LivingEntity living, BlockPos pos) {

		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if (block.isAir(state, world, pos)) return false;

		for (Pair<Predicate<BlockState>, UnaryOperator<BlockState>> transform : crumbleTransforms) {
			if (transform.getLeft().test(state) && world.rand.nextInt(CHANCE_CRUMBLE) == 0) {
				world.setBlockState(pos, transform.getRight().apply(state), 3);
				world.playEvent(2001, pos, Block.getStateId(state));

				postTrigger(living, stack, world, pos);

				return true;
			}
		}

		for (Predicate<BlockState> predicate : harvestedStates) {
			if (predicate.test(state) && world.rand.nextInt(CHANCE_HARVEST) == 0) {
				if (living instanceof PlayerEntity) {
					if (block.canHarvestBlock(state, world, pos, (PlayerEntity) living)) {
						world.removeBlock(pos, false);
						block.harvestBlock(world, (PlayerEntity) living, pos, state, world.getTileEntity(pos), ItemStack.EMPTY);
						world.playEvent(2001, pos, Block.getStateId(state));

						postTrigger(living, stack, world, pos);

						return true;
					}
				} else if (ForgeEventFactory.getMobGriefingEvent(world, living)) {
					world.destroyBlock(pos, true);

					postTrigger(living, stack, world, pos);

					return true;
				}
			}
		}

		return false;
	}

	private void postTrigger(LivingEntity living, ItemStack stack, World world, BlockPos pos) {
		if (living instanceof ServerPlayerEntity)
			TFAdvancements.ITEM_USE_TRIGGER.trigger((ServerPlayerEntity) living, stack, world, pos);
	}
}
