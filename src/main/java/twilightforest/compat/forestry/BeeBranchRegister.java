package twilightforest.compat.forestry;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IClassification;
import forestry.apiculture.genetics.alleles.AlleleEffects;
import forestry.core.genetics.IBranchDefinition;
import forestry.core.genetics.alleles.AlleleHelper;
import forestry.core.genetics.alleles.EnumAllele;

import java.util.Locale;

public enum BeeBranchRegister implements IBranchDefinition {
    TWILIGHT     ( "Crepusculum" ),
    SWAMP        ( "Palus"       ),
    DARK_FOREST  ( "Maestus"     ),
    SNOWY_FOREST ( "Frigidus"    ),
    HIGHLANDS    ( "Superior"    );

    private final IClassification classification;
    private static IAllele[] defaultTemplate;

    BeeBranchRegister(String genus) {
        classification = BeeManager.beeFactory.createBranch(this.name().toLowerCase(Locale.ENGLISH), genus);
    }

    @Override
    public IAllele[] getTemplate() {
        IAllele[] template = copyDefaultTemplate();
        IAllele[] copiedAlleles = new IAllele[template.length];
        System.arraycopy(template, 0, copiedAlleles, 0, template.length);

        return copiedAlleles;
    }

    @Override
    public IClassification getBranch() {
        return this.classification;
    }

    private static IAllele[] copyDefaultTemplate() {
        if (defaultTemplate == null) {
            defaultTemplate = new IAllele[EnumBeeChromosome.values().length];

            AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.SPEED                , EnumAllele.Speed.NORMAL     );
            AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.LIFESPAN             , EnumAllele.Lifespan.NORMAL  );
            AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.FERTILITY            , EnumAllele.Fertility.NORMAL );
            AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.NONE   );
            AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.NEVER_SLEEPS         , false                       );
            AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.HUMIDITY_TOLERANCE   , EnumAllele.Tolerance.NONE   );
            AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.TOLERATES_RAIN       , false                       );
            AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.CAVE_DWELLING        , false                       );
            AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.FLOWER_PROVIDER      , EnumAllele.Flowers.VANILLA  );
            AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.FLOWERING            , EnumAllele.Flowering.AVERAGE);
            AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.TERRITORY            , EnumAllele.Territory.AVERAGE);
            AlleleHelper.getInstance().set(defaultTemplate, EnumBeeChromosome.EFFECT               , AlleleEffects.effectNone    );
        }

        IAllele[] copiedAlleles = new IAllele[defaultTemplate.length];
        System.arraycopy(defaultTemplate, 0, copiedAlleles, 0, defaultTemplate.length);

        return copiedAlleles;
    }
}
