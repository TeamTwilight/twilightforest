package twilightforest.forge.attachment;

import com.mojang.serialization.DataResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.nbt.Tag;
import twilightforest.TwilightForestMod;

import java.util.HashMap;
import java.util.Map;

/**
 * Le sac de donnees attachees a une entite.
 *
 * <p>Un seul exemplaire par entite, porte par une capability Forge. Les valeurs sont rangees par
 * type ; celles qui declarent un codec sont ecrites dans la sauvegarde, les autres ne vivent que
 * le temps de la session — c'est la meme regle que chez NeoForge.</p>
 */
public class AttachmentHolder {
	private final Map<AttachmentType<?>, Object> values = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <T> T get(AttachmentType<T> type) {
		return (T) this.values.computeIfAbsent(type, t -> ((AttachmentType<T>) t).defaultValue());
	}

	public <T> void set(AttachmentType<T> type, T value) {
		this.values.put(type, value);
	}

	public boolean has(AttachmentType<?> type) {
		return this.values.containsKey(type);
	}

	public void remove(AttachmentType<?> type) {
		this.values.remove(type);
	}

	/**
	 * Recopie les attachements d'un autre sac.
	 *
	 * <p>{@code deathOnly} restreint la copie a ceux qui ont demande a survivre a la mort ; un
	 * changement de dimension, lui, conserve tout.</p>
	 */
	public void copyFrom(AttachmentHolder other, boolean deathOnly) {
		for (Map.Entry<AttachmentType<?>, Object> e : other.values.entrySet()) {
			if (!deathOnly || e.getKey().copyOnDeath()) {
				this.values.put(e.getKey(), e.getValue());
			}
		}
	}

	/**
	 * {@code registries} donne aux codecs l'acces aux registres : sans lui, une donnee qui reference
	 * un objet du jeu ne saurait pas se relire.
	 */
	public CompoundTag serialize(HolderLookup.Provider registries) {
		RegistryOps<net.minecraft.nbt.Tag> ops = registries.createSerializationContext(NbtOps.INSTANCE);
		CompoundTag tag = new CompoundTag();

		for (Map.Entry<AttachmentType<?>, Object> e : this.values.entrySet()) {
			@SuppressWarnings("unchecked")
			AttachmentType<Object> type = (AttachmentType<Object>) e.getKey();

			if (type.codec() == null) {
				continue;
			}

			DataResult<Tag> encoded = type.codec().encodeStart(ops, e.getValue());
			encoded.resultOrPartial(err ->
					TwilightForestMod.LOGGER.warn("Attachement {} non serialise : {}", type.id(), err))
					.ifPresent(t -> tag.put(type.id(), t));
		}

		return tag;
	}

	public void deserialize(HolderLookup.Provider registries, CompoundTag tag, Iterable<AttachmentType<?>> known) {
		RegistryOps<net.minecraft.nbt.Tag> ops = registries.createSerializationContext(NbtOps.INSTANCE);
		this.values.clear();

		for (AttachmentType<?> raw : known) {
			@SuppressWarnings("unchecked")
			AttachmentType<Object> type = (AttachmentType<Object>) raw;

			if (type.codec() == null || !tag.contains(type.id())) {
				continue;
			}

			type.codec().parse(ops, tag.get(type.id()))
					.resultOrPartial(err ->
							TwilightForestMod.LOGGER.warn("Attachement {} non relu : {}", type.id(), err))
					.ifPresent(v -> this.values.put(type, v));
		}
	}
}
