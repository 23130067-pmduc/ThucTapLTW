document.addEventListener("DOMContentLoaded", function () {

    function initSlider(sliderSelector, dotsSelector, itemClass) {
        const slider = document.querySelector(sliderSelector);
        const dotsContainer = document.querySelector(dotsSelector);

        if (!slider || !dotsContainer) return;

        const items = slider.querySelectorAll(itemClass);
        if (items.length === 0) return;

        dotsContainer.innerHTML = "";

        const gap = 24;
        const itemWidth = items[0].offsetWidth + gap;

        const maxIndex = items.length - 4;

        for (let i = 0; i <= maxIndex; i++) {
            const dot = document.createElement("span");
            dot.classList.add("dot");
            if (i === 0) dot.classList.add("active");

            dot.addEventListener("click", () => {
                slider.scrollTo({
                    left: i * itemWidth,
                    behavior: "smooth"
                });
                setActiveDot(i);
            });

            dotsContainer.appendChild(dot);
        }
        function setActiveDot(index) {
            const dots = dotsContainer.querySelectorAll(".dot");
            dots.forEach(d => d.classList.remove("active"));
            if (dots[index]) dots[index].classList.add("active");
        }

        slider.addEventListener("scroll", () => {
            const index = Math.round(slider.scrollLeft / itemWidth);
            setActiveDot(Math.min(index, maxIndex));
        });
    }

    initSlider("#new-slider", "#new-dots", ".product-card");
    initSlider("#boy-slider", "#boy-dots", ".product-mini");
    initSlider("#girl-slider", "#girl-dots", ".product-mini");
    initSlider("#acc-slider", "#acc-dots", ".product-mini");
});