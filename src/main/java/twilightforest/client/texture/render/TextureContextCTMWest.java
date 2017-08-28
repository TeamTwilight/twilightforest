package twilightforest.client.texture.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.client.model.AbstractCTMBakedModel;
import team.chisel.ctm.client.texture.ctx.TextureContextCTM;
import team.chisel.ctm.client.texture.render.TextureCTM;
import team.chisel.ctm.client.util.CTMLogic;

import javax.annotation.Nonnull;

public class TextureContextCTMWest extends TextureContextCTM implements ITextureContext {

	public TextureContextCTMWest(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos, TextureCTM<?> tex) {
		super(state, world, pos, tex);
	}

	protected CTMLogic createCTM(@Nonnull IBlockState state) {
		IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
		if (model instanceof AbstractCTMBakedModel) {
			return CTMLogicWest.getInstance().ignoreStates(((AbstractCTMBakedModel) model).getModel().ignoreStates());
		}
		return CTMLogicWest.getInstance();
	}
}