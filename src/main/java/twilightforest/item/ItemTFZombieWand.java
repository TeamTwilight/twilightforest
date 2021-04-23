package twilightforest.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.potion.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.EntityTFLoyalZombie;
import twilightforest.entity.TFEntities;
import twilightforest.util.EntityUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemTFZombieWand extends Item {

	protected ItemTFZombieWand(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {

		ItemStack stack = player.getHeldItem(hand);

		if (stack.getDamage() == stack.getMaxDamage()) {
			return ActionResult.resultFail(stack);
		}

		if (!world.isRemote) {
			// what block is the player pointing at?
			BlockRayTraceResult blockray = EntityUtil.rayTrace(player, 20.0);

			if (blockray.getType() != RayTraceResult.Type.MISS) {
				EntityTFLoyalZombie zombie = TFEntities.loyal_zombie.create(world);
				Direction face = blockray.getFace();
				zombie.setPositionAndRotation(blockray.getPos().getX() + 0.5F + face.getXOffset(), blockray.getPos().getY() + face.getYOffset(), blockray.getPos().getZ() + 0.5F + face.getZOffset(), 1.0F, 1.0F);
				zombie.setTamed(true);
				zombie.setOwnerId(player.getUniqueID());
				zombie.addPotionEffect(new EffectInstance(Effects.STRENGTH, 1200, 1));
				world.addEntity(zombie);

				stack.attemptDamageItem(1, random, (ServerPlayerEntity) null);
			}
		}

		return ActionResult.resultFail(stack);
	}

	@Override
	public float getXpRepairRatio(ItemStack stack) {
		return 1f;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(new TranslationTextComponent("twilightforest.scepter_charges", stack.getMaxDamage() - stack.getDamage()));
	}
}
