package twilightforest.client.model.block;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import org.jetbrains.annotations.NotNull;
import twilightforest.block.entity.ReactorDebrisBlockEntity;
import twilightforest.client.renderer.block.ReactorDebrisRenderer;

import java.util.List;

public class ReactorDebrisModel implements DynamicBlockStateModel {

	private final BlockStateModel wrappedModel;

	public ReactorDebrisModel(BlockStateModel wrappedModel) {
		this.wrappedModel = wrappedModel;
	}

	@Override
	public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		// Determine the particle texture from the block entity
		Identifier textureForParticle = ReactorDebrisBlockEntity.DEFAULT_TEXTURE;
		if (level.getBlockEntity(pos) instanceof ReactorDebrisBlockEntity reactorDebrisBlockEntity
			&& level instanceof ClientLevel clientLevel) {
			textureForParticle = reactorDebrisBlockEntity.textures[clientLevel.getRandom().nextInt(reactorDebrisBlockEntity.textures.length)];
		}
		Material.Baked particleMaterial = new Material.Baked(ReactorDebrisRenderer.getSprite(textureForParticle), false);

		// Delegate to the wrapped model
		this.wrappedModel.collectParts(level, pos, state, random, parts);
	}

	@Override
	@Deprecated
	public void collectParts(RandomSource random, @NotNull List<BlockStateModelPart> parts) {
		this.wrappedModel.collectParts(random, parts);
	}

	@Override
	public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.wrappedModel.particleMaterial(level, pos, state);
	}

	@Override
	@Deprecated
	public Material.Baked particleMaterial() {
		return this.wrappedModel.particleMaterial();
	}

	@Override
	public int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.wrappedModel.materialFlags(level, pos, state);
	}

	@Override
	@Deprecated
	public int materialFlags() {
		return this.wrappedModel.materialFlags();
	}
}
