// TODO: Remove when real API is published. This is here so project compiles.
// Copied from: https://github.com/Azanor/thaumcraft-api
package thaumcraft.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.GameData;
import thaumcraft.api.aspects.Aspect;
//import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;
//import thaumcraft.api.crafting.CrucibleRecipe;
//import thaumcraft.api.crafting.IArcaneRecipe;
//import thaumcraft.api.crafting.IThaumcraftRecipe;
//import thaumcraft.api.crafting.InfusionRecipe;
//import thaumcraft.api.crafting.Part;
//import thaumcraft.api.internal.CommonInternals;
//import thaumcraft.api.internal.DummyInternalMethodHandler;
//import thaumcraft.api.internal.IInternalMethodHandler;
//import thaumcraft.api.internal.WeightedRandomLoot;


/**
 * @author Azanor
 *
 *
 * IMPORTANT: If you are adding your own aspects to items it is a good idea to do it AFTER Thaumcraft adds its aspects, otherwise odd things may happen.
 *
 */
public class ThaumcraftApi {

    /**
     * Calling methods from this will only work properly once Thaumcraft is past the FMLPreInitializationEvent phase.
     * This is used to access the varius methods described in <code>IInternalMethodHandler</code>
     * @see IInternalMethodHandler
     */
    //public static IInternalMethodHandler internalMethods = new DummyInternalMethodHandler();

    //RESEARCH/////////////////////////////////////////

    /**
     * <i><b>Important</b>: This must be called <b>before</b> the postInit phase.<br></i>
     * Allows you to register the location of a json fil in your assets folder that contains your research.
     * For example: <code>"thaumcraft:research/basics"</code>
     * There is a sample <code>_example.json.txt</code> file in <code>thaumcraft.api.research</code>
     * @param loc the resourcelocation of the json file
     */
    public static void registerResearchLocation(ResourceLocation loc) {
        //if (!CommonInternals.jsonLocs.containsKey(loc.toString())) {
        //    CommonInternals.jsonLocs.put(loc.toString(), loc);
        //}
    }

    //RECIPES/////////////////////////////////////////

    public static class SmeltBonus {
        public Object in;
        public ItemStack out;
        public float chance;
        public SmeltBonus(Object in, ItemStack out, float chance) {
            this.in = in;
            this.out = out;
            this.chance = chance;
        }
    }

    /**
     * This method is used to determine what bonus items are generated when the infernal furnace smelts items
     * @param in The input of the smelting operation. Can either be an itemstack or a ore dictionary entry (e.g. "oreGold")
     * @param out The bonus item that can be produced from the smelting operation.
     * @param chance the base chance of the item being produced as a bonus. Default value is .33f
     */
    public static void addSmeltingBonus(Object in, ItemStack out, float chance) {
        //if (in instanceof ItemStack || in instanceof String)
            //CommonInternals.smeltingBonus.add(new SmeltBonus(in,out,chance));
    }

    public static void addSmeltingBonus(Object in, ItemStack out) {
        //if (in instanceof ItemStack || in instanceof String)
            //CommonInternals.smeltingBonus.add(new SmeltBonus(in,out,.33f));
    }


    //public static HashMap<ResourceLocation, IThaumcraftRecipe> getCraftingRecipes() {
    //    return CommonInternals.craftingRecipeCatalog;
    //}

    //public static HashMap<ResourceLocation, Object> getCraftingRecipesFake() {
    //    return CommonInternals.craftingRecipeCatalogFake;
    //}

    /**
     * This adds recipes to the 'fake' recipe catalog. These recipes won't be craftable, but are useful for display in the thaumonomicon if
     * they are dynamic recipes like infusion enchantment or runic infusion.
     * @param registry
     * @param recipe
     */
    public static void addFakeCraftingRecipe(ResourceLocation registry, Object recipe)
    {
        //getCraftingRecipesFake().put(registry, recipe);
    }


    /**
     * Use this method to add a multiblock blueprint recipe to the thaumcraft recipe catalog. This is used for display purposes in the thaumonomicon
     * @param name unique identifier for this recipe. I advise making your mod-id part of this
     * Recipes grouped under the same name will be displayed under one bookmark in thaumonomicon.
     * @param recipes a matrix of placable objects and what they will turn into
     */
    //public static void addMultiblockRecipeToCatalog(ResourceLocation registry, BluePrint recipe) {
    //    //getCraftingRecipes().put(registry, recipe);
    //}

    //public static class BluePrint implements IThaumcraftRecipe{
    //    Part[][][] parts;
    //    String research;
    //    ItemStack displayStack;
    //    ItemStack[] ingredientList;

    //    public BluePrint(String research, Part[][][] parts, ItemStack... ingredientList) {
    //        this.parts = parts;
    //        this.research = research;
    //        this.ingredientList = ingredientList;
    //    }

    //    public BluePrint(String research, ItemStack display, Part[][][] parts, ItemStack... ingredientList) {
    //        this.parts = parts;
    //        this.research = research;
    //        this.displayStack = display;
    //        this.ingredientList = ingredientList;
    //    }

    //    public Part[][][] getParts() {
    //        return parts;
    //    }

    //    @Override
    //    public String getResearch() {
    //        return research;
    //    }

    //    /**
    //     * the items needed to craft this block - used for listing in the thaumonomicon and does not influance the actual recipe
    //     * @return
    //     */
    //    public ItemStack[] getIngredientList() {
    //        return ingredientList;
    //    }

    //    /**
    //     * This stack will be displayed instead of multipart object - used for recipe bookmark display in thaumonomicon only.
    //     * @return
    //     */
    //    public ItemStack getDisplayStack() {
    //        return displayStack;
    //    }

    //    private String group;

    //    @Override
    //    public String getGroup() {
    //        return group;
    //    }

    //    public BluePrint setGroup(ResourceLocation loc) {
    //        group = loc.toString();
    //        return this;
    //    }
    //}

    /**
     * @param name unique identifier for this recipe used my thaumonomicon to link a recipe to research. I advise making your mod-id part of this.
     * Recipes grouped under the same name will be displayed under one bookmark in thaumonomicon.
     * @param @param registry
     * @param recipe
     */
    //public static void addArcaneCraftingRecipe(ResourceLocation registry, IArcaneRecipe recipe)
    //{
    //    recipe.setRegistryName(registry);
    //    GameData.register_impl(recipe);
    //}

    /**
     * @param name unique identifier for this recipe used my thaumonomicon to link a recipe to research.
     * Recipes grouped under the same name will be displayed under one bookmark in thaumonomicon.
     * @param registry
     * @param recipe
     */
    //public static void addInfusionCraftingRecipe(ResourceLocation registry, InfusionRecipe recipe)
    //{
    //    getCraftingRecipes().put(registry, recipe);
    //}



    /**
     * @param stack the recipe result
     * @return the recipe
     */
    //public static InfusionRecipe getInfusionRecipe(ItemStack res) {
    //    for (Object r:getCraftingRecipes().values()) {
    //        if (r instanceof InfusionRecipe[]) {
    //            for (InfusionRecipe recipe:(InfusionRecipe[])r) {
    //                if (recipe.getRecipeOutput() instanceof ItemStack) {
    //                    if (((ItemStack) recipe.getRecipeOutput()).isItemEqual(res))
    //                        return recipe;
    //                }
    //            }

    //        }
    //    }
    //    return null;
    //}

    /**
     * @param name unique identifier for this recipe used my thaumonomicon to link a recipe to research.
     * Recipes grouped under the same name will be displayed under one bookmark in thaumonomicon.
     * @param recipes One or more recipes linked to the unique identifier.
     */

    //public static void addCrucibleRecipe(ResourceLocation registry, CrucibleRecipe recipe) {
    //    getCraftingRecipes().put(registry, recipe);
    //}

    /**
     * @param stack the recipe result
     * @return the recipe
     */
    //public static CrucibleRecipe getCrucibleRecipe(ItemStack stack) {
    //    for (Object r:getCraftingRecipes().values()) {
    //        if (r instanceof CrucibleRecipe[]) {
    //            for (CrucibleRecipe recipe:(CrucibleRecipe[])r) {
    //                if (recipe.getRecipeOutput().isItemEqual(stack))
    //                    return recipe;
    //            }

    //        }
    //    }
    //    return null;
    //}

    /**
     * @param hash the unique recipe code
     * @return the recipe
     */
    //public static CrucibleRecipe getCrucibleRecipeFromHash(int hash) {
    //    for (Object r:getCraftingRecipes().values()) {
    //        if (r instanceof CrucibleRecipe[]) {
    //            for (CrucibleRecipe recipe:(CrucibleRecipe[])r) {
    //                if (recipe.hash==hash)
    //                    return recipe;
    //            }
    //        }
    //    }
    //    return null;
    //}



    //ASPECTS////////////////////////////////////////

    /**
     * Checks to see if the passed item/block already has aspects associated with it.
     * @param id
     * @param meta
     * @return
     */
    public static boolean exists(ItemStack item) {
        ItemStack stack = item.copy();
        stack.setCount(1);
        //AspectList tmp = CommonInternals.objectTags.get(stack.serializeNBT().toString());
        //if (tmp==null) {
        //    try {
        //        stack.setItemDamage(OreDictionary.WILDCARD_VALUE);
        //        tmp = CommonInternals.objectTags.get(stack.serializeNBT().toString());
        //        if (item.getItemDamage()==OreDictionary.WILDCARD_VALUE && tmp==null) {
        //            int index=0;
        //            do {
        //                stack.setItemDamage(index);
        //                tmp = CommonInternals.objectTags.get(stack.serializeNBT().toString());
        //                index++;
        //            } while (index<16 && tmp==null);
        //        }
        //        if (tmp==null) return false;
        //    } catch (Exception e) {
        //    }
        //}

        return true;
    }

    /**
     * Used to assign apsects to the given item/block. Here is an example of the declaration for cobblestone:<p>
     * <i>ThaumcraftApi.registerObjectTag(new ItemStack(Blocks.COBBLESTONE), (new AspectList()).add(Aspect.ENTROPY, 1).add(Aspect.EARTH, 1));</i>
     * @param item the item passed. Pass OreDictionary.WILDCARD_VALUE if all damage values of this item/block should have the same aspects
     * @param aspects A ObjectTags object of the associated aspects
     */
    public static void registerObjectTag(ItemStack item, AspectList aspects) {
        if (aspects==null) aspects=new AspectList();
        try {
            ItemStack tmp = item.copy();
            tmp.setCount(1);
            NBTTagCompound nbt = new NBTTagCompound();
            aspects.writeToNBT(nbt);
            //CommonInternals.objectTags.put(tmp.serializeNBT().toString(), aspects);
        } catch (Exception e) {}
    }


    /**
     * Used to assign apsects to the given item/block. Here is an example of the declaration for cobblestone:<p>
     * <i>ThaumcraftApi.registerObjectTag(new ItemStack(Blocks.COBBLESTONE), new int[]{0,1}, (new AspectList()).add(Aspect.ENTROPY, 1).add(Aspect.EARTH, 1));</i>
     * @param item
     * @param meta A range of meta values if you wish to lump several item meta's together as being the "same" item (i.e. stair orientations)
     * @param aspects A ObjectTags object of the associated aspects
     */
    public static void registerObjectTag(ItemStack item, int[] meta, AspectList aspects) {
        if (aspects==null) aspects=new AspectList();
        try {
            ItemStack tmp = item.copy();
            tmp.setCount(1);
            String s = tmp.serializeNBT().toString();
            //CommonInternals.objectTags.put(s, aspects);
            //for (int m:meta) {
            //    CommonInternals.groupedObjectTags.put(m+":"+s, meta);
            //}

        } catch (Exception e) {}
    }

    /**
     * Used to assign apsects to the given ore dictionary item.
     * @param oreDict the ore dictionary name
     * @param aspects A ObjectTags object of the associated aspects
     */
    public static void registerObjectTag(String oreDict, AspectList aspects) {
        if (aspects==null) aspects=new AspectList();
        //List<ItemStack> ores = ThaumcraftApiHelper.getOresWithWildCards(oreDict);
        //if (ores!=null && ores.size()>0) {
        //    for (ItemStack ore:ores) {
        //        try {
        //            ItemStack oc = ore.copy();
        //            oc.setCount(1);
        //            registerObjectTag(oc,aspects.copy());
        //        } catch (Exception e) {}
        //    }
        //}
    }

    /**
     * Used to assign aspects to the given item/block.
     * Attempts to automatically generate aspect tags by checking registered recipes.
     * Here is an example of the declaration for pistons:<p>
     * <i>ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.PISTON), (new AspectList()).add(Aspect.MECHANISM, 2).add(Aspect.MOTION, 4));</i>
     * IMPORTANT - this should only be used if you are not happy with the default aspects the object would be assigned.
     * @param item, pass OreDictionary.WILDCARD_VALUE to meta if all damage values of this item/block should have the same aspects
     * @param aspects A ObjectTags object of the associated aspects
     */
    public static void registerComplexObjectTag(ItemStack item, AspectList aspects ) {
        if (!exists(item)) {
            //AspectList tmp = AspectHelper.generateTags(item);
            //if (tmp != null && tmp.size()>0) {
            //    for(Aspect tag:tmp.getAspects()) {
            //        aspects.add(tag, tmp.getAmount(tag));
            //    }
            //}
            registerObjectTag(item,aspects);
        } else {
            //AspectList tmp = AspectHelper.getObjectAspects(item);
            //for(Aspect tag:aspects.getAspects()) {
            //    tmp.merge(tag, tmp.getAmount(tag));
            //}
            //registerObjectTag(item,tmp);
        }
    }

    /**
     * Used to assign apsects to the given ore dictionary item.
     * Attempts to automatically generate aspect tags by checking registered recipes.
     * IMPORTANT - this should only be used if you are not happy with the default aspects the object would be assigned.
     * @param oreDict the ore dictionary name
     * @param aspects A ObjectTags object of the associated aspects
     */
    public static void registerComplexObjectTag(String oreDict, AspectList aspects) {
        if (aspects==null) aspects=new AspectList();
        //List<ItemStack> ores = ThaumcraftApiHelper.getOresWithWildCards(oreDict);
        //if (ores!=null && ores.size()>0) {
        //    for (ItemStack ore:ores) {
        //        try {
        //            ItemStack oc = ore.copy();
        //            oc.setCount(1);
        //            registerComplexObjectTag(oc,aspects.copy());
        //        } catch (Exception e) {}
        //    }
        //}
    }

    public static class EntityTagsNBT {
        public EntityTagsNBT(String name, Object value) {
            this.name = name;
            this.value = value;
        }
        public String name;
        public Object value;
    }

    public static class EntityTags {
        public EntityTags(String entityName, AspectList aspects, EntityTagsNBT... nbts) {
            this.entityName = entityName;
            this.nbts = nbts;
            this.aspects = aspects;
        }
        public String entityName;
        public EntityTagsNBT[] nbts;
        public AspectList aspects;
    }


    /**
     * This is used to add aspects to entities which you can then scan using a thaumometer.
     * Also used to calculate vis drops from mobs.
     * @param entityName
     * @param aspects
     * @param nbt you can specify certain nbt keys and their values
     * 			  to differentiate between mobs. <br>For example the normal and wither skeleton:
     * 	<br>ThaumcraftApi.registerEntityTag("Skeleton", (new AspectList()).add(Aspect.DEATH, 5));
     * 	<br>ThaumcraftApi.registerEntityTag("Skeleton", (new AspectList()).add(Aspect.DEATH, 8), new NBTTagByte("SkeletonType",(byte) 1));
     */
    public static void registerEntityTag(String entityName, AspectList aspects, EntityTagsNBT... nbt ) {
        //CommonInternals.scanEntities.add(new EntityTags(entityName,aspects,nbt));
    }

    //WARP/////////////////////////////////////////

    /**
     * This method is used to determine how much warp is gained if the item is crafted. The warp
     * added is "sticky" warp
     * @param craftresult The item crafted
     * @param amount how much warp is gained
     */
    public static void addWarpToItem(ItemStack craftresult, int amount) {
        //CommonInternals.warpMap.put(Arrays.asList(craftresult.getItem(),craftresult.getItemDamage()),amount);
    }

    /**
     * Returns how much warp is gained from the item or research passed in
     * @param in itemstack or string
     * @return how much warp it will give
     */
    public static int getWarp(ItemStack in) {
        if (in==null) return 0;
        //if (in instanceof ItemStack && CommonInternals.warpMap.containsKey(Arrays.asList(in.getItem(),in.getItemDamage()))) {
        //    return CommonInternals.warpMap.get(Arrays.asList(in.getItem(),in.getItemDamage()));
        //}
        return 0;
    }

    // LOOT BAGS

    /**
     * Used to add possible loot to treasure bags. As a reference, the weight of gold coins are 2000
     * and a diamond is 50.
     * The weights are the same for all loot bag types - the only difference is how many items the bag
     * contains.
     * @param item
     * @param weight
     * @param bagTypes array of which type of bag to add this loot to. Multiple types can be specified
     * 0 = common, 1 = uncommon, 2 = rare
     */
    public static void addLootBagItem(ItemStack item, int weight, int... bagTypes) {
        //if (bagTypes==null || bagTypes.length==0)
        //    WeightedRandomLoot.lootBagCommon.add(new WeightedRandomLoot(item,weight));
        //else {
        //    for (int rarity:bagTypes) {
        //        switch(rarity) {
        //            case 0: WeightedRandomLoot.lootBagCommon.add(new WeightedRandomLoot(item,weight)); break;
        //            case 1: WeightedRandomLoot.lootBagUncommon.add(new WeightedRandomLoot(item,weight)); break;
        //            case 2: WeightedRandomLoot.lootBagRare.add(new WeightedRandomLoot(item,weight)); break;
        //        }
        //    }
        //}
    }


    // CROPS


    /**
     * This method is used to register an item that will act as a seed for the specified block.
     * If your seed items use IPlantable it might not be necessary to do this as I
     * attempt to automatically detect such links.
     * @param block
     * @param seed
     */
    public static void registerSeed(Block block, ItemStack seed) {
        //CommonInternals.seedList.put(block.getUnlocalizedName(), seed);
    }

    //public static ItemStack getSeed(Block block) {
    //    return CommonInternals.seedList.get(block.getUnlocalizedName());
    //}


    /**
     * To define mod crops you need to use FMLInterModComms in your @Mod.Init method.
     * There are two 'types' of crops you can add. Standard crops and clickable crops.
     *
     * Standard crops work like normal vanilla crops - they grow until a certain metadata
     * value is reached and you harvest them by destroying the block and collecting the blocks.
     * You need to create and ItemStack that tells the golem what block id and metadata represents
     * the crop when fully grown. Sending a metadata of [OreDictionary.WILDCARD_VALUE] will mean the metadata won't get
     * checked.
     * Example for vanilla wheat:
     * FMLInterModComms.sendMessage("Thaumcraft", "harvestStandardCrop", new ItemStack(Block.crops,1,7));
     *
     * Clickable crops are crops that you right click to gather their bounty instead of destroying them.
     * As for standard crops, you need to create and ItemStack that tells the golem what block id
     * and metadata represents the crop when fully grown. The golem will trigger the blocks onBlockActivated method.
     * Sending a metadata of [OreDictionary.WILDCARD_VALUE] will mean the metadata won't get checked.
     * Example (this will technically do nothing since clicking wheat does nothing, but you get the idea):
     * FMLInterModComms.sendMessage("Thaumcraft", "harvestClickableCrop", new ItemStack(Block.crops,1,7));
     *
     * Stacked crops (like reeds) are crops that you wish the bottom block should remain after harvesting.
     * As for standard crops, you need to create and ItemStack that tells the golem what block id
     * and metadata represents the crop when fully grown. Sending a metadata of [OreDictionary.WILDCARD_VALUE] will mean the actualy md won't get
     * checked. If it has the order upgrade it will only harvest if the crop is more than one block high.
     * Example:
     * FMLInterModComms.sendMessage("Thaumcraft", "harvestStackedCrop", new ItemStack(Block.reed,1,7));
     */

    // PORTABLE HOLE BLACKLIST
    /**
     * You can blacklist blocks that may not be portable holed through using the "portableHoleBlacklist"
     * string message using FMLInterModComms in your @Mod.Init method.
     *
     * Simply add the mod and block name you don't want the portable hole to go through with a
     * 'modid:blockname' designation. For example: "thaumcraft:log" or "minecraft:plank"
     *
     * You can also specify blockstates by adding ';' delimited 'name=value' pairs.
     * For example: "thaumcraft:log;variant=greatwood;variant=silverwood"
     *
     * You can also give an ore dictionary entry instead: For example: "logWood"
     */

    // NATIVE CLUSTERS
    /**
     * You can define certain ores that will have a chance to produce native clusters via FMLInterModComms
     * in your @Mod.Init method using the "nativeCluster" string message.
     * The format should be:
     * "[ore item/block id],[ore item/block metadata],[cluster item/block id],[cluster item/block metadata],[chance modifier float]"
     *
     * NOTE: The chance modifier is a multiplier applied to the default chance for that cluster to be produced (default 27.5% for a pickaxe of the core)
     *
     * Example for vanilla iron ore to produce one of my own native iron clusters (assuming default id's) at double the default chance:
     * FMLInterModComms.sendMessage("Thaumcraft", "nativeCluster","15,0,25016,16,2.0");
     */

    // LAMP OF GROWTH BLACKLIST
    /**
     * You can blacklist crops that should not be effected by the Lamp of Growth via FMLInterModComms
     * in your @Mod.Init method using the "lampBlacklist" itemstack message.
     * Sending a metadata of [OreDictionary.WILDCARD_VALUE] will mean the metadata won't get checked.
     * Example for vanilla wheat:
     * FMLInterModComms.sendMessage("Thaumcraft", "lampBlacklist", new ItemStack(Block.crops,1,OreDictionary.WILDCARD_VALUE));
     */

    // DIMENSION BLACKLIST
    /**
     * You can blacklist a dimension to not spawn certain thaumcraft features
     * in your @Mod.Init method using the "dimensionBlacklist" string message in the format "[dimension]:[level]"
     * The level values are as follows:
     * [0] stop all tc spawning and generation
     * [1] allow ore and node generation (and node special features)
     * [2] allow mob spawning
     * [3] allow ore and node gen + mob spawning (and node special features)
     * Example:
     * FMLInterModComms.sendMessage("Thaumcraft", "dimensionBlacklist", "15:1");
     */

    // BIOME BLACKLIST
    /**
     * You can blacklist a biome to not spawn certain thaumcraft features
     * in your @Mod.Init method using the "biomeBlacklist" string message in the format "[biome id]:[level]"
     * The level values are as follows:
     * [0] stop all tc spawning and generation
     * [1] allow ore and node generation (and node special features)
     * [2] allow mob spawning
     * [3] allow ore and node gen + mob spawning (and node special features)
     * Example:
     * FMLInterModComms.sendMessage("Thaumcraft", "biomeBlacklist", "180:2");
     */

    // CHAMPION MOB WHITELIST
    /**
     * You can whitelist an entity class so it can rarely spawn champion versions in your @Mod.Init method using
     * the "championWhiteList" string message in the format "[Entity]:[level]"
     * The entity must extend EntityMob.
     * [Entity] is in a similar format to what is used for mob spawners and such (see EntityList.class for vanilla examples).
     * The [level] value indicate how rare the champion version will be - the higher the number the more common.
     * The number roughly equals the [n] in 100 chance of a mob being a champion version.
     * You can give 0 or negative numbers to allow champions to spawn with a very low chance only in particularly dangerous places.
     * However anything less than about -2 will probably result in no spawns at all.
     * Example:
     * FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Thaumcraft.Wisp:1");
     */



}