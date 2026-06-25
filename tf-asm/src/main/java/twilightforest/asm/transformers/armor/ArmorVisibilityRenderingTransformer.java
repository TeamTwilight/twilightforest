package twilightforest.asm.transformers.armor;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;

/**
 * {@link twilightforest.asmhooks.ArmorHooks#modifyArmorVisibility}
 */
public class ArmorVisibilityRenderingTransformer extends SimpleMethodTransformer {

	public ArmorVisibilityRenderingTransformer() {
		super(
			"net.minecraft.world.entity.LivingEntity",
			"getVisibilityPercent",
			"(Lnet/minecraft/world/entity/Entity;)D"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findVarInstructions(method, Opcodes.FSTORE, 4)
			.findFirst()
			.ifPresent(target -> method.instructions.insertBefore(
				target,
				ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 0),
					new MethodInsnNode(
						Opcodes.INVOKESTATIC,
						"twilightforest/asmhooks/ArmorHooks",
						"modifyArmorVisibility",
						"(FLnet/minecraft/world/entity/LivingEntity;)F"
					)
				)
			));
	}

}
