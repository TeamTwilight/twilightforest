package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.init.TFDataAttachments;

public class ShieldCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("shield")
                .requires(cs -> cs.hasPermission(2))
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
        if (e instanceof LivingEntity living) {
            living.getData(TFDataAttachments.FORTIFICATION_SHIELDS).addShields(living, num, temporary);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int set(Entity e, int num, boolean temporary) {
        if (e instanceof LivingEntity living) {
            living.getData(TFDataAttachments.FORTIFICATION_SHIELDS).setShields(living, num, temporary);
        }
        return Command.SINGLE_SUCCESS;
    }
}
