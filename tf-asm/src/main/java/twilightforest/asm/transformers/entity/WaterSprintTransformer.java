package twilightforest.asm.transformers.entity;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.EntityHooks#unrestrainedSprintingInWater}
 * {@link twilightforest.asmhooks.EntityHooks#unrestrainedSwimPredicate}
 */
public class WaterSprintTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("water_sprint");
	}

	private static void injectIsInWater(MethodNode node) {
		ASMUtil.findMethodInstructions(node, Opcodes.INVOKEVIRTUAL,
			"net/minecraft/client/player/LocalPlayer",
			"isInWater",
			"()Z"
		).forEach(m -> node.instructions.insert(m, ASMUtil.listOf(
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
		).forEach(call -> node.instructions.insertBefore(call, ASMUtil.listOf(
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
	public void transform(MethodNode node, SimpleTransformationContext context) {
		injectIsInWater(node);
		injectIsInFluidType(node);
	}

	@Override
	public Set<SimpleMethodProcessor.Target> targets() {
		return Set.of(new SimpleMethodProcessor.Target(
			"net.minecraft.client.player.LocalPlayer",
			"aiStep",
			"()V"
		));
	}
}
