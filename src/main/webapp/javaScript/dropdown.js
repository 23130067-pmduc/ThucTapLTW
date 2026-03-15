document.addEventListener("click", function (e) {
    const dropdowns = document.querySelectorAll(".dropdown");
    dropdowns.forEach((dropdown) => {
        if (dropdown.contains(e.target)) {
            dropdown.classList.toggle("show");
        } else {
            dropdown.classList.remove("show");
        }
    });
});