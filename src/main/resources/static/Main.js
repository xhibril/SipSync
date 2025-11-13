const inputType = document.getElementById("inputTypeSelection");
const input = document.getElementById("input");
const submitBtn = document.getElementById("submitBtn");
const viewPeriods = document.querySelector("#periodSelection");

let choice = "ADD";

inputType.addEventListener("change", function () {
    choice = inputType.value;

    switch (choice) {
        case "ADD":
            submitBtn.textContent = "ADD";
            break;

        case "EDIT":
            submitBtn.textContent = "EDIT";
            break;

        case "GOAL":
            submitBtn.textContent = "GOAL";
            break;
    }
});


submitBtn.addEventListener("click", () => {

    const amount = parseInt(input.value);
    if (isNaN(amount) || amount <= 0) return;
    input.value = "";
    callDB(amount);

});


input.addEventListener("keydown", (e) => {
    if (e.key === "Enter") {
        submitBtn.click();
        input.value = "";
    }
});


let view = "DAILY";

viewPeriods.addEventListener("change", function (){

    view = viewPeriods.value;

    switch(view){
        case "DAILY":
            dailyFrontPageContents();
            break;


        case "WEEKLY":
            weeklyFrontPageContents();
            break;


        case "MONTHLY":
            monthlyFrontPageContents();
            break;

    }


})














function callDB(amount) {

    switch (choice) {
        case "ADD":

            fetch(`/add?add=${amount}`, {method: "POST"})
                .then(e  => {
            console.log("Saved successfully!");
                    dailyFrontPageContents();
            })
                .catch(err => console.error("Error:", err));

            break;

        case "GOAL":
            fetch(`/add/goal?goal=${amount}`, {method: "POST"})
                .then(e  => {
                    dailyFrontPageContents();
                })
                .catch(err => console.error("Error:", err));

            break;


        case "EDIT":
            fetch(`add/edit?value=${amount}`, {method: "POST"})

                .then(e => dailyFrontPageContents())
                .catch (err => console.error("Error:", err));

            break;
    }
}

















