package twilightforest.client.model.block.forcefield;

import com.mojang.math.Quadrant;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.FaceBakery;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.ExtraFaceData;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record UnbakedForceFieldBlockStateModel(Material texture, boolean ambientOcclusion, List<ForceFieldElement> elements) implements CustomUnbakedBlockStateModel {

	public static final MapCodec<UnbakedForceFieldBlockStateModel> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Material.CODEC.fieldOf("texture").forGetter(UnbakedForceFieldBlockStateModel::texture),
		Codec.BOOL.optionalFieldOf("ambient_occlusion", true).forGetter(UnbakedForceFieldBlockStateModel::ambientOcclusion),
		ForceFieldElement.CODEC.listOf().fieldOf("elements").forGetter(UnbakedForceFieldBlockStateModel::elements)
	).apply(instance, UnbakedForceFieldBlockStateModel::new));

	@Override
	public BlockStateModel bake(ModelBaker modelBaker) {
		Material.Baked material = modelBaker.materials().get(this.texture, this.texture.sprite()::toDebugFileName);
		String textureName = material.sprite().contents().name().toString();
		List<ForceFieldModel.BakedElement> bakedElements = new ArrayList<>();

		for (ForceFieldElement element : this.elements) {
			ExtraFaceData faceData = new ExtraFaceData(ExtraFaceData.DEFAULT.color(), element.lightEmission(), this.ambientOcclusion);

			for (Map.Entry<Direction, ForceFieldElement.Face> entry : element.faces().entrySet()) {
				Direction side = entry.getKey();
				ForceFieldElement.Face face = entry.getValue();
				Direction cullFace = face.cullFace().orElse(null);

				CuboidFace cuboidFace = new CuboidFace(cullFace, face.tintIndex(), textureName, face.uvs().orElse(null), Quadrant.R0, faceData, new MutableObject<>());
				BakedQuad quad = FaceBakery.bakeQuad(
					modelBaker,
					element.from(),
					element.to(),
					cuboidFace,
					material,
					side,
					BlockModelRotation.IDENTITY,
					null,
					element.shade(),
					element.lightEmission()
				);

				bakedElements.add(new ForceFieldModel.BakedElement(element.condition().orElse(null), side, cullFace, quad));
			}
		}

		return new ForceFieldModel(bakedElements, material, this.ambientOcclusion);
	}

	@Override
	public void resolveDependencies(Resolver resolver) {}

	@Override
	public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
		return MAP_CODEC;
	}
}
