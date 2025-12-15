const forgotPasswordContainer = document.querySelector("#forgot-password-container");
const emailReset = document.querySelector("#email-reset");
const verificationCode = document.querySelector("#verification-code");
const newPassword = document.querySelector("#new-password");
const confirmNewPassword = document.querySelector("#re-enter-password");
const resetPasswordContinueBtn = document.querySelector("#resetPasswordContinueBtn");


import {
    handleValidation,
    validatePasswordStrength,
    validateEmailDomain,
    showMessage,
    validateNumInputs
} from "./validation.js";

const STEPS = {
    EMAIL: 1,
    CODE: 2,
    NEW_PASSWORD: 3
};

let currentStep = STEPS.EMAIL;

resetPasswordContinueBtn.addEventListener("click", (e) => {
    console.log("test");
    e.preventDefault();

    if (currentStep === STEPS.EMAIL) {

        verificationCode.classList.add('hidden');
        newPassword.classList.add('hidden');
        confirmNewPassword.classList.add('hidden');
        handleEmailStep();


    }

    if (currentStep === STEPS.CODE) {

        emailReset.classList.add('hidden');
        verificationCode.classList.remove('hidden');

        handleCodeStep();
    }

    if (currentStep === STEPS.NEW_PASSWORD) {

        verificationCode.classList.add('hidden');
        newPassword.classList.remove('hidden');
        confirmNewPassword.classList.remove('hidden');

        handleNewPasswordStep();
    }
});

// sends email over to backend to gen verification code and save it
async function handleEmailStep() {
    const email = emailReset.value;

    if (!(handleValidation("EMAIL", email, "email"))) return;

    const emailDomainValidationStatus = validateEmailDomain(email);
    if (emailDomainValidationStatus !== "VALID") {
        showMessage("error", `${emailDomainValidationStatus}`);
        return;
    }

    try {
        const emailResetResponse = await fetch("/password-reset", {
        method: "POST",
        headers:{"Content-Type": "application/json"},
        body: JSON.stringify({email: email})
        });

        if (!emailResetResponse.ok) {
            throw new Error();
        }

        const codeGenerated = await emailResetResponse.json();

        if (codeGenerated) {
            // means code is generated and saved in db
            // proceed to next step
            currentStep = STEPS.CODE;

            // save user email in local storage to grab it later
            localStorage.setItem("userEmail", email);
        }
    } catch (err) {
        showMessage("error", "Could not send verification code. Please try again.");
    }
}


// sends code to compare
async function handleCodeStep() {
    const code = verificationCode.value;
    const email = localStorage.getItem("userEmail");

    if (!(validateNumInputs(code))) return;

    try {

        const codeCompareResponse = await fetch("password-reset/verify",{
            method: "POST",
            headers: {"Content-Type": "application/json",
            body: JSON.stringify({email: email, code: code})}
        });

        if(!codeCompareResponse){
            const error = await codeCompareResponse.json();
            showMessage("error", error.message + error.attemptsRemaining);
            return;
        }

        localStorage.setItem("code", code);
        currentStep = STEPS.NEW_PASSWORD;

    } catch (err){
        showMessage("Something went wrong. Please try again.");
    }
}



async function handleNewPasswordStep(){
    const password = newPassword.value;
    const confirmPassword = confirmNewPassword.value;

    // check if input is right
    if(!handleValidation("PASSWORD", password, "password")) return;

    // check if pass strength passes
    const passwordStrengthStatus = validatePasswordStrength(password);
    if(passwordStrengthStatus !== "VALID"){
        showMessage("error", `${passwordStrengthStatus}`);
        return;
    }

    if(!(password === confirmPassword)){
        showMessage("error", "Passwords do not match. Please double check.");
    }

    try {
        const email = localStorage.getItem("userEmail");
        const code = localStorage.getItem("code");

        const changePasswordResponse = await fetch("password-reset/change", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email: email, code: code, newPassword: password})
        });

        if(!changePasswordResponse.ok){
            const error = await changePasswordResponse.json();
            showMessage("error", error.message);
            return;
        }

        showMessage("success", "Password successfully changed.");
        window.location.href = "/login";


    } catch(err){
        showMessage("error", "Could not change password. Please try again.");
    }



}

