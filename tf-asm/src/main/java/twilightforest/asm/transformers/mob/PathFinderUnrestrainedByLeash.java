package twilightforest.asm.transformers.mob;

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

public class PathFinderUnrestrainedByLeash implements ITransformer<MethodNode> {
	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext iTransformerVotingContext) {
		ASMUtil.findInstructions(
			node,
			Opcodes.IRETURN
		).forEach(target -> node.instructions.insertBefore(
			target,
			ASMAPI.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0), // PathfinderMob.this
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/ASMHooks",
					"overrideStayCloseToHolder",
					"(ZLnet/minecraft/world/entity/PathfinderMob;)Z"
				)
			)
		));

		return node;
	}

	@Override
	public @NotNull TransformerVoteResult castVote(ITransformerVotingContext iTransformerVotingContext) {
		return TransformerVoteResult.YES;
	}

	@Override
	public @NotNull Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.world.entity.PathfinderMob",
			"shouldStayCloseToLeashHolder",
			"()Z"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}
}
