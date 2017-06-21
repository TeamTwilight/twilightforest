package twilightforest.client.texture.render;

import net.minecraft.client.renderer.block.model.BakedQuad;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.render.AbstractTexture;
import team.chisel.ctm.client.util.Quad;
import twilightforest.client.texture.type.TextureTypeMCTM;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Texture_MCTM extends AbstractTexture<TextureTypeMCTM> {

	public Texture_MCTM(TextureTypeMCTM type, TextureInfo info) {
		super(type, info);
	}

	@Override
	public List<BakedQuad> transformQuad(BakedQuad bq, ITextureContext context, int quadGoal) {
		Quad quad = makeQuad(bq);
		if (context == null) {
			return Collections.singletonList(quad.transformUVs(sprites[0]).rebake());
		}

		Quad[] quads = quad.subdivide(4);

		int[] ctm = ((TextureContextMCTM) context).getCTM(bq.getFace()).getSubmapIndices();

		for (int i = 0; i < quads.length; i++) {
			Quad q = quads[i];
			if (q != null) {
				int ctmid = q.getUvs().normalize().getQuadrant();
				quads[i] = q.grow().transformUVs(sprites[ctm[ctmid] > 15 ? 0 : 1], CTMLogicReversed.uvs[ctm[ctmid]].normalize());
			}
		}
		return Arrays.stream(quads).filter(Objects::nonNull).map(q -> q.rebake()).collect(Collectors.toList());
	}
}