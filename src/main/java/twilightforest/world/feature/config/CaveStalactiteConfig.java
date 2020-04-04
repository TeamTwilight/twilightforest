package twilightforest.world.feature.config;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class CaveStalactiteConfig implements IFeatureConfig {
	public BlockState blockState;
	public float sizeFactor;
	public int maxLength;
	public int minHeight;
	public boolean hang;

	public CaveStalactiteConfig(BlockState state, float size, int length, int height, boolean hang) {
		this.blockState = state;
		this.sizeFactor = size;
		this.maxLength = length;
		this.minHeight = height;
		this.hang = hang;
	}

	@Override
	public <T> Dynamic<T> serialize(DynamicOps<T> dynOps) {
		return new Dynamic<>(dynOps, dynOps.createMap(ImmutableMap.of(
				dynOps.createString("state"), BlockState.serialize(dynOps, this.blockState).getValue(),
				dynOps.createString("size_factor"), dynOps.createFloat(this.sizeFactor),
				dynOps.createString("max_length"), dynOps.createInt(this.maxLength),
				dynOps.createString("min_height"), dynOps.createInt(this.minHeight),
				dynOps.createString("hanging"), dynOps.createBoolean(this.hang))
		));
	}

	public static <T> CaveStalactiteConfig deserialize(Dynamic<T> config) {
		BlockState blockstate = config.get("state").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
		float size = config.get("size_factor").asFloat(0);
		int length = config.get("max_length").asInt(-1);
		int height = config.get("min_height").asInt(-1);
		boolean hang = config.get("hanging").asBoolean(false);
		return new CaveStalactiteConfig(blockstate, size, length, height, hang);
	}
}
