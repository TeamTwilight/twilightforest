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
 * {@link twilightforest.asmhooks.EntityHooks#processWaterWalking}
 */
public class WaterWalkTransformer implements ITransformer<MethodNode> {
	@Override
	public MethodNode transform(MethodNode method, ITransformerVotingContext context) {
		ASMUtil.findInstructions(method, Opcodes.IRETURN).forEach(
			(instruction) -> method.instructions.insertBefore(instruction, ASMAPI.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new VarInsnNode(Opcodes.ALOAD, 1),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/EntityHooks",
					"processWaterWalking",
					"(ZLnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/material/FluidState;)Z",
					false
				))));
		return method;
	}

	@Override
	public TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.world.entity.LivingEntity",
			"canStandOnFluid",
			"(Lnet/minecraft/world/level/material/FluidState;)Z"
		));
	}

	@Override
	public TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}
}
