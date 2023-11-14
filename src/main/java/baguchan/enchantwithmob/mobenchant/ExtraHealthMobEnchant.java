package baguchan.enchantwithmob.mobenchant;

public class ExtraHealthMobEnchant extends MobEnchant {
    public ExtraHealthMobEnchant(Properties properties) {
        super(properties);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + (enchantmentLevel - 1) * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 30;
    }


}
