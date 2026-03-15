const  thongbaoBtn = document.getElementById("thongBao");
const  notificationBox = document.getElementById("notification-box");


thongbaoBtn.addEventListener("click", function(event){
    event.stopPropagation();  // Cái này dùng để ngăn click lan ra ngoài.
    notificationBox.classList.toggle("active");

})
document.addEventListener("click", function(event){
    if (!notificationBox.contains(event.target) && !thongbaoBtn.contains(event.target)){
        notificationBox.classList.remove("active");
    }
})