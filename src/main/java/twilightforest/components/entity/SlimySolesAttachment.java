package twilightforest.components.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class SlimySolesAttachment {

	//optionals are here as the fields were improperly named and caused logspam after the fix
	public static final MapCodec<SlimySolesAttachment> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.DOUBLE.optionalFieldOf("bounce_velocity", 0.0D).forGetter(o -> o.bounceVelocity),
			Codec.DOUBLE.optionalFieldOf("double_jump_boost_velocity", 0.0D).forGetter(o -> o.doubleJumpBoostVelocity),
			Codec.BOOL.optionalFieldOf("force_bounce", false).forGetter(o -> o.forceBounce),
			Codec.BOOL.optionalFieldOf("bounce", false).forGetter(o -> o.hasBounced))
		.apply(instance, SlimySolesAttachment::new));

	public double bounceVelocity;
	public double doubleJumpBoostVelocity;
	public boolean forceBounce;
	public boolean hasBounced;

	public SlimySolesAttachment() {
		this(0, 0, false, false);
	}

	public SlimySolesAttachment(double bounceVelocity, double doubleJumpBoostVelocity, boolean forceBounce, boolean hasBounced) {
		this.bounceVelocity = bounceVelocity;
		this.forceBounce = forceBounce;
		this.hasBounced = hasBounced;
	}
}
