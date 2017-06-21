package twilightforest.client.texture.type;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.ctx.TextureContextCTM;
import team.chisel.ctm.client.texture.render.TextureCTM;
import team.chisel.ctm.client.texture.type.TextureTypeCTM;
import twilightforest.client.texture.render.TextureContextMCTM;

@TextureType("MCTM")
public class TextureTypeMCTM extends TextureTypeCTM {

	@Override
	public ICTMTexture<? extends TextureTypeCTM> makeTexture(TextureInfo info) {
		return new TextureCTM(this, info);
	}

	@Override
	public TextureContextCTM getBlockRenderContext(IBlockState state, IBlockAccess world, BlockPos pos, ICTMTexture<?> tex) {
		return new TextureContextMCTM(state, world, pos);
	}

	@Override
	public int getQuadsPerSide() {
		return 4;
	}

	@Override
	public int requiredTextures() {
		return 2;
	}

	@Override
	public ITextureContext getContextFromData(long data) {
		return new TextureContextMCTM(data);
	}
}