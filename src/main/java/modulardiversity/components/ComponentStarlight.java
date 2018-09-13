package modulardiversity.components;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import modulardiversity.components.requirements.RequirementStarlight;

public class ComponentStarlight extends ComponentType<RequirementStarlight> {
	@Nonnull
    @Override
    public String getRegistryName() {
        return "starlight";
    }

    @Nullable
    @Override
    public String requiresModid() {
        return null;
    }
    
    @Nonnull
    @Override
    public RequirementStarlight provideComponent(MachineComponent.IOType ioType, JsonObject requirement) {
        if(requirement.has("starlight") && requirement.get("starlight").isJsonPrimitive() && requirement.get("starlight").getAsJsonPrimitive().isNumber()) {
            double requiredStarlight = requirement.getAsJsonPrimitive("starlight").getAsDouble();
            return new RequirementStarlight(ioType, requiredStarlight);
        } else {
            throw new JsonParseException("The ComponentType \'"+getRegistryName()+"\' expects a \'starlight\'-entry that defines the required starlight!");
        }
    }
}
