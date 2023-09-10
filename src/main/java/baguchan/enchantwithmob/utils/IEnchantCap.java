package baguchan.enchantwithmob.utils;

import org.jetbrains.annotations.Nullable;

public interface IEnchantCap {

    @Nullable
    MobEnchantCap getEnchantCap();

    void setEnchantCap(MobEnchantCap capability);
}