package baguchan.enchantwithmob.register;

import baguchan.enchantwithmob.mobenchant.ExtraHealthMobEnchant;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.mobenchant.ThornMobEnchant;
import com.google.common.collect.Maps;

import java.util.Map;

public class MobEnchants {
    public static final Map<String, MobEnchant> MOB_ENCHANTS = Maps.newHashMap();

    public static final MobEnchant PROTECTION = new MobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.COMMON, 4));
    public static final MobEnchant THORN = new ThornMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.UNCOMMON, 2));
    public static final MobEnchant EXTRA_HEALTH = new ExtraHealthMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.RARE, 5));

    public static void init() {
        MOB_ENCHANTS.put("protection", PROTECTION);
        MOB_ENCHANTS.put("thorn", THORN);
        MOB_ENCHANTS.put("extra_health", EXTRA_HEALTH);
    }
}
