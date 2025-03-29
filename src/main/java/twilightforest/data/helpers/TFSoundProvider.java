package twilightforest.data.helpers;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.LangGenerator;

public abstract class TFSoundProvider extends SoundDefinitionsProvider {

	protected TFSoundProvider(PackOutput output, ExistingFileHelper helper) {
		super(output, TwilightForestMod.ID, helper);
	}

	public void generateNewSoundWithSubtitle(DeferredHolder<SoundEvent, SoundEvent> event, String baseSoundDirectory, int numberOfSounds, String subtitle, float volume, float pitch) {
		this.generateNewSound(event, baseSoundDirectory, numberOfSounds, subtitle, volume, pitch);
	}

	public void generateNewSoundWithSubtitle(DeferredHolder<SoundEvent, SoundEvent> event, String baseSoundDirectory, int numberOfSounds, String subtitle) {
		this.generateNewSound(event, baseSoundDirectory, numberOfSounds, subtitle, 1.0F, 1.0F);
	}

	public void generateNewSound(DeferredHolder<SoundEvent, SoundEvent> event, String baseSoundDirectory, int numberOfSounds, @Nullable String subtitle, float volume, float pitch) {
		SoundDefinition definition = SoundDefinition.definition();
		if (subtitle != null) {
			this.createSubtitleAndLangEntry(event, definition, subtitle);
		}
		for (int i = 1; i <= numberOfSounds; i++) {
			definition.with(SoundDefinition.Sound.sound(TwilightForestMod.prefix(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), SoundDefinition.SoundType.SOUND).volume(volume).pitch(pitch));
		}
		this.add(event, definition);
	}

	public void generateNewSoundMC(DeferredHolder<SoundEvent, SoundEvent> event, String baseSoundDirectory, int numberOfSounds, @Nullable String subtitle) {
		SoundDefinition definition = SoundDefinition.definition();
		if (subtitle != null) {
			this.createSubtitleAndLangEntry(event, definition, subtitle);
		}
		for (int i = 1; i <= numberOfSounds; i++) {
			definition.with(SoundDefinition.Sound.sound(ResourceLocation.withDefaultNamespace(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), SoundDefinition.SoundType.SOUND));
		}
		this.add(event, definition);
	}

	public void generateExistingSoundWithSubtitle(DeferredHolder<SoundEvent, SoundEvent> event, SoundEvent referencedSound, String subtitle) {
		this.generateExistingSoundWithSubtitle(event, referencedSound, subtitle, 1.0F, 1.0F);
	}

	public void generateExistingSoundWithSubtitle(DeferredHolder<SoundEvent, SoundEvent> event, SoundEvent referencedSound, String subtitle, float volume, float pitch) {
		this.generateExistingSound(event, referencedSound, subtitle, volume, pitch);
	}

	public void generateSoundWithExistingSubtitle(DeferredHolder<SoundEvent, SoundEvent> event, SoundEvent referencedSound, String subtitle) {
		this.add(event, SoundDefinition.definition()
			.subtitle(subtitle)
			.with(SoundDefinition.Sound.sound(referencedSound.getLocation(), SoundDefinition.SoundType.EVENT)));
	}

	public void generateExistingSound(DeferredHolder<SoundEvent, SoundEvent> event, SoundEvent referencedSound, @Nullable String subtitle, float volume, float pitch) {
		SoundDefinition definition = SoundDefinition.definition();
		if (subtitle != null) {
			this.createSubtitleAndLangEntry(event, definition, subtitle);
		}
		this.add(event, definition
			.with(SoundDefinition.Sound.sound(referencedSound.getLocation(), SoundDefinition.SoundType.EVENT).volume(volume).pitch(pitch)));
	}

	public void makeStepSound(DeferredHolder<SoundEvent, SoundEvent> event, SoundEvent referencedSound) {
		this.add(event, SoundDefinition.definition()
			.subtitle("subtitles.block.generic.footsteps")
			.with(SoundDefinition.Sound.sound(referencedSound.getLocation(), SoundDefinition.SoundType.EVENT)));
	}

	public void makeNewStepjSound(DeferredHolder<SoundEvent, SoundEvent> event, String baseSoundDirectory, int numberOfSounds) {
		SoundDefinition definition = SoundDefinition.definition();
		for (int i = 1; i <= numberOfSounds; i++) {
			definition.with(SoundDefinition.Sound.sound(TwilightForestMod.prefix(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), SoundDefinition.SoundType.SOUND));
		}
		this.add(event, definition.subtitle("subtitles.block.generic.footsteps"));
	}

	public void makeNewGenericSound(DeferredHolder<SoundEvent, SoundEvent> event, String baseSoundDirectory, int numberOfSounds, @Nullable String type) {
		SoundDefinition definition = SoundDefinition.definition();
		for (int i = 1; i <= numberOfSounds; i++) {
			definition.with(SoundDefinition.Sound.sound(TwilightForestMod.prefix(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), SoundDefinition.SoundType.SOUND));
		}
		this.add(event, type != null ? definition.subtitle("subtitles.block.generic." + type) : definition);
	}

	public void makeMusicDisc(DeferredHolder<SoundEvent, SoundEvent> event, String discName) {
		this.add(event, SoundDefinition.definition()
			.with(SoundDefinition.Sound.sound(TwilightForestMod.prefix("music/" + discName), SoundDefinition.SoundType.SOUND)
				.stream()));
	}

	public void generateParrotSound(DeferredHolder<SoundEvent, SoundEvent> event, SoundEvent referencedSound, String subtitle) {
		SoundDefinition definition = SoundDefinition.definition();
		this.createSubtitleAndLangEntry(event, definition, subtitle);

		this.add(event, definition
			.with(SoundDefinition.Sound.sound(referencedSound.getLocation(), SoundDefinition.SoundType.EVENT).pitch(1.8F).volume(0.6F)));
	}

	private void createSubtitleAndLangEntry(DeferredHolder<SoundEvent, SoundEvent> event, SoundDefinition definition, String subtitle) {
		String[] splitSoundName = event.getId().getPath().split("\\.", 3);
		String subtitleKey = "subtitles.twilightforest." + splitSoundName[0] + "." + splitSoundName[2];
		definition.subtitle(subtitleKey);
		LangGenerator.SUBTITLE_GENERATOR.put(subtitleKey, subtitle);
	}
}
