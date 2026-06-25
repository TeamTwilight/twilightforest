package twilightforest.asm.transformers.block;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.BlockHooks#stopBouncing}
 */
public final class SlimeBlockBounceUpTransformer extends SimpleMethodTransformer {

	public SlimeBlockBounceUpTransformer() {
		super(
			"net.minecraft.world.level.block.SlimeBlock",
			"bounceUp",
			"(Lnet/minecraft/world/entity/Entity;)V"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(
			method,
			Opcodes.INVOKEVIRTUAL,
			"net/minecraft/world/entity/Entity",
			"getDeltaMovement",
			"()Lnet/minecraft/world/phys/Vec3;"
		).findFirst().ifPresent(target -> method.instructions.insertBefore(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 1),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/BlockHooks",
					"stopBouncing",
					"(Lnet/minecraft/world/entity/Entity;)V",
					false
				)
			)
		));
	}
}
