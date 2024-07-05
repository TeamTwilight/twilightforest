package twilightforest.asm.transformers.lead;

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
 * {@link twilightforest.ASMHooks#leashFenceKnotSurvives}
 */
public class LeashFenceKnotSurvivesTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findInstructions(
			node,
			Opcodes.IRETURN
		).findFirst().ifPresent(target -> node.instructions.insertBefore(
			target,
			ASMAPI.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/ASMHooks",
					"leashFenceKnotSurvives",
					"(ZLnet/minecraft/world/entity/decoration/LeashFenceKnotEntity;)Z"
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
			"net.minecraft.world.entity.decoration.LeashFenceKnotEntity",
			"survives",
			"()Z"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}
