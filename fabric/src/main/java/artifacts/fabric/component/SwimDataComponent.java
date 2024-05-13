package artifacts.fabric.component;

import artifacts.component.SwimData;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.ladysnake.cca.api.v3.component.ComponentV3;

public class SwimDataComponent extends SwimData implements ComponentV3 {

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider provider) {
        shouldSwim = tag.getBoolean("ShouldSwim");
        hasTouchedWater = tag.getBoolean("HasTouchedWater");
        swimTime = tag.getInt("SwimTime");
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putBoolean("ShouldSwim", shouldSwim);
        tag.putBoolean("HasTouchedWater", hasTouchedWater);
        tag.putInt("SwimTime", swimTime);
    }
}
