package me.cirex.waternether.dimension;

import me.cirex.waternether.WaterNetherMod;
import net.minecraft.block.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.NetherDimension;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.NetherGenSettings;

public class WaterNetherDimension extends NetherDimension {
	

	public WaterNetherDimension(World worldIn, DimensionType typeIn) {
		super(worldIn, typeIn);
	}

	@Override
	public ChunkGenerator<?> createChunkGenerator() {
		NetherGenSettings nethergensettings = ChunkGeneratorType.CAVES.createSettings();
		nethergensettings.setDefaultBlock(Blocks.NETHERRACK.getDefaultState());
		nethergensettings.setDefaultFluid(Blocks.WATER.getDefaultState());
		return ChunkGeneratorType.CAVES.create(this.world,
				BiomeProviderType.FIXED.create(
						BiomeProviderType.FIXED.func_226840_a_(this.world.getWorldInfo()).setBiome(WaterNetherMod.NETHER)),
				nethergensettings);
	}

	

}
