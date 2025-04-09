package twilightforest.client.model.block.carpet;

import net.minecraft.client.renderer.block.model.*;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import org.apache.commons.lang3.mutable.MutableObject;
import org.joml.Vector3f;
import twilightforest.client.model.block.connected.ConnectionLogic;
import twilightforest.client.model.block.connected.UnbakedConnectedTextureModel;
import twilightforest.init.TFBlocks;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

//FIXME remove this once the connected texture loader supports custom geometry
public class UnbakedRoyalRagsModel extends UnbakedConnectedTextureModel {

	public UnbakedRoyalRagsModel(StandardModelParameters parameters, RenderTypeGroup group) {
		super(EnumSet.of(Direction.UP), true, List.of(TFBlocks.CORONATION_CARPET.get()), 0, 0, 0, 0, parameters, group);
		//base elements - the side faces without ctm. No Connected Textures on this bit.
		//the array is made of horizontal directions (Direction.get2DDataValue) and quads
		this.baseElements = new BlockElement[6][4];

		//face elements - the connected bit of the model.
		//the array is made of the directions, quads, and each logic value in the ConnectionLogic class
		//Topmost array indexes to up/down directions (Direction.get3DDataValue, down = 0, up = 1) then inside are quads
		this.faceElements = new BlockElement[6][4][5];
		Vec3i center = new Vec3i(8, 8, 8);

		for (Direction face : Direction.values()) {
			Direction[] planeDirections = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];

			for (int quad = 0; quad < 4; quad++) {
				Vec3i corner = face.getUnitVec3i().offset(planeDirections[quad].getUnitVec3i()).offset(planeDirections[(quad + 1) % 4].getUnitVec3i()).offset(1, 1, 1).multiply(8);
				BlockElement element = new BlockElement(new Vector3f((float) Math.min(center.getX(), corner.getX()), (float) Math.min(center.getY(), corner.getY()) / 16f, (float) Math.min(center.getZ(), corner.getZ())), new Vector3f((float) Math.max(center.getX(), corner.getX()), (float) Math.max(center.getY(), corner.getY()) / 16f, (float) Math.max(center.getZ(), corner.getZ())), Map.of(), null, true, 0);

				this.baseElements[face.get3DDataValue()][quad] = new BlockElement(element.from, element.to, Map.of(face, new BlockElementFace(face, -1, "", new BlockFaceUV(ConnectionLogic.NONE.remapUVs(element.uvsByFace(face)), 0))), null, true, 0);

				for (ConnectionLogic connectionType : ConnectionLogic.values()) {
					this.faceElements[face.get3DDataValue()][quad][connectionType.ordinal()] = new BlockElement(element.from, element.to, Map.of(face, new BlockElementFace(face, 0, "", new BlockFaceUV(connectionType.remapUVs(element.uvsByFace(face)), 0), null, new MutableObject<>())), null, true, 0);
				}
			}
		}
	}
}