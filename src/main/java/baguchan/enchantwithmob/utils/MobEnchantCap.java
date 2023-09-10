package baguchan.enchantwithmob.utils;

import baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.google.common.collect.Lists;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.Tag;
import net.minecraft.core.entity.EntityLiving;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MobEnchantCap {
    private static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("6699a403-e2cc-31e6-195e-4757200e0935");

    private List<MobEnchantmentData> mobEnchants = Lists.newArrayList();
    private Optional<EntityLiving> enchantOwner = Optional.empty();
    private boolean fromOwner;
    private EnchantType enchantType = EnchantType.NORMAL;


    /**
     * add MobEnchant on Entity
     *
     * @param entity       Entity given a MobEnchant
     * @param mobEnchant   Mob Enchant attached to mob
     * @param enchantLevel Mob Enchant Level
     */
    public void addMobEnchant(EntityLiving entity, MobEnchant mobEnchant, int enchantLevel) {

        this.mobEnchants.add(new MobEnchantmentData(mobEnchant, enchantLevel));
       /* if (!entity.world.isClientSide) {
            MobEnchantedMessage message = new MobEnchantedMessage(entity, mobEnchant, enchantLevel);
            EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
        }*/
        this.sync(entity);
    }

    public final void sync(EntityLiving entity) {
        MobEnchantCap capability = new MobEnchantCap();
        capability.mobEnchants = this.mobEnchants;
        capability.enchantOwner = this.enchantOwner;
        capability.enchantType = this.enchantType;
        ((IEnchantCap) entity).setEnchantCap(capability);

    }

    /*
     * Remove MobEnchant on Entity
     */
    public void removeAllMobEnchant(EntityLiving entity) {
        //Sync Client Enchant
        /*if (!entity.world.isClientSide) {
            RemoveAllMobEnchantMessage message = new RemoveAllMobEnchantMessage(entity);
            EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
        }*/
        this.mobEnchants.removeAll(mobEnchants);
    }

    public List<MobEnchantmentData> getMobEnchants() {
        return mobEnchants;
    }

    public boolean hasEnchant() {
        return !this.mobEnchants.isEmpty();
    }

    public Optional<EntityLiving> getEnchantOwner() {
        return enchantOwner;
    }

    public boolean hasOwner() {
        return this.enchantOwner.isPresent() && this.enchantOwner.get().isAlive();
    }

    //check this enchant from owner
    public boolean isFromOwner() {
        return this.fromOwner;
    }

    public EnchantType getEnchantType() {
        return enchantType;
    }

    public boolean isAncient() {
        return enchantType == EnchantType.ANCIENT;
    }

    public CompoundTag writeNBT() {
        CompoundTag nbt = new CompoundTag();

        ListTag listnbt = new ListTag();

        for (int i = 0; i < mobEnchants.size(); i++) {
            listnbt.addTag(mobEnchants.get(i).writeNBT());
        }

        nbt.put("StoredMobEnchants", listnbt);
        nbt.putBoolean("FromOwner", fromOwner);

        nbt.putString("EnchantType", enchantType.name());

        return nbt;
    }

    public void readNBT(CompoundTag nbt) {
        ListTag list = MobEnchantUtils.getEnchantmentListForNBT(nbt);

        mobEnchants.clear();

        for (int i = 0; i < list.getValue().size(); ++i) {
            Tag compoundnbt = list.getValue().get(i);

            if (compoundnbt instanceof CompoundTag) {
                MobEnchant mobEnchant = MobEnchantUtils.getMobEnchantNames(((CompoundTag) compoundnbt).getString(MobEnchantUtils.TAG_MOBENCHANT));
                //check mob enchant is not null
                if (mobEnchant != null) {
                    mobEnchants.add(new MobEnchantmentData(mobEnchant, ((CompoundTag) compoundnbt).getInteger(MobEnchantUtils.TAG_ENCHANT_LEVEL)));
                }
            }
        }

        fromOwner = nbt.getBoolean("FromOwner");
        enchantType = EnchantType.get(nbt.getString("EnchantType"));
    }

    public enum EnchantType {
        NORMAL,
        ANCIENT;

        public static EnchantType get(String nameIn) {
            for (EnchantType enchantType : values()) {
                if (enchantType.name().equals(nameIn))
                    return enchantType;
            }
            return NORMAL;
        }
    }
}