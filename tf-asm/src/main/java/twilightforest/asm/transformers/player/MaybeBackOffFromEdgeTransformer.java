package twilightforest.asm.transformers.player;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.neoforged.coremod.api.ASMAPI;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.PlayerHooks#cancelHighStepModifierForStepDownDuringSneaking}
 */
public class MaybeBackOffFromEdgeTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findInstructions(node, Opcodes.INVOKEVIRTUAL)
			.filter(insn -> insn instanceof MethodInsnNode)
			.map(insn -> (MethodInsnNode) insn)
			.filter(mn -> "net/minecraft/world/entity/player/Player".equals(mn.owner)
				&& "maxUpStep".equals(mn.name)
				&& "()F".equals(mn.desc))
			.findFirst()
			.ifPresent(target -> node.instructions.insert(target, ASMAPI.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new InsnNode(Opcodes.SWAP),  // Swap so that the original float (returned by maxUpStep) is below the player.
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/PlayerHooks",
					"cancelHighStepModifierForStepDownDuringSneaking",
					"(Lnet/minecraft/world/entity/player/Player;F)F",
					false
				)
			)));
		return node;
	}

	@Override
	public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public @NotNull Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.world/entity/player/Player",
			"maybeBackOffFromEdge",
			"(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/entity/MoverType;)Lnet/minecraft/world/phys/Vec3;"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}
}
