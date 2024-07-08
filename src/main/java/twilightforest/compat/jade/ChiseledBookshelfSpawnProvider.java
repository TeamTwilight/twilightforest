package twilightforest.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import twilightforest.TwilightForestMod;
import twilightforest.block.ChiseledCanopyShelfBlock;
import twilightforest.block.entity.bookshelf.BookshelfSpawner;
import twilightforest.block.entity.bookshelf.ChiseledCanopyShelfBlockEntity;
import twilightforest.init.TFBlocks;

public enum ChiseledBookshelfSpawnProvider implements IBlockComponentProvider {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (accessor.getBlockState().is(TFBlocks.CHISELED_CANOPY_BOOKSHELF) && accessor.getBlockState().getValue(ChiseledCanopyShelfBlock.SPAWNER)) {
			if (accessor.getPlayer().isCreative()) {
				BlockEntity te = accessor.getBlockEntity();
				if (te instanceof ChiseledCanopyShelfBlockEntity shelf) {
					BookshelfSpawner logic = shelf.getSpawner();
					CompoundTag tag = logic.getNextSpawnData().entityToSpawn();
					EntityType.by(tag).ifPresent(type -> tooltip.replace(ResourceLocation.fromNamespaceAndPath("jade", "object_name"), IThemeHelper.get().title(Component.translatable("jade.spawner", accessor.getBlock().getName().getString(), type.getDescription().getString()))));
				}
			}
		}
	}

	@Override
	public ResourceLocation getUid() {
		return TwilightForestMod.prefix("chiseled_bookshelf_spawner");
	}
}
