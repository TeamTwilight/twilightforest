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

import java.util.Optional;
import java.util.Set;

/**
 * {@link twilightforest.ASMHooks#resolveEntitiesForRendering}
 */
public class ResolveEntitiesForRendereringTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findMethodInstructions(
			node,
			Opcodes.INVOKEVIRTUAL,
			"net/minecraft/client/multiplayer/ClientLevel",
			"entitiesForRendering",
			"()Ljava/lang/Iterable;"
		).map(searchTarget -> ASMUtil.findMethodInstructions(
			node,
			searchTarget,
			Opcodes.INVOKEINTERFACE,
			"java/lang/Iterable",
			"iterator",
			"()Ljava/util/Iterator;"
		).findFirst()).filter(Optional::isPresent).map(Optional::get).forEach(target -> node.instructions.insert(
			target,
			ASMAPI.listOf(
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/ASMHooks",
					"resolveEntitiesForRendering",
					"(Ljava/lang/Iterator;)Ljava/lang/Iterator;"
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
			"net.minecraft.client.renderer.LevelRenderer",
			"renderLevel",
			"(Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}
