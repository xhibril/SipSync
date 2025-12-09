const emailSignUp = document.querySelector("#emailSignUp");
const passwordSignUp = document.querySelector("#passwordSignUp");
const inputs = document.querySelectorAll(".input");
const signUpContinueBtn = document.querySelector("#signUpContinueBtn");

import {showMessage, handleValidation} from "./validation.js";

let email, password;
signUpContinueBtn.addEventListener("click", (e) => {
        e.preventDefault();
        email = emailSignUp.value;
        password = passwordSignUp.value;
        if(!(handleValidation("EMAIL", email, "email"))) return;
        if(!(handleValidation("PASSWORD", password, "password"))) return;
        handleSignUp(email, password);
});

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
            // nav to homepage
            window.location.href = "/Home";

    } catch (err) {
        showMessage("error", "Could not sign you up. Please try again later.");
    }
}