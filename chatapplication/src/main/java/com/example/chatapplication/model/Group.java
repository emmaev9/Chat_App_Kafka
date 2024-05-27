package com.example.chatapplication.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Group {
    private String groupId;
    private Set<String> usernames = new HashSet<>();

    public Group(String groupId, String creator) {
        this.groupId = groupId;
        this.usernames = new HashSet<>();
        this.usernames.add(creator);
    }

    public void addUser(String username) {
        usernames.add(username);
    }

}