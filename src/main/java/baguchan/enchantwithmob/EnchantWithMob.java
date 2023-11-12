package baguchan.enchantwithmob;

import baguchan.enchantwithmob.register.MobEnchants;
import baguchan.enchantwithmob.register.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EnchantWithMob implements ModInitializer {
    public static final String MOD_ID = "enchantwithmob";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        MobEnchants.init();
        ModItems.createItems();
    }
}
