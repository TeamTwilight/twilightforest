package twilightforest.compat.top;

import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.items.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.block.ChiseledCanopyShelfBlock;
import twilightforest.block.entity.bookshelf.BookshelfSpawner;
import twilightforest.block.entity.bookshelf.ChiseledCanopyShelfBlockEntity;
import twilightforest.init.TFBlocks;

import java.util.Optional;

public enum ChiseledBookshelfSpawnProvider implements IProbeInfoProvider {

	INSTANCE;

	@Override
	public ResourceLocation getID() {
		return TwilightForestMod.prefix("chiseled_bookshelf_spawner");
	}

	@Override
	public void addProbeInfo(ProbeMode probeMode, IProbeInfo info, Player player, Level level, BlockState blockState, IProbeHitData data) {
		if (blockState.is(TFBlocks.CHISELED_CANOPY_BOOKSHELF) && blockState.getValue(ChiseledCanopyShelfBlock.SPAWNER)) {
			if (player.isHolding(ModItems.CREATIVE_PROBE) || player.isCreative()) {
				BlockEntity te = level.getBlockEntity(data.getPos());
				if (te instanceof ChiseledCanopyShelfBlockEntity shelf) {
					BookshelfSpawner logic = shelf.getSpawner();
					CompoundTag tag = logic.getNextSpawnData().entityToSpawn();
					Optional<EntityType<?>> optional = EntityType.by(tag);
					optional.ifPresent(type -> info.horizontal(info.defaultLayoutStyle()
							.alignment(ElementAlignment.ALIGN_CENTER))
						.text(CompoundText.create().style(TextStyleClass.LABEL).text("Mob: ").info(type.getDescriptionId())));
				}
			}
		}
	}
}
