package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;

import java.util.function.Supplier;

public class SimpleAbility implements ArtifactAbility {

    private final Supplier<Type<SimpleAbility>> type;
    private final Supplier<Boolean> isEnabled;

    public SimpleAbility(Supplier<Type<SimpleAbility>> type, Supplier<Boolean> isEnabled) {
        this.type = type;
        this.isEnabled = isEnabled;
    }

    @Override
    public Type<?> getType() {
        return type.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return isEnabled.get();
    }

    public static SimpleAbility cancelFallDamage() {
        return new SimpleAbility(ModAbilities.CANCEL_FALL_DAMAGE, ModGameRules.BUNNY_HOPPERS_DO_CANCEL_FALL_DAMAGE);
    }

    public static SimpleAbility sprintOnWater() {
        return new SimpleAbility(ModAbilities.SPRINT_ON_WATER, ModGameRules.AQUA_DASHERS_ENABLED);
    }

    public static SimpleAbility sinking() {
        return new SimpleAbility(ModAbilities.SINKING, ModGameRules.CHARM_OF_SINKING_ENABLED);
    }

    public static SimpleAbility smeltOres() {
        return new SimpleAbility(ModAbilities.SMELT_ORES, ModGameRules.PICKAXE_HEATER_ENABLED);
    }

    public static SimpleAbility scareCreepers() {
        return new SimpleAbility(ModAbilities.SCARE_CREEPERS, ModGameRules.KITTY_SLIPPERS_ENABLED);
    }

    public static SimpleAbility walkOnPowderSnow() {
        return new SimpleAbility(ModAbilities.WALK_ON_POWDER_SNOW, ModGameRules.SNOWSHOES_ALLOW_WALKING_ON_POWDER_SNOW);
    }

    public static SimpleAbility lightningImmunity() {
        return new SimpleAbility(ModAbilities.LIGHTNING_IMMUNITY, ModGameRules.SHOCK_PENDANT_DO_CANCEL_LIGHTNING_DAMAGE);
    }
}
