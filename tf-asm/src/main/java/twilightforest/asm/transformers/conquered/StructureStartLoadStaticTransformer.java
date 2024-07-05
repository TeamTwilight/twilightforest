package twilightforest.asm.transformers.conquered;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.neoforged.coremod.api.ASMAPI;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.ASMHooks#loadStaticStart}
 */
public class StructureStartLoadStaticTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findMethodInstructions(
			node,
			Opcodes.INVOKESPECIAL,
			"net/minecraft/world/level/levelgen/structure/StructureStart",
			"<init>",
			"(Lnet/minecraft/world/level/levelgen/structure/Structure;Lnet/minecraft/world/level/ChunkPos;ILnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;)V"
		).findFirst().ifPresent(target -> node.instructions.insert(
			target,
			ASMAPI.listOf(
				new VarInsnNode(Opcodes.ALOAD, 10),
				new VarInsnNode(Opcodes.ALOAD, 1),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/ASMHooks",
					"loadStaticStart",
					"(Lnet/minecraft/world/level/levelgen/structure/StructureStart;Lnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/level/levelgen/structure/StructureStart;"
				)
			)
		));
		return node;
	}

	@Override
	public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public @NotNull Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.world.level.levelgen.structure.StructureStart",
			"loadStaticStart",
			"(Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;Lnet/minecraft/nbt/CompoundTag;J)Lnet/minecraft/world/level/levelgen/structure/StructureStart;"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}
