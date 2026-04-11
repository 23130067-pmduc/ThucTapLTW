let originalValues = {};

window.addEventListener("load", function () {
    saveOriginalValues();
    setupEvents();
});

function setupEvents() {
    const btnEdit = document.getElementById("btn-edit-profile");
    const btnCancel = document.getElementById("btn-cancel-profile");

    if (btnEdit) btnEdit.onclick = enableEditMode;
    if (btnCancel) btnCancel.onclick = cancelEdit;
}

function enableEditMode() {
    toggleInputs(false);

    document.getElementById("btn-edit-profile").style.display = "none";
    document.getElementById("btn-save-profile").style.display = "inline-block";
    document.getElementById("btn-cancel-profile").style.display = "inline-block";
}

function disableEditMode() {
    toggleInputs(true);

    document.getElementById("btn-edit-profile").style.display = "inline-block";
    document.getElementById("btn-save-profile").style.display = "none";
    document.getElementById("btn-cancel-profile").style.display = "none";
}

function toggleInputs(disabled) {
    const ids = ["fullname", "phone", "email"];
    ids.forEach(id => {
        var el = document.getElementById(id);
        if (el) el.disabled = disabled;
    });

    const genders = document.getElementsByName("gender");
    genders.forEach(g => g.disabled = disabled);
}

function saveOriginalValues() {
    originalValues = {
        fullname: document.getElementById("fullname").value,
        phone: document.getElementById("phone").value,
        email: document.getElementById("email").value,
        gender: document.querySelector("input[name='gender']:checked")?.value
    };
}

function cancelEdit() {
    document.getElementById("fullname").value = originalValues.fullname;
    document.getElementById("phone").value = originalValues.phone;
    document.getElementById("email").value = originalValues.email;

    const genders = document.getElementsByName("gender");
    genders.forEach(g => g.checked = g.value === originalValues.gender);

    disableEditMode();
}

