package twilightforest.asm.transformers.abstractclientplayer;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.neoforged.coremod.api.ASMAPI;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.ASMHooks#forwardBoostNullify}
 * {@link twilightforest.ASMHooks#forwardBoostRestore}
 */
public class GetFieldOfViewModifierTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		node.instructions.insert(ASMAPI.listOf(
			new VarInsnNode(Opcodes.ALOAD, 0),
			new MethodInsnNode(Opcodes.INVOKESTATIC,
				"twilightforest/ASMHooks",
				"forwardBoostNullify",
				"(Lnet/minecraft/client/player/AbstractClientPlayer;)V",
				false)
		));

		ASMUtil.findInstructions(node, Opcodes.FRETURN).forEach(insn ->
			node.instructions.insertBefore(insn, ASMAPI.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(Opcodes.INVOKESTATIC,
					"twilightforest/ASMHooks",
					"forwardBoostRestore",
					"(Lnet/minecraft/client/player/AbstractClientPlayer;)V",
					false)
			))
		);

		return node;
	}

	@Override
	public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public @NotNull Set<Target<MethodNode>> targets() {
		return Set.of(
			Target.targetMethod(
				"net/minecraft/client/player/AbstractClientPlayer",
				"getFieldOfViewModifier",
				"()F"
			)
		);
	}
}
