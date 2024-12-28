package com.PMS.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PMS.model.Chat;
import com.PMS.model.Message;
import com.PMS.request.CreateMessageRequest;
import com.PMS.service.MessageService;
import com.PMS.service.ProjectService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private ProjectService projectService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody CreateMessageRequest request) throws Exception {
        // User user = userService.findUserById(request.getSenderId());

        Chat chats = projectService.getProjectById(request.getProjectId()).getChat();
        if (chats == null) {
            throw new Exception("Chat not found");
        }
        Message sentMessage = messageService.sendMessage(request.getSenderId(), request.getProjectId(),
                request.getContent());

        return ResponseEntity.ok(sentMessage);
    }

    @GetMapping("/chat/{projectId}")
    public ResponseEntity<List<Message>> getMessageByChatId(@PathVariable Long projectId) throws Exception {
        List<Message> messages = messageService.getMessagesByProjectId(projectId);

        return ResponseEntity.ok(messages);
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public Message sendMessage(Message message) {
        messageService.saveMessage(message);
        return message;
    }
}