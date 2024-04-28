package com.teamabnormals.personality.core;

import com.teamabnormals.blueprint.common.world.storage.tracking.DataProcessors;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedData;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.personality.client.PersonalityClient;
import com.teamabnormals.personality.common.network.MessageC2SCrawl;
import com.teamabnormals.personality.common.network.MessageC2SSit;
import com.teamabnormals.personality.common.network.MessageS2CSyncCrawl;
import com.teamabnormals.personality.common.network.MessageS2CSyncSit;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod(Personality.MOD_ID)
public class Personality {
	public static final String MOD_ID = "personality";
	public static final String NETWORK_PROTOCOL = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MOD_ID, "net"))
			.networkProtocolVersion(() -> NETWORK_PROTOCOL)
			.clientAcceptedVersions(NETWORK_PROTOCOL::equals)
			.serverAcceptedVersions(NETWORK_PROTOCOL::equals)
			.simpleChannel();

	public static final TrackedData<Byte> CLIMBING = TrackedData.Builder.create(DataProcessors.BYTE, () -> (byte) 0).build();

	public static final Set<UUID> SITTING_PLAYERS = new HashSet<>();
	public static final Set<UUID> SYNCED_SITTING_PLAYERS = new HashSet<>();

	public Personality() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext context = ModLoadingContext.get();

		MinecraftForge.EVENT_BUS.register(this);

		this.setupMessages();

		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			bus.addListener(this::registerKeyBindings);
			bus.addListener(this::configUpdate);
		});

		context.registerConfig(ModConfig.Type.CLIENT, PersonalityConfig.CLIENT_SPEC);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(Personality.MOD_ID, "climbing"), CLIMBING);
	}

	private void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			PersonalityClient.TOGGLE_CRAWL.set(PersonalityConfig.CLIENT.toggleCrawl.get());
			PersonalityClient.TOGGLE_SIT.set(PersonalityConfig.CLIENT.toggleSitting.get());
		});
	}

	@OnlyIn(Dist.CLIENT)
	private void registerKeyBindings(RegisterKeyMappingsEvent event) {
		PersonalityClient.CRAWL.setKeyConflictContext(KeyConflictContext.IN_GAME);
		PersonalityClient.SIT.setKeyConflictContext(KeyConflictContext.IN_GAME);
		event.register(PersonalityClient.CRAWL);
		event.register(PersonalityClient.SIT);
	}

	@OnlyIn(Dist.CLIENT)
	private void configUpdate(ModConfigEvent event) {
		if (event.getConfig().getType() == Type.CLIENT) {
			updateArmorValues();
		}
	}

	private static void updateArmorValues() {
		if (PersonalityConfig.CLIENT.deflateArmorModel.get()) {
			LayerDefinitions.INNER_ARMOR_DEFORMATION = new CubeDeformation(PersonalityConfig.CLIENT.innerArmorDeformation.get().floatValue());
			LayerDefinitions.OUTER_ARMOR_DEFORMATION = new CubeDeformation(PersonalityConfig.CLIENT.outerArmorDeformation.get().floatValue());
		}
	}

	private void setupMessages() {
		int id = -1;

		CHANNEL.registerMessage(id++, MessageC2SCrawl.class, MessageC2SCrawl::serialize, MessageC2SCrawl::deserialize, MessageC2SCrawl::handle);
		CHANNEL.registerMessage(id++, MessageC2SSit.class, MessageC2SSit::serialize, MessageC2SSit::deserialize, MessageC2SSit::handle);
		CHANNEL.registerMessage(id++, MessageS2CSyncCrawl.class, MessageS2CSyncCrawl::serialize, MessageS2CSyncCrawl::deserialize, MessageS2CSyncCrawl::handle);
		CHANNEL.registerMessage(id, MessageS2CSyncSit.class, MessageS2CSyncSit::serialize, MessageS2CSyncSit::deserialize, MessageS2CSyncSit::handle);
	}
}