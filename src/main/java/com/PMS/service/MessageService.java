package com.PMS.service;

import java.util.List;

import com.PMS.model.Message;

public interface MessageService {
    Message sendMessage(Long senderId, Long projectId, String content) throws Exception;

    List<Message> getMessagesByProjectId(Long projectId) throws Exception;

    void saveMessage(Message message);
}