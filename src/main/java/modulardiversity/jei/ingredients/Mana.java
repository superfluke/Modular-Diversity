package modulardiversity.jei.ingredients;

import modulardiversity.jei.IFakeIngredient;

public class Mana implements IFakeIngredient {
    float consumedMana;

    public Mana(float consumedMana) {
        this.consumedMana = consumedMana;
    }

    public float getConsumedMana() {
        return consumedMana;
    }

    @Override
    public String getDisplayName() {
        return "Mana";
    }

    @Override
    public String getUniqueID() {
        return "mana";
    }
}
