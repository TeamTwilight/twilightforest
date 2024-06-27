package twilightforest.asm;

import cpw.mods.modlauncher.api.ITransformer;
import net.neoforged.neoforgespi.coremod.ICoreMod;
import twilightforest.asm.transformers.armor.ArmorColorRenderingTransformer;
import twilightforest.asm.transformers.armor.ArmorVisibilityRenderingTransformer;
import twilightforest.asm.transformers.armor.CancelArmorRenderingTransformer;
import twilightforest.asm.transformers.book.ModifyWrittenBookNameTransformer;
import twilightforest.asm.transformers.chunk.ChunkStatusListTransformer;

import java.util.List;

public class TFCoreMod implements ICoreMod {
	@Override
	public Iterable<? extends ITransformer<?>> getTransformers() {
		return List.of(
			// armor
			new CancelArmorRenderingTransformer(),
			new ArmorColorRenderingTransformer(),
			new ArmorVisibilityRenderingTransformer(),

			// book
			new ModifyWrittenBookNameTransformer(),

			// chunk
			new ChunkStatusListTransformer()
		);
	}
}