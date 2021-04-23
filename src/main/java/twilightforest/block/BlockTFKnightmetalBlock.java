package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import twilightforest.util.TFDamageSources;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTFKnightmetalBlock extends Block {

	private static final VoxelShape SHAPE = VoxelShapes.create(new AxisAlignedBB(1 / 16F, 1 / 16F, 1 / 16F, 15 / 16F, 15 / 16F, 15 / 16F));
	private static final float BLOCK_DAMAGE = 4;

	public BlockTFKnightmetalBlock(Properties props) {
		super(props);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Nullable
	@Override
	public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity) {
		return PathNodeType.DAMAGE_CACTUS;
	}

	@Override
	@Deprecated
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
		entity.attackEntityFrom(TFDamageSources.KNIGHTMETAL, BLOCK_DAMAGE);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("block.knightmetal.tooltip"));
	}
}
