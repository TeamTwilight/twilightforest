package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;

import java.util.Locale;

public class ShieldCommand {
    public static LiteralArgumentBuilder<CommandSource> register() {
        return Commands.literal("shield")
                .requires(cs -> cs.hasPermissionLevel(2))
                .then(Commands.argument("target", EntityArgument.entity())
                    .then(Commands.literal("set")
                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                .executes(ctx -> set(EntityArgument.getEntity(ctx, "target"), IntegerArgumentType.getInteger(ctx, "amount"), true))
                                .then(Commands.argument("temp", BoolArgumentType.bool())
                                        .executes(ctx -> set(EntityArgument.getEntity(ctx, "target"), IntegerArgumentType.getInteger(ctx, "amount"), BoolArgumentType.getBool(ctx, "temp"))))))
                .then(Commands.literal("add")
                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                .executes(ctx -> add(EntityArgument.getEntity(ctx, "target"), IntegerArgumentType.getInteger(ctx, "amount"), true))
                                .then(Commands.argument("temp", BoolArgumentType.bool())
                                        .executes(ctx -> add(EntityArgument.getEntity(ctx, "target"), IntegerArgumentType.getInteger(ctx, "amount"), BoolArgumentType.getBool(ctx, "temp")))))));
    }

    private static int add(Entity e, int num, boolean temporary) {
        e.getCapability(CapabilityList.SHIELDS).ifPresent(cap -> cap.addShields(num, temporary));
        return Command.SINGLE_SUCCESS;
    }

    private static int set(Entity e, int num, boolean temporary) {
        e.getCapability(CapabilityList.SHIELDS).ifPresent(cap -> cap.setShields(num, temporary));
        return Command.SINGLE_SUCCESS;
    }
}
