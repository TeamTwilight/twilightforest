package twilightforest.init;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.Unit;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.components.entity.*;
import twilightforest.components.item.OreScannerComponent;
import twilightforest.util.Codecs;

public class TFDataAttachments {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, TwilightForestMod.ID);

	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> FEATHER_FAN = ATTACHMENT_TYPES.register("feather_fan_falling", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<PotionFlaskTrackingAttachment>> FLASK_DOSES = ATTACHMENT_TYPES.register("flask_doses", () -> AttachmentType.builder(PotionFlaskTrackingAttachment::new).serialize(PotionFlaskTrackingAttachment.CODEC).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<FortificationShieldAttachment>> FORTIFICATION_SHIELDS = ATTACHMENT_TYPES.register("fortification_shields", () -> AttachmentType.builder(FortificationShieldAttachment::new).serialize(FortificationShieldAttachment.CODEC).sync(FortificationShieldAttachment.STREAM_CODEC).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<GiantPickaxeMiningAttachment>> GIANT_PICKAXE_MINING = ATTACHMENT_TYPES.register("giant_pickaxe_mining", () -> AttachmentType.builder(GiantPickaxeMiningAttachment::new).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<YetiThrowAttachment>> YETI_THROWING = ATTACHMENT_TYPES.register("yeti_throwing", () -> AttachmentType.builder(YetiThrowAttachment::new).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<MultiplayerInclusivityAttachment>> MULTIPLAYER_FIGHT = ATTACHMENT_TYPES.register("multiplayer_fight", () -> AttachmentType.builder(MultiplayerInclusivityAttachment::new).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<TFPortalAttachment>> TF_PORTAL_COOLDOWN = ATTACHMENT_TYPES.register("tf_portal_cooldown", () -> AttachmentType.builder(TFPortalAttachment::new).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<SmashBlocksEnchantmentAttachment>> SMASH_BLOCKS = ATTACHMENT_TYPES.register("smash_blocks", () -> AttachmentType.builder(() -> new SmashBlocksEnchantmentAttachment()).serialize(SmashBlocksEnchantmentAttachment.CODEC).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<GameProfile>> ZOMBIFIED_PLAYER = ATTACHMENT_TYPES.register("zombified_player", () -> AttachmentType.builder(() -> UUIDUtil.createOfflineProfile("GizmoTheMoonPig")).serialize(Codecs.SIMPLE_GAME_PROFILE).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Unit>> LEASH_PATHFINDER_OVERRIDE = ATTACHMENT_TYPES.register("leashed_pathfinder_override", () -> AttachmentType.builder(() -> Unit.INSTANCE).serialize(Codec.unit(Unit.INSTANCE)).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<TravellersWingsAnimAttachment>> TRAVELLERS_WINGS_ANIM = ATTACHMENT_TYPES.register("travellers_wings_anim", () -> AttachmentType.builder(TravellersWingsAnimAttachment::new).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> TRAVELLERS_GOGGLES_RED_THREAD_VISION = ATTACHMENT_TYPES.register("travellers_goggles_red_thread_vision", () -> AttachmentType.builder(() -> true).serialize(Codec.BOOL).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> TRAVELLERS_GOGGLES_ITEM_DISPLAY = ATTACHMENT_TYPES.register("travellers_goggles_item_display", () -> AttachmentType.builder(() -> true).serialize(Codec.BOOL).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Long>> LAST_TICK_WATER_WALKING = ATTACHMENT_TYPES.register("last_tick_water_walking", () -> AttachmentType.builder(() -> 0L).serialize(Codec.LONG).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> HAS_DOUBLE_JUMP = ATTACHMENT_TYPES.register("has_double_jump", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> DOUBLE_JUMP_VALIDATOR = ATTACHMENT_TYPES.register("double_jump_validator", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> DOUBLE_JUMP_VALIDATOR_LAST_CHECK = ATTACHMENT_TYPES.register("double_jump_validator_last_check", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Double>> TEMPORARY_SAVED_FORWARD_BOOST = ATTACHMENT_TYPES.register("temporary_saved_forward_boost", () -> AttachmentType.builder(() -> 1D).serialize(Codec.DOUBLE).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Long>> LAST_JUMP_KEY_PRESS_TIME = ATTACHMENT_TYPES.register("last_jump_key_press_time", () -> AttachmentType.builder(() -> 0L).serialize(Codec.LONG).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Long>> LAST_SIDESTEP_TIME = ATTACHMENT_TYPES.register("last_sidestep_time", () -> AttachmentType.builder(() -> 0L).serialize(Codec.LONG).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Float>> LAST_HORIZONTAL_IMPULSE = ATTACHMENT_TYPES.register("last_horizontal_impulse", () -> AttachmentType.builder(() -> 0F).serialize(Codec.FLOAT).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Float>> LAST_NON_ZERO_HORIZONTAL_IMPULSE = ATTACHMENT_TYPES.register("last_non_horizontal_impulse", () -> AttachmentType.builder(() -> 0F).serialize(Codec.FLOAT).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Long>> LAST_HORIZONTAL_WALKING_TIME = ATTACHMENT_TYPES.register("last_horizontal_walking_time", () -> AttachmentType.builder(() -> 0L).serialize(Codec.LONG).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> SIDESTEP_VALIDATOR = ATTACHMENT_TYPES.register("sidestep_validator", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> SIDESTEP_VALIDATOR_LAST_CHECK = ATTACHMENT_TYPES.register("sidestep_validator_last_check", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Unit>> BANISHED_TO_TWILIGHT_FOREST = ATTACHMENT_TYPES.register("twilightforest_banished", () -> AttachmentType.builder(() -> Unit.INSTANCE).serialize(Codec.unit(Unit.INSTANCE)).copyOnDeath().build());

	private static <T> T directCopy(T attachment, IAttachmentHolder holder, HolderLookup.Provider provider) {
		return attachment;
	}
}
