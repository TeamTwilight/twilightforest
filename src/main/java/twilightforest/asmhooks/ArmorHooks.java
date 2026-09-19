package twilightforest.asmhooks;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import tamaized.beanification.Autowired;
import twilightforest.util.ArmorUtil;

@SuppressWarnings({"JavadocReference", "unused"})
public class ArmorHooks {

	@Autowired
	private static ArmorUtil armorUtil;

	/**
	 * {@link twilightforest.asm.transformers.armor.ArmorVisibilityRenderingTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.entity.LivingEntity#getVisibilityPercent(Entity)}
	 * Targets: {@link net.minecraft.world.entity.LivingEntity.getArmorCoverPercentage()}
	 */
	public static float modifyArmorVisibility(float o, LivingEntity entity) {
		return o - armorUtil.getShroudedArmorPercentage(entity);
	}
}
