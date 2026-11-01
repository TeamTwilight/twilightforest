package twilightforest.forge.attachment;

import io.netty.buffer.Unpooled;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.forge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

/**
 * Le point d'entree des attachements : declaration des types, acces, et branchement sur Forge.
 *
 * <p>NeoForge ajoute {@code getData}/{@code setData} directement sur les entites. Java ne permet pas
 * d'ajouter une methode a une classe existante, et un mixin ne serait pas visible du compilateur :
 * les appels passent donc par les methodes statiques d'ici. C'est un peu plus verbeux au site
 * d'appel, mais explicite et sans magie.</p>
 */
public final class Attachments {
	private Attachments() {}

	public static final Capability<AttachmentHolder> CAPABILITY =
			CapabilityManager.get(new CapabilityToken<>() {});

	private static final ResourceLocation ID =
			ResourceLocation.fromNamespaceAndPath(TwilightForestMod.ID, "attachments");

	/** Tous les types declares, pour savoir quoi relire dans une sauvegarde. */
	private static final List<AttachmentType<?>> KNOWN = new ArrayList<>();

	/**
	 * Declare un type d'attachement.
	 *
	 * <p>Remplace l'enregistrement dans un registre : Forge n'a pas de registre d'attachements, et
	 * une simple liste suffit puisque l'identifiant sert seulement de cle dans le NBT.</p>
	 */
	public static <T> AttachmentType<T> declare(AttachmentType<T> type) {
		KNOWN.add(type);
		return type;
	}

	@Nullable
	private static AttachmentHolder holderOf(Entity entity) {
		return entity.getCapability(CAPABILITY).orElse(null);
	}

	public static <T> T get(Entity entity, AttachmentType<T> type) {
		AttachmentHolder holder = holderOf(entity);
		return holder != null ? holder.get(type) : type.defaultValue();
	}

	public static <T> void set(Entity entity, AttachmentType<T> type, T value) {
		AttachmentHolder holder = holderOf(entity);

		if (holder != null) {
			holder.set(type, value);
			sync(entity, type);
		}
	}

	public static boolean has(Entity entity, AttachmentType<?> type) {
		AttachmentHolder holder = holderOf(entity);
		return holder != null && holder.has(type);
	}

	public static void remove(Entity entity, AttachmentType<?> type) {
		AttachmentHolder holder = holderOf(entity);

		if (holder != null) {
			holder.remove(type);
			sync(entity, type);
		}
	}

	// --- synchronisation vers les clients --------------------------------------------------
	//
	// NeoForge envoie lui-meme les attachements declares avec .sync(...) : au joueur concerne et a
	// ceux qui voient l'entite, a chaque modification et quand un joueur commence a la voir. Ici,
	// meme regle, par SyncAttachmentPacket. Sans elle, le client ignore les boucliers de
	// fortification, le zoom des lunettes, le vol plane et la chute a l'eventail de plumes.

	/** Branche les envois initiaux (connexion, reapparition, changement de dimension, suivi). */
	public static void registerSyncEvents() {
		MinecraftForge.EVENT_BUS.addListener((PlayerEvent.StartTracking event) -> {
			if (event.getEntity() instanceof ServerPlayer player) sendAll(event.getTarget(), player);
		});
		MinecraftForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedInEvent event) -> sendOwn(event.getEntity()));
		MinecraftForge.EVENT_BUS.addListener((PlayerEvent.PlayerRespawnEvent event) -> sendOwn(event.getEntity()));
		MinecraftForge.EVENT_BUS.addListener((PlayerEvent.PlayerChangedDimensionEvent event) -> sendOwn(event.getEntity()));
	}

	private static void sendOwn(Player player) {
		if (player instanceof ServerPlayer serverPlayer) sendAll(serverPlayer, serverPlayer);
	}

	private static void sendAll(Entity entity, ServerPlayer to) {
		AttachmentHolder holder = holderOf(entity);
		if (holder == null) return;
		for (AttachmentType<?> type : KNOWN) {
			if (type.streamCodec() != null && holder.has(type)) {
				PacketDistributor.sendToPlayer(to, packet(entity, type, holder));
			}
		}
	}

	private static void sync(Entity entity, AttachmentType<?> type) {
		if (entity.level().isClientSide() || type.streamCodec() == null) return;
		AttachmentHolder holder = holderOf(entity);
		if (holder == null) return;
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, packet(entity, type, holder));
	}

	@SuppressWarnings("unchecked")
	private static <T> SyncAttachmentPacket packet(Entity entity, AttachmentType<T> type, AttachmentHolder holder) {
		if (!holder.has(type)) {
			return new SyncAttachmentPacket(entity.getId(), type.id(), false, new byte[0]);
		}
		RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), entity.registryAccess());
		((StreamCodec<RegistryFriendlyByteBuf, T>) type.streamCodec()).encode(buf, holder.get(type));
		byte[] data = new byte[buf.readableBytes()];
		buf.readBytes(data);
		return new SyncAttachmentPacket(entity.getId(), type.id(), true, data);
	}

	/** Cote client : range la valeur recue, sans renvoyer quoi que ce soit. */
	@SuppressWarnings("unchecked")
	static void applySync(@Nullable Player clientPlayer, SyncAttachmentPacket packet) {
		if (clientPlayer == null) return;
		Entity entity = clientPlayer.level().getEntity(packet.entityId());
		AttachmentHolder holder = entity != null ? holderOf(entity) : null;
		if (holder == null) return;
		for (AttachmentType<?> known : KNOWN) {
			if (!known.id().equals(packet.typeId()) || known.streamCodec() == null) continue;
			AttachmentType<Object> type = (AttachmentType<Object>) known;
			if (packet.present()) {
				RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(packet.data()), entity.registryAccess());
				holder.set(type, ((StreamCodec<RegistryFriendlyByteBuf, Object>) type.streamCodec()).decode(buf));
			} else {
				holder.remove(type);
			}
			return;
		}
	}

	// --- branchement sur Forge -------------------------------------------------------------

	@SubscribeEvent
	public static void registerCapability(RegisterCapabilitiesEvent event) {
		event.register(AttachmentHolder.class);
	}

	@SubscribeEvent
	public static void attach(AttachCapabilitiesEvent<Entity> event) {
		event.addCapability(ID, new Provider());
	}

	/**
	 * Le joueur qui reapparait est une nouvelle entite : sans cela il perdrait tout.
	 *
	 * <p>Au retour d'une dimension on conserve l'ensemble ; apres une mort, seuls les attachements
	 * qui l'ont demande.</p>
	 */
	@SubscribeEvent
	public static void copyOnClone(PlayerEvent.Clone event) {
		AttachmentHolder from = holderOf(event.getOriginal());
		AttachmentHolder to = holderOf(event.getEntity());

		if (from == null || to == null) {
			return;
		}

		event.getOriginal().reviveCaps();
		to.copyFrom(from, event.isWasDeath());
		event.getOriginal().invalidateCaps();
	}

	private static class Provider implements ICapabilitySerializable<CompoundTag> {
		private final AttachmentHolder holder = new AttachmentHolder();
		private final LazyOptional<AttachmentHolder> optional = LazyOptional.of(() -> this.holder);

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
			return CAPABILITY.orEmpty(capability, this.optional);
		}

		@Override
		public CompoundTag serializeNBT(HolderLookup.Provider registries) {
			return this.holder.serialize(registries);
		}

		@Override
		public void deserializeNBT(HolderLookup.Provider registries, CompoundTag tag) {
			this.holder.deserialize(registries, tag, KNOWN);
		}
	}
}
