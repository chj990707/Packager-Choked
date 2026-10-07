package com.bunting.packagerchoked.mixin;

import com.bunting.packagerchoked.PackagerChokedConfig;
import com.bunting.packagerchoked.StackNumberUtil;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.simibubi.create.content.logistics.box.PackageItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;

import static com.bunting.packagerchoked.PackagerChokedDataComponents.PACKAGE_STACK_NUM;

@Mixin(PackageItem.class)
public class MixinPackageItem {

    @WrapMethod(
            method = "containing(Lnet/neoforged/neoforge/items/ItemStackHandler;)Lnet/minecraft/world/item/ItemStack;",
            remap = false
    )
    private static ItemStack updateStackNumber(ItemStackHandler stacks, Operation<ItemStack> original) {
        ItemStack result = original.call(stacks);
        ItemStackHandler boxContents = PackageItem.getContents(result);
        result.set(PACKAGE_STACK_NUM, StackNumberUtil.getStackNumber(boxContents));
        return result;
    }
}
