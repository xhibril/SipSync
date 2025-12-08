const unitType = document.querySelectorAll('input[name="unitType"]');
const ageInput = document.querySelector("#calcAgeInput");
const weightInput = document.querySelector("#weightInput");
const activityInput = document.querySelector("#activityInput");
const displayWaterIntake = document.querySelector("#waterIntake");
const calculateConfirmBtn = document.querySelector("#calcSubmitBtn");

import {validateNumInputs} from "./validation.js";

// change placeholders for inputs based on unit type
let unitTypeValue = "IMPERIAL";
unitType.forEach(radio => {
    radio.addEventListener("change", ()=>{
        unitTypeValue = radio.value;

        switch(unitTypeValue){
            case "IMPERIAL":
                weightInput.placeholder = "Pounds";
                weightInput.value ="";

                break;
            case "METRIC":
                weightInput.placeholder = "Kg";
                weightInput.value ="";

                break;
        }
    });
});


let weight;
function weightValue(unitTypeValue){
    if(unitTypeValue === "IMPERIAL"){
        weight = Number(weightInput.value) / 2.20462;
    } else {
        weight = Number(weightInput.value);
    }
}

calculateConfirmBtn.addEventListener("click", ()=>{
    calculateWaterIntake(unitTypeValue);
});


function calculateWaterIntake(unitTypeValue){
    // get the gender selected
    const selectedGender = document.querySelector('input[name="inputGender"]:checked');
    let gender = selectedGender.value;

    // check weight based on unit type
    weightValue(unitTypeValue);

    let age = Number(ageInput.value);
    let activity = activityInput.value;

    // get multipliers for each
    gender = parseFloat(getGenderMultiplier(gender));
    age = parseFloat(getAgeMultiplier(age));
    activity = parseFloat(getActivityMultiplier(activity));

    // check if input is valid
    if(validateNumInputs(weight, age, gender, activity)){

        let waterIntake = calculate(weight, age, gender, activity);
        let roundedWaterIntake = Number(waterIntake.toFixed(2));

        displayWaterIntake.style.visibility = "visible";
        displayWaterIntake.innerHTML = "Your recommended water intake: " + roundedWaterIntake + " L";
    }
}


function calculate(weightFactor, ageFactor, genderFactor, activityFactor){
    return (weightFactor * 0.033 * ageFactor * genderFactor * activityFactor);
}

function getGenderMultiplier(gender){
    switch(gender){
        case "MALE":
            return 1;
        case "FEMALE":
            return 0.95;
    }
}

function getAgeMultiplier(age){

    if(age >= 14 && age <= 30) return 1;
    if(age >= 31 && age <= 50) return 0.98;
    if(age >= 51 && age <= 65) return 0.95;
    if(age >= 66) return 0.90;
    return 1;
}

function getActivityMultiplier(activity){

    switch(activity){
        case "NONE":
            return 1;
        case "LIGHT":
            return 1.1;
        case "MODERATE":
            return 1.2;
        case "HIGH":
            return 1.35;
        case "EXTREME":
            return 1.5;
    }
}

/*

// Water (L/day)=(Weight (kg)×0.033×Age/Gender Factor)+(Exercise min×0.012)+Climate/Diet Adjustment


// 2.20462

BASE:
base water = weight(kg) ×0.033

GENDER:
Male: * 1
Female: * 0.95

AGE:
14-30  = * 1
31-50 = * .98
51-65 = * .95
66+ =  * .90


ACTIVITY:

NONE: water = base water * 1
LIGHT: water = base water * 1.1
MODERATE: water = base water * 1.2
HIGH: water = base water * 1.35
EXTREME: water = base water * 1.5



 */
