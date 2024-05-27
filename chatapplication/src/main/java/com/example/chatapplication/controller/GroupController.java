package com.example.chatapplication.controller;

import com.example.chatapplication.model.Group;
import com.example.chatapplication.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/addUser/{groupName}/{userId}")
    public ResponseEntity<?> addUserToGroup(@PathVariable String groupName, @PathVariable String userId) {
        System.out.println("Group name: " + groupName);
        System.out.println("User: "+ userId);
        groupService.addUserToGroup(groupName, userId);
        return ResponseEntity.ok("Successfully added group");
    }

}