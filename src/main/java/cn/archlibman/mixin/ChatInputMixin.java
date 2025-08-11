package cn.archlibman.mixin;

import cn.archlibman.modules.CommandManager;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatInputMixin {
    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    private void onSendMessage(String chatText, boolean addToHistory, CallbackInfoReturnable<Boolean> cir) {
        // 通过实例访问而非静态访问
        if (CommandManager.INSTANCE.processCommand(chatText)) {
            cir.setReturnValue(true);
        }
    }
}
