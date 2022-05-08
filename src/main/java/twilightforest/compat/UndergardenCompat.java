package twilightforest.compat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import quek.undergarden.entity.projectile.slingshot.SlingshotProjectile;
import quek.undergarden.item.tool.slingshot.AbstractSlingshotAmmoBehavior;
import quek.undergarden.item.tool.slingshot.SlingshotItem;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.CicadaModel;
import twilightforest.client.model.entity.FireflyModel;
import twilightforest.client.model.entity.MoonwormModel;
import twilightforest.compat.undergarden.BugProjectileRenderer;
import twilightforest.compat.undergarden.CicadaSlingshotProjectile;
import twilightforest.compat.undergarden.FireflySlingshotProjectile;
import twilightforest.compat.undergarden.MoonwormSlingshotProjectile;

public class UndergardenCompat extends TFCompat {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, TwilightForestMod.ID);

	public static final RegistryObject<EntityType<CicadaSlingshotProjectile>> CICADA_SLINGSHOT = ENTITIES.register("cicada_slingshot", () -> EntityType.Builder.<CicadaSlingshotProjectile>of(CicadaSlingshotProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).build("twilightforest:cicada_slingshot"));
	public static final RegistryObject<EntityType<FireflySlingshotProjectile>> FIREFLY_SLINGSHOT = ENTITIES.register("firefly_slingshot", () -> EntityType.Builder.<FireflySlingshotProjectile>of(FireflySlingshotProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).build("twilightforest:firefly_slingshot"));
	public static final RegistryObject<EntityType<MoonwormSlingshotProjectile>> MOONWORM_SLINGSHOT = ENTITIES.register("moonworm_slingshot", () -> EntityType.Builder.<MoonwormSlingshotProjectile>of(MoonwormSlingshotProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).build("twilightforest:moonworm_slingshot"));

	protected UndergardenCompat() {
		super("undergarden");
	}

	@Override
	protected boolean preInit() {
		return true;
	}

	@Override
	protected void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			SlingshotItem.registerAmmo(TFBlocks.CICADA.get().asItem(), new AbstractSlingshotAmmoBehavior() {
				@Override
				public SlingshotProjectile getProjectile(Level level, BlockPos blockPos, Player player, ItemStack itemStack) {
					return new CicadaSlingshotProjectile(level, player);
				}
			});

			SlingshotItem.registerAmmo(TFBlocks.FIREFLY.get().asItem(), new AbstractSlingshotAmmoBehavior() {
				@Override
				public SlingshotProjectile getProjectile(Level level, BlockPos blockPos, Player player, ItemStack itemStack) {
					return new FireflySlingshotProjectile(level, player);
				}
			});

			SlingshotItem.registerAmmo(TFBlocks.MOONWORM.get().asItem(), new AbstractSlingshotAmmoBehavior() {
				@Override
				public SlingshotProjectile getProjectile(Level level, BlockPos blockPos, Player player, ItemStack itemStack) {
					return new MoonwormSlingshotProjectile(level, player);
				}
			});
		});
	}

	@Override
	protected void postInit() {

	}

	@Override
	protected void handleIMCs() {

	}

	@Override
	protected void initItems(RegistryEvent.Register<Item> evt) {

	}

	@OnlyIn(Dist.CLIENT)
	public static void registerSlingshotRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(CICADA_SLINGSHOT.get(), ctx -> new BugProjectileRenderer(ctx, new CicadaModel(ctx.bakeLayer(TFModelLayers.CICADA)), TwilightForestMod.getModelTexture("cicada-model.png")));
		event.registerEntityRenderer(FIREFLY_SLINGSHOT.get(), ctx -> new BugProjectileRenderer(ctx, new FireflyModel(ctx.bakeLayer(TFModelLayers.FIREFLY)), TwilightForestMod.getModelTexture("firefly-tiny.png")));
		event.registerEntityRenderer(MOONWORM_SLINGSHOT.get(), ctx -> new BugProjectileRenderer(ctx, new MoonwormModel(ctx.bakeLayer(TFModelLayers.MOONWORM)), TwilightForestMod.getModelTexture("moonworm.png")));
	}
}
