package baguchan.enchantwithmob.packet;

import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import baguchan.enchantwithmob.utils.MobEnchantmentData;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.net.handler.NetHandler;
import net.minecraft.core.net.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MobEnchantPacket extends Packet {
    public int entityId;
    public MobEnchant enchantType;
    public int level;

    public MobEnchantPacket(Entity entity, MobEnchantmentData enchantType) {
        this.entityId = entity.id;
        this.enchantType = enchantType.getMobEnchant();
        this.level = enchantType.getEnchantLevel();
    }

    public MobEnchantPacket(int entityId, MobEnchant enchantType, int level) {
        this.entityId = entityId;
        this.enchantType = enchantType;
        this.level = level;
    }

    @Override
    public void readPacketData(DataInputStream dataInputStream) throws IOException {
        this.entityId = dataInputStream.readInt();
        this.enchantType = MobEnchantUtils.getMobEnchantNames(dataInputStream.readUTF());
        this.level = dataInputStream.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(this.entityId);
        dataOutputStream.writeUTF(MobEnchantUtils.getMobEnchantNames(this.enchantType));
        dataOutputStream.writeInt(this.level);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        ((IEnchantPacket) netHandler).handleMobEnchant(this);
    }

    @Override
    public int getPacketSize() {
        return 3;
    }
}
