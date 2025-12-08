const continueBtn = document.querySelector("#signUpContinueBtn")
const emailInput = document.querySelector("#emailSignUp");
const passwordInput = document.querySelector("#passwordSignUp");
const inputs = document.querySelectorAll(".input");

import {showMessage, validateForm} from "./validation.js";

let email, password;
continueBtn.addEventListener("click", (e) => {
        e.preventDefault();
        email = emailInput.value;
        password = passwordInput.value;

        if (validateForm(email, password)) {
            addUser(email, password);
        } else {
            showMessage(true, "Invalid characters used. Only letters, numbers, and !@#$%^&* are allowed.");
        }
    }
)

inputs.forEach(input => {
    input.addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
            event.preventDefault();
            continueBtn.click();
        }
    });
});


async function addUser(email, password) {
    try {
        const response = await fetch(`/Signup?email=${email}&password=${password}`, {method: "POST"});

        // nav to homepage
        if (response.ok) {
            window.location.href = "/Home";
        }
        // send user email to verify
        const data = await response.json();
        fetch(`/sendVerificationEmail?email=${data.email}&token=${data.token}`, {method: "POST"});

    } catch (err) {
        showMessage(true, "Failed to sign you up, please try again later.");
    }
}