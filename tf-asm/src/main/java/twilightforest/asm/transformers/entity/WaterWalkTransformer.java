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
 * {@link twilightforest.asmhooks.EntityHooks#processWaterWalking}
 */
public class WaterWalkTransformer extends SimpleMethodTransformer {

	public WaterWalkTransformer() {
		super(
			"net.minecraft.world.entity.LivingEntity",
			"canStandOnFluid",
			"(Lnet/minecraft/world/level/material/FluidState;)Z"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findInstructions(method, Opcodes.IRETURN).forEach(
			(instruction) -> method.instructions.insertBefore(instruction, ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new VarInsnNode(Opcodes.ALOAD, 1),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/EntityHooks",
					"processWaterWalking",
					"(ZLnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/material/FluidState;)Z",
					false
				))));
	}

}
