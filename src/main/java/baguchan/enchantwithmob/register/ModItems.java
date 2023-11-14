package baguchan.enchantwithmob.register;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.item.ItemMobEnchantBook;
import net.minecraft.core.item.Item;
import turniplabs.halplibe.helper.ItemHelper;

import static turniplabs.halplibe.helper.ItemHelper.findOpenIds;

public class ModItems {
    public static final Item mob_enchant_book = ItemHelper.createItem(EnchantWithMob.MOD_ID, new ItemMobEnchantBook("MobEnchantBook", findOpenIds(920)), "mob_enchant_book", "mob_enchant_book.png");

    public static void createItems() {

    }
}
