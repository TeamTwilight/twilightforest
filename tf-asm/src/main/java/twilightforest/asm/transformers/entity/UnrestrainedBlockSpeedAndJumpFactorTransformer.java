package twilightforest.asm.transformers.entity;

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
 * {@link twilightforest.asmhooks.EntityHooks#resetFactorWithUnrestrained}
 */
public class UnrestrainedBlockSpeedAndJumpFactorTransformer implements ITransformer<MethodNode> {

	@Override
	public MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findInstructions(
			node,
			Opcodes.FRETURN
		).forEach(target -> node.instructions.insertBefore(
			target,
			ASMAPI.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/EntityHooks",
					"resetFactorWithUnrestrained",
					"(FLnet/minecraft/world/entity/Entity;)F"
				)
			)
		));
		return node;
	}

	@Override
	public TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.world.entity.Entity",
			"getBlockJumpFactor",
			"()F"
		), Target.targetMethod(
			"net.minecraft.world.entity.Entity",
			"getBlockSpeedFactor",
			"()F"
		));
	}

	@Override
	public TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}
}
