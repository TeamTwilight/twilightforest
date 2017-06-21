package twilightforest.structures.lichtower;


public class ComponentTFTowerRoofPointyOverhang extends ComponentTFTowerRoofPointy {

	public ComponentTFTowerRoofPointyOverhang() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ComponentTFTowerRoofPointyOverhang(int i, ComponentTFTowerWing wing) {
		super(i, wing);

		// same facing, but it doesn't matter
		this.setCoordBaseMode(wing.getCoordBaseMode());

		this.size = wing.size + 2; // assuming only square towers and roofs right now.
		this.height = size;

		// just hang out at the very top of the tower
		makeOverhangBB(wing);

	}
}
