package com.example.rickmorty;
import net.minecraft.util.TypedActionResult;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.item.ItemStack;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Two-click portal prototype: use on a block to place endpoint A, then endpoint B.
 * Walking close to either endpoint transports the player to the other endpoint.
 */
public class PortalGunItem extends Item {
    private static final Map<UUID, PortalPair> PORTALS = new ConcurrentHashMap<>();
    private static boolean tickRegistered = false;
    private final float rangeMultiplier;
    private final int portalColor;

    public PortalGunItem(Settings settings, float rangeMultiplier, int portalColor) {
        super(settings);
        this.rangeMultiplier = rangeMultiplier;
        this.portalColor = portalColor;
    }

    public int getPortalColor() { return portalColor; }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient) return ActionResult.SUCCESS;
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResult.PASS;

        UUID id = player.getUuid();
        BlockPos target = context.getBlockPos().offset(context.getSide());
        PortalPair pair = PORTALS.get(id);

        if (pair == null || pair.second != null) {
            PORTALS.put(id, new PortalPair(target.toCenterPos(), null, world.getRegistryKey().getValue().toString()));
            player.sendMessage(Text.literal("§aPortal A placed. Fire again to place portal B."), true);
        } else {
            if (!pair.dimension.equals(world.getRegistryKey().getValue().toString())) {
                player.sendMessage(Text.literal("§cBoth portals must be in the same dimension."), true);
                return ActionResult.FAIL;
            }
            pair.second = target.toCenterPos();
            player.sendMessage(Text.literal("§aPortal B placed. Walk into either portal to travel!"), true);
        }
        return ActionResult.CONSUME;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            PortalPair pair = PORTALS.remove(user.getUuid());
            user.sendMessage(Text.literal(pair == null
                    ? "§eNo active portal pair."
                    : "§7Portal pair reset."), true);
        }
        return TypedActionResult.success(user.getStackInHand(hand), world.isClient);
    }

    public static void registerServerTick() {
        if (tickRegistered) return;
        tickRegistered = true;
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (PlayerEntity player : world.getPlayers()) {
                PortalPair pair = PORTALS.get(player.getUuid());
                if (pair == null || pair.second == null ||
                        !pair.dimension.equals(world.getRegistryKey().getValue().toString())) continue;
                if (player.getPos().squaredDistanceTo(pair.first) < 2.25) {
                    teleport(player, pair.second);
                } else if (player.getPos().squaredDistanceTo(pair.second) < 2.25) {
                    teleport(player, pair.first);
                }
            }
        });
    }

    private static void teleport(PlayerEntity player, Vec3d destination) {
        // Small offset prevents immediately triggering the destination portal again.
        player.teleport(destination.x, destination.y + 0.2, destination.z);
        player.setVelocity(0, 0, 0);
        player.velocityModified = true;
    }

    private static class PortalPair {
        final Vec3d first;
        Vec3d second;
        final String dimension;
        PortalPair(Vec3d first, Vec3d second, String dimension) {
            this.first = first; this.second = second; this.dimension = dimension;
        }
    }
}
