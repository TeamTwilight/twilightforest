package twilightforest.client.model.block.carpet;

import org.jetbrains.annotations.NotNull;
import twilightforest.block.GenericModelLoader;

public class RoyalRagsModelLoader extends GenericModelLoader<@NotNull UnbakedRoyalRagsModel> {
	public static final RoyalRagsModelLoader INSTANCE = new RoyalRagsModelLoader();

	public RoyalRagsModelLoader() {
		super(UnbakedRoyalRagsModel::new);
	}
}
