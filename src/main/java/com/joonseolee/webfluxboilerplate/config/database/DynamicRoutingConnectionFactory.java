package com.joonseolee.webfluxboilerplate.config.database;

import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import reactor.core.publisher.Mono;

public class DynamicRoutingConnectionFactory extends AbstractRoutingConnectionFactory {

    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        return Mono.deferContextual(Mono::just)
                .map(it -> {
                    if (it.size() == 1)
                        return ReplicationType.SLAVE;
                    return ReplicationType.MASTER;
                });
    }
}
