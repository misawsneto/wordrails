ConnectivityManagerImpl(package) ::=

<<package $package$;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityManagerImpl implements IConnectivityManager {
	private Context context;
	
	public ConnectivityManagerImpl(Context context) {
		this.context = context;
	}

	@Override
	public boolean isConnectedOrConnecting() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isConnectedOrConnecting();
		} else {
			return false;
		}
	}
}>>