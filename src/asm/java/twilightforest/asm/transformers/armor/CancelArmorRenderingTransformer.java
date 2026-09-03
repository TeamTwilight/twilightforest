package twilightforest.asm.transformers.armor;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.ArmorHooks#cancelArmorRendering}
 */
public class CancelArmorRenderingTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("cancel_armor_rendering");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findInstructions(node, Opcodes.INSTANCEOF)
			.findFirst()
			.ifPresent(target -> node.instructions.insert(
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

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer",
			"renderArmorPiece",
			"(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V"
		));
	}

}
