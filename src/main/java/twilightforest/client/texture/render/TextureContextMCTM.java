package twilightforest.client.texture.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.client.model.AbstractCTMBakedModel;
import team.chisel.ctm.client.texture.ctx.TextureContextCTM;
import team.chisel.ctm.client.util.RegionCache;

import javax.annotation.Nonnull;
import java.util.EnumMap;

public class TextureContextMCTM extends TextureContextCTM implements ITextureContext {

	private EnumMap<EnumFacing, CTMLogicReversed> ctmData = new EnumMap<>(EnumFacing.class);

	private long data;

	public TextureContextMCTM(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
		super(state, world, pos);
		ctmData.clear();

		world = new RegionCache(pos, 1, world);
		for (EnumFacing face : EnumFacing.VALUES) {
			CTMLogicReversed ctm = createCTM(state);
			ctm.createSubmapIndices(world, pos, face);
			ctmData.put(face, ctm);
			this.data |= ctm.serialized() << (face.ordinal() * 8);
		}
	}

	public TextureContextMCTM(long data) {
		super(data);
		ctmData.clear();

		this.data = data;
		for (EnumFacing face : EnumFacing.VALUES) {
			CTMLogicReversed ctm = createCTM(null); // FIXME
			ctm.createSubmapIndices(data, face);
			ctmData.put(face, ctm);
		}
	}

	protected CTMLogicReversed createCTM(@Nonnull IBlockState state) {
		IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
		if (model instanceof AbstractCTMBakedModel) {
			return (CTMLogicReversed) CTMLogicReversed.getInstance().ignoreStates(((AbstractCTMBakedModel) model).getModel().ignoreStates());
		}
		return CTMLogicReversed.getInstance();
	}

	public CTMLogicReversed getCTM(EnumFacing face) {
		return ctmData.get(face);
	}

	@Override
	public long getCompressedData() {
		return this.data;
	}
}