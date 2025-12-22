import {lockBtn, unlockBtn} from "./button-state.js";
import {
    handleValidation, validatePasswordStrength, validateEmailDomain, validateNumInputs
} from "./validation.js";
import {showMessage} from "./notification.js";
import {rateLimited} from "./http-responses.js";

const emailReset = document.querySelector("#reset-email");
const verificationCode = document.querySelector("#verification-code");
const newPassword = document.querySelector("#reset-password");
const confirmNewPassword = document.querySelector("#reset-confirm-password");
const resetPasswordBtn = document.querySelector("#reset-btn");

const STEPS = {
    EMAIL: 1, CODE: 2, NEW_PASSWORD: 3
};
let currentStep = STEPS.EMAIL;

resetPasswordBtn.addEventListener("click", (e) => {
    e.preventDefault();
    if (currentStep === STEPS.EMAIL) return handleEmailStep();
    if (currentStep === STEPS.CODE) return handleCodeStep();
    if (currentStep === STEPS.NEW_PASSWORD) return handleNewPasswordStep();
});

function goToStep(step) {
    currentStep = step;
    emailReset.classList.toggle("hidden", step !== STEPS.EMAIL);
    verificationCode.classList.toggle("hidden", step !== STEPS.CODE);
    newPassword.classList.toggle("hidden", step !== STEPS.NEW_PASSWORD);
    confirmNewPassword.classList.toggle("hidden", step !== STEPS.NEW_PASSWORD);
}

// sends email over to backend to gen veri code and save it
async function handleEmailStep() {
    lockBtn(resetPasswordBtn, "Sending...");
    try {
        const email = emailReset.value;

        if (!(handleValidation("EMAIL", email, "email"))) {
            return;
        }

        const emailDomainValidationStatus = validateEmailDomain(email);
        if (emailDomainValidationStatus !== "VALID") {
            showMessage("error", `${emailDomainValidationStatus}`);
            return;
        }
        const emailResetResponse = await fetch("/password/reset", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email: email})
        });

        if (!emailResetResponse.ok) {
            await handleError(emailResetResponse);
            rateLimited(emailResetResponse);
            return;
        }
        // save user email in local storage to grab it later
        localStorage.setItem("userEmail", email);
        showMessage("success", "Code has been sent, please check your e-mail address");

        goToStep(STEPS.CODE);
    } catch (err) {
        console.log(err);
        showMessage("error", "Could not send verification code. Please try again.");
    } finally {
        unlockBtn(resetPasswordBtn);
    }
}

// sends code to compare
async function handleCodeStep() {
    lockBtn(resetPasswordBtn, "Verifying...");
    try {
        const code = verificationCode.value;
        const email = localStorage.getItem("userEmail");

        if (!(validateNumInputs(code)) || code.length !== 6) {
            showMessage("error", "Invalid code. Please try again.");
            return;
        }
        const codeCompareResponse = await fetch("/password/reset/verify", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email: email, code: code})
        });

        if (!codeCompareResponse.ok) {
            await handleError(codeCompareResponse);
            rateLimited(codeCompareResponse);
            return;
        }
        const data = await codeCompareResponse.json();

        verificationCode.value = "";
        localStorage.setItem("resetToken", data.resetToken);
        showMessage("success", "Verification successful. You have 10 minutes to reset your password.");

        goToStep(STEPS.NEW_PASSWORD);
    } catch (err) {
        showMessage("Something went wrong. Please try again.");
    } finally {
        unlockBtn(resetPasswordBtn);
    }
}


async function handleNewPasswordStep() {
    lockBtn(resetPasswordBtn, "Updating...");
    try {
        const password = newPassword.value;
        const confirmPassword = confirmNewPassword.value;

        // check if input is right
        if (!handleValidation("PASSWORD", password, "password")) {
            return;
        }
        // check if input is right
        if (!handleValidation("PASSWORD", confirmPassword, "password")) {
            return;
        }

        // check if pass strength passes
        const passwordStrengthStatus = validatePasswordStrength(password);
        if (passwordStrengthStatus !== "VALID") {
            showMessage("error", `${passwordStrengthStatus}`);
            return;
        }

        if (!(password === confirmPassword)) {
            showMessage("error", "Passwords do not match. Please double check.");
            return;
        }

        const email = localStorage.getItem("userEmail");
        const resetToken = localStorage.getItem("resetToken");

        const changePasswordResponse = await fetch("/password/reset/change", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email: email, password: password, resetToken: resetToken})
        });

        if (!changePasswordResponse.ok) {
            await handleError(changePasswordResponse);
            rateLimited(changePasswordResponse);
            return;
        }

        // save message to display later
        localStorage.setItem("flashMessage", "Password successfully changed");

        localStorage.removeItem("userEmail");
        localStorage.removeItem("resetToken");
        window.location.href = "/login";
    } catch (err) {
        showMessage("error", "Could not change password. Please try again.");
    } finally {
        unlockBtn(resetPasswordBtn);
    }
}

async function handleError(res) {
    const error = await res.json();
    showMessage("error", error.message);
}



