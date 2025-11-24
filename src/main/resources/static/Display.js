const dateDisplay = document.querySelector("#dateDisplay");
const amountDisplay = document.querySelector("#amount");
const goalDisplay = document.querySelector("#goal");
const averageAmount = document.querySelector("#averageAmount");
const celebrationContainer = document.querySelector(".celebrationContainer");

let amountDrank = 0;
let goal = 0;

dailyFrontPageContents();

async function dailyFrontPageContents() {

    averageAmount.style.display = "none";



    const todayRes = await fetch("/today").then(r => r.json())
    const goalRes = await fetch("/goal").then(r => r.json())

    // display daily water intake and date
    dateDisplay.innerHTML = todayRes.date + " | " + todayRes.day;
    amountDisplay.innerHTML = "Today's Hydration:<br>" + todayRes.amount + " ML";
    averageAmount.hidden = true;

    // display goal
    goalDisplay.innerHTML = "Goal: <br>" + goalRes.amount + " ML";


    amountDrank = todayRes.amount;
    goal = goalRes.amount;

    goalReached(amountDrank, goal);
    bottleFilling(goal, amountDrank);


}


async function weeklyFrontPageContents() {
    averageAmount.style.display = "flex";
    // show total amount drank past 7 days, average per day and date
    const weeklyRes = await fetch("/weekly").then(r => r.json())
    const goalRes = await fetch("/goal").then(r => r.json())

    dateDisplay.innerHTML = weeklyRes.date + " | " + weeklyRes.day;
    amountDisplay.innerHTML = "Weekly Water Intake:<br>" + weeklyRes.amount + " ML";
    averageAmount.hidden = false;

    avgAmountDrank = Math.ceil(weeklyRes.amount / weeklyRes.count);

    if(isAvgEmpty(avgAmountDrank))
        avgAmountDrank = 0;

    averageAmount.innerHTML = "Average Per Day: <br>" + avgAmountDrank + " ML";
    // display goal
    goalDisplay.innerHTML = "Goal: <br>" + goalRes.amount + " ML";

    goal = goalRes.amount;
    bottleFilling(goal, avgAmountDrank)
    goalReached(amountDrank, goal);
}


async function monthlyFrontPageContents(){
    averageAmount.style.display = "flex";
    // show total amount drank past 30 days, average per day and date
    const monthlyRes = await fetch("/monthly").then(r => r.json())
    const goalRes = await fetch("/goal").then(r => r.json())



    dateDisplay.innerHTML = monthlyRes.date + " | " + monthlyRes.day;
    amountDisplay.innerHTML = "Monthly Water Intake: <br>" + monthlyRes.amount + " ML";
    averageAmount.hidden = false;

    avgAmountDrank = Math.ceil(monthlyRes.amount / monthlyRes.count);

    if(isAvgEmpty(avgAmountDrank))
        avgAmountDrank = 0;

    averageAmount.innerHTML = "Average Per Day: <br>" + avgAmountDrank + " ML";

    // display goal
    goalDisplay.innerHTML = "Goal: <br>" + goalRes.amount + " ML";



    goal = goalRes.amount;
    bottleFilling(goal, avgAmountDrank)
    goalReached(amountDrank, goal);
}


function bottleFilling(goal, amountDrank) {
    let percent = Math.ceil((amountDrank * 100) / goal);
    if (percent > 100) percent = 100;
    if (percent < 0) percent = 0;

    document.querySelector(".bottle").style.backgroundSize = `100% ${percent}%`;
}




function goalReached(amountDrank, goal){

    if(amountDrank >= goal){
        celebrationContainer.style.display = "flex";


    } else {
        celebrationContainer.style.display = "none";

    }
}

// check if avg is not valid
function isAvgEmpty(avg){
 return !avg || isNaN(avg);
}

