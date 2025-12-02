const cards = document.querySelectorAll(".card");

const waterFacts = [
    "The human body is about 60% water.",
    "Adults need 2–3 liters of water daily.",
    "Losing just 2% of body water affects performance.",
    "Drinking water can slightly boost metabolism.",
    "Clear urine shows proper hydration.",
    "Dark urine indicates dehydration.",
    "Thirst often comes too late; you may already be dehydrated.",
    "Drink water before meals to feel fuller.",
    "Too much water can cause dangerous sodium loss.",
    "Exercise increases water needs, especially in heat.",
    "Caffeine in moderation doesn’t dehydrate much.",
    "Alcohol can dehydrate you.",
    "Infants and elderly are more prone to dehydration.",
    "Water helps regulate body temperature.",
    "Water supports kidney function and prevents stones.",
    "Hydration can improve skin health.",
    "Fruits and veggies provide 20–30% of your water.",
    "Sports drinks replace electrolytes during heavy exercise.",
    "Carbonated water hydrates as well as plain water.",
    "Hydration needs vary with climate and activity."
];

let index = 0;
const shuffled = [...waterFacts].sort(() => Math.random() - 0.5);

cards.forEach(card =>{
    card.addEventListener("click", ()=>{
        const para = card.querySelector("p");
        para.textContent = shuffled[index];
        card.style.boxShadow = "none";
        card.style.background = "white";
        para.style.color = "black";
        index++;
    } , { once: true });
})







