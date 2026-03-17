package com.payments.pay_microservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String QUEUE = "payment.completed.queue";
    public static final String EXCHANGE = "payment.exchange";
    public static final String ROUTING_KEY = "payment.completed";

    @Bean
    public Queue paymentQueue(){
        System.out.println("****** CONFIGURANDO COLA: " + QUEUE + " ******");
        return new Queue(QUEUE,true); // true para que sea Durable
    }

    @Bean
    public TopicExchange paymentExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding paymentBinding(Queue paymentQueue, TopicExchange paymentExchange){
        return BindingBuilder
                .bind(paymentQueue)
                .to(paymentExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public ApplicationRunner initializeRabbit(RabbitAdmin rabbitAdmin) {
        return args -> {
            System.out.println(">>> ESTABLECIENDO CONEXIÓN Y CREANDO COLAS EN DOCKER...");
            // Esto obliga a Spring a declarar la Queue, el Exchange y el Binding físicamente
            rabbitAdmin.initialize();
            System.out.println(">>> ¡RABBITMQ LISTO!");
        };
    }

}
