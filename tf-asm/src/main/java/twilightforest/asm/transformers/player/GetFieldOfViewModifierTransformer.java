package twilightforest.asm.transformers.player;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.neoforged.coremod.api.ASMAPI;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;

import java.util.Optional;
import java.util.Set;

/**
 * {@link twilightforest.asmhooks.PlayerHooks#straightAheadNullify}
 * {@link twilightforest.asmhooks.PlayerHooks#straightAheadRestore}
 */
public class GetFieldOfViewModifierTransformer implements ITransformer<MethodNode> {

	@Override
	public TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

	@Override
	public MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findInstructions(node, Opcodes.FCONST_1).findFirst().ifPresent(target -> {
			node.instructions.insertBefore(target, ASMAPI.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/PlayerHooks",
					"straightAheadNullify",
					"(Lnet/minecraft/client/player/AbstractClientPlayer;)V",
					false)
			));

			ASMUtil.findInstructions(node, Opcodes.FRETURN).forEach(insn ->
				node.instructions.insertBefore(insn, ASMAPI.listOf(
					new VarInsnNode(Opcodes.ALOAD, 0),
					new MethodInsnNode(Opcodes.INVOKESTATIC,
						"twilightforest/asmhooks/PlayerHooks",
						"straightAheadRestore",
						"(Lnet/minecraft/client/player/AbstractClientPlayer;)V",
						false)
				))
			);
		});

		return node;
	}

	@Override
	public TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public Set<Target<MethodNode>> targets() {
		return Set.of(
			Target.targetMethod(
				"net/minecraft/client/player/AbstractClientPlayer",
				"getFieldOfViewModifier",
				"()F"
			)
		);
	}
}
