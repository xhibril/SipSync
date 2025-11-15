const dateDisplay = document.querySelector("#dateDisplay");
const amountDisplay = document.querySelector("#amount");
const goalDisplay = document.querySelector("#goal");
const averageAmount = document.querySelector("#averageAmount");

let amountDrank = 0;
let goal = 0;

dailyFrontPageContents();

async function dailyFrontPageContents() {
    const todayRes = await fetch("/today").then(r => r.json())
    const goalRes = await  fetch("/goal").then(r => r.json())

    // display daily water intake and date
    dateDisplay.innerHTML = todayRes.date;
    amountDisplay.innerHTML = "Today's Hydration:<br>" + todayRes.amount + " ML";
    averageAmount.hidden = true;

    // display goal
    goalDisplay.innerHTML = "Goal: <br>" + goalRes.amount + " ML";

    amountDrank = todayRes.amount;
    goal = goalRes.amount;
    bottleFilling(goal, amountDrank);

}


async function weeklyFrontPageContents() {
    // show total amount drank past 7 days, average per day and date
    const weeklyRes = await fetch("/weekly").then(r => r.json())
    const goalRes = await  fetch("/goal").then(r => r.json())

    dateDisplay.innerHTML = weeklyRes.date;
    amountDisplay.innerHTML = "Weekly Water Intake:<br>" + weeklyRes.amount + " ML";
    averageAmount.hidden = false;
    averageAmount.innerHTML = "Average Per Day: <br>" + Math.ceil(weeklyRes.amount / weeklyRes.count) + " ML";

    // display goal
    goalDisplay.innerHTML = "Goal: <br>" + goalRes.amount + " ML";

    // pass avg amount of water drank daily past week
    amountDrank = Math.ceil(weeklyRes.amount / weeklyRes.count);

    amountDrank = weeklyRes.amount / weeklyRes.count;
    goal = goalRes.amount;
    bottleFilling(goal, amountDrank)
}


async function monthlyFrontPageContents(){
    // show total amount drank past 30 days, average per day and date
    const monthlyRes = await fetch("/monthly").then(r => r.json())
    const goalRes = await  fetch("/goal").then(r => r.json())

    dateDisplay.innerHTML = monthlyRes.date;
    amountDisplay.innerHTML = "Monthly Water Intake:<br>" + monthlyRes.amount + " ML";
    averageAmount.hidden = false;
    averageAmount.innerHTML = "Average Per Day: <br>" + Math.ceil(monthlyRes.amount / monthlyRes.count) + " ML";

    // display goal
    goalDisplay.innerHTML = "Goal: <br>" + goalRes.amount + " ML";

    // pass avg amount of water drank daily past month
    amountDrank = Math.ceil(monthlyRes.amount / monthlyRes.count);

    amountDrank = monthlyRes.amount / monthlyRes.count;
    goal = goalRes.amount;
    bottleFilling(goal, amountDrank)
}



function bottleFilling(goal, amountDrank){

let percent = Math.ceil((amountDrank * 100) / goal);

if(percent > 100) percent = 100;
if(percent < 0) percent = 0;

document.querySelector(".bottle").style.backgroundSize = `100% ${percent}%`;
}

