(() => {
    "use strict";

    const reduceMotionQuery = window.matchMedia("(prefers-reduced-motion: reduce)");
    const reduceMotion = reduceMotionQuery.matches;
    const canAnimate = typeof Element !== "undefined" && "animate" in Element.prototype;
    const activeAnimations = new Set();

    document.body.dataset.motion = reduceMotion ? "off" : canAnimate ? "on" : "fallback";

    const play = (element, keyframes, options = {}) => {
        if (!element || reduceMotion || !canAnimate) {
            return null;
        }

        const animation = element.animate(keyframes, {
            duration: 620,
            easing: "cubic-bezier(0.16, 1, 0.3, 1)",
            fill: "both",
            ...options
        });

        activeAnimations.add(animation);
        const cleanup = () => activeAnimations.delete(animation);
        animation.addEventListener("finish", cleanup, { once: true });
        animation.addEventListener("cancel", cleanup, { once: true });
        return animation;
    };

    const initReveals = () => {
        const revealItems = [...document.querySelectorAll("[data-reveal]")];
        revealItems.forEach((item, index) => {
            item.style.setProperty("--reveal-delay", `${Math.min(index * 55, 280)}ms`);
        });

        const reveal = (item, index) => {
            item.classList.add("is-visible");
            const preservesPointerTransform = item.matches(".wallpaper-card, .hero-art, .dashboard-art, .upload-visual");
            const keyframes = preservesPointerTransform
                ? [{ opacity: 0 }, { opacity: 1 }]
                : [
                    { opacity: 0, transform: "translateY(18px)" },
                    { opacity: 1, transform: "translateY(0)" }
                ];

            play(item, keyframes, {
                delay: Math.min(index * 55, 280),
                duration: 680
            });
        };

        if (reduceMotion || !canAnimate || !("IntersectionObserver" in window)) {
            revealItems.forEach((item) => item.classList.add("is-visible"));
            return;
        }

        const observer = new IntersectionObserver((entries, instance) => {
            entries.forEach((entry) => {
                if (!entry.isIntersecting) {
                    return;
                }

                reveal(entry.target, revealItems.indexOf(entry.target));
                instance.unobserve(entry.target);
            });
        }, { threshold: 0.12, rootMargin: "0px 0px -40px" });

        revealItems.forEach((item) => observer.observe(item));
    };

    const initVisualEntrances = () => {
        const images = document.querySelectorAll(".hero-art img, .dashboard-art img, .upload-visual img, .auth-art img");
        images.forEach((image, index) => {
            const animation = play(image, [
                { opacity: 0.25, transform: "scale(1.045)" },
                { opacity: 1, transform: "scale(1)" }
            ], {
                delay: 180 + index * 70,
                duration: 1100
            });
            animation?.addEventListener("finish", () => animation.cancel(), { once: true });
        });
    };

    const initPointerDepth = () => {
        if (reduceMotion || !window.matchMedia("(pointer: fine)").matches) {
            return;
        }

        const targets = document.querySelectorAll(".wallpaper-card, .hero-art, .dashboard-art, .upload-visual");
        targets.forEach((target) => {
            const reset = () => {
                target.classList.remove("is-pointer-active");
                target.style.setProperty("--pointer-x", "50%");
                target.style.setProperty("--pointer-y", "50%");
                target.style.setProperty("--tilt-x", "0deg");
                target.style.setProperty("--tilt-y", "0deg");
            };

            target.addEventListener("pointerenter", (event) => {
                if (event.pointerType !== "touch") {
                    target.classList.add("is-pointer-active");
                }
            });

            target.addEventListener("pointermove", (event) => {
                if (event.pointerType === "touch") {
                    return;
                }

                const bounds = target.getBoundingClientRect();
                const x = (event.clientX - bounds.left) / bounds.width;
                const y = (event.clientY - bounds.top) / bounds.height;
                const intensity = target.classList.contains("wallpaper-card") ? 2.8 : 2.1;

                target.style.setProperty("--pointer-x", `${Math.max(0, Math.min(1, x)) * 100}%`);
                target.style.setProperty("--pointer-y", `${Math.max(0, Math.min(1, y)) * 100}%`);
                target.style.setProperty("--tilt-x", `${((0.5 - y) * intensity).toFixed(2)}deg`);
                target.style.setProperty("--tilt-y", `${((x - 0.5) * intensity).toFixed(2)}deg`);
            });

            target.addEventListener("pointerleave", reset);
        });
    };

    const initBatchSelection = () => {
        const batchForm = document.querySelector("#batchForm");
        if (!batchForm) {
            return;
        }

        const checkboxes = [...document.querySelectorAll('input[name="wallpaperIds"]')];
        const status = batchForm.querySelector("[data-selection-status]");
        const controls = batchForm.querySelectorAll("select, button");

        const updateSelection = (changedCheckbox) => {
            const selected = checkboxes.filter((checkbox) => checkbox.checked);
            const hasSelection = selected.length > 0;

            checkboxes.forEach((checkbox) => {
                const card = checkbox.closest(".wallpaper-card");
                card?.classList.toggle("is-selected", checkbox.checked);
            });

            batchForm.classList.toggle("has-selection", hasSelection);
            controls.forEach((control) => {
                control.disabled = !hasSelection;
            });

            if (status) {
                status.textContent = hasSelection ? `已选择 ${selected.length} 张壁纸` : "选择图片后可批量整理";
            }

            if (changedCheckbox) {
                play(changedCheckbox.closest(".select-box"), [
                    { transform: "scale(0.78)" },
                    { transform: "scale(1.08)" },
                    { transform: "scale(1)" }
                ], { duration: 360, easing: "cubic-bezier(0.16, 1, 0.3, 1)" });
            }
        };

        checkboxes.forEach((checkbox) => {
            checkbox.addEventListener("change", () => updateSelection(checkbox));
        });
        updateSelection();
    };

    const formatFileSize = (bytes) => {
        if (bytes < 1024 * 1024) {
            return `${Math.max(1, Math.round(bytes / 1024))} KB`;
        }
        return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
    };

    const initFileDrop = () => {
        const fileInput = document.querySelector("#wallpaperFile");
        const fileDrop = document.querySelector(".file-drop");
        const filePreview = document.querySelector("#filePreview");
        const fileName = document.querySelector("#fileName");
        if (!fileInput || !fileDrop || !filePreview || !fileName) {
            return;
        }

        let previewUrl = "";

        const showFile = (file) => {
            if (!file || !file.type.startsWith("image/")) {
                fileInput.value = "";
                fileDrop.classList.remove("is-selected");
                fileDrop.classList.add("has-error");
                fileName.textContent = "请选择图片文件";
                filePreview.hidden = true;
                return;
            }

            if (previewUrl) {
                URL.revokeObjectURL(previewUrl);
            }
            previewUrl = URL.createObjectURL(file);
            filePreview.src = previewUrl;
            filePreview.hidden = false;
            fileName.textContent = `${file.name} / ${formatFileSize(file.size)}`;
            fileDrop.classList.remove("has-error");
            fileDrop.classList.add("is-selected");
            play(filePreview, [
                { opacity: 0, transform: "scale(1.03)" },
                { opacity: 0.32, transform: "scale(1)" }
            ], { duration: 420 });
        };

        const assignDroppedFile = (file) => {
            try {
                if (typeof DataTransfer !== "undefined") {
                    const transfer = new DataTransfer();
                    transfer.items.add(file);
                    fileInput.files = transfer.files;
                }
            } catch (error) {
                // The preview still helps the user when a browser blocks programmatic file assignment.
            }
            showFile(file);
        };

        fileInput.addEventListener("change", () => showFile(fileInput.files[0]));
        ["dragenter", "dragover"].forEach((eventName) => {
            fileDrop.addEventListener(eventName, (event) => {
                event.preventDefault();
                fileDrop.classList.add("is-dragging");
            });
        });
        fileDrop.addEventListener("dragleave", (event) => {
            if (!event.relatedTarget || !fileDrop.contains(event.relatedTarget)) {
                fileDrop.classList.remove("is-dragging");
            }
        });
        fileDrop.addEventListener("drop", (event) => {
            event.preventDefault();
            fileDrop.classList.remove("is-dragging");
            assignDroppedFile(event.dataTransfer.files[0]);
        });
        window.addEventListener("pagehide", () => {
            if (previewUrl) {
                URL.revokeObjectURL(previewUrl);
            }
        }, { once: true });
    };

    const initSubmitStates = () => {
        document.querySelectorAll("form[data-submit-state]").forEach((form) => {
            form.addEventListener("submit", () => {
                const submitButton = form.querySelector('button[type="submit"]');
                if (!submitButton || form.dataset.submitting === "true") {
                    return;
                }

                form.dataset.submitting = "true";
                submitButton.disabled = true;
                submitButton.classList.add("is-loading");
                submitButton.setAttribute("aria-busy", "true");
                submitButton.textContent = form.dataset.submittingLabel || "正在提交...";
            });
        });
    };

    initReveals();
    initVisualEntrances();
    initPointerDepth();
    initBatchSelection();
    initFileDrop();
    initSubmitStates();

    window.addEventListener("pagehide", () => {
        activeAnimations.forEach((animation) => animation.cancel());
        activeAnimations.clear();
    }, { once: true });
})();
