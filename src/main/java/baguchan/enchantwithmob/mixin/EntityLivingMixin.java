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
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityLiving.class, remap = false)
public abstract class EntityLivingMixin extends Entity implements IEnchantCap {
    @Shadow(remap = false)
    public int health;
    @Shadow(remap = false)
    public int prevHealth;

    @Shadow
    public abstract boolean hurt(Entity entity, int damage, DamageType type);

    @Shadow
    protected abstract void damageEntity(int i, DamageType damageType);

    public MobEnchantCap MOB_ENCHANT_CAP = new MobEnchantCap();

    public EntityLivingMixin(World world) {
        super(world);
    }

    @Override
    public void spawnInit() {
        super.spawnInit();

        if (this.random.nextFloat() < 0.085F + 0.1F * this.world.seasonManager.getSeasonProgress()) {
            EntityLiving living = (EntityLiving) ((Object) this);
            MobEnchantUtils.addRandomEnchantmentToEntity(living, this, this.random, (int) (15 + 20 * this.world.seasonManager.getSeasonProgress()), true, true);
            int i = MobEnchantUtils.getMobEnchantLevelFromHandler(this.getEnchantCap().getMobEnchants(), MobEnchants.EXTRA_HEALTH);
            this.health += i * 4;
            this.prevHealth += i * 4;

        }
    }

    @Inject(method = "damageEntity", at = @At("HEAD"), remap = false, cancellable = true)
    protected void damageEntityInject(int i, DamageType damageType, CallbackInfo callbackInfo) {
        int i2 = MobEnchantUtils.getMobEnchantLevelFromHandler(this.getEnchantCap().getMobEnchants(), MobEnchants.PROTECTION);

        if (i > 0) {
            this.health -= (int) MobEnchantCombatRules.getDamageAddition(i, i2, this.getEnchantCap().getMobEnchants().size());
            callbackInfo.cancel();
        }
    }

    @Inject(method = "hurt", at = @At("RETURN"))
    public void hurt(Entity entity, int damage, DamageType type, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof EntityLiving) {
            EntityLiving entityLiving = (EntityLiving) entity;
            int i = MobEnchantUtils.getMobEnchantLevelFromHandler(((IEnchantCap) entityLiving).getEnchantCap().getMobEnchants(), MobEnchants.THORN);
            if (!this.world.isClientSide) {
                if (this.random.nextFloat() < 0.085F * i) {
                    entity.hurt(entity, MathHelper.floor_float(MobEnchantCombatRules.getThornDamage(damage, i)), DamageType.COMBAT);
                }
            }
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
