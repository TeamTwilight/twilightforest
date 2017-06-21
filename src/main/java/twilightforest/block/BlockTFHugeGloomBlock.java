package twilightforest.block;

import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockTFHugeGloomBlock extends BlockHugeMushroom {

	public BlockTFHugeGloomBlock() {
		super(Material.WOOD, MapColor.ADOBE, TFBlocks.plant); // todo 1.9 mapcolor and ensure plant is initialized
		this.setHardness(0.2F);
		this.setSoundType(SoundType.WOOD);
		this.setLightLevel(5F / 16F);
	}
}