package twilightforest.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;

public class BlockTFTrapDoor extends TrapDoorBlock {

    protected BlockTFTrapDoor(MaterialColor mapColor) {
        super(Properties.create(Material.WOOD, mapColor).hardnessAndResistance(3.0F).sound(SoundType.WOOD));
    }
}
