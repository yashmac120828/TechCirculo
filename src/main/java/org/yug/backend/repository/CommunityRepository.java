package org.yug.backend.repository;

import com.techcirculo.model.Community;
import com.techcirculo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CommunityRepository extends JpaRepository<Community, UUID> {
    List<Community> findByMembersContaining(User user);
}