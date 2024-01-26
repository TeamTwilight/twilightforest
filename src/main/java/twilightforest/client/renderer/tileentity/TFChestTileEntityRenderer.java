package twilightforest.client.renderer.tileentity;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;

import java.util.EnumMap;
import java.util.Map;

public class TFChestTileEntityRenderer<T extends ChestBlockEntity> extends ChestRenderer<T> {
    public static final Map<Block, EnumMap<ChestType, Material>> MATERIALS;

    static {
        ImmutableMap.Builder<Block, EnumMap<ChestType, Material>> builder = ImmutableMap.builder();

        builder.put(TFBlocks.TWILIGHT_OAK_CHEST.get(), chestMaterial("twilight", false));
        builder.put(TFBlocks.CANOPY_CHEST.get(), chestMaterial("canopy", false));
        builder.put(TFBlocks.MANGROVE_CHEST.get(), chestMaterial("mangrove", false));
        builder.put(TFBlocks.DARK_CHEST.get(), chestMaterial("darkwood", false));
        builder.put(TFBlocks.TIME_CHEST.get(), chestMaterial("time", false));
        builder.put(TFBlocks.TRANSFORMATION_CHEST.get(), chestMaterial("transformation", false));
        builder.put(TFBlocks.MINING_CHEST.get(), chestMaterial("mining", false));
        builder.put(TFBlocks.SORTING_CHEST.get(), chestMaterial("sorting", false));

        builder.put(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(), chestMaterial("twilight", true));
        builder.put(TFBlocks.CANOPY_TRAPPED_CHEST.get(), chestMaterial("canopy", true));
        builder.put(TFBlocks.MANGROVE_TRAPPED_CHEST.get(), chestMaterial("mangrove", true));
        builder.put(TFBlocks.DARK_TRAPPED_CHEST.get(), chestMaterial("darkwood", true));
        builder.put(TFBlocks.TIME_TRAPPED_CHEST.get(), chestMaterial("time", true));
        builder.put(TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(), chestMaterial("transformation", true));
        builder.put(TFBlocks.MINING_TRAPPED_CHEST.get(), chestMaterial("mining", true));
        builder.put(TFBlocks.SORTING_TRAPPED_CHEST.get(), chestMaterial("sorting", true));

        MATERIALS = builder.build();
    }

    public TFChestTileEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Material getMaterial(T blockEntity, ChestType chestType) {
        EnumMap<ChestType, Material> b = MATERIALS.get(blockEntity.getBlockState().getBlock());

        if (b == null) return super.getMaterial(blockEntity, chestType);

        Material material = b.get(chestType);

        return material != null ? material : super.getMaterial(blockEntity, chestType);
    }

    private static EnumMap<ChestType, Material> chestMaterial(String type, boolean trapped) {
        EnumMap<ChestType, Material> map = new EnumMap<>(ChestType.class);

        map.put(ChestType.SINGLE, new Material(Sheets.CHEST_SHEET, TwilightForestMod.prefix("entity/chest/" + type + "/" + (trapped ? "trapped" : "single"))));
        map.put(ChestType.LEFT, new Material(Sheets.CHEST_SHEET, TwilightForestMod.prefix("entity/chest/" + type + "/" + (trapped ? "trapped_left" : "left"))));
        map.put(ChestType.RIGHT, new Material(Sheets.CHEST_SHEET, TwilightForestMod.prefix("entity/chest/" + type + "/" + (trapped ? "trapped_right" : "right"))));

        return map;
    }
}
