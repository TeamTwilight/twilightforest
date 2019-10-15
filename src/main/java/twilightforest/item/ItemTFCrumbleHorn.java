package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.BlockTFMazestone;
import twilightforest.block.BlockTFTowerWood;
import twilightforest.block.BlockTFUnderBrick;
import twilightforest.block.TFBlocks;
import twilightforest.enums.MazestoneVariant;
import twilightforest.enums.TowerWoodVariant;
import twilightforest.enums.UnderBrickVariant;
import twilightforest.util.WorldUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ItemTFCrumbleHorn extends ItemTF {

	private static final int CHANCE_HARVEST = 20;
	private static final int CHANCE_CRUMBLE = 5;

	private final List<Pair<Predicate<IBlockState>, UnaryOperator<IBlockState>>> crumbleTransforms = new ArrayList<>();
	private final List<Predicate<IBlockState>> harvestedStates = new ArrayList<>();

	ItemTFCrumbleHorn(EnumRarity rarity) {
		super(rarity);
		this.maxStackSize = 1;
		this.setMaxDamage(1024);
		this.addCrumbleTransforms();
	}

	private void addCrumbleTransforms() {
		addCrumble(() -> Blocks.STONE, () -> Blocks.COBBLESTONE.getDefaultState());
		addCrumble(state -> state.getBlock() == Blocks.STONEBRICK
						&& state.getValue(BlockStoneBrick.VARIANT) == BlockStoneBrick.EnumType.DEFAULT,
				state -> state.withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED)
		);
		addCrumble(state -> state.getBlock() == TFBlocks.maze_stone
						&& state.getValue(BlockTFMazestone.VARIANT) == MazestoneVariant.BRICK,
				state -> state.withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.CRACKED)
		);
		addCrumble(state -> state.getBlock() == TFBlocks.underbrick
						&& state.getValue(BlockTFUnderBrick.VARIANT) == UnderBrickVariant.NORMAL,
				state -> state.withProperty(BlockTFUnderBrick.VARIANT, UnderBrickVariant.CRACKED)
		);
		addCrumble(state -> state.getBlock() == TFBlocks.tower_wood
						&& state.getValue(BlockTFTowerWood.VARIANT) == TowerWoodVariant.PLAIN,
				state -> state.withProperty(BlockTFTowerWood.VARIANT, TowerWoodVariant.CRACKED)
		);
		addCrumble(() -> Blocks.COBBLESTONE, () -> Blocks.GRAVEL.getDefaultState());
		addCrumble(() -> Blocks.SANDSTONE, () -> Blocks.SAND.getDefaultState());
		addCrumble(() -> Blocks.RED_SANDSTONE,
				() -> Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND)
		);
		addCrumble(() -> Blocks.GRASS, () -> Blocks.DIRT.getDefaultState());
		addCrumble(() -> Blocks.MYCELIUM, () -> Blocks.DIRT.getDefaultState());

		addHarvest(() -> Blocks.GRAVEL);
		addHarvest(() -> Blocks.DIRT);
		addHarvest(() -> Blocks.SAND);
		addHarvest(() -> Blocks.CLAY);
	}

	private void addCrumble(Supplier<Block> block, Supplier<IBlockState> result) {
		addCrumble(state -> state.getBlock() == block.get(), state -> result.get());
	}

	private void addCrumble(Predicate<IBlockState> test, UnaryOperator<IBlockState> transform) {
		crumbleTransforms.add(Pair.of(test, transform));
	}

	private void addHarvest(Supplier<Block> block) {
		addHarvest(state -> state.getBlock() == block.get());
	}

	private void addHarvest(Predicate<IBlockState> test) {
		harvestedStates.add(test);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
		player.playSound(SoundEvents.ENTITY_SHEEP_AMBIENT, 1.0F, 0.8F);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase living, int count) {
		if (count > 10 && count % 5 == 0 && !living.world.isRemote) {
			int crumbled = doCrumble(stack, living.world, living);

			if (crumbled > 0) {
				stack.damageItem(crumbled, living);
			}

			living.world.playSound(null, living.posX, living.posY, living.posZ, SoundEvents.ENTITY_SHEEP_AMBIENT, living.getSoundCategory(), 1.0F, 0.8F);
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
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

	private int doCrumble(ItemStack stack, World world, EntityLivingBase living) {

		final double range = 3.0D;
		final double radius = 2.0D;

		Vec3d srcVec = new Vec3d(living.posX, living.posY + living.getEyeHeight(), living.posZ);
		Vec3d lookVec = living.getLookVec().scale(range);
		Vec3d destVec = srcVec.add(lookVec);

		AxisAlignedBB crumbleBox = new AxisAlignedBB(destVec.x - radius, destVec.y - radius, destVec.z - radius, destVec.x + radius, destVec.y + radius, destVec.z + radius);

		return crumbleBlocksInAABB(stack, world, living, crumbleBox);
	}

	private int crumbleBlocksInAABB(ItemStack stack, World world, EntityLivingBase living, AxisAlignedBB box) {
		int crumbled = 0;
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			if (crumbleBlock(stack, world, living, pos)) crumbled++;
		}
		return crumbled;
	}

	private boolean crumbleBlock(ItemStack stack, World world, EntityLivingBase living, BlockPos pos) {

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if (block.isAir(state, world, pos)) return false;

		for (Pair<Predicate<IBlockState>, UnaryOperator<IBlockState>> transform : crumbleTransforms) {
			if (transform.getLeft().test(state) && world.rand.nextInt(CHANCE_CRUMBLE) == 0) {
				world.setBlockState(pos, transform.getRight().apply(state), 3);
				world.playEvent(2001, pos, Block.getStateId(state));

				postTrigger(living, stack, world, pos);

				return true;
			}
		}

		for (Predicate<IBlockState> predicate : harvestedStates) {
			if (predicate.test(state) && world.rand.nextInt(CHANCE_HARVEST) == 0) {
				if (living instanceof EntityPlayer) {
					if (block.canHarvestBlock(world, pos, (EntityPlayer) living)) {
						world.setBlockToAir(pos);
						block.harvestBlock(world, (EntityPlayer) living, pos, state, world.getTileEntity(pos), ItemStack.EMPTY);
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

	private void postTrigger(EntityLivingBase living, ItemStack stack, World world, BlockPos pos) {
		if (living instanceof EntityPlayerMP)
			TFAdvancements.ITEM_USE_TRIGGER.trigger((EntityPlayerMP) living, stack, world, pos);
	}
}
