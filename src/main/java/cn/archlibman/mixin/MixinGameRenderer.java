package cn.archlibman.mixin;

import cn.archlibman.modules.NoHurtCam;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void onTiltViewWhenHurt(CallbackInfo ci) {
        if (NoHurtCam.isActive()) {  // 使用 INSTANCE 访问单例对象（Kotlin object）
            ci.cancel();
        }
    }
}
