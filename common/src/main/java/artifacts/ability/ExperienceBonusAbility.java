package artifacts.ability;

import artifacts.ability.value.DoubleValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public record ExperienceBonusAbility(DoubleValue experienceBonus) implements ArtifactAbility {

    public static MapCodec<ExperienceBonusAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DoubleValue.field("amount", ModGameRules.GOLDEN_HOOK_EXPERIENCE_BONUS).forGetter(ExperienceBonusAbility::experienceBonus)
    ).apply(instance, ExperienceBonusAbility::new));

    public static StreamCodec<ByteBuf, ExperienceBonusAbility> STREAM_CODEC = StreamCodec.composite(
            DoubleValue.defaultStreamCodec(ModGameRules.GOLDEN_HOOK_EXPERIENCE_BONUS),
            ExperienceBonusAbility::experienceBonus,
            ExperienceBonusAbility::new
    );

    public static ArtifactAbility createDefaultInstance() {
        return ArtifactAbility.createDefaultInstance(CODEC);
    }

    public static int getExperienceBonus(int originalXP, LivingEntity entity, Player attacker) {
        if (attacker == null || entity instanceof Player) {
            return 0;
        }

        double multiplier = AbilityHelper.maxDouble(ModAbilities.EXPERIENCE_BONUS.get(), attacker, ability -> ability.experienceBonus().get(), false);
        int experienceBonus = (int) (originalXP * multiplier);
        return Math.max(0, experienceBonus);
    }


    @Override
    public Type<?> getType() {
        return ModAbilities.EXPERIENCE_BONUS.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return !experienceBonus().fuzzyEquals(0);
    }
}
