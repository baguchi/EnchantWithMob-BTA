package baguchan.enchantwithmob.utils;

import baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.mojang.nbt.CompoundTag;

public class MobEnchantmentData {
    public MobEnchant enchantment;
    public int enchantmentLevel;

    public MobEnchantmentData(MobEnchant enchantmentObj, int enchLevel) {
        this.enchantment = enchantmentObj;
        this.enchantmentLevel = enchLevel;
    }

    public int getEnchantLevel() {
        return enchantmentLevel;
    }

    public MobEnchant getMobEnchant() {
        return enchantment;
    }

    public CompoundTag writeNBT() {
        CompoundTag nbt = new CompoundTag();
        if (enchantment != null) {
            nbt.putString("MobEnchant", MobEnchantUtils.getMobEnchantNames(enchantment));
            nbt.putInt("EnchantLevel", enchantmentLevel);
        }

        return nbt;
    }

    public void readNBT(CompoundTag nbt) {
        enchantment = MobEnchantUtils.getMobEnchantNames(nbt.getString(MobEnchantUtils.TAG_STORED_MOBENCHANTS));
        enchantmentLevel = nbt.getInteger(MobEnchantUtils.TAG_ENCHANT_LEVEL);
    }
}