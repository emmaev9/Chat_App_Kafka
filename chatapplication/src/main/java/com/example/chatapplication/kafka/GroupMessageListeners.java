package com.example.chatapplication.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class GroupMessageListeners {
    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    @Autowired
    private ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void addGroupTopicListener(String topic) {
        try {
            Method method = GroupMessageListeners.class.getMethod("handleMessage", ConsumerRecord.class);
            MethodKafkaListenerEndpoint<String, String> endpoint = new MethodKafkaListenerEndpoint<>();
            endpoint.setId(topic);
            endpoint.setGroupId("group_id_" + topic);
            endpoint.setTopics(topic);
            endpoint.setMethod(method);
            endpoint.setBean(this);

            kafkaListenerEndpointRegistry.registerListenerContainer(endpoint, kafkaListenerContainerFactory, true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void handleMessage(ConsumerRecord<String, String> record) {
        String topic = record.topic();
        String message = record.value();

        if (topic.startsWith("private_")) {
            // This is a private message
            String recipient = topic.substring("private_".length());
            messagingTemplate.convertAndSendToUser(recipient, "/queue/private", message);
        } else {
            // This is a group message
            messagingTemplate.convertAndSend("/topic/group/" + topic, message);
        }
    }
}
