package twilightforest.data.helpers;

import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import twilightforest.TwilightForestMod;

public abstract class TFSoundProvider extends SoundDefinitionsProvider {

	protected TFSoundProvider(PackOutput output, ExistingFileHelper helper) {
		super(output, TwilightForestMod.ID, helper);
	}

	public void generateNewSoundWithSubtitle(DeferredHolder<SoundEvent, SoundEvent> event, String baseSoundDirectory, int numberOfSounds) {
		generateNewSound(event, baseSoundDirectory, numberOfSounds, true);
	}

	public void generateNewSound(DeferredHolder<SoundEvent, SoundEvent> event, String baseSoundDirectory, int numberOfSounds, boolean subtitle) {
		SoundDefinition definition = SoundDefinition.definition();
		if (subtitle) {
			String[] splitSoundName = event.getId().getPath().split("\\.", 3);
			definition.subtitle("subtitles.twilightforest." + splitSoundName[0] + "." + splitSoundName[2]);
		}
		for (int i = 1; i <= numberOfSounds; i++) {
			definition.with(SoundDefinition.Sound.sound(new ResourceLocation(TwilightForestMod.ID, baseSoundDirectory + (numberOfSounds > 1 ? i : "")), SoundDefinition.SoundType.SOUND));
		}
		this.add(event, definition);
	}

	public void generateNewSoundMC(DeferredHolder<SoundEvent, SoundEvent> event, String baseSoundDirectory, int numberOfSounds, boolean subtitle) {
		SoundDefinition definition = SoundDefinition.definition();
		if (subtitle) {
			String[] splitSoundName = event.getId().getPath().split("\\.", 3);
			definition.subtitle("subtitles.twilightforest." + splitSoundName[0] + "." + splitSoundName[2]);
		}
		for (int i = 1; i <= numberOfSounds; i++) {
			definition.with(SoundDefinition.Sound.sound(new ResourceLocation(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), SoundDefinition.SoundType.SOUND));
		}
		this.add(event, definition);
	}

	public void generateExistingSoundWithSubtitle(DeferredHolder<SoundEvent, SoundEvent> event, SoundEvent referencedSound) {
		this.generateExistingSound(event, referencedSound, true);
	}

	public void generateSoundWithCustomSubtitle(DeferredHolder<SoundEvent, SoundEvent> event, SoundEvent referencedSound, String subtitle) {
		this.add(event, SoundDefinition.definition()
				.subtitle(subtitle)
				.with(SoundDefinition.Sound.sound(referencedSound.getLocation(), SoundDefinition.SoundType.EVENT)));
	}

	public void generateExistingSound(DeferredHolder<SoundEvent, SoundEvent> event, SoundEvent referencedSound, boolean subtitle) {
		SoundDefinition definition = SoundDefinition.definition();
		if (subtitle) {
			String[] splitSoundName = event.getId().getPath().split("\\.", 3);
			definition.subtitle("subtitles.twilightforest." + splitSoundName[0] + "." + splitSoundName[2]);
		}
		this.add(event, definition
				.with(SoundDefinition.Sound.sound(referencedSound.getLocation(), SoundDefinition.SoundType.EVENT)));
	}

	public void makeStepSound(DeferredHolder<SoundEvent, SoundEvent> event, SoundEvent referencedSound) {
		this.add(event, SoundDefinition.definition()
				.subtitle("subtitles.block.generic.footsteps")
				.with(SoundDefinition.Sound.sound(referencedSound.getLocation(), SoundDefinition.SoundType.EVENT)));
	}

	public void makeMusicDisc(DeferredHolder<SoundEvent, SoundEvent> event, String discName) {
		this.add(event, SoundDefinition.definition()
				.with(SoundDefinition.Sound.sound(new ResourceLocation(TwilightForestMod.ID, "music/" + discName), SoundDefinition.SoundType.SOUND)
						.stream()));
	}

	public void generateParrotSound(DeferredHolder<SoundEvent, SoundEvent> event, SoundEvent referencedSound) {
		SoundDefinition definition = SoundDefinition.definition();
		String[] splitSoundName = event.getId().getPath().split("\\.", 3);
		definition.subtitle("subtitles.twilightforest." + splitSoundName[0] + "." + splitSoundName[2]);

		this.add(event, definition
				.with(SoundDefinition.Sound.sound(referencedSound.getLocation(), SoundDefinition.SoundType.EVENT).pitch(1.8F).volume(0.6F)));
	}
}
