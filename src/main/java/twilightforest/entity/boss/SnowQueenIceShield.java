package twilightforest.entity.boss;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFPart;
import twilightforest.init.TFSounds;

public class SnowQueenIceShield extends TFPart<SnowQueen> {

	public static final Identifier RENDERER = TwilightForestMod.prefix("snowqueen_iceshield");

	public SnowQueenIceShield(SnowQueen parent) {
		super(parent);
		this.dimensions = EntityDimensions.scalable(0.75F, 0.75F);
	}

	@Override
	public Identifier renderer() {
		return RENDERER;
	}

	@Override
	public boolean hurtServer(ServerLevel server, DamageSource source, float amount) {
		if (source.getDirectEntity() instanceof AbstractArrow arrow && arrow.getPierceLevel() > 0) {
			return true;
		}

		this.playSound(TFSounds.SNOW_QUEEN_BREAK.get(), 1.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		return false;
	}

	@Override
	protected void readAdditionalSaveData(ValueInput compound) {

	}

	@Override
	protected void addAdditionalSaveData(ValueOutput compound) {

	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}
}
