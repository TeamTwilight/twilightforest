package twilightforest;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class TFSounds {

	// Call target to ensure loaded class
	public static void init() {
	}

	public static final SoundEvent KOBOLD_DEATH = registerSound("mob.kobold.die");
	public static final SoundEvent KOBOLD_AMBIENT = registerSound("mob.kobold.kobold");
	public static final SoundEvent KOBOLD_HURT = registerSound("mob.kobold.hurt");
	public static final SoundEvent CICADA = registerSound("mob.cicada");
	public static final SoundEvent NAGA_HISS = registerSound("mob.naga.hiss");
	public static final SoundEvent NAGA_HURT = registerSound("mob.naga.hurt");
	public static final SoundEvent NAGA_RATTLE = registerSound("mob.naga.rattle");
	public static final SoundEvent RAVEN_CAW = registerSound("mob.raven.caw");
	public static final SoundEvent RAVEN_SQUAWK = registerSound("mob.raven.squawk");
	public static final SoundEvent REDCAP_DEATH = registerSound("mob.redcap.die");
	public static final SoundEvent REDCAP_AMBIENT = registerSound("mob.redcap.redcap");
	public static final SoundEvent REDCAP_HURT = registerSound("mob.redcap.hurt");
	public static final SoundEvent TINYBIRD_CHIRP = registerSound("mob.tinybird.chirp");
	public static final SoundEvent TINYBIRD_HURT = registerSound("mob.tinybird.hurt");
	public static final SoundEvent TINYBIRD_SONG = registerSound("mob.tinybird.song");
	public static final SoundEvent URGHAST_TRAP_ACTIVE = registerSound("mob.urghast.trapactive");
	public static final SoundEvent URGHAST_TRAP_ON = registerSound("mob.urghast.trapon");
	public static final SoundEvent URGHAST_TRAP_SPINDOWN = registerSound("mob.urghast.trapspindown");
	public static final SoundEvent URGHAST_TRAP_WARMUP = registerSound("mob.urghast.trapwarmup");
	public static final SoundEvent WRAITH = registerSound("mob.wraith.wraith");
	public static final SoundEvent HYDRA_HURT = registerSound("mob.hydra.hurt");
	public static final SoundEvent HYDRA_DEATH = registerSound("mob.hydra.death");
	public static final SoundEvent HYDRA_GROWL = registerSound("mob.hydra.growl");
	public static final SoundEvent HYDRA_ROAR = registerSound("mob.hydra.roar");
	public static final SoundEvent HYDRA_WARN = registerSound("mob.hydra.warn");
	public static final SoundEvent MOSQUITO = registerSound("mob.mosquito.mosquito");
	public static final SoundEvent ICE_AMBIENT = registerSound("mob.ice.noise");
	public static final SoundEvent ICE_HURT = registerSound("mob.ice.hurt");
	public static final SoundEvent ICE_DEATH = registerSound("mob.ice.death");
	public static final SoundEvent SLIDER = registerSound("random.slider");

	private static SoundEvent registerSound(String sound) {
		ResourceLocation name = new ResourceLocation(TwilightForestMod.ID, sound);
		SoundEvent evt = new SoundEvent(name);
		GameRegistry.register(evt, name);
		return evt;
	}

	private TFSounds() {
	}

}
