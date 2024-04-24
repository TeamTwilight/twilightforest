package twilightforest.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFSounds;

import java.util.function.Consumer;

public class GlassSwordItem extends SwordItem {
	protected static final BlockParticleOption GLASS_PARTICLE = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_STAINED_GLASS.defaultBlockState());

	public GlassSwordItem(Tier toolMaterial, Properties properties) {
		super(toolMaterial, 3, -2.4F, properties);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (target.level() instanceof ServerLevel server) {
			for (int i = 0; i < 20; i++) {
				double px = target.getX() + target.getRandom().nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth();
				double py = target.getY() + target.getRandom().nextFloat() * target.getBbHeight();
				double pz = target.getZ() + target.getRandom().nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth();
				server.sendParticles(GLASS_PARTICLE, px, py, pz, 1, 0, 0, 0, 0);
			}
		}

		this.hurtAndBreak(stack, attacker, (user) -> {
			user.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), TFSounds.GLASS_SWORD_BREAK.get(), attacker.getSoundSource(), 1F, 0.5F);
			user.broadcastBreakEvent(InteractionHand.MAIN_HAND);
		});
		return true;
	}

	private <T extends LivingEntity> void hurtAndBreak(ItemStack stack, T entity, Consumer<T> onBroken) {
		if (!entity.level().isClientSide() && (!(entity instanceof Player player) || !player.getAbilities().instabuild)) {
			if (this.hurt(stack, entity.getRandom(), entity instanceof ServerPlayer sp ? sp : null)) {
				onBroken.accept(entity);
				stack.shrink(1);
				if (entity instanceof Player player) {
					player.awardStat(Stats.ITEM_BROKEN.get(this));
				}
			}
		}
	}

	private boolean hurt(ItemStack stack, RandomSource random, @Nullable ServerPlayer player) {
		if (stack.getTag() != null && stack.getTag().contains("TFInfiniteGlassSword")) {
			return false;
		} else {
			if (DigDurabilityEnchantment.shouldIgnoreDurabilityDrop(stack, Math.min(3, EnchantmentHelper.getTagEnchantmentLevel(Enchantments.UNBREAKING, stack)), random)) {
				return false;
			}

			if (player != null) {
				CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(player, stack, 0);
			}

			return true;
		}
	}

	@Override
	public int getDefaultTooltipHideFlags(ItemStack stack) {
		return stack.getTag() != null && stack.getTag().contains("TFInfiniteGlassSword") ? super.getDefaultTooltipHideFlags(stack) : ItemStack.TooltipPart.UNBREAKABLE.getMask();
	}
}