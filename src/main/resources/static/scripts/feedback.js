const confirmBtnFeedback = document.querySelector("#feedbackConfirmBtn");
const nameFeedback = document.querySelector("#nameFeedback");
const emailFeedback = document.querySelector("#emailFeedback");
const messageFeedback = document.querySelector("#messageFeedback");

import {showMessage} from "./validation.js";

confirmBtnFeedback.addEventListener("click", async () => {
   handleSubmitFeedback();
});


async function handleSubmitFeedback(){
    let name = nameFeedback.value;
    let email = emailFeedback.value;
    let message = messageFeedback.value;

    try {
        const feedbackResponse = await fetch("/post/feedback",{
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, email, message })
        });

        if (feedbackResponse.ok){

            showMessage("success", "Feedback sent! Thank you.");

            nameFeedback.value = "";
            emailFeedback.value = "";
            messageFeedback.value = "";
        } else {
            showMessage("error", "Oops! Something went wrong, please try again.");
        }

    } catch (err){
        showMessage("error", "Oops! Something went wrong, please try again.");
    }
}

