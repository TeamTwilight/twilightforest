package twilightforest.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

import java.util.Arrays;
import java.util.function.Function;

public final class Codecs {
    public final static Codec<BlockPos> STRING_POS = Codec.STRING.comapFlatMap(Codecs::parseString2BlockPos, Vec3i::toShortString);
    public static final Codec<Direction> ONLY_HORIZONTAL = Direction.CODEC.comapFlatMap(direction -> direction.getAxis() != Direction.Axis.Y ? DataResult.success(direction) : DataResult.error("Horizontal direction only!", direction), Function.identity());

    private static DataResult<BlockPos> parseString2BlockPos(String string) {
        try {
            return Util.fixedSize(Arrays.stream(string.split(" *, *")).mapToInt(Integer::parseInt), 3).map(arr -> new BlockPos(arr[0], arr[1], arr[2]));
        } catch (Throwable e) {
            return DataResult.error(e.getMessage());
        }
    }

    private Codecs() {
    }
}
