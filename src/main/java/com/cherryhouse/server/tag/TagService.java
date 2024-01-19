package com.cherryhouse.server.tag;

import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.posttag.PostTag;
import com.cherryhouse.server.posttag.PostTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    @Transactional
    public void create(Post post, List<String> tags){
        for(String tagName : tags){
            Optional<Tag> existingTag = tagRepository.findByName(tagName);

            //태그 이름이 있으면 위에서 가져온 태그 그대로 넣고, 없으면 새로운 태그 생성
            Tag tag = existingTag.orElseGet(() -> {
                Tag newTag = Tag.builder()
                        .name(tagName)
                        .build();
                tagRepository.save(newTag);
                return newTag;
            });

            PostTag postTag = PostTag.builder()
                    .post(post)
                    .tag(tag)
                    .build();
            postTagRepository.save(postTag);
        }
    }

    public List<String> getTags(Long postId){
        return postTagRepository.findByPostId(postId)
                .stream()
                .map(postTag -> postTag.getTag().getName())
                .toList();
    }
}
