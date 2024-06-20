package snw.hatplacer;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class HatPlacer extends JavaPlugin {
    private static final ItemStack ITEM;
    private Location give;
    private Location clean;

    static {
        final Material material = Material.getMaterial("TZZ_YANJING");
        Preconditions.checkNotNull(material, "Material missing");
        ITEM = new ItemStack(material);
        ITEM.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        give = getConfig().getLocation("give");
        clean = getConfig().getLocation("clean");

        getServer().getScheduler()
                .runTaskTimer(this, this::tickPlayer, 10L,10L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getConfig().set("give", give);
        getConfig().set("clean", clean);
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            switch (args[0]) {
                case "give" -> {
                    give = player.getLocation().add(0, -1, 0).getBlock().getLocation();
                    player.sendMessage("OK");
                }
                case "clean" -> {
                    clean = player.getLocation().add(0, -1, 0).getBlock().getLocation();
                    player.sendMessage("OK");
                }
            }
        }
        return true;
    }

    private void tickPlayer() {
        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            final Location underLoc = onlinePlayer.getLocation().add(0, -1, 0).getBlock().getLocation();
            if (!has(onlinePlayer)) {
                final Location giveLoc = give;
                if (giveLoc != null) {
                    if (giveLoc.equals(underLoc)) {
                        give(onlinePlayer);
                    }
                }
            } else {
                final Location cleanLoc = clean;
                if (cleanLoc != null) {
                    if (cleanLoc.equals(underLoc)) {
                        cleanItem(onlinePlayer);
                    }
                }
            }
        }
    }

    private boolean has(Player player) {
        final ItemStack helmet = player.getInventory().getHelmet();
        return ITEM.isSimilar(helmet);
    }

    private void cleanItem(Player player) {
        player.getInventory().setHelmet(null);
    }

    private void give(Player player) {
        player.getInventory().setHelmet(ITEM);
    }
}
