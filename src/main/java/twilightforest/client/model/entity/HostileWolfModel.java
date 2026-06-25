package twilightforest.client.model.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.util.Mth;

public class HostileWolfModel<T extends WolfRenderState> extends EntityModel<T> {

	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart rightHindLeg;
	private final ModelPart leftHindLeg;
	private final ModelPart rightFrontLeg;
	private final ModelPart leftFrontLeg;
	private final ModelPart tail;
	private final ModelPart upperBody;

	public HostileWolfModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.upperBody = root.getChild("upper_body");
		this.rightHindLeg = root.getChild("right_hind_leg");
		this.leftHindLeg = root.getChild("left_hind_leg");
		this.rightFrontLeg = root.getChild("right_front_leg");
		this.leftFrontLeg = root.getChild("left_front_leg");
		this.tail = root.getChild("tail");
	}

	@Override
	public void setupAnim(T entity) {
		super.setupAnim(entity);
		if (entity.isAngry) {
			this.tail.yRot = 0.0F;
		} else {
			this.tail.yRot = Mth.cos(entity.walkAnimationPos * 0.6662F) * 1.4F * entity.walkAnimationSpeed;
		}

		this.body.setPos(0.0F, 14.0F, 2.0F);
		this.body.xRot = Mth.HALF_PI;
		this.upperBody.setPos(-1.0F, 14.0F, -3.0F);
		this.upperBody.xRot = this.body.xRot;
		this.tail.setPos(-1.0F, 12.0F, 8.0F);
		this.rightHindLeg.setPos(-2.5F, 16.0F, 7.0F);
		this.leftHindLeg.setPos(0.5F, 16.0F, 7.0F);
		this.rightFrontLeg.setPos(-2.5F, 16.0F, -4.0F);
		this.leftFrontLeg.setPos(0.5F, 16.0F, -4.0F);
		this.rightHindLeg.xRot = Mth.cos(entity.walkAnimationPos * 0.6662F) * 1.4F * entity.walkAnimationSpeed;
		this.leftHindLeg.xRot = Mth.cos(entity.walkAnimationPos * 0.6662F + Mth.PI) * 1.4F * entity.walkAnimationSpeed;
		this.rightFrontLeg.xRot = Mth.cos(entity.walkAnimationPos * 0.6662F + Mth.PI) * 1.4F * entity.walkAnimationSpeed;
		this.leftFrontLeg.xRot = Mth.cos(entity.walkAnimationPos * 0.6662F) * 1.4F * entity.walkAnimationSpeed;
		this.head.xRot = entity.xRot * Mth.DEG_TO_RAD;
		this.head.yRot = entity.yRot * Mth.DEG_TO_RAD;
		this.tail.xRot = entity.ageInTicks;
	}
}
