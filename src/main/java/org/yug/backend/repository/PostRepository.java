package org.yug.backend.repository;

import com.techcirculo.model.Community;
import com.techcirculo.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByCommunityOrderByCreatedAtDesc(Community community);
}