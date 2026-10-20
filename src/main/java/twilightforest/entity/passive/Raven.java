package twilightforest.entity.passive;

import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFSounds;
import twilightforest.inventory.InventoryUtil;
import twilightforest.loot.TFLootTables;
import twilightforest.tags.TFItemTags;

public class Raven extends FlyingBird {
	private static final EntityDataAccessor<Boolean> HAS_DROPPED_ITEM = SynchedEntityData.defineId(Raven.class, EntityDataSerializers.BOOLEAN);
	private static final int INTERACTION_FAIL_COOLDOWN = 100;
	private int failCooldown;

	public Raven(EntityType<? extends Raven> type, Level world) {
		super(type, world);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return FlyingBird.createAnimalAttributes()
			.add(Attributes.MAX_HEALTH, 10.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.2D)
			.add(Attributes.STEP_HEIGHT, 1.0D);
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide() && this.failCooldown > 0) {
			this.failCooldown--;
		}
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
		boolean canGive = !this.getHasDroppedItem() && player.getItemInHand(hand).isEmpty();

		if (!(this.level() instanceof ServerLevel level)) {
			return canGive ? InteractionResult.SUCCESS : InteractionResult.FAIL;
		}

		if (!canGive) {
			if (this.failCooldown == 0) {
				level.getChunkSource().sendToTrackingPlayers(this, new ClientboundSoundEntityPacket(TFSounds.RAVEN_SQUAWK, SoundSource.NEUTRAL, this, 1.0F, 1.0F, level.getRandom().nextLong()));
				this.failCooldown = INTERACTION_FAIL_COOLDOWN;
			}
			return InteractionResult.FAIL;
		}

		this.hurtServer(level, this.damageSources().playerAttack(player), 0.0F);
		this.dropFromEntityInteractLootTable(level, TFLootTables.RAVEN_INTERACTION, player, player.getItemInHand(hand), (_, stack) -> InventoryUtil.giveItemToPlayerAndPlaySound(player, stack));
		this.setHasDroppedItem(true);
		return InteractionResult.SUCCESS;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(HAS_DROPPED_ITEM, false);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putBoolean("HasDroppedItem", getHasDroppedItem());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setHasDroppedItem(input.getBooleanOr("HasDroppedItem", false));
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

	public boolean getHasDroppedItem() {
		return this.getEntityData().get(HAS_DROPPED_ITEM);
	}

	public void setHasDroppedItem(boolean hasDroppedItem) {
		this.getEntityData().set(HAS_DROPPED_ITEM, hasDroppedItem);
	}
}
