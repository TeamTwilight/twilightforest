package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.FakePlayer;
import twilightforest.TFRegistries;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorItem;
import twilightforest.item.travellers_gear.modifiers.InsertableTravellersModifier;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.function.Function;

@twilightforest.beans.Component
public class TravellersGearCommand {

	private static final DynamicCommandExceptionType ERROR_INVALID_MODIFIER = new DynamicCommandExceptionType(p_304101_ -> Component.translatableEscape("commands.tffeature.invalid_modifier", p_304101_));
	private final SimpleCommandExceptionType ERROR_NOT_RUN_BY_PLAYER = new SimpleCommandExceptionType(Component.translatable("commands.tffeature.not_player"));
	private final SimpleCommandExceptionType ERROR_NOT_HOLDING_GEAR = new SimpleCommandExceptionType(Component.translatable("commands.tffeature.not_travellers_gear"));
	private final SimpleCommandExceptionType ERROR_TOO_MANY_MODIFIERS = new SimpleCommandExceptionType(Component.translatable("commands.tffeature.too_many_modifiers"));
	private final Function<Component, SimpleCommandExceptionType> ERROR_NO_MODIFIER = component -> new SimpleCommandExceptionType(Component.translatable("commands.tffeature.no_modifier", component));
	private final Function<Component, SimpleCommandExceptionType> ERROR_HAS_MODIFIER = component -> new SimpleCommandExceptionType(Component.translatable("commands.tffeature.has_modifier", component));
	private final Function<Component, SimpleCommandExceptionType> ERROR_WRONG_SLOT = component -> new SimpleCommandExceptionType(Component.translatable("commands.tffeature.wrong_modifier_slot", component));
	private final SimpleCommandExceptionType ERROR_ABILITY = new SimpleCommandExceptionType(Component.translatable("commands.tffeature.ability_modifier"));

	public LiteralArgumentBuilder<CommandSourceStack> register() {
		return Commands.literal("travellers_gear").requires(cs -> cs.hasPermission(Commands.LEVEL_GAMEMASTERS))
			.then(Commands.literal("add_modifier")
				.then(Commands.argument("modifier", ResourceKeyArgument.key(TFRegistries.Keys.TRAVELLERS_MODIFIERS))
					.executes(context -> this.addModifier(context.getSource(), ResourceKeyArgument.resolveKey(context, "modifier", TFRegistries.Keys.TRAVELLERS_MODIFIERS, ERROR_INVALID_MODIFIER)))))
				.then(Commands.literal("remove_modifier")
					.then(Commands.argument("modifier", ResourceKeyArgument.key(TFRegistries.Keys.TRAVELLERS_MODIFIERS))
						.executes(context -> this.removeModifier(context.getSource(), ResourceKeyArgument.resolveKey(context, "modifier", TFRegistries.Keys.TRAVELLERS_MODIFIERS, ERROR_INVALID_MODIFIER)))));
	}

	private int addModifier(CommandSourceStack source, Holder.Reference<TravellersModifier> modifier) throws CommandSyntaxException {
		Component modKey = Component.translatable(modifier.key().location().toLanguageKey(modifier.value().getPrefix()));
		if (!(source.getEntity() instanceof Player player) || player instanceof FakePlayer) throw ERROR_NOT_RUN_BY_PLAYER.create();
		if (!(player.getMainHandItem().getItem() instanceof TravellersArmorItem armor)) throw ERROR_NOT_HOLDING_GEAR.create();
		if (modifier.value().isAbility()) throw ERROR_ABILITY.create();
		if (TravellersModifiersManager.countInsertableModifiers(source.registryAccess(), player.getMainHandItem()) >= armor.getModifierSlots()) throw ERROR_TOO_MANY_MODIFIERS.create();
		if (TravellersModifiersManager.hasTravellersModifier(source.registryAccess(), player.getMainHandItem(), modifier.key())) throw ERROR_HAS_MODIFIER.apply(modKey).create();
		if (!modifier.value().group().test(player.getEquipmentSlotForItem(player.getMainHandItem()))) throw ERROR_WRONG_SLOT.apply(modKey).create();

		TravellersModifiersManager.addModifier(source.registryAccess(), player.getMainHandItem(), modifier.key());
		source.sendSuccess(() -> Component.translatable("commands.tffeature.added_modifier", modKey, player.getMainHandItem().getHoverName()), true);
		return Command.SINGLE_SUCCESS;
	}

	private int removeModifier(CommandSourceStack source, Holder.Reference<TravellersModifier> modifier) throws CommandSyntaxException {
		Component modKey = Component.translatable(modifier.key().location().toLanguageKey(modifier.value().getPrefix()));
		if (!(source.getEntity() instanceof Player player) || player instanceof FakePlayer) throw ERROR_NOT_RUN_BY_PLAYER.create();
		if (!(player.getMainHandItem().getItem() instanceof TravellersArmorItem)) throw ERROR_NOT_HOLDING_GEAR.create();
		if (modifier.value().isAbility()) throw ERROR_ABILITY.create();
		if (!TravellersModifiersManager.hasTravellersModifier(source.registryAccess(), player.getMainHandItem(), modifier.key())) throw ERROR_NO_MODIFIER.apply(modKey).create();

		((InsertableTravellersModifier) modifier.value()).removeModifier(player.getMainHandItem());
		source.sendSuccess(() -> Component.translatable("commands.tffeature.removed_modifier", modKey, player.getMainHandItem().getHoverName()), true);
		return Command.SINGLE_SUCCESS;
	}
}
