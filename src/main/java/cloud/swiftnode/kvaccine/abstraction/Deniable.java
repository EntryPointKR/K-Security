package cloud.swiftnode.kvaccine.abstraction;

/**
 * Created by Junhyeong Lim on 2017-01-10.
 */
public interface Deniable {
    void deny();

    String getDenyMsg();

    void setDenyMsg(String msg);
}