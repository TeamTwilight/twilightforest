package twilightforest.init;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.Unit;
import twilightforest.forge.attachment.AttachmentType;
import twilightforest.forge.attachment.Attachments;
import twilightforest.TwilightForestMod;
import twilightforest.components.entity.*;
import twilightforest.util.Codecs;

public class TFDataAttachments {

	public static final AttachmentType<Boolean> FEATHER_FAN = Attachments.declare(AttachmentType.builder("feather_fan_falling", () -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());
	public static final AttachmentType<PotionFlaskTrackingAttachment> FLASK_DOSES = Attachments.declare(AttachmentType.builder("flask_doses", PotionFlaskTrackingAttachment::new).serialize(PotionFlaskTrackingAttachment.CODEC).build());
	public static final AttachmentType<FortificationShieldAttachment> FORTIFICATION_SHIELDS = Attachments.declare(AttachmentType.builder("fortification_shields", FortificationShieldAttachment::new).serialize(FortificationShieldAttachment.CODEC).sync(FortificationShieldAttachment.STREAM_CODEC).build());
	public static final AttachmentType<GiantPickaxeMiningAttachment> GIANT_PICKAXE_MINING = Attachments.declare(AttachmentType.builder("giant_pickaxe_mining", GiantPickaxeMiningAttachment::new).build());
	public static final AttachmentType<YetiThrowAttachment> YETI_THROWING = Attachments.declare(AttachmentType.builder("yeti_throwing", YetiThrowAttachment::new).build());
	public static final AttachmentType<MultiplayerInclusivityAttachment> MULTIPLAYER_FIGHT = Attachments.declare(AttachmentType.builder("multiplayer_fight", MultiplayerInclusivityAttachment::new).build());
	public static final AttachmentType<TFPortalAttachment> TF_PORTAL_COOLDOWN = Attachments.declare(AttachmentType.builder("tf_portal_cooldown", TFPortalAttachment::new).build());
	public static final AttachmentType<SmashBlocksEnchantmentAttachment> SMASH_BLOCKS = Attachments.declare(AttachmentType.builder("smash_blocks", () -> new SmashBlocksEnchantmentAttachment()).serialize(SmashBlocksEnchantmentAttachment.CODEC).build());
	public static final AttachmentType<GameProfile> ZOMBIFIED_PLAYER = Attachments.declare(AttachmentType.builder("zombified_player", () -> UUIDUtil.createOfflineProfile("GizmoTheMoonPig")).serialize(Codecs.SIMPLE_GAME_PROFILE).build());
	public static final AttachmentType<Unit> LEASH_PATHFINDER_OVERRIDE = Attachments.declare(AttachmentType.builder("leashed_pathfinder_override", () -> Unit.INSTANCE).serialize(Codec.unit(Unit.INSTANCE)).build());
	public static final AttachmentType<Unit> BANISHED_TO_TWILIGHT_FOREST = Attachments.declare(AttachmentType.builder("twilightforest_banished", () -> Unit.INSTANCE).serialize(Codec.unit(Unit.INSTANCE)).copyOnDeath().build());
	public static final AttachmentType<TravellersWingsAttachment> TRAVELLERS_WINGS = Attachments.declare(AttachmentType.builder("travellers_wings", TravellersWingsAttachment::new).build());
	public static final AttachmentType<TravellersWingsAnimAttachment> TRAVELLERS_WINGS_ANIM = Attachments.declare(AttachmentType.builder("travellers_wings_anim", TravellersWingsAnimAttachment::new).build());
	public static final AttachmentType<Boolean> IS_USING_GOGGLES_ZOOM_MODIFIER = Attachments.declare(AttachmentType.builder("is_using_goggles_zoom_modifier", () -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());
	public static final AttachmentType<Boolean> TRAVELLERS_GOGGLES_RED_THREAD_VISION = Attachments.declare(AttachmentType.builder("travellers_goggles_red_thread_vision", () -> true).serialize(Codec.BOOL).build());
	public static final AttachmentType<Long> LAST_TICK_WATER_WALKING = Attachments.declare(AttachmentType.builder("last_tick_water_walking", () -> 0L).serialize(Codec.LONG).build());
	public static final AttachmentType<Boolean> HAS_DOUBLE_JUMP = Attachments.declare(AttachmentType.builder("has_double_jump", () -> false).serialize(Codec.BOOL).build());
	public static final AttachmentType<Integer> DOUBLE_JUMP_VALIDATOR = Attachments.declare(AttachmentType.builder("double_jump_validator", () -> 0).serialize(Codec.INT).build());
	public static final AttachmentType<Integer> DOUBLE_JUMP_VALIDATOR_LAST_CHECK = Attachments.declare(AttachmentType.builder("double_jump_validator_last_check", () -> 0).serialize(Codec.INT).build());
	public static final AttachmentType<Double> TEMPORARY_SAVED_STRAIGHT_AHEAD = Attachments.declare(AttachmentType.builder("temporary_saved_straight_ahead", () -> 1D).serialize(Codec.DOUBLE).build());
	public static final AttachmentType<Long> LAST_DAMAGE_ARMOR_TIME = Attachments.declare(AttachmentType.builder("last_damage_armor_time", () -> 0L).serialize(Codec.LONG).build());
	public static final AttachmentType<Integer> LAST_JUMP_KEY_PRESS_TIME = Attachments.declare(AttachmentType.builder("last_jump_key_press_time", () -> 0).serialize(Codec.INT).build());
	public static final AttachmentType<Float> LAST_HORIZONTAL_IMPULSE = Attachments.declare(AttachmentType.builder("last_horizontal_impulse", () -> 0F).serialize(Codec.FLOAT).build());
	public static final AttachmentType<Float> LAST_NON_ZERO_HORIZONTAL_IMPULSE = Attachments.declare(AttachmentType.builder("last_non_horizontal_impulse", () -> 0F).serialize(Codec.FLOAT).build());
	public static final AttachmentType<Integer> LAST_HORIZONTAL_WALKING_TIME = Attachments.declare(AttachmentType.builder("last_horizontal_walking_time", () -> 0).serialize(Codec.INT).build());
	public static final AttachmentType<Integer> SIDESTEP_VALIDATOR = Attachments.declare(AttachmentType.builder("sidestep_validator", () -> 0).serialize(Codec.INT).build());
	public static final AttachmentType<Integer> SIDESTEP_VALIDATOR_LAST_CHECK = Attachments.declare(AttachmentType.builder("sidestep_validator_last_check", () -> 0).serialize(Codec.INT).build());
	public static final AttachmentType<Boolean> IS_GRADUALLY_GLIDING = Attachments.declare(AttachmentType.builder("is_gradually_gliding", () -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());
	public static final AttachmentType<SlimySolesAttachment> SLIMY_SOLES_BOUNCE_INFO = Attachments.declare(AttachmentType.builder("slimy_soles_bounce_info", SlimySolesAttachment::new).serialize(SlimySolesAttachment.CODEC).build());

}
