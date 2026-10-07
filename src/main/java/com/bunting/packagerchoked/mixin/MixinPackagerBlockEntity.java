package com.bunting.packagerchoked.mixin;

import com.bunting.packagerchoked.PackagerChokedConfig;
import com.bunting.packagerchoked.StackNumberUtil;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
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

import static com.bunting.packagerchoked.PackagerChokedDataComponents.PACKAGE_STACK_NUM;

@Mixin(PackagerBlockEntity.class)
public abstract class MixinPackagerBlockEntity extends SmartBlockEntity {
    @Shadow(remap = false)
    public InvManipulationBehaviour targetInventory;

    @Unique
    float packagerchoked$stacksEmpty = 0.0f;

    public MixinPackagerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(
            method = "lazyTick",
            at = @At("HEAD"),
            remap = false
    )
    private void packagerchoked$checkEmptyStackNum(CallbackInfo ci){
        IItemHandler targetInv = targetInventory.getInventory();
        packagerchoked$stacksEmpty = StackNumberUtil.getEmptyStackNumber(targetInv);
    }

    @WrapMethod(
            method = "unwrapBox",
            remap = false
    )
    private boolean packagerchoked$checkInsertResult(ItemStack box, boolean simulate, Operation<Boolean> original){
        if (!box.has(PACKAGE_STACK_NUM)) {
            box.set(PACKAGE_STACK_NUM, StackNumberUtil.getStackNumber(PackageItem.getContents(box)));
        }
        float boxStacks = box.get(PACKAGE_STACK_NUM);
        if (boxStacks > packagerchoked$stacksEmpty) return false;
        else return original.call(box, simulate);
    }
}
