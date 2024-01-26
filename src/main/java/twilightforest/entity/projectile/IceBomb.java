package twilightforest.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.enchantment.ChillAuraEnchantment;
import twilightforest.entity.boss.AlphaYeti;
import twilightforest.entity.monster.Yeti;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEntities;

import java.util.List;

public class IceBomb extends TFThrowable {

	private int zoneTimer = 80;
	private boolean hasHit;

	public IceBomb(EntityType<? extends IceBomb> type, Level world) {
		super(type, world);
	}

	public IceBomb(EntityType<? extends IceBomb> type, Level world, LivingEntity thrower) {
		super(type, world, thrower);
	}

	public IceBomb(Level world, Position pos) {
		super(TFEntities.THROWN_ICE.get(), world, pos.x(), pos.y(), pos.z());
	}

	@Override
	protected void onHit(HitResult result) {
		this.setDeltaMovement(0.0D, 0.0D, 0.0D);
		this.hasHit = true;

		this.doTerrainEffects();
	}

	private void doTerrainEffects() {

		final int range = 3;

		int ix = Mth.floor(this.xOld);
		int iy = Mth.floor(this.yOld);
		int iz = Mth.floor(this.zOld);

		for (int x = -range; x <= range; x++) {
			for (int y = -range; y <= range; y++) {
				for (int z = -range; z <= range; z++) {
					BlockPos pos = new BlockPos(ix + x, iy + y, iz + z);
					this.doTerrainEffect(pos);
				}
			}
		}
	}

	/**
	 * Freeze water, put snow on snowable surfaces
	 */
	private void doTerrainEffect(BlockPos pos) {
		BlockState state = this.level().getBlockState(pos);
		if (!this.level().isClientSide()) {
			if (state.is(Blocks.WATER)) {
				this.level().setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState());
			}
			if (state == Blocks.LAVA.defaultBlockState()) {
				this.level().setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
			}
			if (this.level().isEmptyBlock(pos) && Blocks.SNOW.defaultBlockState().canSurvive(this.level(), pos)) {
				this.level().setBlockAndUpdate(pos, Blocks.SNOW.defaultBlockState());
			}
			if (state.is(BlockTagGenerator.ICE_BOMB_REPLACEABLES)) {
				this.level().setBlock(pos, Blocks.SNOW.defaultBlockState().canSurvive(this.level(), pos) ? Blocks.SNOW.defaultBlockState() : Blocks.AIR.defaultBlockState(), 3);
			}
			if (state.is(Blocks.SNOW) && state.getValue(SnowLayerBlock.LAYERS) < 8) {
				this.level().setBlockAndUpdate(pos, state.setValue(SnowLayerBlock.LAYERS, state.getValue(SnowLayerBlock.LAYERS) + 1));
			}
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (this.hasHit) {
			this.getDeltaMovement().multiply(0.1D, 0.1D, 0.1D);

			this.zoneTimer--;
			this.makeIceZone();

			if (!this.level().isClientSide() && this.zoneTimer <= 0) {
				this.level().levelEvent(2001, new BlockPos(this.blockPosition()), Block.getId(Blocks.ICE.defaultBlockState()));
				this.discard();
			}
		} else {
			this.makeTrail(new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SNOW.defaultBlockState()), this.getOwner() instanceof AlphaYeti ? 2 : 5);
		}
	}

	private void makeIceZone() {
		if (this.level().isClientSide()) {
			// sparkles
			BlockState stateId = Blocks.SNOW.defaultBlockState();
			for (int i = 0; i < 15; i++) {
				double dx = this.getX() + (this.random.nextFloat() - this.random.nextFloat()) * 3.0F;
				double dy = this.getY() + (this.random.nextFloat() - this.random.nextFloat()) * 3.0F;
				double dz = this.getZ() + (this.random.nextFloat() - this.random.nextFloat()) * 3.0F;

				this.level().addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, stateId), dx, dy, dz, 0, 0, 0);
			}
		} else {
			if (this.zoneTimer % 20 == 0) {
				this.hitNearbyEntities();
			}
		}
	}

	private void hitNearbyEntities() {
		List<LivingEntity> nearby = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3, 2, 3));

		for (LivingEntity entity : nearby) {
			if (entity != this.getOwner()) {
				if (entity instanceof Yeti) {
					// TODO: make "frozen yeti" entity?
					BlockPos pos = BlockPos.containing(entity.xOld, entity.yOld, entity.zOld);
					this.level().setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState());
					this.level().setBlockAndUpdate(pos.above(), Blocks.ICE.defaultBlockState());

					entity.discard();
				} else {
					if (!entity.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
						entity.hurt(TFDamageTypes.getIndirectEntityDamageSource(this.level(), TFDamageTypes.FROZEN, this, this.getOwner()), entity.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES) ? 5 : 1);
						ChillAuraEnchantment.doChillAuraEffect(entity, 100, 0, true);
					}
				}
			}
		}
	}

	public BlockState getBlockState() {
		return Blocks.PACKED_ICE.defaultBlockState();
	}

	@Override
	protected float getGravity() {
		return this.hasHit ? 0F : 0.025F;
	}
}
