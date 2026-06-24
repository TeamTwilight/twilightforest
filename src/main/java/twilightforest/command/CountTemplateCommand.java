package twilightforest.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.*;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

@tamaized.beanification.Component
public class CountTemplateCommand {
	public LiteralArgumentBuilder<CommandSourceStack> register() {
		return Commands.literal("count_template").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
			.then(Commands.argument("filter_structure", ResourceKeyArgument.key(Registries.STRUCTURE)).executes(this::countTemplates));
	}

	private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
	private static final Method TemplateStructurePiece_makeTemplateLocation = ObfuscationReflectionHelper.findMethod(TemplateStructurePiece.class, "makeTemplateLocation");
	private static final MethodHandle handle_TemplateStructurePiece_makeTemplateLocation;

	static {
		MethodHandle tmp_handle_TemplateStructurePiece_makeTemplateLocation = null;

		try {
			tmp_handle_TemplateStructurePiece_makeTemplateLocation =
				LOOKUP.unreflect(TemplateStructurePiece_makeTemplateLocation);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		handle_TemplateStructurePiece_makeTemplateLocation = tmp_handle_TemplateStructurePiece_makeTemplateLocation;
	}

	public static Identifier makeTemplateLocation(TemplateStructurePiece piece) {
		try {
			return (Identifier) handle_TemplateStructurePiece_makeTemplateLocation.invokeExact(piece);
		} catch (Throwable t) {
			throw new RuntimeException("Failed to invoke makeTemplateLocation", t);
		}
	}

	private int countTemplates(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Holder.Reference<Structure> structure = ResourceKeyArgument.getStructure(context, "filter_structure");

		if (!structure.isBound()) return 0;

		CommandSourceStack source = context.getSource();
		ServerLevel level = source.getLevel();
		BlockPos commandPos = BlockPos.containing(source.getPosition());

		StructureStart structureAt = level.structureManager().getStructureAt(commandPos, structure.value());

		Object2IntMap<Identifier> templateCounts = new Object2IntOpenHashMap<>();

		List<StructurePiece> structurePieces = structureAt.getPieces();
		for (StructurePiece piece : structurePieces) {
			if (piece instanceof TemplateStructurePiece templatePiece) {
				Identifier identifier = makeTemplateLocation(templatePiece);
				templateCounts.put(identifier, templateCounts.getOrDefault(identifier, 0) + 1);
			}
		}

		for (Object2IntMap.Entry<Identifier> countedTemplate : templateCounts.object2IntEntrySet().stream().sorted(Comparator.comparing(Object2IntMap.Entry::getKey)).sorted(Comparator.comparing(Object2IntMap.Entry::getIntValue)).toList()) {
			MutableComponent text = Component.literal(countedTemplate.getKey() + "    " + countedTemplate.getIntValue());
			context.getSource().sendSystemMessage(text);
		}

		return templateCounts.size();
	}
}
