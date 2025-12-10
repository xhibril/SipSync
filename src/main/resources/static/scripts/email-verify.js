const userEmail = localStorage.getItem("userEmail");
const resendToken = document.querySelector("#resendTokenLink");

import {showMessage} from "./validation.js";

let userId;
function setUserId(id){
    userId = id;
}

resendToken.addEventListener("click", async (e) => {

    e.preventDefault();
    await getUserIdByEmail();
    await resendVerificationToken();

})


async function getUserIdByEmail(){
    try {
        const userIdResponse = await fetch (`/users/id?email=${userEmail}`, {method:"GET"})
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

async function resendVerificationToken(){
    console.log("email:" + userEmail + "id: " + userId);
    try {
        const resendToken = await fetch("/email/send-token",{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email: userEmail, id: userId})
        });

        if (!resendToken.ok){
            throw new Error("Server returned an error.");
        }

    } catch (err){
        showMessage("error", "Could not resend verification token. Please try again.");
    }
}

