package twilightforest.structures.start;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;

import java.util.Random;

public abstract class StructureStartTFFeatureAbstract extends StructureStartTFAbstract {
    private static int NUM_LOCKS = 4;
    public boolean isConquered;
    public byte[] lockBytes = new byte[NUM_LOCKS];

    @Deprecated
    private TFFeature feature;

    public StructureStartTFFeatureAbstract() {
    }

    public StructureStartTFFeatureAbstract(World world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);

        this.isConquered = false;
        this.feature = feature;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setBoolean("Conquered", this.isConquered);
        compound.setByteArray("Locks", this.lockBytes);

        compound.setInteger("FeatureID", this.feature.ordinal());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.isConquered = compound.getBoolean("Conquered");
        this.lockBytes = compound.getByteArray("Locks");

        this.feature = TFFeature.values()[compound.getInteger("FeatureID")];
    }

    public boolean isLocked(int lockIndex) {
        if (lockIndex < this.lockBytes.length) {
            TwilightForestMod.LOGGER.info("Checking locks for lockIndex " + lockIndex);

            for (int i = 0; i < this.lockBytes.length; i++)
                TwilightForestMod.LOGGER.info("Lock " + i + " = " + this.lockBytes[i]);

            return this.lockBytes[lockIndex] != 0;
        } else {
            TwilightForestMod.LOGGER.warn("Current lock index, " + lockIndex + " is beyond array bounds " + this.lockBytes.length);

            return false;
        }
    }

    @Override
    public boolean isSizeableStructure() {
        return feature.isStructureEnabled;
    }
}
