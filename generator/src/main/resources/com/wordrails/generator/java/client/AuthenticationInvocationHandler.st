AuthenticationInvocationHandler(package) ::=

<<package $package$;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

class AuthenticationInvocationHandler implements InvocationHandler {
	Object target;
	AuthenticationRequestInterceptor interceptor;

	@Override
	public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
		try {
			if (args != null && args.length \> 0) {
				Object arg = args[args.length - 1];
				if (arg != null && arg instanceof Callback<?\>) {
					final Callback callback  = (Callback) arg;
					args[args.length - 1] = new Callback() {
						@Override
						public void success(Object t, Response response) {
							callback.success(t, response);
						}

						@Override
						public void failure(RetrofitError error) {
							Response response = error.getResponse();
							if (response != null && response.getStatus() == 401) {
								new Thread(new Runnable() {							
									@Override
									public void run() {
										interceptor.logIn();
										try {
											method.invoke(target, args);
										} catch (IllegalAccessException exception) {
											callback.failure(RetrofitError.unexpectedError(null,  exception));
										} catch (IllegalArgumentException exception) {
											callback.failure(RetrofitError.unexpectedError(null,  exception));
										} catch (InvocationTargetException exception) {
											callback.failure(RetrofitError.unexpectedError(null,  exception));
										}
									}
								}).start();
							} else {
								callback.failure(error);
							}
						}
					};
				}
			}
			return method.invoke(target, args);
		} catch (InvocationTargetException exception) {
			Throwable targetException = exception.getTargetException();
			if (targetException == null) {
				throw exception;
			} else {
				if (targetException instanceof RetrofitError) {
					RetrofitError error = (RetrofitError) targetException;
					retrofit.client.Response response = error.getResponse();
					if (error.getResponse() == null) {
						throw targetException;
					} else {
						if (response.getStatus() == 401) {
							interceptor.logIn();
							return method.invoke(target, args);
						} else {
							throw targetException;
						}
					}
				} else {
					throw targetException;
				}
			}				
		}
	}
}>>