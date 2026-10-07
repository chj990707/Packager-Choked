package com.bunting.packagerchoked;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class StackNumberUtil {
    public static float getStackNumber(IItemHandler handler) {
        if (handler == null) return 0;
        float stacks = 0.0f;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stackInSlot = handler.getStackInSlot(i);
            if (!stackInSlot.isEmpty()){
                int maxInSlot = stackInSlot.getMaxStackSize();
                stacks += (float) stackInSlot.getCount() / maxInSlot;
            }
        }
        return stacks;
    }

    public static float getEmptyStackNumber(IItemHandler handler) {
        if (handler == null) return 0;
        return handler.getSlots() - getStackNumber(handler);
    }
}
