package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Hello world!
 *
 */

@SpringBootApplication
public class App 
{
    public static void main( String[] args )
    {
        try {
        	SpringApplication.run(App.class, args);
        	System.out.println("Application demarree !");
        }catch (Exception e) {
        	System.out.println("Application erreu \n " + e);
        }
    }
}
