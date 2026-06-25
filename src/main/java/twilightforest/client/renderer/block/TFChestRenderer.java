package twilightforest.client.renderer.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;

import java.util.EnumMap;
import java.util.Map;

public class TFChestRenderer<T extends ChestBlockEntity> extends ChestRenderer<T> {
	public static final Map<Block, EnumMap<ChestType, SpriteId>> MATERIALS;

	static {
		ImmutableMap.Builder<Block, EnumMap<ChestType, SpriteId>> builder = ImmutableMap.builder();

		builder.put(TFBlocks.TWILIGHT_OAK_CHEST.get(), chestMaterial("twilight_oak", "normal"));
		builder.put(TFBlocks.CANOPY_CHEST.get(), chestMaterial("canopy", "normal"));
		builder.put(TFBlocks.MANGROVE_CHEST.get(), chestMaterial("mangrove", "normal"));
		builder.put(TFBlocks.DARK_CHEST.get(), chestMaterial("darkwood", "normal"));
		builder.put(TFBlocks.TIME_CHEST.get(), chestMaterial("time", "normal"));
		builder.put(TFBlocks.TRANSFORMATION_CHEST.get(), chestMaterial("transformation", "normal"));
		builder.put(TFBlocks.MINING_CHEST.get(), chestMaterial("mining", "normal"));
		builder.put(TFBlocks.SORTING_CHEST.get(), chestMaterial("sorting", "normal"));

		builder.put(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(), chestMaterial("twilight_oak", "trapped"));
		builder.put(TFBlocks.CANOPY_TRAPPED_CHEST.get(), chestMaterial("canopy", "trapped"));
		builder.put(TFBlocks.MANGROVE_TRAPPED_CHEST.get(), chestMaterial("mangrove", "trapped"));
		builder.put(TFBlocks.DARK_TRAPPED_CHEST.get(), chestMaterial("darkwood", "trapped"));
		builder.put(TFBlocks.TIME_TRAPPED_CHEST.get(), chestMaterial("time", "trapped"));
		builder.put(TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(), chestMaterial("transformation", "trapped"));
		builder.put(TFBlocks.MINING_TRAPPED_CHEST.get(), chestMaterial("mining", "trapped"));
		builder.put(TFBlocks.SORTING_TRAPPED_CHEST.get(), chestMaterial("sorting", "trapped"));

		MATERIALS = builder.build();
	}

	public TFChestRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected @Nullable SpriteId getCustomSprite(T blockEntity, ChestRenderState renderState) {
		return MATERIALS.get(blockEntity.getBlockState().getBlock()).get(renderState.type);
	}

	private static EnumMap<ChestType, SpriteId> chestMaterial(String type, String suffix) {
		EnumMap<ChestType, SpriteId> map = new EnumMap<>(ChestType.class);
		map.put(ChestType.SINGLE, Sheets.CHEST_MAPPER.apply(TwilightForestMod.prefix(type + "/" + suffix)));
		map.put(ChestType.LEFT, Sheets.CHEST_MAPPER.apply(TwilightForestMod.prefix(type + "/" + suffix + "_left")));
		map.put(ChestType.RIGHT, Sheets.CHEST_MAPPER.apply(TwilightForestMod.prefix(type + "/" + suffix + "_right")));
		return map;
	}
}
