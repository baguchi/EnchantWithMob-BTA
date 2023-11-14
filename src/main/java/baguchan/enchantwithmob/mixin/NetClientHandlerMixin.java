package baguchan.enchantwithmob.mixin;

import baguchan.enchantwithmob.packet.IEnchantPacket;
import baguchan.enchantwithmob.packet.MobEnchantPacket;
import baguchan.enchantwithmob.utils.IEnchantCap;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.net.handler.NetClientHandler;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = NetClientHandler.class, remap = false)
public class NetClientHandlerMixin implements IEnchantPacket {
    @Shadow
    private Minecraft mc;

    @Override
    public void handleMobEnchant(MobEnchantPacket packet) {
        Entity entity = getEntityByID(packet.entityId);
        if (entity != null && entity instanceof EntityLiving) {
            if (entity instanceof IEnchantCap) {
                if (!MobEnchantUtils.findMobEnchantFromHandler(((IEnchantCap) entity).getEnchantCap().getMobEnchants(), packet.enchantType)) {
                    ((IEnchantCap) entity).getEnchantCap().addMobEnchant((EntityLiving) entity, packet.enchantType, packet.level);
                }
            }
        }


    }

    @Shadow
    protected Entity getEntityByID(int i) {
        return null;
    }
}