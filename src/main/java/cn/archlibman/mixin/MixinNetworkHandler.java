package cn.archlibman.mixin;

import cn.archlibman.event.EventBus;
import cn.archlibman.event.events.ChatEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinNetworkHandler {
    @Inject(method = "sendChatMessage",at = @At("HEAD"), cancellable = true)
    public void sendMessage(String content, CallbackInfo ci) {
        ChatEvent chatEvent = new ChatEvent(content);
        EventBus.INSTANCE.post(chatEvent);
        if(chatEvent.getCancelled()) ci.cancel();
    }
}
