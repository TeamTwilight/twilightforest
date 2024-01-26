package twilightforest.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.HollowLogClimbable;
import twilightforest.block.HollowLogHorizontal;
import twilightforest.block.HollowLogVertical;

import java.util.Map;

public class HollowLogItem extends BlockItem {
    private final HollowLogHorizontal horizontalLog;
    private final HollowLogVertical verticalLog;
    private final HollowLogClimbable climbable;

    public HollowLogItem(DeferredHolder<Block, HollowLogHorizontal> horizontalLog, DeferredHolder<Block, HollowLogVertical> verticalLog, DeferredHolder<Block, HollowLogClimbable> climbable, Properties properties) {
        super(verticalLog.get(), properties);
        this.horizontalLog = horizontalLog.get();
        this.verticalLog = verticalLog.get();
        this.climbable = climbable.get();
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        return switch (context.getClickedFace().getAxis()) {
            case Y -> this.verticalLog.getStateForPlacement(context);
            case X, Z -> this.horizontalLog.getStateForPlacement(context);
        };
    }

    @Override
    public void registerBlocks(Map<Block, Item> blockItemMap, Item item) {
        super.registerBlocks(blockItemMap, item);
        blockItemMap.put(this.horizontalLog, item);
        blockItemMap.put(this.verticalLog, item);
        blockItemMap.put(this.climbable, item);
    }

    @Override
    public void removeFromBlockToItemMap(Map<Block, Item> blockItemMap, Item item) {
        super.removeFromBlockToItemMap(blockItemMap, item);
        blockItemMap.remove(this.horizontalLog);
        blockItemMap.remove(this.verticalLog);
        blockItemMap.remove(this.climbable);
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return 300;
    }
}