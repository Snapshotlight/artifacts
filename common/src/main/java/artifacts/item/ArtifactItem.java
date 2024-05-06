package artifacts.item;

import artifacts.Artifacts;
import artifacts.ArtifactsClient;
import artifacts.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class ArtifactItem extends Item {

    @SuppressWarnings("UnstableApiUsage")
    public ArtifactItem(Properties properties) {
        super(properties.arch$tab(ModItems.CREATIVE_TAB.supplier()).stacksTo(1).rarity(Rarity.RARE).fireResistant());
    }

    public ArtifactItem() {
        this(new Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltipList, TooltipFlag tooltipFlag) {
        if (Artifacts.CONFIG.client.showTooltips) {
            List<MutableComponent> tooltip = new ArrayList<>();
            Player player = null;
            if (Minecraft.getInstance() != null) {
                player = ArtifactsClient.getLocalPlayer();
            }
            addTooltip(stack, tooltip, player);
            tooltip.forEach(line -> tooltipList.add(line.withStyle(ChatFormatting.GRAY)));
        }
    }

    protected void addTooltip(ItemStack stack, List<MutableComponent> tooltip, @Nullable Player player) {
        if (isCosmetic(stack)) {
            tooltip.add(Component.translatable("%s.tooltip.cosmetic".formatted(Artifacts.MOD_ID)).withStyle(ChatFormatting.ITALIC));
        } else {
            addEffectsTooltip(stack, tooltip, player);
        }
    }

    protected void addEffectsTooltip(ItemStack stack, List<MutableComponent> tooltip, @Nullable Player player) {
        tooltip.add(Component.translatable("%s.tooltip.item.%s".formatted(Artifacts.MOD_ID, getTooltipItemName())));
    }

    protected MutableComponent tooltipLine(String lineId, Object... args) {
        return Component.translatable("%s.tooltip.item.%s.%s".formatted(Artifacts.MOD_ID, getTooltipItemName(), lineId), args);
    }

    protected String getTooltipItemName() {
        return BuiltInRegistries.ITEM.getKey(this).getPath();
    }

    public abstract boolean isCosmetic(ItemStack stack);

    public boolean isOnCooldown(LivingEntity entity) {
        if (entity instanceof Player player) {
            return player.getCooldowns().isOnCooldown(this);
        }
        return false;
    }

    public void addCooldown(LivingEntity entity, int ticks) {
        if (ticks > 0 && !entity.level().isClientSide() && entity instanceof Player player) {
            player.getCooldowns().addCooldown(this, ticks);
        }
    }
}
