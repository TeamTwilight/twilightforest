package twilightforest.entity.boss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.projectile.AbstractArrow;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFPart;
import twilightforest.init.TFSounds;

public class SnowQueenIceShield extends TFPart<SnowQueen> {

	public static final ResourceLocation RENDERER = TwilightForestMod.prefix("snowqueen_iceshield");

	public SnowQueenIceShield(SnowQueen parent) {
		super(parent);
		this.dimensions = EntityDimensions.scalable(0.75F, 0.75F);
	}

	@Override
	public ResourceLocation renderer() {
		return RENDERER;
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		if (source.getDirectEntity() instanceof AbstractArrow arrow && arrow.getPierceLevel() > 0) {
			return true;
		}

		this.playSound(TFSounds.SNOW_QUEEN_BREAK.get(), 1.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		return false;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}
}
