package baguchan.enchantwithmob.mobenchant;

import net.minecraft.core.entity.EntityLiving;

public class MobEnchant {
    protected final Rarity enchantType;
    private final int level;
    private int minlevel = 1;

    public MobEnchant(Properties properties) {
        this.enchantType = properties.enchantType;
        this.level = properties.level;
    }

    public Rarity getRarity() {
        return enchantType;
    }

    public MobEnchant setMinLevel(int level) {
        this.minlevel = level;

        return this;
    }


    /**
     * Returns the minimum level that the enchantment can have.
     */
    public int getMinLevel() {
        return minlevel;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return level;
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + enchantmentLevel * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 5;
    }


    public void tick(EntityLiving entity, int level) {

    }

    public final boolean isCompatibleWith(MobEnchant enchantmentIn) {
        return this.canApplyTogether(enchantmentIn) && enchantmentIn.canApplyTogether(this);
    }

    public boolean isTresureEnchant() {
        return false;
    }

    public boolean isOnlyChest() {
        return false;
    }

    public boolean isCompatibleMob(EntityLiving livingEntity) {
        return true;
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    protected boolean canApplyTogether(MobEnchant ench) {
        return this != ench;
    }


    public boolean isCursedEnchant() {
        return false;
    }

    public boolean isDisabled() {
        return false;
    }


    public static class Properties {
        private final Rarity enchantType;
        private final int level;

        public Properties(Rarity enchantType, int level) {
            this.enchantType = enchantType;
            this.level = level;
        }
    }

    public static enum Rarity {
        COMMON(10),
        UNCOMMON(5),
        RARE(2),
        VERY_RARE(1);

        private final int weight;

        private Rarity(int rarityWeight) {
            this.weight = rarityWeight;
        }

        /**
         * Retrieves the weight of Rarity.
         */
        public int getWeight() {
            return this.weight;
        }
    }
}