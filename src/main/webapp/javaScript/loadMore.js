document.addEventListener("DOMContentLoaded", () => {
    const product = document.querySelectorAll(".product-card");
    const loadMoreButton = document.getElementById("load-more");

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

})