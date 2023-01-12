package com.diffusion.training.lab1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pushtechnology.diffusion.client.Diffusion;
import com.pushtechnology.diffusion.client.features.Topics;
import com.pushtechnology.diffusion.client.features.Topics.ValueStream;
import com.pushtechnology.diffusion.client.session.Session;
import com.pushtechnology.diffusion.client.topics.details.TopicSpecification;

public class HelloWorldSubscriber {

    Session session;
    Topics topics;
    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldSubscriber.class);

    public HelloWorldSubscriber() {
        connect("ws://localhost:8080");
        this.subscribeToTopic("my/first/topic");

    }

    public void connect(String url) {

        session = Diffusion.sessions().open(url);
    }

    public void subscribeToTopic(String topicPath) {
        // Get the Topics feature to subscribe to topics
        topics = session.feature(Topics.class);

        // Add a new stream for topic
        topics.addStream(topicPath, String.class, new ValueStreamPrintLn());

        // Subscribe to the topic
        topics.subscribe(topicPath).whenComplete((voidResult, exception) -> {
            if (exception != null) {
                LOG.info("subscription failed", exception);
            }
        });

        // Wait for a minute while the stream prints updates
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new HelloWorldSubscriber();

    }

    private static class ValueStreamPrintLn extends ValueStream.Default<String> {
        @Override
        public void onValue(String topicPath, TopicSpecification specification, String oldValue, String newValue) {
            System.out.println(topicPath + ":   " + newValue);
        }
    }

}
