package co.edu.unicauca.usuario_service.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${app.exchange}")
    private String exchange;

    public static final String QUEUE_ZONA = "zona.queue";

    @Bean
    public TopicExchange zonaExchange() {
        return new TopicExchange(exchange, true, false);
    }

    @Bean
    public Queue zonaQueue() {
        return QueueBuilder.durable(QUEUE_ZONA).build();
    }

    @Bean
    public Binding binding(Queue zonaQueue, TopicExchange zonaExchange) {
        return BindingBuilder.bind(zonaQueue).to(zonaExchange).with("zona.#");
    }
}
