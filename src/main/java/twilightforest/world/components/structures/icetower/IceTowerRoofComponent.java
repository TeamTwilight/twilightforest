package twilightforest.world.components.structures.icetower;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.structures.lichtower.TowerRoofComponent;
import twilightforest.world.components.structures.lichtower.TowerWingComponent;

public class IceTowerRoofComponent extends TowerRoofComponent {

	public IceTowerRoofComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFITRoof.get(), nbt);
	}

	public IceTowerRoofComponent(int i, TowerWingComponent wing, int x, int y, int z) {
		super(TFStructurePieceTypes.TFITRoof.get(), i, x, y, z);

		// same alignment
		this.setOrientation(wing.getOrientation());
		// same size
		this.size = wing.size; // assuming only square towers and roofs right now.
		this.height = getRHeight(size, size);

		this.deco = wing.deco;

		// just hang out at the very top of the tower
		makeCapBB(wing);
	}

	/**
	 * Swoopy ice roof
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);
		for (int x = 0; x < this.size; x++) {
			for (int z = 0; z < this.size; z++) {
				//int rHeight = this.size - (int) MathHelper.sqrt_float(x * z); // interesting office building pattern
				int rHeight = getRHeight(x, z);
				//int rHeight = MathHelper.ceiling_float_int(Math.min(x * x / 9F, z * z / 9F));

				for (int y = 0; y < rHeight; y++) {
					this.placeBlock(world, deco.blockState, x, y, z, sbb);

				}
			}
		}
	}

	private static int getRHeight(int x, int z) {
		return Math.round(Mth.sqrt(x * x + z * z));
	}
}
