package erco.myfirstandroidgame;

import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import 	android.content.Context;

/**
 * Created by ercoa on 27-11-2016.
 */
public class Util {
    public static String getMyIp(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        @SuppressWarnings("deprecation")
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }
}
