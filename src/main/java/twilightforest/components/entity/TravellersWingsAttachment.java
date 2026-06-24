package twilightforest.components.entity;

public class TravellersWingsAttachment {
	public static final int DOUBLE_JUMP_DURATION = 15;
	public static final int SIDESTEP_DURATION = 11;
	public WingState state = WingState.IDLE;
	public boolean sidestepLeft;
	public int doubleJumpTimer;
	public int sidestepTimer;
	public long lastSidestepTime;
	public boolean shouldPlaySideStepCooldownSound;
	public enum WingState {
		IDLE,
		WALK,
		SPRINT,
		FALL_SLOW,
		FALL_FAST,
		SWIM,
		RIDE,
		DOUBLE_JUMP,
		SIDESTEP
	}
}
