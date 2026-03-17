package com.payments.pay_microservice.adapters.redis;

import com.payments.pay_microservice.ports.IdempotencyRepository;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Repository
public class RedisIdempotencyRepository implements IdempotencyRepository {

    private final ReactiveRedisTemplate<String, String> redis;

    public RedisIdempotencyRepository(ReactiveRedisTemplate<String, String> redis) {
        this.redis = redis;
    }

    @Override
    public Mono<Boolean> tryLock(String key) {

        return redis.opsForValue()
                .setIfAbsent(
                        "idem:lock:" + key,
                        "locked",
                        Duration.ofMinutes(1)
                );
    }

    @Override
    public Mono<Void> savePaymentId(String key, String paymentId) {

        return redis.opsForValue()
                .set(
                        "idem:result:" + key,
                        paymentId,
                        Duration.ofMinutes(2)
                )
                .then();
    }

    @Override
    public Mono<String> getPaymentId(String key) {

        return redis.opsForValue()
                .get("idem:result:" + key)
                .flatMap(value -> value == null ? Mono.empty() : Mono.just(value));
    }
}