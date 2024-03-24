package com.mini.advice_park.domain.user.service;

import com.mini.advice_park.domain.Comment.CommentRepository;
import com.mini.advice_park.domain.Comment.like.LikeRepository;
import com.mini.advice_park.domain.Image.Image;
import com.mini.advice_park.domain.Image.ImageRepository;
import com.mini.advice_park.domain.Image.ImageS3Service;
import com.mini.advice_park.domain.favorite.UserPostFavoriteRepository;
import com.mini.advice_park.domain.post.PostRepository;
import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDeleteService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final ImageS3Service imageS3Service;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;
    private final UserPostFavoriteRepository userPostFavoriteRepository;

    @Transactional
    public void deleteUserAccount(User user) {

        postRepository.deleteAllByUser(user);

        List<Image> userImages = imageRepository.findByUserId(user.getUserId());
        imageS3Service.deleteImages(userImages);

        likeRepository.deleteAllByUser(user);

        userPostFavoriteRepository.deleteAllByUser(user);

        commentRepository.deleteAllByUser(user);

        userRepository.delete(user);
    }

}
