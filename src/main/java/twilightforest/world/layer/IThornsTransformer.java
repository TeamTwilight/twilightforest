package twilightforest.world.layer;

import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;
import net.minecraft.world.gen.layer.traits.IDimOffset1Transformer;

/**
 * Works like ICastleTransformer, but has extra parameters for diagonal checks
 * TODO: Verify the logic
 */
public interface IThornsTransformer extends IAreaTransformer1, IDimOffset1Transformer {
	int apply(INoiseRandom noise, int north, int west, int south, int east, int middle, int nw, int sw, int se, int ne);

	@Override
	default int func_215728_a(IExtendedNoiseRandom<?> noise, IArea area, int width, int depth) {
		return this.apply(noise,
				area.getValue(this.func_215721_a(width + 1), this.func_215722_b(depth + 0)),
				area.getValue(this.func_215721_a(width + 2), this.func_215722_b(depth + 1)),
				area.getValue(this.func_215721_a(width + 1), this.func_215722_b(depth + 2)),
				area.getValue(this.func_215721_a(width + 0), this.func_215722_b(depth + 1)),
				area.getValue(this.func_215721_a(width + 1), this.func_215722_b(depth + 1)),
				area.getValue(this.func_215721_a(width + 2), this.func_215722_b(depth + 0)),
				area.getValue(this.func_215721_a(width + 2), this.func_215722_b(depth + 2)),
				area.getValue(this.func_215721_a(width + 0), this.func_215722_b(depth + 2)),
				area.getValue(this.func_215721_a(width + 0), this.func_215722_b(depth + 0))
		);
	}
}
