package com.example.demo.chat.kafka;


import com.example.demo.chat.Constants;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.example.demo.chat.model.ModelChat;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
public class ProducerController {

	private static final Logger LOG = LoggerFactory.getLogger(ProducerController.class);

	private final ProducerCallback producerCallback = new ProducerCallback();

	@Autowired
	private KafkaTemplate<String, ModelChat> kafkaTemplate;

	@GetMapping("/chat/{message}")
	public String generateMessages(@PathVariable String message) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		this.waitFor();
		this.sendToKafka(new ModelChat(username + ": " + message));
		return username;
	}

	@GetMapping("chat/user")
	public String checkUsername(){
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	private void sendToKafka(ModelChat model) {
		this.kafkaTemplate
				.send(Constants.KAFKA_TOPIC, UUID.randomUUID().toString(), model)
				.addCallback(this.producerCallback);
	}

	private void waitFor() {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		Future<Void> future = scheduler.schedule(() -> null, 200, TimeUnit.MILLISECONDS);
		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	static class ProducerCallback implements ListenableFutureCallback<SendResult<String, ModelChat>> {
		@Override
		public void onSuccess(SendResult<String, ModelChat> result) {
			RecordMetadata record = Objects.requireNonNull(result).getRecordMetadata();
			LOG.info("Sending {} to topic {} - partition {}",
					result.getProducerRecord().key(),
					result.getProducerRecord().topic(),
					record.partition());
		}

		@Override
		public void onFailure(@NotNull Throwable e) {
			LOG.error("Producer Failure", e);
		}
	}

}