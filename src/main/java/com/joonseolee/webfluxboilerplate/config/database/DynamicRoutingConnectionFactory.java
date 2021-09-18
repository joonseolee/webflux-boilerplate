package com.joonseolee.webfluxboilerplate.config.database;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import org.springframework.transaction.reactive.TransactionSynchronizationManager;
import reactor.core.publisher.Mono;

public class DynamicRoutingConnectionFactory extends AbstractRoutingConnectionFactory {

    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        try {
            return TransactionSynchronizationManager.forCurrentTransaction().map(it -> {
                if (it.isCurrentTransactionReadOnly())
                    return ReplicationType.SLAVE;
                return ReplicationType.MASTER;
            });
        } catch (DataAccessResourceFailureException e) {
            return Mono.just(ReplicationType.SLAVE);
        }
    }
}
