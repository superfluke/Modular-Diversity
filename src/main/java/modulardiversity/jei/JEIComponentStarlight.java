package modulardiversity.jei;

import java.awt.Point;
import java.util.List;

import com.google.common.collect.Lists;

import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.integration.recipe.RecipeLayoutPart;
import mezz.jei.api.ingredients.IIngredientRenderer;
import modulardiversity.components.requirements.RequirementStarlight;
import modulardiversity.jei.JEIComponentHotAir.LayoutPart;
import modulardiversity.jei.ingredients.Starlight;
import modulardiversity.jei.renderer.RendererStarlight;
import net.minecraft.client.Minecraft;

public class JEIComponentStarlight extends ComponentRequirement.JEIComponent<Starlight> {
	private final RequirementStarlight requirement;
	
	public JEIComponentStarlight(RequirementStarlight requirement) {
        this.requirement = requirement;
    }
	
	@Override
	public Class<Starlight> getJEIRequirementClass(){
		return Starlight.class;
	}
	
	@Override
	public List<Starlight> getJEIIORequirements(){
		return Lists.newArrayList(new Starlight(requirement.requiredStarlight));
	}
	
	@Override
    public RecipeLayoutPart<Starlight> getLayoutPart(Point point) {
        return new LayoutPart(point);
    }
	
	@Override
    public void onJEIHoverTooltip(int i, boolean b, Starlight starlight, List<String> list) {
    }

    public static class LayoutPart extends RecipeLayoutPart<Starlight> {
        public LayoutPart(Point offset) {
            super(offset);
        }

        @Override
        public int getComponentWidth() {
            return 28;
        }

        @Override
        public int getComponentHeight() {
            return 34;
        }

        @Override
        public Class<Starlight> getLayoutTypeClass() {
            return Starlight.class;
        }

        @Override
        public IIngredientRenderer<Starlight> provideIngredientRenderer() {
            return new RendererStarlight();
        }

        @Override
        public int getRendererPaddingX() {
            return 0;
        }

        @Override
        public int getRendererPaddingY() {
            return 0;
        }

        @Override
        public int getMaxHorizontalCount() {
            return 1;
        }

        @Override
        public int getComponentHorizontalGap() {
            return 4;
        }

        @Override
        public int getComponentVerticalGap() {
            return 4;
        }

        @Override
        public int getComponentHorizontalSortingOrder() {
            return 900;
        }

        @Override
        public boolean canBeScaled() {
            return true;
        }

        @Override
        public void drawBackground(Minecraft minecraft) {
        }

        @Override
        public void drawForeground(Minecraft minecraft, Starlight starlight) {
        }
    }
}
