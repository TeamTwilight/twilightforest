package twilightforest.client.model.block.aurorablock;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.DelegateBakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;
import twilightforest.util.SimplexNoiseHelper;

import java.util.List;

public class NoiseVaryingModel extends DelegateBakedModel {
	private static final ModelProperty<Integer> VARIANT = new ModelProperty<>();
	private final BakedModel[] variants;

	public NoiseVaryingModel(BakedModel[] variants) {
		// First variation will propagate properties among other variants
		super(variants[0]);
		this.variants = variants;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
		if (extraData.has(VARIANT)) {
			@SuppressWarnings("DataFlowIssue") // Just checked above
			int variant = extraData.get(VARIANT);
			return this.variants[variant].getQuads(state, side, rand, extraData, renderType);
		}

		// Defer to our primary wrapped model
		return super.getQuads(state, side, rand, extraData, renderType);
	}

	@Override
	public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
		if (modelData.has(VARIANT))
			return modelData;

		return modelData.derive().with(VARIANT, SimplexNoiseHelper.calcVariant(pos, this.variants.length)).build();
	}
}
