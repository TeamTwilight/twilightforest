package twilightforest.client.renderer.block;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.block.KeepsakeCasketBlock;
import twilightforest.init.TFBlocks;

import java.util.List;

public class KeepsakeCasketRenderer<T extends BlockEntity & LidBlockEntity> extends SkullChestRenderer<T> {
	private static final List<ResourceLocation> CASKET_TEXTURES = List.of(
		TwilightForestMod.getModelTexture("casket/keepsake_casket_0.png"),
		TwilightForestMod.getModelTexture("casket/keepsake_casket_1.png"),
		TwilightForestMod.getModelTexture("casket/keepsake_casket_2.png")
	);

	public KeepsakeCasketRenderer(BlockEntityRendererProvider.Context context) {
		super(context, TFBlocks.KEEPSAKE_CASKET.get().defaultBlockState());
	}

	@NotNull
	@Override
	protected ResourceLocation getTextureLocation(BlockState blockstate) {
		return this.getTextureLocation(blockstate.getValue(KeepsakeCasketBlock.BREAKAGE));
	}

	@NotNull
	@Override
	public ResourceLocation getTextureLocation(int damage) {
		return CASKET_TEXTURES.get(Mth.clamp(damage, 0, CASKET_TEXTURES.size() - 1));
	}
}
