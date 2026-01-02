import {handleValidation, validateEmailDomain} from "./validation.js";
import {resendVerificationToken} from "./verification.js";
import {unlockBtn, lockBtn} from "./button-state.js";
import {showMessage} from "./notification.js";
import {isBeingRateLimited} from "./http-responses.js";

const inputFields = document.querySelectorAll(".Input");
const loginContainer = document.querySelector("#login-container");
const emailLogin = document.querySelector("#login-email");
const passwordLogin = document.querySelector("#login-password");
const loginBtn = document.querySelector("#login-confirm");
const signUpLink = document.querySelector("#signup-link");
const togglePassword = document.querySelector("#toggle-login-password");

// forgot password
const forgotPassword = document.querySelector("#forgot-password");
const forgotPasswordContainer = document.querySelector("#forgot-password-container");

// show any flash messages (eg after user changes password and is redirected to login)
document.addEventListener("DOMContentLoaded", () => {
    const flash = localStorage.getItem("flashMessage");
    if (flash) {
        showMessage("success", flash);
        localStorage.removeItem("flashMessage");
    }
});


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


loginBtn.addEventListener("click", (e) => {
    e.preventDefault();
    handleLoginClick();
});

togglePassword.addEventListener("click", ()=>{
    passwordLogin.type =
        passwordLogin.type === "password" ? "text" : "password";
})

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

    // temp disable btn
    lockBtn(loginBtn, "Logging in...");
    handleLogin(email, password);
}


inputFields.forEach(input =>{
    input.addEventListener("keydown", (e)=>{
        if (e.key === "Enter") {
            e.preventDefault();
            loginBtn.click();
        }
    });
})


async function handleLogin(email, password){
    const rememberMe = document.querySelector("#remember-me").checked;
    try {
        const loginResponse = await fetch ("/api/login", {
            method: "POST",
            headers: {"Content-Type": "application/json" },
            body: JSON.stringify({email: email, password: password, rememberMe: rememberMe})
        });

        if(!loginResponse.ok){
            if(isBeingRateLimited(loginResponse)) return;
            throw new Error();
        }

            const areCredentialsValid = await loginResponse.json();
            if(areCredentialsValid){
                const isUserVerifiedResponse = await fetch("/api/verification-status", {
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify({email})
                });

                if(!isUserVerifiedResponse.ok){
                    if(isBeingRateLimited(isUserVerifiedResponse)) return;
                    throw new Error();
                }

                const isUserVerifiedRes = await isUserVerifiedResponse.json();

                 if(isUserVerifiedRes){
                   // if user is verified go to homepage
                    window.location.href = "/home";

                    // delete user email if verified from storage
                    localStorage.removeItem("userEmail");
                } else {

                   // if user is not verified ask them to verify
                    localStorage.setItem("userEmail", email);
                    await resendVerificationToken();
                    window.location.href = "/verify"
                }
            } else {
                showMessage("error", "Invalid credentials, try again.");
            }
    } catch (err){
        console.log(err);
        showMessage("error", "Could not log in. Please try again later.");
    } finally {
        unlockBtn(loginBtn);
    }
}







