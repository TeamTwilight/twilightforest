package twilightforest.client.texture.type;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.ctx.TextureContextCTM;
import team.chisel.ctm.client.texture.render.TextureCTM;
import team.chisel.ctm.client.texture.type.TextureTypeCTM;
import twilightforest.client.texture.render.TextureContextCTMWest;

@TextureType("CTM_TF_WEST")
public class TextureTypeCTMWest extends TextureTypeCTM {

	@Override
	public ICTMTexture<? extends TextureTypeCTM> makeTexture(TextureInfo info) {
		return new TextureCTM<TextureTypeCTM>(this, info);
	}

	@Override
	public TextureContextCTM getBlockRenderContext(IBlockState state, IBlockAccess world, BlockPos pos, ICTMTexture<?> tex) {
		return new TextureContextCTMWest(state, world, pos, (TextureCTM<?>) tex);
	}
}