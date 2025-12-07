const emailInput = document.querySelector("#emailLogin");
const passwordInput = document.querySelector("#passwordLogin");
const inputFields = document.querySelectorAll(".Input");
const continueBtn = document.querySelector("#loginContinueBtn");

import {displayErrorMessage, validateForm} from "./validation.js";

let email, password, message;
continueBtn.addEventListener("click", (e) => {
        e.preventDefault();
        email = emailInput.value;
        password = passwordInput.value;

        if(validateForm(email, password)){
            handleLogin(email, password);
        } else {
            message = "Invalid characters used. Only letters, numbers, and !@#$%^&* are allowed."
            displayErrorMessage(true, message);
        }
    }
)

inputFields.forEach(input =>{
    input.addEventListener("keydown", (e)=>{
        if (e.key === "Enter") {
            e.preventDefault();
            continueBtn.click();
        }
    });
})



async function handleLogin(email, password){

    try {
        const res = await fetch(`/Login?email=${email}&password=${password}`, {method: "GET"});
        const result = await res.text();

        if(result === "SUCCESS"){
            // got to homepage if details are correct
            window.location.href = "/Home";
        } else {
            message = "Invalid credentials. Try again.";
            displayErrorMessage(true, message);
        }
    } catch (err){
        message = "Login failed, please try again later."
        displayErrorMessage(true, message);
    }
}