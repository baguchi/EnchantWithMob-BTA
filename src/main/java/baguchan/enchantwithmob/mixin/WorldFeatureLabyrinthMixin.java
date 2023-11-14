package baguchan.enchantwithmob.mixin;

import baguchan.enchantwithmob.register.ModItems;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(value = WorldFeatureLabyrinth.class, remap = false)
public class WorldFeatureLabyrinthMixin {

    @Inject(method = "pickCheckLootItem(Ljava/util/Random;)Lnet/minecraft/core/item/ItemStack;", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", shift = At.Shift.AFTER), cancellable = true)
    private void addToLabyrinthLoot(Random random, CallbackInfoReturnable<ItemStack> cir) {
        int i = random.nextInt(32);
        if (i == 0) {
            ItemStack stack = new ItemStack(ModItems.mob_enchant_book);

            cir.setReturnValue(MobEnchantUtils.addRandomEnchantmentToItemStack(random, stack, 25 + random.nextInt(10), true, false));
        }
    }
}
