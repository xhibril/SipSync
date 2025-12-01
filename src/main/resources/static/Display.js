console.log("test");
const dateDisplay = document.querySelector("#dateDisplay");
const amountDisplay = document.querySelector("#amount");
const goalDisplay = document.querySelector("#goal");
const displayRemaining = document.querySelector("#displayRemaining");
const displayStreak = document.querySelector("#displayStreak");

let amountDrank = 0;
let goal = 0;


refreshMainPageContent();

export async function refreshMainPageContent(){
    const todayRes = await fetch("/today").then(r => r.json())
    const goalRes = await fetch("/goal").then(r => r.json())

    dateDisplay.innerHTML = todayRes.date;
    amountDisplay.innerHTML = todayRes.amount + " mL";

    amountDrank = todayRes.amount;
    goalDisplay.innerHTML = goalRes + " mL";

    goal = goalRes;

    progressBar(amountDrank, goal);


    let rem = remaining(amountDrank, goal);
    displayRemaining.innerHTML = rem;

    if(rem == 0 && goal != 0){
        incrementStreak();
    }

}


function progressBar(amountDrank, goal){
    let percent = Math.ceil((amountDrank * 100) / goal);
    if (percent > 100) percent = 100;
    if (percent < 0) percent = 0;

    document.querySelector(".progressBar").style.backgroundSize = `${percent}% 100%, 100% 100%`;
}


function remaining(amountDrank, goal){
    if(amountDrank >= goal) return 0;

    return (goal - amountDrank);
}


async function incrementStreak(streak) {


        const res = await fetch(`/increment/streak`, {method: "POST"}).then(r => r.json());
        console.log("Streak Saved Successfully");
        console.log(res);
        displayStreak.innerHTML = res;


}





