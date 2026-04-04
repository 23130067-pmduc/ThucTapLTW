document.addEventListener("DOMContentLoaded", () => {
    const product = document.querySelectorAll(".product-card");
    const loadMoreButton = document.getElementById("load-more");
    const scrollTopBtn = document.getElementById("scrollTopBtn");

    let visibleCount = 16;
    const increment = 8;

    product.forEach((p , i) =>{
        if ( i >= visibleCount) p.classList.add("hidden");
    });


    if (product.length <= visibleCount){
        loadMoreButton.style.display = "none";
    }

    loadMoreButton.addEventListener("click", () =>{
        const  hiddenProducts = document.querySelectorAll(".product-card.hidden");
        for ( let i = 0; i < increment && i < hiddenProducts.length; i++){
            hiddenProducts[i].classList.remove("hidden");
        }

        if(document.querySelectorAll(".product-card.hidden").length === 0){
            loadMoreButton.style.display = "none";
        }
    })


    if (scrollTopBtn) {
        window.addEventListener("scroll", () => {
            if (window.scrollY > 300) {
                scrollTopBtn.style.display = "block";
            } else {
                scrollTopBtn.style.display = "none";
            }
        });

        scrollTopBtn.addEventListener("click", () => {
            window.scrollTo({
                top: 0,
                behavior: "smooth"
            });
        });
    }
})