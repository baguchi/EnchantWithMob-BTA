package baguchan.enchantwithmob.mixin;

import baguchan.enchantwithmob.packet.IEnchantPacket;
import baguchan.enchantwithmob.packet.MobEnchantPacket;
import net.minecraft.core.net.handler.NetHandler;
import net.minecraft.core.net.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = NetHandler.class, remap = false)
public abstract class NetHandlerMixin implements IEnchantPacket {
    @Shadow
    public abstract void handleInvalidPacket(Packet packet);

    @Override
    public void handleMobEnchant(MobEnchantPacket packet) {
        handleInvalidPacket(packet);
    }

}