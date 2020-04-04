package twilightforest.client.model.armor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class ModelTFYetiArmor extends ModelTFArmor {

	private final EquipmentSlotType slot;

	private ModelRenderer bipedLegBody;
	private ModelRenderer rightRuff;
	private ModelRenderer leftRuff;
	private ModelRenderer rightToe;
	private ModelRenderer leftToe;

	public ModelTFYetiArmor(EquipmentSlotType slot, float expand) {
		super(expand);
		this.slot = slot;

		//bigger head
		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addCuboid(-4.5F, -7.5F, -4.0F, 9, 8, 8, expand);
		this.bipedHead.setRotationPoint(0.0F, 0.0F + expand, 0.0F);

		// add horns
		addPairHorns(-8.0F, 35F);
		addPairHorns(-6.0F, 15F);
		addPairHorns(-4.0F, -5F);


		// change leg texture
		this.bipedRightLeg = new ModelRenderer(this, 40, 0);
		this.bipedRightLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4, 12, 4, expand);
		this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + 0.0F, 0.0F);

		this.bipedLeftLeg = new ModelRenderer(this, 40, 0);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4, 12, 4, expand);
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + 0.0F, 0.0F);

		this.rightRuff = new ModelRenderer(this, 40, 22);
		this.rightRuff.addCuboid(-2.5F, 0.0F, -2.5F, 5, 2, 5, expand);
		this.rightRuff.setRotationPoint(0.0F, 6.0F, 0.0F);
		this.bipedRightLeg.addChild(rightRuff);

		this.leftRuff = new ModelRenderer(this, 40, 22);
		this.leftRuff.addCuboid(-2.5F, 0.0F, -2.5F, 5, 2, 5, expand);
		this.leftRuff.setRotationPoint(0.0F, 6.0F, 0.0F);
		this.bipedLeftLeg.addChild(leftRuff);

		this.rightToe = new ModelRenderer(this, 40, 17);
		this.rightToe.addCuboid(-2.0F, 0.0F, -1.0F, 4, 2, 1, expand);
		this.rightToe.setRotationPoint(0.0F, 10.0F, -2.0F);
		this.bipedRightLeg.addChild(rightToe);

		this.leftToe = new ModelRenderer(this, 40, 17);
		this.leftToe.addCuboid(-2.0F, 0.0F, -1.0F, 4, 2, 1, expand);
		this.leftToe.setRotationPoint(0.0F, 10.0F, -2.0F);
		this.bipedLeftLeg.addChild(leftToe);

		// stuff for chest and legs
		this.bipedBody = new ModelRenderer(this, 0, 0);
		this.bipedBody.addCuboid(-4.0F, 0.0F, -2.0F, 8, 11, 4, expand);
		this.bipedBody.setRotationPoint(0.0F, 0.0F + 0.0f, 0.0F);

		this.bipedLegBody = new ModelRenderer(this, 40, 16);
		this.bipedLegBody.addCuboid(-4.0F, 0.0F, -2.0F, 8, 12, 4, expand);
		this.bipedLegBody.setRotationPoint(0.0F, 0.0F + 0.0f, 0.0F);

		this.bipedRightArm = new ModelRenderer(this, 0, 16);
		this.bipedRightArm.addCuboid(-3.0F, -2.0F, -2.0F, 4, 10, 4, expand);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + 0.0f, 0.0F);

		this.bipedLeftArm = new ModelRenderer(this, 0, 16);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addCuboid(-1.0F, -2.0F, -2.0F, 4, 10, 4, expand);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + 0.0f, 0.0F);

	}

	@Override
	protected Iterable<ModelRenderer> getBodyParts() {
		return Iterables.concat(super.getBodyParts(), ImmutableList.of(bipedLegBody));
	}

	private void addPairHorns(float height, float zangle) {
		ModelRenderer horn1a;
		ModelRenderer horn1b;

		horn1a = new ModelRenderer(this, 0, 19);
		horn1a.addCuboid(-3.0F, -1.5F, -1.5F, 3, 3, 3);
		horn1a.setRotationPoint(-4.5F, height, -1.0F);
		horn1a.rotateAngleY = -30F / (180F / (float) Math.PI);
		horn1a.rotateAngleZ = zangle / (180F / (float) Math.PI);
		this.bipedHead.addChild(horn1a);

		horn1b = new ModelRenderer(this, 0, 26);
		horn1b.addCuboid(-4.0F, -1.0F, -1.0F, 5, 2, 2);
		horn1b.setRotationPoint(-3.0F, 0.0F, 0.0F);
		horn1b.rotateAngleY = -20F / (180F / (float) Math.PI);
		horn1b.rotateAngleZ = zangle / (180F / (float) Math.PI);
		horn1a.addChild(horn1b);

		ModelRenderer horn2a;
		ModelRenderer horn2b;

		horn2a = new ModelRenderer(this, 0, 19);
		horn2a.addCuboid(0.0F, -1.5F, -1.5F, 3, 3, 3);
		horn2a.setRotationPoint(4.5F, height, -1.0F);
		horn2a.rotateAngleY = 30F / (180F / (float) Math.PI);
		horn2a.rotateAngleZ = -zangle / (180F / (float) Math.PI);
		this.bipedHead.addChild(horn2a);

		horn2b = new ModelRenderer(this, 0, 26);
		horn2b.addCuboid(-1.0F, -1.0F, -1.0F, 5, 2, 2);
		horn2b.setRotationPoint(3.0F, 0.0F, 0.0F);
		horn2b.rotateAngleY = 20F / (180F / (float) Math.PI);
		horn2b.rotateAngleZ = -zangle / (180F / (float) Math.PI);
		horn2a.addChild(horn2b);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		bipedLegBody.showModel = visible;
	}

	@Override
	public void render(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float scale) {
		switch (slot) {
			case HEAD:
				this.bipedHead.showModel = true;
				this.bipedHeadwear.showModel = false;
				this.bipedBody.showModel = false;
				this.bipedRightArm.showModel = false;
				this.bipedLeftArm.showModel = false;
				this.bipedLegBody.showModel = false;
				this.bipedRightLeg.showModel = false;
				this.bipedLeftLeg.showModel = false;
				break;
			case CHEST:
				this.bipedHead.showModel = false;
				this.bipedHeadwear.showModel = false;
				this.bipedBody.showModel = true;
				this.bipedRightArm.showModel = true;
				this.bipedLeftArm.showModel = true;
				this.bipedLegBody.showModel = false;
				this.bipedRightLeg.showModel = false;
				this.bipedLeftLeg.showModel = false;
				break;
			case LEGS:
				this.bipedHead.showModel = false;
				this.bipedHeadwear.showModel = false;
				this.bipedBody.showModel = false;
				this.bipedRightArm.showModel = false;
				this.bipedLeftArm.showModel = false;
				this.bipedLegBody.showModel = true;
				this.bipedRightLeg.showModel = true;
				this.bipedLeftLeg.showModel = true;
				this.leftRuff.showModel = false;
				this.leftToe.showModel = false;
				this.rightRuff.showModel = false;
				this.rightToe.showModel = false;
				break;
			case FEET:
				this.bipedHead.showModel = false;
				this.bipedHeadwear.showModel = false;
				this.bipedBody.showModel = false;
				this.bipedRightArm.showModel = false;
				this.bipedLeftArm.showModel = false;
				this.bipedLegBody.showModel = false;
				this.bipedRightLeg.showModel = true;
				this.bipedLeftLeg.showModel = true;
				this.leftRuff.showModel = true;
				this.leftToe.showModel = true;
				this.rightRuff.showModel = true;
				this.rightToe.showModel = true;
				break;
			default:
				break;
		}
		super.render(stack, builder, light, overlay, red, green, blue, scale);
		//this.bipedLegBody.render(scale);
	}
}
