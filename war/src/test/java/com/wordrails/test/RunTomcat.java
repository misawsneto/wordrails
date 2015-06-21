package com.wordrails.test; 

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class RunTomcat {
	public static void main(String args[]) throws ServletException, LifecycleException, IOException {
		Tomcat tomcat = new Tomcat();
		tomcat.setBaseDir("target");
        
        if(args.length > 0) {
            tomcat.setPort(Integer.valueOf(args[0]));
        } else {
            tomcat.setPort(8080);
        }
        
		
		Context context = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());

		System.out.println("Starting Tomcat");
		tomcat.start();
		System.out.println("Tomcat started");
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