package com.bunting.packagerchoked;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.Map;

public class StackNumberUtil {

    public static float getStackNumber(IItemHandler handler, @Nullable Map<String, Float> stackMap) {
        if(stackMap != null) stackMap.clear();
        if (handler == null) return 0;
        float stacks = 0.0f;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stackInSlot = handler.getStackInSlot(i);
            if (stackInSlot.isEmpty()) continue;
            int maxInSlot = stackInSlot.getMaxStackSize();
            float currentStackSize = (float) stackInSlot.getCount() / maxInSlot;
            stacks += currentStackSize;
            String descId = stackInSlot.getDescriptionId();
            if(stackMap != null) stackMap.put(descId, stackMap.getOrDefault(descId, 0.0f) + currentStackSize);
        }
        return stacks;
    }

    public static float getEmptyStackNumber(IItemHandler handler, @Nullable Map<String, Float> stackMap) {
        if(stackMap != null) stackMap.clear();
        if (handler == null) return 0;
        float stacks = 0.0f;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stackInSlot = handler.getStackInSlot(i);
            if (stackInSlot.isEmpty()) {
                stacks += 1.0f;
                if(stackMap != null) stackMap.put("empty", stackMap.getOrDefault("empty", 0.0f) + 1.0f);
            } else {
                int maxInSlot = stackInSlot.getMaxStackSize();
                if (maxInSlot <= stackInSlot.getCount()) continue;
                float currentStackSize = 1.0f - (float) stackInSlot.getCount() / maxInSlot;
                stacks += currentStackSize;
                String descId = stackInSlot.getDescriptionId();
                if(stackMap != null) stackMap.put(descId, stackMap.getOrDefault(descId, 0.0f) + currentStackSize);
            }
        }
        return stacks;
    }
}
