package se.luppii.ladders.block;

import static net.minecraftforge.common.util.ForgeDirection.*;

import java.util.Random;

import se.luppii.ladders.lib.References;
import se.luppii.ladders.tile.TileEntityRopeLadder;
import se.luppii.ladders.tile.TileEntitySturdyLadder;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSturdyLadder extends Block implements ITileEntityProvider {
	
	private IIcon blockIIcon;
	public static int renderID;
	
	public BlockSturdyLadder() {
		
		super(Material.circuits);
		this.setHardness(0.4F);
		this.setStepSound(soundTypeLadder);
		this.setBlockName("lladders.block.sturdyladder");
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {		
		this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
		return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
	}
	
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {	
		this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
		return super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
	}
	
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {	
		this.updateLadderBounds(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
	}
	
    public void updateLadderBounds(int par1) {
		
		int direction = par1 & 3;
		float f = 0.185F;
		
		if (direction == 0) {
			
			this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		}
		if (direction == 1) {
			
			this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
		}
		if (direction == 2) {
			
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
		}
		if (direction == 3) {
			
			this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}
	
    @Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {	
		return false;
	}
	
	@Override
	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		return false;
	}
	
	public static void setRenderID(int id) {		
		renderID = id;
	}
	
	public int getRenderType() {		
		return renderID;
	}
	
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		
		return par1World.isSideSolid(par2, par3 - 1, par4, UP) ||
				par1World.isSideSolid(par2 - 1, par3, par4, EAST ) ||
				par1World.isSideSolid(par2 + 1, par3, par4, WEST ) ||
				par1World.isSideSolid(par2, par3, par4 - 1, SOUTH) ||
				par1World.isSideSolid(par2, par3, par4 + 1, NORTH) ||
				par1World.getBlock(par2, par3 - 1, par4) == this ||
				par1World.getBlock(par2, par3 + 1, par4) == this;
	}
	
	
	@Override
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
		
		int j1 = par9 & 3;
		
		if (par1World.getBlock(par2, par3 + 1, par4) == this) {
			return par1World.getBlockMetadata(par2, par3 + 1, par4) & 3;
		}
		if (par1World.getBlock(par2, par3 - 1, par4) == this) {
			return par1World.getBlockMetadata(par2, par3 - 1, par4) & 3;
		}
		
		if (par5 == 2) {
			j1 = 0;
		}
		if (par5 == 3) {
			j1 = 2;
		}
		if (par5 == 4) {
			j1 = 3;
		}
		if (par5 == 5) {
			j1 = 1;
		}
		
		return j1;
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {

		if (par5EntityLivingBase == null) {
			return;
		}
		
		int direction = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		
		// Use the same direction as ladders above or below.
		if (par1World.getBlock(par2, par3 + 1, par4) == this) {
			direction = par1World.getBlockMetadata(par2, par3 + 1, par4) & 3;
		}
		if (par1World.getBlock(par2, par3 - 1, par4) == this) {
			direction = par1World.getBlockMetadata(par2, par3 - 1, par4) & 3;
		}
		
		par1World.setBlockMetadataWithNotify(par2, par3, par4, direction, 2);
	}
	
	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5Block) {
		
		int metadata = par1World.getBlockMetadata(par2, par3, par4) & 3;
		boolean flag = false;
		
		if (par1World.isSideSolid(par2, par3 - 1, par4, UP) || par1World.getBlock(par2, par3 - 1, par4) == this) {
			
			flag = true;
		}
		
		// Placing ladders on the opposite of another, effectively making "flying" ladders - isn't allowed.
		if (metadata != 2 && par1World.isSideSolid(par2, par3, par4 + 1, NORTH)) {
			
			flag = true;
		}
		if (metadata != 3 && par1World.isSideSolid(par2 - 1, par3, par4, EAST)) {
			
			flag = true;
		}
		if (metadata != 0 && par1World.isSideSolid(par2, par3, par4 - 1, SOUTH)) {
			
			flag = true;
		}
		if (metadata != 1 && par1World.isSideSolid(par2 + 1, par3, par4, WEST)) {
			
			flag = true;
		}
		
		if (!flag) {
			
			this.dropBlockAsItem(par1World, par2, par3, par4, metadata, 0);
			par1World.setBlockToAir(par2, par3, par4);
		}
		super.onNeighborBlockChange(par1World, par2, par3, par4, par5Block);
	}
	
	public int quantityDropped(Random par1Random) {
		return 1;
	}
	
	@Override
	public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity) {
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IIconRegister) {		
		blockIIcon = par1IIconRegister.registerIcon("lladders:" + getUnlocalizedName());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2) {		
		return blockIIcon;
	}
	
	public TileEntity getBlockEntity(int par1) {
		return new TileEntitySturdyLadder();
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		
		try {
			
			TileEntitySturdyLadder te = TileEntitySturdyLadder.class.newInstance();
			return te;
		}
		catch (IllegalAccessException ex) {
			
			FMLLog.severe(References.MOD_NAME, "Unable to create TileEntitySturdyLadder instance.");
			return null;
		}
		catch (InstantiationException ex) {
			
			FMLLog.severe(References.MOD_NAME, "Unable to create TileEntitySturdyLadder instance.");
			return null;
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		
		return new TileEntityRopeLadder();
	}
}