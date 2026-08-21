package twilightforest.asm.transformers.beardifier;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleClassProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * New Fields:<br/>
 * {@code private it.unimi.dsi.fastutil.objects.ObjectListIterator<net.minecraft.world.level.levelgen.DensityFunction> twilightforest_customStructureDensities;}
 */
public class BeardifierClassTransformer extends SimpleClassProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("beardifier_class");
	}

	@Override
	public void transform(ClassNode node, SimpleTransformationContext context) {
		node.fields.add(new FieldNode(
			Opcodes.ACC_PUBLIC,
			"twilightforest_customStructureDensities",
			"Lit/unimi/dsi/fastutil/objects/ObjectListIterator;",
			"Lit/unimi/dsi/fastutil/objects/ObjectListIterator<Lnet/minecraft/world/level/levelgen/DensityFunction;>;",
			null
		));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target("net.minecraft.world.level.levelgen.Beardifier"));
	}

}
