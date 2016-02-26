package co.xarx.trix;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RunTomcat {

	public static void main(String args[]) throws ServletException, LifecycleException, IOException {
		long timeStart = new Date().getTime();

		Tomcat tomcat = new Tomcat();
		tomcat.setBaseDir("target");

		if (args.length > 0) {
			tomcat.setPort(Integer.valueOf(args[0]));
		} else {
			tomcat.setPort(8080);
		}


		Context context = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());

		System.out.println("Starting Tomcat");
		tomcat.start();

		long timeToStart = new Date().getTime() - timeStart;
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");

		System.out.println("Tomcat started in " + sdf.format(new Date(timeToStart)));
		System.out.println();
		int c = System.in.read();
		while (c != -1) {
			System.out.println("Reloading context");
			context.reload();
			System.out.println("Context reloaded");
			System.out.println();
			c = System.in.read();
		}
		System.out.println("Stopping Tomcat");
		tomcat.stop();
		System.out.println("Tomcat stopped");
		System.out.println();
	}
}