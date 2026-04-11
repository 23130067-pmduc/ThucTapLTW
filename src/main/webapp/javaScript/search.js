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

    const searchInput = document.getElementById('ajaxSearchInput');
    const searchResults = document.getElementById('ajaxSearchResults');
    const historyBox = document.getElementById('searchHistoryBox');
    let timeout = null;

    if (!searchInput || !searchResults) return;

    const contextPath = searchInput.getAttribute('data-context') || '';

    searchInput.addEventListener('input', function () {
        clearTimeout(timeout);
        const keyword = this.value.trim();

        if (keyword.length > 0) {
            if (historyBox) historyBox.style.display = 'none';

            timeout = setTimeout(function () {
                fetch(contextPath + '/ajax-suggest?keyword=' + encodeURIComponent(keyword))
                    .then(response => response.json())
                    .then(data => {
                        searchResults.innerHTML = '';

                        if (data.length > 0) {
                            const ul = document.createElement('ul');
                            ul.className = 'suggest-list';

                            data.forEach(item => {
                                const li = document.createElement('li');

                                const a = document.createElement('a');
                                a.href = contextPath + '/chi-tiet-san-pham?id=' + item.id;
                                a.className = 'suggest-item';

                                const img = document.createElement('img');
                                img.src = item.thumbnail;
                                img.alt = item.name;
                                img.onerror = function() {
                                    this.onerror = null;
                                    this.src = contextPath + '/img/gau.png';
                                };

                                const infoDiv = document.createElement('div');
                                infoDiv.className = 'suggest-info';

                                const nameDiv = document.createElement('div');
                                nameDiv.className = 'suggest-name';
                                nameDiv.textContent = item.name;

                                const priceBox = document.createElement('div');
                                priceBox.className = 'suggest-price-box';

                                if (item.sale_price && item.sale_price > 0) {
                                    const saleSpan = document.createElement('span');
                                    saleSpan.className = 'suggest-price-sale';
                                    saleSpan.textContent = item.salePriceFormatted;

                                    const oldSpan = document.createElement('span');
                                    oldSpan.className = 'suggest-price-old';
                                    oldSpan.textContent = item.priceFormatted;

                                    priceBox.appendChild(saleSpan);
                                    priceBox.appendChild(oldSpan);
                                } else {
                                    const priceSpan = document.createElement('span');
                                    priceSpan.className = 'suggest-price';
                                    priceSpan.textContent = item.priceFormatted;

                                    priceBox.appendChild(priceSpan);
                                }

                                infoDiv.appendChild(nameDiv);
                                infoDiv.appendChild(priceBox);

                                a.appendChild(img);
                                a.appendChild(infoDiv);

                                li.appendChild(a);
                                ul.appendChild(li);
                            });

                            searchResults.appendChild(ul);

                            const moreDiv = document.createElement('div');
                            moreDiv.className = 'suggest-more';

                            const moreA = document.createElement('a');
                            moreA.href = contextPath + '/SearchController?keyword=' + encodeURIComponent(keyword);
                            moreA.textContent = 'Xem tất cả kết quả cho "' + keyword + '"';

                            moreDiv.appendChild(moreA);
                            searchResults.appendChild(moreDiv);
                        } else {
                            const emptyDiv = document.createElement('div');
                            emptyDiv.className = 'suggest-empty';
                            emptyDiv.textContent = 'Không tìm thấy sản phẩm nào';
                            searchResults.appendChild(emptyDiv);
                        }
                        searchResults.style.display = 'block';
                    })
                    .catch(err => console.error(err));
            }, 300);
        } else {
            searchResults.style.display = 'none';
            if (historyBox) historyBox.style.display = 'block';
        }
    });

    // Ẩn kết quả khi click ra ngoài
    document.addEventListener('click', function (e) {
        const boxSearch = document.querySelector('.boxSearch');
        if (boxSearch && !boxSearch.contains(e.target)) {
            searchResults.style.display = 'none';
        }
    });

    // Hiện lại khi click vào ô search
    searchInput.addEventListener('focus', function () {
        if (this.value.trim().length > 0 && searchResults.innerHTML !== '') {
            searchResults.style.display = 'block';
            if (historyBox) historyBox.style.display = 'none';
        }
    });
})