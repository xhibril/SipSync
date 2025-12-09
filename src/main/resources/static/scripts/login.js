const emailLogin = document.querySelector("#emailLogin");
const passwordLogin = document.querySelector("#passwordLogin");
const inputFields = document.querySelectorAll(".Input");
const loginContinueBtn = document.querySelector("#loginContinueBtn");

import {showMessage, handleValidation} from "./validation.js";

let email, password;
loginContinueBtn.addEventListener("click", (e) => {
        e.preventDefault();
        email = emailLogin.value;
        password = passwordLogin.value;

        if(!(handleValidation("EMAIL", email, "email"))) return;
        if(!(handleValidation("PASSWORD", password, "password"))) return;
        handleLogin(email, password);
    }
)

inputFields.forEach(input =>{
    input.addEventListener("keydown", (e)=>{
        if (e.key === "Enter") {
            e.preventDefault();
            loginContinueBtn.click();
        }
    });
})


async function handleLogin(email, password){
    try {
        const loginResponse = await fetch ("/api/login", {
            method: "POST",
            headers: {"Content-Type": "application/json" },
            body: JSON.stringify({email, password})
        });

        if(!loginResponse.ok){
            throw new Error("Server returned an error.");
        }
            const result = await loginResponse.text();
            if(result === "SUCCESS"){
                // got to homepage if details are correct
                window.location.href = "/Home";
            } else {
                showMessage("error", "Invalid credentials, try again.");
            }
    } catch (err){
        showMessage("error", "Could not log in. Please try again later.");
    }
}