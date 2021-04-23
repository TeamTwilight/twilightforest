package twilightforest.worldgen.treeplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class BranchesConfig {
    public static final Codec<BranchesConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.intRange(0, 16).fieldOf("count_minimum").forGetter(o -> o.branchCount),
                    Codec.intRange(0, 16).fieldOf("random_add_count").orElse(0).forGetter(o -> o.randomAddBranches),
                    Codec.doubleRange(1, 24).fieldOf("length").forGetter(o -> o.length),
                    Codec.doubleRange(0, 16).fieldOf("random_add_length").orElse(0d).forGetter(o -> o.randomAddLength),
                    // Yaw - Float between 0 and 1 for the angle, where 0 is 0 degrees, .5 is 180 degrees and 1 and 360 degrees.
                    Codec.doubleRange(0, 0.5).fieldOf("spacing_yaw").orElse(0.3d).forGetter(o -> o.spacingYaw),
                    // Tilt - Float between 0 and 1 where 0 is straight up, 0.5 is straight out and 1 is straight down.
                    Codec.doubleRange(0, 1).fieldOf("downwards_pitch").orElse(0.2d).forGetter(o -> o.downwardsPitch)
            ).apply(instance, BranchesConfig::new)
    );

    public final int branchCount;
    public final int randomAddBranches;
    public final double length;
    public final double randomAddLength;
    public final double spacingYaw;
    public final double downwardsPitch;

    public BranchesConfig(int branchCount, int randomAddBranches, double length, double randomAddLength, double spacingYaw, double downwardsPitch) {
        this.branchCount = branchCount;
        this.randomAddBranches = randomAddBranches;
        this.length = length;
        this.randomAddLength = randomAddLength;
        this.spacingYaw = spacingYaw;
        this.downwardsPitch = downwardsPitch;
    }

    public BranchesConfig(int branchCount, double length) {
        this.branchCount = branchCount;
        this.randomAddBranches = 0;
        this.length = length;
        this.randomAddLength = 0d;
        this.spacingYaw = 0.3d;
        this.downwardsPitch = 0.2d;
    }

    /*public int getBranchCount() {
        return branchCount;
    }

    public int getRandomAddBranches() {
        return randomAddBranches;//.orElse(0);
    }

    public double getLength() {
        return length;
    }

    public double getRandomAddLength() {
        return randomAddLength;//.orElse(0d);
    }

    public double getBranchRadians() {
        return branchRadians;//.orElse(0.3d);
    }

    public double getBranchPitch() {
        return branchPitch;//.orElse(0.2d);
    }*/
}
