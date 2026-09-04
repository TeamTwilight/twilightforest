package twilightforest.asm.transformers.model;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.ModelHooks#shouldSpecialModelUpdate}
 */
public class AnimateSpecialModelsTransformer extends SimpleMethodProcessor {

	@Override
	public void transform(MethodNode input, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(
			input,
			Opcodes.INVOKEVIRTUAL,
			"net/minecraft/client/renderer/item/ItemStackRenderState",
			"newLayer",
			"()Lnet/minecraft/client/renderer/item/ItemStackRenderState$LayerRenderState;"
		).findFirst().ifPresent(target -> {
			LabelNode skip = new LabelNode();
			input.instructions.insert(
				target,
				ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 2),
					new MethodInsnNode(
						Opcodes.INVOKESTATIC,
						"twilightforest/asmhooks/ModelHooks",
						"shouldSpecialModelUpdate",
						"(Lnet/minecraft/world/item/ItemStack;)Z",
						false
					),
					new JumpInsnNode(Opcodes.IFEQ, skip),
					new VarInsnNode(Opcodes.ALOAD, 1),
					new MethodInsnNode(
						Opcodes.INVOKEVIRTUAL,
						"net/minecraft/client/renderer/item/ItemStackRenderState",
						"setAnimated",
						"()V",
						false
					),
					skip
				)
			);
		});
	}

	@Override
	public Set<Target> targets() {
		return Set.of(
			new Target(
				"net.minecraft.client.renderer.item.SpecialModelWrapper",
				"update",
				"(Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/item/ItemModelResolver;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/entity/ItemOwner;I)V"
			)
		);
	}

	@Override
	public ProcessorName name() {
		return ASMUtil.named("animate_special_models");
	}
}