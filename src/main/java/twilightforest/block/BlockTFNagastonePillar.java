package twilightforest.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;

/**
 * Created by Drullkus on 12/12/17.
 */
public class BlockTFNagastonePillar extends BlockTFDirectionalRotatedPillar {

    protected BlockTFNagastonePillar() {
        super(Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE));
        this.setDefaultState(this.stateContainer.getBaseState().with(AXIS, Direction.Axis.Y).with(REVERSED, false));
    }
}
