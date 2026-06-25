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
 * {@link twilightforest.asmhooks.ArmorHooks#cancelArmorRendering}
 */
public class CancelElytraRenderingTransformer extends SimpleMethodTransformer {

	public CancelElytraRenderingTransformer() {
		super(
			"net.minecraft.client.renderer.entity.layers.ElytraLayer",
			"shouldRender",
			"(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)Z"
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
					new VarInsnNode(Opcodes.ALOAD, 1),
					new MethodInsnNode(
						Opcodes.INVOKESTATIC,
						"twilightforest/asmhooks/ArmorHooks",
						"cancelArmorRendering",
						"(ZLnet/minecraft/world/item/ItemStack;)Z"
					)
				)
			));
	}

}
