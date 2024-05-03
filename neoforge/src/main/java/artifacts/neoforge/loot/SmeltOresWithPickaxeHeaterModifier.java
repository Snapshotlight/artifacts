package artifacts.neoforge.loot;

import artifacts.event.ArtifactEvents;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.function.Supplier;

public class SmeltOresWithPickaxeHeaterModifier extends LootModifier {

    public static final Supplier<Codec<SmeltOresWithPickaxeHeaterModifier>> CODEC = Suppliers.memoize(
            () -> RecordCodecBuilder.create(instance -> codecStart(instance)
                    .apply(instance, SmeltOresWithPickaxeHeaterModifier::new)
            )
    );

    public SmeltOresWithPickaxeHeaterModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> items, LootContext context) {
        return ArtifactEvents.getPickaxeHeaterModifiedBlockDrops(items, context, Tags.Blocks.ORES, Tags.Items.RAW_MATERIALS);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
