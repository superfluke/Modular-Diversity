package modulardiversity.tile;

import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import modulardiversity.tile.base.TileEntityMana;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ITickable;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.common.core.handler.ManaNetworkHandler;

public class TileManaOutputHatch extends TileEntityMana implements IManaPool, ITickable {
    public TileManaOutputHatch()
    {
        super(MachineComponent.IOType.OUTPUT);
    }

    @Override
    public boolean isOutputtingPower() {
        return true;
    }

    @Override
    public EnumDyeColor getColor() {
        return EnumDyeColor.WHITE;
    }

    @Override
    public void setColor(EnumDyeColor enumDyeColor) {
        //NOOP
    }

    @Override
    public boolean isFull() {
        return true;
    }

    @Override
    public boolean canRecieveManaFromBursts() {
        return false;
    }
    
    @Override
   	public void invalidate() {
   		super.invalidate();
   		ManaNetworkEvent.removePool(this);
   	}

   	@Override
   	public void onChunkUnload() {
   		super.onChunkUnload();
   		ManaNetworkEvent.removePool(this);
   	}
   	
   	@Override
	public void update() {
		if(!ManaNetworkHandler.instance.isPoolIn(this) && !isInvalid())
			ManaNetworkEvent.addPool(this);
   	}
}
