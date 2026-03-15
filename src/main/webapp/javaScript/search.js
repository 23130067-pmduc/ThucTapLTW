document.addEventListener("DOMContentLoaded", () => {
    const searchIcon = document.querySelector(".iconSearch");
    const searchOverlay = document.getElementById("searchOverlay");
    const closeSearch = document.getElementById("closeSearch");

    searchIcon.addEventListener("click", (e) => {
        e.preventDefault();
        searchOverlay.classList.add("active");
    })

    closeSearch.addEventListener("click", () => {
        searchOverlay.classList.remove("active");
    })

    document.addEventListener("click", (e) => {
        if (!searchOverlay.contains(e.target) && !searchIcon.contains(e.target)) {
            searchOverlay.classList.remove("active");
        }
    })
})