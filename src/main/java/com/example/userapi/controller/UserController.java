package com.example.userapi.controller;

import com.example.userapi.client.FluentdClient;
import com.example.userapi.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {
    private int counter = 4;
    private final FluentdClient fluentdClient;

    private static final String USER_TAG = "user";
    private List<Map<String, String>> messages = new ArrayList<> () {
        {
            add(new HashMap<>() {{put("id", "1"); put("name", "Andriy");}});
            add(new HashMap<>() {{put("id", "2"); put("name", "Yaroslava");}});
            add(new HashMap<>() {{put("id", "3"); put("name", "Jen");}});
        }

    };

    @Autowired
    private UserController(FluentdClient fluentdClient) {
        this.fluentdClient = fluentdClient;
    }

    @GetMapping
    public List<Map<String, String>> list() {
        fluentdClient.send(USER_TAG, Map.of("message", "Request list of Users"));
        return messages;
    }

    @GetMapping("{id}")
    public Map<String, String> getOne(@PathVariable String id) {
        fluentdClient.send(USER_TAG, Map.of("message", "Request User " + id));
        return getMessage(id);
    }

    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> message) {
        message.put("id", String.valueOf(counter));
        messages.add(message);
        fluentdClient.send(USER_TAG, Map.of("message", "Create User " + counter));
        counter++;
        return message;
    }

    @PutMapping("{id}")
    public Map<String, String> update(@PathVariable String id, @RequestBody Map<String, String> message) {
        Map<String, String> messageFromDb = getMessage(id);
        messageFromDb.putAll(message);
        messageFromDb.put("id", id);
        fluentdClient.send(USER_TAG, Map.of("message", "Update User " + id));
        return messageFromDb;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Map<String, String> message = getMessage(id);
        messages.remove(message);
        fluentdClient.send(USER_TAG, Map.of("message", "Delete User " + id));
    }

    private Map<String, String> getMessage(@PathVariable String id) {
        return messages.stream()
                .filter(message -> message.get("id").equals(id))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
