package com.project.websocket.chatroom;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	
	public Optional<String> getChatRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExists){
		
		return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
				.map(ChatRoom::getChatId)
				.or(() -> {
					if(createNewRoomIfNotExists) {
						var chatId = createChatId(senderId, recipientId);
						return Optional.of(chatId);
					}
					return Optional.empty();
				});
	}

	private String createChatId(String senderId, String recipientId) {
		var chatId = String.format("%s_%s",senderId,recipientId);
		//kreiranje objekta za sendera
		ChatRoom senderRecipient = ChatRoom.builder()
				.chatId(chatId)
				.senderId(senderId)
				.recipientId(recipientId)
				.build();
		//kreiranje objekta za recipient-a
		ChatRoom recipientSender = ChatRoom.builder()
				.chatId(chatId)
				.senderId(recipientId)
				.recipientId(senderId)
				.build();
		chatRoomRepository.save(senderRecipient);
		chatRoomRepository.save(recipientSender);
		return chatId;
	}
}
