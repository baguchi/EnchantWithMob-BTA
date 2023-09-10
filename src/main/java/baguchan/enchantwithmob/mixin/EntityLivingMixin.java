package baguchan.enchantwithmob.mixin;

import baguchan.enchantwithmob.register.MobEnchants;
import baguchan.enchantwithmob.utils.IEnchantCap;
import baguchan.enchantwithmob.utils.MobEnchantCap;
import baguchan.enchantwithmob.utils.MobEnchantCombatRules;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import com.mojang.nbt.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLiving.class)
public abstract class EntityLivingMixin extends Entity implements IEnchantCap {
    @Shadow
    public int health;
    @Shadow
    public int prevHealth;
    public MobEnchantCap MOB_ENCHANT_CAP = new MobEnchantCap();

    public EntityLivingMixin(World world) {
        super(world);
    }

    @Override
    public void spawnInit() {
        super.spawnInit();

        if (this.random.nextFloat() < 0.025F) {
            EntityLiving living = (EntityLiving) ((Object) this);
            MobEnchantUtils.addRandomEnchantmentToEntity(living, this, this.random, 20, true, true);
            int i = MobEnchantUtils.getMobEnchantLevelFromHandler(this.getEnchantCap().getMobEnchants(), MobEnchants.EXTRA_HEALTH);
            this.health += i * 4;
            this.prevHealth += i * 4;

        }
    }

    @Inject(method = "damageEntity", at = @At("HEAD"), remap = false, cancellable = true)
    protected void damageEntity(int i, DamageType damageType, CallbackInfo callbackInfo) {
        int i2 = MobEnchantUtils.getMobEnchantLevelFromHandler(this.getEnchantCap().getMobEnchants(), MobEnchants.PROTECTION);

        if (i > 0) {
            this.health -= (int) MobEnchantCombatRules.getDamageAddition(i, i2, this.getEnchantCap().getMobEnchants().size());
            callbackInfo.cancel();
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"), remap = false)
    public void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        tag.putCompound(MobEnchantUtils.TAG_STORED_MOBENCHANTS, MOB_ENCHANT_CAP.writeNBT());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"), remap = false)
    public void readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        MOB_ENCHANT_CAP.readNBT(tag.getCompound(MobEnchantUtils.TAG_STORED_MOBENCHANTS));
    }

    @Override
    public @Nullable MobEnchantCap getEnchantCap() {
        return MOB_ENCHANT_CAP;
    }

    @Override
    public void setEnchantCap(MobEnchantCap capability) {
        MOB_ENCHANT_CAP = capability;
    }
}
