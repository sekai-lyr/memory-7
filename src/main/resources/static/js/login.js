const toggle = document.querySelector("#togglePassword");
const password = document.querySelector("#password");

if (toggle && password) {
    toggle.addEventListener("click", () => {
        const visible = password.type === "text";
        password.type = visible ? "password" : "text";
        toggle.textContent = visible ? "显示" : "隐藏";
    });
}
