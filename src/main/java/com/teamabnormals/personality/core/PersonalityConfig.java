package com.teamabnormals.personality.core;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import org.apache.commons.lang3.tuple.Pair;

public class PersonalityConfig {

	public static class Client {
		public final BooleanValue climbingAnimation;
		public final BooleanValue fishingHookModel;
		public final BooleanValue ghastAttackAnimation;

		public final BooleanValue deflateArmorModel;
		public final DoubleValue innerArmorDeformation;
		public final DoubleValue outerArmorDeformation;

		public final BooleanValue toggleCrawl;
		public final BooleanValue toggleSitting;

		public Client(ForgeConfigSpec.Builder builder) {
			builder.push("keybindings");
			this.toggleCrawl = builder.comment("If true, crawling will be toggled on or off instead of the keybinding being held down").define("toggleCrawl", false);
			this.toggleSitting = builder.comment("If true, sitting will be toggled on or off instead of the keybinding being held down").define("toggleSitting", false);
			builder.pop();
			builder.push("tweaks");
			this.fishingHookModel = builder.comment("If Fishing Hooks should have a special 3D model").define("Fishing Hook model", true);
			this.climbingAnimation = builder.comment("If there should be a special animation for climbing ladders").define("Climbing animation", true);
			this.ghastAttackAnimation = builder.comment("If Ghasts should have a squish animation when shooting fireballs").define("Ghast attack animation", true);
			builder.push("armor");
			this.deflateArmorModel = builder.comment("If the armor model should be deflated using the inner and outer armor deformations").define("Deflate armor model", true);
			this.innerArmorDeformation = builder.comment("The CubeDeformation for the inner (leggings) armor model - Vanilla is 0.5").defineInRange("Inner armor deformation", 0.375F, 0.0F, 2.0F);
			this.outerArmorDeformation = builder.comment("The CubeDeformation for the outer armor model - Vanilla is 1.0").defineInRange("Outer armor deformation", 0.625F, 0.0F, 2.0F);
			builder.pop();
			builder.pop();
		}
	}

	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Client CLIENT;

	static {
		Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = clientSpecPair.getRight();
		CLIENT = clientSpecPair.getLeft();
	}
}
