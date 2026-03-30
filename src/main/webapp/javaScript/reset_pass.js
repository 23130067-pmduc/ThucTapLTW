const passwordInput = document.getElementById("password");
const submitBtn = document.getElementById("submitBtn");

const rules = {
    lower: document.getElementById("rule-lower"),
    upper: document.getElementById("rule-upper"),
    length: document.getElementById("rule-length"),
    digit: document.getElementById("rule-digit"),
    special: document.getElementById("rule-special"),
};

passwordInput.addEventListener("input", () => {
    const value = passwordInput.value;

    const checks = {
        lower: /[a-z]/.test(value),
        upper: /[A-Z]/.test(value),
        digit: /\d/.test(value),
        special: /[^A-Za-z0-9]/.test(value),
        length: value.length >= 8 && value.length <= 16,
    };

    let allValid = true;

    for (let key in checks) {
        if (checks[key]) {
            rules[key].classList.add("valid");
            rules[key].classList.remove("invalid");
        } else {
            rules[key].classList.add("invalid");
            rules[key].classList.remove("valid");
            allValid = false;
        }
    }

    submitBtn.disabled = !allValid;
});
