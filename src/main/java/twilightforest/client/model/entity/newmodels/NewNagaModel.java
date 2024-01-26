package twilightforest.client.model.entity.newmodels;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.entity.boss.Naga;
import twilightforest.entity.boss.NagaSegment;

/**
 * ModelNagaHead - Undefined
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class NewNagaModel<T extends Entity> extends ListModel<T> {

    public final ModelPart root;
    public final ModelPart head;
    public final ModelPart body;
    private T entity;

    public NewNagaModel(ModelPart root) {
        this.root = root;

        this.head = root.getChild("head");
        this.body = root.getChild("body");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition definition = mesh.getRoot();

        var head = definition.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F),
                PartPose.offset(0.0F, 8.0F, 0.0F));

        head.addOrReplaceChild("tongue", CubeListBuilder.create()
                        .texOffs(84, 0)
                        .addBox(-6.0F, 0.0F, -12.0F, 12.0F, 0.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, -16.0F, 0.4363323129985824F, 0.0F, 0.0F));

        definition.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F),
                PartPose.offset(0.0F, 8.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float alpha) {
        if (entity instanceof Naga) {
            head.render(stack, builder, light, overlay, red, green, blue, alpha * 2);
        } else if (entity instanceof NagaSegment) {
            body.render(stack, builder, light, overlay, red, green, blue, alpha * 2);
        } else {
            head.render(stack, builder, light, overlay, red, green, blue, alpha * 2);
        }
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(head, body);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
}
