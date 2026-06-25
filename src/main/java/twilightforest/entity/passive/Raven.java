package twilightforest.entity.passive;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import twilightforest.init.TFSounds;
import twilightforest.tags.TFItemTags;

public class Raven extends FlyingBird {

	public Raven(EntityType<? extends Raven> type, Level world) {
		super(type, world);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return FlyingBird.createMobAttributes()
			.add(Attributes.MAX_HEALTH, 10.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.2D)
			.add(Attributes.STEP_HEIGHT, 1.0D)
		.add(Attributes.TEMPT_RANGE, 10.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.RAVEN_CAW.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.RAVEN_SQUAWK.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.RAVEN_SQUAWK.get();
	}

	@Override
	public boolean isSpooked() {
		return this.getLastHurtByMob() != null;
	}

	@Override
	public TagKey<Item> getTemptItems() {
		return TFItemTags.RAVEN_TEMPT_ITEMS;
	}
}
