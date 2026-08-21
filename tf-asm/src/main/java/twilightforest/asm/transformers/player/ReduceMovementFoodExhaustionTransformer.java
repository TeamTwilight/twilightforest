package twilightforest.asm.transformers.player;

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
import java.util.stream.Stream;

/**
 * {@link twilightforest.asmhooks.PlayerHooks#getFoodExhaustion}
 */
public class ReduceMovementFoodExhaustionTransformer implements ITransformer<MethodNode> {
	@Override
	public TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

	@Override
	public MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		Stream.concat(
			ASMUtil.findMethodInstructions(node, Opcodes.INVOKEVIRTUAL,
				"net/minecraft/world/entity/player/Player",
				"causeFoodExhaustion",
				"(F)V"
			),
			ASMUtil.findMethodInstructions(node, Opcodes.INVOKEVIRTUAL,
				"net/minecraft/server/level/ServerPlayer",
				"causeFoodExhaustion",
				"(F)V"
			)
		).forEach(target -> node.instructions.insertBefore(
			target,
			ASMAPI.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/PlayerHooks",
					"getFoodExhaustion",
					"(FLnet/minecraft/world/entity/player/Player;)F"
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
		return Set.of(
			Target.targetMethod(
				"net/minecraft/server/level/ServerPlayer",
				"checkMovementStatistics",
				"(DDD)V"
			),
			Target.targetMethod(
				"net/minecraft/world/entity/player/Player",
				"jumpFromGround",
				"()V"
			)
		);
	}
}
