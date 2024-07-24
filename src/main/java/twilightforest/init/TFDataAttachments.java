package twilightforest.init;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.components.entity.*;
import twilightforest.components.item.OreScannerComponent;

public class TFDataAttachments {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, TwilightForestMod.ID);

	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> FEATHER_FAN = ATTACHMENT_TYPES.register("feather_fan_falling", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<PotionFlaskTrackingAttachment>> FLASK_DOSES = ATTACHMENT_TYPES.register("flask_doses", () -> AttachmentType.builder(() -> new PotionFlaskTrackingAttachment()).serialize(PotionFlaskTrackingAttachment.CODEC).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<FortificationShieldAttachment>> FORTIFICATION_SHIELDS = ATTACHMENT_TYPES.register("fortification_shields", () -> AttachmentType.builder(FortificationShieldAttachment::new).serialize(FortificationShieldAttachment.CODEC).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<GiantPickaxeMiningAttachment>> GIANT_PICKAXE_MINING = ATTACHMENT_TYPES.register("giant_pickaxe_mining", () -> AttachmentType.builder(GiantPickaxeMiningAttachment::new).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<OreScannerComponent>> ORE_SCANNER = ATTACHMENT_TYPES.register("ore_scanner", () -> AttachmentType.builder(OreScannerComponent::getEmpty).serialize(OreScannerComponent.CODEC).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<YetiThrowAttachment>> YETI_THROWING = ATTACHMENT_TYPES.register("yeti_throwing", () -> AttachmentType.builder(YetiThrowAttachment::new).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<MultiplayerInclusivityAttachment>> MULTIPLAYER_FIGHT = ATTACHMENT_TYPES.register("multiplayer_fight", () -> AttachmentType.builder(MultiplayerInclusivityAttachment::new).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<TFPortalAttachment>> TF_PORTAL_COOLDOWN = ATTACHMENT_TYPES.register("tf_portal_cooldown", () -> AttachmentType.builder(TFPortalAttachment::new).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<SmashBlocksEnchantmentAttachment>> SMASH_BLOCKS = ATTACHMENT_TYPES.register("smash_blocks", () -> AttachmentType.builder(() -> new SmashBlocksEnchantmentAttachment()).serialize(SmashBlocksEnchantmentAttachment.CODEC).build());
}
