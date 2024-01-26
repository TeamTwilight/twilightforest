package twilightforest.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.doubles.Double2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectSortedMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public final class Codecs {
    public static final Codec<BlockPos> STRING_POS = Codec.STRING.comapFlatMap(Codecs::parseString2BlockPos, Vec3i::toShortString);
    public static final Codec<Direction> ONLY_HORIZONTAL = Direction.CODEC.comapFlatMap(direction -> direction.getAxis() != Direction.Axis.Y ? DataResult.success(direction) : DataResult.error(() -> "Horizontal direction only!", direction), Function.identity());
    public static final Codec<Float> FLOAT_STRING = Codec.STRING.comapFlatMap(Codecs::parseString2Float, f -> Float.toString(f));
    public static final Codec<Double> DOUBLE_STRING = Codec.STRING.comapFlatMap(Codecs::parseString2Double, f -> Double.toString(f));

    public static final Codec<Climate.ParameterList<Holder<Biome>>> CLIMATE_SYSTEM = ExtraCodecs.nonEmptyList(RecordCodecBuilder.<Pair<Climate.ParameterPoint, Holder<Biome>>>create((instance) -> instance.group(Climate.ParameterPoint.CODEC.fieldOf("parameters").forGetter(Pair::getFirst), Biome.CODEC.fieldOf("biome").forGetter(Pair::getSecond)).apply(instance, Pair::of)).listOf()).xmap(Climate.ParameterList::new, Climate.ParameterList::values);

    public static <T> Codec<Float2ObjectSortedMap<T>> floatTreeCodec(Codec<T> elementCodec) {
        return Codec
                .compoundList(Codecs.FLOAT_STRING, elementCodec)
                .xmap(floatEList -> floatEList.stream().collect(Float2ObjectAVLTreeMap::new, (map, pair) -> map.put(pair.getFirst(), pair.getSecond()), Float2ObjectAVLTreeMap::putAll), map -> map.entrySet().stream().map(entry -> new Pair<>(entry.getKey(), entry.getValue())).toList());
    }

    public static <T> Codec<Double2ObjectSortedMap<T>> doubleTreeCodec(Codec<T> elementCodec) {
        return Codec
                .compoundList(Codecs.DOUBLE_STRING, elementCodec)
                .xmap(floatEList -> floatEList.stream().collect(Double2ObjectAVLTreeMap::new, (map, pair) -> map.put(pair.getFirst(), pair.getSecond()), Double2ObjectAVLTreeMap::putAll), map -> map.entrySet().stream().map(entry -> new Pair<>(entry.getKey(), entry.getValue())).toList());
    }

    private static DataResult<BlockPos> parseString2BlockPos(String string) {
        try {
            return Util.fixedSize(Arrays.stream(string.split(" *, *")).mapToInt(Integer::parseInt), 3).map(arr -> new BlockPos(arr[0], arr[1], arr[2]));
        } catch (Throwable e) {
            return DataResult.error(e::getMessage);
        }
    }

    private static DataResult<Float> parseString2Float(String string) {
        try {
            return DataResult.success(Float.valueOf(string));
        } catch (Throwable e) {
            return DataResult.error(e::getMessage);
        }
    }

    private static DataResult<Double> parseString2Double(String string) {
        try {
            return DataResult.success(Double.valueOf(string));
        } catch (Throwable e) {
            return DataResult.error(e::getMessage);
        }
    }

    public static <E> DataResult<Pair<E, E>> arrayToPair(List<E> list) {
        return Util.fixedSize(list, 2).map(l -> Pair.of(l.get(0), l.get(1)));
    }

    private Codecs() {}
}
