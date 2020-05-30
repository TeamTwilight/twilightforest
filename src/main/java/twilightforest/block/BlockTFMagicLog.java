package twilightforest.block;

import net.minecraft.block.LogBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class BlockTFMagicLog extends LogBlock {

	protected BlockTFMagicLog(MaterialColor topColor, MaterialColor sideColor) {
		super(topColor, Properties.create(Material.WOOD, sideColor).hardnessAndResistance(2.0F).sound(SoundType.WOOD));
	}
}
