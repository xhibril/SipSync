import {showMessage, handleValidation, validatePasswordStrength, validateEmailDomain} from "./validation.js";
import {resendVerificationToken} from "./verification.js";
import {disableBtn, enableBtn, showOverlay} from "./button-and-overlay.js";

const loginContainer = document.querySelector("#login-input-container");
const emailLogin = document.querySelector("#emailLogin");
const passwordLogin = document.querySelector("#passwordLogin");
const inputFields = document.querySelectorAll(".Input");
const loginContinueBtn = document.querySelector("#loginContinueBtn");

// forgot password
const forgotPassword = document.querySelector("#forgotPassword");
const forgotPasswordContainer = document.querySelector("#forgot-password-container");

// sing up link
const signUpLink = document.querySelector("#signup-link");


// show any flash messages (eg after user changes password and is redirected to login)
document.addEventListener("DOMContentLoaded", () => {
    const flash = localStorage.getItem("flashMessage");
    if (flash) {
        showMessage("success", flash);
        localStorage.removeItem("flashMessage");
    }
});

console.log(signUpLink);

// go to sign up page
signUpLink.addEventListener("click", (e)=>{
    e.preventDefault();
    window.location.href = "/signup";
})


// switch to forgot password ui
forgotPassword.addEventListener("click", (e)=>{
    e.preventDefault();
    forgotPasswordContainer.classList.remove('hidden');
    loginContainer.classList.add('hidden');
});


loginContinueBtn.addEventListener("click", (e) => {
    e.preventDefault();
    handleLoginClick();
});

function handleLoginClick(){
    const email = emailLogin.value;
    const password = passwordLogin.value;

    // check input validation
    if (!(handleValidation("EMAIL", email, "email"))) return;
    if (!(handleValidation("PASSWORD", password, "password"))) return;

    // check email domain validation
    const emailDomainValidationStatus = validateEmailDomain(email);

    // if password meets all requirements continue, if not give error msg
    if (emailDomainValidationStatus !== "VALID") {
        showMessage("error", `${emailDomainValidationStatus}`);
        return;
    }


    // temp disable btn so user cant spam and show overlay
    disableBtn(loginContinueBtn);
    showOverlay(true);
    handleLogin(email, password);
}




inputFields.forEach(input =>{
    input.addEventListener("keydown", (e)=>{
        if (e.key === "Enter") {
            e.preventDefault();
            loginContinueBtn.click();
        }
    });
})


async function handleLogin(email, password){
    const rememberMe = document.querySelector("#remember-me").checked;

    try {
        const loginResponse = await fetch ("/api/login", {
            method: "POST",
            headers: {"Content-Type": "application/json" },
            body: JSON.stringify({email, password, rememberMe})
        });

        if(!loginResponse.ok){
            throw new Error("Server returned an error.");
        }
            const areCredentialsValid = await loginResponse.json();

            if(areCredentialsValid){

                const isUserVerifiedResponse = await fetch(`/api/verification-status?email=${encodeURIComponent(email)}`);
                const isUserVerifiedRes = await isUserVerifiedResponse.json();

               if(isUserVerifiedRes){

                   // if user is verified go to homepage

                    // re-enable btn and disable overlay
                    enableBtn(loginContinueBtn);
                    showOverlay(false);

                    window.location.href = "/home";

                    // delete user email if verified from storage
                    localStorage.removeItem("userEmail");
                } else {

                   // if user is not verified ask them to verify
                    localStorage.setItem("userEmail", email);

                    // re-enable btn
                   loginContinueBtn.disabled = false;
                    window.location.href = "/verify"
                    await resendVerificationToken();
                }
            } else {
                // re-enable btn and disable overlay
                enableBtn(loginContinueBtn);
                showOverlay(false);
                showMessage("error", "Invalid credentials, try again.");
            }

    } catch (err){
        // re-enable btn and disable overlay
        enableBtn(loginContinueBtn);
        showOverlay(false);
        showMessage("error", "Could not log in. Please try again later.");
    }
}







