package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

public class BlockTFFluffyCloud extends Block implements ModelRegisterCallback {

	protected BlockTFFluffyCloud() {
		super(Material.PACKED_ICE);
		this.setSoundType(SoundType.CLOTH);
		this.setCreativeTab(TFItems.creativeTab);
		this.setHardness(0.8F);
	}

}
