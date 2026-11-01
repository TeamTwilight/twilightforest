package twilightforest.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

/**
 * Acces aux inventaires exposes par les blocs et les entites.
 *
 * <p>NeoForge interroge une capability par position, avec un cache dedie ; Forge la demande a
 * l'objet lui-meme et rend un {@code LazyOptional}. Les deux se ramenent a « donne-moi l'inventaire
 * ou rien », ce que cette classe expose.</p>
 */
public final class ItemHandlers {
	private ItemHandlers() {}

	@Nullable
	public static IItemHandler block(Level level, BlockPos pos, @Nullable Direction side) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		return blockEntity == null ? null
				: blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, side).orElse(null);
	}

	@Nullable
	public static IItemHandler entity(Entity entity, @Nullable Direction side) {
		return entity.getCapability(ForgeCapabilities.ITEM_HANDLER, side).orElse(null);
	}
}
