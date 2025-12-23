import {handleValidation, validatePasswordStrength, validateEmailDomain} from "./validation.js";
import {lockBtn, unlockBtn} from "./button-state.js";
import {showMessage} from "./notification.js";
import {isBeingRateLimited} from "./http-responses.js";

const inputs = document.querySelectorAll(".input");
const emailSignUp = document.querySelector("#signup-email");
const passwordSignUp = document.querySelector("#signup-password");
const signUpBtn = document.querySelector("#signup-btn");
const loginLink = document.querySelector('#login-link');

loginLink.addEventListener("click", (e)=>{
    e.preventDefault();
    window.location.href = "/login";
})

signUpBtn.addEventListener("click", (e) => {
     handleSignUpClick(e);
});

inputs.forEach(input => {
    input.addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
            event.preventDefault();
            signUpBtn.click();
        }
    });
});

function handleSignUpClick(e){
    e.preventDefault();
   const email = emailSignUp.value;
   const password = passwordSignUp.value;

    // check input validation
    if(!(handleValidation("EMAIL", email, "email"))) return;
    if(!(handleValidation("PASSWORD", password, "password"))) return;

    // check email domain validation
    const emailDomainValidationStatus = validateEmailDomain(email);
    if (emailDomainValidationStatus !== "VALID") {
        showMessage("error", `${emailDomainValidationStatus}`);
        return;
    }

    // check if password meets all strength requirements
    const passwordStrengthStatus = validatePasswordStrength(password);
    if (passwordStrengthStatus !== "VALID") {
        showMessage("error", `${passwordStrengthStatus}`);
        return;
    }
    lockBtn(signUpBtn, "Signing up...");
    handleSignUp(email, password);
}


async function handleSignUp(email, password) {
    try {
        const signUpResponse = await fetch("/api/signup", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email, password})
        });

        if(!signUpResponse.ok){
            if(isBeingRateLimited(signUpResponse)) return;
            throw new Error("Server returned an error.");
        }

        const signUpRes = await signUpResponse.json();
        if(signUpRes){
            localStorage.setItem("userEmail", email);
            // nav to verify page after signing up
            window.location.href = "/verify";
        } else {
            showMessage("error", "This user already exists.");
        }
    } catch (err) {
        showMessage("error", "Could not sign you up. Please try again later.");
    } finally {
        unlockBtn(signUpBtn);
    }
}

