package modulardiversity.jei.ingredients;

import modulardiversity.jei.IFakeIngredient;

public class Starlight implements IFakeIngredient{
	double requiredStarlight;
	
	public Starlight(double requiredStarlight) {
		this.requiredStarlight = requiredStarlight;
	}
	
	public double getStarlightRequired() {
        return requiredStarlight;
    }

    @Override
    public String getDisplayName() {
        return "Starlight";
    }

    @Override
    public String getUniqueID() {
        return "starlight";
    }

}
