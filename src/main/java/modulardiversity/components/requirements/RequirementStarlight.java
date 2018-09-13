package modulardiversity.components.requirements;

import hellfirepvp.modularmachinery.common.crafting.ComponentType.Registry;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.helper.RecipeCraftingContext;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement.JEIComponent;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.machine.MachineComponent.IOType;
import modulardiversity.components.MachineComponents;
import modulardiversity.jei.JEIComponentStarlight;
import modulardiversity.jei.ingredients.Starlight;
import modulardiversity.util.IResourceToken;

public class RequirementStarlight extends RequirementConsumePerTick<Starlight, RequirementStarlight.ResourceToken> {
	public final double requiredStarlight;
	
	public RequirementStarlight(IOType ioType, double requiredStarlight) {
		super(Registry.getComponent("starlight"), ioType);
		this.requiredStarlight = requiredStarlight;
	}
	
	@Override
    public ComponentRequirement deepCopy() {
        return new RequirementStarlight(getActionType(), requiredStarlight);
    }
	
	@Override
    public JEIComponent<Starlight> provideJEIComponent() {
        return new JEIComponentStarlight(this);
    }
	
	@Override
    protected ResourceToken emitConsumptionToken(RecipeCraftingContext context) {
        return new ResourceToken(requiredStarlight);
    }
	
	@Override
    protected boolean isCorrectHatch(MachineComponent component) {
        return component.getComponentType().getRegistryName().equals("starlight") &&
                component instanceof MachineComponents.StarlightHatch &&
                component.getIOType() == getActionType();
    }
	
	public static class ResourceToken implements IResourceToken {
        private double requiredStarlight;
        private boolean requiredStarlightMet;

        public ResourceToken(double requiredStarlight) {
            this.requiredStarlight = requiredStarlight;
        }

        public double getRequiredStarlight() {
            return requiredStarlight;
        }
        
        public void setRequiredStarlightMet() {
        	requiredStarlightMet = true;
        }

        @Override
        public float getModifier() {
            return (float)requiredStarlight;
        }

        @Override
        public void setModifier(float modifier) {
        	requiredStarlight =  modifier;
        }

        @Override
        public boolean isEmpty() {
            return requiredStarlightMet;
        }
    }

}
