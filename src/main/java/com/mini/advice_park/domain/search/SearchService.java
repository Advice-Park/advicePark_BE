package com.mini.advice_park.domain.search;

import com.mini.advice_park.domain.Comment.CommentRepository;
import com.mini.advice_park.domain.Comment.dto.CommentResponse;
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
    private final CommentRepository commentRepository;

    /**
     * 검색어로 게시글 검색
     * 제목, 내용에서 검색
     */
    @Transactional(readOnly = true)
    public List<PostResponse> searchPosts(String keyword) {

        List<PostResponse> postsByTitle = postRepository.findByTitleContaining(keyword).stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());

        List<PostResponse> postsByContents = postRepository.findByContentsContaining(keyword).stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());

        List<PostResponse> combinedResults = Stream.concat(postsByTitle.stream(), postsByContents.stream())
                .distinct()
                .collect(Collectors.toList());

        return combinedResults;
    }

    /**
     * 검색어로 댓글 검색
     */
    @Transactional(readOnly = true)
    public List<CommentResponse> searchComments(String keyword) {

        List<CommentResponse> commentsByContent = commentRepository.findByContentContaining(keyword).stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());

        return commentsByContent;
    }

}
