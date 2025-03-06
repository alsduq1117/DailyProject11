package com.example.project11.repository;

import com.example.project11.domain.Post;
import com.example.project11.request.PostSearch;
import org.springframework.data.domain.Page;

public interface PostCustomRepository {
    Page<Post> getPostPage(PostSearch postSearch);
}
