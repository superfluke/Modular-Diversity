package modulardiversity.components;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.machine.MachineComponent.IOType;
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
        if(requirement.has("level") && requirement.get("level").isJsonPrimitive() && requirement.get("level").getAsJsonPrimitive().isNumber()) {
            double requiredStarlight = requirement.getAsJsonPrimitive("level").getAsDouble();
            int outputTime = 0; //TODO fix me bro
            //if(ioType == IOType.OUTPUT && requirement.has("output_time") && requirement.get("output_time").isJsonPrimitive() && requirement.get("output_time").getAsJsonPrimitive().isNumber())
            	outputTime = requirement.getAsJsonPrimitive("output_time").getAsInt();
            //else
            	//throw new JsonParseException("The ComponentType \'"+getRegistryName()+"\' expects a \'output_time\'-entry that defines the number of ticks to output starlight!");
            
            return new RequirementStarlight(ioType, requiredStarlight, outputTime);
        } else {
            throw new JsonParseException("The ComponentType \'"+getRegistryName()+"\' expects a \'level\'-entry that defines the required starlight!");
        }
    }
}
