const totalFacts = 20;
let fact = Math.floor(Math.random() * totalFacts) + 1;

let textToDisplay = document.getElementById(fact.toString());
textToDisplay.classList.add("show");

setInterval(() => {
    let newFact;

    // ensure new fact != previous
    do {
        newFact = Math.floor(Math.random() * totalFacts) + 1;
    } while(newFact === fact);

    // hide previous
    textToDisplay.classList.remove("show");


    textToDisplay = document.getElementById(fact.toString());
    textToDisplay.classList.add("show");

    fact = newFact
}, 5000);


