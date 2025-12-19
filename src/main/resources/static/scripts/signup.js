import {showMessage, handleValidation, validatePasswordStrength, validateEmailDomain} from "./validation.js";
import {disableBtn, enableBtn, showOverlay} from "./button-state.js";

const emailSignUp = document.querySelector("#emailSignUp");
const passwordSignUp = document.querySelector("#passwordSignUp");
const inputs = document.querySelectorAll(".input");
const signUpContinueBtn = document.querySelector("#signUpContinueBtn");

const loginLink = document.querySelector('#login-link');

loginLink.addEventListener("click", (e)=>{
    e.preventDefault();
    window.location.href = "/login";
})


signUpContinueBtn.addEventListener("click", (e) => {
     handleSignUpClick(e);
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

    // if password meets all requirements continue, if not give error msg
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

    // temp disable btn so user cant spam and show overlay
    disableBtn(signUpContinueBtn);
    showOverlay(true);
    handleSignUp(email, password);
}






inputs.forEach(input => {
    input.addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
            event.preventDefault();
            signUpContinueBtn.click();
        }
    });
});


async function handleSignUp(email, password) {
    try {
        const signUpResponse = await fetch("/api/signup", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email, password})
        });

        if(!signUpResponse.ok){
            throw new Error("Server returned an error.");
        }

        const signUpRes = await signUpResponse.json();


        if(signUpRes){
            localStorage.setItem("userEmail", email);
            // nav to verify page after signing up


            // re-enable btn and disable overlay
            enableBtn(signUpContinueBtn);
            showOverlay(false);
            window.location.href = "/verify";

        } else {

            // re-enable btn and disable overlay
            enableBtn(signUpContinueBtn);
            showOverlay(false);
            showMessage("error", "This user already exists.");
        }
    } catch (err) {
        // re-enable btn and disable overlay
        enableBtn(signUpContinueBtn);
        showOverlay(false);

        showMessage("error", "Could not sign you up. Please try again later.");
    }
}

