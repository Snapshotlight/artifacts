package artifacts.config.value;

import artifacts.ability.UpgradeToolTierAbility;
import artifacts.config.value.type.BooleanValueType;
import artifacts.config.value.type.NumberValueType;
import artifacts.config.value.type.StringRepresentableValueType;
import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;

public class ValueTypes {

    public static final BooleanValueType BOOLEAN = new BooleanValueType();
    public static final NumberValueType<Double> NON_NEGATIVE_DOUBLE = new NumberValueType<>(0D, Double.POSITIVE_INFINITY, Codec.DOUBLE, ByteBufCodecs.DOUBLE);
    public static final NumberValueType<Double> ATTRIBUTE_MODIFIER_AMOUNT = new NumberValueType<>(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Codec.DOUBLE, ByteBufCodecs.DOUBLE);
    public static final NumberValueType<Double> FRACTION = new NumberValueType<>(0D, 1D, Codec.DOUBLE, ByteBufCodecs.DOUBLE);
    public static final NumberValueType<Integer> NON_NEGATIVE_INT = new NumberValueType<>(0, Integer.MAX_VALUE, Codec.INT, ByteBufCodecs.INT);
    public static final NumberValueType<Integer> DURATION = new NumberValueType<>(0, 60 * 60 * 20, Codec.INT, ByteBufCodecs.INT);
    public static final NumberValueType<Integer> ENCHANTMENT_LEVEL = new NumberValueType<>(0, 100, Codec.INT, ByteBufCodecs.INT);
    public static final NumberValueType<Integer> MOB_EFFECT_LEVEL = new NumberValueType<>(0, 127, Codec.INT, ByteBufCodecs.INT);
    public static final StringRepresentableValueType<UpgradeToolTierAbility.Tier> TOOL_TIER = new StringRepresentableValueType<>(UpgradeToolTierAbility.Tier.values());

}
