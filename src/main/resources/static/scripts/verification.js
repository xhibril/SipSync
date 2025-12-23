import {showMessage} from "./notification.js";
import {isBeingRateLimited} from "./http-responses.js";
const userEmail = localStorage.getItem("userEmail");


// if user clicks "resend token link"
document.addEventListener("DOMContentLoaded", () => {
    const resendToken = document.querySelector("#resend-token-link");
    if (!resendToken) return;  // stops errors if element doesn’t exist

    resendToken.addEventListener("click", async (e) => {
        e.preventDefault();
        await resendVerificationToken();
    });
});

// resend token
export async function resendVerificationToken(){
    try {
        const resendTokenResponse = await fetch("/email/send-token",{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email: userEmail})
        });

        if (!resendTokenResponse.ok){
            if(isBeingRateLimited(resendTokenResponse)) return;
            throw new Error();
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