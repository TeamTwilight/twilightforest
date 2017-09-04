package twilightforest.block;

import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import twilightforest.client.ModelRegisterCallback;

public class BlockTFHugeGloomBlock extends BlockHugeMushroom implements ModelRegisterCallback {

	public BlockTFHugeGloomBlock() {
		super(Material.WOOD, MapColor.ADOBE, TFBlocks.plantCave); // todo 1.9 mapcolor and ensure plant is initialized
		this.setHardness(0.2F);
		this.setSoundType(SoundType.WOOD);
		this.setLightLevel(5F / 16F);
	}
}