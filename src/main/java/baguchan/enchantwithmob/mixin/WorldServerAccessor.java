package baguchan.enchantwithmob.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldServer.class)
public interface WorldServerAccessor {
    @Accessor("mcServer")
    MinecraftServer getMinecraftServer();
}
