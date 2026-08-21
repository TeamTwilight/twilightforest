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

/**
 * {@link twilightforest.asmhooks.EntityHooks#unrestrainedSprintingInWater}
 * {@link twilightforest.asmhooks.EntityHooks#unrestrainedSwimPredicate}
 */
public class WaterSprintTransformer implements ITransformer<MethodNode> {

	private static void injectIsInWater(MethodNode node) {
		ASMUtil.findMethodInstructions(node, Opcodes.INVOKEVIRTUAL,
			"net/minecraft/client/player/LocalPlayer",
			"isInWater",
			"()Z"
		).forEach(m -> node.instructions.insert(m, ASMAPI.listOf(
			new VarInsnNode(Opcodes.ALOAD, 0),
			new MethodInsnNode(
				Opcodes.INVOKESTATIC,
				"twilightforest/asmhooks/EntityHooks",
				"unrestrainedSprintingInWater",
				"(ZLnet/minecraft/world/entity/LivingEntity;)Z",
				false
			)
		)));
	}

	private static void injectIsInFluidType(MethodNode node) {
		ASMUtil.findMethodInstructions(node, Opcodes.INVOKEVIRTUAL,
			"net/minecraft/client/player/LocalPlayer",
			"isInFluidType",
			"(Ljava/util/function/BiPredicate;)Z"
		).forEach(call -> node.instructions.insertBefore(call, ASMAPI.listOf(
			new VarInsnNode(Opcodes.ALOAD, 0),
			new MethodInsnNode(
				Opcodes.INVOKESTATIC,
				"twilightforest/asmhooks/EntityHooks",
				"unrestrainedSwimPredicate",
				"(Ljava/util/function/BiPredicate;Lnet/minecraft/world/entity/LivingEntity;)Ljava/util/function/BiPredicate;",
				false
			)
		)));
	}

	@Override
	public MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		injectIsInWater(node);
		injectIsInFluidType(node);
		return node;
	}

	@Override
	public TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public java.util.Set<Target<MethodNode>> targets() {
		return java.util.Set.of(Target.targetMethod(
			"net.minecraft.client.player.LocalPlayer",
			"aiStep",
			"()V"
		));
	}

	@Override
	public TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}
}
