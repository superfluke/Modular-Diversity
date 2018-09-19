package modulardiversity.tile;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import hellfirepvp.astralsorcery.common.auxiliary.link.ILinkableTile;
import hellfirepvp.astralsorcery.common.item.crystal.CrystalProperties;
import hellfirepvp.astralsorcery.common.lib.Constellations;
import hellfirepvp.astralsorcery.common.starlight.IIndependentStarlightSource;
import hellfirepvp.astralsorcery.common.block.network.BlockCollectorCrystalBase;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.starlight.IStarlightSource;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightUpdateHandler;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionSource;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.SourceClassRegistry;
import hellfirepvp.astralsorcery.common.starlight.transmission.TransmissionNetworkHelper;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.SimpleTransmissionSourceNode;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.crystal.IndependentCrystalSource;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.SourceClassRegistry.SourceProvider;
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
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class TileStarlightOutput extends TileEntityStarlight implements ITickable, IStarlightSource, ILinkableTile {
	//TODO shift right click to unlink
	private boolean isFirstTick;
	private List<BlockPos> positions = new LinkedList<>();
	private boolean needsUpdate = false;
	private boolean linked;
	private int outputTimer;
	
	public TileStarlightOutput() {
		super();
		isFirstTick = true;
		linked = false;
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
		{
			outputTimer = token.getOutputTime();
			setStarlight(token.getRequiredStarlight());
			StarlightProducer.setStarlightPower((float) getStarlight());
		}
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
			needsUpdate = true;
			isFirstTick = false;
		}
		
		if(outputTimer == 0 && getStarlight() > 0.0)
		{
			setStarlight(0.0);
			StarlightProducer.setStarlightPower(0.0F);
		}
		
		if(outputTimer > 0)
			outputTimer--;
		
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
		needsUpdate = false;
	}

	@Override
	public boolean needToUpdateStarlightSource() {
		return needsUpdate;
	}

	@Override
	public IIndependentStarlightSource provideNewSourceNode() {
		return new StarlightProducer((float)getStarlight(), linked);
	}

	@Override
	public ITransmissionSource provideSourceNode(BlockPos pos) {
		return new SimpleTransmissionSourceNode(pos);
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
		return positions;
	}

	@Override
	public String getUnLocalizedDisplayName() {
		return "tile.blockstarlightoutputhatch.name";
	}
	
	 public boolean hasBeenLinked() {
        return linked;
    }

	@Override
	public void onLinkCreate(EntityPlayer player, BlockPos endpoint) {
		if(endpoint.equals(getPos())) return;

        if(TransmissionNetworkHelper.createTransmissionLink(this, endpoint)) {
            if(!positions.contains(endpoint)) {
                positions.add(endpoint);
                markDirty();
            }

            if(!linked) {
                linked = true;
                needsUpdate = true;
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
            positions.remove(endpoint);
            markDirty();
            return true;
        }
        return false;
	}
	
	@Override
    public boolean onSelect(EntityPlayer player) {
        if(player.isSneaking()) {
            for (BlockPos linkTo : Lists.newArrayList(getLinkedPositions())) {
                tryUnlink(player, linkTo);
            }
            player.sendMessage(new TextComponentTranslation("misc.link.unlink.all").setStyle(new Style().setColor(TextFormatting.GREEN)));
            return false;
        }
        return true;
    }
	
	@Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        NBTTagList list = new NBTTagList();
        for (BlockPos pos : positions) {
            NBTTagCompound tag = new NBTTagCompound();
            NBTHelper.writeBlockPosToNBT(pos, tag);
            
            list.appendTag(tag);
        }
        compound.setTag("linkedto", list);
        compound.setBoolean("previouslylinked", linked);
        compound.setInteger("outputtimer", outputTimer);
    }
	
	@Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);
        
        positions.clear();
        if(compound.hasKey("linkedto")) {
            NBTTagList list = compound.getTagList("linkedto", 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                positions.add(NBTHelper.readBlockPosFromNBT(tag));
            }
        }
        System.out.println(Arrays.toString(positions.toArray()));
        linked = compound.getBoolean("previouslylinked");
        outputTimer = compound.getInteger("outputtimer");
        needsUpdate = true;
    }
	
	public static class StarlightProducer implements IIndependentStarlightSource {
		private static float starlightPower;
		private static boolean autoLink;
		
		public StarlightProducer(float power, boolean autoLink) {
			starlightPower = power;
			this.autoLink = autoLink;
		}
		
		public static void setStarlightPower(float power) {
			starlightPower = power;
		}
		
		@Override
		public SourceClassRegistry.SourceProvider getProvider() {
			return new Provider();
		}

		@Override
		public IWeakConstellation getStarlightType() {
			// TODO I think this might be okay but it feels wrong
			return Constellations.lucerna;
		}

		@Override
		public void informTileStateChange(IStarlightSource sauce) {
			TileStarlightOutput te = MiscUtils.getTileAt(sauce.getTrWorld(), sauce.getTrPos(), TileStarlightOutput.class, true);
			if(te != null)
				autoLink = te.hasBeenLinked();
		}

		@Override
		public float produceStarlightTick(World world, BlockPos pos) {
			return starlightPower;
		}
		
		@Override
		public void threadedUpdateProximity(BlockPos arg0, Map<BlockPos, IIndependentStarlightSource> arg1) {	
		}

		@Override
		public void readFromNBT(NBTTagCompound compound) {
			starlightPower = compound.getFloat("power");
			autoLink = compound.getBoolean("autoLink");
		}	

		@Override
		public void writeToNBT(NBTTagCompound compound) {
			compound.setFloat("power", starlightPower);
			compound.setBoolean("autolink", autoLink);
		}
		
		@Override
	    public boolean providesAutoLink() {
	        return autoLink;
	    }

	}
	
	public static class Provider implements SourceClassRegistry.SourceProvider {

        @Override
        public IIndependentStarlightSource provideEmptySource() {
            return new StarlightProducer(0.0F, false);
        }

        @Override
        public String getIdentifier() {
            return ModularDiversity.MODID + ":TileStarlightOutput";
        }

    }

}
