package com.aneeshgarg.jmsexample;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import java.util.Scanner;

@SpringBootApplication
@EnableJms
public class Application {

    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all boot's default to this factory, including the message converter
        configurer.configure(factory, connectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
    }

    public static void main(String[] args) {

        // Launch the application
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        try {
            boolean continueFlag = false;
            do {
                Scanner scanner = new Scanner(System.in);
                String message = "";
                System.out.println("Enter Message: ");
                message = scanner.nextLine();

                // Send a message with a POJO - the template reuse the message converter
                System.out.println("Sending an message: " + message);
                jmsTemplate.convertAndSend("messageBox", message);

                System.out.println("Do you want to continue (y/n): ");
                continueFlag = "y".equalsIgnoreCase(scanner.nextLine())? true : false;
            } while (continueFlag);
        } finally {
            context.close();
        }
    }
}