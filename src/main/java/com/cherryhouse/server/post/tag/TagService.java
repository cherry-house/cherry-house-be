package com.cherryhouse.server.post.tag;

import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.post.posttag.PostTag;
import com.cherryhouse.server.post.posttag.PostTagMapping;
import com.cherryhouse.server.post.posttag.PostTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    @Transactional
    public void create(Post post, List<String> tags){
        tags.forEach(tagName -> save(post, tagName));
    }

    @Transactional
    public void update(Post post, List<String> tags){
        List<PostTag> postTags = postTagRepository.findByPostId(post.getId());

        //tags(새로 들어온 태그) 중 가지고 있지 않은 경우만 확인하면 됨
        tags.stream()
                .filter(tagName -> postTags.stream().noneMatch(postTag -> postTag.getTag().getName().equals(tagName)))
                .forEach(tagName -> save(post, tagName));

        //tags(새로 들어온 태그)에 없고 post tag 에 있는 태그 삭제
        postTagRepository.findByPostId(post.getId())
                .stream()
                .filter(postTag -> !tags.contains(postTag.getTag().getName()))
                .forEach(postTag -> postTagRepository.deleteById(postTag.getId()));
    }

    @Transactional
    public void delete(Long postId){
        postTagRepository.deleteByPostId(postId);
    }

    private void save(Post post, String tagName) {
        Tag tag = tagRepository.findByName(tagName)
                .orElseGet(() -> {
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

    //post id, tags 반환 : getPosts, getHearts
    public List<PostTagMapping.TagsDto> getTagsDtoList(List<Post> postList){
        return postList.stream()
                .map(this::getTagsDto)
                .toList();
    }

    //tags 반환 : getPost
    public PostTagMapping.TagsDto getTagsDto(Post post) {
        List<String> tags = postTagRepository.findByPostId(post.getId())
                .stream()
                .map(postTag -> postTag.getTag().getName())
                .toList();
        return new PostTagMapping.TagsDto(post.getId(), tags);
    }
}
