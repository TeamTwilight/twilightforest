package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nullable;
import java.util.List;

public class MiniatureStructureBlock extends Block {
    public MiniatureStructureBlock() {
        super(Properties.of(Material.BARRIER).requiresCorrectToolForDrops().strength(0.75F).noOcclusion());
    }
}
