package modulardiversity.components;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import modulardiversity.components.requirements.RequirementEmber;
import modulardiversity.components.requirements.RequirementMana;


public class ComponentMana  extends ComponentType<RequirementMana> {
    @Nonnull
    @Override
    public String getRegistryName() {
        return "mana";
    }

    @Nullable
    @Override
    public String requiresModid() {
        return null;
    }

    @Nonnull
    @Override
    public RequirementMana provideComponent(MachineComponent.IOType ioType, JsonObject requirement) {
    	if(requirement.has("mana") && requirement.get("mana").isJsonPrimitive() && requirement.get("mana").getAsJsonPrimitive().isNumber()) {
            float manaRequired = requirement.getAsJsonPrimitive("mana").getAsFloat();
            return new RequirementMana(ioType, manaRequired);
        } else {
            throw new JsonParseException("The ComponentType \'"+getRegistryName()+"\' expects a \'mana\'-entry that defines the required mana!");
        }
    }
}
