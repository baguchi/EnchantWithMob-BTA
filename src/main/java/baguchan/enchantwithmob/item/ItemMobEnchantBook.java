package baguchan.enchantwithmob.item;

import baguchan.enchantwithmob.register.MobEnchants;
import baguchan.enchantwithmob.utils.IEnchantCap;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class ItemMobEnchantBook extends Item {
    public ItemMobEnchantBook(String name, int openIds) {
        super(name, openIds);
        this.setMaxStackSize(1);
    }

    @Override
    public boolean useItemOnEntity(ItemStack itemstack, EntityLiving entityliving, EntityPlayer entityPlayer) {
        if (entityliving instanceof IEnchantCap) {
            if (((IEnchantCap) entityliving).getEnchantCap() != null && !((IEnchantCap) entityliving).getEnchantCap().hasEnchant()) {

                if (MobEnchantUtils.hasMobEnchant(itemstack)) {
                    if (MobEnchantUtils.addItemMobEnchantToEntity(itemstack, entityliving, ((IEnchantCap) entityliving))) {
                        itemstack.consumeItem(entityPlayer);
                        int i = MobEnchantUtils.getMobEnchantLevelFromHandler(((IEnchantCap) entityliving).getEnchantCap().getMobEnchants(), MobEnchants.EXTRA_HEALTH);
                        entityliving.health += i * 4;
                        entityliving.prevHealth += i * 4;

                        return true;
                    }
                } else {
                    MobEnchantUtils.addRandomEnchantmentToEntity(entityliving, ((IEnchantCap) entityliving), itemRand, 10, true, false);
                    itemstack.consumeItem(entityPlayer);
                    int i = MobEnchantUtils.getMobEnchantLevelFromHandler(((IEnchantCap) entityliving).getEnchantCap().getMobEnchants(), MobEnchants.EXTRA_HEALTH);
                    entityliving.health += i * 4;
                    entityliving.prevHealth += i * 4;

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (entityplayer instanceof IEnchantCap) {
            if (((IEnchantCap) entityplayer).getEnchantCap() != null && !((IEnchantCap) entityplayer).getEnchantCap().hasEnchant()) {
                if (MobEnchantUtils.hasMobEnchant(itemstack)) {
                    if (MobEnchantUtils.addItemMobEnchantToEntity(itemstack, entityplayer, ((IEnchantCap) entityplayer))) {
                        itemstack.consumeItem(entityplayer);
                        int i = MobEnchantUtils.getMobEnchantLevelFromHandler(((IEnchantCap) entityplayer).getEnchantCap().getMobEnchants(), MobEnchants.EXTRA_HEALTH);
                        entityplayer.health += i * 4;
                        entityplayer.prevHealth += i * 4;
                        return itemstack;
                    }
                } else {
                    MobEnchantUtils.addRandomEnchantmentToEntity(entityplayer, ((IEnchantCap) entityplayer), itemRand, 10, true, false);
                    itemstack.consumeItem(entityplayer);
                    int i = MobEnchantUtils.getMobEnchantLevelFromHandler(((IEnchantCap) entityplayer).getEnchantCap().getMobEnchants(), MobEnchants.EXTRA_HEALTH);
                    entityplayer.health += i * 4;
                    entityplayer.prevHealth += i * 4;

                    return itemstack;
                }
            }
        }
        return itemstack;
    }
}
