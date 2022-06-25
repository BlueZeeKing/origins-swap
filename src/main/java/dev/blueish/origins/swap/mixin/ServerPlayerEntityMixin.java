package dev.blueish.origins.swap.mixin;

import dev.blueish.origins.swap.OriginsSwap;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.networking.ModPackets;
import io.github.apace100.origins.networking.ModPacketsC2S;
import io.github.apace100.origins.origin.*;
import io.github.apace100.origins.registry.ModComponents;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
	@Inject(at = @At("HEAD"), method = "onDeath")
	public void onDeath(DamageSource damageSource, CallbackInfo ci) {
		OriginsSwap.LOGGER.info("Player dies");
		ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;

		OriginComponent component = ModComponents.ORIGIN.get(player);
		ArrayList<OriginLayer> layers = new ArrayList<>();
		OriginLayers.getLayers().forEach(layer -> {
			if(layer.isEnabled()) {
				component.setOrigin(layer, Origin.EMPTY);
				layers.add(layer);
			}
		});
		component.checkAutoChoosingLayers(player, false);
		component.sync();
		Collections.sort(layers);

		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeString(layers.get(0).getIdentifier().toString());

		ModPacketsC2SInvoker.invokeChooseRandomOrigin(player.getServer(), player, player.networkHandler, buf, null);
	}
}
