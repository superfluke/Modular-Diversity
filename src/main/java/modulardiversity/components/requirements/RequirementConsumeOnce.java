package modulardiversity.components.requirements;

import com.google.common.collect.Lists;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentOutputRestrictor;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.helper.RecipeCraftingContext;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.util.ResultChance;
import modulardiversity.util.ICraftingResourceHolder;
import modulardiversity.util.IResourceToken;

import java.util.List;

public abstract class RequirementConsumeOnce<T, V extends IResourceToken> extends ComponentRequirement<T> {
    V checkToken;

    public RequirementConsumeOnce(ComponentType componentType, MachineComponent.IOType actionType) {
        super(componentType, actionType);
    }

    @Override
    public boolean startCrafting(MachineComponent component, RecipeCraftingContext context, ResultChance resultChance) {
        return canStartCrafting(component, context, Lists.newArrayList()) == CraftCheck.SUCCESS;
    }

    @Override
    public boolean finishCrafting(MachineComponent machineComponent, RecipeCraftingContext recipeCraftingContext, ResultChance resultChance) {
        return true;
    }

    @Override
    public CraftCheck canStartCrafting(MachineComponent component, RecipeCraftingContext context, List<ComponentOutputRestrictor> list) {
        if(!isCorrectHatch(component)) return CraftCheck.INVALID_SKIP;
        ICraftingResourceHolder<V> handler = (ICraftingResourceHolder<V>) context.getProvidedCraftingComponent(component);
        switch (getActionType()) {
            case INPUT:
                boolean didConsume = handler.consume(checkToken);
                if(!didConsume) {
                    return CraftCheck.FAILURE_MISSING_INPUT;
                } else if(checkToken.isEmpty()) {
                    return CraftCheck.SUCCESS;
                } else {
                    return CraftCheck.PARTIAL_SUCCESS;
                }
            case OUTPUT:
                return CraftCheck.SUCCESS;
        }
        return CraftCheck.FAILURE_MISSING_INPUT;
    }

    @Override
    public void startRequirementCheck(ResultChance chance, RecipeCraftingContext context) {
        checkToken = emitConsumptionToken();
        checkToken.setModifier(context.applyModifiers(this,getActionType(),checkToken.getModifier(),false));
    }

    @Override
    public void endRequirementCheck() {
        checkToken = emitConsumptionToken();
    }

    protected abstract boolean isCorrectHatch(MachineComponent component);

    protected abstract V emitConsumptionToken();
}
