package twilightforest.enums;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum BanisterShape implements StringRepresentable {
    SHORT,
    TALL,
    CONNECTED;

    // FIXME Once Mojang fixes this bug that's already older than half the age of the game itself,
    //  we can re-look at readding these two variants. The models themselves expand behind the normal 0->16 Voxel range,
    //  so before re-adding these models PLEASE PLEASE ensure the lighting looks good and proper on them.
    //  https://bugs.mojang.com/browse/MC-84633
    //TILT_RIGHT,
    //TILT_LEFT;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
