package twilightforest.mixin.enums;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Le constructeur prive de {@link Rarity}, appele par {@link RarityMixin}. */
@Mixin(Rarity.class)
public interface RarityInvoker {

	@Invoker("<init>")
	static Rarity twilightforest$create(String enumName, int ordinal, int id, String name, ChatFormatting color) {
		throw new AssertionError();
	}
}
