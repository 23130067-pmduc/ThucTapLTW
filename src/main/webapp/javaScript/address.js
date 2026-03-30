const LOCATION_DATA = {
    "Hồ Chí Minh": {
        districts: {
            "Quận 1": ["Bến Nghé", "Bến Thành", "Đa Kao", "Tân Định"],
            "Quận 3": ["Phường 1", "Phường 2", "Phường 3"],
            "Quận 5": ["Phường 1", "Phường 2", "Phường 3", "Phường 5"],
            "Quận 7": ["Tân Phong", "Tân Phú", "Phú Mỹ"],
            "Thành phố Thủ Đức": ["Linh Trung", "Linh Xuân", "Hiệp Bình Chánh"]
        }
    },
    "Hà Nội": {
        districts: {
            "Ba Đình": ["Phúc Xá", "Trúc Bạch", "Ngọc Hà"],
            "Đống Đa": ["Láng Hạ", "Khâm Thiên"],
            "Cầu Giấy": ["Dịch Vọng", "Mai Dịch"]
        }
    },
    "Bình Dương": {
        districts: {
            "Thủ Dầu Một": ["Phú Cường", "Phú Hòa"],
            "Dĩ An": ["Dĩ An", "Tân Đông Hiệp"],
            "Thuận An": ["Lái Thiêu", "An Phú"]
        }
    }
};

let modal;
let form;
let citySelect;
let districtSelect;
let wardSelect;
let phoneInput;
let phoneError;

window.addEventListener("DOMContentLoaded", () => {
    modal = document.getElementById("addressModal");
    form = document.getElementById("addressForm");
    citySelect = document.getElementById("citySelect");
    districtSelect = document.getElementById("districtSelect");
    wardSelect = document.getElementById("wardSelect");
    phoneInput = document.getElementById("phoneInput");
    phoneError = document.getElementById("phoneError");

    bindEvents();
});

function bindEvents() {
    document.getElementById("btnOpenModal")?.addEventListener("click", openAddModal);
    document.getElementById("btnCloseModal")?.addEventListener("click", closeModal);
    document.getElementById("btnCancelModal")?.addEventListener("click", closeModal);

    modal?.addEventListener("click", (event) => {
        if (event.target === modal) {
            closeModal();
        }
    });

    citySelect?.addEventListener("change", () => {
        populateDistricts(citySelect.value);
        resetWardOptions();
    });

    districtSelect?.addEventListener("change", () => {
        populateWards(citySelect.value, districtSelect.value);
    });

    phoneInput?.addEventListener("input", validatePhoneRealtime);
    phoneInput?.addEventListener("blur", validatePhoneRealtime);

    form?.addEventListener("submit", (event) => {
        if (!isValidPhone(phoneInput.value.trim())) {
            showPhoneError("Số điện thoại không hợp lệ");
            event.preventDefault();
        }
    });

    document.querySelectorAll(".js-edit-address").forEach((button) => {
        button.addEventListener("click", () => openEditModal(button.dataset));
    });
}

function openAddModal() {
    resetForm();
    document.getElementById("modalTitle").textContent = "Thêm địa chỉ mới";
    document.getElementById("formAction").value = "add";
    document.getElementById("submitButton").textContent = "Lưu địa chỉ";
    modal.classList.add("active");
}

function openEditModal(data) {
    resetForm();

    document.getElementById("modalTitle").textContent = "Cập nhật địa chỉ";
    document.getElementById("formAction").value = "update";
    document.getElementById("addressId").value = data.id || "";
    document.getElementById("receiverName").value = data.receiverName || "";
    phoneInput.value = data.phone || "";
    document.getElementById("detailAddress").value = data.detailAddress || "";
    document.getElementById("isDefault").checked = data.defaultAddress === "true";
    document.getElementById("submitButton").textContent = "Lưu thay đổi";

    citySelect.value = data.city || "";
    populateDistricts(data.city || "", data.district || "");
    populateWards(data.city || "", data.district || "", data.ward || "");

    modal.classList.add("active");
}

function closeModal() {
    modal.classList.remove("active");
}

function resetForm() {
    form.reset();
    document.getElementById("addressId").value = "";
    document.getElementById("formAction").value = "add";
    hidePhoneError();
    resetDistrictOptions();
    resetWardOptions();
}

function populateDistricts(city, selectedDistrict = "") {
    resetDistrictOptions();
    resetWardOptions();

    if (!LOCATION_DATA[city]) {
        return;
    }

    Object.keys(LOCATION_DATA[city].districts).forEach((district) => {
        const option = document.createElement("option");
        option.value = district;
        option.textContent = district;
        districtSelect.appendChild(option);
    });

    districtSelect.disabled = false;
    districtSelect.value = selectedDistrict;
}

function populateWards(city, district, selectedWard = "") {
    resetWardOptions();

    if (!LOCATION_DATA[city] || !LOCATION_DATA[city].districts[district]) {
        return;
    }

    LOCATION_DATA[city].districts[district].forEach((ward) => {
        const option = document.createElement("option");
        option.value = ward;
        option.textContent = ward;
        wardSelect.appendChild(option);
    });

    wardSelect.disabled = false;
    wardSelect.value = selectedWard;
}

function resetDistrictOptions() {
    districtSelect.innerHTML = '<option value="">-- Chọn Quận / Huyện --</option>';
    districtSelect.disabled = true;
}

function resetWardOptions() {
    wardSelect.innerHTML = '<option value="">-- Chọn Phường / Xã --</option>';
    wardSelect.disabled = true;
}

function isValidPhone(phone) {
    const normalizedPhone = phone.replace(/\s|\.|-/g, "");
    return /^(0[3|5|7|8|9])[0-9]{8}$/.test(normalizedPhone);
}

function validatePhoneRealtime() {
    const phone = phoneInput.value.trim();

    if (!phone) {
        hidePhoneError();
        return;
    }

    if (!isValidPhone(phone)) {
        showPhoneError("Số điện thoại phải đúng định dạng, ví dụ: 090xxxxxxx");
        return;
    }

    hidePhoneError();
}

function showPhoneError(message) {
    phoneError.textContent = message;
    phoneError.style.display = "block";
    phoneInput.classList.add("input-error");
}

function hidePhoneError() {
    phoneError.textContent = "";
    phoneError.style.display = "none";
    phoneInput.classList.remove("input-error");
}
document.addEventListener("DOMContentLoaded", function () {
    const deleteModal = document.getElementById("deleteConfirmModal");
    const cancelDeleteBtn = document.getElementById("cancelDeleteBtn");
    const confirmDeleteBtn = document.getElementById("confirmDeleteBtn");
    const deleteButtons = document.querySelectorAll(".btn-open-delete-modal");

    let currentDeleteForm = null;

    deleteButtons.forEach((button) => {
        button.addEventListener("click", function () {
            currentDeleteForm = this.closest("form");
            if (deleteModal) {
                deleteModal.classList.add("show");
            }
        });
    });

    if (cancelDeleteBtn) {
        cancelDeleteBtn.addEventListener("click", function () {
            if (deleteModal) {
                deleteModal.classList.remove("show");
            }
            currentDeleteForm = null;
        });
    }

    if (confirmDeleteBtn) {
        confirmDeleteBtn.addEventListener("click", function () {
            if (currentDeleteForm) {
                currentDeleteForm.submit();
            }
        });
    }

    if (deleteModal) {
        deleteModal.addEventListener("click", function (e) {
            if (e.target === deleteModal) {
                deleteModal.classList.remove("show");
                currentDeleteForm = null;
            }
        });
    }
});