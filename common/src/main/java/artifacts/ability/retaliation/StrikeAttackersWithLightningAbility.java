package artifacts.ability.retaliation;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class StrikeAttackersWithLightningAbility extends RetaliationAbility {

    public StrikeAttackersWithLightningAbility() {
        super(ModGameRules.SHOCK_PENDANT_STRIKE_CHANCE, ModGameRules.SHOCK_PENDANT_COOLDOWN);
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.STRIKE_ATTACKERS_WITH_LIGHTNING.get();
    }

    @Override
    protected void applyEffect(LivingEntity target, LivingEntity attacker) {
        if (attacker.level().canSeeSky(BlockPos.containing(attacker.position()))) {
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(attacker.level());
            if (lightningBolt != null) {
                lightningBolt.moveTo(Vec3.atBottomCenterOf(attacker.blockPosition()));
                lightningBolt.setCause(attacker instanceof ServerPlayer player ? player : null);
                attacker.level().addFreshEntity(lightningBolt);
            }
        }
    }
}
