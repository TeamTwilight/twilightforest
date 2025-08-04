package twilightforest.asm.transformers.entity;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.EntityHooks#processWaterWalking}
 */
public class WaterWalkTransformer implements ITransformer<MethodNode> {

	/*
		 We insert roughly this at the beginning of LivingEntity::canStandOnFluid

		 Boolean tmp = EntityHooks.processWaterWalking(this, fluidState);
		 if (tmp != null) {
			 return tmp.booleanValue();
		 }
	 */

	@Override
	public @NotNull MethodNode transform(MethodNode method, ITransformerVotingContext context) {
		InsnList list = new InsnList();

		// Reserve a new local variable slot for our Boolean result.
		int resultVar = method.maxLocals;
		method.maxLocals += 1;
		LabelNode continueLabel = new LabelNode();  // label for continuing after our method didn't return true or false

		list.add(new VarInsnNode(Opcodes.ALOAD, 0));
		list.add(new VarInsnNode(Opcodes.ALOAD, 1));

		list.add(new MethodInsnNode(
			Opcodes.INVOKESTATIC,
			"twilightforest/asmhooks/EntityHooks",
			"processWaterWalking",
			"(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/material/FluidState;)Ljava/lang/Boolean;",
			false
		));

		list.add(new VarInsnNode(Opcodes.ASTORE, resultVar));

		list.add(new VarInsnNode(Opcodes.ALOAD, resultVar));
		list.add(new JumpInsnNode(Opcodes.IFNULL, continueLabel));

		// If not null, unbox the Boolean and return its primitive value.
		list.add(new VarInsnNode(Opcodes.ALOAD, resultVar));
		list.add(new MethodInsnNode(
			Opcodes.INVOKEVIRTUAL,
			"java/lang/Boolean",
			"booleanValue",
			"()Z",
			false
		));
		list.add(new InsnNode(Opcodes.IRETURN));

		list.add(continueLabel);

		method.instructions.insert(list);
		return method;
	}

	@Override
	public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public @NotNull Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.world.entity.LivingEntity",
			"canStandOnFluid",
			"(Lnet/minecraft/world/level/material/FluidState;)Z"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}
}
