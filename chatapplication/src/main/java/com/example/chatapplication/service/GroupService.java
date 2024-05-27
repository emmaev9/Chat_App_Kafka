package com.example.chatapplication.service;

import com.example.chatapplication.kafka.KafkaConfiguration;
import com.example.chatapplication.model.Group;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@AllArgsConstructor
public class GroupService {

    private final HashMap<String, Group> groups;

    @Autowired
    private KafkaConfiguration kafkaConfig;

    public void addUserToGroup(String groupName, String username) {
        if(groups.containsKey(groupName)) {
            groups.get(groupName).addUser(username);
        }
        else {
            groups.put(groupName, new Group(groupName, username));
        }
    }

    public Group getGroup(String groupName) {
        return groups.get(groupName);
    }

}
