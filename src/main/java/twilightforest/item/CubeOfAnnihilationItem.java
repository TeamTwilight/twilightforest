package twilightforest.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.projectile.CubeOfAnnihilation;
import twilightforest.init.TFEntities;

import java.util.UUID;

public class CubeOfAnnihilationItem extends Item {

	private static final String THROWN_UUID_KEY = "cubeEntity";

	public CubeOfAnnihilationItem(Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity holder, int slot, boolean isSelected) {
		if (!level.isClientSide() && getThrownUuid(stack) != null && getThrownEntity(level, stack) == null) {
			stack.getTag().remove(THROWN_UUID_KEY);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (getThrownUuid(stack) != null)
			return new InteractionResultHolder<>(InteractionResult.PASS, stack);

		if (!level.isClientSide()) {
			CubeOfAnnihilation launchedCube = new CubeOfAnnihilation(TFEntities.CUBE_OF_ANNIHILATION.get(), level, player, stack);
			level.addFreshEntity(launchedCube);
			setThrownEntity(stack, launchedCube);
		}

		player.startUsingItem(hand);
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@Nullable
	public static UUID getThrownUuid(ItemStack stack) {
		if (stack.hasTag() && stack.getTag().hasUUID(THROWN_UUID_KEY)) {
			return stack.getTag().getUUID(THROWN_UUID_KEY);
		}

		return null;
	}

	@Nullable
	private static CubeOfAnnihilation getThrownEntity(Level level, ItemStack stack) {
		if (level instanceof ServerLevel server) {
			UUID id = getThrownUuid(stack);
			if (id != null) {
				Entity e = server.getEntity(id);
				if (e instanceof CubeOfAnnihilation) {
					return (CubeOfAnnihilation) e;
				}
			}
		}

		return null;
	}

	private static void setThrownEntity(ItemStack stack, CubeOfAnnihilation cube) {
		if (!stack.hasTag()) {
			stack.setTag(new CompoundTag());
		}
		stack.getTag().putUUID(THROWN_UUID_KEY, cube.getUUID());
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BLOCK;
	}

	@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
		return true;
	}
}