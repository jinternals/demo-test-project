package com.jinternals.demo.event;

import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

public interface EventGateway {

     Mono<SenderResult<Void>> publish(Object event);

}
