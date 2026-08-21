package twilightforest.asm.transformers.armor;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.neoforged.coremod.api.ASMAPI;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.ArmorHooks#fixCapeRendering}
 */
public class FixCapeUnrenderingTransformer implements ITransformer<MethodNode> {

	@Override
	public MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findMethodInstructions(
				node,
				Opcodes.INVOKEVIRTUAL,
				"net/minecraft/world/item/ItemStack",
				"is",
				"(Lnet/minecraft/world/item/Item;)Z"
		).findFirst().ifPresent(target -> node.instructions.insert(
				target,
				ASMAPI.listOf(
					new VarInsnNode(Opcodes.ALOAD, 12),
					new MethodInsnNode(
						Opcodes.INVOKESTATIC,
						"twilightforest/asmhooks/ArmorHooks",
						"fixCapeRendering",
						"(ZLnet/minecraft/world/item/ItemStack;)Z"
					)
				)
			));
		return node;
	}

	@Override
	public TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.client.renderer.entity.layers.CapeLayer",
			"render",
			"(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V"
		));
	}

	@Override
	public TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}
