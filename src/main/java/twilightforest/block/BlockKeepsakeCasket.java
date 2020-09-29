package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.tileentity.TileEntityKeepsakeCasket;

import javax.annotation.Nullable;

public class BlockKeepsakeCasket extends ContainerBlock implements BlockLoggingEnum.IMultiLoggable {
    protected BlockKeepsakeCasket() {
        super(Block.Properties.create(Material.IRON, MaterialColor.BLACK).setRequiresTool().hardnessAndResistance(50.0F, 1200.0F).sound(SoundType.NETHERITE));
    }

    public BlockRenderType getRenderType(BlockState state) {
        return state.get(BlockLoggingEnum.MULTILOGGED) == BlockLoggingEnum.OBSIDIAN ? BlockRenderType.MODEL : BlockRenderType.INVISIBLE;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityKeepsakeCasket();
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.isIn(newState.getBlock())) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof IInventory) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            INamedContainerProvider inamedcontainerprovider = this.getContainer(state, worldIn, pos);

            if (inamedcontainerprovider != null) {
                player.openContainer(inamedcontainerprovider);
            }

            return ActionResultType.CONSUME;
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityKeepsakeCasket) {
                ((TileEntityKeepsakeCasket)tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }

    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockLoggingEnum.MULTILOGGED, BlockStateProperties.HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context)
                .with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite())
                .with(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.getFromFluid(context.getWorld().getFluidState(context.getPos()).getFluid()));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(BlockLoggingEnum.MULTILOGGED).getFluid().getDefaultState();
    }
}
