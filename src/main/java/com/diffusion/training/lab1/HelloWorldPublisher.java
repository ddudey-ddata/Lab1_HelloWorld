package com.diffusion.training.lab1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.pushtechnology.diffusion.client.Diffusion;
import com.pushtechnology.diffusion.client.features.TopicUpdate;
import com.pushtechnology.diffusion.client.features.control.topics.TopicControl;
import com.pushtechnology.diffusion.client.session.Session;
import com.pushtechnology.diffusion.client.topics.details.TopicType;

public class HelloWorldPublisher {

	Session session;
	TopicControl topicControl;
	TopicUpdate topicUpdate;

	private static final Logger LOG = LoggerFactory.getLogger(HelloWorldPublisher.class);

	public HelloWorldPublisher() {
		connect("ws://localhost:8080", "admin", "password");
		try {
			this.publishToTopic("my/first/topic");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void connect(String url, String username, String password) {

		session = Diffusion.sessions().principal(username).password(password).open(url);

	}

	public void publishToTopic(String topicPath) throws InterruptedException, ExecutionException, TimeoutException {
		// Get the TopicControl and TopicUpdate feature
		final TopicControl topicControl = session.feature(TopicControl.class);

		final TopicUpdate topicUpdate = session.feature(TopicUpdate.class);

		// Create an int64 topic 'foo/counter'
		final CompletableFuture<TopicControl.AddTopicResult> future = topicControl.addTopic(topicPath,
				TopicType.STRING);

		// Wait for the CompletableFuture to complete
		future.get(10, TimeUnit.SECONDS);

		// Update the topic
		for (long i = 0; i < 1000; ++i) {

			// Use the non-exclusive updater to update the topic without locking it
			LOG.info("Publishing to Topic");
			topicUpdate.set(topicPath, String.class, "Hello World -" + i);

			Thread.sleep(1000);
		}
	}

	public static void main(String[] args) {
		new HelloWorldPublisher();

	}

}
