package dev.blueish.origins.swap.mixin;

import io.github.apace100.origins.networking.ModPacketsC2S;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ModPacketsC2S.class)
public interface ModPacketsC2SInvoker {
    @Invoker("chooseRandomOrigin")
    public static void invokeChooseRandomOrigin(MinecraftServer minecraftServer, ServerPlayerEntity playerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        throw new AssertionError();
    }
}
