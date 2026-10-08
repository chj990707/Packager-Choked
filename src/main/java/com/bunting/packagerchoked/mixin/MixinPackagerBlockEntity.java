package com.bunting.packagerchoked.mixin;

import com.bunting.packagerchoked.PackagerChokedConfig;
import com.bunting.packagerchoked.StackNumberUtil;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.logging.LogUtils;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.packager.PackagerBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.InvManipulationBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(PackagerBlockEntity.class)
public abstract class MixinPackagerBlockEntity extends SmartBlockEntity {
    @Shadow(remap = false)
    public InvManipulationBehaviour targetInventory;

    @Unique
    float packagerchoked$stacksEmpty = 0.0f;

    @Unique
    Map<String, Float> packagerchoked$stacksMap = new HashMap<>();

    public MixinPackagerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(
            method = "lazyTick",
            at = @At("HEAD"),
            remap = false
    )
    private void packagerchoked$checkEmptyStackNum(CallbackInfo ci){
        if (level == null || level.isClientSide()) return;
        IItemHandler targetInv = targetInventory.getInventory();
        packagerchoked$stacksEmpty = StackNumberUtil.getEmptyStackNumber(targetInv, packagerchoked$stacksMap);
    }

    @WrapMethod(
            method = "unwrapBox",
            remap = false
    )
    private boolean packagerchoked$checkInsertResult(ItemStack box, boolean simulate, Operation<Boolean> original){
        if (level == null || level.isClientSide) return original.call(box, simulate);
        Map<String, Float> boxMap = new HashMap<>();
        float boxStacks = StackNumberUtil.getStackNumber(PackageItem.getContents(box), boxMap);
        if (boxStacks > packagerchoked$stacksEmpty) return false;
        Map<String, Float> stackMapCopy = new HashMap<>(packagerchoked$stacksMap);
        for(Map.Entry<String, Float> entry : boxMap.entrySet()){
            String key = entry.getKey();
            Float value = entry.getValue();
            Float partialSlot = stackMapCopy.getOrDefault(key, 0.0f);
            Float emptySlot = stackMapCopy.getOrDefault("empty", 0.0f);
            if (partialSlot + emptySlot < value) return false;
            stackMapCopy.put(key, Math.max(partialSlot - value, 0.0f));
            stackMapCopy.put("empty", Math.min(emptySlot + partialSlot - value, emptySlot));
        }
        return original.call(box, simulate);
    }
}
