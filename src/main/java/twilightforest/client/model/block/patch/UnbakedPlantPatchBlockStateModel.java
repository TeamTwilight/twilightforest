package twilightforest.client.model.block.patch;

import com.mojang.blaze3d.platform.Transparency;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;

public record UnbakedPlantPatchBlockStateModel(Material texture, boolean shaggify) implements CustomUnbakedBlockStateModel {

    public static final MapCodec<UnbakedPlantPatchBlockStateModel> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Material.CODEC.fieldOf("texture").forGetter(UnbakedPlantPatchBlockStateModel::texture),
            Codec.BOOL.optionalFieldOf("shaggify", true).forGetter(UnbakedPlantPatchBlockStateModel::shaggify)
    ).apply(instance, UnbakedPlantPatchBlockStateModel::new));

    @Override
    public BlockStateModel bake(ModelBaker modelBaker) {
        Material.Baked bakedMaterial = modelBaker.materials().get(this.texture, this.texture.sprite()::toDebugFileName);

        BakedQuad.MaterialInfo materialInfo = BakedQuad.MaterialInfo.of(bakedMaterial, bakedMaterial.forceTranslucent() ? Transparency.TRANSLUCENT : bakedMaterial.sprite().transparency(), 0, true, 0, false);

        return new PatchModel(materialInfo, this.shaggify, bakedMaterial);
    }

    @Override
    public void resolveDependencies(Resolver resolver) {}

    @Override
    public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
        return MAP_CODEC;
    }
}
