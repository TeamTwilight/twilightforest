package twilightforest.compat.jade;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import twilightforest.TwilightForestMod;
import twilightforest.block.ChiseledCanopyShelfBlock;
import twilightforest.block.entity.bookshelf.ChiseledCanopyShelfBlockEntity;
import twilightforest.init.TFBlocks;

import java.util.Optional;

public enum ChiseledBookshelfSpawnProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (accessor.getBlockState().is(TFBlocks.CHISELED_CANOPY_BOOKSHELF) && accessor.getBlockState().getValue(ChiseledCanopyShelfBlock.SPAWNER)) {
			if (accessor.getPlayer().isCreative()) {
				BlockEntity te = accessor.getBlockEntity();
				if (te instanceof ChiseledCanopyShelfBlockEntity shelf) {
					SpawnData logic = shelf.getSpawner().getNextSpawnData();
					if (logic != null) {
						ValueInput input = TagValueInput.create(ProblemReporter.DISCARDING, accessor.getLevel().registryAccess(), logic.entityToSpawn());
						Optional<EntityType<?>> type = EntityType.by(input);
						if (type.isPresent()) {
							Component name = Component.translatable("jade.spawner", accessor.getBlock().getName().getString(), type.get().getDescription().getString());
							tooltip.replace(Identifier.fromNamespaceAndPath("jade", "object_name"), IThemeHelper.get().title(name));
						}
					}
				}
			}
		}
	}

	@Override
	public Identifier getUid() {
		return TwilightForestMod.prefix("chiseled_bookshelf_spawner");
	}
}