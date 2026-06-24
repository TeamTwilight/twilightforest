package twilightforest.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import twilightforest.entity.projectile.CubeOfAnnihilation;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFEntities;

import java.util.UUID;

public class CubeOfAnnihilationItem extends Item {

	public CubeOfAnnihilationItem(Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
		if (!level.isClientSide() && stack.get(TFDataComponents.THROWN_PROJECTILE) != null && getThrownEntity(level, stack) == null) {
			stack.remove(TFDataComponents.THROWN_PROJECTILE);
		}
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.get(TFDataComponents.THROWN_PROJECTILE) != null)
			return InteractionResult.PASS;

		if (!level.isClientSide()) {
			CubeOfAnnihilation launchedCube = new CubeOfAnnihilation(TFEntities.CUBE_OF_ANNIHILATION.get(), level, player, stack);
			level.addFreshEntity(launchedCube);
			stack.set(TFDataComponents.THROWN_PROJECTILE, launchedCube.getUUID());
		}

		player.startUsingItem(hand);
		return InteractionResult.SUCCESS;
	}

	@Nullable
	private static CubeOfAnnihilation getThrownEntity(Level level, ItemStack stack) {
		if (level instanceof ServerLevel server) {
			UUID id = stack.get(TFDataComponents.THROWN_PROJECTILE);
			if (id != null) {
				Entity e = server.getEntity(id);
				if (e instanceof CubeOfAnnihilation) {
					return (CubeOfAnnihilation) e;
				}
			}
		}

		return null;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 72000;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BLOCK;
	}
}