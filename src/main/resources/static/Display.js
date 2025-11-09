const dateDisplay = document.getElementById("dateDisplay");
const amountDisplay = document.getElementById("amount");
const goalDisplay = document.getElementById("goal");


// show today total amount drank and date
fetch("/today")
    .then(res => res.json())
    .then(data => {
        dateDisplay.innerHTML = data.date
        amountDisplay.innerHTML = "Water drank today:<br>" + data.amount;
    })

// show input for goal
fetch("/goal")
    .then(res=> res.json())
    .then(data => {
        goalDisplay.innerHTML = "Goal: <br>"  + data.amount;
    })


