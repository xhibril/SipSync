import {redirectToLoginPage} from "./redirect.js";
import {handleValidation, showMessage} from "./validation.js";

const feedBackBtn = document.querySelector("#feedback-send");
const nameFeedback = document.querySelector("#feedback-name");
const emailFeedback = document.querySelector("#feedback-email");
const messageFeedback = document.querySelector("#feedback-message");


feedBackBtn.addEventListener("click", async () => {
   await handleSubmitFeedback();
});


async function handleSubmitFeedback(){
    let name = nameFeedback.value;
    let email = emailFeedback.value;
    let message = messageFeedback.value;

    // check if inputs r correct
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
            redirectToLoginPage(feedbackResponse);
            return;
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