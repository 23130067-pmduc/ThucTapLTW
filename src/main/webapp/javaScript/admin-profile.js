const originalValues = {};

document.addEventListener("DOMContentLoaded", function () {
    saveOriginalValues();
    setupProfileEvents();
});

function setupProfileEvents() {
    const editButton = document.getElementById("btn-edit-profile");
    const cancelButton = document.getElementById("btn-cancel-profile");

    if (editButton) {
        editButton.addEventListener("click", enableEditMode);
    }

    if (cancelButton) {
        cancelButton.addEventListener("click", cancelEditMode);
    }
}

function enableEditMode() {
    toggleEditableFields(false);

    document.getElementById("btn-edit-profile").style.display = "none";
    document.getElementById("btn-save-profile").style.display = "inline-block";
    document.getElementById("btn-cancel-profile").style.display = "inline-block";
}

function disableEditMode() {
    toggleEditableFields(true);

    document.getElementById("btn-edit-profile").style.display = "inline-flex";
    document.getElementById("btn-save-profile").style.display = "none";
    document.getElementById("btn-cancel-profile").style.display = "none";
}

function toggleEditableFields(disabled) {
    const editableIds = ["fullname", "email", "phone"];

    editableIds.forEach(function (id) {
        const element = document.getElementById(id);
        if (element) {
            element.disabled = disabled;
        }
    });

    const genderInputs = document.querySelectorAll("input[name='gender']");
    genderInputs.forEach(function (input) {
        input.disabled = disabled;
    });

    const genderGroup = document.querySelector(".gender-group");
    if (genderGroup) {
        genderGroup.style.background = disabled ? "#f4f5f7" : "#fff";
    }
}

function saveOriginalValues() {
    originalValues.fullname = document.getElementById("fullname") ? document.getElementById("fullname").value : "";
    originalValues.email = document.getElementById("email") ? document.getElementById("email").value : "";
    originalValues.phone = document.getElementById("phone") ? document.getElementById("phone").value : "";

    const checkedGender = document.querySelector("input[name='gender']:checked");
    originalValues.gender = checkedGender ? checkedGender.value : "";
}

function cancelEditMode() {
    if (document.getElementById("fullname")) {
        document.getElementById("fullname").value = originalValues.fullname;
    }

    if (document.getElementById("email")) {
        document.getElementById("email").value = originalValues.email;
    }

    if (document.getElementById("phone")) {
        document.getElementById("phone").value = originalValues.phone;
    }

    const genderInputs = document.querySelectorAll("input[name='gender']");
    genderInputs.forEach(function (input) {
        input.checked = input.value === originalValues.gender;
    });

    disableEditMode();
}