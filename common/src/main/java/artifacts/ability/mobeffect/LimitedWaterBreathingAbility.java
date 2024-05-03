package artifacts.ability.mobeffect;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

import java.util.List;

public class LimitedWaterBreathingAbility extends MobEffectAbility {

    public LimitedWaterBreathingAbility() {
        super(MobEffects.WATER_BREATHING);
    }

    private int getMaxDuration() {
        return ModGameRules.SNORKEL_WATER_BREATHING_DURATION.get();
    }

    private boolean isInfinite() {
        return ModGameRules.SNORKEL_IS_INFINITE.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.LIMITED_WATER_BREATHING.get();
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        if (isInfinite()) {
            tooltip.add(tooltipLine("infinite"));
        } else {
            tooltip.add(tooltipLine("limited"));
        }
    }

    @Override
    protected int getDuration(LivingEntity entity) {
        int duration = getMaxDuration();
        if (!isInfinite()
                && entity instanceof Player
                && entity.getItemBySlot(EquipmentSlot.HEAD).is(Items.TURTLE_HELMET)
                && !entity.isEyeInFluid(FluidTags.WATER)
        ) {
            duration += 200;
        }
        return duration + 19;
    }

    @Override
    protected boolean shouldShowIcon() {
        return !isInfinite();
    }

    @Override
    public boolean shouldApplyMobEffect(LivingEntity entity) {
        return isInfinite() || !entity.isEyeInFluid(FluidTags.WATER);
    }

    @Override
    public boolean isNonCosmetic() {
        return getMaxDuration() > 0;
    }
}
