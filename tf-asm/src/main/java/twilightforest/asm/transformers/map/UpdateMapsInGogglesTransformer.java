package twilightforest.asm.transformers.map;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.MapHooks#updateMapsInGoggles}
 */
public class UpdateMapsInGogglesTransformer extends SimpleMethodTransformer {

	public UpdateMapsInGogglesTransformer() {
		super(
			"net.minecraft.world.level.saveddata.maps.MapItemSavedData",
			"tickCarriedBy",
			"(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(
			method,
			Opcodes.INVOKEVIRTUAL,
			"net/minecraft/world/entity/player/Inventory",
			"contains",
			"(Ljava/util/function/Predicate;)Z"
		).forEach(target -> method.instructions.insert(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 2),
				new VarInsnNode(Opcodes.ALOAD, 1),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/MapHooks",
					"updateMapsInGoggles",
					"(ZLnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;)Z"
				)
			)
		));
	}

}
