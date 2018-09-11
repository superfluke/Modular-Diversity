package modulardiversity.components;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.machine.MachineComponent.IOType;
import modulardiversity.components.requirements.RequirementHotAir;


public class ComponentHotAir extends ComponentType<RequirementHotAir> {
	@Nonnull
    @Override
    public String getRegistryName() {
        return "hotair";
    }

    @Nullable
    @Override
    public String requiresModid() {
        return null;
    }
    
    @Nonnull
    @Override
    public RequirementHotAir provideComponent(MachineComponent.IOType ioType, JsonObject requirement) {
        if(requirement.has("temp") && requirement.get("temp").isJsonPrimitive() && requirement.get("temp").getAsJsonPrimitive().isNumber()) {
            int requiredTemp = requirement.getAsJsonPrimitive("temp").getAsInt();
            return new RequirementHotAir(ioType, requiredTemp);
        } else {
            throw new JsonParseException("The ComponentType \'"+getRegistryName()+"\' expects a \'temp\'-entry that defines the required hot air temp!");
        }
    }
}
