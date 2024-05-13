package artifacts.item;

import artifacts.Artifacts;
import artifacts.ability.ArtifactAbility;
import artifacts.ability.AttributeModifierAbility;
import artifacts.ability.SimpleAbility;
import artifacts.platform.PlatformServices;
import artifacts.registry.ModDataComponents;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModItems;
import artifacts.util.AbilityHelper;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WearableArtifactItem extends Item {

    private final Holder<SoundEvent> equipSound;
    private final float equipSoundPitch;

    public WearableArtifactItem(Item.Properties properties, Holder<SoundEvent> equipSound, float equipSoundPitch) {
        super(properties);
        this.equipSound = equipSound;
        this.equipSoundPitch = equipSoundPitch;
    }

    public SoundEvent getEquipSound() {
        return equipSound.value();
    }

    public float getEquipSoundPitch() {
        return equipSoundPitch;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltipList, TooltipFlag tooltipFlag) {
        if (Artifacts.CONFIG.client.showTooltips) {
            List<MutableComponent> tooltip = new ArrayList<>();
            if (AbilityHelper.isCosmetic(stack)) {
                tooltip.add(Component.translatable("%s.tooltip.cosmetic".formatted(Artifacts.MOD_ID)).withStyle(ChatFormatting.ITALIC));
            } else {
                PlatformServices.platformHelper.addCosmeticToggleTooltip(tooltip, stack);
            }
            tooltip.forEach(line -> tooltipList.add(line.withStyle(ChatFormatting.GRAY)));
        }
    }

    public static class Builder {

        private final String itemName;
        private Holder<SoundEvent> equipSound = SoundEvents.ARMOR_EQUIP_GENERIC;
        private float equipSoundPitch = 1;
        private final List<ArtifactAbility> abilities = new ArrayList<>();
        private final Item.Properties properties = new Item.Properties();

        public Builder(String itemName) {
            this.itemName = itemName;
        }

        public Builder equipSound(SoundEvent equipSound) {
            return equipSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(equipSound));
        }

        public Builder equipSound(Holder<SoundEvent> equipSound) {
            this.equipSound = equipSound;
            return this;
        }

        public Builder equipSoundPitch(float pitch) {
            this.equipSoundPitch = pitch;
            return this;
        }

        public Builder addAttributeModifier(Holder<Attribute> attribute, ModGameRules.DoubleGameRule amount) {
            return addAttributeModifier(attribute, amount, AttributeModifier.Operation.ADD_VALUE);
        }

        public Builder addAttributeModifier(Holder<Attribute> attribute, ModGameRules.DoubleGameRule amount, AttributeModifier.Operation operation) {
            return addAttributeModifier(attribute, amount, operation, true);
        }

        public Builder addAttributeModifier(Holder<Attribute> attribute, ModGameRules.DoubleGameRule amount, AttributeModifier.Operation operation, boolean ignoreCooldown) {
            return addAbility(AttributeModifierAbility.create(attribute, amount, operation, Artifacts.id(itemName + '/' + attribute.unwrapKey().orElseThrow().location().getPath()).toString(), ignoreCooldown));
        }

        public Builder addAbility(Pair<MapCodec<SimpleAbility>, ?> pair) {
            return addAbility(pair.getFirst());
        }

        public Builder addAbility(MapCodec<? extends ArtifactAbility> codec) {
            return addAbility(ArtifactAbility.createDefaultInstance(codec));
        }

        public Builder addAbility(ArtifactAbility ability) {
            this.abilities.add(ability);
            return this;
        }

        public Builder properties(Consumer<Properties> consumer) {
            consumer.accept(this.properties);
            return this;
        }

        public WearableArtifactItem build() {
            //noinspection UnstableApiUsage
            properties.arch$tab(ModItems.CREATIVE_TAB.supplier());
            properties.component(ModDataComponents.ABILITIES.get(), abilities);
            properties.stacksTo(1).rarity(Rarity.RARE).fireResistant();
            return new WearableArtifactItem(properties, equipSound, equipSoundPitch);
        }
    }
}
