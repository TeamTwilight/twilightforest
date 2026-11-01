package twilightforest.mixin.enums;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

/**
 * Ajoute la rarete {@code twilightforest:twilight} (nom d'objet en vert fonce).
 *
 * <p>Meme raison et meme moment que {@link DamageEffectsMixin} : ajoutee juste apres
 * {@code $VALUES}, elle precede {@code CODEC}, {@code BY_ID} et {@code STREAM_CODEC}.</p>
 *
 * <p>NeoForge passait un {@code UnaryOperator<Style>} ; le constructeur vanilla prend une
 * couleur, et celle du mod est justement {@code DARK_GREEN}. L'identifiant numerique doit suivre
 * les quatre raretes vanilla (0 a 3) : {@code BY_ID} est une table continue.</p>
 */
@Mixin(Rarity.class)
public abstract class RarityMixin {

	@Shadow(remap = false)
	@Final
	@Mutable
	private static Rarity[] $VALUES;

	@Inject(method = "<clinit>", remap = false, at = @At(value = "FIELD", opcode = Opcodes.PUTSTATIC, shift = At.Shift.AFTER, remap = false,
		target = "Lnet/minecraft/world/item/Rarity;$VALUES:[Lnet/minecraft/world/item/Rarity;"))
	private static void twilightforest$addTwilight(CallbackInfo ci) {
		Rarity[] values = $VALUES;
		Rarity twilight = RarityInvoker.twilightforest$create("TWILIGHTFOREST_TWILIGHT", values.length, values.length, "twilightforest:twilight", ChatFormatting.DARK_GREEN);
		Rarity[] extended = Arrays.copyOf(values, values.length + 1);
		extended[values.length] = twilight;
		$VALUES = extended;
	}
}
