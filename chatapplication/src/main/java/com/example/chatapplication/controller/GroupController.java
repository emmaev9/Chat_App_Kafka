package com.example.chatapplication.controller;

import com.example.chatapplication.model.Group;
import com.example.chatapplication.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/addUser")
    public ResponseEntity<?> addUserToGroup(@PathVariable String groupName, @RequestParam String userId) {
        groupService.addUserToGroup(groupName, userId);
        return ResponseEntity.ok("Succesfully added group");
    }

}