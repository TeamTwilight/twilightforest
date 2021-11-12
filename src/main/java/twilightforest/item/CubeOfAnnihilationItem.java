package twilightforest.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import twilightforest.entity.CubeOfAnnihilation;
import twilightforest.entity.TFEntities;

import javax.annotation.Nullable;
import java.util.UUID;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class CubeOfAnnihilationItem extends Item {

	private static final String THROWN_UUID_KEY = "cubeEntity";

	protected CubeOfAnnihilationItem(Properties props) {
		super(props);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity holder, int slot, boolean isSelected) {
		if (!world.isClientSide && getThrownUuid(stack) != null && getThrownEntity(world, stack) == null) {
			stack.getTag().remove(THROWN_UUID_KEY);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (getThrownUuid(stack) != null)
			return new InteractionResultHolder<>(InteractionResult.PASS, stack);

		if (!world.isClientSide) {
			CubeOfAnnihilation launchedCube = new CubeOfAnnihilation(TFEntities.CUBE_OF_ANNIHILATION, world, player);
			world.addFreshEntity(launchedCube);
			setThrownEntity(stack, launchedCube);
		}

		player.startUsingItem(hand);
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@Nullable
	protected static UUID getThrownUuid(ItemStack stack) {
		if (stack.hasTag() && stack.getTag().hasUUID(THROWN_UUID_KEY)) {
			return stack.getTag().getUUID(THROWN_UUID_KEY);
		}

		return null;
	}

	@Nullable
	private static CubeOfAnnihilation getThrownEntity(Level world, ItemStack stack) {
		if (world instanceof ServerLevel) {
			UUID id = getThrownUuid(stack);
			if (id != null) {
				Entity e = ((ServerLevel) world).getEntity(id);
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
