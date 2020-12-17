# hello-world-reactor

The objective of this repo is to reproduce the following Pool#acquire(Duration) time out error



Steps to reproduce:

- Start the application

- Hit the url http://localhost:8080/hello multiple times

This is an attempt to simulate the following scenario:

- Once the request is received , request is acknowledged
- Some processing happens in a different thread (Response of this process is not sent to the caller. It is intentional)
- Creates a flux from a list(based on the request) and calls an API for each item in the list 
- Failure happens when 3/4 requests land simulatenously.


Stack: 

2020-12-17 14:51:44.218 ERROR 37356 --- [     parallel-1] reactor.core.publisher.Operators         : Operator called default onErrorDropped

org.springframework.web.reactive.function.client.WebClientRequestException: Pool#acquire(Duration) has been pending for more than the configured timeout of 4500ms; nested exception is reactor.netty.internal.shaded.reactor.pool.PoolAcquireTimeoutException: Pool#acquire(Duration) has been pending for more than the configured timeout of 4500ms
	at org.springframework.web.reactive.function.client.ExchangeFunctions$DefaultExchangeFunction.lambda$wrapException$9(ExchangeFunctions.java:137) ~[spring-webflux-5.3.2.jar:5.3.2]
	Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException: 
Error has been observed at the following site(s):
	|_ checkpoint ⇢ Request to GET null [DefaultWebClient]
Stack trace:
		at org.springframework.web.reactive.function.client.ExchangeFunctions$DefaultExchangeFunction.lambda$wrapException$9(ExchangeFunctions.java:137) ~[spring-webflux-5.3.2.jar:5.3.2]
		at reactor.core.publisher.MonoErrorSupplied.subscribe(MonoErrorSupplied.java:70) ~[reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.Mono.subscribe(Mono.java:4046) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onError(FluxOnErrorResume.java:103) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.FluxPeek$PeekSubscriber.onError(FluxPeek.java:221) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.FluxPeek$PeekSubscriber.onError(FluxPeek.java:221) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.FluxPeek$PeekSubscriber.onError(FluxPeek.java:221) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.MonoNext$NextSubscriber.onError(MonoNext.java:93) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.MonoFlatMapMany$FlatMapManyMain.onError(MonoFlatMapMany.java:204) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.SerializedSubscriber.onError(SerializedSubscriber.java:124) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.FluxRetryWhen$RetryWhenMainSubscriber.whenError(FluxRetryWhen.java:224) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.FluxRetryWhen$RetryWhenOtherSubscriber.onError(FluxRetryWhen.java:273) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.FluxConcatMap$ConcatMapImmediate.drain(FluxConcatMap.java:413) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.FluxConcatMap$ConcatMapImmediate.onNext(FluxConcatMap.java:250) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.EmitterProcessor.drain(EmitterProcessor.java:491) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.EmitterProcessor.tryEmitNext(EmitterProcessor.java:299) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.SinkManySerialized.tryEmitNext(SinkManySerialized.java:97) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.InternalManySink.emitNext(InternalManySink.java:27) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.FluxRetryWhen$RetryWhenMainSubscriber.onError(FluxRetryWhen.java:189) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.publisher.MonoCreate$DefaultMonoSink.error(MonoCreate.java:189) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.netty.http.client.HttpClientConnect$MonoHttpConnect$ClientTransportSubscriber.onError(HttpClientConnect.java:306) [reactor-netty-http-1.0.2.jar:1.0.2]
		at reactor.core.publisher.MonoCreate$DefaultMonoSink.error(MonoCreate.java:189) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.netty.resources.DefaultPooledConnectionProvider$DisposableAcquire.onError(DefaultPooledConnectionProvider.java:166) [reactor-netty-core-1.0.2.jar:1.0.2]
		at reactor.netty.internal.shaded.reactor.pool.AbstractPool$Borrower.run(AbstractPool.java:365) [reactor-netty-core-1.0.2.jar:1.0.2]
		at reactor.core.scheduler.SchedulerTask.call(SchedulerTask.java:68) [reactor-core-3.4.1.jar:3.4.1]
		at reactor.core.scheduler.SchedulerTask.call(SchedulerTask.java:28) [reactor-core-3.4.1.jar:3.4.1]
		at java.util.concurrent.FutureTask.run(FutureTask.java:266) [na:1.8.0_191]
		at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.access$201(ScheduledThreadPoolExecutor.java:180) [na:1.8.0_191]
		at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:293) [na:1.8.0_191]
		at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149) [na:1.8.0_191]
		at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624) [na:1.8.0_191]
		at java.lang.Thread.run(Thread.java:748) [na:1.8.0_191]
Caused by: reactor.netty.internal.shaded.reactor.pool.PoolAcquireTimeoutException: Pool#acquire(Duration) has been pending for more than the configured timeout of 4500ms
	at reactor.netty.internal.shaded.reactor.pool.AbstractPool$Borrower.run(AbstractPool.java:365) [reactor-netty-core-1.0.2.jar:1.0.2]
	at reactor.core.scheduler.SchedulerTask.call(SchedulerTask.java:68) [reactor-core-3.4.1.jar:3.4.1]
	at reactor.core.scheduler.SchedulerTask.call(SchedulerTask.java:28) [reactor-core-3.4.1.jar:3.4.1]
	at java.util.concurrent.FutureTask.run(FutureTask.java:266) [na:1.8.0_191]
	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.access$201(ScheduledThreadPoolExecutor.java:180) [na:1.8.0_191]
	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:293) [na:1.8.0_191]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149) [na:1.8.0_191]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624) [na:1.8.0_191]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_191]
