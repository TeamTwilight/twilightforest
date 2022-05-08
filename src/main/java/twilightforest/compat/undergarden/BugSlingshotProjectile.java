package twilightforest.compat.undergarden;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import quek.undergarden.entity.projectile.slingshot.SlingshotProjectile;
import twilightforest.TFSounds;
import twilightforest.block.CritterBlock;

public abstract class BugSlingshotProjectile extends SlingshotProjectile {

	public BugSlingshotProjectile(EntityType<? extends ThrowableItemProjectile> type, Level level) {
		super(type, level);
	}

	public BugSlingshotProjectile(EntityType<? extends ThrowableItemProjectile> type, Level level, LivingEntity shooter) {
		super(type, shooter, level);
		this.setDropItem(false);
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if(!level.isClientSide() && ricochetTimes == 0) {
			BlockPos pos = result.getBlockPos();
			Direction direction = result.getDirection();
			if (this.getBug().setValue(CritterBlock.FACING, direction).canSurvive(this.level, pos.relative(direction)) && this.level.getBlockState(pos.relative(direction)).getMaterial().isReplaceable()) {
				this.level.setBlock(pos.relative(direction), this.getBug().setValue(CritterBlock.FACING, direction), 2);
			} else {
				ItemEntity squish = new ItemEntity(level, pos.relative(direction).getX(), pos.relative(direction).getY(), pos.relative(direction).getZ(), this.getSquishResult());
				level.playSound(null, pos, TFSounds.BUG_SQUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
				((ServerLevel)level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState()),
						pos.relative(direction).getX() + Mth.nextFloat(level.getRandom(), 0.25F, 0.75F),
						pos.relative(direction).getY(),
						pos.relative(direction).getZ() + Mth.nextFloat(level.getRandom(), 0.25F, 0.75F), 50,
						0.0D, 0.0D, 0.0D, 0);
				squish.spawnAtLocation(squish.getItem());
			}
		}
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
			living.hurt(new IndirectEntityDamageSource("arrow", this, null).damageHelmet().setProjectile(), 1.0F);
			level.playSound(null, result.getEntity().blockPosition(), TFSounds.BUG_SQUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
			this.discard();
		}
	}

	protected abstract BlockState getBug();

	protected abstract ItemStack getSquishResult();
}
