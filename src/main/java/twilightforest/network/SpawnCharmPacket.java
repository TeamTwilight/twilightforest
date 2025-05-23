package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.entity.CharmEffect;
import twilightforest.init.TFEntities;

public record SpawnCharmPacket(ItemStack charm, ResourceKey<SoundEvent> event) implements CustomPacketPayload {

	public static final Type<SpawnCharmPacket> TYPE = new Type<>(TwilightForestMod.prefix("spawn_charm"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SpawnCharmPacket> STREAM_CODEC = CustomPacketPayload.codec(SpawnCharmPacket::write, SpawnCharmPacket::new);

	public SpawnCharmPacket(RegistryFriendlyByteBuf buf) {
		this(ItemStack.STREAM_CODEC.decode(buf), buf.readResourceKey(Registries.SOUND_EVENT));
	}

	public void write(RegistryFriendlyByteBuf buf) {
		ItemStack.STREAM_CODEC.encode(buf, this.charm());
		buf.writeResourceKey(this.event());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	@SuppressWarnings("Convert2Lambda")
	public static void handle(SpawnCharmPacket packet, IPayloadContext ctx) {
		if (ctx.flow().isClientbound()) {
			ctx.enqueueWork(new Runnable() {
				@Override
				public void run() {
					Player player = ctx.player();
					ClientLevel level = (ClientLevel) player.level();
					Entity camera = Minecraft.getInstance().getCameraEntity();
					if (TFConfig.spawnCharmAnimationAsTotem) {
						Minecraft.getInstance().gameRenderer.displayItemActivation(packet.charm());
						//prefer the camera pos over the player as the player position isnt quite synced to the client yet
						Minecraft.getInstance().particleEngine.createTrackingEmitter(camera != null ? camera : player, new ItemParticleOption(ParticleTypes.ITEM, packet.charm()), 20);
					} else {
						CharmEffect effect = new CharmEffect(TFEntities.CHARM_EFFECT.get(), player.level(), player, packet.charm());
						effect.offset = (float) Math.PI;
						level.addEntity(effect);
					}
					SoundEvent event = BuiltInRegistries.SOUND_EVENT.getValue(packet.event());
					if (camera != null && event != null) {
						level.playLocalSound(camera.getX(), camera.getY(), camera.getZ(), event, player.getSoundSource(), 1.5F, 1.0F, false);
					}
				}
			});
		}
	}
}
