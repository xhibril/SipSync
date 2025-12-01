const unitType = document.querySelectorAll('input[name="unitType"]');
const ageInput = document.querySelector("#calcAgeInput");
const genderInput = document.querySelectorAll('input[name="inputGender"]');
const weightInput = document.querySelector("#weightInput");
const activityInput = document.querySelector("#activityInput");
const displayWaterIntake = document.querySelector("#waterIntake");
const calcConfirmBtn = document.querySelector("#calcSubmitBtn");

// Water (L/day)=(Weight (kg)×0.033×Age/Gender Factor)+(Exercise min×0.012)+Climate/Diet Adjustment

// change placeholders for inputs based on unity type
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

let gender = "NONE";
genderInput.forEach(radio =>{
    radio.addEventListener("change", ()=>{
        gender = radio.value;

        switch(gender){
            case "MALE":
                gender = "MALE"; break;
            case "FEMALE":
                gender = "FEMALE"; break;
        }
    });
});




calcConfirmBtn.addEventListener("click", ()=>{

    inputValues(unitTypeValue);
})





function inputValues(unitTypeValue){

    if (
        !weightInput.value ||
        !ageInput.value ||
        !gender ||
        !activityInput.value
    ) {
        alert("Please fill in all fields before submitting!");
        return; // stop the function
    }

let weightFactor = 0.0;
   if(unitTypeValue === "IMPERIAL"){
        weightFactor = Number(weightInput.value) / 2.20462;
   } else {
        weightFactor = Number(weightInput.value);
   }


    let ageFactor = Number(ageInput.value);
    let genderFactor = gender;
    let activityFactor = activityInput.value;

    genderFactor = parseFloat(getGenderFactorValue(genderFactor));
    ageFactor = parseFloat(getAgeFactorValue(ageFactor));
    activityFactor = (getActivityFactorValue(activityFactor));

    let waterIntake = calculate(weightFactor, ageFactor, genderFactor, activityFactor);
    let roundedWaterIntake = Number(waterIntake.toFixed(2));
    console.log(waterIntake);

    displayWaterIntake.style.visibility = "visible";
    displayWaterIntake.innerHTML = "Your recommended water intake: " + roundedWaterIntake + " L";
}





function calculate(weightFactor, ageFactor, genderFactor, activityFactor){
    return (weightFactor * 0.033 * ageFactor * genderFactor * activityFactor);
}


function getGenderFactorValue(gender){
    switch(gender){
        case "MALE":
            return 1;
        case "FEMALE":
            return 0.95;
    }
}

function getAgeFactorValue(age){

    if(age >= 14 && age <= 30) return 1;
    if(age >= 31 && age <= 50) return 0.98;
    if(age >= 51 && age <= 65) return 0.95;
    if(age >= 66) return 0.90;
    return 1;


}

function getActivityFactorValue(activityFactor){

    switch(activityFactor){
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
