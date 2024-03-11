package com.cherryhouse.server.reserve;

import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.post.dto.PostResponse;
import com.cherryhouse.server.post.image.ImageMapping;
import com.cherryhouse.server.post.posttag.PostTagMapping;

import java.time.LocalDate;
import java.util.List;

public class ReserveResponse {

    public record ReserveDto(

            PageData page,
            List<ReservePostDto> reserveList

    ) {
        public static ReserveDto of(
                PageData pageData,
                List<Reserve> reserveList,
                List<PostTagMapping.TagsDto> tagsDtoList,
                List<ImageMapping.UrlDto> urlDtoList
        ) {
            return new ReserveDto(
                    pageData,
                    reserveList.stream()
                            .map(reserve -> new ReserveResponse.ReserveDto.ReservePostDto(
                                    reserve,
                                    reserve.getPost(),
                                    PostTagMapping.getTagsByPostId(tagsDtoList, reserve.getPost().getId()),
                                    ImageMapping.getUrlByPostId(urlDtoList, reserve.getPost().getId())
                            ))
                            .toList()
            );
        }

        public record ReservePostDto(

                Status status,
                LocalDate reserveDate,
                Long pId,
                String pTitle,
                String pLocation,
                String pAddress,
                String pDistance,
                List<String> pTags,
                String pImage

        ) {
            public ReservePostDto(Reserve reserve, Post post, List<String> tags, String image) {
                this(
                        reserve.getStatus(),
                        reserve.getReservationDate(),
                        post.getId(),
                        post.getTitle(),
                        null,
                        null,
                        null,
                        tags,
                        image
                );
            }
        }
    }
}


