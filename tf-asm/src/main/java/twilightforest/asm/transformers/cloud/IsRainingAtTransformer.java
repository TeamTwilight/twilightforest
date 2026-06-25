package twilightforest.asm.transformers.cloud;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.BlockHooks#isRainingAt}
 */
public class IsRainingAtTransformer extends SimpleMethodTransformer {

	public IsRainingAtTransformer() {
		super(
			"net.minecraft.world.level.Level",
			"isRainingAt",
			"(Lnet/minecraft/core/BlockPos;)Z"
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
				new VarInsnNode(Opcodes.ALOAD, 0),
				new VarInsnNode(Opcodes.ALOAD, 1),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/BlockHooks",
					"isRainingAt",
					"(ZLnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"
				)
			)
		));
	}

}
