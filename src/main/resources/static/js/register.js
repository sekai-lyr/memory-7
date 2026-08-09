const form = document.querySelector("#registerForm");

if (form) {
    form.addEventListener("submit", (event) => {
        const password = document.querySelector("#password").value;
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
