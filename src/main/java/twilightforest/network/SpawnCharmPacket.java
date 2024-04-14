package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.config.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.entity.CharmEffect;
import twilightforest.init.TFEntities;

public record SpawnCharmPacket(ItemStack charm, ResourceKey<SoundEvent> event) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("spawn_charm");

	public SpawnCharmPacket(FriendlyByteBuf buf) {
		this(buf.readItem(), buf.readResourceKey(Registries.SOUND_EVENT));
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeItem(this.charm());
		buf.writeResourceKey(this.event());
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@SuppressWarnings("Convert2Lambda")
	public static void handle(SpawnCharmPacket packet, PlayPayloadContext ctx) {
		//ensure this is only done on clients as this uses client only code
		if (ctx.flow().isClientbound()) {
			ctx.workHandler().execute(new Runnable() {
				@Override
				public void run() {
					Player player = ctx.player().orElseThrow();
					ClientLevel level = (ClientLevel) ctx.level().orElse(Minecraft.getInstance().level);
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
					SoundEvent event = BuiltInRegistries.SOUND_EVENT.get(packet.event());
					if (camera != null && event != null) {
						level.playLocalSound(camera.getX(), camera.getY(), camera.getZ(), event, player.getSoundSource(), 1.5F, 1.0F, false);
					}
				}
			});
		}
	}
}
