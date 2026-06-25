package twilightforest.asm.transformers.entity;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.EntityHooks#unrestrainedSprintingInWater}
 * {@link twilightforest.asmhooks.EntityHooks#unrestrainedSwimPredicate}
 */
public class WaterSprintTransformer extends SimpleMethodTransformer {

	public WaterSprintTransformer() {
		super(
			"net.minecraft.client.player.LocalPlayer",
			"aiStep",
			"()V"
		);
	}

	private static void injectIsInWater(MethodNode method) {
		ASMUtil.findMethodInstructions(method, Opcodes.INVOKEVIRTUAL,
			"net/minecraft/client/player/LocalPlayer",
			"isInWater",
			"()Z"
		).forEach(m -> method.instructions.insert(m, ASMUtil.listOf(
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

	private static void injectIsInFluidType(MethodNode method) {
		ASMUtil.findMethodInstructions(method, Opcodes.INVOKEVIRTUAL,
			"net/minecraft/client/player/LocalPlayer",
			"isInFluidType",
			"(Ljava/util/function/BiPredicate;)Z"
		).forEach(call -> method.instructions.insertBefore(call, ASMUtil.listOf(
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
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		injectIsInWater(method);
		injectIsInFluidType(method);
	}

}
