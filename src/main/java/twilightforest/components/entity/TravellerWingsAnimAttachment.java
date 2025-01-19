package twilightforest.components.entity;

import java.util.Random;

public class TravellerWingsAnimAttachment {
	public double accumulatedPhase = new Random().nextDouble(0, 2 * Math.PI);  // Desync instances of wings when entering the game
	public double oldAgeInTicks = 0;
	public float xRotOld = 0;
	public float yRotOld = 0;
	public float zRotOld = 0;
}
