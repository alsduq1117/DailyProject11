package com.example.project11.repository;

import com.example.project11.domain.Post;
import com.example.project11.domain.QPost;
import com.example.project11.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<Post> getPostPage(PostSearch postSearch) {
        long totalCount = jpaQueryFactory.select(QPost.post.count())
                .from(QPost.post)
                .fetchFirst();

        List<Post> postList = jpaQueryFactory.selectFrom(QPost.post)
                .offset(postSearch.getOffset())
                .limit(postSearch.getSize())
                .orderBy(QPost.post.id.desc())
                .fetch();

        return new PageImpl<>(postList, postSearch.getPageable(), totalCount);
    }
}
