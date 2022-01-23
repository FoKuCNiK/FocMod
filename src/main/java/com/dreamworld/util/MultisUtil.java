package com.dreamworld.util;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;

public class MultisUtil {
	
	public static Vector3ic rotateOffsetVector(Vector3ic forgeDirection, int x, int y, int z) {
		final Vector3i offset = new Vector3i();
		
		// either direction on z-axis
		if (forgeDirection.x() == 0 && forgeDirection.z() == -1) {
			offset.x = x;
			offset.y = y;
			offset.z = z;
		}
		if (forgeDirection.x() == 0 && forgeDirection.z() == 1) {
			offset.x = -x;
			offset.y = y;
			offset.z = -z;
		}
		// either direction on x-axis
		if (forgeDirection.x() == -1 && forgeDirection.z() == 0) {
			offset.x = z;
			offset.y = y;
			offset.z = -x;
		}
		if (forgeDirection.x() == 1 && forgeDirection.z() == 0) {
			offset.x = -z;
			offset.y = y;
			offset.z = x;
		}
		// either direction on y-axis
		if (forgeDirection.y() == -1) {
			offset.x = x;
			offset.y = z;
			offset.z = y;
		}
		
		return offset;
	}
	
	public static void setBlock(IGregTechTileEntity te, Vector3ic vec, Block block, int meta) {
		te.getWorld().setBlock(vec.x() + te.getXCoord(), vec.y() + te.getYCoord(), vec.z() + te.getZCoord(), block, meta, 3);
	}
	
	public static boolean getBlock(IGregTechTileEntity te, Vector3ic vec, Block block, int meta) {
		return te.getBlockOffset(vec.x(), vec.y(), vec.z()) == block && te.getMetaIDOffset(vec.x(), vec.y(), vec.z()) == meta;
	}
}
