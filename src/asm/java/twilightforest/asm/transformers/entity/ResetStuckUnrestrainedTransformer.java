package twilightforest.asm.transformers.entity;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.EntityHooks#resetStuckUnrestrained}
 */
public class ResetStuckUnrestrainedTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("reset_stuck_unrestrained");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findFieldInstructions(
				node,
				Opcodes.GETFIELD,
				"net/minecraft/world/entity/Entity",
				"stuckSpeedMultiplier"
			)
			.forEach(target -> node.instructions.insertBefore(target, ASMUtil.listOf(
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/EntityHooks",
					"resetStuckUnrestrained",
					"(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/entity/Entity;"
				)
			)));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.world.entity.Entity",
			"move",
			"(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"
		));
	}
}

