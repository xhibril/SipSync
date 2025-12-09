const confirmBtnFeedback = document.querySelector("#feedbackConfirmBtn");
const nameFeedback = document.querySelector("#nameFeedback");
const emailFeedback = document.querySelector("#emailFeedback");
const messageFeedback = document.querySelector("#messageFeedback");

import {handleValidation, showMessage} from "./validation.js";

confirmBtnFeedback.addEventListener("click", async () => {
   handleSubmitFeedback();
});


async function handleSubmitFeedback(){
    let name = nameFeedback.value;
    let email = emailFeedback.value;
    let message = messageFeedback.value;


    if(!(handleValidation("NAME", name, "name"))) return;
    if(!(handleValidation("EMAIL", email, "email"))) return;
    if(!(handleValidation("MESSAGE", message, "message"))) return;

    try {
        const feedbackResponse = await fetch("/post/feedback",{
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, email, message })
        });

        if (!feedbackResponse.ok){
            throw new Error("Server returned an error.");
        }
            showMessage("success", "Feedback sent! Thank you.");
            nameFeedback.value = "";
            emailFeedback.value = "";
            messageFeedback.value = "";

    } catch (err){
        showMessage("error", "Could not send your feedback. Please try again.");
    }
}

