package baguchan.enchantwithmob.utils;

import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.register.MobEnchants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.Tag;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MobEnchantUtils {
    public static final String TAG_MOBENCHANT = "MobEnchant";
    public static final String TAG_ENCHANT_LEVEL = "EnchantLevel";
    public static final String TAG_STORED_MOBENCHANTS = "StoredMobEnchants";

    //when projectile Shooter has mob enchant, start Runnable
    public static void executeIfPresent(Entity entity, MobEnchant mobEnchantment, Runnable runnable) {
        if (entity != null) {
            if (entity instanceof IEnchantCap) {
                if (MobEnchantUtils.findMobEnchantFromHandler(((IEnchantCap) entity).getEnchantCap().getMobEnchants(), mobEnchantment)) {
                    runnable.run();
                }
            }
        }
    }

    public static void executeIfPresent(Entity entity, Runnable runnable) {
        if (entity != null) {
            if (entity instanceof IEnchantCap) {
                if (((IEnchantCap) entity).getEnchantCap().hasEnchant()) {
                    runnable.run();
                }
            }
            ;
        }
    }

    /**
     * check ItemStack has Mob Enchant
     *
     * @param stack MobEnchanted Item
     */
    public static boolean hasMobEnchant(ItemStack stack) {
        CompoundTag compoundnbt = stack.getData();
        return compoundnbt != null && compoundnbt.containsKey(TAG_STORED_MOBENCHANTS);
    }

    /**
     * check NBT has Mob Enchant
     *
     * @param compoundnbt nbt tag
     */
    public static ListTag getEnchantmentListForNBT(CompoundTag compoundnbt) {
        return compoundnbt != null ? compoundnbt.getList(TAG_STORED_MOBENCHANTS) : new ListTag();
    }

    /**
     * get Mob Enchantments From ItemStack
     *
     * @param stack MobEnchanted Item
     */
    public static Map<MobEnchant, Integer> getEnchantments(ItemStack stack) {
        ListTag listnbt = getEnchantmentListForNBT(stack.getData());
        return makeMobEnchantListFromListNBT(listnbt);
    }

    /**
     * set Mob Enchantments From ItemStack
     *
     * @param enchMap MobEnchants and those level map
     * @param stack   MobEnchanted Item
     */
    public static void setEnchantments(Map<MobEnchant, Integer> enchMap, ItemStack stack) {
        ListTag listnbt = new ListTag();

        for (Map.Entry<MobEnchant, Integer> entry : enchMap.entrySet()) {
            MobEnchant enchantment = entry.getKey();
            if (enchantment != null) {
                int i = entry.getValue();
                CompoundTag compoundnbt = new CompoundTag();
                compoundnbt.putString(TAG_MOBENCHANT, getMobEnchantNames(enchantment));
                compoundnbt.putShort(TAG_ENCHANT_LEVEL, (short) i);
                listnbt.addTag(compoundnbt);
                /*if (stack.getItem() == ModItems.MOB_ENCHANT_BOOK.get()) {
                    addMobEnchantToItemStack(stack, enchantment, i);
                }*/
            }
        }

       /* if (listnbt.getValue().isEmpty()) {
            stack.getData().(TAG_STORED_MOBENCHANTS);
        }*/
    }

    private static Map<MobEnchant, Integer> makeMobEnchantListFromListNBT(ListTag p_226652_0_) {
        Map<MobEnchant, Integer> map = Maps.newLinkedHashMap();

        for (int i = 0; i < p_226652_0_.getValue().size(); ++i) {
            Tag compoundnbt = p_226652_0_.getValue().get(i);
            if (compoundnbt instanceof CompoundTag) {
                MobEnchant mobEnchant = getMobEnchantNames(((CompoundTag) compoundnbt).getString(TAG_MOBENCHANT));
                map.put(mobEnchant, ((CompoundTag) compoundnbt).getInteger(TAG_ENCHANT_LEVEL));
            }
        }

        return map;
    }

    //add MobEnchantToItemstack (example,this method used to MobEnchantBook)
    public static void addMobEnchantToItemStack(ItemStack itemIn, MobEnchant mobenchant, int level) {
        ListTag listnbt = getEnchantmentListForNBT(itemIn.getData());

        boolean flag = true;
        String resourcelocation1 = null;


        for (int i = 0; i < listnbt.getValue().size(); ++i) {
            Tag compoundnbt = listnbt.getValue().get(i);
            if (compoundnbt instanceof CompoundTag) {
                resourcelocation1 = ((CompoundTag) compoundnbt).getString("MobEnchant");
                if (resourcelocation1 != null) {
                    if (((CompoundTag) compoundnbt).getInteger(TAG_ENCHANT_LEVEL) < level) {
                        ((CompoundTag) compoundnbt).putInt(TAG_ENCHANT_LEVEL, level);
                    }

                    flag = false;
                    break;
                }
            }
        }

        if (flag) {
            String string = getMobEnchantNames(mobenchant);
            CompoundTag compoundnbt1 = new CompoundTag();
            compoundnbt1.putString(TAG_MOBENCHANT, string);
            compoundnbt1.putInt(TAG_ENCHANT_LEVEL, level);
            listnbt.addTag(compoundnbt1);
        }

        itemIn.getData().put(TAG_STORED_MOBENCHANTS, listnbt);
    }

    public static MobEnchant getMobEnchantNames(String string) {
        Optional<Map.Entry<String, MobEnchant>> optional = MobEnchants.MOB_ENCHANTS.entrySet().stream().filter((data) -> {
            return data.getKey().equals(string);
        }).findFirst();
        return optional.map(Map.Entry::getValue).orElse(null);
    }

    public static String getMobEnchantNames(MobEnchant mobEnchant) {
        Optional<Map.Entry<String, MobEnchant>> optional = MobEnchants.MOB_ENCHANTS.entrySet().stream().filter((data) -> {
            return data.getValue().equals(mobEnchant);
        }).findFirst();
        return optional.map(Map.Entry::getKey).orElse(null);
    }


    /**
     * add Mob Enchantments From ItemStack
     *
     * @param itemIn     MobEnchanted Item
     * @param entity     Enchanting target
     * @param capability MobEnchant Capability
     */
    public static boolean addItemMobEnchantToEntity(ItemStack itemIn, EntityLiving entity, IEnchantCap capability) {
        ListTag listnbt = getEnchantmentListForNBT(itemIn.getData());
        boolean flag = false;

        for (int i = 0; i < listnbt.getValue().size(); ++i) {
            Tag compoundnbt = listnbt.getValue().get(i);
            if (compoundnbt instanceof CompoundTag) {
                if (checkAllowMobEnchantFromMob(MobEnchantUtils.getMobEnchantNames(((CompoundTag) compoundnbt).getString(TAG_STORED_MOBENCHANTS)), entity, capability)) {
                    capability.getEnchantCap().addMobEnchant(entity, MobEnchantUtils.getMobEnchantNames(((CompoundTag) compoundnbt).getString(TAG_STORED_MOBENCHANTS)), ((CompoundTag) compoundnbt).getInteger(TAG_ENCHANT_LEVEL));
                    flag = true;
                }
            }
        }
        return flag;
    }

    public static void removeMobEnchantToEntity(EntityLiving entity, IEnchantCap capability) {
        capability.getEnchantCap().removeAllMobEnchant(entity);
    }

    public static int getExperienceFromMob(IEnchantCap cap) {
        int l = 0;
        for (MobEnchantmentData list : cap.getEnchantCap().getMobEnchants()) {
            MobEnchant enchantment = list.getMobEnchant();
            int integer = list.getEnchantLevel();
            l += enchantment.getMinEnchantability(integer);
        }
        return l;
    }

    /**
     * add Mob Enchantments To Entity
     *
     * @param livingEntity Enchanting target
     * @param capability   MobEnchant Capability
     * @param data         MobEnchant Data
     */
    public static boolean addEnchantmentToEntity(EntityLiving livingEntity, IEnchantCap capability, MobEnchantmentData data) {
        boolean flag = false;
        if (checkAllowMobEnchantFromMob(data.enchantment, livingEntity, capability)) {
            capability.getEnchantCap().addMobEnchant(livingEntity, data.enchantment, data.enchantmentLevel);
            flag = true;
        }
        return flag;
    }

    /**
     * add Mob Enchantments To Entity
     *
     * @param livingEntity Enchanting target
     * @param capability   MobEnchant Capability
     * @param random       Random
     * @param level        max limit level MobEnchant
     * @param allowTresure setting is allow rare enchant
     */
    public static boolean addRandomEnchantmentToEntity(EntityLiving livingEntity, IEnchantCap capability, Random random, int level, boolean allowTresure, boolean allowCurse) {
        List<MobEnchantmentData> list = buildEnchantmentList(random, level, allowTresure, allowCurse);

        boolean flag = false;
        for (MobEnchantmentData enchantmentdata : list) {
            if (checkAllowMobEnchantFromMob(enchantmentdata.enchantment, livingEntity, capability)) {
                capability.getEnchantCap().addMobEnchant(livingEntity, enchantmentdata.enchantment, enchantmentdata.enchantmentLevel);
                flag = true;
            }
        }
        return flag;
    }

    public static ItemStack addRandomEnchantmentToItemStack(Random random, ItemStack stack, int level, boolean allowRare, boolean allowCurse) {
        List<MobEnchantmentData> list = buildEnchantmentList(random, level, allowRare, allowCurse);

        for (MobEnchantmentData enchantmentdata : list) {
            if (!enchantmentdata.enchantment.isDisabled()) {
                addMobEnchantToItemStack(stack, enchantmentdata.enchantment, enchantmentdata.enchantmentLevel);
            }
        }

        return stack;
    }

    public static boolean findMobEnchantmentData(List<MobEnchantmentData> list, MobEnchant findMobEnchant) {
        for (MobEnchantmentData mobEnchant : list) {
            if (mobEnchant.getMobEnchant().equals(findMobEnchant)) {
                return true;
            }
        }
        return false;
    }

    public static boolean findMobEnchant(List<MobEnchant> list, MobEnchant findMobEnchant) {
        if (list.contains(findMobEnchant)) {
            return true;
        }
        return false;
    }

    public static boolean findMobEnchantFromHandler(List<MobEnchantmentData> list, MobEnchant findMobEnchant) {
        for (MobEnchantmentData mobEnchant : list) {
            if (mobEnchant != null && !findMobEnchant.isDisabled()) {
                if (mobEnchant.getMobEnchant().equals(findMobEnchant)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkAllowMobEnchantFromMob(@Nullable MobEnchant mobEnchant, EntityLiving livingEntity, IEnchantCap capability) {
        //if (!EnchantConfig.COMMON.universalEnchant.get()) {
        if (mobEnchant != null && !mobEnchant.isCompatibleMob(livingEntity)) {
            return false;
        }
        //}

        if (mobEnchant.isDisabled()) {
            return false;
        }

        for (MobEnchantmentData enchantHandler : capability.getEnchantCap().getMobEnchants()) {
            if (mobEnchant != null && enchantHandler.getMobEnchant() != null && !enchantHandler.getMobEnchant().isCompatibleWith(mobEnchant)) {
                return false;
            }
        }

        //check mob enchant is not null
        return mobEnchant != null;
    }

    public static int getMobEnchantLevelFromHandler(List<MobEnchantmentData> list, MobEnchant findMobEnchant) {
        for (MobEnchantmentData mobEnchant : list) {
            if (mobEnchant != null) {
                if (mobEnchant.getMobEnchant().equals(findMobEnchant)) {
                    return mobEnchant.getEnchantLevel();
                }
            }
        }
        return 0;
    }

    /*
     * build MobEnchantment list like vanilla's enchantment
     */
    public static List<MobEnchantmentData> buildEnchantmentList(Random randomIn, int level, boolean allowTresure, boolean allowCursed) {
        List<MobEnchantmentData> list = Lists.newArrayList();
        WeightedRandomBag<MobEnchantmentData> weightedRandomBag = new WeightedRandomBag();
        int i = 1; //Enchantability
        if (i <= 0) {
            return list;
        } else {
            level = level + 1 + randomIn.nextInt(i / 4 + 1) + randomIn.nextInt(i / 4 + 1);
            float f = (randomIn.nextFloat() + randomIn.nextFloat() - 1.0F) * 0.15F;
            level = MathHelper.clamp(Math.round((float) level + (float) level * f), 1, Integer.MAX_VALUE);
            List<MobEnchantmentData> list1 = makeMobEnchantmentDatas(level, allowTresure, allowCursed);
            if (!list1.isEmpty()) {


                while (randomIn.nextInt(50) <= level) {
                    if (!list.isEmpty()) {
                        removeIncompatible(list1, list.get(list.size() - 1));
                    }
                    if (list1.isEmpty()) {
                        break;
                    }
                    list1.forEach((mobEnchantmentData) -> {
                        weightedRandomBag.addEntry(mobEnchantmentData, mobEnchantmentData.getMobEnchant().getRarity().getWeight());
                        list.add(weightedRandomBag.getRandom());
                    });

                    level /= 2;
                }
            }

            return list;
        }
    }

    /*
     * get MobEnchantment data.
     * when not allow rare enchantment,Ignore rare enchantment
     */
    public static List<MobEnchantmentData> makeMobEnchantmentDatas(int p_185291_0_, boolean allowTresure, boolean allowCursed) {
        List<MobEnchantmentData> list = Lists.newArrayList();

        for (String string : MobEnchants.MOB_ENCHANTS.keySet()) {
            MobEnchant enchantment = MobEnchants.MOB_ENCHANTS.get(string);
            if ((!enchantment.isCursedEnchant() || allowCursed) && (!enchantment.isTresureEnchant() || allowTresure) && !enchantment.isOnlyChest()) {
                for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
                    if (p_185291_0_ >= enchantment.getMinEnchantability(i) && p_185291_0_ <= enchantment.getMaxEnchantability(i)) {
                        list.add(new MobEnchantmentData(enchantment, i));
                        break;
                    }
                }
            }
        }

        return list;
    }

    private static void removeIncompatible(List<MobEnchantmentData> dataList, MobEnchantmentData data) {
        Iterator<MobEnchantmentData> iterator = dataList.iterator();

        while (iterator.hasNext()) {
            if (!data.enchantment.isCompatibleWith((iterator.next()).enchantment) || data.enchantment.isDisabled()) {
                iterator.remove();
            }
        }

    }
}