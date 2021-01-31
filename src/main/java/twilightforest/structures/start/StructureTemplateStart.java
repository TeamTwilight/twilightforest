package twilightforest.structures.start;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructureStart;
import twilightforest.structures.StructureTFComponentTemplate;

import java.util.Random;

public abstract class StructureTemplateStart<T extends IFeatureConfig> extends StructureStart<T> {

	public StructureTemplateStart(Structure<T> p_i225876_1_, int p_i225876_2_, int p_i225876_3_, MutableBoundingBox p_i225876_4_, int p_i225876_5_, long p_i225876_6_) {
		super(p_i225876_1_, p_i225876_2_, p_i225876_3_, p_i225876_4_, p_i225876_5_, p_i225876_6_);
	}

	@Override
	public void func_230366_a_(ISeedReader p_230366_1_, StructureManager p_230366_2_, ChunkGenerator p_230366_3_, Random p_230366_4_, MutableBoundingBox p_230366_5_, ChunkPos p_230366_6_) {
		components.stream().filter(StructureTFComponentTemplate.class::isInstance).map(StructureTFComponentTemplate.class::cast).filter(component -> component.LAZY_TEMPLATE_LOADER != null).
				forEach(component -> component.LAZY_TEMPLATE_LOADER.run());
		super.func_230366_a_(p_230366_1_, p_230366_2_, p_230366_3_, p_230366_4_, p_230366_5_, p_230366_6_);
	}

}
