const select = document.getElementById("inputTypeSelection");
const input = document.getElementById("input");
const submitBtn = document.getElementById("submitBtn");

let choice = "ADD";
select.addEventListener("change", function () {
    choice = select.value;

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


function callDB(amount) {

    switch (choice) {
        case "ADD":

            fetch(`/add?add=${amount}`, {method: "POST"})
                .then(() => console.log("Saved successfully!"))
                .catch(err => console.error("Error:", err));

            break;

        case "GOAL":
            fetch(`/add/goal?goal=${amount}`, {method: "POST"})
                .then(() => console.log("Saved successfully!"))
                .catch(err => console.error("Error:", err));

            break;

    }
}















