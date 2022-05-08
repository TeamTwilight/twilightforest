package twilightforest.world.components.chunkgenerators.warp;

/*
 * This generally exists as it did in 1.17. Used for modifiers such as Noodle Caves
 */
public interface NoiseModifier {
    NoiseModifier PASS = ((density, height, zWidth, xWidth) -> density);

    double modifyNoise(double density, int height, int zWidth, int xWidth);
}
