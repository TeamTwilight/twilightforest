package twilightforest.world.components.placements;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import twilightforest.world.registration.TwilightFeatures;

import java.util.Random;
import java.util.stream.Stream;

public class ChunkCenterModifier extends PlacementModifier {
	private static final ChunkCenterModifier INSTANCE = new ChunkCenterModifier();
	public static final Codec<ChunkCenterModifier> CODEC = Codec.unit(() -> INSTANCE);

	public static ChunkCenterModifier center() {
		return INSTANCE;
	}
	@Override
	public Stream<BlockPos> getPositions(PlacementContext ctx, Random random, BlockPos pos) {
		return Stream.of(new BlockPos((pos.getX() & 0xfffffff0) + 8, pos.getY(), (pos.getZ() & 0xfffffff0) + 8));
	}

	@Override
	public PlacementModifierType<?> type() {
		return TwilightFeatures.CHUNK_CENTERER.get();
	}
}
