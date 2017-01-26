package cloud.swiftnode.kvaccine.abstraction.processor;

import cloud.swiftnode.kvaccine.abstraction.SpamExecutor;
import cloud.swiftnode.kvaccine.abstraction.SpamProcessor;
import cloud.swiftnode.kvaccine.abstraction.checker.FirstKickChecker;
import cloud.swiftnode.kvaccine.abstraction.checker.LocalChecker;
import cloud.swiftnode.kvaccine.abstraction.deniable.DeniableInfoAdapter;

/**
 * Created by Junhyeong Lim on 2017-01-11.
 */
public class SyncJoinProcessor extends SpamProcessor {
    public SyncJoinProcessor(SpamExecutor executor, DeniableInfoAdapter adapter) {
        super(executor, adapter);
        setCheckerList(LocalChecker.class, FirstKickChecker.class);
    }
}
