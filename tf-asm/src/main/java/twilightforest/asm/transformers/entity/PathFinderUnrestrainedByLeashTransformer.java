package twilightforest.asm.transformers.entity;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.EntityHooks#overrideStayCloseToHolder}
 */
public class PathFinderUnrestrainedByLeashTransformer extends SimpleMethodTransformer {

	public PathFinderUnrestrainedByLeashTransformer() {
		super(
			"net.minecraft.world.entity.PathfinderMob",
			"shouldStayCloseToLeashHolder",
			"()Z"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findInstructions(
			method,
			Opcodes.IRETURN
		).forEach(target -> method.instructions.insertBefore(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0), // PathfinderMob.this
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/EntityHooks",
					"overrideStayCloseToHolder",
					"(ZLnet/minecraft/world/entity/PathfinderMob;)Z"
				)
			)
		));
	}

}
