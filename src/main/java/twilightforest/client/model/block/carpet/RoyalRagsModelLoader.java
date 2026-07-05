package twilightforest.client.model.block.carpet;

import twilightforest.block.GenericModelLoader;

public class RoyalRagsModelLoader extends GenericModelLoader<UnbakedRoyalRagsModel> {
	public static final RoyalRagsModelLoader INSTANCE = new RoyalRagsModelLoader();

	public RoyalRagsModelLoader() {
		super(UnbakedRoyalRagsModel::new);
	}
}
