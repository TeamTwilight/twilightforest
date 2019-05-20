package twilightforest.enums;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum CompressedVariant implements IStringSerializable {

    IRONWOOD(Material.WOOD, SoundType.WOOD),
    FIERY(Material.IRON, SoundType.METAL, MapColor.BLACK_STAINED_HARDENED_CLAY),
    STEELLEAF(Material.LEAVES, SoundType.PLANT),
    ARCTIC_FUR(Material.CLOTH, SoundType.CLOTH),
    CARMINITE(Material.CLAY, SoundType.SLIME, MapColor.RED);

    public final Material material;
    public final SoundType soundType;
    public final MapColor mapColor;

    CompressedVariant(Material material, SoundType soundType) {
        this(material, soundType, material.getMaterialMapColor());
    }

    CompressedVariant(Material material, SoundType soundType, MapColor mapColor) {
        this.material = material;
        this.soundType = soundType;
        this.mapColor = mapColor;
    }

    @Override
    public String getName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
