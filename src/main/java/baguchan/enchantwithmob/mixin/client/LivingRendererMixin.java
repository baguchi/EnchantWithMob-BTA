package baguchan.enchantwithmob.mixin.client;

import baguchan.enchantwithmob.utils.IEnchantCap;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingRenderer;
import net.minecraft.client.render.model.ModelBase;
import net.minecraft.core.entity.EntityLiving;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingRenderer.class, remap = false)
public abstract class LivingRendererMixin<T extends EntityLiving> extends EntityRenderer<T> {

    @Shadow
    protected ModelBase mainModel;

    @Inject(method = "doRenderLiving", at = @At("TAIL"))
    public void doRenderLiving(T entity, double x, double y, double z, float yaw, float renderPartialTicks, CallbackInfo ci) {
        if (entity instanceof IEnchantCap) {
            if (((IEnchantCap) entity).getEnchantCap().hasEnchant()) {
                GL11.glPushMatrix();
                GL11.glDisable(2884);

                try {
                    float headYawOffset = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * renderPartialTicks;
                    float headYaw = entity.yRotO + (entity.yRot - entity.yRotO) * renderPartialTicks;
                    float headPitch = entity.xRotO + (entity.xRot - entity.xRotO) * renderPartialTicks;
                    this.translateModel(entity, x, y, z);
                    float ticksExisted = this.ticksExisted(entity, renderPartialTicks);
                    this.rotateModel(entity, ticksExisted, headYawOffset, renderPartialTicks);
                    float scale = 0.0625F;
                    GL11.glEnable(32826);
                    GL11.glScalef(-1.0F, -1.0F, 1.0F);
                    this.preRenderCallback(entity, renderPartialTicks);
                    GL11.glTranslatef(0.0F, -24.0F * scale - 0.0078125F, 0.0F);
                    float limbYaw = entity.prevLimbYaw + (entity.limbYaw - entity.prevLimbYaw) * renderPartialTicks;
                    float limbSwing = entity.limbSwing - entity.limbYaw * (1.0F - renderPartialTicks);
                    if (limbYaw > 1.0F) {
                        limbYaw = 1.0F;
                    }
                    GL11.glEnable(3042);
                    GL11.glDisable(3008);
                    GL11.glBlendFunc(770, 771);
                    this.loadTexture("/assets/enchantwithmob/mobs/enchant_glint.png");

                    this.mainModel.setLivingAnimations(entity, limbSwing, limbYaw, renderPartialTicks);
                    this.mainModel.render(limbSwing, limbYaw, ticksExisted, headYaw - headYawOffset, headPitch, scale);
                    GL11.glDisable(3042);
                    GL11.glEnable(3008);
                    GL11.glDisable(32826);
                } catch (Exception var24) {
                    var24.printStackTrace();
                }
                GL11.glEnable(2884);
                GL11.glPopMatrix();
            }
        }
    }

    @Shadow
    protected abstract float ticksExisted(T entity, float renderPartialTicks);

    @Shadow
    protected abstract void rotateModel(T entity, float ticksExisted, float headYawOffset, float renderPartialTicks);

    @Shadow
    protected abstract void preRenderCallback(T entity, float renderPartialTicks);

    @Shadow
    protected abstract void translateModel(T entity, double x, double y, double z);
}
