package twilightforest.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import twilightforest.TFFeature;
import twilightforest.world.ChunkGeneratorTwilightBase;
import twilightforest.world.TFGenerationSettings;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class OutOfStructurePlacement extends Placement<NoPlacementConfig> {

	public OutOfStructurePlacement(Codec<NoPlacementConfig> p_i232086_1_) {
		super(p_i232086_1_);
	}

	@Override
	public Stream<BlockPos> getPositions(WorldDecoratingHelper worldDecoratingHelper, Random random, NoPlacementConfig noPlacementConfig, BlockPos blockPos) {
		if (worldDecoratingHelper.chunkGenerator instanceof ChunkGeneratorTwilightBase) {
			Optional<StructureStart<?>> struct = TFGenerationSettings.locateTFStructureInRange(worldDecoratingHelper.field_242889_a, blockPos, 0);
			if(struct.isPresent()) {
				StructureStart<?> structure = struct.get();
				if(structure.getBoundingBox().isVecInside(blockPos)) {
					TFFeature nearbyFeature = TFFeature.getFeatureAt(blockPos.getX(), blockPos.getZ(), worldDecoratingHelper.field_242889_a);

						// TODO: This is terrible but *works* for now.. proper solution is to figure out why the stronghold bounding box is going so high
						if (nearbyFeature == TFFeature.KNIGHT_STRONGHOLD && blockPos.getY() >= 33)
							return Stream.of(blockPos);

						return Stream.empty();
				}
			}
		}
		return Stream.of(blockPos);
	}
}
