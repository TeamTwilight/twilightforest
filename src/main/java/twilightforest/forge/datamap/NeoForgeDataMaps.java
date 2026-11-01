package twilightforest.forge.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import twilightforest.mixin.entity.ParrotAccessor;

import java.util.HashMap;
import java.util.Map;

/**
 * Les tables de donnees standard de NeoForge que le mod remplit : objets compostables, combustibles
 * de four et cris imites par les perroquets.
 *
 * <p>Sous NeoForge, le jeu lui-meme lit ces tables. Forge garde les tables d'origine du jeu
 * ({@code ComposterBlock.COMPOSTABLES}, la table des cris du perroquet) et un evenement pour les
 * combustibles : les fichiers du mod sont lus avec le meme format, puis recopies dans ces tables a
 * chaque chargement des donnees du serveur.</p>
 */
public final class NeoForgeDataMaps {

	public record Compostable(float chance) {
		static final Codec<Compostable> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.floatRange(0, 1).fieldOf("chance").forGetter(Compostable::chance)
		).apply(i, Compostable::new));
	}

	public record FurnaceFuel(int burnTime) {
		static final Codec<FurnaceFuel> CODEC = RecordCodecBuilder.create(i -> i.group(
				ExtraCodecs.POSITIVE_INT.fieldOf("burn_time").forGetter(FurnaceFuel::burnTime)
		).apply(i, FurnaceFuel::new));
	}

	public record ParrotImitation(SoundEvent sound) {
		static final Codec<ParrotImitation> CODEC = RecordCodecBuilder.create(i -> i.group(
				BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("sound").forGetter(ParrotImitation::sound)
		).apply(i, ParrotImitation::new));
	}

	public static final DataMapType<Item, Compostable> COMPOSTABLES = DataMapType.builder(
			neoforge("compostables"), Registries.ITEM, Compostable.CODEC).build();
	public static final DataMapType<Item, FurnaceFuel> FURNACE_FUELS = DataMapType.builder(
			neoforge("furnace_fuels"), Registries.ITEM, FurnaceFuel.CODEC).build();
	public static final DataMapType<EntityType<?>, ParrotImitation> PARROT_IMITATIONS = DataMapType.builder(
			neoforge("parrot_imitations"), Registries.ENTITY_TYPE, ParrotImitation.CODEC).build();

	/** Ce que le mod a ajoute aux tables du jeu, pour le retirer au rechargement suivant. */
	private static final Map<Item, Float> addedCompostables = new HashMap<>();
	private static final Map<EntityType<?>, SoundEvent> addedImitations = new HashMap<>();

	private NeoForgeDataMaps() {
	}

	public static void init() {
		MinecraftForge.EVENT_BUS.addListener(NeoForgeDataMaps::onTagsUpdated);
		MinecraftForge.EVENT_BUS.addListener(NeoForgeDataMaps::onFurnaceFuel);
	}

	private static ResourceLocation neoforge(String path) {
		return ResourceLocation.fromNamespaceAndPath("neoforge", path);
	}

	/** Les tags sont lies a ce moment-la : les entrees designees par un tag se resolvent. */
	private static void onTagsUpdated(TagsUpdatedEvent event) {
		if (event.getUpdateCause() != TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) {
			return;
		}

		addedCompostables.keySet().forEach(ComposterBlock.COMPOSTABLES::removeFloat);
		addedCompostables.clear();
		for (Item item : BuiltInRegistries.ITEM) {
			Compostable value = DataMaps.get(item.builtInRegistryHolder(), COMPOSTABLES);
			// Une valeur du jeu de base reste prioritaire, comme sous NeoForge.
			if (value != null && !ComposterBlock.COMPOSTABLES.containsKey(item)) {
				ComposterBlock.COMPOSTABLES.put(item, value.chance());
				addedCompostables.put(item, value.chance());
			}
		}

		Map<EntityType<?>, SoundEvent> sounds = ParrotAccessor.twilightforest$getMobSoundMap();
		addedImitations.keySet().forEach(sounds::remove);
		addedImitations.clear();
		for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
			ParrotImitation value = DataMaps.get(type.builtInRegistryHolder(), PARROT_IMITATIONS);
			if (value != null && !sounds.containsKey(type)) {
				sounds.put(type, value.sound());
				addedImitations.put(type, value.sound());
			}
		}
	}

	private static void onFurnaceFuel(FurnaceFuelBurnTimeEvent event) {
		Holder<Item> item = event.getItemStack().getItemHolder();
		FurnaceFuel value = DataMaps.get(item, FURNACE_FUELS);
		if (value != null) {
			event.setBurnTime(value.burnTime());
		}
	}
}
