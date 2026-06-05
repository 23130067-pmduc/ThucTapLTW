<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%
    request.setAttribute("pageCss", "chitietsanpham.css");
    request.setAttribute("pageTitle" , "Chi tiết sản phẩm");
%>

<%@include file="header.jsp"%>


<main class="product-detail">
    <div class="product-container">

        <div class="product-image">
            <c:forEach var="img" items="${images}">
                <c:if test="${img.main}">
                    <img id="main-image"
                         src="${img.imageUrl}"
                         alt="${product.name}">
                </c:if>
            </c:forEach>

            <div class="thumbs-carousel">
                <button type="button" class="thumb-arrow thumb-prev" id="thumbPrev">
                    &lt;
                </button>

                <div class="thumbs-viewport">
                    <div class="image-thumbs" id="imageThumbs">
                        <c:forEach var="img" items="${images}">
                            <img class="thumb ${img.main ? 'active' : ''}"
                                 src="${img.imageUrl}"
                                 alt="${product.name}">
                        </c:forEach>
                    </div>
                </div>

                <button type="button" class="thumb-arrow thumb-next" id="thumbNext">
                    &gt;
                </button>
            </div>
        </div>

        <div class="product-info">
            <h1 class="product-name">${product.name}</h1>

            <p class="product-price">Giá:
                <span><fmt:formatNumber value="${product.sale_price}" type="number"/>₫</span>
            </p>
            <div class="product-rating">
                <c:forEach begin="1" end="${displayStar}">⭐
                </c:forEach>

                <c:forEach begin="1" end="${5 - displayStar}">☆
                </c:forEach>

                (${totalReviews} đánh giá)
            </div>


            <div class="product-colors">
                <p><strong>Màu sắc:</strong></p>
                <div class="color-options">
                    <c:forEach var="color" items="${colors}">
                        <div class="color-thumb" data-color-id="${color.id}" style="background-color: ${color.hex};">
                        </div>
                    </c:forEach>
                </div>
            </div>


            <div class="product-sizes">
                <p><strong>Chọn size:</strong></p>
                <div class="size-options">
                    <c:forEach var="s" items="${sizes}">
                        <button class="size-btn"
                                data-size-id="${s.id}">
                                ${s.code}
                        </button>
                    </c:forEach>
                </div>
            </div>

            <div class="product-quantity">
                <p><strong>Số lượng:</strong></p>
                <div class="quantity-control">
                    <button class="btn-decrease">−</button>
                    <input type="number" id="quantity" min="1" value="1">
                    <button class="btn-increase">+</button>
                </div>
            </div>


            <div class="product-actions">
                <button class="btn-add-cart">Thêm vào giỏ hàng</button>
            </div>
        </div>
    </div>



    <div id="imageLightbox" class="image-lightbox">
        <button type="button" class="lightbox-close" id="lightboxClose">&times;</button>

        <button type="button" class="lightbox-nav lightbox-prev" id="lightboxPrev">
            &lt;
        </button>

        <img id="lightboxImage" src="" alt="${product.name}">

        <button type="button" class="lightbox-nav lightbox-next" id="lightboxNext">
            &gt;
        </button>
    </div>



    <section class="product-description-section">
        <div class="description-tab">Mô tả</div>

        <div class="description-box">
            <c:choose>
                <c:when test="${not empty product.description}">
                    <div id="productDescriptionWrapper" class="description-wrapper">
                        <div id="productDescription" class="description-content collapsed">
                                ${product.description}
                        </div>
                    </div>

                    <button type="button" id="toggleDescriptionBtn" class="toggle-description-btn">
                        Xem thêm
                    </button>
                </c:when>

                <c:otherwise>
                    <div class="description-empty">Đang cập nhật thông tin</div>
                </c:otherwise>
            </c:choose>
        </div>
    </section>


    <section class="product-review">
        <c:if test="${param.reviewSuccess == '1'}">
            <p class="review-success">Đánh giá của bạn đã được gửi thành công!</p>
        </c:if>


        <c:if test="${canReview}">
            <form action="${pageContext.request.contextPath}/review" method="post"
                  enctype="multipart/form-data" class="review-form">
                <input type="hidden" name="product_id" value="${product.id}">
                <input type="hidden" name="rating" id="rating-value" value="5">

                <div class="star-select">
                    <span class="star" data-value="1">★</span>
                    <span class="star" data-value="2">★</span>
                    <span class="star" data-value="3">★</span>
                    <span class="star" data-value="4">★</span>
                    <span class="star" data-value="5">★</span>
                </div>

                <textarea id="review-text"
                          name="comment"
                          required
                          placeholder="Nhập nhận xét của bạn..."></textarea>


                <label class="review-upload-box">
                    <!--  <span class="review-upload-icon">📷</span> -->
                    <span class="review-upload-text">Thêm ảnh đánh giá</span>

                    <input type="file"
                           id="reviewImages"
                           name="reviewImages"
                           multiple
                           accept="image/*"
                           class="review-image-input">
                </label>

                <div id="reviewImagePreview" class="review-image-preview"></div>

                <button type="submit" class="review-submit-btn">Gửi đánh giá</button>
            </form>

        </c:if>



        <section class="review-list">
            <h3>Đánh giá của khách hàng</h3>

            <div class="review-sort">
                <a class="${empty sortRating ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${product.id}">
                    Mới nhất
                </a>

                <a class="${sortRating == 'desc' ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${product.id}&sortRating=desc">
                    Từ 5 sao xuống
                </a>

                <a class="${sortRating == 'asc' ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${product.id}&sortRating=asc">
                    Từ 1 sao lên
                </a>
            </div>

            <c:if test="${empty reviews}">
                <p>Chưa có đánh giá nào.</p>
            </c:if>

            <c:forEach var="r" items="${reviews}">
                <div class="review-item">
                    <strong>${r.userName}</strong>
                    <div>
                        <c:forEach begin="1" end="${r.rating}">⭐</c:forEach>
                    </div>
                    <p>${r.comment}</p>
                    <small>${r.createdAtFormatted}</small>
                </div>
            </c:forEach>
        </section>

    </section>

    <section class="suggested-products">
        <h2>Sản phẩm phù hợp khác</h2>
        <div class="suggested-list">
            <c:forEach var="item" items="${ralatedProducts}">
                <div class="suggested-item">
                    <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${item.id}" class="link-cover">
                        <img src="${item.thumbnail}" alt="${item.name}">
                    </a>
                    <h3 class="name"><a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${item.id}">${item.name}</a></h3>
                    <fmt:setLocale value="vi_VN"/>
                    <p class="price">
                        Giá: <span class="new-price">
                        <fmt:formatNumber value="${item.sale_price}" type="number" groupingUsed="true"/>đ
                    </span>
                    </p>
                    <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${item.id}" class="btn-add">
                        Thêm vào giỏ
                    </a>
                </div>
            </c:forEach>

            <c:if test="${empty ralatedProducts}">
                <p>Không tìm thấy sản phẩm phù hợp khác.</p>
            </c:if>
        </div>
        <a href="san-pham" class="btn-view-more">Xem thêm</a>
    </section>
</main>

<script>
    const variants = [
        <c:forEach var="v" items="${variants}" varStatus="st">
        {
            id: ${v.id},
            colorId: ${v.colorId},
            sizeId: ${v.sizeId},
            stock: ${v.stock}
        }<c:if test="${!st.last}">,</c:if>
        </c:forEach>
    ];
</script>


<script>
    document.addEventListener("DOMContentLoaded", function () {
        const thumbsTrack = document.getElementById("imageThumbs");
        const prevBtn = document.getElementById("thumbPrev");
        const nextBtn = document.getElementById("thumbNext");

        if (!thumbsTrack || !prevBtn || !nextBtn) return;

        const thumbs = thumbsTrack.querySelectorAll(".thumb");
        const totalThumbs = thumbs.length;

        const visibleCount = 5;
        const thumbWidth = 70;
        const gap = 10;
        const moveSize = thumbWidth + gap;

        let currentIndex = 0;

        if (totalThumbs <= 4) {
            thumbsTrack.classList.add("few-thumbs");
            prevBtn.classList.add("hidden");
            nextBtn.classList.add("hidden");
            return;
        }

        if (totalThumbs === 5) {
            prevBtn.classList.add("hidden");
            nextBtn.classList.add("hidden");
            return;
        }

        function updateCarousel() {
            const maxIndex = totalThumbs - visibleCount;

            if (currentIndex < 0) {
                currentIndex = 0;
            }

            if (currentIndex > maxIndex) {
                currentIndex = maxIndex;
            }

            thumbsTrack.style.transform =
                "translateX(-" + (currentIndex * moveSize) + "px)";

            prevBtn.disabled = currentIndex === 0;
            nextBtn.disabled = currentIndex === maxIndex;
        }

        prevBtn.addEventListener("click", function () {
            currentIndex--;
            updateCarousel();
        });

        nextBtn.addEventListener("click", function () {
            currentIndex++;
            updateCarousel();
        });

        updateCarousel();
    });
</script>


<script>
    document.addEventListener("DOMContentLoaded", function () {
        const content = document.getElementById("productDescription");
        const wrapper = document.getElementById("productDescriptionWrapper");
        const button = document.getElementById("toggleDescriptionBtn");

        if (!content || !wrapper || !button) return;

        const collapsedHeight = 320;

        function checkDescriptionHeight() {
            content.classList.remove("expanded");
            content.classList.add("collapsed");

            if (content.scrollHeight <= collapsedHeight + 10) {
                content.classList.remove("collapsed");
                wrapper.classList.remove("has-fade");
                button.style.display = "none";
            } else {
                content.classList.add("collapsed");
                wrapper.classList.add("has-fade");
                button.style.display = "block";
                button.textContent = "Xem thêm";
            }
        }

        button.addEventListener("click", function () {
            const isExpanded = content.classList.contains("expanded");

            if (isExpanded) {
                content.classList.remove("expanded");
                content.classList.add("collapsed");
                wrapper.classList.add("has-fade");
                button.textContent = "Xem thêm";

                wrapper.scrollIntoView({
                    behavior: "smooth",
                    block: "start"
                });
            } else {
                content.classList.remove("collapsed");
                content.classList.add("expanded");
                wrapper.classList.remove("has-fade");
                button.textContent = "Thu gọn";
            }
        });

        checkDescriptionHeight();
        window.addEventListener("load", checkDescriptionHeight);
    });
</script>


<script>
    document.addEventListener("DOMContentLoaded", function () {
        const mainImage = document.getElementById("main-image");

        const lightbox = document.getElementById("imageLightbox");
        const lightboxImage = document.getElementById("lightboxImage");
        const closeBtn = document.getElementById("lightboxClose");
        const prevBtn = document.getElementById("lightboxPrev");
        const nextBtn = document.getElementById("lightboxNext");

        const thumbs = Array.from(document.querySelectorAll(".image-thumbs .thumb"));

        if (!mainImage || !lightbox || !lightboxImage || !closeBtn || !prevBtn || !nextBtn) return;

        let currentIndex = 0;

        let zoomLevel = 1;
        let translateX = 0;
        let translateY = 0;

        let isDragging = false;
        let hasDragged = false;

        let startX = 0;
        let startY = 0;
        let lastTranslateX = 0;
        let lastTranslateY = 0;

        function applyTransform() {
            lightboxImage.style.transform =
                "translate(" + translateX + "px, " + translateY + "px) scale(" + zoomLevel + ")";
        }

        function resetZoom() {
            zoomLevel = 1;
            translateX = 0;
            translateY = 0;

            lastTranslateX = 0;
            lastTranslateY = 0;

            isDragging = false;
            hasDragged = false;

            lightboxImage.classList.remove("zoomed");
            lightboxImage.classList.remove("dragging");
            lightboxImage.style.transform = "";
        }

        function zoomIn() {
            zoomLevel = 2;
            translateX = 0;
            translateY = 0;

            lastTranslateX = 0;
            lastTranslateY = 0;

            lightboxImage.classList.add("zoomed");
            applyTransform();
        }

        function zoomOut() {
            resetZoom();
        }

        function findCurrentIndexBySrc(src) {
            const index = thumbs.findIndex(function (thumb) {
                return thumb.src === src;
            });

            return index === -1 ? 0 : index;
        }

        function showImage(index) {
            if (thumbs.length === 0) {
                lightboxImage.src = mainImage.src;
                resetZoom();
                return;
            }

            if (index < 0) {
                index = thumbs.length - 1;
            }

            if (index >= thumbs.length) {
                index = 0;
            }

            currentIndex = index;
            lightboxImage.src = thumbs[currentIndex].src;

            resetZoom();
        }

        function openLightbox() {
            currentIndex = findCurrentIndexBySrc(mainImage.src);
            showImage(currentIndex);

            lightbox.classList.add("show");
            document.body.style.overflow = "hidden";
        }

        function closeLightbox() {
            lightbox.classList.remove("show");
            document.body.style.overflow = "";

            resetZoom();
        }

        mainImage.addEventListener("click", function () {
            openLightbox();
        });

        closeBtn.addEventListener("click", function (event) {
            event.stopPropagation();
            closeLightbox();
        });

        prevBtn.addEventListener("click", function (event) {
            event.stopPropagation();
            showImage(currentIndex - 1);
        });

        nextBtn.addEventListener("click", function (event) {
            event.stopPropagation();
            showImage(currentIndex + 1);
        });

        lightboxImage.addEventListener("click", function (event) {
            event.stopPropagation();

            if (hasDragged) {
                hasDragged = false;
                return;
            }

            if (zoomLevel === 1) {
                zoomIn();
            } else {
                zoomOut();
            }
        });

        lightboxImage.addEventListener("mousedown", function (event) {
            if (zoomLevel === 1) return;

            event.preventDefault();
            event.stopPropagation();

            isDragging = true;
            hasDragged = false;

            lightboxImage.classList.add("dragging");

            startX = event.clientX;
            startY = event.clientY;

            lastTranslateX = translateX;
            lastTranslateY = translateY;
        });

        document.addEventListener("mousemove", function (event) {
            if (!isDragging || zoomLevel === 1) return;

            const deltaX = event.clientX - startX;
            const deltaY = event.clientY - startY;

            if (Math.abs(deltaX) > 3 || Math.abs(deltaY) > 3) {
                hasDragged = true;
            }

            translateX = lastTranslateX + deltaX;
            translateY = lastTranslateY + deltaY;

            applyTransform();
        });

        document.addEventListener("mouseup", function () {
            if (!isDragging) return;

            isDragging = false;
            lightboxImage.classList.remove("dragging");
        });

        lightbox.addEventListener("click", function (event) {
            if (event.target === lightbox) {
                closeLightbox();
            }
        });

        document.addEventListener("keydown", function (event) {
            if (!lightbox.classList.contains("show")) return;

            if (event.key === "Escape") {
                closeLightbox();
            }

            if (event.key === "ArrowLeft") {
                showImage(currentIndex - 1);
            }

            if (event.key === "ArrowRight") {
                showImage(currentIndex + 1);
            }
        });
    });
</script>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const imageInput = document.getElementById("reviewImages");
        const previewBox = document.getElementById("reviewImagePreview");

        if (!imageInput || !previewBox) return;

        let selectedFiles = [];

        imageInput.addEventListener("change", function () {
            selectedFiles = Array.from(imageInput.files).slice(0, 5);
            renderPreview();
        });

        function renderPreview() {
            previewBox.innerHTML = "";

            selectedFiles.forEach(function (file, index) {
                if (!file.type.startsWith("image/")) return;

                const reader = new FileReader();

                reader.onload = function (e) {
                    const item = document.createElement("div");
                    item.className = "review-preview-item";

                    const img = document.createElement("img");
                    img.src = e.target.result;

                    const removeBtn = document.createElement("button");
                    removeBtn.type = "button";
                    removeBtn.className = "review-preview-remove";
                    removeBtn.textContent = "×";

                    removeBtn.addEventListener("click", function () {
                        selectedFiles.splice(index, 1);
                        updateInputFiles();
                        renderPreview();
                    });

                    item.appendChild(img);
                    item.appendChild(removeBtn);
                    previewBox.appendChild(item);
                };

                reader.readAsDataURL(file);
            });
        }

        function updateInputFiles() {
            const dataTransfer = new DataTransfer();

            selectedFiles.forEach(function (file) {
                dataTransfer.items.add(file);
            });

            imageInput.files = dataTransfer.files;
        }
    });
</script>

<%@include file="footer.jsp"%>