package twilightforest.client.state.block;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import twilightforest.enums.BossVariant;

public class TrophyRenderState extends BlockEntityRenderState {

	public boolean wall;
	public float animationProgress;
	public Transformation transformation = Transformation.IDENTITY;
	public BossVariant variant = BossVariant.NAGA;
}
