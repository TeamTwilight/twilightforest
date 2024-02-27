package twilightforest.init;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.capabilities.*;

public class TFDataAttachments {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, TwilightForestMod.ID);

	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> FEATHER_FAN = ATTACHMENT_TYPES.register("feather_fan_falling", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<PotionFlaskTrackingAttachment>> FLASK_DOSES = ATTACHMENT_TYPES.register("flask_doses", () -> AttachmentType.builder(PotionFlaskTrackingAttachment::new).serialize(PotionFlaskTrackingAttachment.CODEC).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<FortificationShieldAttachment>> FORTIFICATION_SHIELDS = ATTACHMENT_TYPES.register("fortification_shields", () -> AttachmentType.builder(FortificationShieldAttachment::new).serialize(FortificationShieldAttachment.CODEC).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<GiantPickaxeMiningAttachment>> GIANT_PICKAXE_MINING = ATTACHMENT_TYPES.register("giant_pickaxe_mining", () -> AttachmentType.builder(GiantPickaxeMiningAttachment::new).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<OreScannerAttachment>> ORE_SCANNER = ATTACHMENT_TYPES.register("ore_scanner", () -> AttachmentType.builder(OreScannerAttachment::getEmpty).serialize(OreScannerAttachment.Serializer.INSTANCE).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<YetiThrowAttachment>> YETI_THROWING = ATTACHMENT_TYPES.register("yeti_throwing", () -> AttachmentType.builder(YetiThrowAttachment::new).build());
}
