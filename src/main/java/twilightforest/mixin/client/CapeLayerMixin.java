package twilightforest.mixin.client;

import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.asmhooks.ArmorHooks;

/**
 * La cape reste visible sous une elytre d'etoffe de l'Empereur.
 *
 * <p>Le jeu masque la cape des qu'un elytre occupe le torse. Le test est ici detourne : l'elytre
 * invisible ne masque plus rien.</p>
 */
@Mixin(CapeLayer.class)
public abstract class CapeLayerMixin {

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
	private boolean twilightforest$fixCapeRendering(ItemStack stack, Item item) {
		return ArmorHooks.fixCapeRendering(stack.is(item), stack);
	}
}
