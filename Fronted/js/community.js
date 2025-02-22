document.addEventListener("DOMContentLoaded", async function () {
    const API_BASE_URL = "https://your-backend-api.com"; // Replace with actual backend URL
    const userId = localStorage.getItem("userId"); // Assuming user ID is stored after login

    const communityList = document.getElementById("community-list");
    const joinedList = document.getElementById("joined-list");
    const postsSection = document.getElementById("posts-section");
    const announcementsSection = document.getElementById("announcements-section");
    const postsTab = document.getElementById("posts-tab");
    const announcementsTab = document.getElementById("announcements-tab");

    // Default: Show Posts, Hide Announcements
    postsSection.classList.add("active");

    // Fetch all communities from the backend
    async function fetchCommunities() {
        try {
            const response = await fetch(`${API_BASE_URL}/communities`);
            return response.ok ? await response.json() : [];
        } catch (error) {
            console.error("Error fetching communities:", error);
            return [];
        }
    }

    // Fetch joined communities for the user
    async function fetchJoinedCommunities() {
        try {
            const response = await fetch(`${API_BASE_URL}/user/${userId}/joined-communities`);
            return response.ok ? await response.json() : [];
        } catch (error) {
            console.error("Error fetching joined communities:", error);
            return [];
        }
    }

    // Render communities list
    async function renderCommunities() {
        const allCommunities = await fetchCommunities();
        const joinedCommunities = await fetchJoinedCommunities();
        communityList.innerHTML = "";
        joinedList.innerHTML = "";

        allCommunities.forEach(comm => {
            const li = document.createElement("li");
            li.textContent = comm.name;
            li.classList.add("community-item");
            li.onclick = () => displayCommunityData(comm.id);

            const button = document.createElement("button");
            button.classList.add("join-btn");
            button.textContent = "Join";
            button.onclick = (e) => {
                e.stopPropagation(); // Prevent parent li click event
                joinCommunity(comm.id);
            };

            li.appendChild(button);
            communityList.appendChild(li);
        });

        joinedCommunities.forEach(comm => {
            const li = document.createElement("li");
            li.textContent = comm.name;
            li.classList.add("community-item");
            li.onclick = () => displayCommunityData(comm.id);

            const button = document.createElement("button");
            button.classList.add("remove-btn");
            button.textContent = "Remove";
            button.onclick = (e) => {
                e.stopPropagation(); // Prevent parent li click event
                removeCommunity(comm.id);
            };

            li.appendChild(button);
            joinedList.appendChild(li);
        });
    }

    // Join a community
    async function joinCommunity(communityId) {
        try {
            const response = await fetch(`${API_BASE_URL}/user/${userId}/join-community`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ communityId })
            });

            if (response.ok) {
                await renderCommunities();
            } else {
                console.error("Error joining community:", response.statusText);
            }
        } catch (error) {
            console.error("Error:", error);
        }
    }

    // Remove a community
    async function removeCommunity(communityId) {
        try {
            const response = await fetch(`${API_BASE_URL}/user/${userId}/leave-community`, {
                method: "DELETE",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ communityId })
            });

            if (response.ok) {
                await renderCommunities();
            } else {
                console.error("Error removing community:", response.statusText);
            }
        } catch (error) {
            console.error("Error:", error);
        }
    }

    // Fetch and display posts & announcements for a community
    async function displayCommunityData(communityId) {
        try {
            const [postsResponse, announcementsResponse] = await Promise.all([
                fetch(`${API_BASE_URL}/community/${communityId}/posts`),
                fetch(`${API_BASE_URL}/community/${communityId}/announcements`)
            ]);

            const posts = await postsResponse.json();
            const announcements = await announcementsResponse.json();

            postsSection.innerHTML = "<h3>Posts</h3>";
            announcementsSection.innerHTML = "<h3>Announcements</h3>";

            posts.forEach(post => {
                const div = document.createElement("div");
                div.classList.add("post-item");
                div.textContent = post.content;
                postsSection.appendChild(div);
            });

            announcements.forEach(announcement => {
                const div = document.createElement("div");
                div.classList.add("announcement-item");
                div.textContent = announcement.content;
                announcementsSection.appendChild(div);
            });

        } catch (error) {
            console.error("Error fetching posts or announcements:", error);
        }
    }

    // Tab Click Events
    postsTab.addEventListener("click", function () {
        postsSection.classList.add("active");
        announcementsSection.classList.remove("active");
        postsTab.classList.add("active");
        announcementsTab.classList.remove("active");
    });

    announcementsTab.addEventListener("click", function () {
        announcementsSection.classList.add("active");
        postsSection.classList.remove("active");
        announcementsTab.classList.add("active");
        postsTab.classList.remove("active");
    });

    // Initial Render
    renderCommunities();
});
