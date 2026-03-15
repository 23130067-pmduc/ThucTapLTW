function showToast(message){
    const toast = document.getElementById("toast");
    toast.textContent = message;
    toast.className = "show";

    setTimeout(() => toast.className = toast.className.replace("show", ""), 3000);
}