package twilightforest.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TwilightForestMod;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class ItemTFEnderBow extends BowItem {
	private static final String KEY = "twilightforest:ender";

	public ItemTFEnderBow(Properties props) {
		super(props);
	}

	@SubscribeEvent
	public static void onHit(ProjectileImpactEvent.Arrow evt) {
		AbstractArrowEntity arrow = evt.getArrow();
		if (arrow.func_234616_v_() instanceof PlayerEntity
						&& evt.getRayTraceResult() instanceof EntityRayTraceResult
						&& ((EntityRayTraceResult) evt.getRayTraceResult()).getEntity() instanceof LivingEntity) {
			PlayerEntity player = (PlayerEntity) arrow.func_234616_v_();
			LivingEntity living = (LivingEntity) ((EntityRayTraceResult) evt.getRayTraceResult()).getEntity();

			if (arrow.getPersistentData().contains(KEY)) {
				double sourceX = player.getPosX(), sourceY = player.getPosY(), sourceZ = player.getPosZ();
				float sourceYaw = player.rotationYaw, sourcePitch = player.rotationPitch;

				player.rotationYaw = living.rotationYaw;
				player.rotationPitch = living.rotationPitch;
				player.setPositionAndUpdate(living.getPosX(), living.getPosY(), living.getPosZ());
				player.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);

				living.rotationYaw = sourceYaw;
				living.rotationPitch = sourcePitch;
				living.setPositionAndUpdate(sourceX, sourceY, sourceZ);
				living.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
			}
		}
	}

	@Override
	public AbstractArrowEntity customArrow(AbstractArrowEntity arrow) {
		arrow.getPersistentData().putBoolean(KEY, true);
		return arrow;
	}
}
