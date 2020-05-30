package twilightforest.structures.start;

import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;

import java.util.Random;

//TODO: Look, this really isn't going to work whatsoever
public abstract class StructureStartTFFeatureAbstract extends StructureStartTFAbstract {
    private static int NUM_LOCKS = 4;
    public boolean isConquered;
    public byte[] lockBytes = new byte[NUM_LOCKS];

    @Deprecated
    private TFFeature feature;

//    public StructureStartTFFeatureAbstract() {
//    }

    public StructureStartTFFeatureAbstract(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);

        this.isConquered = false;
        this.feature = feature;
    }

//    @Override
//    public void writeToNBT(CompoundNBT compound) {
//        super.writeToNBT(compound);
//
//        compound.putBoolean("Conquered", this.isConquered);
//        compound.putByteArray("Locks", this.lockBytes);
//
//        compound.putInt("FeatureID", this.feature.ordinal());
//    }
//
//    @Override
//    public void readFromNBT(CompoundNBT compound) {
//        super.readFromNBT(compound);
//
//        this.isConquered = compound.getBoolean("Conquered");
//        this.lockBytes = compound.getByteArray("Locks");
//
//        this.feature = TFFeature.getFeatureByID(compound.getInt("FeatureID"));
//    }

    public boolean isLocked(int lockIndex) {
        if (lockIndex < this.lockBytes.length) {
            TwilightForestMod.LOGGER.debug("Checking locks for lockIndex {}", lockIndex);

            for (int i = 0; i < this.lockBytes.length; i++) {
                TwilightForestMod.LOGGER.debug("Lock {} = {}", i, this.lockBytes[i]);
            }

            return this.lockBytes[lockIndex] != 0;

        } else {
            TwilightForestMod.LOGGER.warn("Current lock index {} is beyond array bounds {}", lockIndex, this.lockBytes.length);
            return false;
        }
    }

    //TODO: Does not exist
//    @Override
//    public boolean isSizeableStructure() {
//        return feature.isStructureEnabled;
//    }
}
