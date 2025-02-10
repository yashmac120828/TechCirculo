package org.yug.backend.repository;

import org.yug.backend.model.Community;
import org.yug.backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByCommunityOrderByCreatedAtDesc(Community community);
}