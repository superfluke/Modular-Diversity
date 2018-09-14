package modulardiversity.tile.base;

import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionReceiver;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import hellfirepvp.modularmachinery.common.tiles.base.TileColorableMachineComponent;
import modulardiversity.components.requirements.RequirementStarlight;
import modulardiversity.util.ICraftingResourceHolder;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntityStarlight extends TileColorableMachineComponent implements MachineComponentTile, ICraftingResourceHolder<RequirementStarlight.ResourceToken> {
	private double starlight;
	
	public TileEntityStarlight() {
	}
	
	public void setStarlight(double starlight) {
		this.starlight = starlight;
	}
	
	public double getStarlight() {
		return starlight;
	}
	
	public void addStarlight(double starlight) {
		this.starlight += starlight;
	}
	
	@Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);
        this.starlight = compound.getDouble("starlight");
    }
	
	@Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);
        compound.setDouble("starlight", starlight);
    }
	

}
