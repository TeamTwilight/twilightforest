package twilightforest.asm.transformers.multipart;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.neoforged.coremod.api.ASMAPI;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.MultipartHooks#sendDirtyEntityData}
 */
public class SendDirtyEntityDataTransformer implements ITransformer<MethodNode> {

	@Override
	public MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findFieldInstructions(
			node,
			Opcodes.GETFIELD,
			"net/minecraft/server/level/ServerEntity",
			"entity"
		).findFirst().ifPresent(target -> node.instructions.insert(
			target,
			ASMAPI.listOf(
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/MultipartHooks",
					"sendDirtyEntityData",
					"(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/entity/Entity;"
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
			"net.minecraft.server.level.ServerEntity",
			"sendDirtyEntityData",
			"()V"
		));
	}

	@Override
	public TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}
