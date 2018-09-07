package modulardiversity.components.requirements;

import java.util.List;

import com.google.common.collect.Lists;

import betterwithmods.api.tile.IMechanicalPower;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentOutputRestrictor;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.helper.RecipeCraftingContext;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement.CraftCheck;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement.JEIComponent;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.util.ResultChance;
import modulardiversity.components.MachineComponents;
import modulardiversity.jei.JEIComponentMana;
import modulardiversity.jei.ingredients.Mana;
import vazkii.botania.api.mana.IManaBlock;


public class RequirementMana extends ComponentRequirement<Mana>{
	public float requiredMana;
    private float requirementCheck;

    public RequirementMana(MachineComponent.IOType actionType, float requiredMana) {
        super(ComponentType.Registry.getComponent("mana"), actionType);
        this.requiredMana = requiredMana;
    }

    @Override
    public boolean startCrafting(MachineComponent machineComponent, RecipeCraftingContext recipeCraftingContext, ResultChance resultChance) {
        return canStartCrafting(machineComponent,recipeCraftingContext, Lists.newArrayList()) == CraftCheck.SUCCESS;
    }

    @Override
    public boolean finishCrafting(MachineComponent machineComponent, RecipeCraftingContext recipeCraftingContext, ResultChance resultChance) {
        //return false;
    	return true;
    }

    @Override
    public CraftCheck canStartCrafting(MachineComponent component, RecipeCraftingContext recipeCraftingContext, List<ComponentOutputRestrictor> list) {
        if(component.getComponentType().equals(this.getRequiredComponentType()) && component instanceof MachineComponents.ManaHatch && component.getIOType() == this.getActionType()) {
            CraftCheck x = getCraftCheck(component, recipeCraftingContext);
            if (x != null) return x;
        }

        return CraftCheck.INVALID_SKIP;
    }
    
    private CraftCheck getCraftCheck(MachineComponent machineComponent, RecipeCraftingContext recipeCraftingContext) {
    	IManaBlock manaPower = (IManaBlock) recipeCraftingContext.getProvidedCraftingComponent(machineComponent);
        if(manaPower == null)
            return CraftCheck.FAILURE_MISSING_INPUT;
        switch(getActionType()) {
            case INPUT:
                if(manaPower.getCurrentMana() >= recipeCraftingContext.applyModifiers(this, getActionType(), requiredMana, false))
                    return CraftCheck.SUCCESS;
                break;
            case OUTPUT:
                return CraftCheck.SUCCESS;
            default:
                return CraftCheck.FAILURE_MISSING_INPUT;
        }
        return null;
    }

    @Override
    public ComponentRequirement deepCopy() {
        return new RequirementMana(getActionType(), requiredMana);
    }

    @Override
    public void startRequirementCheck(ResultChance resultChance, RecipeCraftingContext recipeCraftingContext) {
        requirementCheck = recipeCraftingContext.applyModifiers(this, getActionType(), requiredMana, false);
    }

    @Override
    public void endRequirementCheck() {
        requirementCheck = requiredMana;
    }

    @Override
    public JEIComponent<Mana> provideJEIComponent() {
        return new JEIComponentMana(this);
    }

    public class RestrictionMana extends ComponentOutputRestrictor {
        public final float inserted;
        public final MachineComponent exactComponent;

        public RestrictionMana(float inserted, MachineComponent exactComponent) {
            this.inserted = inserted;
            this.exactComponent = exactComponent;
        }
    }
}
