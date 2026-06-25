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
 * {@link twilightforest.asmhooks.ArmorHooks#fixCapeRendering}
 */
public class FixCapeUnrenderingTransformer extends SimpleMethodTransformer {

	public FixCapeUnrenderingTransformer() {
		super(
			"net.minecraft.client.renderer.entity.layers.CapeLayer",
			"render",
			"(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(
				method,
				Opcodes.INVOKEVIRTUAL,
				"net/minecraft/world/item/ItemStack",
				"is",
				"(Lnet/minecraft/world/item/Item;)Z"
		).findFirst().ifPresent(target -> method.instructions.insert(
				target,
				ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 12),
					new MethodInsnNode(
						Opcodes.INVOKESTATIC,
						"twilightforest/asmhooks/ArmorHooks",
						"fixCapeRendering",
						"(ZLnet/minecraft/world/item/ItemStack;)Z"
					)
				)
			));
	}

}
