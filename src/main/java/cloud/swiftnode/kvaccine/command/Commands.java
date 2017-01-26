package cloud.swiftnode.kvaccine.command;

import cloud.swiftnode.kvaccine.abstraction.SpamExecutor;
import cloud.swiftnode.kvaccine.abstraction.convertor.StringToIpConverter;
import cloud.swiftnode.kvaccine.abstraction.deniable.DeniableInfoAdapter;
import cloud.swiftnode.kvaccine.abstraction.executor.BaseSpamExecutor;
import cloud.swiftnode.kvaccine.abstraction.executor.DebugSpamExecutor;
import cloud.swiftnode.kvaccine.abstraction.processor.AsyncLoginProcessor;
import cloud.swiftnode.kvaccine.abstraction.processor.SyncLoginProcessor;
import cloud.swiftnode.kvaccine.util.Config;
import cloud.swiftnode.kvaccine.util.Lang;
import cloud.swiftnode.kvaccine.util.Static;
import cloud.swiftnode.kvaccine.util.StaticStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cloud.swiftnode.kvaccine.util.Static.getConfig;

/**
 * Created by Junhyeong Lim on 2017-01-17.
 */
public class Commands implements CommandExecutor {
    private static Pattern IP_PATTERN;

    static {
        IP_PATTERN = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean isOp = sender.isOp();
        // Lazy
        switch (args.length) {
            case 1:
                if (args[0].equalsIgnoreCase("force")) {
                    if (!isOp) {
                        break;
                    }
                    StaticStorage.forceMode = !StaticStorage.forceMode;
                    sender.sendMessage(Lang.SET.builder().single(Lang.Key.VALUE, StaticStorage.forceMode).build());
                    return true;
                } else if (args[0].equalsIgnoreCase("info")) {
                     /*
                     K-SPAM 은 AGPL 라이선스이며 개발자, 플러그인 정보, 소스 제공이 의무입니다.
                     밑 메세지 전송 코드를 제거 시 법적 책임을 물을 수 있습니다.
                     본 프로젝트에 기여했을 경우 밑 메세지에 자신의 닉네임을 추가할 수 있습니다.
                     */
                    sender.sendMessage(Lang.LAW_INFO.builder().build());
                    sender.sendMessage(Lang.NEW_VERSION.builder().single(Lang.Key.NEW_VERSION, StaticStorage.getNewVer()).build());
                    sender.sendMessage(Lang.CURRENT_VERSION.builder().single(Lang.Key.KSPAM_VERSION, StaticStorage.getCurrVer()).build());
                    sender.sendMessage(Lang.PREFIX + String.valueOf(StaticStorage.cachedSet.size()));
                    sender.sendMessage(Lang.PREFIX + String.valueOf(StaticStorage.firstKickCachedSet.size()));
                    return true;
                } else if (args[0].equalsIgnoreCase("debug")) {
                    if (!isOp) {
                        break;
                    }
                    getConfig().set(Config.DEBUG_MODE, !Config.isDebugMode());
                    sender.sendMessage(Lang.SET.builder().single(Lang.Key.VALUE, Config.isDebugMode()).build());
                    return true;
                } else if (args[0].equalsIgnoreCase("firstkick")) {
                    if (!isOp) {
                        break;
                    }
                    getConfig().set(Config.FIRST_LOGIN_KICK, !Config.isFirstLoginKick());
                    sender.sendMessage(Lang.SET.builder().single(Lang.Key.VALUE, Config.isFirstLoginKick()).build());
                    return true;
                } else if (args[0].equalsIgnoreCase("alert")) {
                    if (!isOp) {
                        break;
                    }
                    getConfig().set(Config.ALERT, !Config.isAlert());
                    sender.sendMessage(Lang.SET.builder().single(Lang.Key.VALUE, Config.isAlert()).build());
                    return true;
                }
                break;
            case 2:
                if (args[0].equalsIgnoreCase("check")) {
                    if (!isOp) {
                        break;
                    }
                    String info = new InfoFacade(args[1]).get();
                    if (info == null) {
                        sender.sendMessage(Lang.INVALID_IP.builder().build());
                        return true;
                    }
                    sender.sendMessage(Lang.COMMAND_CHECK.builder().single(Lang.Key.VALUE, info).build());
                    final DeniableInfoAdapter adapter = new DeniableInfoAdapter(false, info);
                    final SpamExecutor executor = new DebugSpamExecutor(new BaseSpamExecutor(), sender);
                    new SyncLoginProcessor(executor, adapter).process();
                    Static.runTaskAsync(() -> new AsyncLoginProcessor(executor, adapter).process());
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (!isOp) {
                        break;
                    }
                    String info = new InfoFacade(args[1]).get();
                    if (info == null) {
                        sender.sendMessage(Lang.INVALID_IP.builder().build());
                        return true;
                    }
                    sender.sendMessage(Lang.PREFIX.toString() + StaticStorage.cachedSet.remove(info));
                    return true;
                }
        }
        return false;
    }

    class InfoFacade {
        private String target;

        InfoFacade(String target) {
            this.target = target;
        }

        String get() {
            Player player = Bukkit.getPlayer(target);
            if (player != null) {
                return new StringToIpConverter(player.getAddress().getAddress().toString()).convert();
            }
            Matcher matcher = IP_PATTERN.matcher(target);
            if (matcher.find()) {
                return target;
            } else {
                return null;
            }
        }
    }
}