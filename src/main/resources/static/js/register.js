const form = document.querySelector("#registerForm");
const passwordInput = document.querySelector("#password");
const passwordMeter = document.querySelector("#passwordMeter");
const passwordHint = document.querySelector("#passwordHint");

const updatePasswordMeter = () => {
    if (!passwordInput || !passwordMeter || !passwordHint) {
        return;
    }

    const value = passwordInput.value;
    let score = 0;
    if (value.length >= 6) score += 1;
    if (value.length >= 10) score += 1;
    if (/[a-z]/i.test(value) && /\d/.test(value)) score += 1;
    if (/[^a-z\d]/i.test(value)) score += 1;

    const level = score >= 3 ? "strong" : score >= 2 ? "medium" : "weak";
    passwordMeter.dataset.level = level;
    passwordMeter.querySelector(".password-meter-track > span").style.setProperty("--password-progress", Math.min(1, score / 4));
    passwordHint.textContent = !value
        ? "建议使用 6 位以上并混合字母与数字"
        : score >= 3
            ? "密码强度不错，可以继续使用"
            : score >= 2
                ? "再增加长度或混合字符会更稳妥"
                : "密码至少需要 6 位";
};

passwordInput?.addEventListener("input", updatePasswordMeter);
updatePasswordMeter();

if (form) {
    form.addEventListener("submit", (event) => {
        const password = passwordInput.value;
        const confirmPassword = document.querySelector("#confirmPassword").value;
        if (password.length < 6) {
            event.preventDefault();
            alert("密码至少需要 6 位");
            return;
        }
        if (password !== confirmPassword) {
            event.preventDefault();
            alert("两次输入的密码不一致");
        }
    });
}
