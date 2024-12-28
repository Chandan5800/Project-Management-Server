package com.PMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PMS.model.Chat;
import com.PMS.repository.ChatRepository;

@Service
public class ChatServiceImpl implements ChatService{

    @Autowired
    private ChatRepository chatRepository;

    @Override
    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }

}
