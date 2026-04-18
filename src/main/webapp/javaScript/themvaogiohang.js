function showToast(message) {
    const toast = document.getElementById("toast");
    if (!toast) {
        alert(message);
        return;
    }

    toast.textContent = message;
    toast.className = "show";

    setTimeout(() => {
        toast.className = toast.className.replace("show", "");
    }, 3000);
}


document.addEventListener("DOMContentLoaded", function () {

    const modal = document.getElementById("quickAddModal");
    const closeBtn = document.getElementById("closeQuickAdd");


    document.querySelectorAll(".btn-add").forEach(btn => {
        btn.addEventListener("click", function () {
            modal.classList.add("show");
        });
    });


    closeBtn.addEventListener("click", function () {
        modal.classList.remove("show");
    });


    modal.addEventListener("click", function (e) {
        if (e.target === modal) {
            modal.classList.remove("show");
        }
    });

});