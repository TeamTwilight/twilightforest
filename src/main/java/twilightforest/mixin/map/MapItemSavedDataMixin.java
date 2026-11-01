package twilightforest.mixin.map;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.asmhooks.MapHooks;

import java.util.function.Predicate;

@Mixin(MapItemSavedData.class)
public abstract class MapItemSavedDataMixin {

	/** Une carte rangee dans les lunettes du Voyageur continue de se dessiner. */
	@Redirect(method = "tickCarriedBy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;contains(Ljava/util/function/Predicate;)Z"))
	private boolean twilightforest$updateMapsInGoggles(Inventory inventory, Predicate<ItemStack> predicate, Player player, ItemStack stack) {
		return MapHooks.updateMapsInGoggles(inventory.contains(predicate), stack, player);
	}
}
