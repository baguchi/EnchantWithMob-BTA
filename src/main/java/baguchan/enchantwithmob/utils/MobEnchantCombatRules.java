package baguchan.enchantwithmob.utils;

public class MobEnchantCombatRules {

    public static float getDamageAddition(float damage, int mobEnchantLevel, int mobEnchantSize) {
        damage += 1.0F + Math.max(0, mobEnchantLevel - 1) * 1.0F;

        damage += mobEnchantSize * 0.1F;

        return damage;
    }

    public static float getDamageReduction(float damage, int mobEnchantLevel, int mobEnchantSize) {
        float f = 1.0F + mobEnchantSize / 2.0F;
        float f1 = 0.5F * mobEnchantLevel;
        float f2 = Math.max(f1 * 0.1F, (f1 + f) * 0.1F);
        damage *= (1.0F - f2);
        return damage;
    }
}
