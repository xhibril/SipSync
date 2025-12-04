
console.log("display");
const amountDisplay = document.querySelector("#amount");
const goalDisplay = document.querySelector("#goal");
const displayRemaining = document.querySelector("#displayRemaining");
const displayStreak = document.querySelector("#displayStreak");

let amountDrank = 0;
let goal = 0;


export async function refreshMainPageContent(timeRange){
    let res;
    console.log(timeRange);

    switch(timeRange){
        case "DAILY": res = await fetch("/today").then(r => r.json()); break;
        case "WEEKLY": res = await fetch("/weekly").then(r => r.json()); break;
        case "MONTHLY": res = await fetch("/monthly").then(r => r.json()); break;
    }
    const goalRes = await fetch("/goal").then(r => r.json())

    amountDisplay.innerHTML = res + " mL";

    amountDrank = res;
    goalDisplay.innerHTML = goalRes + " mL";

    goal = goalRes;

    progressBar(amountDrank, goal);



        let rem = remaining(amountDrank, goal);
        displayRemaining.innerHTML = rem;

        if(rem === 0 && goal !== 0){
            incrementStreak();
        }




}





export function progressBar(amountDrank, goal){
    if (goal <= 0) {
        document.querySelector(".progressBar").style.backgroundSize = `0% 100%, 100% 100%`;
        return;
    }

    let percent = Math.ceil((amountDrank * 100) / goal);
    if (percent > 100) percent = 100;
    if (percent < 0) percent = 0;

    document.querySelector(".progressBar").style.backgroundSize = `${percent}% 100%, 100% 100%`;
}


function remaining(amountDrank, goal){
    if(amountDrank >= goal) return 0;

    return (goal - amountDrank);
}


async function incrementStreak() {



        const res = await fetch(`/increment/streak`, {method: "POST"}).then(r => r.json());
        console.log("Streak Saved Successfully");
        console.log(res);
        displayStreak.innerHTML = res;


}





