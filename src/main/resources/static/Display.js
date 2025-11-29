console.log("test");
const dateDisplay = document.querySelector("#dateDisplay");
const amountDisplay = document.querySelector("#amount");
const goalDisplay = document.querySelector("#goal");
const displayRemaining = document.querySelector("#displayRemaining");
const displayStreak = document.querySelector("#displayStreak");
let amountDrank = 0;
let goal = 0;

dailyFrontPageContents();

export async function dailyFrontPageContents() {



    const todayRes = await fetch("/today").then(r => r.json())
    const goalRes = await fetch("/goal").then(r => r.json())
    const streakRes = await fetch("/get/streak").then(r => r.json())

    // display daily water intake and date
    dateDisplay.innerHTML = todayRes.date;
    amountDisplay.innerHTML = todayRes.amount + " mL";

    // display goal
    goalDisplay.innerHTML = goalRes + " mL";



    // display streak
    displayStreak.innerHTML = streakRes;
    console.log(streakRes);

    amountDrank = todayRes.amount;
    goal = goalRes;




    displayRemaining.innerHTML = remaining(amountDrank, goal, streakRes);

    bottleFilling(goal, amountDrank);
}


export async function weeklyFrontPageContents() {
    // show total amount drank past 7 days, average per day and date
    const weeklyRes = await fetch("/weekly").then(r => r.json())
    const goalRes = await fetch("/goal").then(r => r.json())

    dateDisplay.innerHTML = weeklyRes.date + " | " + weeklyRes.day;
    amountDisplay.innerHTML = "Weekly Water Intake:<br>" + weeklyRes.amount + " ML";

    // display goal
    goalDisplay.innerHTML = "Goal: <br>" + goalRes.amount + " ML";

    goal = goalRes.amount;
    bottleFilling(goal, avgAmountDrank)
    remaining(amountDrank, goal);
}


export async function monthlyFrontPageContents(){
    // show total amount drank past 30 days, average per day and date
    const monthlyRes = await fetch("/monthly").then(r => r.json())
    const goalRes = await fetch("/goal").then(r => r.json())



    dateDisplay.innerHTML = monthlyRes.date + " | " + monthlyRes.day;
    amountDisplay.innerHTML = "Monthly Water Intake: <br>" + monthlyRes.amount + " ML";


    // display goal
    goalDisplay.innerHTML = "Goal: <br>" + goalRes.amount + " ML";



    goal = goalRes.amount;
    bottleFilling(goal, avgAmountDrank)
    remaining(amountDrank, goal);
}


function bottleFilling(goal, amountDrank) {
    let percent = Math.ceil((amountDrank * 100) / goal);
    if (percent > 100) percent = 100;
    if (percent < 0) percent = 0;

    document.querySelector(".progressBar").style.backgroundSize = `${percent}% 100%, 100% 100%`;
}


function remaining(amountDrank, goal, streak){
    if(amountDrank >= goal){
    incrementStreak(streak);
        return 0;
    }
    return (goal - amountDrank);
}


async function incrementStreak(streak) {

    streak++;
    await fetch(`/set/streak?streak=${streak}`, {method: "POST"})
        .then(e => {
            console.log("Streak Saved Successfully");
        })

        .catch(err => console.log(err));
}





