package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asmhooks.ArmorHooks;

/**
 * L'etoffe de l'Empereur rend l'armure invisible.
 *
 * <p>Le coremod NeoForge falsifiait le test « cet objet est-il une armure ? » place en tete de la
 * methode. Tout le corps tient dans ce test, donc renoncer des l'entree revient exactement au
 * meme, sans dependre de la forme du bytecode.</p>
 */
@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin {

	@Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
	private void twilightforest$cancelArmorRendering(PoseStack poseStack, MultiBufferSource buffers, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<?> model, CallbackInfo ci) {
		if (!ArmorHooks.cancelArmorRendering(true, entity.getItemBySlot(slot))) {
			ci.cancel();
		}
	}
}
