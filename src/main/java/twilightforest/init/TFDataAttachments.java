package twilightforest.init;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.Unit;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.components.entity.*;
import twilightforest.util.Codecs;

public class TFDataAttachments {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, TwilightForestMod.ID);

	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> FEATHER_FAN = ATTACHMENT_TYPES.register("feather_fan_falling", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL.fieldOf("feather_fan_falling")).sync(ByteBufCodecs.BOOL).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<PotionFlaskTrackingAttachment>> FLASK_DOSES = ATTACHMENT_TYPES.register("flask_doses", () -> AttachmentType.builder(PotionFlaskTrackingAttachment::new).serialize(PotionFlaskTrackingAttachment.CODEC).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<FortificationShieldAttachment>> FORTIFICATION_SHIELDS = ATTACHMENT_TYPES.register("fortification_shields", () -> AttachmentType.builder(FortificationShieldAttachment::new).serialize(FortificationShieldAttachment.CODEC).sync(FortificationShieldAttachment.STREAM_CODEC).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<GiantPickaxeMiningAttachment>> GIANT_PICKAXE_MINING = ATTACHMENT_TYPES.register("giant_pickaxe_mining", () -> AttachmentType.builder(GiantPickaxeMiningAttachment::new).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<YetiThrowAttachment>> YETI_THROWING = ATTACHMENT_TYPES.register("yeti_throwing", () -> AttachmentType.builder(YetiThrowAttachment::new).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<MultiplayerInclusivityAttachment>> MULTIPLAYER_FIGHT = ATTACHMENT_TYPES.register("multiplayer_fight", () -> AttachmentType.builder(MultiplayerInclusivityAttachment::new).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<TFPortalAttachment>> TF_PORTAL_COOLDOWN = ATTACHMENT_TYPES.register("tf_portal_cooldown", () -> AttachmentType.builder(TFPortalAttachment::new).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<SmashBlocksEnchantmentAttachment>> SMASH_BLOCKS = ATTACHMENT_TYPES.register("smash_blocks", () -> AttachmentType.builder(() -> new SmashBlocksEnchantmentAttachment()).serialize(SmashBlocksEnchantmentAttachment.CODEC).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<GameProfile>> ZOMBIFIED_PLAYER = ATTACHMENT_TYPES.register("zombified_player", () -> AttachmentType.builder(() -> UUIDUtil.createOfflineProfile("GizmoTheMoonPig")).serialize(Codecs.SIMPLE_GAME_PROFILE).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Unit>> LEASH_PATHFINDER_OVERRIDE = ATTACHMENT_TYPES.register("leashed_pathfinder_override", () -> AttachmentType.builder(() -> Unit.INSTANCE).serialize(MapCodec.unit(Unit.INSTANCE)).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Unit>> BANISHED_TO_TWILIGHT_FOREST = ATTACHMENT_TYPES.register("twilightforest_banished", () -> AttachmentType.builder(() -> Unit.INSTANCE).serialize(MapCodec.unit(Unit.INSTANCE)).copyOnDeath().build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<TravellersWingsAttachment>> TRAVELLERS_WINGS = ATTACHMENT_TYPES.register("travellers_wings", () -> AttachmentType.builder(TravellersWingsAttachment::new).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<TravellersWingsAnimAttachment>> TRAVELLERS_WINGS_ANIM = ATTACHMENT_TYPES.register("travellers_wings_anim", () -> AttachmentType.builder(TravellersWingsAnimAttachment::new).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> IS_USING_GOGGLES_ZOOM_MODIFIER = ATTACHMENT_TYPES.register("is_using_goggles_zoom_modifier", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL.fieldOf("zooming")).sync(ByteBufCodecs.BOOL).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> TRAVELLERS_GOGGLES_RED_THREAD_VISION = ATTACHMENT_TYPES.register("travellers_goggles_red_thread_vision", () -> AttachmentType.builder(() -> true).serialize(Codec.BOOL.fieldOf("red_thread_vision")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Long>> LAST_TICK_WATER_WALKING = ATTACHMENT_TYPES.register("last_tick_water_walking", () -> AttachmentType.builder(() -> 0L).serialize(Codec.LONG.fieldOf("last_water_walking_tick")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> HAS_DOUBLE_JUMP = ATTACHMENT_TYPES.register("has_double_jump", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL.fieldOf("double_jump")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> DOUBLE_JUMP_VALIDATOR = ATTACHMENT_TYPES.register("double_jump_validator", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("double_jump_count")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> DOUBLE_JUMP_VALIDATOR_LAST_CHECK = ATTACHMENT_TYPES.register("double_jump_validator_last_check", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("last_double_jump_count")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Double>> TEMPORARY_SAVED_STRAIGHT_AHEAD = ATTACHMENT_TYPES.register("temporary_saved_straight_ahead", () -> AttachmentType.builder(() -> 1D).serialize(Codec.DOUBLE.fieldOf("straight_ahead")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Long>> LAST_DAMAGE_ARMOR_TIME = ATTACHMENT_TYPES.register("last_damage_armor_time", () -> AttachmentType.builder(() -> 0L).serialize(Codec.LONG.fieldOf("last_armor_damage_timestamp")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> LAST_JUMP_KEY_PRESS_TIME = ATTACHMENT_TYPES.register("last_jump_key_press_time", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("last_jump_key_press")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Float>> LAST_HORIZONTAL_IMPULSE = ATTACHMENT_TYPES.register("last_horizontal_impulse", () -> AttachmentType.builder(() -> 0F).serialize(Codec.FLOAT.fieldOf("last_horizontal_impulse")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Float>> LAST_NON_ZERO_HORIZONTAL_IMPULSE = ATTACHMENT_TYPES.register("last_non_horizontal_impulse", () -> AttachmentType.builder(() -> 0F).serialize(Codec.FLOAT.fieldOf("last_non_horizontal_impulse")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> LAST_HORIZONTAL_WALKING_TIME = ATTACHMENT_TYPES.register("last_horizontal_walking_time", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("last_horizontal_walk_time")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> SIDESTEP_VALIDATOR = ATTACHMENT_TYPES.register("sidestep_validator", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("side_step_count")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> SIDESTEP_VALIDATOR_LAST_CHECK = ATTACHMENT_TYPES.register("sidestep_validator_last_check", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("last_side_step_count")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> IS_GRADUALLY_GLIDING = ATTACHMENT_TYPES.register("is_gradually_gliding", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL.fieldOf("gliding")).sync(ByteBufCodecs.BOOL).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<SlimySolesAttachment>> SLIMY_SOLES_BOUNCE_INFO = ATTACHMENT_TYPES.register("slimy_soles_bounce_info", () -> AttachmentType.builder(SlimySolesAttachment::new).serialize(SlimySolesAttachment.CODEC).build());
}
