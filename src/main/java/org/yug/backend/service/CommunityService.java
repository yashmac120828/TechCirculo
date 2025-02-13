package org.yug.backend.service;



import jakarta.transaction.Transactional;
import org.apache.catalina.connector.Response;
import org.yug.backend.dto.CommunityRequest;
import org.yug.backend.dto.CommunityResponse;
import org.yug.backend.model.Community;
import org.yug.backend.model.User;
import org.yug.backend.repository.CommunityRepository;
import org.yug.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor

public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;

    public List<CommunityResponse> getAllCommunities() {
        return communityRepository.findAll().stream()
                .map(this::mapToCommunityResponse)
                .collect(Collectors.toList());

    }

    public List<CommunityResponse> getMyCommunities(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return communityRepository.findByMembersContaining(user).stream()
                .map(this::mapToCommunityResponse)
                .collect(Collectors.toList());
    }

    public CommunityResponse createCommunity(CommunityRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Community community = new Community();
        community.setId(UUID.randomUUID());
        community.setName(request.getName());
        community.setDescription(request.getDescription());
        community.setCreatedBy(user);
        community.setCreatedAt(LocalDateTime.now());
        community.setMemberCount(0);

        communityRepository.save(community);
        return mapToCommunityResponse(community);
    }

    public void joinCommunity(UUID communityId, UUID userId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found!"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        community.getMembers().add(user);
        community.setMemberCount(community.getMemberCount() + 1);
        communityRepository.save(community);
    }

    public void leaveCommunity(UUID communityId, UUID userId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found!"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        community.getMembers().remove(user);
        community.setMemberCount(community.getMemberCount() - 1);
        communityRepository.save(community);
    }

    private CommunityResponse mapToCommunityResponse(Community community) {
        return new CommunityResponse(
                community.getId(),
                community.getName(),
                community.getDescription(),
                community.getCreatedBy().getUsername(),
                community.getMemberCount(),
                community.getCreatedAt()
        );
    }
}