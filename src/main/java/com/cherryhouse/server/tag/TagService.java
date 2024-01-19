package com.cherryhouse.server.tag;

import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.posttag.PostTag;
import com.cherryhouse.server.posttag.PostTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void update(Post post, List<String> tags){
        List<PostTag> postTags = postTagRepository.findByPostId(post.getId());

        tags.stream()
                //tags(새로 들어온 태그) 중 가지고 있지 않은 경우만 확인하면 됨
                .filter(tagName -> postTags.stream().noneMatch(postTag -> postTag.getTag().getName().equals(tagName)))
                .forEach(tagName -> {
                    //태그 테이블에 있을 경우 get 으로 가져오고, 없을 경우 새로운 태그를 생성한 후 가져옴
                    Tag tag = tagRepository.findByName(tagName)
                            .orElseGet(() -> {
                                Tag newTag = Tag.builder()
                                        .name(tagName)
                                        .build();
                                tagRepository.save(newTag);
                                return newTag;
                            });

                    PostTag newPostTag = PostTag.builder()
                            .post(post)
                            .tag(tag)
                            .build();
                    postTagRepository.save(newPostTag);
                });

        //tags(새로 들어온 태그)에 없고 post tag 에 있는 태그 삭제
        postTagRepository.findByPostId(post.getId())
                .stream()
                .filter(postTag -> !tags.contains(postTag.getTag().getName()))
                .forEach(postTag -> postTagRepository.deleteById(postTag.getId()));

    }
}
