package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.world.registration.TFGenerationSettings;

import java.util.Optional;

//FIXME bring back once conquer flag is in place
public class ConquerCommand {
	private static final SimpleCommandExceptionType NOT_IN_STRUCTURE = new SimpleCommandExceptionType(new TranslatableComponent("commands.tffeature.structure.required"));

	public static LiteralArgumentBuilder<CommandSourceStack> register() {
		LiteralArgumentBuilder<CommandSourceStack> conquer = Commands.literal("conquer").requires(cs -> cs.hasPermission(2)).executes(ctx -> changeStructureActivity(ctx.getSource(), true));
		LiteralArgumentBuilder<CommandSourceStack> reactivate = Commands.literal("reactivate").requires(cs -> cs.hasPermission(2)).executes(ctx -> changeStructureActivity(ctx.getSource(), false));
		return conquer.then(reactivate);
	}

	private static int changeStructureActivity(CommandSourceStack source, boolean flag) throws CommandSyntaxException {
		if (!TFGenerationSettings.usesTwilightChunkGenerator(source.getLevel())) {
			throw TFCommand.NOT_IN_TF.create();
		}

		// are you in a structure?
		ChunkGeneratorTwilight chunkGenerator = WorldUtil.getChunkGenerator(source.getLevel());

		BlockPos pos = new BlockPos(source.getPosition());
		if (chunkGenerator != null/* && chunkGenerator.isBlockInStructureBB(pos)*/) {
			Optional<TFStructureStart<?>.Start> struct = TFGenerationSettings.locateTFStructureInRange(source.getLevel(), pos, 0).map(s -> (TFStructureStart<?>.Start) s);
			if(struct.isEmpty())
				throw NOT_IN_STRUCTURE.create();
			struct.ifPresent(structure -> {
				//source.sendSuccess(new TranslatableComponent("This command currently isnt working as the structure conquered flag has not been reimplemented yet.").withStyle(ChatFormatting.RED), false);
				source.sendSuccess(new TranslatableComponent("commands.tffeature.structure.conquer.update", structure.isConquered(), flag), true);
				structure.setConquered(flag);
			});
			return Command.SINGLE_SUCCESS;
		} else {
			throw NOT_IN_STRUCTURE.create();
		}
	}
}
