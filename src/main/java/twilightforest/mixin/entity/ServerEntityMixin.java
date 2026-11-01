package twilightforest.mixin.entity;

import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asmhooks.MultipartHooks;

@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {

	@Shadow
	@Final
	private Entity entity;

	/** Les creatures en plusieurs morceaux (hydre, lich…) synchronisent leurs parties. */
	@Inject(method = "sendDirtyEntityData", at = @At("HEAD"))
	private void twilightforest$sendDirtyEntityData(CallbackInfo ci) {
		MultipartHooks.sendDirtyEntityData(this.entity);
	}
}
