(function () {
    const modeInput = document.querySelector('input[name="mode"]');
    const mode = modeInput ? modeInput.value : '';

    if (mode === 'view') {
        document.querySelectorAll('input:not([readonly]), select, textarea, button[type="submit"]').forEach(function (el) {
            el.style.pointerEvents = 'none';
            el.style.background = '#f2f2f2';
        });
    }

    if (mode !== 'add' && mode !== 'edit') return;

    const form       = document.querySelector('form');
    const nameInput  = document.getElementById('name');
    const emailInput = document.getElementById('email');
    const phoneInput = document.getElementById('phone');
    const msgInput   = document.getElementById('message');
    const btnSave    = document.getElementById('btnSave');

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phoneRegex = /^(0|\+84)(3[2-9]|5[6-9]|7[0|6-9]|8[0-9]|9[0-9])[0-9]{7}$/;

    function showError(id, msg) {
        const el = document.getElementById(id);
        if (el) { el.textContent = msg; el.style.display = 'block'; }
    }
    function clearError(id) {
        const el = document.getElementById(id);
        if (el) { el.textContent = ''; el.style.display = 'none'; }
    }
    function markInvalid(input) { input.classList.add('input-error'); }
    function markValid(input)   { input.classList.remove('input-error'); }

    nameInput.addEventListener('input', function () {
        if (nameInput.value.trim().length < 2) { showError('nameError', 'Tên phải có ít nhất 2 ký tự'); markInvalid(nameInput); }
        else { clearError('nameError'); markValid(nameInput); }
    });
    emailInput.addEventListener('input', function () {
        if (!emailRegex.test(emailInput.value.trim())) { showError('emailError', 'Email không đúng định dạng (vd: abc@gmail.com)'); markInvalid(emailInput); }
        else { clearError('emailError'); markValid(emailInput); }
    });
    phoneInput.addEventListener('input', function () {
        if (!phoneRegex.test(phoneInput.value.trim())) { showError('phoneError', 'Số điện thoại không hợp lệ (vd: 0901234567)'); markInvalid(phoneInput); }
        else { clearError('phoneError'); markValid(phoneInput); }
    });
    if (msgInput && !msgInput.readOnly) {
        msgInput.addEventListener('input', function () {
            if (msgInput.value.trim().length < 5) { showError('messageError', 'Nội dung phải có ít nhất 5 ký tự'); markInvalid(msgInput); }
            else { clearError('messageError'); markValid(msgInput); }
        });
    }

    form.addEventListener('submit', function (e) {
        let valid = true;

        if (nameInput.value.trim().length < 2) {
            showError('nameError', 'Tên phải có ít nhất 2 ký tự'); markInvalid(nameInput); valid = false;
        }
        if (!emailRegex.test(emailInput.value.trim())) {
            showError('emailError', 'Email không đúng định dạng (vd: abc@gmail.com)'); markInvalid(emailInput); valid = false;
        }
        if (!phoneRegex.test(phoneInput.value.trim())) {
            showError('phoneError', 'Số điện thoại không hợp lệ (vd: 0901234567)'); markInvalid(phoneInput); valid = false;
        }
        if (msgInput && !msgInput.readOnly && msgInput.value.trim().length < 5) {
            showError('messageError', 'Nội dung phải có ít nhất 5 ký tự'); markInvalid(msgInput); valid = false;
        }

        if (!valid) { e.preventDefault(); return; }

        btnSave.disabled = true;
        btnSave.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Đang lưu...';
    });
}());
