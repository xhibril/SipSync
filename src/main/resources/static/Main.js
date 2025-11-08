// show water drank for today, goal and date
fetch("/today")
    .then(res => res.json())
    .then(data => {
        console.log(data);
        document.getElementById("amount").innerHTML = "Water drank: <br> " + data.amount + " ml";
        document.getElementById("dateDisplay").innerHTML = "Date: " + data.date;
    });


// update input type selection menu
const select = document.getElementById("inputTypeSelection");
const submitBtn = document.getElementById("submitBtn");
select.addEventListener("change", function() {

    const choice = select.value;
    console.log("changed:", select.value);

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



submitBtn.addEventListener("click", function() {

    const added = parseFloat(input.value);
    if (isNaN(added) || added <= 0) return;
    drank += added;

    const percent = (drank / goal) * 100;

    bottle.style.background = `linear-gradient(to top, #e2eafc ${percent}%, white ${percent}%)`
});

