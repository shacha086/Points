package com.hzzz.points.listeners;

import com.hzzz.points.data_structure.AntiBoomInfo;
import com.hzzz.points.interfaces.NamedListener;
import com.hzzz.points.text.text;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

import static com.hzzz.points.Points.config;
import static com.hzzz.points.utils.Utils.logDetailInfo;
import static org.bukkit.Material.*;

public class AntiBoomListener implements NamedListener {
    private static final AntiBoomListener INSTANCE = new AntiBoomListener();
    private static final String name = "防爆";

    private static final AntiBoomInfo[] anti_boom_info = {
            new AntiBoomInfo(EntityType.ENDER_CRYSTAL, "anti-boom.ender-crystal.%s"),
            new AntiBoomInfo(EntityType.PRIMED_TNT, "anti-boom.tnt.%s"),
            new AntiBoomInfo(EntityType.MINECART_TNT, "anti-boom.minecart-tnt.%s"),

            new AntiBoomInfo(EntityType.CREEPER, "anti-boom.creeper.%s"),
            new AntiBoomInfo(EntityType.WITHER, "anti-boom.wither.spawn.%s"),

            new AntiBoomInfo(EntityType.WITHER_SKULL, "anti-boom.wither.skull.%s"),
            new AntiBoomInfo(EntityType.FIREBALL, "anti-boom.ghast.%s"),
    };

    private static final Material[] beds = {
            WHITE_BED,
            ORANGE_BED,
            MAGENTA_BED,
            LIGHT_BLUE_BED,
            YELLOW_BED,
            LIME_BED,
            PINK_BED,
            GRAY_BED,
            LIGHT_GRAY_BED,
            CYAN_BED,
            PURPLE_BED,
            BLUE_BED,
            BROWN_BED,
            GREEN_BED,
            RED_BED,
            BLACK_BED,
    };

    public static AntiBoomListener getInstance() {
        return INSTANCE;
    }

    @Override
    public String getName() {
        return name;
    }

    private AntiBoomListener() {
    }

    @EventHandler
    private void onBoom(EntityExplodeEvent e) {  // 实体爆炸
        String world_name = e.getEntity().getWorld().getName();  // 事件发生的世界

        for (AntiBoomInfo info : anti_boom_info) {  // 遍历
            if (e.getEntity().getType().equals(info.type)  // 检查类型
                    && config.getBoolean(String.format(info.config_path, "enable"), false)) {  // anti-boom.类型.enable

                if (config.getBoolean(String.format(info.config_path, "world"), false)  // anti-boom.类型.enable.world
                        && world_name.equals(config.getString("anti-boom.world-name.world", "world"))) {
                    e.setCancelled(true);

                } else if (config.getBoolean(String.format(info.config_path, "world-nether"), false)  // anti-boom.类型.enable.world-nether
                        && world_name.equals(config.getString("anti-boom.world-name.world-nether", "world_nether"))) {
                    e.setCancelled(true);

                } else if (config.getBoolean(String.format(info.config_path, "world-the-end"), false)  // anti-boom.类型.enable.world-the-end
                        && world_name.equals(config.getString("anti-boom.world-name.world-the-end", "world_the_end"))) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    private void onBoom(BlockExplodeEvent e) {  // 方块爆炸 为了床 然而没软用 床爆炸的时候会触发，但是还是照样爆
        String world_name = e.getBlock().getWorld().getName();  // 事件发生的世界

        for (Material bed : beds) {  // 遍历
            if (e.getBlock().getType().equals(bed)  // 检查类型
                    && config.getBoolean("anti-boom.bed.enable", false)) {  // anti-boom.类型.enable

                if (config.getBoolean("anti-boom.bed.world", false)  // anti-boom.类型.enable.world
                        && world_name.equals(config.getString("anti-boom.world-name.world", "world"))) {
                    e.setCancelled(true);
                    logDetailInfo("B");

                } else if (config.getBoolean("anti-boom.bed.world-nether", false)  // anti-boom.类型.enable.world-nether
                        && world_name.equals(config.getString("anti-boom.world-name.world-nether", "world_nether"))) {
                    e.setCancelled(true);
                    logDetailInfo("B");

                } else if (config.getBoolean("anti-boom.bed.world-the-end", false)  // anti-boom.类型.enable.world-the-end
                        && world_name.equals(config.getString("anti-boom.world-name.world-the-end", "world_the_end"))) {
                    e.setCancelled(true);
                }
            }
            break;
        }
    }

    @EventHandler
    private void onWitherDestroyBlocks(EntityChangeBlockEvent e) {  // 实体破坏方块 为了凋零身体移动的破坏
        String world_name = e.getEntity().getWorld().getName();  // 事件发生的世界

        if (e.getEntity().getType().equals(EntityType.WITHER)  // 检查类型
                && config.getBoolean("anti-boom.wither.body.enable", false)) {  // anti-boom.类型.enable

            if (config.getBoolean("anti-boom.wither.body.world", false)  // anti-boom.类型.enable.world
                    && world_name.equals(config.getString("anti-boom.world-name.world", "world"))) {
                e.setCancelled(true);

            } else if (config.getBoolean("anti-boom.wither.body.world-nether", false)  // anti-boom.类型.enable.world-nether
                    && world_name.equals(config.getString("anti-boom.world-name.world-nether", "world_nether"))) {
                e.setCancelled(true);

            } else if (config.getBoolean("anti-boom.wither.body.world-the-end", false)  // anti-boom.类型.enable.world-the-end
                    && world_name.equals(config.getString("anti-boom.world-name.world-the-end", "world_the_end"))) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onBad(PlayerBedEnterEvent e) {  // 阻止睡觉 真的就不让睡，但是依然爆炸
        Player player = e.getPlayer();
        String world_name = player.getWorld().getName();

        if (config.getBoolean("anti-boom.bed.enable", false)) {  // anti-boom.bed.enable
            if (config.getBoolean("anti-boom.bed.world", false)  // 主世界睡觉
                    && world_name.equals(config.getString("anti-boom.world-name.world", "world"))) {
                e.setCancelled(true);
                player.sendMessage(text.enter_bed_canceled);
                logDetailInfo("A");

            } else if (config.getBoolean("anti-boom.bed.world-nether", false)  // 下界睡觉
                    && world_name.equals(config.getString("anti-boom.world-name.world-nether", "world_nether"))) {
                e.setCancelled(true);
                player.sendMessage(text.enter_bed_canceled);
                logDetailInfo("A");

            } else if (config.getBoolean("anti-boom.bed.world-the-end", false)  // 末地睡觉
                    && world_name.equals(config.getString("anti-boom.world-name.world-the-end", "world_the_end"))) {
                e.setCancelled(true);
                player.sendMessage(text.enter_bed_canceled);
            }
        }
    }

    @EventHandler
    private void onBad(PlayerInteractEvent e) {  // 真正阻止床爆炸的 原理是玩家右键床的时候 当右键成空气
        Player player = e.getPlayer();
        String world_name = player.getWorld().getName();

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {  // 是不是右手
            for (Material bed : beds) {  // 遍历
                if (Objects.requireNonNull(e.getClickedBlock()).getType().equals(bed)) {  // 是不是床
                    if (config.getBoolean("anti-boom.bed.enable", false)) {  // anti-boom.bed.enable
                        if (config.getBoolean("anti-boom.bed.world", false)  // 主世界睡觉
                                && world_name.equals(config.getString("anti-boom.world-name.world", "world"))) {
                            e.setCancelled(true);
                            player.sendMessage(text.enter_bed_canceled);
                            logDetailInfo("C");

                        } else if (config.getBoolean("anti-boom.bed.world-nether", false)  // 下界睡觉
                                && world_name.equals(config.getString("anti-boom.world-name.world-nether", "world_nether"))) {
                            e.setCancelled(true);
                            player.sendMessage(text.enter_bed_canceled);
                            logDetailInfo("C");

                        } else if (config.getBoolean("anti-boom.bed.world-the-end", false)  // 末地睡觉
                                && world_name.equals(config.getString("anti-boom.world-name.world-the-end", "world_the_end"))) {
                            e.setCancelled(true);
                            player.sendMessage(text.enter_bed_canceled);
                        }
                    }
                    break;
                }
            }
        }
    }
}
