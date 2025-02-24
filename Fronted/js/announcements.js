document.addEventListener("DOMContentLoaded", function () {
    // Handle Announcements
    const announcementList = document.getElementById("announcement-list");
    const searchInput = document.getElementById("search");
    const filterSelect = document.getElementById("filter");
    const sortSelect = document.getElementById("sort");
    const loadMoreBtn = document.getElementById("load-more");

    let announcements = [];
    let visibleAnnouncements = 5;

    function fetchAnnouncements() {
        fetch("/api/announcements")
            .then(response => response.json())
            .then(data => {
                announcements = data;
                displayAnnouncements();
            });
    }

    function displayAnnouncements() {
        announcementList.innerHTML = "";
        const filtered = filterAnnouncements();
        const sorted = sortAnnouncements(filtered);

        sorted.slice(0, visibleAnnouncements).forEach(announcement => {
            const div = document.createElement("div");
            div.className = `announcement ${announcement.read ? '' : 'unread'}`;
            div.innerHTML = `
                <h3>${announcement.title}</h3>
                <p>${announcement.content}</p>
                <button onclick="toggleRead(${announcement.id})">${announcement.read ? 'Mark as Unread' : 'Mark as Read'}</button>
                <button onclick="toggleBookmark(${announcement.id})">${announcement.bookmarked ? 'Remove Bookmark' : 'Bookmark'}</button>
            `;
            announcementList.appendChild(div);
        });
    }

    function filterAnnouncements() {
        return announcements.filter(a => {
            if (filterSelect.value === "unread" && a.read) return false;
            if (filterSelect.value === "bookmarked" && !a.bookmarked) return false;
            return a.title.toLowerCase().includes(searchInput.value.toLowerCase());
        });
    }

    function sortAnnouncements(list) {
        return list.sort((a, b) => sortSelect.value === "latest" ? b.date - a.date : a.date - b.date);
    }

    function toggleRead(id) {
        const announcement = announcements.find(a => a.id === id);
        if (announcement) {
            announcement.read = !announcement.read;
            displayAnnouncements();
        }
    }

    function toggleBookmark(id) {
        const announcement = announcements.find(a => a.id === id);
        if (announcement) {
            announcement.bookmarked = !announcement.bookmarked;
            displayAnnouncements();
        }
    }

    searchInput.addEventListener("input", displayAnnouncements);
    filterSelect.addEventListener("change", displayAnnouncements);
    sortSelect.addEventListener("change", displayAnnouncements);

    loadMoreBtn.addEventListener("click", function () {
        visibleAnnouncements += 5;
        displayAnnouncements();
    });

    fetchAnnouncements();

    // Sidebar Active State Fix
    let currentPath = window.location.pathname.split("/").pop() || "index.html"; // Default to index.html if empty
    let sidebarLinks = document.querySelectorAll(".sidebar nav a");

    sidebarLinks.forEach(link => {
        let href = link.getAttribute("href").split("/").pop(); // Get only filename (e.g., dashboard.html)

        if (href === currentPath) {
            link.classList.add("active");
        } else {
            link.classList.remove("active");
        }
    });
});
