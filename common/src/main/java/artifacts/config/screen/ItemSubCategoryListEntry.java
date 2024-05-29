package artifacts.config.screen;

import com.google.common.collect.Iterators;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public class ItemSubCategoryListEntry extends SubCategoryListEntry {

    private final ItemStack stack;

    public ItemSubCategoryListEntry(Item item, List<AbstractConfigListEntry<?>> entries) {
        // noinspection deprecation
        super(item.getDescription(), List.copyOf(entries), false);
        this.stack = new ItemStack(item);
    }

    @Override
    public Iterator<String> getSearchTags() {
        // noinspection unchecked
        return Iterators.filter(super.getSearchTags(), tag -> getValue().stream()
                .map(AbstractConfigEntry::getSearchTags)
                .noneMatch(iterator -> Iterators.any(iterator, s -> tag != null && tag.equals(s))));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<AbstractConfigListEntry> filteredEntries() {
        return new AbstractList<>() {
            public Iterator<AbstractConfigListEntry> iterator() {
                return Iterators.filter(
                        ItemSubCategoryListEntry.this.getValue().iterator(),
                        (entry) -> entry.isDisplayed() && ItemSubCategoryListEntry.this.getConfigScreen() != null
                );
            }

            public AbstractConfigListEntry get(int index) {
                return Iterators.get(this.iterator(), index);
            }

            public int size() {
                return Iterators.size(this.iterator());
            }
        };
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);

        graphics.renderItem(stack, x - 4, y + 2);
        graphics.drawString(Minecraft.getInstance().font, this.getActualDisplayedFieldName().getVisualOrderText(), x + 16, y + 6, -1);
    }

    @Override
    public Component getDisplayedFieldName() {
        return CommonComponents.EMPTY;
    }

    public Component getActualDisplayedFieldName() {
        MutableComponent text = this.getFieldName().copy();
        boolean hasError = this.getConfigError().isPresent();
        boolean isEdited = this.isEdited();
        if (hasError) {
            text = text.withStyle(ChatFormatting.RED);
        }

        if (isEdited) {
            text = text.withStyle(ChatFormatting.ITALIC);
        }

        if (!hasError && !isEdited) {
            text = text.withStyle(ChatFormatting.GRAY);
        }

        if (!this.isEnabled()) {
            text = text.withStyle(ChatFormatting.DARK_GRAY);
        }

        return text;
    }
}
