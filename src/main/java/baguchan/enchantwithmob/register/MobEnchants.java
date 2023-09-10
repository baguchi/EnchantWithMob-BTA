package baguchan.enchantwithmob.register;

import baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.google.common.collect.Maps;

import java.util.Map;

public class MobEnchants {
    public static final Map<String, MobEnchant> MOB_ENCHANTS = Maps.newHashMap();

    public static final MobEnchant PROTECTION = new MobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.COMMON, 4));
    public static final MobEnchant EXTRA_HEALTH = new MobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 5));

    public static void init() {
        MOB_ENCHANTS.put("protection", PROTECTION);
        MOB_ENCHANTS.put("extra_health", EXTRA_HEALTH);
    }
}
