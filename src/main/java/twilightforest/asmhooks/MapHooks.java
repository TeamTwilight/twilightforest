package twilightforest.asmhooks;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.Nullable;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.util.WorldUtil;

import java.util.function.Predicate;

@SuppressWarnings({"JavadocReference", "unused"})
public class MapHooks {

	/**
	 * {@link twilightforest.asm.transformers.map.ResolveNearestNonRandomSpreadMapStructureTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.chunk.ChunkGenerator#findNearestMapStructure(ServerLevel, HolderSet, BlockPos, int, boolean)}
	 */
	@Nullable
	public static Pair<BlockPos, Holder<Structure>> resolveNearestNonRandomSpreadMapStructure(@Nullable Pair<BlockPos, Holder<Structure>> o, ServerLevel level, HolderSet<Structure> targetStructures, BlockPos pos, int searchRadius, boolean skipKnownStructures) {
		return WorldUtil.findNearestMapLandmark(level, targetStructures, pos, searchRadius, skipKnownStructures).orElse(o);
	}

	/**
	 * {@link twilightforest.asm.transformers.map.UpdateMapsInGogglesTransformer}
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.saveddata.maps.MapItemSavedData#tickCarriedBy(Player, ItemStack)}<br/>
	 * Targets: {@link net.minecraft.world.entity.player.Inventory.contains( Predicate )}
	 */
	public static boolean updateMapsInGoggles(boolean o, ItemStack stack, Player player) {
		if (o) return true;
		ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
		if (!TravellersModifiersManager.isModifierActive(player, headStack, TravellersModifiersManager.ITEM_DISPLAY_MODIFIER)) return false;
		ItemDisplayContents contents = headStack.get(TFDataComponents.ITEM_DISPLAY);
		if (contents == null || contents.isEmpty()) return false;
		NonNullList<ItemStack> items = contents.items();
		int activeMapSlot = contents.findActiveMapSlot();
		if (activeMapSlot < 0) return false;

		ItemStack map = items.get(activeMapSlot);
		return !map.isEmpty() && ItemStack.isSameItemSameComponents(stack, map);
	}
}
