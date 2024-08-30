package twilightforest.enums.extensions;

import net.minecraft.world.entity.vehicle.Boat;
import net.neoforged.neoforge.common.util.Lazy;
import twilightforest.TFEnumExtensions;
import twilightforest.beans.Autowired;
import twilightforest.beans.Component;
import twilightforest.util.ModidPrefixUtil;

@Component
public class TFBoatTypeEnumExtension {

	@Autowired
	private ModidPrefixUtil modidPrefixUtil;

	/**
	 * {@link TFEnumExtensions#Boat$Type_TWILIGHT_OAK(int, Class)}
	 */
	public final Lazy<Boat.Type> TWILIGHT_OAK = Lazy.of(() -> Boat.Type.byName(modidPrefixUtil.stringPrefix("twilight_oak")));

	/**
	 * {@link TFEnumExtensions#Boat$Type_CANOPY(int, Class)}
	 */
	public final Lazy<Boat.Type> CANOPY = Lazy.of(() -> Boat.Type.byName(modidPrefixUtil.stringPrefix("canopy")));

	/**
	 * {@link TFEnumExtensions#Boat$Type_MANGROVE(int, Class)}
	 */
	public final Lazy<Boat.Type> MANGROVE = Lazy.of(() -> Boat.Type.byName(modidPrefixUtil.stringPrefix("mangrove")));

	/**
	 * {@link TFEnumExtensions#Boat$Type_DARK(int, Class)}
	 */
	public final Lazy<Boat.Type> DARK = Lazy.of(() -> Boat.Type.byName(modidPrefixUtil.stringPrefix("dark")));

	/**
	 * {@link TFEnumExtensions#Boat$Type_TIME(int, Class)}
	 */
	public final Lazy<Boat.Type> TIME = Lazy.of(() -> Boat.Type.byName(modidPrefixUtil.stringPrefix("time")));

	/**
	 * {@link TFEnumExtensions#Boat$Type_TRANSFORMATION(int, Class)}
	 */
	public final Lazy<Boat.Type> TRANSFORMATION = Lazy.of(() -> Boat.Type.byName(modidPrefixUtil.stringPrefix("transformation")));

	/**
	 * {@link TFEnumExtensions#Boat$Type_MINING(int, Class)}
	 */
	public final Lazy<Boat.Type> MINING = Lazy.of(() -> Boat.Type.byName(modidPrefixUtil.stringPrefix("mining")));

	/**
	 * {@link TFEnumExtensions#Boat$Type_SORTING(int, Class)}
	 */
	public final Lazy<Boat.Type> SORTING = Lazy.of(() -> Boat.Type.byName(modidPrefixUtil.stringPrefix("sorting")));

}
