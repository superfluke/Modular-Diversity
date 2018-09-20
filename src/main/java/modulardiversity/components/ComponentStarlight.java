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
            int requiredStarlight = requirement.getAsJsonPrimitive("level").getAsInt();
            int outputTime = 0; 
            if(ioType == IOType.OUTPUT) {
            	if(requirement.has("output_time") && requirement.get("output_time").isJsonPrimitive() && requirement.get("output_time").getAsJsonPrimitive().isNumber() ) {
            		outputTime = requirement.getAsJsonPrimitive("output_time").getAsInt();
            	} 
            	else {
            		throw new JsonParseException("The ComponentType \'"+getRegistryName()+"\' expects a \'output_time\'-entry that defines the number of ticks to output starlight!");
            	}
            }            	
            
            //starlight requirements given by players are 200x greater than actual output, keeping with how the starlight altars multiply input starlight by 200x
            return new RequirementStarlight(ioType, requiredStarlight/200.0, outputTime);
        } else {
            throw new JsonParseException("The ComponentType \'"+getRegistryName()+"\' expects a \'level\'-entry that defines the required starlight!");
        }
    }
}
