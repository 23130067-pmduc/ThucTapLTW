document.addEventListener("DOMContentLoaded", function () {

    const inputs = document.querySelectorAll('.o');
    if (inputs.length > 0) inputs[0].focus();

    inputs.forEach((input, index) => {
        input.addEventListener('input', () => {
            input.value = input.value.replace(/[^0-9]/g, '');
            if (input.value && index < inputs.length - 1) {
                inputs[index + 1].focus();
            }
        });

        input.addEventListener('keydown', (e) => {
            if (e.key === 'Backspace' && !input.value && index > 0) {
                inputs[index - 1].focus();
            }
        });
    });

    const resendLink = document.getElementById("resendLink");
    const resendText = document.getElementById("resendText");
    const countdownBox = document.getElementById("countdown");
    const timeSpan = document.getElementById("time");

    let seconds = 60;
    let timer = null;

    resendLink.addEventListener("click", function (e) {
        e.preventDefault();

        if (timer) return;

        const email = document.querySelector("input[name='email']").value;

        fetch("otp", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "action=resend&email=" + encodeURIComponent(email)
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    startCountdown();
                } else {
                    alert(data.message || "Không thể gửi lại OTP");
                }
            });
    });


    function startCountdown() {
        resendText.style.display = "none";
        countdownBox.style.display = "inline";
        seconds = 60;
        timeSpan.innerText = seconds;

        timer = setInterval(() => {
            seconds--;
            timeSpan.innerText = seconds;

            if (seconds <= 0) {
                clearInterval(timer);
                countdownBox.style.display = "none";
                resendText.style.display = "inline";
            }
        }, 1000);
    }
});


function joinOtp() {
    let otp = '';
    document.querySelectorAll('.o').forEach(i => otp += i.value);

    if (otp.length !== 6) {
        alert("Vui lòng nhập đủ 6 số OTP");
        return false;
    }

    document.getElementById('otp').value = otp;
    return true;
}
