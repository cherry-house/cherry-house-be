package com.cherryhouse.server.chatroom;

import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.post.PostService;
import com.cherryhouse.server.user.User;
import com.cherryhouse.server.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final PostService postService;
    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;

    public void getChatRooms(Pageable pageable, Long userId){
        Page<Post> posts = chatRoomRepository.findAllOrderByCreatedDate(pageable, userId);

    }

    @Transactional
    public void create(String email, Long postId){
        Post post = postService.getPostById(postId);
        User user = userService.findByEmail(email);

        ChatRoom chatRoom = ChatRoom.builder()
                .post(post)
                .user(user)
                .build();
        chatRoomRepository.save(chatRoom);
    }
}
