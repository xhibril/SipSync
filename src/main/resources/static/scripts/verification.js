const userEmail = localStorage.getItem("userEmail");

import {showMessage} from "./validation.js";

let userId;
function setUserId(id){
    userId = id;
}
// if user clicks "resend token link"
document.addEventListener("DOMContentLoaded", () => {
    const resendToken = document.querySelector("#resendTokenLink");

    if (!resendToken) return;  // stops errors if element doesn’t exist

    resendToken.addEventListener("click", async (e) => {
        e.preventDefault();
        await getUserIdByEmail();
        await resendVerificationToken();
    });
});

// get user id by their email stored in storage
async function getUserIdByEmail(){
    try {
        const userIdResponse = await fetch (`/users/id?email=${encodeURIComponent(userEmail)}`, {method:"GET"})
        if(!userIdResponse.ok){
            throw new Error("Server returned an error.");
        }

        const userIdRes = await userIdResponse.json();
        console.log(userIdRes);
        setUserId(userIdRes);

    } catch (err){
        showMessage("error", "Could not resend verification token. Please try again.");
    }
}

// resend token
export async function resendVerificationToken(){
    console.log(userEmail);
    try {
        const resendToken = await fetch("/email/send-token",{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email: userEmail, id: userId})
        });

        if (!resendToken.ok){
            throw new Error("Server returned an error.");
        }

        showMessage("success", "Successfully sent. Please check your e-mail address.")

    } catch (err){
        showMessage("error", "Could not resend verification token. Please try again.");
    }
}


const params = new URLSearchParams(window.location.search);
const verified = params.get("verified");

if (verified === "false") {
    showMessage("error", "Your verification token is invalid or expired.");
} else if (verified === "true") {
    showMessage("success", "Email verified! You can now log in.");
}



