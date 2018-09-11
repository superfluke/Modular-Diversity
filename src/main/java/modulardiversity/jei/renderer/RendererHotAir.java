package modulardiversity.jei.renderer;

import java.awt.Color;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredientRenderer;
import modulardiversity.jei.JEIHelpers;
import modulardiversity.jei.ingredients.HotAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.math.MathHelper;

public class RendererHotAir implements IIngredientRenderer<HotAir> {
    private IDrawableStatic mana_bar;
    private IDrawableStatic mana_bar_fill;

    public RendererHotAir() {
    }

    private void registerDrawables() {
        if (mana_bar == null)
            mana_bar = JEIHelpers.GUI_HELPER.createDrawable(JEIHelpers.TEXTURE, 28, 0, 5, 63);
        if (mana_bar_fill == null)
            mana_bar_fill = JEIHelpers.GUI_HELPER.createDrawable(JEIHelpers.TEXTURE, 33, 0, 3, 61);
    }

    @Override
    public List<String> getTooltip(Minecraft minecraft, HotAir ingredient, ITooltipFlag tooltipFlag) {
        return Lists.newArrayList("HotAir");
    }

    @Override
    public void render(Minecraft minecraft, int xPosition, int yPosition, @Nullable HotAir hotair) {
        registerDrawables();

        GlStateManager.enableAlpha();
        Color color = new Color(Color.HSBtoRGB(0.55F, (float) Math.min(1F, Math.sin(System.currentTimeMillis() / 200D) * 0.5 + 1F), 1F));
        int airTemp = (int) (hotair != null ? hotair.getTempRequired() : 0);
        renderManaBar(minecraft,xPosition,yPosition,color.getRGB(), 1.0f, airTemp, 10000);
        GlStateManager.disableAlpha();
    }

    public void renderManaBar(Minecraft minecraft, int x, int y, int color, float alpha, int airTemp, int maxTemp) {
        GlStateManager.color(1F, 1F, 1F, alpha);
        mana_bar.draw(minecraft,x,y);
        int mapTemp = 225; //TODO 
        int tempPercent = Math.max(0, (int) ((double) airTemp / (double) maxTemp * 61));

        if(tempPercent == 0 && airTemp > 0)
        	tempPercent = 1;

        mana_bar_fill.draw(minecraft,x+1,y+1);

        Color color_ = new Color(color);
        GL11.glColor4ub((byte) color_.getRed(), (byte) color_.getGreen(),(byte) color_.getBlue(), (byte) (255F * alpha));
        mana_bar_fill.draw(minecraft,x+1,y+1, MathHelper.clamp(61 - tempPercent, 0, 61),0,0,0);
        GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) 255);
    }
}
