package modulardiversity.tile;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import hellfirepvp.astralsorcery.common.auxiliary.link.ILinkableTile;
import hellfirepvp.astralsorcery.common.starlight.IIndependentStarlightSource;
import hellfirepvp.astralsorcery.common.starlight.IStarlightSource;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightUpdateHandler;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionSource;
import hellfirepvp.astralsorcery.common.starlight.transmission.TransmissionNetworkHelper;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import modulardiversity.components.MachineComponents;
import modulardiversity.components.requirements.RequirementStarlight;
import modulardiversity.components.requirements.RequirementStarlight.ResourceToken;
import modulardiversity.tile.base.TileEntityStarlight;
import modulardiversity.util.ICraftingResourceHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileStarlightOutput extends TileEntityStarlight implements ITickable, IStarlightSource, ILinkableTile{
	private boolean isFirstTick;
	private List<BlockPos> positions = new LinkedList<>();
	private boolean needsUpdate = false;
	private boolean linked = false;
	
	public TileStarlightOutput() {
		super();
		isFirstTick = true;
	}
	
	@Nullable
    @Override
    public MachineComponent provideComponent() {
        return new MachineComponents.StarlightHatch(MachineComponent.IOType.OUTPUT) {
            @Override
            public ICraftingResourceHolder<RequirementStarlight.ResourceToken> getContainerProvider() {
                return TileStarlightOutput.this;
            }
        };
    }

	@Override
	public boolean consume(ResourceToken token, boolean doConsume) {
		return false;
	}

	@Override
	public boolean generate(ResourceToken token, boolean doGenerate) {
		token.setRequiredStarlightMet();
		if(doGenerate)
			setStarlight(token.getRequiredStarlight());
		return true;
	}
	
	private void addToStarlightNetwork() {		
		if(world.isRemote) 
			return;
		WorldNetworkHandler handler = WorldNetworkHandler.getNetworkHandler(getWorld());
		handler.addNewSourceTile(this);
		IPrismTransmissionNode node = handler.getTransmissionNode(getPos());
		if(node != null)
			StarlightUpdateHandler.getInstance().addNode(getWorld(), node);
	}
	
	public void removeFromStarlightNetwork() {
		WorldNetworkHandler handler = WorldNetworkHandler.getNetworkHandler(getWorld());
        IPrismTransmissionNode node = handler.getTransmissionNode(getPos());
        if(node != null)
        	StarlightUpdateHandler.getInstance().removeNode(getTrWorld(), node);
        handler.removeSource(this);
	}

	@Override
	public void update() {
		if(isFirstTick)
		{
			addToStarlightNetwork();
			isFirstTick = false;
		}
		
	}

	@Override
	public BlockPos getTrPos() {
		return getPos();
	}

	@Override
	public World getTrWorld() {
		return getWorld();
	}

	@Override
	public void markUpdated() {
		this.needsUpdate = false;
	}

	@Override
	public boolean needToUpdateStarlightSource() {
		return needsUpdate;
	}

	@Override
	public IIndependentStarlightSource provideNewSourceNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITransmissionSource provideSourceNode(BlockPos arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockPos getLinkPos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public World getLinkWorld() {
		return getTrWorld();
	}

	@Override
	public List<BlockPos> getLinkedPositions() {
		return positions;
	}

	@Override
	public String getUnLocalizedDisplayName() {
		return "tile.blockstarlightoutputhatch.name";
	}

	@Override
	public void onLinkCreate(EntityPlayer player, BlockPos endpoint) {
		if(endpoint.equals(getPos())) return;

        if(TransmissionNetworkHelper.createTransmissionLink(this, endpoint)) {
            if(!this.positions.contains(endpoint)) {
                this.positions.add(endpoint);
                markDirty();
            }

            if(!linked) {
                this.linked = true;
                this.needsUpdate = true;
            }
        }
	}

	@Override
	public boolean tryLink(EntityPlayer player, BlockPos endpoint) {
		return !endpoint.equals(getPos()) && TransmissionNetworkHelper.canCreateTransmissionLink(this, endpoint);
	}

	@Override
	public boolean tryUnlink(EntityPlayer player, BlockPos endpoint) {
		if(endpoint.equals(getPos())) return false;

        if(TransmissionNetworkHelper.hasTransmissionLink(this, endpoint)) {
            TransmissionNetworkHelper.removeTransmissionLink(this, endpoint);
            this.positions.remove(endpoint);
            markDirty();
            return true;
        }
        return false;
	}

}
