const input = document.getElementById("amount");
const bottle = document.getElementById("bottle");
const button = document.getElementById("addBtn");
const goalText = document.getElementById("goalText");
const drankText = document.getElementById("drankText");

let drank = 0;
let goal = 3000;
goalText.textContent = "Goal: " + goal + " ML";
drankText.innerHTML = "Drank today:<br> " + drank + " ML";




// fill the bottle based on input
button.addEventListener("click", function() {

    const added = parseFloat(input.value);
    if (isNaN(added) || added <= 0) return;
    drank += added;

    const percent = (drank / goal) * 100;

    bottle.style.background = `linear-gradient(to top, #e2eafc ${percent}%, white ${percent}%)`
});