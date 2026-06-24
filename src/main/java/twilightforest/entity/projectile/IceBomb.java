package twilightforest.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import twilightforest.enchantment.ApplyFrostedEffect;
import twilightforest.entity.boss.AlphaYeti;
import twilightforest.entity.monster.Yeti;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.init.TFParticleType;
import twilightforest.tags.TFBlockTags;

import java.util.List;

public class IceBomb extends TFThrowable {
	private int zoneTimer = 101;
	private boolean hasHit;

	public IceBomb(EntityType<? extends IceBomb> type, Level level) {
		super(type, level);
	}

	public IceBomb(Level level, LivingEntity thrower, ItemStack stack) {
		super(TFEntities.THROWN_ICE.get(), level, thrower, stack);
	}

	public IceBomb(Level level, Position pos, ItemStack stack) {
		super(TFEntities.THROWN_ICE.get(), level, pos.x(), pos.y(), pos.z(), stack);
	}

	@Override
	protected Item getDefaultItem() {
		return TFItems.ICE_BOMB.get();
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		this.setDeltaMovement(0.0D, 0.0D, 0.0D);
		this.hasHit = true;
		this.doTerrainEffects(2);
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		if (result.getEntity() instanceof LivingEntity entity && this.level() instanceof ServerLevel serverLevel) {
			this.inflictDamage(serverLevel, entity, 2);
		}
	}

	private void doTerrainEffects(int range) {
		int ix = Mth.floor(this.xOld);
		int iy = Mth.floor(this.yOld);
		int iz = Mth.floor(this.zOld);

		for (int x = -range; x <= range; x++) {
			for (int z = -range; z <= range; z++) {
				if (Math.abs(x) == range && Math.abs(z) == range) continue;
				for (int y = -range; y <= range; y++) {
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
			if (state.is(TFBlockTags.ICE_BOMB_REPLACEABLES)) {
				this.level().setBlock(pos, Blocks.SNOW.defaultBlockState().canSurvive(this.level(), pos) ? Blocks.SNOW.defaultBlockState() : Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
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
				this.level().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, new BlockPos(this.blockPosition()), Block.getId(Blocks.ICE.defaultBlockState()));
				this.discard();
			}
		} else {
			this.makeTrail(TFParticleType.SNOW_GUARDIAN.get(), this.getOwner() instanceof AlphaYeti ? 2 : 5);
		}
	}

	@Override
	public void makeTrail(ParticleOptions particle, double r, double g, double b, int amount) {
		for (int i = 0; i < amount; i++) {
			double dx = this.getX() + 0.5D * (this.random.nextDouble() - this.random.nextDouble());
			double dy = this.getY() + 0.5D * (this.random.nextDouble() - this.random.nextDouble()) + 0.5D;
			double dz = this.getZ() + 0.5D * (this.random.nextDouble() - this.random.nextDouble());
			this.level().addParticle(particle, dx, dy, dz, r, g, b);
		}
	}

	private void makeIceZone() {
		if (this.level().isClientSide()) {
			for (int i = 0; i < 16; i++) {
				double dx = this.getX() + (this.random.nextFloat() - this.random.nextFloat()) * 3.5F;
				double dy = this.getY() + (this.random.nextFloat() - this.random.nextFloat()) * 3.5F;
				double dz = this.getZ() + (this.random.nextFloat() - this.random.nextFloat()) * 3.5F;

				this.level().addParticle(TFParticleType.SNOW_GUARDIAN.get(), dx, dy, dz, 0, 0, 0);
			}
		} else {
			if (this.zoneTimer == 99) this.doTerrainEffects(3);
			if (this.zoneTimer % 20 == 0) this.hitNearbyEntities((ServerLevel) this.level());
		}
	}

	private void hitNearbyEntities(ServerLevel level) {
		List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3, 2, 3));

		for (LivingEntity entity : nearby) {
			if (entity != this.getOwner()) {
				if (entity instanceof Yeti) {
					// TODO: make "frozen yeti" entity?
					BlockPos pos = BlockPos.containing(entity.xOld, entity.yOld, entity.zOld);
					level.setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState());
					level.setBlockAndUpdate(pos.above(), Blocks.ICE.defaultBlockState());

					entity.discard();
				} else this.inflictDamage(level, entity, 1);
			}
		}
	}

	private void inflictDamage(ServerLevel level, LivingEntity entity, int dmgMultiplier) {
		if (!entity.is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
			entity.hurtServer(level, TFDamageTypes.getIndirectEntityDamageSource(this.level(), TFDamageTypes.FROZEN, this, this.getOwner()),
				(entity.is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES) ? 5.0F : 1.0F) * dmgMultiplier);
			ApplyFrostedEffect.doChillAuraEffect(entity, 100 * dmgMultiplier, 0, true);
		}
	}

	public BlockState getBlockState() {
		return Blocks.PACKED_ICE.defaultBlockState();
	}

	@Override
	protected double getDefaultGravity() {
		return this.hasHit ? 0F : 0.025F;
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("zone_timer", this.zoneTimer);
		output.putBoolean("has_hit", this.hasHit);
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.zoneTimer = input.getIntOr("zone_timer", 100);
		this.hasHit = input.getBooleanOr("has_hit", false);
	}
}
