package twilightforest.mixin.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

/** La table des cris que les perroquets imitent : NeoForge la remplace par une table de donnees. */
@Mixin(Parrot.class)
public interface ParrotAccessor {
	@Accessor("MOB_SOUND_MAP")
	static Map<EntityType<?>, SoundEvent> twilightforest$getMobSoundMap() {
		throw new AssertionError();
	}
}
