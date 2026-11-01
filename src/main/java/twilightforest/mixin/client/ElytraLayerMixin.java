package twilightforest.mixin.client;

import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.asmhooks.ArmorHooks;

/**
 * L'etoffe de l'Empereur rend l'elytre invisible.
 *
 * <p>Forge pose la decision {@code shouldRender} sur une interface d'extension, et la version de
 * Mixin qu'il embarque refuse d'injecter dans une interface. On detourne donc l'appel que la
 * couche de rendu fait a cette methode : c'est le seul du jeu.</p>
 */
@Mixin(ElytraLayer.class)
public abstract class ElytraLayerMixin {

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/ElytraLayer;shouldRender(Lnet/minecraft/world/item/ItemStack;Ljava/lang/Object;)Z", remap = false))
	private boolean twilightforest$cancelElytraRendering(ElytraLayer layer, ItemStack stack, Object entity) {
		return ArmorHooks.cancelArmorRendering(layer.shouldRender(stack, entity), stack);
	}
}
