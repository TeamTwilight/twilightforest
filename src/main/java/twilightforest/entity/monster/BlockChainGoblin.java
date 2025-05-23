package twilightforest.entity.monster;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.SpikeBlock;
import twilightforest.entity.TFPart;
import twilightforest.entity.ai.goal.AvoidAnyEntityGoal;
import twilightforest.entity.ai.goal.ThrowSpikeBlockGoal;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFSounds;
import twilightforest.util.entities.EntityUtil;

import java.util.List;

public class BlockChainGoblin extends Monster {
	//this is here but its never been used
	//private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
	//private static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "speedPenalty", -0.25D, AttributeModifier.Operation.ADDITION);

	private static final float CHAIN_SPEED = 16F;
	private static final EntityDataAccessor<Byte> DATA_CHAINLENGTH = SynchedEntityData.defineId(BlockChainGoblin.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> DATA_CHAINPOS = SynchedEntityData.defineId(BlockChainGoblin.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Boolean> IS_THROWING = SynchedEntityData.defineId(BlockChainGoblin.class, EntityDataSerializers.BOOLEAN);

	private int recoilCounter;
	private float chainAngle;

	private float chainMoveLength;

	public final SpikeBlock block = new SpikeBlock(this);

	private final MultipartGenericsAreDumb[] partsArray;

	public BlockChainGoblin(EntityType<? extends BlockChainGoblin> type, Level level) {
		super(type, level);
		this.partsArray = new MultipartGenericsAreDumb[]{this.block};
	}

	public static abstract class MultipartGenericsAreDumb extends TFPart<Entity> {

		public MultipartGenericsAreDumb(Entity parent) {
			super(parent);
		}
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new AvoidAnyEntityGoal<>(this, PrimedTnt.class, 2.0F, 1.0F, 2.0F));
		this.goalSelector.addGoal(4, new ThrowSpikeBlockGoal(this, this.block));
		this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0F, false));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_CHAINLENGTH, (byte) 0);
		builder.define(DATA_CHAINPOS, (byte) 0);
		builder.define(IS_THROWING, false);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 20.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.28D)
			.add(Attributes.ATTACK_DAMAGE, 8.0D)
			.add(Attributes.ARMOR, 11.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.BLOCKCHAIN_GOBLIN_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.BLOCKCHAIN_GOBLIN_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.BLOCKCHAIN_GOBLIN_DEATH.get();
	}

	/**
	 * How high is the chain
	 */
	public double getChainYOffset() {
		return 1.5D - this.getChainLength() / 4.0D;
	}

	/**
	 * Get the block & chain position
	 */
	public Vec3 getChainPosition() {
		return this.getChainPosition(this.getChainAngle(), this.getChainLength());
	}

	/**
	 * Get the block & chain position
	 */
	public Vec3 getChainPosition(float angle, float distance) {
		double dx = Math.cos((angle) * Math.PI / 180.0D) * distance;
		double dz = Math.sin((angle) * Math.PI / 180.0D) * distance;

		return new Vec3(this.getX() + dx, this.getY() + this.getChainYOffset(), this.getZ() + dz);
	}

	public boolean isSwingingChain() {
		return this.swinging || (this.getTarget() != null && this.recoilCounter == 0);
	}

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity entity) {
		return EntityUtil.properlyApplyCustomDamageSource(level, this, entity, TFDamageTypes.getIndirectEntityDamageSource(this.level(), TFDamageTypes.SPIKED, this, this.block), null);
	}

	@Override
	public void tick() {
		super.tick();
		this.block.tick();

		if (this.recoilCounter > 0) {
			this.recoilCounter--;
		}

		this.chainAngle += CHAIN_SPEED;
		this.chainAngle %= 360;

		if (!this.level().isClientSide()) {
			this.getEntityData().set(DATA_CHAINLENGTH, (byte) Math.floor(this.getChainLength() * 127.0F));
			this.getEntityData().set(DATA_CHAINPOS, (byte) Math.floor(this.getChainAngle() / 360.0F * 255.0F));
		} else {
			// synch chain pos if it's wrong
			if (Math.abs(this.chainAngle - this.getChainAngle()) > CHAIN_SPEED * 2) {
				this.chainAngle = this.getChainAngle();
			}
		}

		//when alive,Holding SpikeBlock
		if (this.isAlive()) {
			if (this.chainMoveLength > 0) {

				Vec3 blockPos = this.getThrowPos();

				double sx2 = this.getX();
				double sy2 = this.getY() + this.getBbHeight() - 0.1D;
				double sz2 = this.getZ();

				double ox2 = sx2 - blockPos.x();
				double oy2 = sy2 - blockPos.y() - 0.25F;
				double oz2 = sz2 - blockPos.z();

				//When the thrown chainblock exceeds a certain distance, return to the owner
				if (this.chainMoveLength >= 6.0F || !this.isAlive()) {
					this.setThrowing(false);
				}

				this.block.setPos(sx2 - ox2, sy2 - oy2, sz2 - oz2);
			} else {

				// set block position
				Vec3 blockPos = this.getChainPosition();
				this.block.setPos(blockPos.x(), blockPos.y(), blockPos.z());
				this.block.setYRot(getChainAngle());
			}
		}

		// collide things with the block
		if (this.level() instanceof ServerLevel level && this.isAlive() && (this.isThrowing() || this.isSwingingChain())) {
			this.applyBlockCollisions(level, this.block);
		}
		this.chainMove();
	}

	private Vec3 getThrowPos() {
		Vec3 vec3d = this.getViewVector(1.0F);
		return new Vec3(this.getX() + vec3d.x() * this.chainMoveLength, this.getY() + this.getEyeHeight(), this.getZ() + vec3d.z() * this.chainMoveLength);
	}

	private void chainMove() {
		if (this.isThrowing()) {
			this.chainMoveLength = Mth.clamp(this.chainMoveLength + 0.5F, 0.0F, 6.0F);
		} else {
			this.chainMoveLength = Mth.clamp(this.chainMoveLength - 1.5F, 0.0F, 6.0F);
		}
	}

	public float getChainMoveLength() {
		return chainMoveLength;
	}

	/**
	 * Check if the block is colliding with any nearby entities
	 */
	protected void applyBlockCollisions(ServerLevel level, Entity collider) {
		List<Entity> list = this.level().getEntities(collider, collider.getBoundingBox().inflate(0.2D, 0.0D, 0.2D));

		for (Entity entity : list) {
			if (entity.isPushable()) {
				this.applyBlockCollision(level, collider, entity);
			}
		}

		if (this.isThrowing() && collider.isInWall()) {
			this.setThrowing(false);
			collider.playSound(TFSounds.BLOCK_AND_CHAIN_COLLIDE.get(), 0.65F, 0.75F);
			this.gameEvent(GameEvent.HIT_GROUND);
		}
	}

	/**
	 * Do the effect where the block hits something
	 */
	protected void applyBlockCollision(ServerLevel level, Entity collider, Entity collided) {
		if (collided != this) {
			collided.push(collider);
			if (collided instanceof LivingEntity) {
				if (super.doHurtTarget(level, collided)) {
					collided.push(0, 0.4, 0);
					this.playSound(TFSounds.BLOCK_AND_CHAIN_HIT.get(), 1.0F, 1.0F);
					this.gameEvent(GameEvent.PROJECTILE_LAND);
					this.recoilCounter = 40;
					if (this.isThrowing()) {
						this.setThrowing(false);
					}
				}

			}
		}
	}

	public boolean isThrowing() {
		return this.getEntityData().get(IS_THROWING);
	}

	public void setThrowing(boolean isThrowing) {
		this.getEntityData().set(IS_THROWING, isThrowing);
	}

	/**
	 * Angle between 0 and 360 to place the chain at
	 */
	private float getChainAngle() {
		if (!this.level().isClientSide()) {
			return this.chainAngle;
		} else {
			return (this.getEntityData().get(DATA_CHAINPOS) & 0xFF) / 255.0F * 360.0F;
		}
	}

	/**
	 * Between 0.0F and 2.0F, how long is the chain right now?
	 */
	private float getChainLength() {
		if (!this.level().isClientSide()) {
			if (this.isSwingingChain()) {
				return 0.9F;
			} else {
				return 0.3F;
			}
		} else {
			return (this.getEntityData().get(DATA_CHAINLENGTH) & 0xFF) / 127.0F;
		}
	}

	@Override
	public boolean isMultipartEntity() {
		return true;
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		TFPart.assignPartIDs(this);
	}

	/**
	 * We need to do this for the bounding boxes on the parts to become active
	 */
	@Override
	public MultipartGenericsAreDumb[] getParts() {
		return partsArray;
	}
}
