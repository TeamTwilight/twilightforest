package twilightforest.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFDataMaps;
import twilightforest.util.entities.EntityUtil;

import javax.annotation.Nonnull;

public class TransformPowderItem extends Item {

	public TransformPowderItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (!target.isAlive()) {
			return InteractionResult.PASS;
		}

		return transformEntityIfPossible(target, player.getItemInHand(hand), !player.isCreative()) ? InteractionResult.SUCCESS : InteractionResult.PASS;
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {
		if (level.isClientSide()) {
			AABB area = this.getEffectAABB(player);

			// particle effect
			for (int i = 0; i < 30; i++) {
				level.addParticle(ParticleTypes.CRIT, area.minX + level.getRandom().nextFloat() * (area.maxX - area.minX),
					area.minY + level.getRandom().nextFloat() * (area.maxY - area.minY),
					area.minZ + level.getRandom().nextFloat() * (area.maxZ - area.minZ),
					0, 0, 0);
			}

		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
	}

	public static boolean transformEntityIfPossible(LivingEntity target, ItemStack powder, boolean shrinkStack) {
		//dont transform tamed animals that have owners
		if (target instanceof OwnableEntity ownable && ownable.getOwner() != null) return false;

		var datamap = target.getType().builtInRegistryHolder().getData(TFDataMaps.TRANSFORMATION_POWDER);

		if (datamap != null) {
			boolean flag = EntityUtil.convertEntity(target, datamap.result());
			if (flag && shrinkStack) {
				powder.shrink(1);
			}
			return flag;
		}
		return false;
	}

	private AABB getEffectAABB(Player player) {
		double range = 2.0D;
		double radius = 1.0D;
		Vec3 srcVec = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
		Vec3 lookVec = player.getLookAngle();
		Vec3 destVec = srcVec.add(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range);

		return new AABB(destVec.x() - radius, destVec.y() - radius, destVec.z() - radius, destVec.x() + radius, destVec.y() + radius, destVec.z() + radius);
	}
}