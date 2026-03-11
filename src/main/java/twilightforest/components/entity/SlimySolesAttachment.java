package twilightforest.components.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class SlimySolesAttachment {
	public static final Codec<SlimySolesAttachment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.DOUBLE.fieldOf("bounce_velocity").forGetter(o -> o.bounceVelocity),
			Codec.BOOL.fieldOf("bounce_velocity").forGetter(o -> o.forceBounce),
			Codec.BOOL.fieldOf("bounce_velocity").forGetter(o -> o.hasBounced))
		.apply(instance, SlimySolesAttachment::new));

	public double bounceVelocity;
	public boolean forceBounce;
	public boolean hasBounced;

	public SlimySolesAttachment() {
		this(0, false, false);
	}

	public SlimySolesAttachment(double bounceVelocity, boolean forceBounce, boolean hasBounced) {
		this.bounceVelocity = bounceVelocity;
		this.forceBounce = forceBounce;
		this.hasBounced = hasBounced;
	}
}
