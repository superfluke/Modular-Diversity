package modulardiversity.tile;

import javax.annotation.Nullable;

import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import modulardiversity.components.MachineComponents;
import modulardiversity.components.requirements.RequirementHotAir;
import modulardiversity.components.requirements.RequirementHotAir.ResourceToken;
import modulardiversity.tile.base.TileEntityHotAir;
import modulardiversity.util.ICraftingResourceHolder;

public class TileHotAirOutput extends TileEntityHotAir {
    public TileHotAirOutput() {
        super();
    }
    
    @Override
	public boolean consume(ResourceToken token, boolean doConsume) {
		return false;
	}
    
    @Nullable
    @Override
    public MachineComponent provideComponent() {
        return new MachineComponents.HotAirHatch(MachineComponent.IOType.OUTPUT) {
            @Override
            public ICraftingResourceHolder<RequirementHotAir.ResourceToken> getContainerProvider() {
                return TileHotAirOutput.this;
            }
        };
    }

	@Override
	public int getOutAirTemperature() {
		return 0;
	}

}
