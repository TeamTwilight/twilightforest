package twilightforest.asm.transformers.render;

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
 * {@link twilightforest.asmhooks.RenderHooks#applyHeadVisibility}
 */
public class HideHeadUnderTrophyTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("hide_head_under_trophy");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(
			node,
			Opcodes.INVOKEVIRTUAL,
			"net/minecraft/client/model/Model",
			"setupAnim",
			"(Ljava/lang/Object;)V"
		).findFirst().ifPresent(target -> {
			node.instructions.insert(target, ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 1),
				new MethodInsnNode(Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/RenderHooks",
					"applyHeadVisibility",
					"(Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit;)V",
					false)
			));
		});
	}

	@Override
	public Set<SimpleMethodProcessor.Target> targets() {
		return Set.of(new SimpleMethodProcessor.Target(
				"net.minecraft.client.renderer.feature.ModelFeatureRenderer",
				"renderModel",
				"(Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit;Lnet/minecraft/client/renderer/rendertype/RenderType;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/renderer/OutlineBufferSource;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;)V"
			)
		);
	}

}
