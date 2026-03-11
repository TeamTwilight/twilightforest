package twilightforest.components.entity;

import java.util.Random;

public class TravellersWingsAnimAttachment {
	public double accumulatedPhase = new Random().nextDouble(0, 2 * Math.PI);  // Desync instances of wings when entering the game
	public double oldAgeInTicks = 0;
	public float xRotOld = 0;
	public float yRotOldLeft = 0;
	public float yRotOldRight = 0;
	public float zRotOld = 0;
	public boolean doubleJump = false;
}
