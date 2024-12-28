package com.PMS.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PMS.model.Chat;
import com.PMS.model.Message;
import com.PMS.model.User;
import com.PMS.repository.MessageRepository;
import com.PMS.repository.UserRepository;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectService projectService;

    @Override
    public Message sendMessage(Long senderId, Long projectId, String content) throws Exception {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new Exception("User not found with id: " + senderId));
        Chat chat = projectService.getProjectById(projectId).getChat();

        Message message = new Message();
        message.setSender(sender);
        message.setChat(chat);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);

        chat.getMessages().add(savedMessage);

        return savedMessage;
    }

    @Override
    public List<Message> getMessagesByProjectId(Long projectId) throws Exception {
        Chat chat = projectService.getChatByProjectId(projectId);
        List<Message> messages = messageRepository.findByChatIdOrderByCreatedAtAsc(chat.getId());
        return messages;
    }

    @Override
    public void saveMessage(Message message) {
        message.setCreatedAt(LocalDateTime.now());
        messageRepository.save(message);
    }
}