package twilightforest.compat.undergarden;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import twilightforest.TFSounds;
import twilightforest.block.TFBlocks;
import twilightforest.compat.UndergardenCompat;

public class FireflySlingshotProjectile extends BugSlingshotProjectile {

	public FireflySlingshotProjectile(EntityType<? extends BugSlingshotProjectile> type, Level level) {
		super(type, level);
	}

	public FireflySlingshotProjectile(Level level, LivingEntity shooter) {
		super(UndergardenCompat.FIREFLY_SLINGSHOT.get(), level, shooter);
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		if (result.getEntity() instanceof LivingEntity living) {
			if (result.getEntity() instanceof Player player) {
				if (!player.hasItemInSlot(EquipmentSlot.HEAD)) {
					player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(this.getDefaultItem()));
					this.discard();
					return;
				}
			}
			living.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0, false, false, false));
			living.hurt(new IndirectEntityDamageSource("mob", this, null).damageHelmet().setProjectile(), 1.0F);
			level.playSound(null, result.getEntity().blockPosition(), TFSounds.BUG_SQUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
			this.discard();
		}
	}

	@Override
	protected Item getDefaultItem() {
		return TFBlocks.FIREFLY.get().asItem();
	}

	@Override
	protected BlockState getBug() {
		return TFBlocks.FIREFLY.get().defaultBlockState();
	}

	@Override
	protected ItemStack getSquishResult() {
		return Items.GLOWSTONE_DUST.getDefaultInstance();
	}
}
