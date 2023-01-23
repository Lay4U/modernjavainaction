package com.example.modernjavainaction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.endpoint.MethodInvokingMessageSource;

import java.util.concurrent.atomic.AtomicInteger;

/* 스프링 통합 */
@Configuration
@EnableIntegration
public class MyConfiguration {

    @Bean
    public MessageSource<?> integerMessageSource() {
        MethodInvokingMessageSource source = new MethodInvokingMessageSource();
        source.setObject(new AtomicInteger());
        source.setMethodName("getAndIncrement");
        return source;
    }

    @Bean
    public DirectChannel inputChannel(){
        return new DirectChannel();
    }
    @Bean
    public IntegrationFlow myFlow() {
//        return IntegrationFlows
//                .from(this.integerMessageSource(),
//                        c -> c.poller(p -> p.fixedRate(10)))
//                .channel(this.inputChannel())
//                .transform(Object::toString)
//                .channel(MessageChannels.queue("queueCHannel"))
//                .get();

        return flow -> flow.filter((Integer p) -> p % 2 == 0)
                .transform(Object::toString)
                .handle(System.out::print);
    }
}
