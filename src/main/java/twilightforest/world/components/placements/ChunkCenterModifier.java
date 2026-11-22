package twilightforest.world.components.placements;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.function.Consumer;

public class ChunkCenterModifier implements PlacementModifier {

	private static final ChunkCenterModifier INSTANCE = new ChunkCenterModifier();
	public static final MapCodec<ChunkCenterModifier> CODEC = MapCodec.unit(() -> INSTANCE);

	public static ChunkCenterModifier center() {
		return INSTANCE;
	}

	@Override
	public void modify(PlacementContext placementContext, RandomSource randomSource, BlockPos blockPos, Consumer<BlockPos> consumer) {
		consumer.accept(new BlockPos((blockPos.getX() & 0xfffffff0) + 8, blockPos.getY(), (blockPos.getZ() & 0xfffffff0) + 8));
	}

	@Override
	public MapCodec<? extends PlacementModifier> codec() {
		return CODEC;
	}
}
