package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.trees.Tree;

public class BlockTFSapling extends SaplingBlock {

	protected BlockTFSapling(Tree tree) {
		super(tree, Properties.create(Material.PLANTS).hardnessAndResistance(0.0F).sound(SoundType.PLANT).doesNotBlockMovement().tickRandomly());
	}
}
