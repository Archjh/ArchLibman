package cn.archlibman.mixin;

import cn.archlibman.event.EventBus;
import cn.archlibman.event.events.DrawEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHUDDraw {
    // 修改注入点为更稳定的位置
    @Inject(method = "render", at = @At("HEAD"))
    public void onRender(DrawContext context, float tickDelta, CallbackInfo ci) {
        EventBus.INSTANCE.post(new DrawEvent(context, tickDelta));
    }
}
