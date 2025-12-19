import {disableBtn, enableBtn} from "./button-state.js";
import {
    handleValidation, validatePasswordStrength, validateEmailDomain, showMessage, validateNumInputs
} from "./validation.js";


const emailReset = document.querySelector("#email-reset");
const verificationCode = document.querySelector("#verification-code");
const newPassword = document.querySelector("#new-password");
const confirmNewPassword = document.querySelector("#re-enter-password");
const resetPasswordContinueBtn = document.querySelector("#resetPasswordContinueBtn");





const STEPS = {
    EMAIL: 1, CODE: 2, NEW_PASSWORD: 3
};
let currentStep = STEPS.EMAIL;

resetPasswordContinueBtn.addEventListener("click", (e) => {
    e.preventDefault();

    if (currentStep === STEPS.EMAIL) {
        verificationCode.classList.add('hidden');
        newPassword.classList.add('hidden');
        confirmNewPassword.classList.add('hidden');
        handleEmailStep();
    }

    if (currentStep === STEPS.CODE) {
        handleCodeStep();
    }

    if (currentStep === STEPS.NEW_PASSWORD) {
        handleNewPasswordStep();
    }
});

// sends email over to backend to gen verification code and save it
async function handleEmailStep() {
    // disable btn so user doesnt spam click
    disableBtn(resetPasswordContinueBtn);
    resetPasswordContinueBtn.textContent = "Sending...";

    const email = emailReset.value;
    if (!(handleValidation("EMAIL", email, "email"))){
        enableBtn(resetPasswordContinueBtn);
        resetPasswordContinueBtn.textContent = "Continue";
        return;
    }

    const emailDomainValidationStatus = validateEmailDomain(email);
    if (emailDomainValidationStatus !== "VALID") {
        enableBtn(resetPasswordContinueBtn);
        resetPasswordContinueBtn.textContent = "Continue";
        showMessage("error", `${emailDomainValidationStatus}`);
        return;
    }

    try {
        const emailResetResponse = await fetch("/password-reset", {
            method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify({email: email})
        });

        if (!emailResetResponse.ok) {
            const emailResetRes = await emailResetResponse.json();
            showMessage("error", emailResetRes.message);
            enableBtn(resetPasswordContinueBtn);
            resetPasswordContinueBtn.textContent = "Continue";
            return;
        }

        // means code is generated and saved in db
        // proceed to next step
        currentStep = STEPS.CODE;

        // update ui
        emailReset.classList.add('hidden');
        verificationCode.classList.remove('hidden');

        // save user email in local storage to grab it later
        localStorage.setItem("userEmail", email);
        showMessage("success", "Code has been sent, please check your e-mail address");

        // re-enable btn
        enableBtn(resetPasswordContinueBtn);
        resetPasswordContinueBtn.textContent = "Continue";

    } catch (err) {
        showMessage("error", "Could not send verification code. Please try again.");
    }
}


// sends code to compare
async function handleCodeStep() {
    disableBtn(resetPasswordContinueBtn);

    const code = verificationCode.value;
    const email = localStorage.getItem("userEmail");
    if (!(validateNumInputs(code))) {
        enableBtn(resetPasswordContinueBtn);
        return;
    }

    try {
        const codeCompareResponse = await fetch("password-reset/verify", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email: email, code: code})
        });

        if (!codeCompareResponse.ok) {
            const error = await codeCompareResponse.json();
            showMessage("error", error.message);
            enableBtn(resetPasswordContinueBtn);
            return;
        }

        disableBtn(resetPasswordContinueBtn)
        localStorage.setItem("code", code);

        // update ui and proceed to next step
        currentStep = STEPS.NEW_PASSWORD;
        verificationCode.classList.add('hidden');
        newPassword.classList.remove('hidden');
        confirmNewPassword.classList.remove('hidden');
        showMessage("success", "Verification successful. You have 10 minutes to reset your password.");

        // re-enable btn
        enableBtn(resetPasswordContinueBtn);
    } catch (err) {
        showMessage("Something went wrong. Please try again.");
    }
}


async function handleNewPasswordStep() {
    disableBtn(resetPasswordContinueBtn);

    const password = newPassword.value;
    const confirmPassword = confirmNewPassword.value;

    // check if input is right
    if (!handleValidation("PASSWORD", password, "password")){
        enableBtn(resetPasswordContinueBtn);
        return;
    }
    // check if input is right
    if (!handleValidation("PASSWORD", confirmPassword, "password")) {
        enableBtn(resetPasswordContinueBtn);
        return;
    }

    // check if pass strength passes
    const passwordStrengthStatus = validatePasswordStrength(password);
    if (passwordStrengthStatus !== "VALID") {
        enableBtn(resetPasswordContinueBtn);
        showMessage("error", `${passwordStrengthStatus}`);
        return;
    }

    if (!(password === confirmPassword)) {
        enableBtn(resetPasswordContinueBtn);
        showMessage("error", "Passwords do not match. Please double check.");
        return;
    }

    try {
        const email = localStorage.getItem("userEmail");
        const code = localStorage.getItem("code");

        const changePasswordResponse = await fetch("password-reset/change", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email: email, code: code, password: password})
        });

        if (!changePasswordResponse.ok) {
            const error = await changePasswordResponse.json();
            showMessage("error", error.message);
            enableBtn(resetPasswordContinueBtn);
            return;
        }

        // re-enable btn
        enableBtn(resetPasswordContinueBtn);

        // save message to display later
        localStorage.setItem(
            "flashMessage",
            "Password successfully changed"
        );

        window.location.href = "/login";
    } catch (err) {
        showMessage("error", "Could not change password. Please try again.");
    }
}

