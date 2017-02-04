package cloud.swiftnode.ksecurity.module.kvaccine.abstraction.intercepter;

import cloud.swiftnode.ksecurity.util.Lang;
import cloud.swiftnode.ksecurity.util.StaticStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;

/**
 * Created by Junhyeong Lim on 2017-02-03.
 */
public class KPermissible extends PermissibleBase {
    private Permissible permissible = this;
    private Player player;

    public KPermissible(Permissible permissible, Player player) {
        super(player);
        this.permissible = permissible;
        this.player = player;
    }

    @Override
    public void setOp(boolean value) {
        if (player.isOp() == value)
            return;
        checkOpable();
        super.setOp(value);
    }

    @Override
    public void recalculatePermissions() {
        checkOpable();
        super.recalculatePermissions();
    }

    @Override
    public boolean isPermissionSet(String name) {
        return permissible.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return permissible.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return permissible.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return permissible.hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return permissible.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return permissible.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return permissible.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return permissible.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        permissible.removeAttachment(attachment);
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return permissible.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return permissible != null && permissible.isOp();
    }

    private void checkOpable() {
        if (StaticStorage.ALLOWED_OP_SET.size() <= 0)
            return;
        if (player != null
                && isOp()
                && !StaticStorage.ALLOWED_OP_SET.contains(player.getName())) {
            player.setOp(false);
            Bukkit.broadcastMessage(Lang.DEOP.builder()
                    .single(Lang.Key.VALUE, player.getName())
                    .build());
        }
    }
}
