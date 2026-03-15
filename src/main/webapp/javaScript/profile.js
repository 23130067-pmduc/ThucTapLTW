var originalValues = {};

window.addEventListener("load", function () {
    saveOriginalValues();
    setupEvents();
});

function setupEvents() {
    var btnEdit = document.getElementById("btn-edit-profile");
    var btnCancel = document.getElementById("btn-cancel-profile");

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
    var ids = ["fullname", "phone", "email", "birthdayDisplay", "address"];
    ids.forEach(id => {
        var el = document.getElementById(id);
        if (el) el.disabled = disabled;
    });

    var genders = document.getElementsByName("gender");
    genders.forEach(g => g.disabled = disabled);
}

function saveOriginalValues() {
    originalValues = {
        fullname: document.getElementById("fullname").value,
        phone: document.getElementById("phone").value,
        email: document.getElementById("email").value,
        birthday: document.getElementById("birthdayDisplay").value,
        address: document.getElementById("address").value,
        gender: document.querySelector("input[name='gender']:checked")?.value
    };
}

function cancelEdit() {
    document.getElementById("fullname").value = originalValues.fullname;
    document.getElementById("phone").value = originalValues.phone;
    document.getElementById("email").value = originalValues.email;
    document.getElementById("birthdayDisplay").value = originalValues.birthday;
    document.getElementById("address").value = originalValues.address;

    var genders = document.getElementsByName("gender");
    genders.forEach(g => g.checked = g.value === originalValues.gender);

    disableEditMode();
}

function syncBirthday() {
    var display = document.getElementById("birthdayDisplay").value; // dd-MM-yyyy
    if (!display) return;

    var parts = display.includes("/")
        ? display.split("/")
        : display.split("-");

    if (parts.length !== 3) return;

    var day = parts[0].padStart(2, "0");
    var month = parts[1].padStart(2, "0");
    var year = parts[2];

    document.getElementById("birthday").value =
        year + "-" + month + "-" + day;
}