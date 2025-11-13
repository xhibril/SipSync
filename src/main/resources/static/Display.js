const dateDisplay = document.getElementById("dateDisplay");
const amountDisplay = document.getElementById("amount");
const goalDisplay = document.getElementById("goal");

dailyFrontPageContents();

function dailyFrontPageContents() {
// show today total amount drank and date
    fetch("/today")
        .then(res => res.json())
        .then(data => {
            dateDisplay.innerHTML = data.date;
            amountDisplay.innerHTML = "Water drank today:<br>" + data.amount;
        })

// show input for goal
    fetch("/goal")
        .then(res => res.json())
        .then(data => {
            goalDisplay.innerHTML = "Goal: <br>" + data.amount;
        })
}



function weeklyFrontPageContents(){
    // show total amount drank past 7 days

    fetch("/weekly")
        .then(res => res.json())
        .then(data => {
            dateDisplay.innerHTML = data.date;
            amountDisplay.innerHTML = "Water drank past Seven Days:<br>" + data.amount;
        })
}


function monthlyFrontPageContents(){
    // show total amount drank past 30 days

    fetch("/monthly")
        .then(res => res.json())
        .then(data => {
            dateDisplay.innerHTML = data.date;
            amountDisplay.innerHTML = "Water past month:<br>" + data.amount;
        })
}

