package artifacts.ability;

import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import artifacts.util.DamageSourceHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.event.EventResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public record FireAspectAbility(IntegerValue fireDuration) implements ArtifactAbility {

    public static MapCodec<FireAspectAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IntegerValue.field("amount", ModGameRules.FIRE_GAUNTLET_FIRE_DURATION).forGetter(FireAspectAbility::fireDuration)
    ).apply(instance, FireAspectAbility::new));

    public static StreamCodec<ByteBuf, FireAspectAbility> STREAM_CODEC = StreamCodec.composite(
            IntegerValue.defaultStreamCodec(ModGameRules.FIRE_GAUNTLET_FIRE_DURATION),
            FireAspectAbility::fireDuration,
            FireAspectAbility::new
    );

    public static EventResult onLivingHurt(LivingEntity entity, DamageSource damageSource, float amount) {
        LivingEntity attacker = DamageSourceHelper.getAttacker(damageSource);
        if (attacker != null && DamageSourceHelper.isMeleeAttack(damageSource) && !entity.fireImmune()) {
            int duration = AbilityHelper.maxInt(ModAbilities.FIRE_ASPECT.get(), attacker, ability -> ability.fireDuration().get(), false);
            entity.igniteForTicks(duration);
        }
        return EventResult.pass();
    }

    public static ArtifactAbility createDefaultInstance() {
        return ArtifactAbility.createDefaultInstance(CODEC);
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.FIRE_ASPECT.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return fireDuration().get() > 0;
    }
}
