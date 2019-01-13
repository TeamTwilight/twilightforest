package twilightforest.entity.passive;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityTFBighorn extends EntitySheep {

	public static final ResourceLocation[] LOOT_TABLES = {
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/white"      ),
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/orange"     ),
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/magenta"    ),
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/light_blue" ),
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/yellow"     ),
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/lime"       ),
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/pink"       ),
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/gray"       ),
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/silver"     ),
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/cyan"       ),
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/purple"     ),
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/blue"       ),
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/brown"      ),
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/green"      ),
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/red"        ),
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/black"      ),
			new ResourceLocation( TwilightForestMod.ID, "entities/bighorn_sheep/sheared"    )
	};

	public EntityTFBighorn(World world) {
		super(world);
		setSize(0.9F, 1.3F);
	}

	public EntityTFBighorn(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y, z);
	}

	@Override
	public ResourceLocation getLootTable() {
		if (this.getSheared())
			return LOOT_TABLES[16];
		else
			return LOOT_TABLES[MathHelper.clamp(this.getFleeceColor().ordinal(), 0, 15)];
	}

	private static EnumDyeColor getRandomFleeceColor(Random random) {
		return random.nextBoolean()
				? EnumDyeColor.BROWN
				: EnumDyeColor.byMetadata(random.nextInt(16));
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		this.setFleeceColor(getRandomFleeceColor(this.world.rand));
		return livingdata;
	}

	@Override
	public EntitySheep createChild(EntityAgeable ageable) {
		EntityTFBighorn otherParent = (EntityTFBighorn) ageable;
		EntityTFBighorn babySheep = new EntityTFBighorn(world);
		babySheep.setFleeceColor(getDyeColorMixFromParents(this, otherParent));
		return babySheep;
	}
}
