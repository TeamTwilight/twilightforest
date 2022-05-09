package twilightforest.entity.monster;

import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import twilightforest.TFSounds;
import twilightforest.entity.Chain;
import twilightforest.entity.SpikeBlock;
import twilightforest.entity.TFPart;
import twilightforest.entity.ai.AvoidAnyEntityGoal;
import twilightforest.entity.ai.ThrowSpikeBlockGoal;
import twilightforest.util.TFDamageSources;

import java.util.List;
import java.util.UUID;

public class BlockChainGoblin extends Monster {
	private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
	private static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "speedPenalty", -0.25D, AttributeModifier.Operation.ADDITION);

	private static final float CHAIN_SPEED = 16F;
	private static final EntityDataAccessor<Byte> DATA_CHAINLENGTH = SynchedEntityData.defineId(BlockChainGoblin.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> DATA_CHAINPOS = SynchedEntityData.defineId(BlockChainGoblin.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Boolean> IS_THROWING = SynchedEntityData.defineId(BlockChainGoblin.class, EntityDataSerializers.BOOLEAN);

	private int recoilCounter;
	private float chainAngle;

	private float chainMoveLength;

	public final SpikeBlock block = new SpikeBlock(this);
	public final Chain chain1;
	public final Chain chain2;
	public final Chain chain3;

	private final MultipartGenericsAreDumb[] partsArray;

	public BlockChainGoblin(EntityType<? extends BlockChainGoblin> type, Level world) {
		super(type, world);

		chain1 = new Chain(this);
		chain2 = new Chain(this);
		chain3 = new Chain(this);

		partsArray = new MultipartGenericsAreDumb[]{block, chain1, chain2, chain3};
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
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_CHAINLENGTH, (byte) 0);
		entityData.define(DATA_CHAINPOS, (byte) 0);
		entityData.define(IS_THROWING, false);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.28D)
				.add(Attributes.ATTACK_DAMAGE, 8.0D)
				.add(Attributes.ARMOR, 11.0D);
	}

    @Override
    public float getEyeHeight(Pose pose) {
        return this.getBbHeight() * 0.78F;
    }

    @Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.BLOCKCHAIN_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.BLOCKCHAIN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.BLOCKCHAIN_DEATH;
	}

	/**
	 * How high is the chain
	 */
	public double getChainYOffset() {
		return 1.5 - this.getChainLength() / 4.0;
	}

	/**
	 * Get the block & chain position
	 */
	public Vec3 getChainPosition() {
		return this.getChainPosition(getChainAngle(), getChainLength());
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
	public boolean doHurtTarget(Entity entity) {
		swing(InteractionHand.MAIN_HAND);
		entity.hurt(TFDamageSources.spiked(this.block, this), (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		block.tick();
		chain1.tick();
		chain2.tick();
		chain3.tick();

		if (recoilCounter > 0) {
			recoilCounter--;
		}

		chainAngle += CHAIN_SPEED;
		chainAngle %= 360;

		if (!this.level.isClientSide) {
			entityData.set(DATA_CHAINLENGTH, (byte) Math.floor(getChainLength() * 127.0F));
			entityData.set(DATA_CHAINPOS, (byte) Math.floor(getChainAngle() / 360.0F * 255.0F));
		} else {
			// synch chain pos if it's wrong
			if (Math.abs(this.chainAngle - this.getChainAngle()) > CHAIN_SPEED * 2) {
				//FMLLog.info("Fixing chain pos on client");
				this.chainAngle = getChainAngle();
			}
		}

		//when alive,Holding SpikeBlock
		if (this.isAlive()) {
			if (this.chainMoveLength > 0) {

				Vec3 blockPos = this.getThrowPos();

				double sx2 = this.getX();
				double sy2 = this.getY() + this.getBbHeight() - 0.1;
				double sz2 = this.getZ();

				double ox2 = sx2 - blockPos.x;
				double oy2 = sy2 - blockPos.y - 0.25F;
				double oz2 = sz2 - blockPos.z;

				//When the thrown chainblock exceeds a certain distance, return to the owner
				if (this.chainMoveLength >= 6.0F || !this.isAlive()) {
					this.setThrowing(false);
				}

				this.chain1.setPos(sx2 - ox2 * 0.25, sy2 - oy2 * 0.25, sz2 - oz2 * 0.25);
				this.chain2.setPos(sx2 - ox2 * 0.5, sy2 - oy2 * 0.5, sz2 - oz2 * 0.5);
				this.chain3.setPos(sx2 - ox2 * 0.85, sy2 - oy2 * 0.85, sz2 - oz2 * 0.85);

				this.block.setPos(sx2 - ox2, sy2 - oy2, sz2 - oz2);
			} else {

				// set block position
				Vec3 blockPos = this.getChainPosition();
				this.block.setPos(blockPos.x, blockPos.y, blockPos.z);
				this.block.setYRot(getChainAngle());

				// interpolate chain position
				double sx = this.getX();
				double sy = this.getY() + this.getBbHeight() - 0.1;
				double sz = this.getZ();

				double ox = sx - blockPos.x;
				double oy = sy - blockPos.y - (block.getBbHeight() / 3D);
				double oz = sz - blockPos.z;

				this.chain1.setPos(sx - ox * 0.4, sy - oy * 0.4, sz - oz * 0.4);
				this.chain2.setPos(sx - ox * 0.5, sy - oy * 0.5, sz - oz * 0.5);
				this.chain3.setPos(sx - ox * 0.6, sy - oy * 0.6, sz - oz * 0.6);
			}
		}

		// collide things with the block
		if (!level.isClientSide && this.isAlive() && (this.isThrowing() || this.isSwingingChain())) {
			this.applyBlockCollisions(this.block);
		}
		this.chainMove();
	}

	private Vec3 getThrowPos() {
		Vec3 vec3d = this.getViewVector(1.0F);
		return new Vec3(this.getX() + vec3d.x * this.chainMoveLength, this.getY() + this.getEyeHeight(), this.getZ() + vec3d.z * this.chainMoveLength);
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
	protected void applyBlockCollisions(Entity collider) {
		List<Entity> list = this.level.getEntities(collider, collider.getBoundingBox().inflate(0.20000000298023224D, 0.0D, 0.20000000298023224D));

		for (Entity entity : list) {
			if (entity.isPushable()) {
				applyBlockCollision(collider, entity);
			}
		}

		if (this.isThrowing() && collider.isInWall()) {
			this.setThrowing(false);
			collider.playSound(TFSounds.BLOCKCHAIN_COLLIDE, 0.65F, 0.75F);
		}
	}

	/**
	 * Do the effect where the block hits something
	 */
	protected void applyBlockCollision(Entity collider, Entity collided) {
		if (collided != this) {
			collided.push(collider);
			if (collided instanceof LivingEntity) {
				if (super.doHurtTarget(collided)) {
					collided.push(0, 0.4, 0);
					this.playSound(TFSounds.BLOCKCHAIN_HIT, 1.0F, 1.0F);
					this.recoilCounter = 40;
					if (this.isThrowing()) {
						this.setThrowing(false);
					}
				}

			}
		}
	}

	public boolean isThrowing() {
		return this.entityData.get(IS_THROWING);
	}

	public void setThrowing(boolean isThrowing) {
		this.entityData.set(IS_THROWING, isThrowing);
	}

	/**
	 * Angle between 0 and 360 to place the chain at
	 */
	private float getChainAngle() {
		if (!this.level.isClientSide) {
			return this.chainAngle;
		} else {
			return (entityData.get(DATA_CHAINPOS) & 0xFF) / 255.0F * 360.0F;
		}
	}

	/**
	 * Between 0.0F and 2.0F, how long is the chain right now?
	 */
	private float getChainLength() {
		if (!this.level.isClientSide) {
			if (isSwingingChain()) {
				return 0.9F;
			} else {
				return 0.3F;
			}
		} else {
			return (entityData.get(DATA_CHAINLENGTH) & 0xFF) / 127.0F;
		}
	}

	@Override
	public boolean isMultipartEntity() {
		return true;
	}

	@Override
	public void recreateFromPacket(ClientboundAddMobPacket p_147206_) {
		super.recreateFromPacket(p_147206_);
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
