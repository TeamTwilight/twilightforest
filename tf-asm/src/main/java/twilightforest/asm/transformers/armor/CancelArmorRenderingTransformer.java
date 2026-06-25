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
public class CancelArmorRenderingTransformer extends SimpleMethodTransformer {

	public CancelArmorRenderingTransformer() {
		super(
			"net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer",
			"renderArmorPiece",
			"(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findInstructions(method, Opcodes.INSTANCEOF)
			.findFirst()
			.ifPresent(target -> method.instructions.insert(
				target,
				ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 13),
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
