package twilightforest.client.model.entity;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import twilightforest.entity.monster.DeathTome;

public class DeathTomeModel extends HierarchicalModel<DeathTome> {

	private final ModelPart root;
	private final ModelPart book;
	private final ModelPart pagesRight;
	private final ModelPart pagesLeft;
	private final ModelPart flippingPageRight;
	private final ModelPart flippingPageLeft;
	private final ModelPart coverRight;
	private final ModelPart coverLeft;

	private final ModelPart paperStorm;
	private final ModelPart loosePage0;
	private final ModelPart loosePage1;
	private final ModelPart loosePage2;
	private final ModelPart loosePage3;

	public DeathTomeModel(ModelPart root) {
		this.root = root;

		this.book = root.getChild("book");

		this.pagesRight = this.book.getChild("pages_right");
		this.pagesLeft = this.book.getChild("pages_left");

		this.flippingPageRight = this.book.getChild("flipping_page_right");
		this.flippingPageLeft = this.book.getChild("flipping_page_left");

		this.coverRight = this.book.getChild("cover_right");
		this.coverLeft = this.book.getChild("cover_left");

		this.paperStorm = this.root.getChild("paper_storm");

		this.loosePage0 = this.paperStorm.getChild("loose_page_0");
		this.loosePage1 = this.paperStorm.getChild("loose_page_1");
		this.loosePage2 = this.paperStorm.getChild("loose_page_2");
		this.loosePage3 = this.paperStorm.getChild("loose_page_3");
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		var book = partdefinition.addOrReplaceChild("book", CubeListBuilder.create(), PartPose.ZERO);

		book.addOrReplaceChild("pages_right", CubeListBuilder.create()
				.texOffs(0, 10)
				.addBox(0.0F, -4.0F, -0.99F, 5.0F, 8.0F, 1.0F),
			PartPose.ZERO);

		book.addOrReplaceChild("pages_left", CubeListBuilder.create()
				.texOffs(12, 10)
				.addBox(0.0F, -4.0F, -0.01F, 5.0F, 8.0F, 1.0F),
			PartPose.ZERO);

		book.addOrReplaceChild("flipping_page_right", CubeListBuilder.create()
				.texOffs(24, 10)
				.addBox(0.0F, -4.0F, 0.0F, 5.0F, 8.0F, 0.005F),
			PartPose.ZERO);

		book.addOrReplaceChild("flipping_page_left", CubeListBuilder.create()
				.texOffs(24, 10)
				.addBox(0.0F, -4.0F, 0.0F, 5.0F, 8.0F, 0.005F),
			PartPose.ZERO);

		book.addOrReplaceChild("cover_right", CubeListBuilder.create()
				.addBox(-6.0F, -5.0F, -0.005F, 6.0F, 10.0F, 0.005F),
			PartPose.offset(0.0F, 0.0F, -1.0F));

		book.addOrReplaceChild("cover_left", CubeListBuilder.create()
				.texOffs(16, 0)
				.addBox(0.0F, -5.0F, -0.005F, 6.0F, 10.0F, 0.005F),
			PartPose.offset(0.0F, 0.0F, 1.0F));

		book.addOrReplaceChild("book_spine", CubeListBuilder.create()
				.texOffs(12, 0)
				.addBox(-1.0F, -5.0F, 0.0F, 2.0F, 10.0F, 0.005F),
			PartPose.rotation(0, Mth.HALF_PI, 0));

		var paperStorm = partdefinition.addOrReplaceChild("paper_storm", CubeListBuilder.create(), PartPose.ZERO);

		paperStorm.addOrReplaceChild("loose_page_0", CubeListBuilder.create()
				.texOffs(24, 10)
				.addBox(0.0F, -4.0F, -8.0F, 5.0F, 8.0F, 0.005F),
			PartPose.ZERO);

		paperStorm.addOrReplaceChild("loose_page_1", CubeListBuilder.create()
				.texOffs(24, 10)
				.addBox(0.0F, -4.0F, 9.0F, 5.0F, 8.0F, 0.005F),
			PartPose.ZERO);

		paperStorm.addOrReplaceChild("loose_page_2", CubeListBuilder.create()
				.texOffs(24, 10)
				.addBox(0.0F, -4.0F, 11.0F, 5.0F, 8.0F, 0.005F),
			PartPose.ZERO);

		paperStorm.addOrReplaceChild("loose_page_3", CubeListBuilder.create()
				.texOffs(24, 10)
				.addBox(0.0F, -4.0F, 7.0F, 5.0F, 8.0F, 0.005F),
			PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(DeathTome entity, float limbAngle, float limbDistance, float ageInTicks, float headYaw, float headPitch) {
		this.root.yRot = Mth.HALF_PI;

		if (entity.isOnLectern()) {
			this.book.zRot = -0.8726646259971647F * 1.35F;
			this.book.x = 1.75F;
		} else {
			this.book.zRot = -0.8726646259971647F;
			this.book.x = 0.0F;
		}

		this.paperStorm.yRot = ageInTicks * Mth.DEG_TO_RAD + Mth.HALF_PI;
		this.paperStorm.zRot = 0.8726646259971647F;
	}

	@Override
	public void prepareMobModel(DeathTome entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		boolean onLectern = entity.isOnLectern();

		float bounce = onLectern ? 0.0F : entity.tickCount + partialTicks;
		float open = onLectern ? 1.2F : 0.9f;

		float flip = Mth.lerp(partialTicks, entity.oFlip, entity.flip);
		float flipRight = Mth.clamp(Mth.frac(flip + 0.25F) * 1.6F - 0.3F, 0.0F, 1.0F);
		float flipLeft = Mth.clamp(Mth.frac(flip + 0.75F) * 1.6F - 0.3F, 0.0F, 1.0F);


		this.loosePage0.skipDraw = onLectern;
		this.loosePage1.skipDraw = onLectern;
		this.loosePage2.skipDraw = onLectern;
		this.loosePage3.skipDraw = onLectern;

		// page rotations
		this.loosePage0.yRot = (bounce) / 4.0F;
		this.loosePage0.xRot = Mth.sin((bounce) / 5.0F) / 3.0F;
		this.loosePage0.zRot = Mth.cos((bounce) / 5.0F) / 5.0F;

		this.loosePage1.yRot = (bounce) / 3.0F;
		this.loosePage1.xRot = Mth.sin((bounce) / 5.0F) / 3.0F;
		this.loosePage1.zRot = Mth.cos((bounce) / 5.0F) / 4.0F + 2;

		this.loosePage2.yRot = (bounce) / 4.0F;
		this.loosePage2.xRot = -Mth.sin((bounce) / 5.0F) / 3.0F;
		this.loosePage2.zRot = Mth.cos((bounce) / 5.0F) / 5.0F - 1.0F;

		this.loosePage3.yRot = (bounce) / 4.0F;
		this.loosePage3.xRot = -Mth.sin((bounce) / 2.0F) / 4.0F;
		this.loosePage3.zRot = Mth.cos((bounce) / 7.0F) / 5.0F;

		// hoveriness
		this.book.setPos(0, 8 - Mth.sin((bounce) * 0.3F) * 2.0F, 0);


		// book openness
		float openAngle = (Mth.sin(bounce * 0.4F) * 0.3F + 1.25F) * open;
		this.coverRight.yRot = Mth.PI + openAngle;
		this.coverLeft.yRot = -openAngle;
		this.pagesRight.yRot = openAngle;
		this.pagesLeft.yRot = -openAngle;
		this.flippingPageRight.yRot = openAngle - openAngle * 2.0F * flipRight;
		this.flippingPageLeft.yRot = openAngle - openAngle * 2.0F * flipLeft;
		this.pagesRight.x = Mth.sin(openAngle);
		this.pagesLeft.x = Mth.sin(openAngle);
		this.flippingPageRight.x = Mth.sin(openAngle);
		this.flippingPageLeft.x = Mth.sin(openAngle);
	}
}
