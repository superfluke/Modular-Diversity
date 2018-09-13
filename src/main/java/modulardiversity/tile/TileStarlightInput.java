package modulardiversity.tile;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import hellfirepvp.astralsorcery.common.auxiliary.link.ILinkableTile;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.starlight.IStarlightReceiver;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightUpdateHandler;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionReceiver;
import hellfirepvp.astralsorcery.common.starlight.transmission.TransmissionNetworkHelper;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.SimpleTransmissionReceiver;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionClassRegistry;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionClassRegistry.TransmissionProvider;
import hellfirepvp.astralsorcery.common.tile.TileAltar.TransmissionReceiverAltar;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import modulardiversity.ModularDiversity;
import modulardiversity.components.MachineComponents;
import modulardiversity.components.requirements.RequirementStarlight;
import modulardiversity.components.requirements.RequirementStarlight.ResourceToken;
import modulardiversity.tile.base.TileEntityStarlight;
import modulardiversity.util.ICraftingResourceHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileStarlightInput extends TileEntityStarlight implements IStarlightReceiver, ILinkableTile, ITickable{
	private Set<BlockPos> sourcesToThis = new HashSet<>();
	private boolean isFirstTick;
	public TileStarlightInput() {
		super();
		isFirstTick = true;
	}

	@Nullable
    @Override
    public MachineComponent provideComponent() {
        return new MachineComponents.StarlightHatch(MachineComponent.IOType.INPUT) {
            @Override
            public ICraftingResourceHolder<RequirementStarlight.ResourceToken> getContainerProvider() {
                return TileStarlightInput.this;
            }
        };
    }

	@Override
	public boolean consume(ResourceToken token, boolean doConsume) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean generate(ResourceToken token, boolean doGenerate) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void receiveStarlight(IWeakConstellation type, double amount) {
        setStarlight(amount * 200.0);
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
	public BlockPos getLinkPos() {
		return getTrPos();
	}

	@Override
	public World getLinkWorld() {
		return getTrWorld();
	}

	@Override
	public List<BlockPos> getLinkedPositions() {
		return new LinkedList<>();
	}

	@Override
	public String getUnLocalizedDisplayName() {
		return "tile.blockstarlightinputhatch.name";
	}

	@Override
	public void onLinkCreate(EntityPlayer player, BlockPos pos) {
	}

	@Override
	public boolean tryLink(EntityPlayer player, BlockPos pos) {
		return false;
	}

	@Override
	public boolean tryUnlink(EntityPlayer player, BlockPos pos) {
		return false;
	}

	@Override
	public ITransmissionReceiver provideEndpoint(BlockPos pos) {
		return new TransmissionReceiverInputHatch(pos);
	}
	
	@Override
	public void update() {
		if(isFirstTick) {
			 if(world.isRemote) 
				 return;
			 WorldNetworkHandler handler = WorldNetworkHandler.getNetworkHandler(getWorld());
			 handler.addTransmissionTile(this);
			 IPrismTransmissionNode node = handler.getTransmissionNode(getPos());
//			 if(node == null) {
//	            AstralSorcery.log.warn("[AstralSorcery] Placed a network tile that didn't produce a network node! At: dim=" + tileNetwork.getWorld().provider.getDimension() + ", pos=" + tileNetwork.getPos());
//	        } else if(node.needsUpdate()) {
//	            StarlightUpdateHandler.getInstance().addNode(tileNetwork.getWorld(), node);
//	        }
			StarlightUpdateHandler.getInstance().addNode(getWorld(), node);
			
			isFirstTick = false;
		}
		
		
	}
	
	//TODO on break remove from network


	public static class TransmissionReceiverInputHatch extends SimpleTransmissionReceiver {

        public TransmissionReceiverInputHatch(BlockPos thisPos) {
            super(thisPos);
        }

        @Override
        public void onStarlightReceive(World world, boolean isChunkLoaded, IWeakConstellation type, double amount) {
            if(isChunkLoaded) {
            	TileStarlightInput tw = MiscUtils.getTileAt(world, getPos(), TileStarlightInput.class, false);
                if(tw != null) {
                    tw.receiveStarlight(type, amount);
                }
            }
        }

        @Override
        public TransmissionClassRegistry.TransmissionProvider getProvider() {
            return new StarlightHatchReceiverProvider();
        }
    }
	
	public static class StarlightHatchReceiverProvider implements TransmissionClassRegistry.TransmissionProvider {

        @Override
        public TransmissionReceiverInputHatch provideEmptyNode() {
            return new TransmissionReceiverInputHatch(null);
        }

        @Override
        public String getIdentifier() {
            return ModularDiversity.MODID + ":TransmissionReceiverInputHatch";
        }

    }

	
	

}
