package twilightforest.entity.boss;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * This class holds the state data for a single hydra head
 *
 * @author Ben
 */
public class HydraHeadContainer {

	// balancing factors
	private static final int FLAME_BURN_FACTOR = 3;
	private static final int FLAME_DAMAGE = 19;
	private static final int BITE_DAMAGE = 48;
	private static double FLAME_BREATH_TRACKING_SPEED = 0.04D;

	private static final State NEXT_AUTOMATIC = null;

	enum State {
		IDLE(10),

		BITE_BEGINNING(40),
		BITE_READY(80),
		BITING(7),
		BITE_ENDING(40),

		FLAME_BEGINNING(40),
		FLAMING(100),
		FLAME_ENDING(30),

		MORTAR_BEGINNING(40),
		MORTAR_SHOOTING(25),
		MORTAR_ENDING(30),

		DYING(70),
		DEAD(20),

		ATTACK_COOLDOWN(80),

		BORN(20),
		ROAR_START(10),
		ROAR_RAWR(50);

		private static final Map<State, State> NEXT_STATE;
		public final int duration;

		State(int duration) {
			this.duration = duration;
		}


		static {
			ImmutableMap.Builder<State, State> b = ImmutableMap.builder();
			b.put(IDLE, IDLE);

			b.put(BITE_BEGINNING, State.BITE_READY);
			b.put(BITE_READY, BITING);
			b.put(BITING, BITE_ENDING);
			b.put(BITE_ENDING, ATTACK_COOLDOWN);

			b.put(FLAME_BEGINNING, FLAMING);
			b.put(FLAMING, FLAME_ENDING);
			b.put(FLAME_ENDING, ATTACK_COOLDOWN);

			b.put(MORTAR_BEGINNING, MORTAR_SHOOTING);
			b.put(MORTAR_SHOOTING, MORTAR_ENDING);
			b.put(MORTAR_ENDING, ATTACK_COOLDOWN);

			b.put(ATTACK_COOLDOWN, IDLE);

			b.put(DYING, DEAD);

			b.put(DEAD, DEAD);

			b.put(BORN, ROAR_START);
			b.put(ROAR_START, ROAR_RAWR);
			b.put(ROAR_RAWR, IDLE);
			NEXT_STATE = b.build();
		}
	}

	public EntityTFHydraHead headEntity;
	public EntityTFHydraNeck necka;
	public EntityTFHydraNeck neckb;
	public EntityTFHydraNeck neckc;
	public EntityTFHydraNeck neckd;
	public EntityTFHydraNeck necke;

	public Entity targetEntity;

	private double targetX;
	private double targetY;
	private double targetZ;

	private State prevState;
	public State currentState;
	public State nextState = NEXT_AUTOMATIC;

	public boolean isSecondaryAttacking;

	private int ticksNeeded;
	private int ticksProgress;

	private final int headNum;

	private int damageTaken;
	public int respawnCounter;

	private final EntityTFHydra hydraObj;

	private Map<State, Float>[] stateNeckLength;
	private Map<State, Float>[] stateXRotations;
	private Map<State, Float>[] stateYRotations;
	private Map<State, Float>[] stateMouthOpen;


	@SuppressWarnings("unchecked")
	public HydraHeadContainer(EntityTFHydra hydra, int number, boolean startActive) {
		this.headNum = number;
		this.hydraObj = hydra;

		this.damageTaken = 0;
		this.respawnCounter = -1;

		// is this a good place to initialize the necks?
		necka = new EntityTFHydraNeck(hydraObj, "neck" + headNum + "a", 2F, 2F);
		neckb = new EntityTFHydraNeck(hydraObj, "neck" + headNum + "b", 2F, 2F);
		neckc = new EntityTFHydraNeck(hydraObj, "neck" + headNum + "c", 2F, 2F);
		neckd = new EntityTFHydraNeck(hydraObj, "neck" + headNum + "d", 2F, 2F);
		necke = new EntityTFHydraNeck(hydraObj, "neck" + headNum + "e", 2F, 2F);

		// state positions, where is each state positioned?
		stateNeckLength = new Map[hydraObj.numHeads];
		stateXRotations = new Map[hydraObj.numHeads];
		stateYRotations = new Map[hydraObj.numHeads];
		stateMouthOpen = new Map[hydraObj.numHeads];

		for (int i = 0; i < hydraObj.numHeads; i++) {
			stateNeckLength[i] = new EnumMap<>(State.class);
			stateXRotations[i] = new EnumMap<>(State.class);
			stateYRotations[i] = new EnumMap<>(State.class);
			stateMouthOpen[i] = new EnumMap<>(State.class);
		}

		setupStateRotations();

		if (startActive) {
			this.prevState = State.IDLE;
			this.currentState = State.IDLE;
			this.nextState = NEXT_AUTOMATIC;
			this.ticksNeeded = 60;
			this.ticksProgress = 60;
		} else {
			this.prevState = State.DEAD;
			this.currentState = State.DEAD;
			this.nextState = NEXT_AUTOMATIC;
			this.ticksNeeded = 20;
			this.ticksProgress = 20;
		}
	}

	protected void setupStateRotations() {
		setAnimation(0, State.IDLE, 60, 0, 7, 0);
		setAnimation(1, State.IDLE, 10, 60, 9, 0);
		setAnimation(2, State.IDLE, 10, -60, 9, 0);
		setAnimation(3, State.IDLE, 50, 90, 8, 0);
		setAnimation(4, State.IDLE, 50, -90, 8, 0);
		setAnimation(5, State.IDLE, -10, 90, 9, 0);
		setAnimation(6, State.IDLE, -10, -90, 9, 0);

		setAnimation(0, State.ATTACK_COOLDOWN, 60, 0, 7, 0);
		setAnimation(1, State.ATTACK_COOLDOWN, 10, 60, 9, 0);
		setAnimation(2, State.ATTACK_COOLDOWN, 10, -60, 9, 0);
		setAnimation(3, State.ATTACK_COOLDOWN, 50, 90, 8, 0);
		setAnimation(4, State.ATTACK_COOLDOWN, 50, -90, 8, 0);
		setAnimation(5, State.ATTACK_COOLDOWN, -10, 90, 9, 0);
		setAnimation(6, State.ATTACK_COOLDOWN, -10, -90, 9, 0);

		setAnimation(0, State.FLAME_BEGINNING, 50, 0, 8, 0.75F);
		setAnimation(1, State.FLAME_BEGINNING, 30, 45, 9, 0.75F);
		setAnimation(2, State.FLAME_BEGINNING, 30, -45, 9, 0.75F);
		setAnimation(3, State.FLAME_BEGINNING, 50, 90, 8, 0.75F);
		setAnimation(4, State.FLAME_BEGINNING, 50, -90, 8, 0.75F);
		setAnimation(5, State.FLAME_BEGINNING, -10, 90, 9, 0.75F);
		setAnimation(6, State.FLAME_BEGINNING, -10, -90, 9, 0.75F);

		setAnimation(0, State.FLAMING, 45, 0, 8, 1);
		setAnimation(1, State.FLAMING, 30, 60, 9, 1);
		setAnimation(2, State.FLAMING, 30, -60, 9, 1);
		setAnimation(3, State.FLAMING, 50, 90, 8, 1);
		setAnimation(4, State.FLAMING, 50, -90, 8, 1);
		setAnimation(5, State.FLAMING, -10, 90, 9, 1);
		setAnimation(6, State.FLAMING, -10, -90, 9, 1);

		setAnimation(0, State.FLAME_ENDING, 60, 0, 7, 0);
		setAnimation(1, State.FLAME_ENDING, 10, 45, 9, 0);
		setAnimation(2, State.FLAME_ENDING, 10, -45, 9, 0);
		setAnimation(3, State.FLAME_ENDING, 50, 90, 8, 0);
		setAnimation(4, State.FLAME_ENDING, 50, -90, 8, 0);
		setAnimation(5, State.FLAME_ENDING, -10, 90, 9, 0);
		setAnimation(6, State.FLAME_ENDING, -10, -90, 9, 0);

		setAnimation(0, State.BITE_BEGINNING, -5, 60, 5, 0.25f);
		setAnimation(1, State.BITE_BEGINNING, -10, 60, 9, 0.25f);
		setAnimation(2, State.BITE_BEGINNING, -10, -60, 9, 0.25f);

		setAnimation(0, State.BITE_READY, -5, 60, 5, 1);
		setAnimation(1, State.BITE_READY, -10, 60, 9, 1);
		setAnimation(2, State.BITE_READY, -10, -60, 9, 1);

		setAnimation(0, State.BITING, -5, -30, 5, 0.2F);
		setAnimation(1, State.BITING, -10, -30, 5, 0.2F);
		setAnimation(2, State.BITING, -10, 30, 5, 0.2F);

		setAnimation(0, State.BITE_ENDING, 60, 0, 7, 0);
		setAnimation(1, State.BITE_ENDING, -10, 60, 9, 0);
		setAnimation(2, State.BITE_ENDING, -10, -60, 9, 0);

		setAnimation(0, State.MORTAR_BEGINNING, 50, 0, 8, 0.75F);
		setAnimation(1, State.MORTAR_BEGINNING, 30, 45, 9, 0.75F);
		setAnimation(2, State.MORTAR_BEGINNING, 30, -45, 9, 0.75F);
		setAnimation(3, State.MORTAR_BEGINNING, 50, 90, 8, 0.75F);
		setAnimation(4, State.MORTAR_BEGINNING, 50, -90, 8, 0.75F);
		setAnimation(5, State.MORTAR_BEGINNING, -10, 90, 9, 0.75F);
		setAnimation(6, State.MORTAR_BEGINNING, -10, -90, 9, 0.75F);

		setAnimation(0, State.MORTAR_SHOOTING, 45, 0, 8, 1);
		setAnimation(1, State.MORTAR_SHOOTING, 30, 60, 9, 1);
		setAnimation(2, State.MORTAR_SHOOTING, 30, -60, 9, 1);
		setAnimation(3, State.MORTAR_SHOOTING, 50, 90, 8, 1);
		setAnimation(4, State.MORTAR_SHOOTING, 50, -90, 8, 1);
		setAnimation(5, State.MORTAR_SHOOTING, -10, 90, 9, 1);
		setAnimation(6, State.MORTAR_SHOOTING, -10, -90, 9, 1);

		setAnimation(0, State.MORTAR_ENDING, 60, 0, 7, 0);
		setAnimation(1, State.MORTAR_ENDING, 10, 45, 9, 0);
		setAnimation(2, State.MORTAR_ENDING, 10, -45, 9, 0);
		setAnimation(3, State.MORTAR_ENDING, 50, 90, 8, 0);
		setAnimation(4, State.MORTAR_ENDING, 50, -90, 8, 0);
		setAnimation(5, State.MORTAR_ENDING, -10, 90, 9, 0);
		setAnimation(6, State.MORTAR_ENDING, -10, -90, 9, 0);

		setAnimation(0, State.DYING, -20, 0, 7, 0);
		setAnimation(1, State.DYING, -20, 60, 9, 0);
		setAnimation(2, State.DYING, -20, -60, 9, 0);
		setAnimation(3, State.DYING, -20, 90, 8, 0);
		setAnimation(4, State.DYING, -20, -90, 8, 0);
		setAnimation(5, State.DYING, -10, 90, 9, 0);
		setAnimation(6, State.DYING, -10, -90, 9, 0);

		setAnimation(0, State.DEAD, 0, 179, 4, 0);
		setAnimation(1, State.DEAD, 0, 179, 4, 0);
		setAnimation(2, State.DEAD, 0, -180, 4, 0);
		setAnimation(3, State.DEAD, 0, 179, 4, 0);
		setAnimation(4, State.DEAD, 0, -180, 4, 0);
		setAnimation(5, State.DEAD, 0, 179, 4, 0);
		setAnimation(6, State.DEAD, 0, -180, 4, 0);

		setAnimation(0, State.BORN, 60, 0, 7, 0);
		setAnimation(1, State.BORN, 10, 60, 9, 0);
		setAnimation(2, State.BORN, 10, -60, 9, 0);
		setAnimation(3, State.BORN, 50, 90, 8, 0);
		setAnimation(4, State.BORN, 50, -90, 8, 0);
		setAnimation(5, State.BORN, -10, 90, 9, 0);
		setAnimation(6, State.BORN, -10, -90, 9, 0);

		setAnimation(0, State.ROAR_START, 60, 0, 7, 0.25F);
		setAnimation(1, State.ROAR_START, 10, 60, 9, 0.25F);
		setAnimation(2, State.ROAR_START, 10, -60, 9, 0.25F);
		setAnimation(3, State.ROAR_START, 50, 90, 8, 0.25F);
		setAnimation(4, State.ROAR_START, 50, -90, 8, 0.25F);
		setAnimation(5, State.ROAR_START, -10, 90, 9, 0.25F);
		setAnimation(6, State.ROAR_START, -10, -90, 9, 0.25F);

		setAnimation(0, State.ROAR_RAWR, 60, 0, 9, 1);
		setAnimation(1, State.ROAR_RAWR, 10, 60, 11, 1);
		setAnimation(2, State.ROAR_RAWR, 10, -60, 11, 1);
		setAnimation(3, State.ROAR_RAWR, 50, 90, 10, 1);
		setAnimation(4, State.ROAR_RAWR, 50, -90, 10, 1);
		setAnimation(5, State.ROAR_RAWR, -10, 90, 11, 1);
		setAnimation(6, State.ROAR_RAWR, -10, -90, 11, 1);


	}

	private void setAnimation(int head, State state, float xRotation, float yRotation, float neckLength, float mouthOpen) {
		this.stateXRotations[head].put(state, xRotation);
		this.stateYRotations[head].put(state, yRotation);
		this.stateNeckLength[head].put(state, neckLength);
		this.stateMouthOpen[head].put(state, mouthOpen); // this doesn't really need to be set per-head, more per-state
	}

	public EntityTFHydraNeck[] getNeckArray() {
		return new EntityTFHydraNeck[]{necka, neckb, neckc, neckd, necke};
	}

	/**
	 * Called once per tick as part of the hydra entity update loop.
	 */
	public void onUpdate() {

		// neck updates
		necka.onUpdate();
		neckb.onUpdate();
		neckc.onUpdate();
		neckd.onUpdate();
		necke.onUpdate();

		// check if the head is here
		if (headEntity == null) {
			headEntity = findNearbyHead("head" + headNum);
		}

		// adjust for difficulty
		setDifficultyVariables();

		if (headEntity != null) {
			// make sure this is set up
			headEntity.width = headEntity.height = this.isActive() ? 4.0F : 1.0F;

			// only actually do these things on the server
			if (!hydraObj.world.isRemote) {
				advanceRespawnCounter();
				advanceHeadState();
				setHeadPosition();
				setHeadFacing();
				executeAttacks();
				playSounds();
			} else {
				clientAnimateHeadDeath();
			}

			setNeckPosition();
			addMouthParticles();
		}
	}

	private void advanceRespawnCounter() {
		if (this.currentState == State.DEAD && respawnCounter > -1) {
			if (--this.respawnCounter <= 0) {
				this.setNextState(State.BORN);
				this.damageTaken = 0;
				this.endCurrentAction();
				this.respawnCounter = -1;
			}
		}
	}

	private void clientAnimateHeadDeath() {
		// this will start the animation
		if (headEntity.getState() == HydraHeadContainer.State.DYING) {
			// several things, like head visibility animate off this
			this.headEntity.deathTime++;

			// make explosion particles and stuff
			if (headEntity.deathTime > 0) {
				if (headEntity.deathTime < 20) {
					doExplosionOn(headEntity, true);
				} else if (headEntity.deathTime < 30) {
					doExplosionOn(necka, false);
				} else if (headEntity.deathTime < 40) {
					doExplosionOn(neckb, false);
				} else if (headEntity.deathTime < 50) {
					doExplosionOn(neckc, false);
				} else if (headEntity.deathTime < 60) {
					doExplosionOn(neckd, false);
				} else if (headEntity.deathTime < 70) {
					doExplosionOn(necke, false);
				}
			}

			// turn necks red
			necka.hurtTime = 20;
			neckb.hurtTime = 20;
			neckc.hurtTime = 20;
			neckd.hurtTime = 20;
			necke.hurtTime = 20;
		} else {
			this.headEntity.deathTime = 0;
			this.headEntity.setHealth((float) this.headEntity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
		}
	}

	private void doExplosionOn(EntityTFHydraPart part, boolean large) {
		for (int i = 0; i < 10; ++i) {
			double var8 = part.getRNG().nextGaussian() * 0.02D;
			double var4 = part.getRNG().nextGaussian() * 0.02D;
			double var6 = part.getRNG().nextGaussian() * 0.02D;
			EnumParticleTypes particle = large && part.getRNG().nextInt(5) == 0 ? EnumParticleTypes.EXPLOSION_LARGE : EnumParticleTypes.EXPLOSION_NORMAL;
			part.world.spawnParticle(particle, part.posX + part.getRNG().nextFloat() * part.width * 2.0F - part.width, part.posY + part.getRNG().nextFloat() * part.height, part.posZ + part.getRNG().nextFloat() * part.width * 2.0F - part.width, var8, var4, var6);
		}
	}

	private void advanceHeadState() {
		// check head state
		if (++ticksProgress >= this.ticksNeeded) {
			State myNext;

			// advance state
			if (this.nextState == NEXT_AUTOMATIC) {
				myNext = State.NEXT_STATE.get(this.currentState);
				if (myNext != currentState) {
					// when returning from a secondary attack, no attack cooldown
					if (this.isSecondaryAttacking && myNext == HydraHeadContainer.State.ATTACK_COOLDOWN) {
						this.isSecondaryAttacking = false;
						myNext = HydraHeadContainer.State.IDLE;
					}
				}
			} else {
				myNext = this.nextState;
				this.nextState = NEXT_AUTOMATIC;
			}

			this.ticksNeeded = this.ticksProgress = myNext.duration;
			this.ticksProgress = 0;

			this.prevState = this.currentState;
			this.currentState = myNext;
		}
		// set datawatcher so client can properly animate
		if (headEntity.getState() != this.currentState) {
			headEntity.setState(this.currentState);
		}

	}

	private void setHeadFacing() {
		if (this.currentState == HydraHeadContainer.State.BITE_READY) {
			// face target within certain constraints
			this.faceEntity(targetEntity, 5F, hydraObj.getVerticalFaceSpeed());

			// head 0 and 1 max yaw
			float biteMaxYaw = -60;
			float biteMinYaw = -90;

			if (headNum == 2) {
				// head 2 max/min yaw
				biteMaxYaw = 60;
				biteMinYaw = 90;
			}

			float yawOffOffset = MathHelper.wrapDegrees(headEntity.rotationYaw - hydraObj.renderYawOffset);

			if (yawOffOffset > biteMaxYaw) {
				headEntity.rotationYaw = hydraObj.renderYawOffset + biteMaxYaw;
			}
			if (yawOffOffset < biteMinYaw) {
				headEntity.rotationYaw = hydraObj.renderYawOffset + biteMinYaw;
			}

			// make the target vector be a point off in the distance in the direction we're already facing
			Vec3d look = this.headEntity.getLookVec();
			double distance = 16.0D;
			this.targetX = headEntity.posX + look.x * distance;
			this.targetY = headEntity.posY + 1.5 + look.y * distance;
			this.targetZ = headEntity.posZ + look.z * distance;
		} else if (this.currentState == State.BITING || this.currentState == HydraHeadContainer.State.BITE_ENDING) {
			this.faceEntity(targetEntity, 5F, hydraObj.getVerticalFaceSpeed());
			headEntity.rotationPitch += Math.PI / 4;
		} else if (this.currentState == HydraHeadContainer.State.ROAR_RAWR) {
			// keep facing target vector, don't move
			this.faceVec(this.targetX, this.targetY, this.targetZ, 10F, hydraObj.getVerticalFaceSpeed());
		} else if (this.currentState == HydraHeadContainer.State.FLAMING || (this.currentState == HydraHeadContainer.State.FLAME_BEGINNING)) {
			// move flame breath slowly towards the player
			moveTargetCoordsTowardsTargetEntity(FLAME_BREATH_TRACKING_SPEED);
			// face the target coordinates
			this.faceVec(this.targetX, this.targetY, this.targetZ, 5F, hydraObj.getVerticalFaceSpeed());
		} else {
			if (this.isActive()) {
				if (this.targetEntity != null) {
					// watch the target entity
					this.faceEntity(targetEntity, 5F, hydraObj.getVerticalFaceSpeed());
				} else {
					// while idle, look where the body is looking?
					faceIdle(1.5F, hydraObj.getVerticalFaceSpeed());
				}
			}
		}
	}

	private void moveTargetCoordsTowardsTargetEntity(double distance) {
		if (this.targetEntity != null) {
			Vec3d vect = new Vec3d(this.targetEntity.posX - this.targetX, this.targetEntity.posY - this.targetY, this.targetEntity.posZ - this.targetZ);

			vect = vect.normalize();

			this.targetX += vect.x * distance;
			this.targetY += vect.y * distance;
			this.targetZ += vect.z * distance;
		}
	}

	private void addMouthParticles() {
		Vec3d vector = headEntity.getLookVec();

		double dist = 3.5;
		double px = headEntity.posX + vector.x * dist;
		double py = headEntity.posY + 1 + vector.y * dist;
		double pz = headEntity.posZ + vector.z * dist;

		if (headEntity.getState() == State.FLAME_BEGINNING) {
			headEntity.world.spawnParticle(EnumParticleTypes.FLAME, px + headEntity.getRNG().nextDouble() - 0.5, py + headEntity.getRNG().nextDouble() - 0.5, pz + headEntity.getRNG().nextDouble() - 0.5, 0, 0, 0);
			headEntity.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, px + headEntity.getRNG().nextDouble() - 0.5, py + headEntity.getRNG().nextDouble() - 0.5, pz + headEntity.getRNG().nextDouble() - 0.5, 0, 0, 0);
		}

		if (headEntity.getState() == State.FLAMING) {
			Vec3d look = headEntity.getLookVec();
			for (int i = 0; i < 5; i++) {
				double dx = look.x;
				double dy = look.y;
				double dz = look.z;

				double spread = 5 + headEntity.getRNG().nextDouble() * 2.5;
				double velocity = 1.0 + headEntity.getRNG().nextDouble();

				// spread flame
				dx += headEntity.getRNG().nextGaussian() * 0.007499999832361937D * spread;
				dy += headEntity.getRNG().nextGaussian() * 0.007499999832361937D * spread;
				dz += headEntity.getRNG().nextGaussian() * 0.007499999832361937D * spread;
				dx *= velocity;
				dy *= velocity;
				dz *= velocity;

				TwilightForestMod.proxy.spawnParticle(headEntity.world, TFParticleType.LARGE_FLAME, px, py, pz, dx, dy, dz);
			}
		}

		if (headEntity.getState() == State.BITE_BEGINNING || headEntity.getState() == State.BITE_READY) {
			headEntity.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, px + headEntity.getRNG().nextDouble() - 0.5, py + headEntity.getRNG().nextDouble() - 0.5, pz + headEntity.getRNG().nextDouble() - 0.5, 0, 0, 0);
		}

		if (headEntity.getState() == State.MORTAR_BEGINNING) {
			headEntity.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, px + headEntity.getRNG().nextDouble() - 0.5, py + headEntity.getRNG().nextDouble() - 0.5, pz + headEntity.getRNG().nextDouble() - 0.5, 0, 0, 0);
		}
	}

	private void playSounds() {
		if (headEntity.getState() == State.FLAMING && headEntity.ticksExisted % 5 == 0) {
			// fire breathing!
			headEntity.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 0.5F + headEntity.getRNG().nextFloat(), headEntity.getRNG().nextFloat() * 0.7F + 0.3F);
		}
		if (headEntity.getState() == State.ROAR_RAWR) {
			headEntity.playSound(TFSounds.HYDRA_ROAR, 1.25F, headEntity.getRNG().nextFloat() * 0.3F + 0.7F);
		}
		if (headEntity.getState() == State.BITE_READY && this.ticksProgress == 60) {
			headEntity.playSound(TFSounds.HYDRA_WARN, 2.0F, headEntity.getRNG().nextFloat() * 0.3F + 0.7F);
		}
	}

	private void setNeckPosition() {
		// set neck positions
		Vec3d vector = null;
		float neckRotation = 0;

		if (headNum == 0) {
			vector = new Vec3d(0, 3, -1);
			neckRotation = 0;
		}
		if (headNum == 1) {
			vector = new Vec3d(-1, 3, 3);
			neckRotation = 90;
		}
		if (headNum == 2) {
			vector = new Vec3d(1, 3, 3);
			neckRotation = -90;
		}
		if (headNum == 3) {
			vector = new Vec3d(-1, 3, 3);
			neckRotation = 135;
		}
		if (headNum == 4) {
			vector = new Vec3d(1, 3, 3);
			neckRotation = -135;
		}

		if (headNum == 5) {
			vector = new Vec3d(-1, 3, 5);
			neckRotation = 135;
		}
		if (headNum == 6) {
			vector = new Vec3d(1, 3, 5);
			neckRotation = -135;
		}


		vector = vector.rotateYaw((-(hydraObj.renderYawOffset + neckRotation) * 3.141593F) / 180F);
		setNeckPositon(hydraObj.posX + vector.x, hydraObj.posY + vector.y, hydraObj.posZ + vector.z, hydraObj.renderYawOffset, 0);
	}

	protected void setHeadPosition() {
		// set head positions
		Vec3d vector;
		double dx, dy, dz;

		// head1 is above
		// head2 is to the right
		// head3 is to the left
		setupStateRotations(); // temporary, for debugging

		float neckLength = getCurrentNeckLength();
		float xRotation = getCurrentHeadXRotation();
		float yRotation = getCurrentHeadYRotation();


		float periodX = (headNum == 0 || headNum == 3) ? 20F : ((headNum == 1 || headNum == 4) ? 5.0f : 7.0F);
		float periodY = (headNum == 0 || headNum == 4) ? 10F : ((headNum == 1 || headNum == 6) ? 6.0f : 5.0F);

		float xSwing = MathHelper.sin(hydraObj.ticksExisted / periodX) * 3.0F;
		float ySwing = MathHelper.sin(hydraObj.ticksExisted / periodY) * 5.0F;

		if (!this.isActive()) {
			xSwing = ySwing = 0;
		}

		vector = new Vec3d(0, 0, neckLength); // -53 = 3.3125
		vector = vector.rotatePitch((xRotation * 3.141593F + xSwing) / 180F);
		vector = vector.rotateYaw((-(hydraObj.renderYawOffset + yRotation + ySwing) * 3.141593F) / 180F);

		dx = hydraObj.posX + vector.x;
		dy = hydraObj.posY + vector.y + 3;
		dz = hydraObj.posZ + vector.z;

		headEntity.setPosition(dx, dy, dz);
		headEntity.setMouthOpen(getCurrentMouthOpen());
	}

	private void executeAttacks() {
		if (this.currentState == State.MORTAR_SHOOTING && this.ticksProgress % 10 == 0) {
			Entity lookTarget = getHeadLookTarget();

			if (lookTarget != null && (lookTarget instanceof EntityTFHydraPart || lookTarget instanceof MultiPartEntityPart)) {
				// stop hurting yourself!
				this.endCurrentAction();
			} else {
				EntityTFHydraMortar mortar = new EntityTFHydraMortar(headEntity.world, this.headEntity);

				// launch blasting mortars if the player is hiding
				if (this.targetEntity != null && !headEntity.getEntitySenses().canSee(this.targetEntity)) {
					mortar.setToBlasting();
				}

				headEntity.world.playEvent(1008, new BlockPos(headEntity), 0);

				headEntity.world.spawnEntity(mortar);
			}
		}
		if (headEntity.getState() == State.BITING) {
			// damage nearby things
			List<Entity> nearbyList = headEntity.world.getEntitiesWithinAABBExcludingEntity(headEntity, headEntity.getEntityBoundingBox().grow(0.0, 1.0, 0.0));

			for (Entity nearby : nearbyList) {
				if (nearby instanceof EntityLivingBase && !(nearby instanceof EntityTFHydraPart) && !(nearby instanceof EntityTFHydra) && !(nearby instanceof MultiPartEntityPart)) {
					// bite it!
					nearby.attackEntityFrom(DamageSource.causeMobDamage(hydraObj), BITE_DAMAGE);
				}
			}
		}

		if (headEntity.getState() == State.FLAMING) {
			Entity target = getHeadLookTarget();

			if (target != null) {
				if (target instanceof EntityTFHydraPart || target instanceof MultiPartEntityPart) {
					// stop hurting yourself!
					this.endCurrentAction();
				} else if (!target.isImmuneToFire() && target.attackEntityFrom(DamageSource.IN_FIRE, FLAME_DAMAGE)) {
					target.setFire(FLAME_BURN_FACTOR);
				}
			}
		}

	}

	private void setDifficultyVariables() {
		if (this.hydraObj.world.getDifficulty() != EnumDifficulty.HARD) {
			HydraHeadContainer.FLAME_BREATH_TRACKING_SPEED = 0.04D;
		} else {
			// hard mode!
			HydraHeadContainer.FLAME_BREATH_TRACKING_SPEED = 0.1D;  // higher is harder
		}
	}

	// TODO this seems copied from somwhere?
	private Entity getHeadLookTarget() {
		Entity pointedEntity = null;
		double range = 30.0D;
		Vec3d srcVec = new Vec3d(headEntity.posX, headEntity.posY + 1.0, headEntity.posZ);
		Vec3d lookVec = headEntity.getLook(1.0F);
		Vec3d destVec = srcVec.addVector(lookVec.x * range, lookVec.y * range, lookVec.z * range);
		float var9 = 3.0F;
		List<Entity> possibleList = headEntity.world.getEntitiesWithinAABBExcludingEntity(headEntity, headEntity.getEntityBoundingBox().offset(lookVec.x * range, lookVec.y * range, lookVec.z * range).grow(var9, var9, var9));
		double hitDist = 0;

		for (Entity possibleEntity : possibleList) {
			if (possibleEntity.canBeCollidedWith() && possibleEntity != headEntity && possibleEntity != necka && possibleEntity != neckb && possibleEntity != neckc) {
				float borderSize = possibleEntity.getCollisionBorderSize();
				AxisAlignedBB collisionBB = possibleEntity.getEntityBoundingBox().grow((double) borderSize, (double) borderSize, (double) borderSize);
				RayTraceResult interceptPos = collisionBB.calculateIntercept(srcVec, destVec);

				if (collisionBB.contains(srcVec)) {
					if (0.0D < hitDist || hitDist == 0.0D) {
						pointedEntity = possibleEntity;
						hitDist = 0.0D;
					}
				} else if (interceptPos != null) {
					double possibleDist = srcVec.distanceTo(interceptPos.hitVec);

					if (possibleDist < hitDist || hitDist == 0.0D) {
						pointedEntity = possibleEntity;
						hitDist = possibleDist;
					}
				}
			}
		}
		return pointedEntity;
	}

	public void setNextState(State next) {
		this.nextState = next;
	}

	public void endCurrentAction() {
		this.ticksProgress = this.ticksNeeded;
	}

	/**
	 * Search for nearby heads with the string as their name
	 */
	private EntityTFHydraHead findNearbyHead(String string) {
		List<EntityTFHydraHead> nearbyHeads = hydraObj.world.getEntitiesWithinAABB(EntityTFHydraHead.class, new AxisAlignedBB(hydraObj.posX, hydraObj.posY, hydraObj.posZ, hydraObj.posX + 1, hydraObj.posY + 1, hydraObj.posZ + 1).grow(16.0D, 16.0D, 16.0D));

		for (EntityTFHydraHead nearbyHead : nearbyHeads) {
			if (nearbyHead.getPartName().equals(string)) {
				nearbyHead.hydraObj = hydraObj;
				return nearbyHead;
			}
		}

		return null;
	}

	private float getCurrentNeckLength() {
		float prevLength = stateNeckLength[this.headNum].get(prevState);
		float curLength = stateNeckLength[this.headNum].get(currentState);
		float progress = (float) ticksProgress / (float) ticksNeeded;

		return (float) MathHelper.clampedLerp(prevLength, curLength, progress);
	}

	private float getCurrentHeadXRotation() {
		float prevRotation = stateXRotations[this.headNum].get(prevState);
		float currentRotation = stateXRotations[this.headNum].get(currentState);
		float progress = (float) ticksProgress / (float) ticksNeeded;

		return (float) MathHelper.clampedLerp(prevRotation, currentRotation, progress);
	}


	private float getCurrentHeadYRotation() {
		float prevRotation = stateYRotations[this.headNum].get(prevState);
		float currentRotation = stateYRotations[this.headNum].get(currentState);
		float progress = (float) ticksProgress / (float) ticksNeeded;

		return (float) MathHelper.clampedLerp(prevRotation, currentRotation, progress);
	}


	protected float getCurrentMouthOpen() {
		float prevOpen = stateMouthOpen[this.headNum].get(prevState);
		float curOpen = stateMouthOpen[this.headNum].get(currentState);
		float progress = (float) ticksProgress / (float) ticksNeeded;

		return (float) MathHelper.clampedLerp(prevOpen, curOpen, progress);
	}

	/**
	 * Sets the four neck positions ranging from the start position to the head position.
	 */
	protected void setNeckPositon(double startX, double startY, double startZ, float startYaw, float startPitch) {

		double endX = headEntity.posX;
		double endY = headEntity.posY;
		double endZ = headEntity.posZ;
		float endYaw = headEntity.rotationYaw;
		float endPitch = headEntity.rotationPitch;

		for (; startYaw - endYaw < -180F; endYaw -= 360F) {
		}
		for (; startYaw - endYaw >= 180F; endYaw += 360F) {
		}
		for (; startPitch - endPitch < -180F; endPitch -= 360F) {
		}
		for (; startPitch - endPitch >= 180F; endPitch += 360F) {
		}


		// translate the end position back 1 unit
		if (endPitch > 0) {
			// if we are looking down, don't raise the first neck position, it looks weird
			Vec3d vector = new Vec3d(0, 0, -1.0).rotateYaw((-endYaw * 3.141593F) / 180F);
			endX += vector.x;
			endY += vector.y;
			endZ += vector.z;
		} else {
			// but if we are looking up, lower it or it goes through the crest
			Vec3d vector = headEntity.getLookVec();
			float dist = 1.0f;

			endX -= vector.x * dist;
			endY -= vector.y * dist;
			endZ -= vector.z * dist;

		}


		float factor = 0F;

		factor = 0.00F;
		necka.setPosition(endX + (startX - endX) * factor, endY + (startY - endY) * factor, endZ + (startZ - endZ) * factor);
		necka.rotationYaw = endYaw + (startYaw - endYaw) * factor;
		necka.rotationPitch = endPitch + (startPitch - endPitch) * factor;

		factor = 0.25F;
		neckb.setPosition(endX + (startX - endX) * factor, endY + (startY - endY) * factor, endZ + (startZ - endZ) * factor);
		neckb.rotationYaw = endYaw + (startYaw - endYaw) * factor;
		neckb.rotationPitch = endPitch + (startPitch - endPitch) * factor;

		factor = 0.50F;
		neckc.setPosition(endX + (startX - endX) * factor, endY + (startY - endY) * factor, endZ + (startZ - endZ) * factor);
		neckc.rotationYaw = endYaw + (startYaw - endYaw) * factor;
		neckc.rotationPitch = endPitch + (startPitch - endPitch) * factor;

		factor = 0.75F;
		neckd.setPosition(endX + (startX - endX) * factor, endY + (startY - endY) * factor, endZ + (startZ - endZ) * factor);
		neckd.rotationYaw = endYaw + (startYaw - endYaw) * factor;
		neckd.rotationPitch = endPitch + (startPitch - endPitch) * factor;

		factor = 1.0F;
		necke.setPosition(endX + (startX - endX) * factor, endY + (startY - endY) * factor, endZ + (startZ - endZ) * factor);
		necke.rotationYaw = endYaw + (startYaw - endYaw) * factor;
		necke.rotationPitch = endPitch + (startPitch - endPitch) * factor;
	}

	private void faceIdle(float yawConstraint, float pitchConstraint) {
		float angle = (((hydraObj.rotationYaw) * 3.141593F) / 180F);
		float distance = 30.0F;

		double dx = hydraObj.posX - MathHelper.sin(angle) * distance;
		double dy = hydraObj.posY + 3.0;
		double dz = hydraObj.posZ + MathHelper.cos(angle) * distance;

		faceVec(dx, dy, dz, yawConstraint, pitchConstraint);
	}

	public void faceEntity(Entity entity, float yawConstraint, float pitchConstraint) {
		double yTarget;
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase entityliving = (EntityLivingBase) entity;
			yTarget = entityliving.posY + entityliving.getEyeHeight();
		} else {
			yTarget = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2D;
		}

		faceVec(entity.posX, yTarget, entity.posZ, yawConstraint, pitchConstraint);

		// let's just set the target vector here
		this.targetX = entity.posX;
		this.targetY = entity.posY;
		this.targetZ = entity.posZ;
	}

	private void faceVec(double x, double y, double z, float yawConstraint, float pitchConstraint) {
		double xOffset = x - headEntity.posX;
		double zOffset = z - headEntity.posZ;
		double yOffset = (headEntity.posY + 1.0) - y;

		double distance = MathHelper.sqrt(xOffset * xOffset + zOffset * zOffset);
		float xyAngle = (float) ((Math.atan2(zOffset, xOffset) * 180D) / Math.PI) - 90F;
		float zdAngle = (float) (-((Math.atan2(yOffset, distance) * 180D) / Math.PI));
		headEntity.rotationPitch = -updateRotation(headEntity.rotationPitch, zdAngle, pitchConstraint);
		headEntity.rotationYaw = updateRotation(headEntity.rotationYaw, xyAngle, yawConstraint);
	}

	private float updateRotation(float current, float intended, float increment) {
		float delta = MathHelper.wrapDegrees(intended - current);

		if (delta > increment) {
			delta = increment;
		}

		if (delta < -increment) {
			delta = -increment;
		}

		return MathHelper.wrapDegrees(current + delta);
	}

	public Entity getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity(Entity targetEntity) {
		this.targetEntity = targetEntity;
	}

	public void setHurtTime(int hurtTime) {
		if (headEntity != null) {
			headEntity.hurtTime = hurtTime;
		}
		necka.hurtTime = hurtTime;
		neckb.hurtTime = hurtTime;
		neckc.hurtTime = hurtTime;
		neckd.hurtTime = hurtTime;
		necke.hurtTime = hurtTime;
	}

	/**
	 * At certain times, some of the heads are "dead" and hidden
	 */
	public boolean shouldRenderHead() {
		return this.headEntity.getState() != State.DEAD && this.headEntity.deathTime < 20;
	}

	/**
	 * Same with the neck parts
	 */
	public boolean shouldRenderNeck(int neckNum) {
		int time = 30 + 10 * neckNum;
		return this.headEntity.getState() != State.DEAD && this.headEntity.deathTime < time;
	}

	/**
	 * Is this head active, that is, not dying or dead?
	 */
	public boolean isActive() {
		return this.currentState != State.DYING && this.currentState != State.DEAD;
	}

	/**
	 * Add to our damage taken counter
	 */
	public void addDamage(float damageAmount) {
		this.damageTaken += damageAmount;
	}

	/**
	 * How much damage has this head taken?
	 */
	public int getDamageTaken() {
		return this.damageTaken;
	}

	public void setRespawnCounter(int count) {
		this.respawnCounter = count;
	}

}
