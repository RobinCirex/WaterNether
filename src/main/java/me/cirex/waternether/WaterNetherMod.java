package me.cirex.waternether;

import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.cirex.waternether.dimension.WaterNetherBiome;
import me.cirex.waternether.dimension.WaterNetherDimension;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.FuzzedBiomeMagnifier;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.IChunkGeneratorFactory;
import net.minecraft.world.gen.NetherChunkGenerator;
import net.minecraft.world.gen.NetherGenSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("waternether")
public class WaterNetherMod {
	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();

	private static DimensionType type = new DimensionType(3, "_waternether", "DIM-3", WaterNetherDimension::new, false,
			FuzzedBiomeMagnifier.INSTANCE, null, null);
	public static final DimensionType THE_WATER_NETHER = Registry.register(Registry.DIMENSION_TYPE, type.getId(),
			"the_water_nether", type);
	public static final ChunkGeneratorType<NetherGenSettings, NetherChunkGenerator> WATER_CAVES = register(
			"water_caves", NetherChunkGenerator::new, NetherGenSettings::new, true);
	public static final Biome NETHER = register(69, "water_nether", new WaterNetherBiome());

	@SuppressWarnings("deprecation")
	private static <C extends GenerationSettings, T extends ChunkGenerator<C>> ChunkGeneratorType<C, T> register(
			String key, IChunkGeneratorFactory<C, T> factoryIn, Supplier<C> settingsIn, boolean canUseForBuffet) {
		return Registry.register(Registry.CHUNK_GENERATOR_TYPE, key,
				new ChunkGeneratorType<>(factoryIn, canUseForBuffet, settingsIn));
	}

	private static Biome register(int id, String key, Biome p_222369_2_) {

		Registry.register(Registry.BIOME, id, key, p_222369_2_);
		if (p_222369_2_.isMutation()) {
			Biome.MUTATION_TO_BASE_ID_MAP.put(p_222369_2_,
					Registry.BIOME.getId(Registry.BIOME.getOrDefault(new ResourceLocation(p_222369_2_.getParent()))));
		}

		return p_222369_2_;
	}

	public WaterNetherMod() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onDimensionChange);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onDimensionChange(PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof PlayerEntity) {
			if(event.getFrom() == DimensionType.OVERWORLD) {
				if(event.getTo() == DimensionType.THE_NETHER) {
					event.getPlayer().changeDimension(THE_WATER_NETHER);
				}
			}
			if(event.getFrom() == THE_WATER_NETHER) {
				if(event.getTo() == DimensionType.THE_NETHER) {
					event.getPlayer().changeDimension(DimensionType.OVERWORLD);
				}
			}
		}
	}

	private void setup(final FMLCommonSetupEvent event) {

	}

	private void doClientStuff(final FMLClientSetupEvent event) {
	}

	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
		}
	}

}
