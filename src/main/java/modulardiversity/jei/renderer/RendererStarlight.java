package modulardiversity.jei.renderer;

import java.awt.Color;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredientRenderer;
import modulardiversity.jei.JEIHelpers;
import modulardiversity.jei.ingredients.Starlight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.math.MathHelper;

public class RendererStarlight implements IIngredientRenderer<Starlight> {
    private IDrawableStatic starIcon;
    private IDrawableStatic starlightEmpty;
    private IDrawableStatic starlightFull;

    public RendererStarlight() {
    }

    private void registerDrawables() {
        if (starIcon == null)
        	starIcon = JEIHelpers.GUI_HELPER.createDrawable(JEIHelpers.TEXTURE, 0, 88, 28, 27);
        if (starlightEmpty == null)
        	starlightEmpty = JEIHelpers.GUI_HELPER.createDrawable(JEIHelpers.TEXTURE, 0, 116, 28, 4);
        if (starlightFull == null)
        	starlightFull = JEIHelpers.GUI_HELPER.createDrawable(JEIHelpers.TEXTURE, 0, 120, 28, 4);
    }

    @Override
    public List<String> getTooltip(Minecraft minecraft, Starlight ingredient, ITooltipFlag tooltipFlag) {
        return Lists.newArrayList((int)(ingredient.getStarlightRequired()) + " Starlight");
    }

    @Override
    public void render(Minecraft minecraft, int xPosition, int yPosition, @Nullable Starlight starlight) {
        registerDrawables();

        GlStateManager.enableAlpha();
        starIcon.draw(minecraft, xPosition, yPosition);
        Color color = new Color(Color.HSBtoRGB(0.18F, 0.18F, 0.50F));
        int starlightReq = (int) (starlight != null ? starlight.getStarlightRequired() : 0);
        //starlightReq = 220;
        int maxStarlight = 2000; //bit less the the ~2700 max starlight from a celestial crystal on a stabilizing platform
        renderStarlightBar(minecraft, xPosition, yPosition,color.getRGB(), (float)(Math.sin(System.currentTimeMillis() / 800D)+1)/8.0F + 0.75F, starlightReq, maxStarlight);
        GlStateManager.disableAlpha();
    }

    public void renderStarlightBar(Minecraft minecraft, int x, int y, int color, float alpha, int starlightReq, int maxStarlight) {
    	GlStateManager.color(1F, 1F, 1F, alpha);
    	starlightEmpty.draw(minecraft, x, y+30);
        int tempPercent = Math.max(0, (int) ((double) starlightReq / (double) maxStarlight*26));
        starlightFull.draw(minecraft, x, y+30);

        Color color_ = new Color(color);
        GL11.glColor4ub((byte) color_.getRed(), (byte) color_.getGreen(), (byte) color_.getBlue(), (byte) (255F * alpha));
        starlightFull.draw(minecraft, x, y+30, 0, 0,  MathHelper.clamp(tempPercent, 1, 30), 0);
        GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) 255);
    }

}
