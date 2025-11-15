const inputType = document.querySelectorAll('input[name="inputType"]');
const input = document.querySelector(".input");

const submitBtn = document.querySelector("#submitBtn");

const viewPeriods = document.querySelectorAll('input[name="period"]');
// change button text based on input menu type
let choice = "ADD";
inputType.forEach(radio => {
    radio.addEventListener("change", () => {
        choice = radio.value;
        console.log(choice);

        switch (choice) {
            case "ADD": submitBtn.textContent = "ADD"; break;
            case "EDIT": submitBtn.textContent = "EDIT"; break;
            case "GOAL": submitBtn.textContent = "GOAL"; break;
        }
    });
});


// handler for inputs
submitBtn.addEventListener("click", () => {
    const amount = parseInt(input.value);
    if (isNaN(amount) || amount <= 0) return;
    input.value = "";
    handleSubmit(amount);
});


input.addEventListener("keydown", (e) => {
    if (e.key === "Enter") {
        submitBtn.click();
        input.value = "";
    }
});



// handles user inputs
function handleSubmit(amount) {

    switch (choice) {

        case "ADD":
            fetch(`/add?add=${amount}`, {method: "POST"})
                .then(e  => {
                    console.log("Saved successfully!");
                    refreshPageContents(view);
                })
                .catch(err => console.error("Error:", err));
            break;

        case "GOAL":
            fetch(`/add/goal?goal=${amount}`, {method: "POST"})
                .then(e  => {
                    console.log("Saved successfully!");
                    refreshPageContents(view);
                })
                .catch(err => console.error("Error:", err));
            break;


        case "EDIT":
            fetch(`add/edit?value=${amount}`, {method: "POST"})
                .then(e  => {
                    console.log("Edited successfully!");
                    refreshPageContents(view);
                })
                .catch (err => console.error("Error:", err));
            break;
    }
}




// handler for view period changes
let view = "DAILY";
viewPeriods.forEach(period => {
    period.addEventListener("change", function (){
        view = period.value;
        console.log(view);
        switch(view){
            case "DAILY": dailyFrontPageContents(); break;
            case "WEEKLY": weeklyFrontPageContents(); break;
            case "MONTHLY": monthlyFrontPageContents(); break;
        }
    })

})




// refresh page contents based on what menu users was in
function refreshPageContents(view){
    switch(view){
        case "DAILY": dailyFrontPageContents(); break;
        case "WEEKLY": weeklyFrontPageContents(); break;
        case "MONTHLY": monthlyFrontPageContents(); break;
    }
}













