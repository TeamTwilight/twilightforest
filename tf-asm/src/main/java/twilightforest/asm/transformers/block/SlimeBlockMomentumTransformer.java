package twilightforest.asm.transformers.block;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.BlockHooks#resetSlimeMomentumWithUnrestrained}
 */
public class SlimeBlockMomentumTransformer extends SimpleMethodTransformer {

	public SlimeBlockMomentumTransformer() {
		super(
			"net.minecraft.world.level.block.SlimeBlock",
			"stepOn",
			"(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/Entity;)V"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(
			method,
			Opcodes.INVOKEVIRTUAL,
			"net/minecraft/world/entity/Entity",
			"isSteppingCarefully",
			"()Z"
		).forEach(target -> method.instructions.insert(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 4),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/BlockHooks",
					"resetSlimeMomentumWithUnrestrained",
					"(ZLnet/minecraft/world/entity/Entity;)Z"
				)
			)
		));
	}
}
