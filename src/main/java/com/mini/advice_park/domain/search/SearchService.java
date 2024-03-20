package com.mini.advice_park.domain.search;

import com.mini.advice_park.domain.post.PostRepository;
import com.mini.advice_park.domain.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final PostRepository postRepository;

    /**
     * 검색어로 게시글 검색
     * 제목, 내용에서 검색
     */
    @Transactional(readOnly = true)
    public List<PostResponse> searchPosts(String keyword) {

        // 제목과 내용에 대한 검색을 수행하여 모든 결과를 가져옴
        List<PostResponse> postsByTitle = postRepository.findByTitleContaining(keyword).stream()
                .map(PostResponse::from) // PostResponse로 변환
                .collect(Collectors.toList());

        List<PostResponse> postsByContents = postRepository.findByContentsContaining(keyword).stream()
                .map(PostResponse::from) // PostResponse로 변환
                .collect(Collectors.toList());

        // 두 결과를 조합하여 하나의 리스트로 반환하거나,
        // 필요에 따라 서로 다른 방식으로 합치거나 처리할 수 있음

        return postsByTitle.stream()
                .distinct() // 중복 제거
                .collect(Collectors.toList());
    }

}
