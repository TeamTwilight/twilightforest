package twilightforest.compat.forestry;

import forestry.api.apiculture.*;
import forestry.api.genetics.IAllele;
import forestry.apiculture.genetics.Bee;
import forestry.apiculture.genetics.IBeeDefinition;
import forestry.core.genetics.IBranchDefinition;
import forestry.core.genetics.alleles.AlleleHelper;
import net.minecraft.item.ItemStack;
import twilightforest.TwilightForestMod;

import java.util.Locale;

public enum BeeRegister implements IBeeDefinition {

    TWILIT           ( BeeBranchRegister.TWILIGHT     , "Crepusculum"  ),
    ENSORCELIZED     ( BeeBranchRegister.TWILIGHT     , "Carminibus"   ),
    MIRY             ( BeeBranchRegister.SWAMP        , "Paluster"     ),
    OMINOUS          ( BeeBranchRegister.DARK_FOREST  , "Minaces"      ),
    FRIGID           ( BeeBranchRegister.SNOWY_FOREST , "Frigidus"     ),
    DRAINED          ( BeeBranchRegister.HIGHLANDS    , "Adficio"      ),

    CONFUSED         ( BeeBranchRegister.TWILIGHT     , "Confusus"     ),
    ENLIGHTENED      ( BeeBranchRegister.TWILIGHT     , "Cultus"       ),

    HERBAL           ( BeeBranchRegister.TWILIGHT     , "Herba"        ),
    TREE             ( BeeBranchRegister.TWILIGHT     , "Arboreal"     ),

    DRUIDIC          ( BeeBranchRegister.TWILIGHT     , "Druidae"      ),

    RHYTHMIC         ( BeeBranchRegister.TWILIGHT     , "Tempus"       ),
    TRANSFIGURATIVE  ( BeeBranchRegister.TWILIGHT     , "Transformato" ),
    EXCAVATOR        ( BeeBranchRegister.TWILIGHT     , "Tergeo"       ),
    CATEGORICAL      ( BeeBranchRegister.TWILIGHT     , "Catalogus"    ),

    ENTANGLED        ( BeeBranchRegister.SWAMP        , "Capti"        ),
    LABYRINTHINE     ( BeeBranchRegister.SWAMP        , "Labyrinthus"  ),

    MISTY            ( BeeBranchRegister.DARK_FOREST  , "Caliginosus"  ),
    PIERCING         ( BeeBranchRegister.DARK_FOREST  , "Penetralis"   ),

    FROZEN           ( BeeBranchRegister.SNOWY_FOREST , "Glacialis"    ),
    GELID            ( BeeBranchRegister.SNOWY_FOREST , "Gelida"       ),

    ANIMATED         ( BeeBranchRegister.HIGHLANDS    , "Vividus"      ),
    SENTINEL         ( BeeBranchRegister.HIGHLANDS    , "Excubitor"    ),

    MYTHOLOGICAL     ( BeeBranchRegister.TWILIGHT     , "Fabulares"    ),
    ENTROPHIED       ( BeeBranchRegister.TWILIGHT     , "Champion"     ),

    SHEEPISH         ( BeeBranchRegister.TWILIGHT     , "Modestus"     ),

    SERPENTINE       ( BeeBranchRegister.TWILIGHT     , "Anguis"       ),

    NECROMANTIC      ( BeeBranchRegister.TWILIGHT     , "Necromantiae" ),

    MINOTAUR         ( BeeBranchRegister.SWAMP        , "Minotaurus"   ),
    DUPLICATIVE      ( BeeBranchRegister.SWAMP        , "Capitibus"    ),

    PHANTASMAGORICAL ( BeeBranchRegister.DARK_FOREST  , "Inconcilio"   ),
    CARMINIATED      ( BeeBranchRegister.DARK_FOREST  , "Vis"          ),

    CRYPTID          ( BeeBranchRegister.SNOWY_FOREST , "Creatura"     ),
    MONARCHICAL      ( BeeBranchRegister.SNOWY_FOREST , "Monarchica"   ),

    ONTOMANTIC       ( BeeBranchRegister.HIGHLANDS    , "Rego"         );

    private final IBranchDefinition branch;
    private final IAlleleBeeSpecies species;
    private IAllele[] template;
    private IBeeGenome genome;

    BeeRegister(IBranchDefinition branch, String species) {
        this.branch = branch;

        String lowerCase = this.toString().toLowerCase(Locale.ROOT);

        IAlleleBeeSpeciesBuilder speciesBuilder = BeeManager.beeFactory.createSpecies(
                TwilightForestMod.ID,
                TwilightForestMod.ID + ":" + lowerCase,
                true,
                "Drullkus",
                TwilightForestMod.ID + ".bee." + lowerCase,
                TwilightForestMod.ID + ".bee." + lowerCase + ".desc",
                branch.getBranch(),
                species.toLowerCase(Locale.ROOT),
                0xFF_FF_FF,
                0xFF_FF_FF
        );
        this.setSpeciesProperties(speciesBuilder);
        this.species = speciesBuilder.build();

        this.template = this.branch.getTemplate();
        AlleleHelper.getInstance().set(this.template, EnumBeeChromosome.SPECIES, this.species);
        this.setAlleles(this.template);
        this.genome = BeeManager.beeRoot.templateAsGenome(this.template);
        BeeManager.beeRoot.registerTemplate(this.template);
    }

    @Override
    public IAllele[] getTemplate() {
        IAllele[] copiedAlleles = new IAllele[template.length];
        System.arraycopy(this.template, 0, copiedAlleles, 0, template.length);

        return copiedAlleles;
    }

    @Override
    public final IBeeGenome getGenome() {
        return this.genome;
    }

    @Override
    public final IBee getIndividual() {
        return new Bee(this.genome);
    }

    @Override
    public ItemStack getMemberStack(EnumBeeType enumBeeType) {
        IBee bee = this.getIndividual();
        return BeeManager.beeRoot.getMemberStack(bee, enumBeeType);
    }

    private void setSpeciesProperties(IAlleleBeeSpeciesBuilder speciesProperties) {}

    private void setAlleles(IAllele[] alleles) {}
}
