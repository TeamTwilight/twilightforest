package twilightforest.asm.transformers.map;

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
 * {@link twilightforest.ASMHooks#shouldMapRenderInArm}
 */
public class ShouldMapRenderInArmTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findFieldInstructions(
			node,
			Opcodes.GETSTATIC,
			"net/minecraft/world/item/Items",
			"FILLED_MAP"
		).findFirst().flatMap(searchTarget -> ASMUtil.findMethodInstructions(
			node,
			searchTarget,
			Opcodes.INVOKEVIRTUAL,
			"net/minecraft/world/item/ItemStack",
			"is",
			"(Lnet/minecraft/world/item/Item;)Z"
		).findFirst()).ifPresent(target -> node.instructions.insert(
			target,
			ASMAPI.listOf(
				new VarInsnNode(Opcodes.ALOAD, 6),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/ASMHooks",
					"shouldMapRenderInArm",
					"(ZLnet/minecraft/world/item/ItemStack;)Z"
				)
			)
		));
		return node;
	}

	@Override
	public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public @NotNull Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.client.renderer.ItemInHandRenderer",
			"renderArmWithItem",
			"(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}
