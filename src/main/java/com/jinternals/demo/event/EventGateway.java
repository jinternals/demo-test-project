package com.jinternals.demo.event;

import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

import javax.validation.Valid;

@Validated
public interface EventGateway {

     Mono<SenderResult<Void>> publish(@Valid Object event);

}
