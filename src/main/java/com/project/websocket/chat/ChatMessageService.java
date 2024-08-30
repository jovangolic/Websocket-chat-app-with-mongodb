package com.project.websocket.chat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.websocket.chatroom.ChatRoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

	private final ChatMessageRepository chatMessageRepository;
	
	private ChatRoomService chatRoomService;
	
	public ChatMessage save(ChatMessage chatMessage) {
		var chatId = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true).orElseThrow();
		chatMessage.setChatId(chatId);
		chatMessageRepository.save(chatMessage);
		return chatMessage;
	}
	
	public List<ChatMessage> findChatMessages(String senderId, String recipientId){
		var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
		return chatId.map(chatMessageRepository::findByChatId).orElse(new ArrayList<>());
	}
}
