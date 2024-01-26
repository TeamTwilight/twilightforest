package twilightforest.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

// For making rectangular grids that are approximately-evenly spaced (floating-point -> integer rounding), even if re-sampled for a different chunk or general region
// Making a hexagonal pattern will require using two of these
// Positions are lazily generated, meaning no excess of positions are produced if terminated early
public class RectangleLatticeIterator<T> implements Iterator<T>, Iterable<T> {
    private final int yLevel, latticeStartX, latticeStartZ, latticeCountX, latticeCountZ;
    private final float xSpacing, zSpacing, xOffset, zOffset;
    private final TernaryIntegerFunction<T> converter;

    private int latticeX = 0, latticeZ = 0;

    public static RectangleLatticeIterator<BlockPos.MutableBlockPos> boundedGrid(BoundingBox chunkBounds, int yLevel, float xSpacing, float zSpacing, float xOffset, float zOffset) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        return new RectangleLatticeIterator<>(chunkBounds.minX(), chunkBounds.minZ(), chunkBounds.maxX(), chunkBounds.maxZ(), yLevel, xSpacing, zSpacing, xOffset, zOffset, mutableBlockPos::set);
    }

    public RectangleLatticeIterator(int minX, int minZ, int maxX, int maxZ, int yLevel, float xSpacing, float zSpacing, float xOffset, float zOffset, TernaryIntegerFunction<T> converter) {
        this.yLevel = yLevel;

        this.xSpacing = xSpacing;
        this.zSpacing = zSpacing;
        this.xOffset = xOffset;
        this.zOffset = zOffset;

        this.latticeStartX = getNearestStartLatticeIndex(this.xSpacing, minX - this.xOffset);
        this.latticeStartZ = getNearestStartLatticeIndex(this.zSpacing, minZ - this.zOffset);
        this.latticeCountX = getNearestEndLatticeIndex(this.xSpacing, (maxX + 0.999f) - this.xOffset) - this.latticeStartX + 1;
        this.latticeCountZ = getNearestEndLatticeIndex(this.zSpacing, (maxZ + 0.999f) - this.zOffset) - this.latticeStartZ + 1;

        this.converter = converter;
    }

    @Override
    public T next() {
        T ret = this.converter.apply(this.generateX(), this.yLevel, this.generateZ());

        // March downwards, then rightwards as each column is completed
        if (this.latticeZ + 1 < this.latticeCountZ) {
            this.latticeZ++;
        } else {
            this.latticeZ = 0;
            this.latticeX++;
        }

        return ret;
    }

    private int generateX() {
        return (int) (this.xOffset + (this.latticeStartX + this.latticeX) * this.xSpacing);
    }

    private int generateZ() {
        return (int) (this.zOffset + (this.latticeStartZ + this.latticeZ) * this.zSpacing);
    }

    @Override
    public boolean hasNext() {
        return this.latticeX < this.latticeCountX;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return this;
    }

    private static int getNearestStartLatticeIndex(float latticeSpacing, float i) {
        return Mth.floor((i + Mth.positiveModulo(-i, latticeSpacing)) / latticeSpacing);
    }

    private static int getNearestEndLatticeIndex(float latticeSpacing, float i) {
        return Mth.floor((i - Mth.positiveModulo(i, latticeSpacing)) / latticeSpacing);
    }

    @FunctionalInterface
    public interface TernaryIntegerFunction<T> {
        T apply(int x, int y, int z);
    }

    public record TriangularLatticeConfig(float spacing, float xOffset, float zOffset, float xSpacing, float zSpacing) {
        private static final Codec<TriangularLatticeConfig> VERBOSE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.floatRange(1, 32).fieldOf("spacing").forGetter(TriangularLatticeConfig::spacing),
                Codec.floatRange(1, 32).fieldOf("x_offset").forGetter(TriangularLatticeConfig::xOffset),
                Codec.floatRange(1, 32).fieldOf("z_offset").forGetter(TriangularLatticeConfig::zOffset),
                Codec.floatRange(1, 32).fieldOf("x_spacing").forGetter(TriangularLatticeConfig::xSpacing),
                Codec.floatRange(1, 32).fieldOf("z_spacing").forGetter(TriangularLatticeConfig::zSpacing)
        ).apply(instance, TriangularLatticeConfig::new));

        private static final Codec<TriangularLatticeConfig> OFFSET_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.floatRange(1, 32).fieldOf("spacing").forGetter(TriangularLatticeConfig::spacing),
                Codec.floatRange(1, 32).fieldOf("x_offset").forGetter(TriangularLatticeConfig::xOffset),
                Codec.floatRange(1, 32).fieldOf("z_offset").forGetter(TriangularLatticeConfig::zOffset)
        ).apply(instance, TriangularLatticeConfig::new));

        private static final Codec<TriangularLatticeConfig> SPACING_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.floatRange(1, 32).fieldOf("spacing").forGetter(TriangularLatticeConfig::spacing)
        ).apply(instance, TriangularLatticeConfig::new));

        public static final TriangularLatticeConfig DEFAULT = new TriangularLatticeConfig(3.5f);

        public static final Codec<TriangularLatticeConfig> CODEC = ExtraCodecs.withAlternative(VERBOSE_CODEC, ExtraCodecs.withAlternative(OFFSET_CODEC, ExtraCodecs.withAlternative(SPACING_CODEC, Codec.unit(DEFAULT))));

        public TriangularLatticeConfig(float spacing) {
            this(spacing, Mth.cos(Mth.PI/6f) * spacing, Mth.sin(Mth.PI/6f) * spacing);
        }

        public TriangularLatticeConfig(float spacing, float xOffset, float zOffset) {
            this(spacing, xOffset, zOffset, xOffset * 2f, spacing);
        }

        public static TriangularLatticeConfig fromNBT(CompoundTag tag) {
            float spacing = tag.getFloat("spacing");
            if (spacing <= 0.0000001) spacing = 3.5f;

            float xOffset = tag.contains("x_offset", 5) ? tag.getFloat("x_offset") : Mth.cos(Mth.PI/6f) * spacing;
            float zOffset = tag.contains("z_offset", 5) ? tag.getFloat("z_offset") : Mth.sin(Mth.PI/6f) * spacing;

            if (tag.contains("x_spacing", 5) || tag.contains("z_spacing", 5)) {
                return new TriangularLatticeConfig(spacing, xOffset, zOffset, tag.getFloat("x_spacing"), tag.getFloat("z_spacing"));
            } else {
                return new TriangularLatticeConfig(spacing, xOffset, zOffset);
            }
        }

        public ZippedIterator<BlockPos.MutableBlockPos> boundedGrid(BoundingBox chunkBounds, int yLevel) {
            return new ZippedIterator<>(RectangleLatticeIterator.boundedGrid(chunkBounds, yLevel, this.xSpacing, this.zSpacing, 0, 0), RectangleLatticeIterator.boundedGrid(chunkBounds, yLevel, this.xSpacing, this.zSpacing, this.xOffset, this.zOffset));
        }
    }
}
