package twilightforest.world.components.structures;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.structure.StructureStart;

/**
 * Implement this interface to Structure classes, for influencing density-based chunkgen around a structure.
 * DensityFunction.FunctionContext objects provided to the returned DensityFunction will NOT be position-normalized to
 * the structure center.
 */
public interface CustomDensitySource {
    /**
     * Expect to make a new density functions for each chunk overlapping this Structure.
     * It will not process for any chunk outside the StructureStart's X-Z boundaries.
     *
     * @param chunkPosAt The current chunk overlapping this Structure.
     * @param structurePieceSource The specific Structure instance, represented by the StructureStart. It has plenty of information that distinguishes the structure in-world. Treat it as if it were an Entity meant for immutability and take care to not modify it nor call its property-changing methods.
     * @return A custom density function, which will be added to the Beardifier's original density value.
     */
    DensityFunction getStructureTerraformer(ChunkPos chunkPosAt, StructureStart structurePieceSource);
}
