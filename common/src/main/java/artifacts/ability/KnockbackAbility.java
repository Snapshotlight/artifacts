package artifacts.ability;

import artifacts.ability.value.DoubleValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import artifacts.util.DamageSourceHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.event.EventResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public record KnockbackAbility(DoubleValue strength) implements ArtifactAbility {

    public static final MapCodec<KnockbackAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DoubleValue.field("amount", ModGameRules.POCKET_PISTON_KNOCKBACK_STRENGTH).forGetter(KnockbackAbility::strength)
    ).apply(instance, KnockbackAbility::new));

    public static final StreamCodec<ByteBuf, KnockbackAbility> STREAM_CODEC = StreamCodec.composite(
            DoubleValue.defaultStreamCodec(ModGameRules.POCKET_PISTON_KNOCKBACK_STRENGTH),
            KnockbackAbility::strength,
            KnockbackAbility::new
    );

    public static ArtifactAbility createDefaultInstance() {
        return ArtifactAbility.createDefaultInstance(CODEC);
    }

    public static EventResult onLivingHurt(LivingEntity entity, DamageSource damageSource, float amount) {
        LivingEntity attacker = DamageSourceHelper.getAttacker(damageSource);
        if (attacker != null) {
            double knockbackBonus = AbilityHelper.sumDouble(ModAbilities.KNOCKBACK.get(), attacker, ability -> ability.strength().get(), false);
            entity.knockback(knockbackBonus, Mth.sin((float) (attacker.getYRot() * (Math.PI / 180))), -Mth.cos((float) (attacker.getYRot() * (Math.PI / 180))));
        }
        return EventResult.pass();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.KNOCKBACK.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return !strength().fuzzyEquals(0);
    }
}
