package twilightforest.asm.transformers.entity;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.EntityHooks#resetStuckUnrestrained}
 */
public class ResetStuckUnrestrainedTransformer extends SimpleMethodTransformer {

	public ResetStuckUnrestrainedTransformer() {
		super(
			"net.minecraft.world.entity.Entity",
			"move",
			"(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext ctx) {
		ASMUtil.findFieldInstructions(
				method,
				Opcodes.GETFIELD,
				"net/minecraft/world/entity/Entity",
				"stuckSpeedMultiplier"
			)
			.forEach(target -> method.instructions.insertBefore(target, ASMUtil.listOf(
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/EntityHooks",
					"resetStuckUnrestrained",
					"(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/entity/Entity;"
				)
			)));
	}

}
