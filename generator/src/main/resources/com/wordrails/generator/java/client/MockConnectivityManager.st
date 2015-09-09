MockConnectivityManager(package) ::=

<<package $package$;

public class MockConnectivityManager implements IConnectivityManager {
	private boolean connectedOrConnecting;
	
	public MockConnectivityManager(boolean connectedOrConnecting) {
		this.connectedOrConnecting = connectedOrConnecting;
	}

	@Override
	public boolean isConnectedOrConnecting() {
		return connectedOrConnecting;
	}
}>>